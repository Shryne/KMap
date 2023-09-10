package basic.java_partner

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x
    }