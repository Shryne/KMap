package com.shryne.kmap

import org.junit.jupiter.api.Test

class BasicTest {
    @Test
    fun onePropertySameName() {
        assertMappingFiles(
            "/basic/one_property_same_name",
            Kotlin("Scalar"),
            Kotlin("Value")
        )
    }

    @Test
    fun noSuchProperty() {
        assertError(
            "/basic/error/no_such_property",
            listOf(
                Kotlin("Source"),
                Kotlin("Target")
            ),
            emptyList(),
            "No such property: y"
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
            "The given source property must be annotated with KMap."
        )
    }
}
