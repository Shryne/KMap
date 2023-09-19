package basic.error.incompatible_types

import com.shryne.kmap.annotations.KMap
import com.shryne.kmap.annotations.MapPartner

@MapPartner(Target::class, packageName = "basic.error.incompatible_types")
class Source {
    @KMap
    var x: Int = 0
}