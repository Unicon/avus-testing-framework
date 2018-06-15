package net.unicon.avus.shibbolethidp.v3

import geb.Page
import geb.Module

/**
 *
 */
class AttributeReleasePage extends Page {

    static at = { title == "Information Release" }

    static content = {
        acceptButton(to: [SamlResponsePage]) { $("input[name='_eventId_proceed']") }
        attributes { $("table tbody tr").moduleList(AttributeRow)}
    }
}

class AttributeRow extends Module {
    static content = {
        attributeRow { $("td", it) }
        attributeName { attributeRow(0).text() }
        attributeValue { attributeRow(1).text() }
    }
}
