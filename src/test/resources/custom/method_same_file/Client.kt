package custom.method_in_class

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap
import com.shryne.kmap.map.MapCustom
import kotlin.math.absoluteValue

@MapPartner(
    User::class,
    packageName = "custom.method_in_class"
)
class Client {
    @KMap
    @MapCustom("abs")
    var age: Int = 0
}

fun abs(value: Int) = value.absoluteValue