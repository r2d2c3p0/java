#!/usr/bin/ksh

#
#
# customkeytool.
#
# Shell wrapper for CustomKeytool.jar (JAVA keytool substitute with cleaner output and operations.)
# r2d2c3p0.
# version 1.0.0 (11/8/2017 - initial version.)
# version 1.1.0 (1/18/2018 - added code to parse non-rsa keys, while creating pfx/p12 files.)
# version 1.2.0 (3/30/2018 - isolated the CA signers.)
# version 1.3.0 (4/4/2018 - fixed rsa private key passwords and repetative options.)
# version 1.4.0 (4/20/2018 - reads pem files.)
#
#
#
# openssl enc -d -base64 -A -v -in ${pem_file} -out [filename].pfx
#

# global settings.
got_keystore=0
got_password=0
got_help=0
got_list=0

# CA and JAR locations.
jar_location=/apps/lib
encrypted_file=encrypted.file

# Functions.

function clear_screen_check_environment {

        { clear;       } 2>/dev/null  ||
        { tput clear;  } 2>/dev/null  ||
                for empty in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 ; do
             echo
        done
                echo;
        if [[ ! -f ${jar_location}/CustomKeytool.jar ]]; then
                        echo "ERROR| CustomKeytool JAR is missing.";echo
                        exit 1
                fi

                which java >/dev/null 2>&1
                if [[ $? -ne 0 ]]; then
                        echo "ERROR| Java (JAVA_HOME) not found.";echo
                        exit 1
                fi
}

