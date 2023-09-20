package inner_mapping.target

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(value = Value::class, packageName = "inner_mapping.target")
class Scalar {
    @KMap("x.value")
    var x: Int = 15
}