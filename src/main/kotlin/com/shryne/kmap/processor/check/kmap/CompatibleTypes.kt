package com.shryne.kmap.processor.check.kmap

import com.shryne.kmap.processor.KMap
import com.shryne.kmap.processor.check.Check
import javax.annotation.processing.Messager
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
internal class CompatibleTypes(private val kMap: KMap) : Check {
    private val sourceType = kMap.sourceProperty?.asType()
    private val targetType = kMap.targetProperty?.asType()

    override fun hasErrors(): Boolean = sourceType != targetType

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