package com.shryne.kmap.annotations

/**
 * Defines the mapping target of the annotated property. Note that this
 * annotation distinguishes between methods and properties. Example:
 * `@KMap("x")" => property named x. `@KMap("x()")` getter and setter method
 * named x.
 *
 * Note that a getter method is expected to have no parameters and the return
 * type must be compatible to the type of the annotated property. A setter
 * must take one parameter and the type of the parameter must be compatible to
 * the type of the annotated property.
 *
 * Note that [value], [targetGet] or [targetSet] must be unset.
 */
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.SOURCE)
annotation class KMap(
    /**
     * The name of the property to be mapped with. The default value "" is used
     * when the annotated and the target of the mapping have the same name.
     */
    val value: String = "",

    /**
     * The getter name of the target. Example:
     * ```kotlin
     * @MapPartner(Target::class)
     * class Source {
     *     @KMap(targetGet = "y()") var x: Int = 0
     * }
     *
     * class Target {
     *     var x: Int = 0
     *
     *     fun y() = 20
     * }
     * ```
     */
    val targetGet: String = "",

    /**
     * The setter name of the target. Example:
     * ```kotlin
     * @MapPartner(Target::class)
     * class Source {
     *     @KMap(targetSet = "y()") var x: Int = 0
     * }
     *
     * class Target {
     *     var x: Int = 0
     *
     *     fun y(value: Int) { }
     * }
     * ```
     */
    val targetSet: String = "",

    /**
     * The name of the property in the annotated (source) class. The default
     * value "" is used when the annotated and the target of the mapping have
     * the same name. This value is useful when the actual property is not
     * annotated.
     */
    val thisValue: String = "",

    /**
     * The getter name of the property in the annotated (source) class. The
     * default value "" is used when the annotated and the target of the
     * mapping have the same name. This value is useful to access a method
     * or another property for the value.
     */
    val thisGet: String = "",

    /**
     * The setter name of the property in the annotated (source) class. The
     * default value "" is used when the annotated and the target of the
     * mapping have the same name. This value is useful to use a separate method
     * or property to set the value.
     */
    val thisSet: String = "",
)
