package `object`.same_package

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x
    }