package com.entity

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EquitiesEntity(
    val id: String?,
    val code: String,
    val description: String,
    val type: String,
    val quantity: Long,
    val participation: Number,
    var price: Number? = null
)