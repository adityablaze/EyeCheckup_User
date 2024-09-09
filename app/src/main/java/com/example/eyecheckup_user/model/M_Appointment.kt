package com.example.eyecheckup_user.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class M_Appointment(
    @Exclude
    var docid: String? = null,

    @get:PropertyName("User_id")
    @set:PropertyName("User_id")
    var Userid: String? = null,

    @get:PropertyName("Client_id")
    @set:PropertyName("Client_id")
    var Clientid: String? = null,

    @get:PropertyName("clinic_Name")
    @set:PropertyName("clinic_Name")
    var clinicName: String? = null,

    var doctorName: String? = null,

    @get:PropertyName("booking_id")
    @set:PropertyName("booking_id")
    var bookingId: String? = null,

    @get:PropertyName("User_address")
    @set:PropertyName("User_address")
    var Useraddress: String? = null,


    @get:PropertyName("User_contact_Number")
    @set:PropertyName("User_contact_Number")
    var UsercontactNumber: String? = null,

    @get:PropertyName("User_contact_Mail")
    @set:PropertyName("User_contact_Mail")
    var UsercontactMail: String? = null,

    var Priority: String? = null,

    @get:PropertyName("Booking_State")
    @set:PropertyName("Booking_State")
    var BookingState: String? = null,

    @get:PropertyName("Image_Url")
    @set:PropertyName("Image_Url")
    var ImageUrl : String? = null,

    @get:PropertyName("Client_address")
    @set:PropertyName("Client_address")
    var Clientaddress: String? = null,

    @get:PropertyName("Client_contact_Number")
    @set:PropertyName("Client_contact_Number")
    var ClientcontactNumber: String? = null,

    @get:PropertyName("Client_contact_Mail")
    @set:PropertyName("Client_contact_Mail")
    var ClientcontactMail: String? = null,
)
