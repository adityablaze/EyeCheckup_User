package com.example.eyecheckup_user.naviation

enum class CheckupScreens {
                          SplashScreen,
    LoginScreen,
    HomeScreen,
    VisionViewScreen,
    VisionTestScreen,
    AmdViewScreen,
    AmdTestScreen,
    AmdResultScreen,
    AstagmatismViewScreen,
    VisionResultScreen,
    LocationScreen,
    AppointmentScreen,
    AppointmentStatusScreen,
    AppointmentListScreen,
    DoctorDetailScreen,
    ClientListScreen;
    companion object{
        fun fromRoute(route:String?): CheckupScreens
                = when(route?.substringBefore("/")){
                    SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            HomeScreen.name -> HomeScreen
            VisionViewScreen.name -> VisionViewScreen
            VisionTestScreen.name -> VisionTestScreen
            AmdViewScreen.name -> AmdViewScreen
            AmdTestScreen.name -> AmdTestScreen
            VisionResultScreen.name -> VisionResultScreen
            LocationScreen.name -> LocationScreen
            AppointmentScreen.name -> AppointmentScreen
            DoctorDetailScreen.name -> DoctorDetailScreen
            ClientListScreen.name -> ClientListScreen
            AmdResultScreen.name -> AmdResultScreen
            AppointmentStatusScreen.name -> AppointmentStatusScreen
            AppointmentListScreen.name -> AppointmentListScreen
            AstagmatismViewScreen.name -> AstagmatismViewScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognised")

        }
    }
}