package service

import net.unicon.avus.TestData
import org.joda.time.DateTime
import org.opensaml.saml.common.SAMLVersion
import org.opensaml.saml.saml2.core.impl.IssuerBuilder
import org.opensaml.saml.saml2.core.impl.NameIDPolicyBuilder


class AnotherDemoService extends TestData {
    static validator = {
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
