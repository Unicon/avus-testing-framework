import net.unicon.avus.TestData
import net.unicon.avus.protocols.Saml2Spec
import net.unicon.avus.shibbolethidp.v3.*
import org.joda.time.DateTime
import org.opensaml.saml.common.SAMLVersion
import org.opensaml.saml.saml2.core.impl.IssuerBuilder
import org.opensaml.saml.saml2.core.impl.NameIDPolicyBuilder

class TestBasicFunctionality extends Saml2Spec {
    def "check IdP login page"() {
        when: "redirected from an SP"
        go KnownService

        then: "I get the Login page"
        at LoginPage
    }

    class KnownService extends TestData {
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
    }

    def "connect to an unknown service"() {
        when: "redirected from an unknown service"
        go BadService

        then: "I get the Unknown Service page"
        at UnknownServicePage
    }

    class BadService extends TestData {
        static initiator = {
            assertionConsumerServiceURL = "https://localhost/Shibboleth.sso/SAML2/POST"
            destination = "https://localhost:4443/idp/profile/SAML2/Redirect/SSO"
            ID = "_" + UUID.randomUUID().toString()
            issueInstant = DateTime.now()
            protocolBinding = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
            version = SAMLVersion.VERSION_20

            issuer = IssuerBuilder.newInstance().buildObject()
            issuer.setValue("https://badsp/donottrust")

            nameIDPolicy = NameIDPolicyBuilder.newInstance().buildObject()
            nameIDPolicy.setAllowCreate(true)
        }
    }

}
