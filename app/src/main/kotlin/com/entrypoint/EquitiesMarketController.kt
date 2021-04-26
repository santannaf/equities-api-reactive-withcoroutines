package com.entrypoint

import com.entity.EquitiesEntity
import com.usecase.EquitiesMarketUseCase
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = [ "api/equities" ])
class EquitiesMarketController(
    private val useCase: EquitiesMarketUseCase
) {
    @PostMapping
    fun findAllEquities(@RequestBody equities: EquitiesEntity) : Mono<EquitiesEntity> {
        return useCase.insertEquities(equities)
    }

    @GetMapping
    fun findAllEquities() : Flux<EquitiesEntity> {
        return useCase.equities()
    }

    @GetMapping(path = [ "/{id}" ])
    fun findOneEquities(@PathVariable id: String) : Mono<EquitiesEntity> {
        return useCase.equitiesById(id)
    }

    @DeleteMapping(path = [ "/{id}" ])
    fun deleteOneEquities(@PathVariable id: String) : Mono<Void> {
        return useCase.deleteEquitiesById(id)
    }

    @GetMapping(path = [ "/bigger" ])
    fun findLastGreaterTenEquitiesWithPrice() : Flux<EquitiesEntity> {
        return useCase.findLastGreaterTenEquitiesWithPrice()
    }

    @GetMapping(path = [ "/bigger/coroutines" ])
    suspend fun findLastGreaterTenEquitiesWithPriceWithCoroutines() : List<EquitiesEntity> {
        return useCase.findLastGreaterTenEquitiesWithPriceWithCoroutines()
    }

    @GetMapping(path = [ "/bigger/onetime" ])
    suspend fun findLastGreaterTenEquitiesWithPriceWithOneTime() : List<EquitiesEntity> {
        return useCase.findLastGreaterTenEquitiesWithPriceWithOneTime()
    }
}