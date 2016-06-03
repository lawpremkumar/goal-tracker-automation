# goal-tracker-automation
goal-tracker-automation for https://github.com/theothermattm/goal-tracker-java

To run, you will need

* [Maven 3](http://maven.apache.org) or above installed  and JDK 1.8

* clone https://github.com/theothermattm/goal-tracker-java

To run using jetty locally:

    mvn clean jetty:run -Dspring.profiles.active=local
    


To Run the test suite
-----------------------
clone https://github.com/lawpremkumar/goal-tracker-automation.git

To run the test suite locally:
    
	mvn test -Dhost=localhost:8080 (Right now http:// is taken care by the test suite ).

Note : To run the test suite make sure you are running the application

