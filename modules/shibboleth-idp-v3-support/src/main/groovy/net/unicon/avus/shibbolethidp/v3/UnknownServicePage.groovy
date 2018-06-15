package net.unicon.avus.shibbolethidp.v3

import geb.Page

/**
 * Login page error, specifically for invalid credentials
 */
class UnknownServicePage extends Page {

    static at = {
        title == "Web Login Service - Unsupported Request"
        $("div .content").text().contains("The application you have accessed is not registered for use with this service.")
    }
}