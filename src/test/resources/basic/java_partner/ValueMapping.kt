package basic.java_partner

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x
    }