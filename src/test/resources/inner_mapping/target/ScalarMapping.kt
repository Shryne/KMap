package inner_mapping.target

public fun Scalar.toValue(): Value =
    Value().also {
        it.x.value = x
    }