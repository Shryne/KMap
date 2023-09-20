package `object`.different_target_package

import com.shryne.kmap.mapping.toValue

public fun Size.toDimension(): Dimension =
    Dimension().also {
        it.width = w.toValue()
        it.height = h.toValue()
    }