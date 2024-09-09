package com.example.eyecheckup_user.Components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions


@Composable
fun CameraPermission(
    modifier: Modifier = Modifier,
    distance: (Float) -> Unit = {}
) {
    val context = LocalContext.current
    val permission = Manifest.permission.CAMERA
    val hasPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    if (hasPermission.value) {
        CameraPreviewWithFaceDetection(modifier){
            distance(it)
        }
    } else {
        LaunchedEffect(Unit) {
            (context as ComponentActivity).requestPermissions(arrayOf(permission), 1001)
        }
        SideEffect {
            hasPermission.value = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}

@Composable
fun CameraPreviewWithFaceDetection(
    modifier: Modifier = Modifier,
    distance: (Float) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .build()

    val faceDetector = FaceDetection.getClient(options)
    val distance = remember { mutableStateOf(0f) }
    DisposableEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val imageAnalysis = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(context), { imageProxy ->
                    processImageProxy(
                        faceDetector,
                        imageProxy ,
                        ondistance = {
                            distance.value = it*100
                            distance((it*100))
                        })
                })
            }

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as ComponentActivity,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            faceDetector.close()
            cameraProvider.unbindAll()
        }
    }
    AndroidView(factory =  { previewView } , modifier = modifier)
}

@OptIn(ExperimentalGetImage::class)
fun processImageProxy(
    faceDetector: FaceDetector,
    imageProxy: ImageProxy,
    ondistance: (Float) -> Unit
) {
    val mediaImage = imageProxy.image ?: return
    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
    faceDetector.process(image)
        .addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                val face = faces.first()
                val distance = calculateDistance(face)
                ondistance(distance)
            }
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}

fun calculateDistance(face: Face): Float {
    // Assume an average face width of 14 cm (0.14 meters)
    val averageFaceWidthInMeters = 0.14f

    // Get the width of the face bounding box
    val boundingBoxWidthPixels = face.boundingBox.width().toFloat()

    // For simplicity, assume a known focal length (in pixels)
    // Focal length would ideally be obtained from camera calibration
    val focalLengthPixels = 500f // Adjust based on your camera calibration

    // Estimate the distance
    val distance = (averageFaceWidthInMeters * focalLengthPixels) / boundingBoxWidthPixels

    return distance
}
