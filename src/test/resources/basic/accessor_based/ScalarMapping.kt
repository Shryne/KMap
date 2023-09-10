package basic.accessor_based

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x
    }