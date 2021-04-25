package com.dataprovider.mongo.respository

import com.dataprovider.mongo.model.EquitiesMarketMongoModel
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface EquitiesMarketMongoReactiveRespository : ReactiveMongoRepository<EquitiesMarketMongoModel, ObjectId> {
    fun findTop10ByOrderByParticipationDesc(): Flux<EquitiesMarketMongoModel>
}