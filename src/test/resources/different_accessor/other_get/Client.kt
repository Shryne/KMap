package different_accessor.other_get

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(
    User::class,
    packageName = "different_accessor.other_get"
)
class Client {
    @KMap(targetGet = "theAge()", targetSet = "theAge()")
    var age: Int = 0

    @KMap
    var name: String = ""
}