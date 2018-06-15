package phpcas

import geb.Page

/**
 * The phpCAS Test App main page
 */
class PhpCasMainPage extends Page {

    static at = {
        title == "phpCAS simple client"
    }

    static content = {
        username { $("p").find("b")[0].text() }
    }
}
