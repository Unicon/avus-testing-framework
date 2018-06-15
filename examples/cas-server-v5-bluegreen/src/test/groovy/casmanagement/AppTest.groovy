package casmanagement

import net.unicon.avus.AvusSpec

class AppTest extends AvusSpec {

    def "login to the CAS Management App"() {
        when: "I go to the application"
        go "https://casservermgmt.herokuapp.com/cas-management/manage.html"

        then:
        at CasLoginPage

        when: "I authenticate with an account"
        username = config.rawConfig.accountUsername
        password = config.rawConfig.accountPassword
        loginButton.click()

        then: "I get the main page"
        at CasManagementMainPage
        text.contains("Loading...")
    }
}


