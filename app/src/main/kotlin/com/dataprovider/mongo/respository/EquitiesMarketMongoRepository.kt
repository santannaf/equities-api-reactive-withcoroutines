package com.dataprovider.mongo.respository

import com.dataprovider.mongo.model.EquitiesMarketMongoModel
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface EquitiesMarketMongoRepository : MongoRepository<EquitiesMarketMongoModel, ObjectId> {
    fun findTop10ByOrderByParticipationDesc(): List<EquitiesMarketMongoModel>
}