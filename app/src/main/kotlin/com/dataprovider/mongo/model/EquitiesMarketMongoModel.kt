package com.dataprovider.mongo.model

import com.dataprovider.mongo.util.ObjectIdSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class EquitiesMarketMongoModel(
    @Id
    @JsonSerialize(using = ObjectIdSerializer::class)
    val id: ObjectId? = ObjectId(),
    val code: String,
    val description: String,
    val type: String,
    val quantity: Long,
    val participation: Number
)