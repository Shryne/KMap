package `object`.recursion

fun Source.toTarget(): Target = Target().also {
    it.rec = this.rec.toTarget()
    it.x = this.x
}
