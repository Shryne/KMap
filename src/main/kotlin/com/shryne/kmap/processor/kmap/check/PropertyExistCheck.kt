package com.shryne.kmap.processor.kmap.check

import com.shryne.kmap.processor.Clazz
import com.shryne.kmap.processor.kmap.KMap
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

typealias KMapAnnotation = com.shryne.kmap.annotations.KMap

/**
 * Checks if the property that is given through [KMapAnnotation.value] exists.
 */
internal class PropertyExistCheck(
    private val sourceProperty: Element,
    private val targetClass: Clazz,
    private val kMap: KMap
) : Check {
    private val errors by lazy {
        val errors = mutableListOf<String>()
        val annotation = sourceProperty.getAnnotation(KMapAnnotation::class.java)
        if (annotation == null) {
            errors.add("The given source property must be annotated with KMap.")
        } else {
                val result = targetClass.properties.find {
                    it.simpleName.toString() == kMap.sourceSet
                }
                if (result == null) {
                    errors.add(
                        "Couldn't find targetProperty in $targetClass. Enclosed " +
                            "elements are: " +
                            "${targetClass.properties.joinToString(", ")}. " +
                            "Expected name was: ${kMap.sourceSet}."
                    )
                }

        }
        errors
    }

    override fun hasErrors(): Boolean = errors.isNotEmpty()

    override fun printErrors(messager: Messager) {
        errors.forEach {
            messager.printMessage(Diagnostic.Kind.ERROR, it, sourceProperty)
        }
    }
}