package `object`.recursion

import com.shryne.kmap.annotations.KMap
import com.shryne.kmap.annotations.MapPartner

@MapPartner(Target::class, packageName = "`object`.recursion")
class Source {
    @KMap
    var rec: Source = Source()

    @KMap
    var x: Int = 0
}