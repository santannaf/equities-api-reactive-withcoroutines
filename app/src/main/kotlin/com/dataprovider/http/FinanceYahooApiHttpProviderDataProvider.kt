package com.dataprovider.http

import com.dataprovider.EquitiesForApiFinanceYahooDataProvider
import com.dataprovider.http.data.response.FinanceYahooApiResponse
import com.dataprovider.http.data.response.ResultResponseApiFinance
import com.entity.ResultApiFinanceYahooEntity
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.stream.Collectors

@Component
class FinanceYahooApiHttpProviderDataProvider : EquitiesForApiFinanceYahooDataProvider {
    val mapper = jacksonObjectMapper()

    override fun equitiesPriceMarket(symbols: String): Number? {
        val request =
            HttpRequest.newBuilder(URI("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-quotes?region=BR&symbols=$symbols"))
                .headers(
                    "x-rapidapi-key",
                    "ed4cf63174mshb9a40aaf269abf2p191103jsn1e9f1bf48670",
                    "x-rapidapi-host",
                    "apidojo-yahoo-finance-v1.p.rapidapi.com"
                )
                .GET()
                .build()
        val response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString())
            .body()
        return mapper.readValue(
            response,
            object : TypeReference<FinanceYahooApiResponse>() {}).quoteResponse.result.stream()
            .map(ResultResponseApiFinance::regularMarketPrice).findFirst().orElse(null)
    }

    override fun equitiesAllPriceMarket(symbols: String): List<ResultApiFinanceYahooEntity> {
        val request =
            HttpRequest.newBuilder(URI("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-quotes?region=BR&symbols=$symbols"))
                .headers(
                    "x-rapidapi-key",
                    "ed4cf63174mshb9a40aaf269abf2p191103jsn1e9f1bf48670",
                    "x-rapidapi-host",
                    "apidojo-yahoo-finance-v1.p.rapidapi.com"
                )
                .GET()
                .build()
        val response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString())
            .body()
        return mapper.readValue(
            response,
            object : TypeReference<FinanceYahooApiResponse>() {}).quoteResponse.result.stream()
            .map {
                ResultApiFinanceYahooEntity(it.regularMarketPrice, it.symbol)
            }.collect(Collectors.toList()).ifEmpty { listOf() }
    }
}