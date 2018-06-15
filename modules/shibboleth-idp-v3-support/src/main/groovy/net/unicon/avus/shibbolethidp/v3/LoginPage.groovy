package net.unicon.avus.shibbolethidp.v3

import geb.Page
import net.unicon.avus.AnyPage

/**
 * The default Shibboleth Login page
 */
class LoginPage extends Page {

    static at = {
        title == "Web Login Service"
    }

    static content = {
        username { $("input[name='j_username']") }
        password { $("input[name='j_password']") }
        loginButton(to: [LoginErrorPage, SamlResponsePage, AttributeReleasePage, AnyPage]) { $("button[name='_eventId_proceed']") }
    }
}
