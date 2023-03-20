package com.amazon.deunit

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import java.io.BufferedReader
import java.io.InputStreamReader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.ResultSetMetaData

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractTestSQL {
    protected lateinit var connection: Connection
    private val url = "jdbc:postgresql://localhost:5432/fwtest"
    private val username = "postgres"
    private val password = "mypassword"

    @BeforeAll
    private fun createConn() {
        connection = DriverManager.getConnection(url, username, password)
    }

    @AfterAll
    private fun destroyConn() {
        connection?.close()
    }

    @BeforeEach
    fun setUp() {
        // need logger
    }

    @AfterEach
    fun tearDown() {
        // need logger
    }

    fun executeQuery(sqlStatement: String, action: QueryResult.() -> Unit = {}): QueryResult {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(sqlStatement)
        val queryResult = QueryResult.create(resultSet)
        queryResult.action();
        return queryResult
    }

    fun executeQueryFile(fileName: String, action: QueryResult.() -> Unit = {}): QueryResult {
        val sqlStatement = readSqlFile(fileName)
        return executeQuery(sqlStatement, action)
    }

    fun readSqlFile(fileName: String): String {
        val inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("product_test.sql")
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.readText()
    }
}