package com.shryne.kmap.processor

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

typealias AMapPartner = com.shryne.kmap.annotations.MapPartner

/**
 * A class that is annotated with [AMapPartner] or that is the target of such a
 * class.
 *
 * @param source The [TypeElement] that represents the class.
 */
internal class Clazz(private val source: TypeElement) {
    /**
     * Returns [AMapPartner] annotation if this class is annotated with it.
     */
    val mapPartner: AMapPartner? = source.getAnnotation(AMapPartner::class.java)

    /**
     * The simple name (without the package) of the class.
     */
    val simpleName: String = source.simpleName.toString()

    /**
     * The fully qualified name (with the package) of the class as a [TypeName].
     */
    val name: TypeName = source.asClassName()

    /**
     * The properties of the class.
     */
    val properties: List<Element> = source.enclosedElements.filter {
        it.kind.isField
    }

    /**
     * Whether this class is annotated with [AMapPartner].
     */
    fun hasMapPartner(): Boolean = mapPartner != null

    override fun toString(): String = source.qualifiedName.toString()
}