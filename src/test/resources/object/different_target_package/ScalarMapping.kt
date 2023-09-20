package com.shryne.kmap.mapping

import `object`.different_target_package.Scalar
import `object`.different_target_package.Value

public fun Scalar.toValue(): Value =
    Value().also {
        it.x = x
    }