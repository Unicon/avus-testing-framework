package bootiful

import geb.Page

/**
 * The Main Bootiful CAS Client page
 */
class BootifulMainPage extends Page {

    static at = {
        title == "Bootiful CAS client sample app"
    }

    static content = {
        protectedLink { $("a", text: "Protected Area") }
    }
}
