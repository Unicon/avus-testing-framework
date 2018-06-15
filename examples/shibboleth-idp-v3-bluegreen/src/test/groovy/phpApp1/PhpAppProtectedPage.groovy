package phpApp1

import geb.Module
import geb.Page

/**
 * The PHP App Protected page
 */
class PhpAppProtectedPage extends Page {

    static at = {
        title == "Protected"
    }

    static content = {
        attributes { $("table tr").tail().moduleList(AttributeRow)}
        uid {attributes.find { it.attributeName == '_SERVER["uid"]' }.attributeValue }
        mail {attributes.find { it.attributeName == '_SERVER["mail"]' }.attributeValue }
    }
}

class AttributeRow extends Module {
    static content = {
        attributeRow { $("td", it) }
        attributeName { attributeRow(0).text() }
        attributeValue { attributeRow(1).text() }
    }
}