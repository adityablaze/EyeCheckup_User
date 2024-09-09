package com.example.eyecheckup_user.naviation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eyecheckup_user.Screens.AmdScreens.AmdResultScreen
import com.example.eyecheckup_user.Screens.AmdScreens.AmdTestScreen
import com.example.eyecheckup_user.Screens.LocationScreen.LocationHelper
import com.example.eyecheckup_user.Screens.HomeScreen.HomeScreen
import com.example.eyecheckup_user.Screens.LoginScreen.LoginScreen
import com.example.eyecheckup_user.Screens.LoginScreen.PhoneAuthHelper
import com.example.eyecheckup_user.Screens.LoginScreen.SplashScreen
import com.example.eyecheckup_user.Screens.AmdScreens.AmdViewScreen
import com.example.eyecheckup_user.Screens.AppointmentScreen.AppointmentListScreen
import com.example.eyecheckup_user.Screens.AppointmentScreen.AppointmentScreen
import com.example.eyecheckup_user.Screens.AppointmentScreen.AppointmentStatusScreen
import com.example.eyecheckup_user.Screens.AstamatismScreen.AstagmatismViewScreen
import com.example.eyecheckup_user.Screens.ClientListScreen.ClientListScreen
import com.example.eyecheckup_user.Screens.DoctorDetailScreen.DoctorDetailScreen
import com.example.eyecheckup_user.Screens.LocationScreen.LocationScreen
import com.example.eyecheckup_user.Screens.VisionScreens.VisionResultScreen
import com.example.eyecheckup_user.Screens.VisionScreens.VisionTestScreen
import com.example.eyecheckup_user.Screens.VisionScreens.VisionViewScreen

@Composable
fun CheckUpNaviation(
    phoneAuthHelper: PhoneAuthHelper,
    locationHelper: LocationHelper,
    visionScore : List<Int?>,
    locationRequest: () -> Unit,
    onClick_Vision: (Int) -> Unit,
    onClear_Score : () -> Unit
) {
    val navController= rememberNavController()
     NavHost(navController = navController ,startDestination = CheckupScreens.HomeScreen.name)
    {
        composable(CheckupScreens.SplashScreen.name){
            SplashScreen(navController)
        }
        composable(
            CheckupScreens.HomeScreen.name){
            HomeScreen(navController , onClear_Score)
        }
        composable(CheckupScreens.LoginScreen.name){
            LoginScreen(phoneAuthHelper,navController)
        }
        composable(CheckupScreens.VisionViewScreen.name){
            VisionViewScreen(navController = navController , visionScore ,  onClear_Score = { onClear_Score.invoke()})
        }
        val VisionTestName = CheckupScreens.VisionTestScreen.name
        composable("$VisionTestName/{RandChar}",
            arguments= listOf(navArgument("RandChar") { type = NavType.StringType })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("RandChar").let {
                VisionTestScreen(
                    navController,
                    it.toString() ,
                    visionScore,
                    onClick_Vision = {score -> onClick_Vision(score) },
                    onClear_Score = {onClear_Score.invoke()}
                )
            }
        }
        composable(CheckupScreens.ClientListScreen.name) {
            ClientListScreen(
                locationHelper,
                navController,
                locationRequest = { locationRequest.invoke() }
            )
        }

        val VisionDetailName = CheckupScreens.DoctorDetailScreen.name
        composable("$VisionDetailName/{RandChar}",
            arguments= listOf(navArgument("RandChar") { type = NavType.StringType })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("RandChar").let {
                DoctorDetailScreen(
                    navController,
                    it.toString(),
                )
            }
        }

        val VisionAppointName = CheckupScreens.AppointmentScreen.name
        composable("$VisionAppointName/{RandChar}",
            arguments= listOf(navArgument("RandChar") { type = NavType.StringType })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("RandChar").let {
                AppointmentScreen(
                    navController,
                    it.toString(),
                )
            }
        }
        val VisionAppointStatusName = CheckupScreens.AppointmentStatusScreen.name
        composable("$VisionAppointStatusName/{RandChar}",
            arguments= listOf(navArgument("RandChar") { type = NavType.StringType })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("RandChar").let {
                AppointmentStatusScreen(
                    navController,
                    it.toString(),
                )
            }
        }

        composable(CheckupScreens.AmdViewScreen.name){
            AmdViewScreen(navController = navController)
        }

        composable(CheckupScreens.AmdTestScreen.name){
            AmdTestScreen(navController)
        }

        composable(CheckupScreens.VisionResultScreen.name){
            VisionResultScreen(navController = navController , visionScore)
        }

        composable(CheckupScreens.AppointmentListScreen.name){
            AppointmentListScreen(navController)
        }

        composable(CheckupScreens.AstagmatismViewScreen.name){
            AstagmatismViewScreen(navController)
        }

        composable(CheckupScreens.LocationScreen.name){
            LocationScreen(
                locationHelper,
                navController,
                locationRequest = { locationRequest.invoke() }
            )
        }

        val VisionAmdResultName = CheckupScreens.AmdResultScreen.name
        composable("$VisionAmdResultName/{RandChar}",
            arguments= listOf(navArgument("RandChar") { type = NavType.StringType })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("RandChar").let {
                AmdResultScreen(
                    navController,
                    it.toString(),
                )
            }
        }
    }
}