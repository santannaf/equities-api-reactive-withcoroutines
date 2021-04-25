package com.dataprovider.mongo

import com.dataprovider.EquitiesDataProvider
import com.dataprovider.mongo.model.EquitiesMarketMongoModel
import com.dataprovider.mongo.respository.EquitiesMarketMongoReactiveRespository
import com.dataprovider.mongo.respository.EquitiesMarketMongoRepository
import com.entity.EquitiesEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.Charset

@Component
class EquitiesMarketMongoProvider(
    private val repositoryReactive: EquitiesMarketMongoReactiveRespository,
    private val repositoryNonReactive: EquitiesMarketMongoRepository,
    private val linesOfFile: MutableList<EquitiesMarketMongoModel>
) : EquitiesDataProvider {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    init {
        dbInitCoroutines()
    }

    private fun dbInitWithWebFlux() {
        repositoryReactive.deleteAll().doAfterTerminate {
            insertAllEquitiesInitMongo()
        }.subscribe()
    }

    private fun dbInitCoroutines() {
        repositoryReactive.deleteAll().doAfterTerminate {
            linesOfFile.map {
                GlobalScope.launch {
                    insertItem(it)
                }
            }
        }.subscribe()
    }

    @Transactional
    protected fun insertAllEquitiesInitMongo() {
        repositoryReactive.saveAll(linesOfFile).subscribe()
    }

    private suspend fun insertItem(equitiesMarket: EquitiesMarketMongoModel) =
        repositoryReactive.save(equitiesMarket).subscribe()

    override fun insertEquities(equities: EquitiesEntity): Mono<EquitiesEntity> {
        return Mono.just(equities)
            .map {
                EquitiesMarketMongoModel(
                    code = it.code,
                    description = it.description,
                    type = it.type,
                    quantity = it.quantity,
                    participation = it.participation
                )
            }
            .flatMap(repositoryReactive::save)
            .map {
                EquitiesEntity(
                    id = it.id.toString(),
                    code = it.code,
                    description = it.description,
                    type = it.type,
                    quantity = it.quantity,
                    participation = it.participation
                )
            }
    }

    override fun findAllEquities(): Flux<EquitiesEntity> {
        return repositoryReactive.findAll().map {
            EquitiesEntity(
                id = it.id.toString(),
                code = it.code,
                description = it.description,
                type = it.type,
                quantity = it.quantity,
                participation = it.participation
            )
        }
    }

    override fun findOneEquities(id: String): Mono<EquitiesEntity> {
        return repositoryReactive.findById(ObjectId(String(id.toByteArray(), 0, 24, Charset.forName("US-ASCII")))).map {
            EquitiesEntity(
                id = it.id.toString(),
                code = it.code,
                description = it.description,
                type = it.type,
                quantity = it.quantity,
                participation = it.participation
            )
        }
    }

    override fun deleteOneEquities(id: String): Mono<Void> {
        return repositoryReactive.findById(ObjectId(String(id.toByteArray(), 0, 24, Charset.forName("US-ASCII"))))
            .flatMap(repositoryReactive::delete)
    }

    override fun findLastGreaterTenEquitiesWithPriceReactive(): Flux<EquitiesEntity> {
        return repositoryReactive.findTop10ByOrderByParticipationDesc().map {
            EquitiesEntity(
                id = it.id.toString(),
                code = it.code,
                description = it.description,
                type = it.type,
                quantity = it.quantity,
                participation = it.participation
            )
        }
    }

    override fun findLastGreaterTenEquitiesWithPriceNonReactive(): List<EquitiesEntity> {
        return repositoryNonReactive.findTop10ByOrderByParticipationDesc().map {
            EquitiesEntity(
                id = it.id.toString(),
                code = it.code,
                description = it.description,
                type = it.type,
                quantity = it.quantity,
                participation = it.participation
            )
        }
    }
}