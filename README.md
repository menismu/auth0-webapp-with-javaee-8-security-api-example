# Auth0 Java EE 8 Security API Example

## Overview

This example shows how to integrate [Auth0 Authentication API](https://auth0.com/docs/api/authentication#introduction) in your application developed with Java EE 8 following [Java EE 8 Security API](https://javaee.github.io/security-spec/) (JSR 375).

The example uses a [Regular Web Application](https://auth0.com/docs/applications/webapps) to grant access to the user, and it also shows how to validate given token added to the session when user logs in. 

## Dependencies

- Auth0 account (create one if you don't have [here](https://auth0.com/)).
- Auth0 API (create one using Auth0 Manager, choose RS256 as Signing Algorithm)
- Regular Web Auth0 Application (create one using Auth0 Manager and choose API created above)
- Java EE Container ([Payara Micro](https://www.payara.fish/payara_micro) recommended)

## Running the example 

Assuming you have already checked out the repository example, the first step is to open the file 'src/main/resources/application.properties' and replace the values below

```
{YOUR_DOMAIN}: go to your application created and copy the value in Domain (e.g. myapp.auht0.com)
{YOUR_CLIENT_ID}: you can also find it in your application settings tab
{YOUR_CLIENT_SECRET}: as above, you can find it in your application settings tab within Auth0
```

Using your favourite Unix shell change the directory to where you have downloaded the repository and run the command below to build the war package.

```
./gradlew war
```

If you use Windows, just run 'gradlew.bat war'.

Now it is time to deploy and run the War package using Payara Micro as follows, substituing <path_to_payara_micro> by the abolute path where you have installed Payara Micro. Note your war filename could be different, just check before you run the war file created in the 'libs' folder.

```
java -jar <path_to_payara_micro>/payara-micro-5.183.jar --deploy build/libs/auth0-webapp-with-javaee-8-security-api-example.war 
 
```

After few seonds Payara Micro shows you the war has been deployed and it is ready to be used. 

## Playing with the example

Once the war has started, you can access the application using a browser and opening the url below:

```
http://{YOUR_IP}:8080/auth0-webapp-with-javaee-8-security-api-example/user
```

Note you need to substitute {YOUR_IP} by your local ip address.

Once the page is loaded you can click in the link and the browser will be redirected to Auth0 Widget.

After enter your email and validation code received, the browser is redirected to /callback Servlet where token is extracted and added to the session, and after that the browser is redirected to /user servlet which checks the logged use id and propmpts it in the page.

A logout link is provided in the /user page to remove the session details prior redirects the browser to the home page.