package com.shryne.kmap.processor

import com.shryne.kmap.processor.kmap.KMap
import com.shryne.kmap.processor.kmap.check.Checks
import com.shryne.kmap.processor.kmap.check.KMapSettingCheck
import com.shryne.kmap.processor.kmap.check.PropertyExistCheck
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
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

                val mp = MP(source, processingEnv)

                val target = mp.target
                val packageName = mp.packageName

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

                    val annotated =
                        source.enclosedElements.filter {
                            it.getAnnotation(
                                KMapAnnotation::class.java
                            ) != null
                        }

                    val kMaps = annotated.map {
                        KMap(
                            it,
                            source,
                            target,
                            processingEnv.typeUtils
                        )
                    }

                    val checks = Checks(
                        kMaps.flatMap {
                            listOf(
                                KMapSettingCheck(
                                    it.sourceProperty
                                ),
                                PropertyExistCheck(
                                    it,
                                    Clazz(target)
                                )
                            )
                        }
                    )
                    if (checks.hasErrors()) {
                        checks.printErrors(
                            processingEnv.messager
                        )
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
        return false
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return mutableSetOf(MapPartnerAnnotation::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_17
    }

    class MP(annotated: Element, processingEnv: ProcessingEnvironment) {
        val target = annotated.annotationMirrors
            .filter {
                it.annotationType.asElement().simpleName.toString() == "MapPartner"
            }
            .map {
                it.elementValues
                    .filter {
                        it.key.simpleName.toString() == "value"
                    }
                    .map {
                        it.value.value as DeclaredType
                    }
                    .map {
                        it.asElement()
                    }
                    .first()
            }
            .first() as TypeElement

        val packageName: String? = annotated.annotationMirrors
            .filter {
                it.annotationType.asElement().simpleName.toString() == "MapPartner"
            }
            .map {
                it.elementValues
                    .filter {
                        it.key.simpleName.toString() == "packageName"
                    }
                    .map {
                        it.value.value.toString()
                    }
                    .firstOrNull()
            }
            .firstOrNull()
    }
}
