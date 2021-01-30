package eu.javimar.data.source

interface LocationDataSource {
    suspend fun findLastRegion(): String?
}