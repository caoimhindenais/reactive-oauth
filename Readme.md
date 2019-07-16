#Password Grant Flow


Project to get to try out the combination of 
- Spring boot reactive 
- OAuth Password grant type flow
- Verifying Access tokens (JWT) signed with a private RS256 keys and verify with a public key

>The Password grant type is used by first-party clients to exchange a user's credentials for an access token. 


_Note: More details to follow_
      
```
keytool -genkeypair -alias token -keyalg RSA -dname "CN=token, L=KILARNEY, S=KERRY, C=IE" -keypass secret -keystore keystore.jks -storepass secret        
keytool -list -rfc --keystore keystore.jks | openssl x509 -inform pem -pubkey
```

```
mvn clean install -DskipTests && docker build . -t reactive-oauth  && docker run --name reactive-oauth -d -p8080:8080 reactive-oauth 
```

```
curl -H "Authorization: Basic $(echo username:secret | base64 )"  'localhost:8080/token?roles=flip&subject=jane'```

export TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJyb2xlcyI6ImZsaXAiLCJleHAiOjE1NjM0NjgzMDR9.CEvU1k7e7PFSAmX-NSKzldKhhfbVoFK3U-_oEcJeOmlxAK-jX4njuz7WQHxmPQfezIbCAdexTCSrBfhkSOiRxjk7EaZyFIQ8ax0Yf7EmEsxQF73lx7MqfoySk162kyul_uQ09KXntzWGWPblovPT5Sxc_S_znZheVi_8XbAUM9O-Xvcj33Ny7iUjdxorohOdB-szJr_96xQ0wfOw-Ohb-KaAYN9VOZdW6EOK412MnZdoQjNyCy8Tg-AF-iDlZldZJz8t5L-dvj_XkIkCj2irF8ooIsHmc5gXZTe_cWmVIWJID_PkrIXeLuFE10yaBTt2l0RjGZzB6FHTQvZVsMvAfw"

curl -H "Authorization: Bearer "$TOKEN localhost:8080/flip
```

* Q: So why does 256 SSL have a 2048 bit Key ?* 

>        The 256-bit is about SSL. In SSL, the server key is used only to transmit a random 256-bit key
>        (that one does not have mathematical structure, it is just a bunch of bits);
>        roughly speaking, the client generates a random 256-bit key, encrypts it with
>        the server's RSA public key (the one which is in the server's certificate and is
>        a "2048-bit key"), and sends the result to the server. The server uses its private
>        RSA key to reverse the operation, and thus obtain the 256-bit key chosen by the client.
>        Afterwards, client and server use the 256-bit to do symmetric encryption and
>        integrity checks, and RSA is not used any further for that connection.



_Resources_
- https://www.baeldung.com/spring-security-oauth-jwt
- https://www.cnblogs.com/softidea/p/7414438.html
- https://github.com/auth0/java-jwt#create-and-sign-a-token
- http://www.bubblecode.net/en/2016/01/22/understanding-oauth2/