package com.shryne.kmap.processor.check.kmap

import com.shryne.kmap.annotations.KMap
import com.shryne.kmap.processor.check.Check
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

/**
 * Checks if the [KMap] annotation on the given [sourceProperty] is set
 * correctly.
 *
 * @param sourceProperty The property that is annotated with [KMap].
 */
internal class CorrectKMapSetting(private val sourceProperty: Element) : Check {
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

    override fun hasErrors(): Boolean = errors.isNotEmpty()

    override fun printErrors(messager: Messager) {
        errors.forEach { messager.printMessage(Diagnostic.Kind.ERROR, it, sourceProperty) }
    }
}