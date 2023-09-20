package `object`.`inner`.in_target

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x
    }