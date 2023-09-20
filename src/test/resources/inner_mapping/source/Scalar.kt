package inner_mapping.source

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(value = Value::class, packageName = "inner_mapping.target")
class Scalar {
    @KMap(thisGet = "x.value", thisSet = "x.value")
    var x = Wrapper()
}