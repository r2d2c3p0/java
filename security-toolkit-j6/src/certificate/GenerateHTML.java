package certificate;

import java.math.BigInteger;

public class GenerateHTML {
	public static void startHTML() {
			System.out.println("<!DOCTYPE html>");
			System.out.println("<html>");
	        System.out.println("\t<head>");
	        System.out.println("\t        <title>Certificate Details (c)</title>");
	        System.out.println("\t        <meta charset=UTF-8>");
	        System.out.println("\t</head>");
	        System.out.println("\t<body>");
	        System.out.println("\t        <style type=text/css>");
	        System.out.println("\t                form {text-align: center;}");
	        System.out.println("\t                table {border-collapse: collapse;width: 20%;}");
	        System.out.println("\t                th {background-image: linear-gradient(#aaa, #aaa);color: #0000; font-size: 20px; border-radius: 5px;}");
	        System.out.println("\t                td {border-radius: 4px;padding: 8px;text-align: center;border-bottom: 1px solid #ddd;}");
	        System.out.println("\t                tr:hover{background-color:#e0e0e0}");
	        System.out.println("\t                table, th, td {border: 2px solid black;border-collapse: collapse;align: center;}");
	        System.out.println("\t                th, td {padding: 5px;}");
	        System.out.println("\t        </style>");
	        System.out.println("\t        <table style='width:100%'>");
	        System.out.println("\t                <tr>");
	        System.out.println("\t                        <th width='28%'>Common Name</th>");
	        System.out.println("\t                        <th width='19%'>Serial Number</th>");
	        System.out.println("\t                        <th width='10%'>Expiration</th>");
	        System.out.println("\t                        <th width='18%'>SAN Entries</th>");
	        System.out.println("\t                        <th width='5%'>Signature</th>");
	        System.out.println("\t                </tr>");
	        HTMLBuffer.main("<!DOCTYPE html>");
	    	HTMLBuffer.main("<html>");
	        HTMLBuffer.main("\t<head>");
            HTMLBuffer.main("\t        <title>Certificate Details (c)</title>");
            HTMLBuffer.main("\t        <meta charset=UTF-8>");
            HTMLBuffer.main("\t</head>");
            HTMLBuffer.main("\t<body>");
            HTMLBuffer.main("\t        <style type=text/css>");
            HTMLBuffer.main("\t                form {text-align: center;}");
            HTMLBuffer.main("\t                table {border-collapse: collapse;width: 20%;}");
            HTMLBuffer.main("\t                th {background-image: linear-gradient(#aaa, #aaa);color: #0000; font-size: 20px; border-radius: 5px;}");
            HTMLBuffer.main("\t                td {border-radius: 4px;padding: 8px;text-align: center;border-bottom: 1px solid #ddd;}");
            HTMLBuffer.main("\t                tr:hover{background-color:#e0e0e0}");
            HTMLBuffer.main("\t                table, th, td {border: 2px solid black;border-collapse: collapse;align: center;}");
            HTMLBuffer.main("\t                th, td {padding: 5px;}");
            HTMLBuffer.main("\t        </style>");
            HTMLBuffer.main("\t        <table style='width:100%'>");
            HTMLBuffer.main("\t                <tr>");
            HTMLBuffer.main("\t                        <th width='28%'>Common Name</th>");
            HTMLBuffer.main("\t                        <th width='19%'>Serial Number</th>");
            HTMLBuffer.main("\t                        <th width='10%'>Expiration</th>");
            HTMLBuffer.main("\t                        <th width='18%'>SAN Entries</th>");
            HTMLBuffer.main("\t                        <th width='5%'>Signature</th>");
            HTMLBuffer.main("\t                </tr>");    
	}
	public static void endHTML() {
        System.out.println("\t        </table>");
        System.out.println("\t</body>");
        System.out.println("</html>");
        HTMLBuffer.main("\t        </table>");
        HTMLBuffer.main("\t</body>");
        HTMLBuffer.main("</html>");
	}
	public static void htmlTable(String cName, BigInteger sNumber, String Exp2, String SANEntries, String Sig) {
		System.out.println("\t                <tr>");
        System.out.println("\t                        <td align='center'>"+cName+"</td>");
        System.out.println("\t                        <td align='center'>"+sNumber+"</td>");
        System.out.println("\t                        <td align='center'>"+Exp2+"</td>");
        System.out.println("\t                        <td align='center'>"+SANEntries+"</td>");
        System.out.println("\t                        <td align='center'><ul>"+Sig+"</ul></td>");
        System.out.println("\t                </tr>");
        HTMLBuffer.main("\t                <tr>");
        HTMLBuffer.main("\t                        <td align='center'>"+cName+"</td>");
        HTMLBuffer.main("\t                        <td align='center'>"+sNumber+"</td>");
        HTMLBuffer.main("\t                        <td align='center'>"+Exp2+"</td>");
        HTMLBuffer.main("\t                        <td align='center'>"+SANEntries+"</td>");
        HTMLBuffer.main("\t                        <td align='center'><ul>"+Sig+"</ul></td>");
        HTMLBuffer.main("\t                </tr>");
	}
}