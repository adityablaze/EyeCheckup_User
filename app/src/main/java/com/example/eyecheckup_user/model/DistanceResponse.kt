package com.example.eyecheckup_user.model

data class DistanceResponse(
    val distance_units: String,
    val mode: String,
    val sources: List<Source>,
    val sources_to_targets: List<List<SourcesToTarget>>,
    val targets: List<Target>,
    val units: String
)