// Class declaration.
public class GenerateHTML {
  // Main method.
  public static void main(String[] args) throws Exception {
                System.out.println("<!DOCTYPE html>");
                System.out.println("<html>");
                System.out.println("    <head>");
                System.out.println("            <title>Certificate Details (c)</title>");
                System.out.println("            <meta charset=UTF-8>");
                System.out.println("    </head>");
                System.out.println("    <body>");
                System.out.println("            <style type=text/css>");
                System.out.println("                    form {text-align: center;}");
                System.out.println("                    table {border-collapse: collapse;width: 20%;}");
                System.out.println("                    th {background-image: linear-gradient(#aaa, #aaa);color: #0000; font-size: 17px; border-radius: 5px;}");
                System.out.println("                    td {border-radius: 4px;padding: 8px;text-align: center;border-bottom: 1px solid #ddd;}");
                System.out.println("                    tr:hover{background-color:#e0e0e0}");
                System.out.println("                    table, th, td {border: 2px solid black;border-collapse: collapse;align: center;}");
                System.out.println("                    th, td {padding: 5px;}");
                System.out.println("            </style>");
                System.out.println("            <table style='width:100%'>");
                System.out.println("                    <tr>");
                System.out.println("                            <th width='28%'>Common Name</th>");
                System.out.println("                            <th width='20%'>Serial Number</th>");
                System.out.println("                            <th width='10%'>Expiration</th>");
                System.out.println("                            <th width='12%'>SAN Entries</th>");
                System.out.println("                            <th width='10%'>Signature</th>");
                System.out.println("                    </tr>");
                System.out.println("                    <tr>");
                System.out.println("                            <td align='center'>EMAILADDRESS=client_root@testcompany.com, CN=client_root, OU=client_root Unit, O=Test Company, L=us-english, ST=California, C=US</td>");
                System.out.println("                            <td align='center'>0</td>");
                System.out.println("                            <td align='center'>4/24/03 5:44 PM </td>");
                System.out.println("                            <td align='center' bgcolor='red'><b><span title='This certificate expired'>4/21/13 5:44 PM</span></td></b>");
                System.out.println("                            <td align='center' bgcolor='red'><b><span title='This certificate expired'>4/21/13 5:44 PM</span></td></b>");
                System.out.println("                    </tr>");
                System.out.println("            </table>");
                System.out.println("    </body>");
                System.out.println("</html>");
        }
}
