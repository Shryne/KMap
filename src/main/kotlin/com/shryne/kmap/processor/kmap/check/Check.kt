package com.shryne.kmap.processor.kmap.check

import javax.annotation.processing.Messager

interface Check {
    fun hasErrors(): Boolean
    fun printErrors(messager: Messager)
}
