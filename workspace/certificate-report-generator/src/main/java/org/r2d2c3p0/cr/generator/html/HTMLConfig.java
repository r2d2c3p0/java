package org.r2d2c3p0.cr.generator.html;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"file:./configuration/html.config", "file:/var/tmp/html.config",
		  "file:/tmp/html.config",
    	  "classpath:html.config" })
public interface HTMLConfig extends Config {

		@Key("javascript.file")
		@DefaultValue("./configuration/js/main.js")
		String jsFile();

		@Key("fontawesome.css")
		@DefaultValue("./configuration/css/fontawesome.css")
		String faCSS();

		@Key("jqdatatable.css")
		@DefaultValue("./configuration/css/jqdatatable.css")
		String jqDTCSS();

		@Key("jquery.js")
		@DefaultValue("./configuration/js/jquery.js")
		String jqJS();

		@Key("datatable.js")
		@DefaultValue("./configuration/js/datatable.js")
		String dtJS();

		@Key("jqdatatable.js")
		@DefaultValue("./configuration/js/jqdatatable.js")
		String jqDTJS();

		@Key("check.url")
		@DefaultValue("http://nfl.com")
		String checkURL();

		@Key("check.internet")
		@DefaultValue("disable")
		String checkInternet();

		@Key("url.timeout")
		@DefaultValue("5000")
		int urlTimeout();

		@Key("request.method")
		@DefaultValue("HEAD")
		String requestMethod();

}