package `object`.same_package

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x
    }