package com.shryne.kmap.processor.check

import javax.annotation.processing.Messager

/**
 * A check for the setting of [KMapAnnotation].
 */
interface Check {
    /**
     * Returns true if there are errors.
     */
    fun hasErrors(): Boolean

    /**
     * Prints the errors to the [Messager].
     */
    fun printErrors(messager: Messager)
}
