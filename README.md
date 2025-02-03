## How to run the tests:

This project was built using Maven and all the dependencies, configurations, etc. are located in the pom.xml file.

If you have the IntelliJ IDE, simply open up the ShoppingCartTest file, click the play button, and then click the "run ShoppingCartTest with test coverage" button.

If you don't have the IntelliJ IDE, you can run the tests in the terminal using the following commands, the output is pretty trash tho compared to how the IDE does it. (NOTE: you may need to install Maven on your terminal):

- run tests without coverage: `mvn test`
- run tests with coverage: `mvn clean test jacoco:report` and then check the index.html file in `/target/site/jacoco`
- to delete the `target` folder run: `mvn clean`
