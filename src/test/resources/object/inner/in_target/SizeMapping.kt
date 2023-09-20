package `object`.`inner`.in_target

public fun Size.toDimension(): Dimension =
    Dimension().also {
        it.width.value = w.toValue()
        it.height = h.toValue()
    }