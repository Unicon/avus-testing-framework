package net.unicon.avus.protocols

import net.shibboleth.utilities.java.support.codec.Base64Support
import net.shibboleth.utilities.java.support.component.ComponentInitializationException
import net.shibboleth.utilities.java.support.xml.BasicParserPool
import net.unicon.avus.AvusSpec
import net.unicon.avus.TestData
import org.opensaml.core.config.ConfigurationService
import org.opensaml.core.config.InitializationException
import org.opensaml.core.config.InitializationService
import org.opensaml.core.xml.config.XMLObjectProviderRegistry
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport
import org.opensaml.core.xml.util.XMLObjectSupport
import org.opensaml.messaging.encoder.MessageEncodingException
import org.opensaml.saml.common.SAMLObject
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPRedirectDeflateEncoder
import org.opensaml.saml.saml2.core.Response
import org.opensaml.saml.saml2.core.impl.AuthnRequestBuilder

/**
 * Provides SAML2 protocol support
 */
abstract class Saml2Spec extends AvusSpec{

    HTTPRedirectDeflateEncoder httpRedirectDeflateEncoder = new MyHTTPRedirectDeflateEncoder()
    AuthnRequestBuilder authnRequestBuilder = new AuthnRequestBuilder()

    /**
     * Initializing OpenSAML library
     */
    void setupSpec() {
        try {
            InitializationService.initialize()
        } catch (InitializationException e) {
            e.printStackTrace()
        }

        XMLObjectProviderRegistry registry
        synchronized (ConfigurationService.class) {
            registry = ConfigurationService.get(XMLObjectProviderRegistry.class)
            if (registry == null) {
                registry = new XMLObjectProviderRegistry()
                ConfigurationService.register(XMLObjectProviderRegistry.class, registry)
            }
        }

        final BasicParserPool pool = new BasicParserPool()
        pool.setMaxPoolSize(100)
        pool.setCoalescing(true)

        pool.setXincludeAware(false)
        pool.setExpandEntityReferences(false)
        pool.setIgnoreComments(true)
        pool.setNamespaceAware(true)
        registry.setParserPool(pool)

        final Map<String, Boolean> features = new HashMap<>()
        features.put("http://apache.org/xml/features/disallow-doctype-decl", Boolean.TRUE)
        features.put("http://apache.org/xml/features/validation/schema/normalized-value", Boolean.FALSE)
        features.put("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE)
        features.put("http://xml.org/sax/features/external-general-entities", Boolean.FALSE)
        features.put("http://xml.org/sax/features/external-parameter-entities", Boolean.FALSE)
        pool.setBuilderFeatures(features)

        try {
            pool.initialize()
        } catch (ComponentInitializationException e) {
            e.printStackTrace()
        }
    }

    /**
     * starts a SAML SSO authentication
     * @param testData TestData with a populated initiator method/closure
     */
    void go(Class<TestData> testData) {
        checkForNullService(testData)

        Closure initiator = (Closure)testData.initiator?.clone()
        initiator.resolveStrategy = Closure.DELEGATE_FIRST
        initiator.delegate = authnRequestBuilder.buildObject()

        if (initiator == null) {
            throw new Exception("The TestData class must define initiator")
        }

        initiator()

        String encodedRequest = URLEncoder.encode(httpRedirectDeflateEncoder.deflateAndBase64Encode(initiator.delegate), "UTF-8")

        go "https://localhost:4443/idp/profile/SAML2/Redirect/SSO?SAMLRequest=" + encodedRequest
    }

    /**
     * Validates a SAML authnResponse
     * @param testData TestData with a populated validator method/closure
     */
    void response(Class<TestData> testData) {
        checkForNullService(testData)

        byte[] decodedBytes = Base64Support.decode(samlResponse)
        InputStream inputStream = new ByteArrayInputStream(decodedBytes)

        Response response = (Response) XMLObjectSupport.unmarshallFromInputStream(
                XMLObjectProviderRegistrySupport.getParserPool(), inputStream)

        Closure validator = (Closure)testData.validator?.clone()
        validator.resolveStrategy = Closure.DELEGATE_FIRST
        validator.delegate = response

        if (validator == null) {
            throw new Exception("The testData class must define validator.")
        }

        validator()
    }

    private void checkForNullService(Class<TestData> testData) {
        if (testData == null) {
            throw new Exception("The testData class must be defined.")
        }
    }

    private class MyHTTPRedirectDeflateEncoder extends HTTPRedirectDeflateEncoder {
        String deflateAndBase64Encode(SAMLObject samlObject) throws MessageEncodingException {
            return super.deflateAndBase64Encode(samlObject)
        }
    }
}
