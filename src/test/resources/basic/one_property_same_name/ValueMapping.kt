package basic.one_property_same_name

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x
    }