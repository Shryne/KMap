package com.shryne.kmap

import com.shryne.kmap.annotations.KMap
import com.shryne.kmap.annotations.MapPartner
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Tests for basic mapping. Basic mapping is defined as the mapping of two or
 * more properties (not methods) using just [KMap] and [MapPartner].
 */
class BasicTest {
    @Test
    fun onePropertySameName() {
        assertMappingFiles(
            "/basic/one_property_same_name",
            Kotlin("Scalar"),
            Kotlin("Value")
        )
    }

    @Nested
    inner class Errors {
        @Test
        fun noSuchProperty() {
            assertError(
                "/basic/error/no_such_property",
                listOf(
                    Kotlin("Source"),
                    Kotlin("Target")
                ),
                emptyList(),
                "Couldn't find targetProperty in " +
                    "basic.error.no_such_property.Target. Enclosed elements are: "
                    + "a. Expected name was: x."
            )
        }

        @Test
        fun thisValueThisGetThisSetAllSet() {
            assertError(
                "/basic/error/this_value_this_get_this_set_all_set",
                listOf(
                    Kotlin("Source"),
                    Kotlin("Target")
                ),
                emptyList(),
                "KMap.thisValue is redundant when KMap.thisGet and " +
                    "KMap.thisSet are set."
            )
        }
    }
}
