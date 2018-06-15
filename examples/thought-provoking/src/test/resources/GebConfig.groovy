/*
	This is the Geb configuration file.
	See: http://www.gebish.org/manual/current/#configuration
	To run the tests, just run “mvn clean test”
*/

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import java.util.logging.LogManager

waiting {
    timeout = 2
}

reportsDir = "build/reports/geb"

//Technically the default driver
driver = {
    def driver = new HtmlUnitDriver()
    driver.javascriptEnabled = true
    driver
}

//baseUrl = "https://hr.unicon.net"


//Suppress org.openqa.selenium.remote.Augmenter WARNings coming for Java logging when using GebReportingSpec
LogManager.getLogManager().reset();
