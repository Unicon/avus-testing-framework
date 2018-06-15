package net.unicon.avus

/**
 * Holds initiator and/or validator closures used by a AvusSpec.
 */
class TestData {

    /**
     * Default Constructor
     */
    TestData() {

    }

    /**
     * Constructor that sets an initiator and/or validator
     * @param initiator a closure that initiates an authentication request
     * @param validator a closure that validates an authentication response
     */
    TestData(Object initiator, Object validator) {
        this.initiator = initiator
        this.validator = validator
    }

    /**
     * a closure that initiates a mock (or an identity provider initiated) authentication request.
     */
    static Object initiator = null

    /**
     * a closure that validates the properties of an authentication response.
     */
    static Object validator = null
}
