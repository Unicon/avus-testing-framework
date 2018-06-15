package casmanagement

import geb.Page

/**
 * The CAS Management page
 */
class CasManagementMainPage extends Page {

    static at = {
        title == "Services Management"
    }

    static content = {
        text { $("app-root").text() }
    }
}
