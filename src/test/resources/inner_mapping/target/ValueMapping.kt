package inner_mapping.target

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x.value
    }