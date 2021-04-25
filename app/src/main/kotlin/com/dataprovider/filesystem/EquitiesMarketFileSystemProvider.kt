package com.dataprovider.filesystem

import com.dataprovider.mongo.model.EquitiesMarketMongoModel
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EquitiesMarketFileSystemProvider {
    val mapper = jacksonObjectMapper()

    @Bean
    fun linesOfFile(): MutableList<EquitiesMarketMongoModel> {
        val lines = javaClass.classLoader.getResourceAsStream("stocks.json")?.readBytes()?.toString(Charsets.UTF_8)
        return mapper.readValue(lines, object : TypeReference<MutableList<EquitiesMarketMongoModel>>() {})
    }
}