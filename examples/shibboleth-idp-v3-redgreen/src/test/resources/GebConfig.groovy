/*
	This is the Geb configuration file.
	See: http://www.gebish.org/manual/current/#configuration
*/

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import java.util.logging.LogManager

accountUsername = System.getenv("username") != null ? System.getenv("username") : "jgasper"
accountPassword = System.getenv("password") != null ? System.getenv("password") : "password"

waiting {
    timeout = 2
}

reportsDir = "build/reports/geb"

driver = {
    def driver = new HtmlUnitDriver()
    driver.javascriptEnabled = true
    driver
}

//Suppress org.openqa.selenium.remote.Augmenter WARNings coming for Java logging when using GebReportingSpec
//This may also suppress HtmlUnit CSS and JavaScript errors
LogManager.getLogManager().reset()
