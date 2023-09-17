package com.shryne.kmap.processor.kmap

import com.shryne.kmap.annotations.KMap
import java.util.*
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.util.Types
import javax.tools.Diagnostic

typealias AMapPartner = com.shryne.kmap.annotations.MapPartner

/**
 * A decorator for [KMap] to create the assignments for the annotated property.
 * Note that this class can be used both ways: annotated property -> property
 * and property -> annotated property.
 *
 * @param sourceProperty The property that contains the value.
 * @param sourceClass The class of the source property.
 * @param targetClass The class of the target property that will get the value.
 * @param types The utility class necessary to operate on Types.
 */
internal class KMap(
    val sourceProperty: Element,
    private val sourceClass: Element,
    private val targetClass: Element,
    private val types: Types
) {
    /**
     * The annotation containing the information about the mapping of a
     * property.
     */
    private val kMap: KMap = sourceProperty.getAnnotation(KMap::class.java)

    private val propertyName: String =
        sourceProperty.simpleName.toString().run {
            if (sourceProperty.kind == ElementKind.METHOD) {
                removeSuffix("\$annotations")
                    .removePrefix("get")
                    .replaceFirstChar {
                        it.lowercase(Locale.getDefault())
                    }
            } else {
                this
            }
        }

    private val sourceGet: String = propertyAccessName(
        propertyName,
        kMap.thisValue,
        kMap.thisGet,
        kMap.thisSet
    )

    val sourceSet: String = propertyAccessName(
        propertyName,
        kMap.thisValue,
        kMap.thisSet,
        kMap.thisSet
    )
    private val targetGet: String = propertyAccessName(
        propertyName,
        kMap.value,
        kMap.targetGet,
        kMap.targetSet
    )
    private val targetSet: String = propertyAccessName(
        propertyName,
        kMap.value,
        kMap.targetSet,
        kMap.targetSet
    )

    /**
     * The name of the property that will be used to access the value of the
     * source property.
     *
     * @param propertyName The name of the property.
     * @param annotationValue The value of the annotation.
     * @param annotationGet The getter value of the annotation.
     * @param annotationSet The setter value of the annotation.
     */
    private fun propertyAccessName(
        propertyName: String,
        annotationValue: String,
        annotationGet: String,
        annotationSet: String
    ): String = annotationGet.ifEmpty {
        annotationValue.ifEmpty { propertyName }
    }

    private val importPackage: String? = mapPartner()?.packageName

    /**
     * The import necessary to apply the assignment or null if no one is
     * necessary.
     */
    val sourceToTargetImport: Pair<String, String>? = importPackage?.run {
        this to "to${
            targetClass.enclosedElements.find {
                it.simpleName.toString() == targetGet.takeWhile { it != '.' }
            }!!.asType()!!.run {
                types.asElement(this)?.simpleName
            }
        }"
    }

    val targetToSourceImport: Pair<String, String>? = mapPartner()?.run {
        packageName to "to${
            sourceClass.enclosedElements.find {
                propertyName == sourceGet.takeWhile { it != '.' }
            }!!.asType()!!.run {
                types.asElement(this)?.simpleName
            }
        }"
    }

    private val targetProperty: Element
        get() {
            return targetClass.enclosedElements.find {
                it.simpleName.toString() == sourceSet
            }!!
        }

    /**
     * @return The assignment from source property to the target property
     * (target = source).
     */
    fun sourceToTargetAssignment(): String =
        assignment(sourceGet, targetSet, targetGet)

    /**
     * @return The assignment from the target property to the source property
     * (source = target).
     */
    fun targetToSourceAssignment(): String =
        assignment(targetGet, sourceSet, sourceGet)

    /**
     * Returns the assignment statement. This method distinguishes between
     * methods and properties. A basic assignment would look like this:
     * ```kotlin
     * setter = getter
     * ```
     * @param getter The getter property. Using () means that the getter is a
     * method.
     * @param setter The setter property. Using () means that the setter is a
     * method.
     * @param propertyName The name of the property that is annotated with KMap.
     * This
     * @return The assignment.
     */
    private fun assignment(
        getter: String,
        setter: String,
        propertyName: String
    ): String {
        val assignment = if (hasMapPartner()) "$getter.to${
            sourceClass.propertyClassName(propertyName)
        }()" else getter
        return if (setter.isMethod())
            "it.${setter.substringBefore("(")}($assignment)"
        else "it.$setter = $assignment"
    }


    private fun String.isMethod(): Boolean = endsWith("()")

    /**
     * @return True if the source class is annotated with [MapPartner] and
     * otherwise false.
     */
    private fun hasMapPartner(): Boolean =
        sourceClass.enclosedElements.find {
            it.simpleName.toString() == sourceGet
        }?.asType()?.run {
            types.asElement(this)
                ?.getAnnotation(AMapPartner::class.java) != null
        } ?: false

    /**
     * @return The [MapPartner] annotation of the source class or null if it
     * isn't annotated with it.
     */
    private fun mapPartner(): AMapPartner? =
        sourceClass.enclosedElements.find {
            it.simpleName.toString() == sourceGet
        }?.asType()?.run {
            types.asElement(this)?.getAnnotation(AMapPartner::class.java)
        }

    private fun Element.propertyClassName(propertyName: String): String =
        if (propertyName.contains('.')) {
            val name = propertyName.takeWhile { it != '.' }
            val element = enclosedElements.find {
                it.simpleName.toString() == name
            }!!.asType()
            println(
                "Given property name is: $propertyName, name is: $name, given Element is: $simpleName, new element is: ${element}, ${
                    propertyName.substring(
                        propertyName.indexOfFirst { it == '.' } + 1)
                }")
            types.asElement(element)
                .propertyClassName(propertyName.substring(propertyName.indexOfFirst { it == '.' } + 1))
        } else {
            println("property name is: $propertyName. Class is: ${this.simpleName}. Enclosed elements are: ${enclosedElements.map { simpleName.toString() }}")
            enclosedElements.find {
                it.simpleName.toString() == propertyName
            }!!.asType()!!.run {
                types.asElement(this)?.simpleName.toString()
            }
        }
}
