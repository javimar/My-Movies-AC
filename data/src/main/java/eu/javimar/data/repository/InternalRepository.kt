package eu.javimar.data.repository

import eu.javimar.data.source.InternalDataSource

class InternalRepository (private val internalDataSource: InternalDataSource)
{
    fun getLanguage():String
    {
        return internalDataSource.getLanguageData()
    }

}