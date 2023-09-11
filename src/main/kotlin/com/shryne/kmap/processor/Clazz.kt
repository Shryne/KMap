package com.shryne.kmap.processor

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.element.TypeElement

/**
 * A class that is annotated with [MapPartner] or that is the target of such a
 * class.
 *
 * @param source The [TypeElement] that represents the class.
 */
internal class Clazz(private val source: TypeElement) {
    /**
     * The simple name (without the package) of the class.
     */
    val simpleName: String = source.simpleName.toString()

    /**
     * The fully qualified name (with the package) of the class as a [TypeName].
     */
    val name: TypeName = source.asClassName()
}