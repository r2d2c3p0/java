package org.r2d2c3p0.cr.generator.csv;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"file:./configuration/csv.config", "file:/var/tmp/csv.config",
		  "file:/tmp/csv.config",
    	  "classpath:csv.config" })
public interface CSVConfig extends Config {

		@Key("csv.version")
		@DefaultValue("1.0.0")
		String csvVer();

}