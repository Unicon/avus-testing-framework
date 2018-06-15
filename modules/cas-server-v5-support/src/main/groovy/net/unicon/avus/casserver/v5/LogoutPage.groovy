package net.unicon.avus.casserver.v5

import geb.Page

/**
 * The default CAS Logout page
 */
class LogoutPage extends Page {

    static at = {
        title == "Logout successful - CAS – Central Authentication Service"
        $("h2").text().contains("Logout successful")
    }
}
