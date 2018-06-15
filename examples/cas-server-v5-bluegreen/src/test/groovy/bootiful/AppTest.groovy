package bootiful

import net.unicon.avus.AvusSpec

class AppTest extends AvusSpec {

    def "login to the CAS Bootiful App"() {
        when: "I go to the application"
        go "https://localhost:8443/"
        at BootifulMainPage

        and: "I click the protected link"
        protectedLink.click()

        then:
        at CasLoginPage

        when: "I authenticate with an account"
        username = config.rawConfig.accountUsername
        password = config.rawConfig.accountPassword
        loginButton.click()

        then: "I get the protected page"
        at BootifulProtectedPage
        username == config.rawConfig.accountUsername
        text.contains("You are in protected area.")
    }
}


