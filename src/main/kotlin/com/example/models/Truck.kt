package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Truck(
    private val licensePlate: String = "!Empty",
    private val onLoadingBay: Boolean = false
) {

    public fun getLicensePlate(): String {
        return this.licensePlate;
    }

    public fun goToLoadingBay(): Truck {
        return copy(onLoadingBay = true);
    }
}

val truckWaitingArea = mutableListOf<Truck>();
val loadingBay = MutableList<Truck>(8) { Truck() };