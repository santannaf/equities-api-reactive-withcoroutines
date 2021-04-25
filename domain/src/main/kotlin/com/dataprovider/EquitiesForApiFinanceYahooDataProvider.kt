package com.dataprovider

interface EquitiesForApiFinanceYahooDataProvider {
    fun equitiesPriceMarket(symbols : String) : Number?
}