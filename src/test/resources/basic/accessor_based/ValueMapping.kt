package basic.accessor_based

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x
    }