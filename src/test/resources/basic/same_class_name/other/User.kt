package basic.same_name.other

import basic.same_name.User
import com.shryne.kmap.annotations.MapPartner
import com.shryne.kmap.annotations.KMap

@MapPartner(User::class, packageName = "basic.same_name")
class User {
    @KMap
    var age: Int = 0

    @KMap
    var name: String = ""

    @KMap
    var hobbies: List<String> = listOf()
}