package com.shryne.kmap.processor

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

typealias MapPartnerAnnotation = com.shryne.kmap.annotations.MapPartner
typealias KMapAnnotation = com.shryne.kmap.annotations.KMap

/**
 * Processor for [MapPartner].
 */
class MapPartnerProcessor : AbstractProcessor() {
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
                                    MapPartner(
                                        processingEnv.typeUtils,
                                        Clazz(source as TypeElement),
                                        Clazz(target),
                                        source.enclosedElements.filter {
                                            it.getAnnotation(KMapAnnotation::class.java) != null
                                        }.map {
                                            KMap(
                                                it,
                                                source,
                                                target,
                                                processingEnv.typeUtils,
                                                processingEnv.messager
                                            )
                                        },
                                        packageName,
                                        processingEnv.messager
                                    ).writeTo(processingEnv.filer)
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
        return SourceVersion.RELEASE_15
    }
}