function print_help {
        echo "ERROR| usage incorrect!"
        echo """
                        cktool <keystore_name> | <key>
                        spass <keystore_name>
                        ckt
        "
        exit 1
}

# Main.
clear_screen_check_environment
if [[ $# -eq 0 ]]; then
        echo "ERROR| usage error, use -h|-help for help."
        exit 1
else
   input_arguments="$@"
   while [[ $# -ne 0 ]]; do
         input_argument=${1}
         case ${input_argument} in
                                -keystore|-k)
                                                if [[ $# -lt 2 ]]; then
                                                   echo "ERROR| provide the full path for the keystore."
                                                   echo "INFO| use '-h|-help' for usage message."
                                                   exit 1
                                                fi
                                                keystore_name=${2}
                                                got_keystore=1
                                                file_type=`echo ${keystore_name} | awk -F "." '{print $NF}'`
                                                if [[ ${file_type} == "pem" ]]; then
                                                        openssl crl2pkcs7 -nocrl -certfile ${keystore_name} | openssl pkcs7 -print_certs -text -noout | grep "Subject:"
                                                        exit 0
                                                elif [[ ${file_type} == "PEM" ]]; then
                                                        openssl crl2pkcs7 -nocrl -certfile ${keystore_name} | openssl pkcs7 -print_certs -text -noout | grep "Subject:"
                                                        exit 0
                                                fi
                                                shift;
                                                shift;
                                                ;;
                                    -h|-help)
                                                got_help=1
                                                shift;
                                                ;;
                                   -password)
                                                if [[ $# -lt 2 ]]; then
                                                   echo "ERROR| provide the full path for the keystore."
                                                   echo "INFO| use '-h|-help' for usage message."
                                                   exit 1
                                                fi
                                                keystore_name=${2}
                                                got_password=1
                                                shift;
                                                shift;
                                                ;;
                                    -l|-list)
                                                got_list=1
                                                shift;
                                                ;;
                                           *)
                                                echo "ERROR| unknown input arguments: ${input_argument}."
                                                echo "ERROR| provide the full path for the keystore."
                                                echo "INFO| use '-h|-help' for usage message."
                                                exit 1
                                                ;;
         esac
   done
fi

if [[ ${got_help} -eq 1 ]]; then
        print_help
fi

if [[ ${got_list} -eq 1 ]]; then
        java -jar ${jar_location}/CustomKeytool.jar
        if [[ $? -eq 8 ]]; then
                echo "  Enter the Key file(Base64) location: "
                read key_file
                echo "  Reading ${key_file}...";sleep 1;
                grep "\-\-\-\-\-BEGIN RSA PRIVATE KEY\-\-\-\-\-" ${key_file} >/dev/null 2>&1
                if [[ $? -eq 0 ]]; then
                        echo "  Enter the public file(Base64) location: "
                        read pub_file;echo "  Reading ${pub_file}...";sleep 1
                        grep "\-\-\-\-\-BEGIN CERTIFICATE\-\-\-\-\-" ${pub_file} >/dev/null 2>&1
                        if [[ $? -eq 0 ]]; then
                                echo "  Enter alias name: ";read alias_name
                                echo "  Enter destination keystore(p12) name: "
                                read ks_name
                                if [[ -f ${ks_name} ]]; then
                                        echo "ERROR| file ${ks_name} already exists."
                                        exit 24
                                else
                                        echo "  building PKCS12 file ${ks_name}...";sleep 1
                                        openssl pkcs12 -export -name ${alias_name} -out ${ks_name} -inkey ${key_file} -in ${pub_file}
                                        if [[ $? -eq 0 ]]; then
                                                echo "  created ${ks_name} successfully."
                                        else
                                                echo "ERROR| occured creating ${ks_name}."
                                                exit 23
                                        fi
                                fi
                        else
                                echo "ERROR| not a valid public key."
                                exit 22
                        fi
                else                            
                                                grep "\-\-\-\-\-BEGIN PRIVATE KEY\-\-\-\-\-" ${key_file} >/dev/null 2>&1
                                                if [[ $? -eq 0 ]]; then
                                                        echo "  Enter the public file(Base64) location: "
                                                        read pub_file;echo "  Reading ${pub_file}...";sleep 1
                                                        grep "\-\-\-\-\-BEGIN CERTIFICATE\-\-\-\-\-" ${pub_file} >/dev/null 2>&1
                                                        if [[ $? -eq 0 ]]; then
                                                                echo "  Enter alias name: ";read alias_name
                                                                echo "  Enter destination keystore(p12) name: "
                                                                read ks_name
                                                                if [[ -f ${ks_name} ]]; then
                                                                                echo "ERROR| file ${ks_name} already exists."
                                                                                exit 28
                                                                else
                                                                                echo "  building PKCS12 file ${ks_name}...";sleep 1
                                                                                openssl pkcs12 -export -name ${alias_name} -out ${ks_name} -inkey ${key_file} -in ${pub_file}
                                                                                if [[ $? -eq 0 ]]; then
                                                                                                echo "  created ${ks_name} successfully."
                                                                                else
                                                                                                echo "ERROR| occured creating ${ks_name}."
                                                                                                exit 27
                                                                                fi
                                                                fi
                                                        else
                                echo "ERROR| not a valid public key."
                                exit 26
                                                        fi
                                                else
                                                        echo "ERROR| not a valid private key."
                                                        exit 25
                                                fi
                fi
        fi
fi

if [[ ${got_keystore} -eq 1 ]]; then
        if [[ -f ${keystore_name} ]]; then
                echo
                echo """
                                1. MS PKI UAT
                                2. MS PKI Production
                                3. DigiCert/Symantec/VeriSign
                                4. Not applicable
                
                "
                echo "  Select CA type:"
                read ca_type
                case ${ca_type} in 
                        1)
                                ca_certificates=/Internal-UAT
                                ;;
                        2)
                                ca_certificates=/Internal
                                ;;
                        3)
                                ca_certificates=/External
                                ;;
                        4)
                                ca_certificates=`pwd`;
                                ;;
                        *)
                                echo "ERROR| Invalid input."
                                exit 1
                                ;;
                esac
                java -jar ${jar_location}/CustomKeytool.jar ${keystore_name} ${ca_certificates}
        else
                echo "ERROR| File ${keystore_name} not found.";echo
                exit 31
        fi
fi

if [[ ${got_password} -eq 1 ]]; then
        file_type=`echo ${keystore_name} | awk -F "." '{print $NF}'`
        if [[ ${file_type} == "key" ]]; then
                echo "password" | perl ssltool -f ${keystore_name} 2>/dev/null
                exit 0
        elif [[ ${file_type} == "sth" ]]; then
                echo "password" | perl ssltool -f ${keystore_name} 2>/dev/null
                exit 0
        else    
                if [[ -f ${keystore_name} ]]; then
                        java -cp ${jar_location}/CustomKeytool.jar custom.ssl.tool.KeystorePassword ${keystore_name} ${encrypted_file}
                else
                        echo "ERROR| File ${keystore_name} not found.";echo
                        exit 31
                fi
        fi
fi

echo;exit 0;

#end-customkeytool
