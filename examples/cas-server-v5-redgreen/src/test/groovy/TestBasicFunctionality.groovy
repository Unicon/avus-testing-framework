import net.unicon.avus.TestData
import net.unicon.avus.protocols.Cas2Spec
import net.unicon.avus.casserver.v5.*

class TestBasicFunctionality extends Cas2Spec {

    def "check CAS Server login page"() {
        when: "redirected from CAS Client"
        go getAuthServerEndpoint() + "/login"

        then: "I get the Login page"
        at LoginPage
    }

    def "connect to non-authorized service"() {
        when: "redirected from CAS Client"
        go BadService

        then: "I get the Service Not Authorized page"
        at ServiceNotAuthorizedPage
    }

    class BadService extends TestData {

        static initiator = {
            serviceId = "http://google.com"
        }
    }

    def "login and logout"() {
        when: "redirected from CAS Client"
        go getAuthServerEndpoint() + "/login"

        and: "I get the Login page"
        at LoginPage

        username = config.rawConfig.accountUsername
        password = config.rawConfig.accountPassword
        loginButton.click()

        then: "I hit the generic success login page"
        at LoginSuccessPage
        username == "casuser"

        when: "I logout"
        go getAuthServerEndpoint() + "/logout"

        then:
        at LogoutPage
    }
}

