package basic.one_property_same_name

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x
    }