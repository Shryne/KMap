package com.shryne.kmap.processor.check.kmap

import com.shryne.kmap.processor.Clazz
import com.shryne.kmap.processor.KMap
import com.shryne.kmap.processor.check.Check
import javax.annotation.processing.Messager
import javax.tools.Diagnostic

typealias KMapAnnotation = com.shryne.kmap.annotations.KMap

/**
 * Checks if the property that is given through [KMapAnnotation.value] exists.
 */
internal class PropertyExists(
    private val kMap: KMap,
    private val targetClass: Clazz
) : Check {
    private val errors by lazy {
        val errors = mutableListOf<String>()
        val annotation = kMap.annotated.getAnnotation(KMapAnnotation::class.java)
        if (annotation == null) {
            errors.add("The given source property must be annotated with KMap.")
        } else {
                val result = targetClass.properties.find {
                    it.simpleName.toString() == kMap.sourceSetName
                }
                if (result == null) {
                    errors.add(
                        "Couldn't find targetProperty in $targetClass. Enclosed " +
                            "elements are: " +
                            "${targetClass.properties.joinToString(", ")}. " +
                            "Expected name was: ${kMap.sourceSetName}."
                    )
                }

        }
        errors
    }

    override fun hasErrors(): Boolean = errors.isNotEmpty()

    override fun printErrors(messager: Messager) {
        errors.forEach {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                it,
                kMap.annotated
            )
        }
    }
}