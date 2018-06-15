package net.unicon.avus.protocols

import net.unicon.avus.AvusSpec
import net.unicon.avus.TestData
import org.jasig.cas.client.ssl.HttpURLConnectionFactory
import org.jasig.cas.client.util.CommonUtils
import org.jasig.cas.client.util.URIBuilder
import org.jasig.cas.client.validation.TicketValidator
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

/**
 * Provides abstract CAS protocol support
 */
abstract class CasSpec extends AvusSpec {

    /**
     * Allows us to test CAS Server instances using a self-signed or old certificate.
     */
    boolean ignoreCertificateIssuesOnValidate = false

    /**
     * starts a CAS SSO authentication
     * @param testData TestData with a populated initiator method/closure
     */
    protected void startRequest(Class<TestData> testData, String serviceParameterName) {
        checkForNullTestData(testData)
        RequestProperties properties = getRequestProperties(testData)

        go CommonUtils.constructRedirectUrl(getAuthServerEndpoint() + "/login",
                serviceParameterName,
                properties.serviceId,
                properties.renew,
                properties.gateway)
    }

    /**
     * Reads the request properties from an TestData's initiator
     * @param testData the test data
     * @return the request properties
     */
    protected RequestProperties getRequestProperties(Class<TestData> testData) {
        def properties = new RequestProperties()
        Closure initiator = (Closure) testData.initiator?.clone()
        initiator.resolveStrategy = Closure.DELEGATE_FIRST
        initiator.delegate = properties

        if (initiator == null) {
            throw new Exception("The TestData class must define initiator.")
        }

        initiator()
        initiator.delegate
    }

    /**
     * Validates a service ticket
     * @param testData TestData with a populated initiator and validator method/closure
     */
    protected void validateResponse(Class<TestData> testData, TicketValidator ticketValidator, String ticketParameterName = "ticket" ) {
        checkForNullTestData(testData)

        RequestProperties requestProperties = getRequestProperties(testData)
        if (requestProperties.serviceId == null) {
            throw new Exception("TestData must have initiator and its serviceId must be populated.")
        }

        def builder = new URIBuilder(page.driver.currentUrl)
        def ticket = builder.getQueryParams().find({ e -> e.name == ticketParameterName })


        if (ignoreCertificateIssuesOnValidate) {
            ticketValidator.setURLConnectionFactory(new AnyHttpsURLConnectionFactory())
        }

        def assertion = ticketValidator.validate(ticket.value, requestProperties.serviceId)

        Closure validateCasResponse = (Closure)testData.validator?.clone()
        validateCasResponse.resolveStrategy = Closure.DELEGATE_FIRST
        validateCasResponse.delegate = assertion

        if (validateCasResponse == null) {
            throw new Exception("The service class must define authnResponse")
        }

        validateCasResponse()
    }

    /**
     *
     * @param testData checks for a null object
     */
    private void checkForNullTestData(Class<TestData> testData) {
        if (testData == null) {
            throw new Exception("The service class must be defined.")
        }
    }

    /**
     * Is a connection factory that allows any certificates through (old, bad hostname, etc)
     */
    private class AnyHttpsURLConnectionFactory implements HttpURLConnectionFactory {

        @Override
        HttpURLConnection buildHttpURLConnection(URLConnection url) {
            if (!(url instanceof HttpsURLConnection)) {
                LOGGER.info("Not an TLS connection.")
                url
            }

            def httpsConnection = (HttpsURLConnection) url

            //Setting our own Trust Manager so the connection completes and we can examine the server cert chain.
            httpsConnection.setSSLSocketFactory(getTheAllTrustingSSLContext().getSocketFactory())
            httpsConnection
        }

        private SSLContext getTheAllTrustingSSLContext() {
            try {
                def sslContext = SSLContext.getInstance("TLS");

                sslContext.init(null, [new X509TrustManager() {

                    @Override
                    void checkClientTrusted(X509Certificate[] xcs, String string) {
                    }

                    @Override
                    void checkServerTrusted(X509Certificate[] xcs, String string) {
                    }

                    @Override
                    X509Certificate[] getAcceptedIssuers() {
                        null
                    }

                }] as TrustManager[], null)

                sslContext

            } catch (Exception e) {
                throw new RuntimeException(e)
            }
        }

    }
}
