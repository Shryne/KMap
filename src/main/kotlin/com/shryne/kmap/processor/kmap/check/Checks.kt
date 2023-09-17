package com.shryne.kmap.processor.kmap.check

import javax.annotation.processing.Messager

/**
 * A composite class for checks.
 */
class Checks(private val checks: List<Check>) : Check {
    constructor(vararg checks: Check) : this(checks.toList())

    override fun hasErrors(): Boolean {
        return checks.any { it.hasErrors() }
    }

    override fun printErrors(messager: Messager) {
        checks.forEach { it.printErrors(messager) }
    }
}