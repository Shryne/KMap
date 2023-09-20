package com.shryne.kmap.processor

import com.shryne.kmap.annotations.KMap
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.util.Types

/**
 * A decorator for [KMap] to create assignments for the annotated property.
 * This class can be used both ways: annotated property -> property and
 * property -> annotated property.
 *
 * @param annotated The annotated element.
 * @param sourceClass The class of the source property.
 * @param targetClass The class of the target property that will receive the value.
 * @param types The utility class necessary to operate on Types.
 */
internal class KMap(
    val annotated: Element,
    private val sourceClass: Element,
    private val targetClass: Element,
    private val types: Types
) {
    /** The annotation containing the information about the mapping of a property. */
    private val kMap: KMap = annotated.getAnnotation(KMap::class.java)

    /**
     * The name of the property that is annotated with [KMap].
     * Computed lazily to ensure that it's only determined when needed.
     */
    private val propertyName: String =
        annotated.simpleName.toString()
            .removeSuffix("\$annotations")
            .removePrefix("get")
            .replaceFirstChar { it.lowercase(Locale.getDefault()) }

    /**
     * Determines the access name of the property based on annotations.
     */
    private fun propertyAccessName(
        propertyName: String,
        annotationValue: String,
        annotationGet: String,
        annotationSet: String
    ): String =
        annotationGet.ifEmpty { annotationValue.ifEmpty { propertyName } }

    private val sourceGetName = propertyAccessName(
        propertyName,
        kMap.thisValue,
        kMap.thisGet,
        kMap.thisSet
    )

    val sourceSetName = propertyAccessName(
        propertyName,
        kMap.thisValue,
        kMap.thisSet,
        kMap.thisSet
    )

    private val targetGetName = propertyAccessName(
        propertyName,
        kMap.value,
        kMap.targetGet,
        kMap.targetSet
    )

    private val targetSetName = propertyAccessName(
        propertyName,
        kMap.value,
        kMap.targetSet,
        kMap.targetSet
    )

    /** The package that may need to be imported for mapping. */
    private val importPackage: String? = mapPartner()?.packageName

    /**
     * Determines the necessary import to map from source to target.
     * Returns null if no import is necessary.
     */
    val sourceToTargetImport: Pair<String, String>? = importPackage?.let {
        it to "to${findElementName(targetClass, targetGetName)}"
    }

    /**
     * Determines the necessary import to map from target to source.
     * Returns null if no import is necessary.
     */
    val targetToSourceImport: Pair<String, String>? = mapPartner()?.let {
        it.packageName to "to${findElementName(sourceClass, sourceGetName)}"
    }

    /** The source property element based on the property name. */
    val sourceProperty: Element?
        get() = findElementByName(sourceClass, propertyName)

    /** The target property element based on the set name. */
    val targetProperty: Element?
        get() = findElementByName(targetClass, sourceSetName)

    /**
     * Finds an element by its name within a given element.
     * @param element The enclosing element.
     * @param name The name of the enclosed element to find.
     */
    private fun findElementByName(element: Element, name: String) =
        element.enclosedElements.find { it.simpleName.toString() == name }

    /**
     * Determines the name of a nested element within a given element.
     * @param element The enclosing element.
     * @param name The name of the enclosed element to find.
     */
    private fun findElementName(element: Element, name: String) =
        findElementByName(element, name.takeWhile { it != '.' })
            ?.asType()
            ?.let { types.asElement(it)?.simpleName.toString() }

    /**
     * Constructs an assignment string for mapping from source to target.
     */
    fun sourceToTargetAssignment(): String =
        assignment(sourceGetName, targetSetName, targetGetName)

    /**
     * Constructs an assignment string for mapping from target to source.
     */
    fun targetToSourceAssignment(): String =
        assignment(targetGetName, sourceSetName, sourceGetName)

    /**
     * Constructs the actual assignment string based on getter and setter.
     */
    private fun assignment(
        getter: String,
        setter: String,
        propertyName: String
    ): String {
        val assignment = if (hasMapPartner()) "$getter.to${
            findElementName(
                sourceClass,
                propertyName
            )
        }()" else getter
        return if (setter.isMethod())
            "it.${setter.substringBefore("(")}($assignment)"
        else
            "it.$setter = $assignment"
    }

    /** Determines if a string represents a method (ends with ()). */
    private fun String.isMethod() = endsWith("()")

    /** Checks if the source class has a [MapPartner] annotation. */
    private fun hasMapPartner() = mapPartner() != null

    /** Retrieves the [MapPartner] annotation from the source class. */
    private fun mapPartner() =
        findElementByName(sourceClass, sourceGetName)
            ?.asType()
            ?.let { types.asElement(it) }
            ?.getAnnotation(AMapPartner::class.java)

    /** Capitalizes the first letter of a string. */
    private fun String.capitalize() =
        replaceFirstChar { it.uppercase(Locale.getDefault()) }
}
