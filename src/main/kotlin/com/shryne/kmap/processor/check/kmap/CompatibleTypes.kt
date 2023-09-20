package com.shryne.kmap.processor.check.kmap

import com.shryne.kmap.processor.KMap
import com.shryne.kmap.processor.check.Check
import javax.annotation.processing.Messager
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Types
import javax.tools.Diagnostic

/**
 * Checks if the parameters that are mapped with [KMap] have compatible types.
 * Currently, if a type doesn't exactly match the other, it is considered
 * incompatible.
 *
 * Note: This check is tricky, because Kotlin saves annotations not on the
 * actual property but instead generates a static method for that. Example:
 * ```kotlin
 * @MapPartner(B::class, packageName = "somepackage"))
 * class A {
 *   @KMap
 *   private var x: Int = 0
 * }
 * ```
 * The translated byte code looks like this:
 * ```java
 * @kotlin.Metadata(...)
 * @MapPartner(value = B.class, packageName = "basic")
 * public final class Source {
 *   private int x: Int = 0;
 *
 *   public final int getX(): int { return x; }
 *   public final void setX(int x) { this.x = x; }
 *
 *   @com.shryne.kmap.annotations.KMap
 *   @java.lang.Deprecated
 *   public static void getX$annotations() {}
 *
 *   // ...
 * }
 * ```
 * `getX$annotations` is a static method that holds the annotation.
 */
internal class CompatibleTypes(
    private val kMap: KMap,
    private val typeUtils: Types
) : Check {
    private val sourceType = kMap.sourceProperty?.asType()
    private val targetType = kMap.targetProperty?.asType()

    override fun hasErrors(): Boolean = !areTypesSame(sourceType, targetType)

    private fun areTypesSame(type1: TypeMirror?, type2: TypeMirror?): Boolean {
        // The type can currently be null, if it targets an accessor.
        if (type1 == null || type2 == null) return true
        if (typeUtils.isSameType(type1, type2)) return true

        // Check if the raw (erased) types are the same
        if (typeUtils.isSameType(typeUtils.erasure(type1), typeUtils.erasure(type2))) {
            // Check if both types are DeclaredTypes (i.e., they might have generics)
            if (type1 is DeclaredType && type2 is DeclaredType) {
                val typeArgs1 = type1.typeArguments
                val typeArgs2 = type2.typeArguments

                // If their type arguments have different sizes,
                // they can't be the same
                if (typeArgs1.size != typeArgs2.size) return false

                // Recursively check the type arguments
                for (i in typeArgs1.indices) {
                    if (!areTypesSame(typeArgs1[i], typeArgs2[i])) return false
                }
            }
        }
        return false
    }

    override fun printErrors(messager: Messager) {
        messager.printMessage(
            Diagnostic.Kind.ERROR,
            "The types of the properties that are mapped with KMap must be " +
                "compatible. The source type is $sourceType and the target " +
                "type is $targetType.",
            kMap.annotated
        )
    }
}