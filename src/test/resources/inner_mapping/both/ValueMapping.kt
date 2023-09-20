package inner_mapping.both

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x.value = x.value
    }