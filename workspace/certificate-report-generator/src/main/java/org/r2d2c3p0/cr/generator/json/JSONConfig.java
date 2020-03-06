package org.r2d2c3p0.cr.generator.json;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"file:./configuration/json.config", "file:/var/tmp/json.config",
		  "file:/tmp/json.config",
    	  "classpath:json.config" })
public interface JSONConfig extends Config {

		@Key("json.pretty.print")
		@DefaultValue("disable")
		String jsonPP();

}