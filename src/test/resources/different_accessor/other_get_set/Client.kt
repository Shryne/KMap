package different_accessor.other_get_set

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(
    User::class,
    packageName = "different_accessor.other_get_set"
)
class Client {
    @KMap(targetGet = "theAge()")
    var age: Int = 0

    @KMap
    var name: String = ""
}