package net.unicon.avus.protocols

/**
 * An initiator data for CAS protocol testing
 */
class RequestProperties {
    /**
     * The service id (callback) of the cas client initiating the authentication request
     */
    String serviceId

    /**
     * Determines whether the request is a renew request type
     */
    boolean renew = false

    /**
     * Determines whether the request is a gateways request type (red/green validation is probably not supported yet)
     */
    boolean gateway = false
}
