package com.shryne.kmap.processor.check.mappartner

import com.shryne.kmap.annotations.MapPartner
import com.shryne.kmap.processor.MapPartnerProcessor
import com.shryne.kmap.processor.check.Check
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

/**
 * A check for the existence of the parameters of [MapPartner].
 */
internal class MapPartnerContainsParameter(
    private val annotated: Element,
    mp: MapPartnerProcessor.MP
) : Check {
    private val packageName: String? = mp.packageName

    override fun hasErrors(): Boolean = packageName == null

    override fun printErrors(messager: Messager) {
        messager.printMessage(
            Diagnostic.Kind.ERROR,
            "MapPartner doesn't contain the parameter 'packageName'.",
            annotated
        )
    }
}