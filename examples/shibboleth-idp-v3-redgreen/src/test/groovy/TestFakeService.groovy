import net.unicon.avus.AnyPage
import net.unicon.avus.TestData
import net.unicon.avus.protocols.Saml2Spec
import net.unicon.avus.shibbolethidp.v3.LoginPage
import net.unicon.avus.shibbolethidp.v3.SamlResponsePage
import org.joda.time.DateTime
import org.opensaml.saml.common.SAMLVersion
import org.opensaml.saml.saml2.core.impl.IssuerBuilder
import org.opensaml.saml.saml2.core.impl.NameIDPolicyBuilder

class TestFakeService extends Saml2Spec {

    def "login to Fake service"() {
        when: "redirected from the fake service"
        go FakeService

        and: "I authenticate with a good account"
        at LoginPage

        username = config.rawConfig.accountUsername
        password = config.rawConfig.accountPassword
        loginButton.click()

        then: "I get the blank SAML response page"
        at SamlResponsePage

        and: "Validate the SAML Authn Response"
        response FakeService
    }

    class FakeService extends TestData {
        static initiator = {
            assertionConsumerServiceURL = "https://localhost/Shibboleth.sso/SAML2/POST"
            destination = "https://localhost:4443/idp/profile/SAML2/Redirect/SSO"
            ID = "_" + UUID.randomUUID().toString()
            issueInstant = DateTime.now()
            protocolBinding = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
            version = SAMLVersion.VERSION_20

            issuer = IssuerBuilder.newInstance().buildObject()
            issuer.setValue("https://fakesp/shibboleth")

            nameIDPolicy = NameIDPolicyBuilder.newInstance().buildObject()
            nameIDPolicy.setAllowCreate(true)

        }
        
        static validator = {
            destination == "https://localhost/Shibboleth.sso/SAML2/POST"
            status.statusCode.value == "urn:oasis:names:tc:SAML:2.0:status:Success"
            signature != null //todo
            encryptedAssertions.size() == 0
            issuer.value == "https://idptestbed/idp/shibboleth"
            assertions.size() == 1
            assertions[0].issuer.value == "https://idptestbed/idp/shibboleth"
            //assertions[0].subject.nameID.value == "<sometransientvalue>"
            assertions[0].subject.nameID.nameQualifier == "https://idptestbed/idp/shibboleth"
            assertions[0].subject.nameID.SPNameQualifier == "https://fakesp/shibboleth"
            assertions[0].subject.nameID.format == "urn:oasis:names:tc:SAML:2.0:nameid-format:transient"

            assertions[0].statements.size() == 2
            assertions[0].statements[0].authnContext.authnContextClassRef.authnContextClassRef == "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport"
            assertions[0].statements[1].indexedChildren.size() == 2

            def child = assertions[0].statements[1].indexedChildren.find { it.name == "urn:oid:1.3.6.1.4.1.5923.1.1.1.6" }
            child.friendlyName == "eduPersonPrincipalName"
            child.attributeValues.size() == 1
            child.attributeValues[0].textContent == "jgasper@example.edu"

            def child2 = assertions[0].statements[1].indexedChildren.find { it.name == "urn:oid:0.9.2342.19200300.100.1.3" }
            child2.friendlyName == "mail"
            child2.attributeValues.size() == 1
            child2.attributeValues[0].textContent == "jgasper@example.edu"
        }
    }
}
