package com.example.eyecheckup_user.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class M_Client(
    @Exclude
    var docid: String? = null,

    var uid: String? = null,
    var fee: String? =null,

    @get:PropertyName("clinic_Name")
    @set:PropertyName("clinic_Name")
    var clinicName: String? = null,

    var doctorName: String? = null,

    var latitude: String? = null,

    var longitude: String? = null,

    var address: String? = null,

    @get:PropertyName("contact_Number")
    @set:PropertyName("contact_Number")
    var contactNumber: String? = null,

    @get:PropertyName("contact_Mail")
    @set:PropertyName("contact_Mail")
    var contactMail: String? = null,

    var rating: String? = null,

    var reviews: List<String?>? = null,

    @get:PropertyName("image_Url")
    @set:PropertyName("image_Url")
    var imageUrl: String? = null,

    var distance: Int? = null,
    var distanceUnit: String? = null,

    var experience : String? = null
)
