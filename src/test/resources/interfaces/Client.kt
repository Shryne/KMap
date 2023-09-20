package interfaces

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap
import kotlin.math.absoluteValue

@MapPartner(
    User::class,
    packageName = "interfaces"
)
class Client : Id {
    @KMap
    var age: Int = 0

    override var id: Int = 0
}

fun abs(value: Int) = value.absoluteValue