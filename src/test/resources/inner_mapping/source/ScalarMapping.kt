package inner_mapping.source

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x.value
    }