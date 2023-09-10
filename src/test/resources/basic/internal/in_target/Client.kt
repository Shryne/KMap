package basic.internal.in_target

import com.shryne.kmap.annotations.MapPartner
import com.shryne.kmap.annotations.KMap

@MapPartner(User::class, packageName = "basic.internal.in_target")
class Client {
    @KMap("age.value")
    var age: Int = 0
}