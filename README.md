# GitHub Repositories API

## Table of contents
* [Info](#info)
* [Setup](#setup)
* [Future features](#future features)

## Info
API allows you to:

* list repositories (name, stars amount)
* return sum of stars in all repositories

for specific GitHub user.

## Setup
To deploy this project into Tomcat Server, install:

* JDK - version 1.8 or above
* Apache Maven - version 3.6.3 or above
* Apache Tomcat - version 7.0, 8.5 or 9.0

After that, add **JDK** and **Maven** directories to your system **PATH**.

Next, clone the project using:
```
git clone https://github.com/Cordianh/github-repositories-api.git
```

When the project is cloned:
* **make sure you have added JDK and Maven to your system PATH 😉**
* open terminal in the project path ***.../github-repositories-api*** 
* run following command:
  ```
  mvn clean install
  ```
If build will be success, you should receive ***GitHubRepositoriesAPI.war*** file in ***/target*** directory.
Then:
* move ***GitHubRepositoriesAPI.war*** file to ***.../TomcatDirectory/webapps*** directory
* open terminal in ***.../TomcatDirectory/bin*** and execute:
  ```
  catalina.bat start
  ```
After that, application should be deployed into your Tomcat server.  
By default, Tomcat starts at **localhost:8080**, so you should be able to access API at:
```
localhost:8080/GithubRepositoriesAPI/
```
I hope everything went well 😊

## Future features
Things I'd like to add/fix:
* Pagination of requested items (e.g. user repositories).
* Endpoint which returns all available API endpoints with a brief description, updating automatically after changes in the application source code.
* OAuth2 token for requests authentication (access to private resources).
* Database to store all usernames with their total stars, which would be refreshed e.g. once a day.
* Ranking of users with the most total stars, based on database data.
