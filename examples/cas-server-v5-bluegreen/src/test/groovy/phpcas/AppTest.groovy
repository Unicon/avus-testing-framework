package phpcas

import net.unicon.avus.AvusSpec

class AppTest extends AvusSpec {

    def "login to phpCAS test app"() {
        when: "I go to the application"
        go "http://localhost/php-cas/"

        then:
        at CasLoginPage

        when: "I authenticate with an account"
        username = config.rawConfig.accountUsername
        password = config.rawConfig.accountPassword
        loginButton.click()

        then: "I get the main page"
        at PhpCasMainPage
        username == "casuser"
    }
}


