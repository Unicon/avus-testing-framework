package phpApp1

import net.unicon.avus.AvusSpec
import net.unicon.avus.shibbolethidp.v3.LoginPage

class AppTest extends AvusSpec {

    def "login to phpApp1"() {
        when: "I go to the application"
        go "http://localhost/php-shib-protected"

        then: "I'm redirected to the Shib Login page"
        at LoginPage

        when: "I authenticate with an account"
        username = config.rawConfig.accountUsername
        password = config.rawConfig.accountPassword
        loginButton.click()

        then: "I get the protected page"
        at PhpAppProtectedPage
        uid == config.rawConfig.accountUsername
        mail.contains("jgasper@")
    }
}


