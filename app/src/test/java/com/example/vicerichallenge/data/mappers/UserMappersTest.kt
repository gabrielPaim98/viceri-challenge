package com.example.vicerichallenge.data.mappers

import com.example.vicerichallenge.data.local.entity.AddressEntity
import com.example.vicerichallenge.data.local.entity.CompanyEntity
import com.example.vicerichallenge.data.local.entity.GeoEntity
import com.example.vicerichallenge.data.local.entity.UserEntity
import com.example.vicerichallenge.data.remote.dto.AddressDto
import com.example.vicerichallenge.data.remote.dto.CompanyDto
import com.example.vicerichallenge.data.remote.dto.GeoDto
import com.example.vicerichallenge.data.remote.dto.UserDto
import com.example.vicerichallenge.domain.model.Address
import com.example.vicerichallenge.domain.model.Company
import com.example.vicerichallenge.domain.model.Geo
import com.example.vicerichallenge.domain.model.User
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UserMapperTest {

    private val geoDto = GeoDto(lat = "1.1", lng = "2.2")
    private val geoEntity = GeoEntity(lat = "1.1", lng = "2.2")
    private val geo = Geo(lat = "1.1", lng = "2.2")

    private val addressDto = AddressDto(
        street = "Main St",
        suite = "Apt 1",
        city = "City",
        zipcode = "12345",
        geo = geoDto
    )

    private val addressEntity = AddressEntity(
        street = "Main St",
        suite = "Apt 1",
        city = "City",
        zipcode = "12345",
        geo = geoEntity
    )

    private val address = Address(
        street = "Main St",
        suite = "Apt 1",
        city = "City",
        zipcode = "12345",
        geo = geo
    )

    private val companyDto = CompanyDto(
        name = "Company",
        catchPhrase = "Innovation",
        bs = "Business"
    )

    private val companyEntity = CompanyEntity(
        name = "Company",
        catchPhrase = "Innovation",
        bs = "Business"
    )

    private val company = Company(
        name = "Company",
        catchPhrase = "Innovation",
        bs = "Business"
    )

    private val userDto = UserDto(
        id = 1,
        name = "John",
        username = "john123",
        email = "john@example.com",
        address = addressDto,
        phone = "123456",
        website = "john.com",
        company = companyDto
    )

    private val userEntity = UserEntity(
        id = 1,
        name = "John",
        username = "john123",
        email = "john@example.com",
        address = addressEntity,
        phone = "123456",
        website = "john.com",
        company = companyEntity
    )

    private val userDomain = User(
        id = 1,
        name = "John",
        username = "john123",
        email = "john@example.com",
        address = address,
        phone = "123456",
        website = "john.com",
        company = company
    )

    @Test
    fun `should map UserDto to Domain`() {
        val result = userDto.toDomain()
        assertEquals(userDomain, result)
    }

    @Test
    fun `should map UserDto to Entity`() {
        val result = userDto.toEntity()
        assertEquals(userEntity, result)
    }

    @Test
    fun `should map UserEntity to Domain`() {
        val result = userEntity.toDomain()
        assertEquals(userDomain, result)
    }

    @Test
    fun `should map AddressDto to Domain`() {
        val result = addressDto.toDomain()
        assertEquals(address, result)
    }

    @Test
    fun `should map CompanyDto to Domain`() {
        val result = companyDto.toDomain()
        assertEquals(company, result)
    }

    @Test
    fun `should map GeoDto to Domain`() {
        val result = geoDto.toDomain()
        assertEquals(geo, result)
    }
}