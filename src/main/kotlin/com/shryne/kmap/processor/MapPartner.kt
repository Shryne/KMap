package com.shryne.kmap.processor

import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.Filer
import javax.lang.model.util.Types

/**
 *
 */
internal class MapPartner(
    private val types: Types,
    private val source: Clazz,
    private val target: Clazz,
    private val kMaps: List<KMap>,
    private val packageName: String
) {
    fun writeTo(filer: Filer) {
        writeTo(
            kMaps.filter { it.sourceToTargetImport != null }
                .map { it.sourceToTargetImport!! },
            kMaps.map { it.sourceToTargetAssignment() },
            (if (source.simpleName == target.simpleName) "S" else "") + "${source.simpleName}Mapping",
            filer,
            source,
            target
        )
        writeTo(
            kMaps.filter { it.targetToSourceImport != null }
                .map { it.targetToSourceImport!! },
            kMaps.map { it.targetToSourceAssignment() },
            (if (source.simpleName == target.simpleName) "S" else "") + "${target.simpleName}Mapping",
            filer,
            target,
            source

        )
    }

    private fun writeTo(
        additionalImports: Iterable<Pair<String, String>>,
        statements: Iterable<String>,
        fileName: String,
        filer: Filer,
        source: Clazz,
        target: Clazz
    ) {
        FileSpec.builder(packageName, fileName)
            .apply {
                additionalImports.forEach {
                    if (it.first != packageName) {
                        addImport(it.first, it.second)
                    }
                }
            }
            .addFunction(
                MapFunction(source, target, statements).asFun()
            ).build().apply {
                writeTo(System.out)
                writeTo(filer)
            }
    }
}