The SMS hibernate logic is currently in a separate Eclipse project. 
It handles all database access using Hibernate. A logic can be tested using Unit Test.

BUILDING
========
Run: mvn clean install

DATABASE SETUP
==============
Two separate hibernate config files are used. One is the default one used by the sms tool, and the other is specifically for
Unit Tests. The unit test will recreate the mySql schema before every run.
Please configure:
* hibernate.properties
* hibernate-test.properties

UNIT TESTS
==========
A series of Unit Tests are used to test all aspects of data access. 
Make sure that the mySQL connection is set up and that the MySQL database exists.
* SmsAccountTest.java
* SmsConfigTest.java
* SmsMessageTest.java
* SmsTaskTest.java
* SmsTransactionTest.java
* SmsDatabaseStressTest.java

Use SmsTestSuite.java to run all of the above in Eclipse or use "mvn -Dtest=* test" in command line. 