
<h3 align="center"><b>Certificate Report Generator</b></h3>

Application to generate Key store and Certificate details in HTML, JSON and CSV formats.

Application to print the certificate information (see below for more) inside the key stores by searching recursively from the root directory.
The default output is CSV but HTML and JSON can be selected from inside the crgenerator.properties file.

##### Application Usage [Java 6 or higher required]

:one:   *java -jar Certificate-Report-Generator.jar*

Make sure the crgenerator and logger properties files are under ./configuration directory.

:two:  *java -jar -Dlog4j.configuration=file:logger.properties Certificate-Report-Generator.jar*

Make sure the crgenerator.properties file is under ./configuration directory.

:three:  *java -jar -Dlog4j.configuration=file:logger.properties Certificate-Report-Generator <properties file path>*


##### Below certificate details are printed in the output file.

| Attribute name | Description                    |
| ------------- | ------------------------------ |
| `Common Name`      | Common Name part of DN.       |
| `Serial Number`   | Serial Number in Decimal and Hexadecimal     |
| `Issuing Authority`      | DN of the CA.       |
| `Validity`      | Issued and Expiring dates.       |
| `Signature Algorithm`      | SHA1/SHA256/MD5 etc.       |
| `SAN Entries`      | Subject Alternative Names (if any).       |
| `Private Key`      | Identifies if entry is Key entry.       |
| `Chained Certificates`      | If a entry is Key, then list chained certificates.       |
| `Current Status`      | Indicates the validity period still left.       |

# License

This repository uses the MIT license. In plain words, this means that you
can reuse it in both opensource and proprietary projects; the only
requirement is that you keep the license text with each source file,
where it already is. This is for my protection: the license text
basically says that whatever happens, it's not my fault, and you
understand it.

#
