package net.unicon.avus.protocols

import net.unicon.avus.TestData
import org.jasig.cas.client.validation.Saml11TicketValidator

/**
 * Provides CAS SAML 11 protocol support
 */
abstract class CasSaml11Spec extends CasSpec {

    /**
     * starts a CAS SSO authentication
     * @param testData TestData with a populated initiator method/closure
     */
    void go(Class<TestData> testData) {
        startRequest testData, "TARGET"
    }


    /**
     * Validates a service ticket
     * @param testData TestData with a populated initiator and validator method/closure
     */
    void response(Class<TestData> testData) {
        validateResponse(testData, new Saml11TicketValidator(getAuthServerEndpoint()), "SAMLart")
    }
}
