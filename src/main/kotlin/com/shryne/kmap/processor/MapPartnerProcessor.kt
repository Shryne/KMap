package com.shryne.kmap.processor

import com.shryne.kmap.processor.kmap.KMap
import com.shryne.kmap.processor.kmap.check.KMapSettingCheck
import com.shryne.kmap.processor.kmap.check.PropertyExistCheck
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

typealias MapPartnerAnnotation = com.shryne.kmap.annotations.MapPartner
typealias KMapAnnotation = com.shryne.kmap.annotations.KMap

/**
 * Processor for [MapPartner].
 */
internal class MapPartnerProcessor : AbstractProcessor() {
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        roundEnv.getElementsAnnotatedWith(MapPartnerAnnotation::class.java)
            .forEach { source ->
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "Source type is: ${source.simpleName}."
                )

                val mapPartnerMirror = processingEnv
                    .elementUtils
                    .getTypeElement(MapPartnerAnnotation::class.java.name)

                for (annotation in source.annotationMirrors) {
                    if (annotation.annotationType.asElement() == mapPartnerMirror) {
                        for ((key, value) in annotation.elementValues) {
                            if (key.simpleName.toString() == "value") {
                                val target = (value.value as DeclaredType)
                                    .asElement() as TypeElement
                                val packageName = annotation.elementValues
                                    .filter {
                                        it.key.simpleName.toString() == "packageName"
                                    }
                                    .map { it.value.value.toString() }
                                    .firstOrNull()

                                if (packageName == null) {
                                    processingEnv.messager.printMessage(
                                        Diagnostic.Kind.ERROR,
                                        "MapPartner doesn't contain parameter packageName."
                                    )
                                } else {
                                    processingEnv.messager.printMessage(
                                        Diagnostic.Kind.NOTE,
                                        "Target type is: ${target.simpleName}. Source type is ${source.simpleName}."
                                    )

                                    val annotated = source.enclosedElements.filter {
                                        it.getAnnotation(KMapAnnotation::class.java) != null
                                    }
                                    val kMaps = annotated.map {
                                        KMap(
                                            it,
                                            source,
                                            target,
                                            processingEnv.typeUtils
                                        )
                                    }

                                    val checks = annotated.map {
                                        KMapSettingCheck(it)
                                    } + kMaps.map {
                                        PropertyExistCheck(
                                            it.sourceProperty,
                                            Clazz(target),
                                            it
                                        )
                                    }
                                    if (checks.any { it.hasErrors() }) {
                                        checks.forEach {
                                            if (it.hasErrors()) {
                                                it.printErrors(processingEnv.messager)
                                            }
                                        }
                                    } else {
                                        MapPartner(
                                            processingEnv.typeUtils,
                                            Clazz(source as TypeElement),
                                            Clazz(target),
                                            kMaps,
                                            packageName
                                        ).writeTo(processingEnv.filer)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        return false
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return mutableSetOf(MapPartnerAnnotation::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_17
    }
}
