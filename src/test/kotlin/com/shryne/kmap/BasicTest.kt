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
}
