import net.unicon.avus.protocols.SamlSpec
import net.unicon.avus.shibbolethidp.v3.*
import service.*
import spock.lang.Unroll


class RedGreenAuthnFlowSpec extends SamlSpec {
    void setup() {
        driver.webClient.cookieManager.clearCookies()
    }

    def "login account success with no release prompt - using service"() {
        when: "redirected from SP"
        startRequestFor FakeDemoService

        and: "I authenticate with a good account"
        at LoginPage
        username = "jgasper"
        password = "password"
        loginButton.click()

        then: "I get the SAML response page"
        at SamlResponsePage
        postUrl.length() > 0
    }


    def "login account success with release prompt"() {
        when: "redirected from SP"
        browser.clearCookies()
        startRequestFor AnotherDemoService
        at LoginPage

        and: "I authenticate with a good account"
        username = "jsmith"
        password = "password"
        loginButton.click()

        then: "I am presented with the attribute release page"
        at AttributeReleasePage
        attributes.every { (it.attributeName == "uid" && it.attributeValue == "jsmith") }

        when: "I accept the attribute release"
        acceptButton.click()

        then: "I get the SAML response page"
        at SamlResponsePage
        postUrl.length() > 0
    }


    @Unroll
    def "Testing multiple - #testUsername - #authnRequest"() {
        when: "redirected from SP"
        startRequestFor authnRequest
        at LoginPage

        and: "I authenticate with a good account"
        username = testUsername
        password = "password"
        loginButton.click()

        then: "I am presented with the attribute release page"
        at AttributeReleasePage
        //attributes.every { (it.attributeName == "uid" && (it.attributeValue == "jsmith" || it.attributeValue == "jgasper")) }

        when: "I accept the attribute release"
        acceptButton.click()

        then: "I get the SAML response page"
        at SamlResponsePage
        //postUrl.length() > 0

        validateResponseFrom authnResponse

        where:
        authnRequest       | authnResponse             | testUsername
        AnotherDemoService | AnotherDemoJsmithService  | "jsmith"
        AnotherDemoService | AnotherDemoJgasperService | "jgasper"
    }

    @Unroll
    def "Testing multiple no attribute - #testUsername"() {
        when: "redirected from SP"
        startRequestFor authnRequest
        at LoginPage

        and: "I authenticate with a good account"
        username = testUsername
        password = "password"
        loginButton.click()

        then: "I get the SAML response page"
        at SamlResponsePage
        //postUrl.length() > 0

        validateResponseFrom service

        where:
        authnRequest    | service               | testUsername
        FakeDemoService | FakeDemoJsmithService | "jsmith"
        FakeDemoService | FakeDemoJgasperService | "jgasper"
    }
}

