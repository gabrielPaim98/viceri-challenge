package com.example.vicerichallenge.fakes

import com.example.vicerichallenge.domain.model.Address
import com.example.vicerichallenge.domain.model.Company
import com.example.vicerichallenge.domain.model.Geo
import com.example.vicerichallenge.domain.model.User

object FakeUser {
    val default = User(
        id = 1,
        name = "John",
        username = "john123",
        email = "john@example.com",
        address = Address(
            street = "Main St",
            suite = "Apt 1",
            city = "City",
            zipcode = "12345",
            geo = Geo(lat = "1.1", lng = "2.2")
        ),
        phone = "123456",
        website = "john.com",
        company = Company(
            name = "Company",
            catchPhrase = "Innovation",
            bs = "Business"
        )
    )

    val list = listOf(default)

    fun dummyUser(id: Int) = User(
        id = id,
        name = "User $id",
        username = "user$id",
        email = "user$id@example.com",
        phone = "123",
        website = "site.com",
        address = Address(
            street = "", suite = "", city = "", zipcode = "",
            geo = Geo("", "")
        ),
        company = Company(name = "", catchPhrase = "", bs = "")
    )

    fun dummyUserList(length: Int): List<User> {
        return (1..length).map { id -> dummyUser(id) }
    }
}