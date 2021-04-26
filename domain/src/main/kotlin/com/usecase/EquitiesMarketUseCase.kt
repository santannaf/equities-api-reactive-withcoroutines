package com.usecase

import com.dataprovider.EquitiesDataProvider
import com.dataprovider.EquitiesForApiFinanceYahooDataProvider
import com.entity.EquitiesEntity
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.inject.Named
import kotlin.system.measureTimeMillis

@Named
class EquitiesMarketUseCase(
    private val dataProvider: EquitiesDataProvider,
    private val apiFinanceYahoo: EquitiesForApiFinanceYahooDataProvider
) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun insertEquities(equities: EquitiesEntity): Mono<EquitiesEntity> {
        log.info("Insert Equities: $equities")
        return dataProvider.insertEquities(equities)
    }

    fun equities(): Flux<EquitiesEntity> {
        log.info("Find All equities")
        return dataProvider.findAllEquities()
    }

    fun equitiesById(id: String): Mono<EquitiesEntity> {
        log.info("Find One equities by id: $id")
        return dataProvider.findOneEquities(id)
    }

    fun deleteEquitiesById(id: String): Mono<Void> {
        log.info("Delete One equities by id: $id")
        return dataProvider.deleteOneEquities(id)
    }

    fun findLastGreaterTenEquitiesWithPriceWebFluxSequencial(): Flux<EquitiesEntity> {
        log.info("Search ten last greater equities into database...")
        return dataProvider.findLastGreaterTenEquitiesWithPriceReactive().map {
            val time = measureTimeMillis {
                val response = apiFinanceYahoo.equitiesPriceMarket("${it.code}.SA")
                with(it) {
                    price = response
                }
            }
            log.info("Call duration time API Finance Yahoo: $time")
            it
        }
    }

    suspend fun findLastGreaterTenEquitiesWithPriceWithCoroutines(): List<EquitiesEntity> = coroutineScope {
        log.info("Search ten last greater equities into database  using coroutines ...")
        dataProvider.findLastGreaterTenEquitiesWithPriceNonReactive().map {
            GlobalScope.async(Dispatchers.Default) {
                val response = apiFinanceYahoo.equitiesPriceMarket("${it.code}.SA")
                with(it) {
                    price = response
                }
                log.info("Equities ${it.code} with price: $response")
                it
            }
        }.awaitAll()
    }

    suspend fun findLastGreaterTenEquitiesWithPriceWithOneTimeWithCoroutines(): List<EquitiesEntity> = coroutineScope {
        val equities = dataProvider.findLastGreaterTenEquitiesWithPriceNonReactive()
        val symbols = StringBuilder()
        equities.forEach { symbols.append("${it.code}.SA, ") }
        val textToRequest =
            Regex(".\\s*$", RegexOption.MULTILINE).replace(symbols.toString(), "")
                .replace(Regex("\\s*"), "")

        val result = apiFinanceYahoo.equitiesAllPriceMarket(textToRequest)

        equities.map { eq ->
            GlobalScope.async(Dispatchers.Default) {
                val filtered = result.first { it.symbol == eq.code }
                eq.price = filtered.regularMarketPrice
                eq
            }
        }.awaitAll()
    }
}