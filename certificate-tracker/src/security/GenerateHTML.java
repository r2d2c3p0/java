package security;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import java.io.FileNotFoundException;

public class GenerateHTML {
	
	public static void startHTML() throws FileNotFoundException {
		System.out.println("<!DOCTYPE html>");
		System.out.println("<html>");
	    System.out.println("\t<head>");
	    System.out.println("\t\t<title>Certificate Details (c)</title>");
	    System.out.println("\t\t<meta charset=UTF-8>");
	    System.out.println("\t</head>");
	    System.out.println("\t<body>");
	    System.out.println("\t\t\t<style type=text/css>");
	    System.out.println("\t\t\t\t\tform {text-align: center;}");
	    System.out.println("\t\t\t\t\ttable {border-collapse: collapse;width: 20%;}");
	    System.out.println("\t\t\t\t\tth {background-image: linear-gradient(#aaa, #aaa);color: #0000; font-size: 20px; border-radius: 5px;}");
	    System.out.println("\t\t\t\t\ttd {border-radius: 4px;padding: 8px;text-align: center;border-bottom: 1px solid #ddd;}");
	    System.out.println("\t\t\t\t\ttr:hover{background-color:#e0e0e0}");
	    System.out.println("\t\t\t\t\ttable, th, td {border: 2px solid black;border-collapse: collapse;align: center;}");
	    System.out.println("\t\t\t\t\tth, td {padding: 5px;}");
	    System.out.println("\t\t\t</style>");
	    System.out.println("\t\t\t<table style='width:100%'>");
	    System.out.println("\t\t\t\t\t<tr>");
	    System.out.println("\t\t\t\t\t\t<th width='28%'>Common Name</th>");
	    System.out.println("\t\t\t\t\t\t<th width='19%'>Serial Number</th>");
	    System.out.println("\t\t\t\t\t\t<th width='10%'>Expiration</th>");
	    System.out.println("\t\t\t\t\t\t<th width='18%'>SAN Entries</th>");
	    System.out.println("\t\t\t\t\t\t<th width='5%'>Signature</th>");
	    System.out.println("\t\t\t\t\t</tr>");
	}
	
	public static void htmlTable(String cName, String IssuerDN, BigInteger sNumber, Date exp1, Date exp2, Collection<List<?>> sANEntries, String Sig) throws FileNotFoundException {
		final Date currentTime = new Date();
		String SANDummyString = "";
		System.out.println("\t\t\t\t\t<tr>");
        System.out.println("\t\t\t\t\t\t<td align='center'>"+cName+"</td>");
        System.out.println("\t\t\t\t\t\t<td align='center'>"+sNumber+"</td>");
        if (currentTime.before(exp2)) {
        	System.out.println("\t\t\t\t\t\t<td align='center'>"+exp2+"</td>");
        }
        if (currentTime.after(exp2)) {
        	System.out.println("\t\t\t\t\t\t<td align='center'bgcolor='red'><b><span title='This certificate expired'>"+exp2+"</td>");        	
        }
        try {
        	for (List<?> SAN: sANEntries) {
        		//System.out.println("\t"+SAN.get(1));
        		SANDummyString = SANDummyString+"<li>"+SAN.get(1)+"</li>";
        	}
        	System.out.println("\t\t\t\t\t\t<td align='center'>"+SANDummyString+"</td>");
        } catch (NullPointerException Nully) {
        	// SAN Nulled
        	System.out.println("\t\t\t\t\t\t<td align='center'>No SAN Entries</td>");            
        }
        System.out.println("\t\t\t\t\t\t<td align='center'><ul>"+Sig+"</ul></td>");
        System.out.println("\t\t\t\t\t</tr>");
	}
	
	public static void endHTML() throws FileNotFoundException {		
		System.out.println("\t\t\t</table>");
        System.out.println("\t</body>");
        System.out.println("</html>");        
    }	
	
}