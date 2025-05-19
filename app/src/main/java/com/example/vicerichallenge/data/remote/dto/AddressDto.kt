package com.example.vicerichallenge.data.remote.dto

import com.example.vicerichallenge.domain.model.Address

data class AddressDto(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: GeoDto
)