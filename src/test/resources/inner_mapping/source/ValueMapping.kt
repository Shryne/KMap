package inner_mapping.source

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x.value = x
    }