package com.amazon.deunit

import java.sql.ResultSet
import java.sql.ResultSetMetaData
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class QueryResult private constructor( private val metaData: ResultSetMetaData
                                     , private val rows: List<QueryResultRow>) {

    companion object {
        fun create(resultSet: ResultSet): QueryResult {
            val metaData = resultSet.metaData
            val numColumns = metaData.columnCount

            val rows = mutableListOf<QueryResultRow>()

            while (resultSet.next()) {
                val row = mutableListOf<Any>()
                for (i in 1..numColumns ) {
                    val value: Any = resultSet.getObject(i)
                    row.add(value)
                }
                rows.add(QueryResultRow(metaData, row))
            }
            return QueryResult(resultSet.metaData, rows)
        }
    }

    val size: Int
        get() = rows.size

    operator fun invoke(action: QueryResult.() -> Unit = {}) {
        this.action()
    }

    fun forEachRow( action: QueryResultRow.() -> Unit = {}) {
        for (row in rows) {
            row.action()
        }
    }

    fun rowAt(i: Int, action: QueryResultRow.() -> Unit = {}): QueryResultRow {
        val row = rows[i]
        row.action()
        return row
    }

    fun assertResultEmpty() {
        assertTrue(size == 0, "Expected: empty result Actual: non-empty")
    }

    fun assertResultNotEmpty() {
        assertTrue(size > 0 ,"Expected: non-empty result Actual: emtpy")
    }

    fun assertResultSize(expected: Int) {
        assertEquals(expected, size)
    }

    fun assertUniqueValue(columns: List<String>) {
        assertUniqueValue(*columns.toTypedArray())
    }

    fun assertUniqueValue(vararg columns: String) {
        val dupes = rows.groupBy { toKey(it, columns) }
            .filterValues { it.size > 1 }

        if ( dupes.size > 0 ) {
            val message = "Error: Duplicates\n ${ dupes.keys.joinToString(separator = "\n") }"
            fail(message)
        }
    }

    private fun toKey(row: QueryResultRow, columns: Array<out String>): String {
        return columns.map { row.columnValueAsString(it) }
                    .joinToString(separator = "~")
    }

}