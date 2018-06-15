package net.unicon.avus

import geb.spock.GebReportingSpec

/**
 * Abstract base class that all AvusSpec extend
 */
abstract class AvusSpec extends GebReportingSpec {
    /**
     * The endpoint to the authnentication server application handing the request.
     */
    String authServerEndpoint = null

    /**
     * returns the GebConfig.groovy `authServerEndpoint` value is no Spec specific `authServerEndpoint` is set.
     * @return the authServerEndpoint string
     */
    String getAuthServerEndpoint() {
        authServerEndpoint ?: properties.browser.config.rawConfig.authServerEndpoint
    }

    /**
     * Clears browser cookies each test iteration.
     */
    void setup() {
        driver.webClient.cookieManager.clearCookies()
    }
}
