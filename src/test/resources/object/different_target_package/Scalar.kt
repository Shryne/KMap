package `object`.different_target_package

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(value = Value::class)
class Scalar {
    @KMap
    var x: Int = 15
}