package service

import net.unicon.avus.TestData

class AnotherDemoJsmithService extends TestData {

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
        assert assertions[0].statements[1].indexedChildren.size() == 1
        assert assertions[0].statements[1].indexedChildren[0].name == "urn:oid:0.9.2342.19200300.100.1.1"
        assert assertions[0].statements[1].indexedChildren[0].friendlyName == "uid"
        assert assertions[0].statements[1].indexedChildren[0].attributeValues.size() == 1
        assert assertions[0].statements[1].indexedChildren[0].attributeValues[0].textContent == "jsmith"
    }
}
