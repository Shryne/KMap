package inner_mapping.both

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(value = Value::class, packageName = "inner_mapping.target")
class Scalar {
    @KMap
    var x = Wrapper()
}