package `object`.same_package

import com.shryne.kmap.map.KMap
import com.shryne.kmap.map.MapPartner

@MapPartner(Dimension::class, packageName = "object.same_package")
class Size {
    @KMap("width")
    var w = Scalar()

    @KMap("height")
    var h = Scalar()
}