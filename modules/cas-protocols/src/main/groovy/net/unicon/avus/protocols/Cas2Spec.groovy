package net.unicon.avus.protocols

import net.unicon.avus.TestData
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator

/**
 * Provides CAS2 protocol support
 */
abstract class Cas2Spec extends CasSpec {

    /**
     * starts a CAS SSO authentication
     * @param testData TestData with a populated initiator method/closure
     */
    void go(Class<TestData> testData) {
        startRequest testData, "service"
    }


    /**
     * Validates a service ticket
     * @param testData TestData with a populated initiator and validator method/closure
     */
    void response(Class<TestData> testData) {
        validateResponse(testData, new Cas20ServiceTicketValidator(getAuthServerEndpoint()))
    }
}
