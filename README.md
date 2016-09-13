# oar-rmm 

This project is part of Data Dissemination effort under OAR (Open Access for Research). OAR is work area under Office of Data Informatics (ODI) in NIST MML lab. 
This project is designed to develope a web service available publicly for users to access NIST public data listing. NIST public data listing follows POD schema, a federal government guideline for open access to reseach data publication.

## System requirements to run this project
1. Git
2. java
3. maven
4. tomcat
5. database server access
 
## how to deploy
- git clone <oar-rmm> project
- Edit config files for database server acccess (By default uses localhost)
- mvn clean install
- mvn tomcat:deploy

## Environment for development
* Java 1.8 : Oralacle Java 8+
* Maven : latest
* MongoDB : version 3.0+
* Tomcat 8+ (Application server)
* Data.json (NIST public data list)
* Other Database files ....


