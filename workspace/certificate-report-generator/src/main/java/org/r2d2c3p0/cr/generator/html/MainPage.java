package org.r2d2c3p0.cr.generator.html;

import org.apache.log4j.Logger;
import org.r2d2c3p0.utils.GetLocalTime;

public class MainPage {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	public static String print(
			String FontAwesomeCSS,
			String jQueryDataTableCSS,
			String jQueryJS,
			String jQueryDataTableJS,
			String DataTableJS,
			String JSIdentifier
			) {
		logger.debug(" [MainPage] called.");
		return "<!doctype html>\n"
				+"<html>\n"
				+"\t<head>\n"
				+"\t\t<meta name=\"Certificate Report Generator\" content=\"Keystore & Certificates\">\n"
				+"\t\t<title>Certificate Report Generator Application</title>\n"
				+"\t\t<meta charset=\"utf-8\" />\n"
				+"\n"
				+"\t\t<link href= "+FontAwesomeCSS+" rel=\"stylesheet\" type=\"text/css\" crossorigin=\"anonymous\">\n"
				+"\t\t<link href= "+jQueryDataTableCSS+" rel=\"stylesheet\" type=\"text/css\">\n"
				+"\n"
				+"\t\t<script src= "+jQueryJS+" type=\"text/javascript\"></script>\n"
				+"\t\t<script src= "+jQueryDataTableJS+" type=\"text/javascript\"></script>\n"
				+"\t\t<script src= "+DataTableJS+" type=\"text/javascript\"></script>\n"
				+"\n"
				+"\t\t<style>\n"
				+"\t\t\ttd.details-control {\n"
				+"\t\t\t\ttext-align:center;\n"
				+"\t\t\t\tcolor:forestgreen;\n"
				+"\t\t\t\tcursor: pointer;\n"
				+"\t\t\t}\n"
				+"\t\t\ttr.shown td.details-control {\n"
				+"\t\t\t\ttext-align:center;\n"
				+"\t\t\t\tcolor:orange;\n"
				+"\t\t\t}\n"
				+"\t\t</style>\n"
				+"\n"
				+"\t</head>\n"
				+"\n"
				+"\t<body>\n"
				+"\t\t<h3 align=\"center\"> Certificate Report Generator </h3>\n"
				+"\t\t<table width=\"100%\" class=\"display\" id=\"crgenerator\" cellspacing=\"0\">\n"
				+"\t\t\t<thead>\n"
				+"\t\t\t\t<tr>\n"
				+"\t\t\t\t\t<th></th>\n"
				+"\t\t\t\t\t<th>Common Name</th>\n"
				+"\t\t\t\t\t<th>Status</th>\n"
				+"\t\t\t\t\t<th>Serial Number</th>\n"
				+"\t\t\t\t</tr>\n"
				+"\t\t\t</thead>\n"
				+"\t\t\t<tfoot>\n"
				+"\t\t\t\t<tr>\n"
				+"\t\t\t\t\t<th></th>\n"
				+"\t\t\t\t\t<th>Common Name</th>\n"
				+"\t\t\t\t\t<th>Status</th>\n"
				+"\t\t\t\t\t<th>Serial Number</th>\n"
				+"\t\t\t\t</tr>\n"
				+"\t\t\t</tfoot>\n"
				+"\t\t</table>\n"
				+"\t\t<p>Report generated on "+GetLocalTime.currentTime()+"</p>\n"
				+"\t\t<script type=\"text/javascript\" src=\"configuration/js/"+JSIdentifier+"_main.js\"></script>\n"
				+"\t</body>\n"
				+"\n"
				+"</html>\n";
	}
}