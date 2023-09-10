package com.shryne.kmap.processor

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.element.TypeElement

/**
 * The method that is going to do the mapping. It will look similar to this:
 * ```
 * fun <source>.to<target>(): <target> =
 *      <target>().also {
 *          it.<mappings.first> = it.<mappings.second>
 *          ...
 *      }
 * ```
 * @param source The class that is going to be mapped to the target.
 * @param target The class that is the target of the mapping.
 * @param statements The statements of the mapping.
 * Example: `it.firstName = firstName`
 */
class MapFunction(
    private val source: Clazz,
    private val target: Clazz,
    private val statements: Iterable<String>,
) {
    fun asFun(): FunSpec =
        FunSpec.builder("to${target.simpleName}")
            .receiver(source.name)
            .returns(target.name)
            .beginControlFlow("return %T().also", target.name)
            .apply {
                statements.forEach(::addStatement)
            }
            .endControlFlow()
            .build()
}