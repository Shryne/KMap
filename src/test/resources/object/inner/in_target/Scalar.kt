package `object`.inner.in_target

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(value = Value::class, packageName = "object.inner.in_target")
class Scalar {
    @KMap
    var x: Int = 15
}