package com.shryne.kmap.processor.kmap.check

import com.shryne.kmap.annotations.KMap
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

class KMapSettingCheck(private val sourceProperty: Element) {
    private val errors by lazy {
        val errors = mutableListOf<String>()
        val annotation = sourceProperty.getAnnotation(KMap::class.java)
        if (annotation == null) {
            errors.add("The given source property must be annotated with KMap.")
        } else {
            if (annotation.thisValue != "") {
                if ((annotation.thisGet != "") and (annotation.thisSet != "")) {
                    errors.add(
                        "KMap.thisValue is redundant when KMap.thisGet and " +
                            "KMap.thisSet are set."
                    )
                }
                if (annotation.targetGet != "" || annotation.targetSet != "") {
                    errors.add(
                        "KMap.thisValue is redundant when KMap.targetGet and " +
                            "KMap.targetSet are set."
                    )
                }
            }
        }
        errors
    }

    fun hasErrors(): Boolean = errors.isNotEmpty()

    fun printErrors(messager: Messager) {
        errors.forEach { messager.printMessage(Diagnostic.Kind.ERROR, it, sourceProperty) }
    }
}