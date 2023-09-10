package basic.internal.source_target

import com.shryne.kmap.annotations.MapPartner
import com.shryne.kmap.annotations.KMap

@MapPartner(User::class, packageName = "basic.internal.source_target")
class Client {
    @KMap(value = "age.value", thisValue = "age.value")
    var age = Wrapper()
}