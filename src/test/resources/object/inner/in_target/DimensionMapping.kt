package `object`.`inner`.in_target

public fun Dimension.toSize(): Size =
    Size().also {
        it.w = width.value.toScalar()
        it.h = height.toScalar()
    }