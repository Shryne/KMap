package basic.accessor_based

import com.shryne.kmap.annotations.MapPartner
import com.shryne.kmap.annotations.KMap

@MapPartner(value = Value::class, packageName = "basic.accessor_based")
class Scalar {
    @KMap
    var x: Int = 15
}