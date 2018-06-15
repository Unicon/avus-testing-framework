import net.unicon.avus.AnyPage
import net.unicon.avus.TestData
import net.unicon.avus.casserver.v5.LoginPage
import net.unicon.avus.protocols.Cas3Spec

class TestCas3Protocol extends Cas3Spec {
    void setup() {
        ignoreCertificateIssuesOnValidate = true
    }

    def "login (CAS 3 Protocol)"() {
        when: "redirected from CAS Client"
        go DummyClientService

        and: "I authenticate with a good account"
        at LoginPage

        username = config.rawConfig.accountUsername
        password = config.rawConfig.accountPassword
        loginButton.click()

        then: "I get a fake CAS client callback page"
        at AnyPage

        then: "Validate the Service Ticket"
        response DummyClientService
    }


    class DummyClientService extends TestData {

        static initiator = {
            serviceId = "http://localhost/dummyapp/authn"
        }

        static validator = {
            principal.name == "casuser"
            attributes.size() == 0
        }
    }
}

