package `object`.same_package

public fun Size.toDimension(): Dimension =
    Dimension().also {
        it.width = w.toValue()
        it.height = h.toValue()
    }