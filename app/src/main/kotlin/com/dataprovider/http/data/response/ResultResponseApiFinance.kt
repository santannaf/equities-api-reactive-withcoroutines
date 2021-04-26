package com.dataprovider.http.data.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResultResponseApiFinance(
    val regularMarketPrice: Number,
    val symbol: String
)