import net.unicon.avus.AnyPage
import net.unicon.avus.TestData
import net.unicon.avus.protocols.Saml2Spec
import net.unicon.avus.shibbolethidp.v3.AttributeReleasePage
import net.unicon.avus.shibbolethidp.v3.LoginPage
import net.unicon.avus.shibbolethidp.v3.SamlResponsePage
import org.joda.time.DateTime
import org.opensaml.saml.common.SAMLVersion
import org.opensaml.saml.saml2.core.impl.IssuerBuilder
import org.opensaml.saml.saml2.core.impl.NameIDPolicyBuilder

class TestAnotherDemoService extends Saml2Spec {

    def "login to Another Demo service"() {
        when: "redirected from Another Demo service"
        go AnotherDemoService

        and: "I authenticate with a good account"
        at LoginPage

        username = config.rawConfig.accountUsername
        password = config.rawConfig.accountPassword
        loginButton.click()

        then: "I get the IdP Attribute Release page"
        at AttributeReleasePage

        when: "I accept the attribute release request"
        acceptButton.click()

        then: "I get the blank SAML response page"
        at SamlResponsePage

        and: "Validate the SAML Authn Response"
        response AnotherDemoService
    }

    class AnotherDemoService extends TestData {
        static initiator = {
            assertionConsumerServiceURL = "https://localhost/Shibboleth.sso/SAML2/POST"
            destination = "https://localhost:4443/idp/profile/SAML2/Redirect/SSO"
            ID = "_" + UUID.randomUUID().toString()
            issueInstant = DateTime.now()
            protocolBinding = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
            version = SAMLVersion.VERSION_20

            issuer = IssuerBuilder.newInstance().buildObject()
            issuer.setValue("https://anothersp/shibboleth")

            nameIDPolicy = NameIDPolicyBuilder.newInstance().buildObject()
            nameIDPolicy.setAllowCreate(true)
        }
        
        static validator = {
            assert destination == "https://localhost/Shibboleth.sso/SAML2/POST"
            assert status.statusCode.value == "urn:oasis:names:tc:SAML:2.0:status:Success"
            assert signature != null //todo
            assert encryptedAssertions.size() == 0
            assert issuer.value == "https://idptestbed/idp/shibboleth"
            assert assertions.size() == 1
            assert assertions[0].issuer.value == "https://idptestbed/idp/shibboleth"
            //assert assertions[0].subject.nameID.value == "<sometransientvalue>"
            assert assertions[0].subject.nameID.nameQualifier == "https://idptestbed/idp/shibboleth"
            assert assertions[0].subject.nameID.SPNameQualifier == "https://anothersp/shibboleth"
            assert assertions[0].subject.nameID.format == "urn:oasis:names:tc:SAML:2.0:nameid-format:transient"

            assert assertions[0].statements.size() == 2
            assert assertions[0].statements[0].authnContext.authnContextClassRef.authnContextClassRef == "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport"
            assert assertions[0].statements[1].indexedChildren.size() == 2
            assert assertions[0].statements[1].indexedChildren[0].name == "urn:oid:0.9.2342.19200300.100.1.1"
            assert assertions[0].statements[1].indexedChildren[0].friendlyName == "uid"
            assert assertions[0].statements[1].indexedChildren[0].attributeValues.size() == 1
            assert assertions[0].statements[1].indexedChildren[0].attributeValues[0].textContent == "jgasper"
            assert assertions[0].statements[1].indexedChildren[1].name == "urn:oid:0.9.2342.19200300.100.1.3"
            assert assertions[0].statements[1].indexedChildren[1].friendlyName == "mail"
            assert assertions[0].statements[1].indexedChildren[1].attributeValues.size() == 1
            assert assertions[0].statements[1].indexedChildren[1].attributeValues[0].textContent == "jgasper@example.edu"
        }
    }
}
