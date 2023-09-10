package basic.internal.in_source

import com.shryne.kmap.annotations.MapPartner
import com.shryne.kmap.annotations.KMap

@MapPartner(User::class, packageName = "basic.internal.in_source")
class Client {
    @KMap(thisValue = "age.value")
    var age = Wrapper()
}