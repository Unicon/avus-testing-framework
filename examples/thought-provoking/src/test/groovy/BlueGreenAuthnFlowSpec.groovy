import net.unicon.avus.protocols.SamlSpec
import net.unicon.avus.shibbolethidp.v3.*

class BlueGreenAuthnFlowSpec extends SamlSpec {
    void setup() {
        driver.webClient.cookieManager.clearCookies()
    }

    def "login account failure - real sp"() {
        when: "redirected from SP"
        go "https://grouperdemo.internet2.edu/Shibboleth.sso/Login?target=https%3A%2F%2Fgrouperdemo.internet2.edu%2Fgrouper_v2_3%2F&entityID=https%3A%2F%2Fidp.unicon.net%2Fidp%2Fshibboleth"
        page LoginPage

        and: "I authenticate with a bad password"
        username = "jsmith"
        password = "test"
        loginButton.click()

        then: "I get the login error page"
        at LoginErrorPage
    }

    def "login account failure - data set"() {
        when: "redirected from SP"
        go "https://grouperdemo.internet2.edu/Shibboleth.sso/Login?target=https%3A%2F%2Fgrouperdemo.internet2.edu%2Fgrouper_v2_3%2F&entityID=https%3A%2F%2Fidp.unicon.net%2Fidp%2Fshibboleth"
        at LoginPage

        and: "I authenticate with a bad username or password"
        username = testUsername
        password = "test"
        loginButton.click()

        then: "I get the login error page"
        at LoginErrorPage

        where:
        testUsername | testPassword | _
        "jsmith" | "test" | _
        "jgasper" | "myrealpassword" | _
    }

    def "end to end test - real sp"() {
        when: "redirected from `real` SP"
        go "http://localhost/php-shib-protected/"
        at LoginPage

        and: "I authenticate with a good password"
        username = "jsmith"
        password = "password"
        loginButton.click()

        then: "I get the attribute release page"
        at AttributeReleasePage

        when: "I accept the attribute release"
        at AttributeReleasePage
        acceptButton.click()

        then: "I get redirected to my application page."
        at phpShibProtectedPage
        username == "jsmith"
    }

}

