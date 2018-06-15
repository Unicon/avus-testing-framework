package service

import net.unicon.avus.TestData

class FakeDemoJgasperService extends TestData {
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
        assert assertions[0].subject.nameID.SPNameQualifier == "https://fakesp/shibboleth"
        assert assertions[0].subject.nameID.format == "urn:oasis:names:tc:SAML:2.0:nameid-format:transient"

        assert assertions[0].statements.size() == 2
        assert assertions[0].statements[0].authnContext.authnContextClassRef.authnContextClassRef == "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport"
        assert assertions[0].statements[1].indexedChildren.size() == 2

        def child = assertions[0].statements[1].indexedChildren.find { it.name == "urn:oid:1.3.6.1.4.1.5923.1.1.1.6" }
        assert child.friendlyName == "eduPersonPrincipalName"
        assert child.attributeValues.size() == 1
        assert child.attributeValues[0].textContent == "jgasper@example.edu"

        def child2 = assertions[0].statements[1].indexedChildren.find { it.name == "urn:oid:0.9.2342.19200300.100.1.3" }
        assert child2.friendlyName == "mail"
        assert child2.attributeValues.size() == 1
        assert child2.attributeValues[0].textContent == "jgasper@example.edu"
   }
}
