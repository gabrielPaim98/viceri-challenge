package com.example.vicerichallenge.fakes

import com.example.vicerichallenge.data.remote.dto.AddressDto
import com.example.vicerichallenge.data.remote.dto.CompanyDto
import com.example.vicerichallenge.data.remote.dto.GeoDto
import com.example.vicerichallenge.data.remote.dto.UserDto

object FakeUserDto {
    val default = UserDto(
        id = 1,
        name = "John",
        username = "john123",
        email = "john@example.com",
        address = AddressDto(
            street = "Main St",
            suite = "Apt 1",
            city = "City",
            zipcode = "12345",
            geo = GeoDto(lat = "1.1", lng = "2.2")
        ),
        phone = "123456",
        website = "john.com",
        company = CompanyDto(
            name = "Company",
            catchPhrase = "Innovation",
            bs = "Business"
        )
    )

    val list = listOf(default)
}