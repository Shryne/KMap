package basic.error.no_such_property

import com.shryne.kmap.annotations.KMap
import com.shryne.kmap.annotations.MapPartner

@MapPartner(Target::class, "basic.error.no_such_property")
class Source {
    @KMap
    var x: Int = 0
}