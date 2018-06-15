package net.unicon.avus.casserver.v5

import geb.Page

/**
 * Login page error, specifically for invalid credentials
 */
class LoginErrorPage extends Page {

    static at = {
        title == "Login - CAS – Central Authentication Service"
        $(".form-error").text() == "Invalid credentials."
    }
}