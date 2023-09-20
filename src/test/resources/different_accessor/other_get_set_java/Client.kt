package different_accessor.other_get_set_java

import com.shryne.kmap.map.MapPartner
import com.shryne.kmap.map.KMap

@MapPartner(
    User::class,
    packageName = "different_accessor.other_get_set_java"
)
class Client {
    @KMap(targetGet = "theAge()", targetSet = "setAge()")
    var age: Int = 0

    @KMap
    var name: String = ""
}