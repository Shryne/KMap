package `object`.inner.in_target

import com.shryne.kmap.map.KMap
import com.shryne.kmap.map.MapPartner

@MapPartner(Dimension::class, packageName = "object.inner.in_target")
class Size {
    @KMap("width.value")
    var w = Scalar()

    @KMap("height")
    var h = Scalar()
}