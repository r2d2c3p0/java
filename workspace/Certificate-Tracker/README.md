<h3 align="center"><b>:no_entry_sign: :rotating_light: :rotating_light: Warning ---- Discontinued ---- :rotating_light: :rotating_light: :no_entry_sign:</b></h3>
## Certificate Tracker (Uber jar) 2/11/2018

[New Version Here](https://github.com/r2d2c3p0/Certificate-Report-Generator)

[Github](https://github.com/r2d2c3p0/Certificate-Tracker/tree/master/src/main/java/com/certificate/tracker) Source Location.

Application to print the certificate information inside the key stores by searching recursively from the root directory.
The default output is CSV and JSON can be selected from inside the certificate-tracker-application.properties file.

```markdown
Application Usage

1. java -jar Certificate-Tracker.jar
- Make sure the certificate-tracker-application and log4j properties files are under ./config directory.

2. java -jar -Dlog4j.configuration=file:log4j.properties Certificate-Tracker.jar
- Make sure the certificate-tracker-application.properties file is under ./config directory.

3. java -jar -Dlog4j.configuration=file:log4j.properties Certificate-Tracker.jar <properties file path>
```

Below certificate details are printed in the output file.
- Common Name
- Serial Number (Both decimal and hexadecimal)
- Issing Authority
- Validity
- Signature Algorithm
- SAN Entries
- Chained Certificates
- Private Key

#
