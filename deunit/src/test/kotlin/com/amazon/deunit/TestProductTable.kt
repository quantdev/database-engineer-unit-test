package com.amazon.deunit

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.SQLException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestProductTable : AbstractTestSQL() {

    @Test
    fun testProductTable() {
        executeQuery("SELECT count(*) FROM product")
        executeQuery("SELECT * FROM product" ) {
            assertResultNotEmpty()
            assertUniqueValue("product_name")
            forEachRow {
                assertTrue( columnValueAsInt("id") > 0 )
                val name = columnValueAsString("product_name")
                assertTrue( name.length > 2, "value must be longer than 2: '$name'")
            }
        }
    }

    @Test
    fun testBadSQL() {
        assertThrows<SQLException> {
            executeQuery("SELECT * FROM bad_table_name WHERE id = 1 AND product_name = 'foo'")
       }
    }

    @Test
    fun testEmptyResult() {
        executeQuery("SELECT * FROM product WHERE id = 0") {
            assertResultEmpty()
        }
    }


    @Test
    fun testResultVerifyLater() {

        val firstResult  = executeQuery("SELECT * FROM product WHERE id = 0")
        val secondResult = executeQuery("SELECT * FROM product WHERE id = 1")

        // is it better to do
        //  firstResult.verify { assertResultEmpty() }
        //  ???
        firstResult  { assertResultEmpty()    }
        secondResult { assertResultNotEmpty() }

        secondResult {
            assertEquals(1, size)
            rowAt(0) {
                assertEquals("Chair", columnValueAsString("product_name"))
            }
        }
    }

    @Test
    fun testExecutingFile() {
        executeQueryFile("product_test.sql") {
            assertResultNotEmpty()
            assertResultSize(1)
            rowAt(0) {
                assertEquals("Desk", columnValueAsString("product_name"))
            }
        }
    }
}