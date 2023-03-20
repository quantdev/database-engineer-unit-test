package com.amazon.deunit

import java.lang.IllegalArgumentException
import java.sql.ResultSetMetaData

class QueryResultRow(private val metaData: ResultSetMetaData, private val row: List<Any>) {

    private val columnNameToIndex: Map<String, Int> = createColumnNameToIndex(metaData)

    fun columnValueAsString(columnName: String): String {
        val n = findColumnIndex(columnName)
        return row[n].toString();
    }

    fun columnValueAsInt(columnName: String): Int {
        val n = findColumnIndex(columnName)
        val value = row[n]
        return when (value) {
            is Int -> value
            else -> value.toString().toInt()
        }
    }

    private fun createColumnNameToIndex(metaData: ResultSetMetaData): Map<String, Int> {
        return (1..metaData.columnCount)
            .map { metaData.getColumnName(it) to it - 1  }
            .toMap()
    }

    private fun findColumnIndex(columnName: String): Int  {
        return columnNameToIndex[columnName]
            ?: throw IllegalArgumentException("Unknown column: ${columnName}")
    }
}
