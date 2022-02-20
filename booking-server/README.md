# Create certificate by executing below command
   1. keytool -genkey -keyalg RSA -alias medium -keystore medium.jks -storepass password -validity 365 -keysize 4096 -storetype pkcs12
   Once you hit this command, it will prompt for a few details and the certificate will be created.
   2. Validity 1 year
   3. Password is password
# Copy this certificate in the src/main/resources directory so that it will be available at classpath.
# Add properties file configuration
    server:
      port : 8080
      ssl:
        key-store: 'classpath:medium.jks'
        key-store-type: pkcs12
        key-store-password: password
        key-password: password
        key-alias: medium
# You will ready to use https
    https://10.10.5.38:8080/v1/1/theatres