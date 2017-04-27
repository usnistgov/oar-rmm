# OAR-RMM-SERVICE

This is the web service project to get resource metadata.
This project is part of Data Dissemination effort under OAR (Open Access for Research). OAR is work area under Office of Data Informatics (ODI) in NIST MML lab. 
This project is designed to develope a web service available publicly for users to access NIST public data listing. NIST public data listing follows POD schema, a federal government guideline for open access to reseach data publication. 
For the project we have MongoDB backend and also enhanced POD schema called NERDm, which has more metadata fields and descriptions

## Installation
The project is a maven based java project. To build: 
- Access: project  `git clone <oar-rmm>`
- Go to:  `cd oar-rmm `
- Edit: config files for database server acccess (By default uses localhost)
- Run: `mvn clean package` 

## System requirements to run this project
1. Git
2. java
3. maven
4. MongoDB database server access

## Layout
```
oar-rmm/                    				--> main folder
	src/						--> code source
	 main/java/gov/nist/oar/rmm/ 			--> code namespace
    	pom.xml 			                --> maven pom file
```
## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Environment for development
* Java 1.8 : Oralacle Java 8+
* Maven : latest
* MongoDB : version 3.0+
* Use oar-pdr project to get the data in mongodb

## History

The previous RMM design used Spring data framework to create restful web service, it was easy to implement but needed some data binding. 

## Credits

TODO: Write credits

## License

TODO: Write license

## Disclaimer
```
NIST-developed software is provided by NIST as a public service. You may use, copy and distribute copies of the software in any medium, provided that you keep intact this entire notice. You may improve, modify and create derivative works of the software or any portion of the software, and you may copy and distribute such modifications or works. Modified works should carry a notice stating that you changed the software and should note the date and nature of any such change. Please explicitly acknowledge the National Institute of Standards and Technology as the source of the software.
NIST-developed software is expressly provided "AS IS." NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED, IN FACT OR ARISING BY OPERATION OF LAW, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT AND DATA ACCURACY. NIST NEITHER REPRESENTS NOR WARRANTS THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED OR ERROR-FREE, OR THAT ANY DEFECTS WILL BE CORRECTED. NIST DOES NOT WARRANT OR MAKE ANY REPRESENTATIONS REGARDING THE USE OF THE SOFTWARE OR THE RESULTS THEREOF, INCLUDING BUT NOT LIMITED TO THE CORRECTNESS, ACCURACY, RELIABILITY, OR USEFULNESS OF THE SOFTWARE.
You are solely responsible for determining the appropriateness of using and distributing the software and you assume all risks associated with its use, including but not limited to the risks and costs of program errors, compliance with applicable laws, damage to or loss of data, programs or equipment, and the unavailability or interruption of operation. This software is not intended to be used in any situation where a failure could cause risk of injury or damage to property. The software developed by NIST employees is not subject to copyright protection within the United States.
```
