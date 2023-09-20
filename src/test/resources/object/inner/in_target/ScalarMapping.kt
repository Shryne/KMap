package `object`.`inner`.in_target

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x
    }