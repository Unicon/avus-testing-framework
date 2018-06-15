package net.unicon.avus.casserver.v5

import geb.Page

/**
 * Page for Service Not Authorized
 */
class ServiceNotAuthorizedPage extends Page {
    static at = {
        title == "Application Not Authorized to Use CAS - CAS – Central Authentication Service"
        $(".alert-danger").text().contains("Application Not Authorized to Use CAS")
    }
}