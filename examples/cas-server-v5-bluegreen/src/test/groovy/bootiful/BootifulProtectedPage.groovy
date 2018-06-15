package bootiful

import net.unicon.avus.AnyPage

/**
 * The Protected Bootiful CAS Client page
 */
class BootifulProtectedPage extends AnyPage {

    static at = {
        title == "Protected area"
    }

    static content = {
        username { $("h2").find("span").text() }
        text { $("div .starter-template").find("h2").text() }
    }
}
