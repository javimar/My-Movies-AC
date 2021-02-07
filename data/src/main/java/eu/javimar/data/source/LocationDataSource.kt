package eu.javimar.data.source

interface LocationDataSource
{
    // Could return null
    suspend fun findLastRegion(): String?
}