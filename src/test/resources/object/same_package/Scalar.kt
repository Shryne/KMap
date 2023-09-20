package `object`.same_package

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(value = Value::class, packageName = "object.same_package")
class Scalar {
    @KMap
    var x: Int = 15
}