package com.shryne.kmap.mapping

import `object`.different_target_package.Scalar
import `object`.different_target_package.Value

public fun Value.toScalar(): Scalar =
    Scalar().also {
        it.x = x
    }