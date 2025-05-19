package com.example.vicerichallenge.data.local.entity

import androidx.room.Embedded

data class AddressEntity(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    @Embedded
    val geo: GeoEntity
)