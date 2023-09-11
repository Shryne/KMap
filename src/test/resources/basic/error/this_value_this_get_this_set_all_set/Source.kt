package basic.error.this_value_this_get_this_set_all_set

import com.shryne.kmap.annotations.KMap
import com.shryne.kmap.annotations.MapPartner

@MapPartner(Target::class, packageName = "basic.error.this_value_this_get_this_set_all_set")
class Source {
    @KMap(thisValue = "x", thisGet = "x", thisSet = "x")
    var x: Int = 0
}