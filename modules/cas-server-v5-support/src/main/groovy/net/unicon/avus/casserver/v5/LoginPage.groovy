package net.unicon.avus.casserver.v5

import geb.Page
import net.unicon.avus.AnyPage

/**
 * The default CAS Login page
 */
class LoginPage extends Page {

    static at = {
        title == "Login - CAS – Central Authentication Service"
    }

    static content = {
        username { $("input[name='username']") }
        password { $("input[name='password']") }
        loginButton(to: [LoginSuccessPage, LoginErrorPage, AnyPage]) { $("input[name='submit']") }
    }
}
