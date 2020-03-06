package org.r2d2c3p0.utils;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"file:./configuration/crgenerator.properties", "file:/var/tmp/crgenerator.properties",
		  "file:/tmp/crgenerator.properties",
    	  "classpath:crgenerator.properties" })
public interface CRGeneratorConfig extends Config {

		@Key("keystore.file.extensions")
		@DefaultValue("jks")
		String kExtensions();

		@Key("certificate.file.extentions")
		@DefaultValue("cer")
		String cExtensions();

		@Key("root.full.path")
		@DefaultValue("/")
		String rootPath();

		@Key("encrypted.passwords.file")
		@DefaultValue("./configuration/passwords.file")
		String encryptedFile();

		@Key("log4j.properties.file")
		@DefaultValue("./configuration/log4j.properties")
		String l4jFile();

		@Key("report.output.format")
		@DefaultValue("CSV")
		String outputFormat();

		@Key("json.output.file")
		@DefaultValue("./crgenerator.json")
		String jsonFile();

		@Key("csv.output.file")
		@DefaultValue("./crgenerator.csv")
		String csvFile();

		@Key("html.output.file")
		@DefaultValue("./crgenerator.html")
		String htmlFile();

}