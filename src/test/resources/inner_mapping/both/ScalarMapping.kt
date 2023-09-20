package inner_mapping.both

public fun Scalar.toValue(): Value =
    Value().also {
        it.x.value = x.value
    }