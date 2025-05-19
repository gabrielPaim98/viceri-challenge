package com.example.vicerichallenge.data.mappers

import com.example.vicerichallenge.data.local.entity.*
import com.example.vicerichallenge.data.remote.dto.*
import com.example.vicerichallenge.domain.model.*

/** -------------------------- DTO → DOMAIN -------------------------- */

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    username = username,
    email = email,
    address = address.toDomain(),
    phone = phone,
    website = website,
    company = company.toDomain()
)

fun AddressDto.toDomain(): Address = Address(
    street = street,
    suite = suite,
    city = city,
    zipcode = zipcode,
    geo = geo.toDomain()
)

fun GeoDto.toDomain(): Geo = Geo(
    lat = lat,
    lng = lng
)

fun CompanyDto.toDomain(): Company = Company(
    name = name,
    catchPhrase = catchPhrase,
    bs = bs
)

/** -------------------------- DTO → ENTITY -------------------------- */

fun UserDto.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    username = username,
    email = email,
    address = AddressEntity(
        street = address.street,
        suite = address.suite,
        city = address.city,
        zipcode = address.zipcode,
        geo = GeoEntity(
            lat = address.geo.lat,
            lng = address.geo.lng
        )
    ),
    phone = phone,
    website = website,
    company = CompanyEntity(
        name = company.name,
        catchPhrase = company.catchPhrase,
        bs = company.bs
    )
)

/** -------------------------- ENTITY → DOMAIN -------------------------- */

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    username = username,
    email = email,
    address = Address(
        street = address.street,
        suite = address.suite,
        city = address.city,
        zipcode = address.zipcode,
        geo = Geo(
            lat = address.geo.lat,
            lng = address.geo.lng
        )
    ),
    phone = phone,
    website = website,
    company = Company(
        name = company.name,
        catchPhrase = company.catchPhrase,
        bs = company.bs
    )
)
