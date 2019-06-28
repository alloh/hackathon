
Prerequisites
1. JRE 1.8+
2. MYSQL 57
Building from sources
3. Make sure you have maven 3.5+ and its added to the path variable
4. Go to the root directory of the sources then run the below command
  mvn clean install -X

5. Executable will be generated in the target folder
6. Run the executable as a jar (Fat jar) using the below command
   java -jar hackathon-0.0.1-SNAPSHOT.jar
The application starts and deploys, when done you should be able to check if the application is up using the below get request
 http://localhost:8080/actuator/health

You can use postman or even browser to do the above test

Installation (Direct from the jar file already generated)

a)create a database in your mysql named movies
b) Run the jar file using command java -jar hackathon-0.0.1-SNAPSHOT.jar
c) Tables are created and initial data loaded

Call the endpoints as per the MovieData.json attached (i.e. you can import directly to postman)