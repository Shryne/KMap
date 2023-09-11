package com.shryne.kmap

import com.shryne.kmap.annotations.MapPartner
import com.shryne.kmap.processor.MapPartnerProcessor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File
import kotlin.test.assertContains

/**
 * The source file. That is the file that is currently processed and is
 * annotated with [MapPartner].
 */
interface Source {
    /**
     * The name of the source file.
     */
    val name: String

    /**
     * The full name of the file, including the format.
     */
    val fileName: String
}

/**
 * A Kotlin source file.
 */
class Kotlin(override val name: String) : Source {
    override val fileName = "$name.kt"
}

/**
 * A Java source file.
 */
class Java(override val name: String) : Source {
    override val fileName = "$name.java"
}

/**
 * Returns a file from the resources folder.
 *
 * @param path The path to the file.
 * @throws IllegalArgumentException If the file is not found.
 */
fun fileFromResources(path: String): File =
    File(
        Source::class.java.getResource(path)?.toURI()
            ?: throw IllegalArgumentException("Resource not found: $path.")
    )

/**
 * Returns the source code of a file from the resources folder.
 *
 * @param path The path to the file.
 * @throws IllegalArgumentException If the file is not found.
 * @see fileFromResources
 */
fun sourceCodeFromResources(path: String): String =
    fileFromResources(path).readText()

/**
 * Asserts that the generated mapping files are correct. This method will
 * compile the sources and assert that the generated mapping files are the same
 * as the expected mapping files. The expected mapping files are the files
 * located in the resources folder next to the source files.
 *
 * @param sourceFolder The folder where the source files are located.
 * @param sources The source files that are annotated with [MapPartner].
 * @param nonMappingSources The source files that are not annotated with
 * [MapPartner].
 */
fun assertMappingFiles(
    sourceFolder: String,
    vararg sources: Source,
    nonMappingSources: Iterable<Source> = emptyList(),
): Unit = assertMappingFiles(
    sourceFolder,
    listOf(*sources),
    nonMappingSources
)

/**
 * Asserts that the generated mapping files are correct. This method will
 * compile the sources and assert that the generated mapping files are the same
 * as the expected mapping files. The expected mapping files are the files
 * located in the resources folder next to the source files.
 *
 * @param sourceFolder The folder where the source files are located.
 * @param sources The source files that are annotated with [MapPartner].
 * @param nonMappingSources The source files that are not annotated with
 * [MapPartner].
 */
fun assertMappingFiles(
    sourceFolder: String,
    sources: Iterable<Source>,
    nonMappingSources: Iterable<Source> = emptyList(),
): Unit =
    assertMappingFiles(
        sourceFolder,
        sources.map { "${it.name}Mapping" },
        sources = sources,
        nonMappingSources
    )

/**
 * Asserts that the generated mapping files are correct. This method will
 * compile the sources and assert that the generated mapping files are the same
 * as the expected mapping files. The expected mapping files are the files
 * located in the resources folder next to the source files.
 *
 * @param sourceFolder The folder where the source files are located.
 * @param sourceMapFiles The names of the expected mapping files.
 * @param sources The source files that are annotated with [MapPartner].
 * @param nonMappingSources The source files that are not annotated with
 * [MapPartner].
 */
fun assertMappingFiles(
    sourceFolder: String,
    sourceMapFiles: Iterable<String>,
    vararg sources: Source,
    nonMappingSources: Iterable<Source> = emptyList(),
) = assertMappingFiles(
    sourceFolder,
    sourceMapFiles,
    listOf(*sources),
    nonMappingSources
)

/**
 * Asserts that the generated mapping files are correct. This method will
 * compile the sources and assert that the generated mapping files are the same
 * as the expected mapping files. The expected mapping files are the files
 * located in the resources folder next to the source files.
 *
 * @param sourceFolder The folder where the source files are located.
 * @param sourceMapFiles The names of the expected mapping files.
 * @param sources The source files that are annotated with [MapPartner].
 * @param nonMappingSources The source files that are not annotated with
 * [MapPartner]
 */
fun assertMappingFiles(
    sourceFolder: String,
    sourceMapFiles: Iterable<String>,
    sources: Iterable<Source>,
    nonMappingSources: Iterable<Source> = emptyList(),
) {
    val result = KotlinCompilation().also {
        it.sources = (sources + nonMappingSources).map {
            SourceFile.fromPath(
                fileFromResources("$sourceFolder/${it.fileName}")
            )
        }
        it.annotationProcessors = listOf(MapPartnerProcessor())
        it.inheritClassPath = true
        it.messageOutputStream = System.out
    }.compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

    sourceMapFiles.forEach { file ->
        println("File: $file.")
        println(result.sourcesGeneratedByAnnotationProcessor.map { it.name })
        println(result.messages)
        Assertions.assertThat(
            result.sourcesGeneratedByAnnotationProcessor.find {
                it.name == "$file.kt"
            }?.readText() ?: throw IllegalStateException(
                "Generated $file.kt couldn't be found."
            )
        ).containsIgnoringWhitespaces(
            sourceCodeFromResources("$sourceFolder/$file.kt")
        )
    }

    sourceMapFiles.forEach { file ->
        result.sourcesGeneratedByAnnotationProcessor.find {
            it.name == "$file.kt"
        }?.let { sourceFile ->
            Assertions.assertThat(
                sourceFile.readText())
                .containsIgnoringWhitespaces(
                    sourceCodeFromResources("$sourceFolder/$file.kt")
                )
        } ?: throw IllegalStateException(
            "$file couldn't be found."
        )
    }
}

/**
 * Asserts that the generated mapping files are correct. This method will
 * compile the sources and assert that the generated mapping files are the same
 * as the expected mapping files. The expected mapping files are the files
 * located in the resources folder next to the source files.
 *
 * @param sourceFolder The folder where the source files are located.
 * @param sourceMapFiles The names of the expected mapping files.
 * @param sources The source files that are annotated with [MapPartner].
 * @param nonMappingSources The source files that are not annotated with
 * [MapPartner]
 */
fun assertError(
    sourceFolder: String,
    sources: Iterable<Source>,
    nonMappingSources: Iterable<Source> = emptyList(),
    errorMessage: String
) {
    val result = KotlinCompilation().also {
        it.sources = (sources + nonMappingSources).map {
            SourceFile.fromPath(
                fileFromResources("$sourceFolder/${it.fileName}")
            )
        }
        it.annotationProcessors = listOf(MapPartnerProcessor())
        it.inheritClassPath = true
        it.messageOutputStream = System.out
    }.compile()

    assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
    assertContains(result.messages, errorMessage)
}
