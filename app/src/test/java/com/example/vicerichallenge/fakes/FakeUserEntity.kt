package com.example.vicerichallenge.fakes

import com.example.vicerichallenge.data.local.entity.AddressEntity
import com.example.vicerichallenge.data.local.entity.CompanyEntity
import com.example.vicerichallenge.data.local.entity.GeoEntity
import com.example.vicerichallenge.data.local.entity.UserEntity

object FakeUserEntity {
    val default = UserEntity(
        id = 1,
        name = "John",
        username = "john123",
        email = "john@example.com",
        address = AddressEntity(
            street = "Main St",
            suite = "Apt 1",
            city = "City",
            zipcode = "12345",
            geo = GeoEntity(lat = "1.1", lng = "2.2")
        ),
        phone = "123456",
        website = "john.com",
        company = CompanyEntity(
            name = "Company",
            catchPhrase = "Innovation",
            bs = "Business"
        )
    )

    val list = listOf(default)
}