import net.unicon.avus.casserver.v5.LoginPage

/**
 * The Heroku CAS Login page changes often, so this may or may not match the base class title.
 */
class CasLoginPage extends LoginPage {

    static at = {
        title == "Login - CAS – Central Authentication Service"
    }
}
