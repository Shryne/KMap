package basic.java_partner

import com.shryne.kmap.annotations.MapPartner
import com.shryne.kmap.annotations.KMap

@MapPartner(Value::class, packageName = "basic.java_partner")
class Scalar {
    @KMap
    var x: Int = 15
}