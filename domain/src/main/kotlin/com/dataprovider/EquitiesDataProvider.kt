package com.dataprovider

import com.entity.EquitiesEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EquitiesDataProvider {
    fun insertEquities(equities: EquitiesEntity) : Mono<EquitiesEntity>
    fun findAllEquities() : Flux<EquitiesEntity>
    fun findOneEquities(id: String) : Mono<EquitiesEntity>
    fun deleteOneEquities(id: String) : Mono<Void>
    fun findLastGreaterTenEquitiesWithPriceReactive(): Flux<EquitiesEntity>
    fun findLastGreaterTenEquitiesWithPriceNonReactive(): List<EquitiesEntity>
}