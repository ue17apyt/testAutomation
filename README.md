# Test Automation at Sogeti

## Test Tools and Completeness of Tasks

Test cases 1 to 3 in the block "UI Tests" are written in the [UserInterfaceTests.java](./src/test/java/com/capgemini/sogeti/testautomation/UserInterfaceTests.java). The test cases are created under the support of JUnit 5.7.1, Selenium 3.141.59 and Awaitility 4.0.3 (see [pom.xml](pom.xml)). In the test case 2, the method of bypassing the <b>recaptcha</b> has not yet been figured out. Therefore, the submit button will not be effective and later the Thank-You message will not be verified.

Test cases 1 to 2 in the block "API Tests" are generated in the [ApplicationProgrammingInterfaceTests.java](./src/test/java/com/capgemini/sogeti/testautomation/ApplicationProgrammingInterfaceTests.java). The test cares are prepared by the way of JUnit 5.7.1, Rest-Assured 4.3.3, Groovy 3.0.7 and Jayway JSON-Path 2.5.0 (see [pom.xml](pom.xml)).

## Test Execution

1. Download (Clone) the code from the github repository
2. Open the main folder of the downloaded (cloned) project within command line (or terminal).
3. Run <b><i>mvn test</i></b> to carry out all test methods
