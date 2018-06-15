package net.unicon.avus.shibbolethidp.v3

import geb.Page

/**
 * Login page error, specifically for invalid credentials
 */
class LoginErrorPage extends Page {

    static at = {
        title == "Web Login Service"
        $(".form-error").text() == "The username you entered cannot be identified." || $(".form-error").text() == "The password you entered was incorrect."
    }
}