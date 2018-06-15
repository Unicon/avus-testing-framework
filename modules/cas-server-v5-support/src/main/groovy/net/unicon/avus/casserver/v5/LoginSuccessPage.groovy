package net.unicon.avus.casserver.v5

import geb.Page

/**
 * The generic CAS Successful Login page
 */
class LoginSuccessPage extends Page {

    static at = {
        title == "Log In Successful - CAS – Central Authentication Service"
    }

    static content = {
        username { $("p").find("strong").text() }
    }
}
