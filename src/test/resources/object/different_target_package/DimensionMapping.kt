package `object`.different_target_package

import com.shryne.kmap.mapping.toScalar

public fun Dimension.toSize(): Size =
    Size().also {
        it.w = width.toScalar()
        it.h = height.toScalar()
    }