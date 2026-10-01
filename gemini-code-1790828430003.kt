package com.example.siwarga

data class Warga(
    val id: Int,
    val kodeRumah: String,
    val nama: String,
    var statusLunas: Boolean = false,
    var totalBayar: Double = 0.0
)