package constructor.same_name

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(value = Value::class, packageName = "basic.constructor")
class Scalar(@KMap var x: Int)