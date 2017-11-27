# 
CustomKeytool - Requires JAVA 5 or more.

customkeytool - Use this to replace the default keytool to perform most the popular tasks. You can create JKS/PKS12 files, list the contents, import the certificates and many more.

        > java -jar CustomKeytool.jar key.jks

        Enter keystore 'key.jks' password:****************

            ==================================================================================
             Keystore [key.jks], Select operation below:
            ==================================================================================
                     1. List Certificates                    2. Add Certificates
                     3. Delete Certificates                  4. Extract Certificates
                     5. Import Keystore                      6. Extract Private Key
                     7. Show Signers Information             8. Change PKCS12 Key Password
            ==================================================================================


    Make your selection [1/..../8]: 
        
        1. List Certificates= This lists all the certificates inside the keystore(input argument 1) in tab indent format. Provides the alias, issuer DN, validity period, SAN entries(if present) and chained entries(if present).
        2. Add Certificates= Use this option to add certificates(as signers) to the keystore(input argument 1), this option requires you provide the location of folder CACertificates(input argument 2) which should have all the certificates that you plan to import in to the keystore(input argument 1).
        3. Delete Certificates= Deletes the certificates inside keystore(input argument 1). Can be used to create(indirectly) a empty keystore.
        4. Extract Certificates= Extracts the certificates inside keystore(input argument 1) in DER and Base64 formats.
        5. Import Keystore= This will import the keystore (JKS, PKCS12 and CMS) formats into the keystore(input argument 1).
        6. Extract Private Key= under construction.
        7. Show Signers Information= This will print only the CA Signers inside the keystore(input argument 1).
        8. Create PKCS12 Keystore= This uses the customkeytool shell script to create the PKCS12 file, follow the promtps when it is launched and provide the user-input.
        9. Change PKCS12 Key Password= under construction.

        If no input keystore is provided, there are two options to choose:
		==================================================================================
                 	Keystore [null], Select operation below:
                ==================================================================================
                     a. Create PKCS12 Keystore                    b. List Security Providers
                     c. Create JKS Keystore
                ==================================================================================
                
#end-README.txt
