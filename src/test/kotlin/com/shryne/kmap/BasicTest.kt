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
