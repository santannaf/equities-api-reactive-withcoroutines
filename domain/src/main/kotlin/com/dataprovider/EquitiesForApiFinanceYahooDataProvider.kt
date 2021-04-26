package com.dataprovider

import com.entity.ResultApiFinanceYahooEntity

interface EquitiesForApiFinanceYahooDataProvider {
    fun equitiesPriceMarket(symbols : String) : Number?
    fun equitiesAllPriceMarket(symbols: String): List<ResultApiFinanceYahooEntity>
}