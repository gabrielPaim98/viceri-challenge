package com.example.vicerichallenge.data.remote.dto

import com.example.vicerichallenge.domain.model.User

data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: AddressDto,
    val phone: String,
    val website: String,
    val company: CompanyDto
)