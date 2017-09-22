# Overview
It is licensed under [MIT](https://opensource.org/licenses/MIT).

## First of all
This is an example using [google-oauth2-client-servlet](https://github.com/riversun/google-oauth2-client-servlet.git) library.
You can check detailed OAuth2-flow that the library supports on [this page](https://github.com/riversun/google-oauth2-client-servlet.git).

## Example of servlet app that supports "login with Google"

- Enable "Login with Google" by OAuth2/OpenId connect
- Get OAuth2 credential(access_token,refresh_token)

<img src="https://riversun.github.io/img/goauth2/lib_oauth2_example02a.png">  

<img src="https://riversun.github.io/img/goauth2/lib_oauth2_example02b.png">  


## How to import into your Eclipse and Run.

### Import into Eclipse

1.Select File>Import>Git - Projects from Git  

2.Clone URI  

3.set clone URI to https://github.com/riversun/google-login-servlet-example-on-jetty.git

4.Select next along the flow  

5.Check "Import as general project" and select "finish"  


### Run example

1. Right click on imported project  
1. Select **Configure>Convert to Maven project**
(Now you can handle this project as a maven project) 

1. run **com.example.MyAppMain**

### Usage

**Example app flow**

1. Click **Login with Google**
1. authentication (with id/password for Google Account)
1. authorization (with Google's consent screen/permission check)
1. login to app with uniqueId provided by Google
1. Get userInfo from Google with access_token(refresh_token)
1. Click **Log out** to forget OAuth2 state and set app state "logout"

<img src="https://riversun.github.io/img/goauth2/lib_oauth2_example02a.png">

<img src="https://riversun.github.io/img/goauth2/lib_oauth2_example02b.png">
