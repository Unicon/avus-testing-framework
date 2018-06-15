package net.unicon.avus.shibbolethidp.v3

import geb.Page

/**
 * OpenSAML intermediate POST page
 */
class SamlResponsePage extends Page {

    static at = {
        title == ""
        $("input[name='SAMLResponse']")
    }

    static content = {
        samlResponse { $("input[name='SAMLResponse']").@value }
        postUrl { $("form").@action }
    }
}
