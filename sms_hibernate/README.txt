The SMS hibernate logic is currently in a separate Eclipse project. 
It handles all data access using hibernate.


DATABASE SETUP
==============
Two separate hibernate config files are used. One is the default one used by the sms service, and the other is specifically for
unit test. The unit test will recreate the mySql schema before every run.
Please configure:
* hibernate.properties
* hibernate-test.properties


UNIT TESTS
==========
A series of unit tests are used to test all aspects of data access. Make sure that the mySQL connection is set up and that the database exists.
* SmsAccountTest.java
* SmsConfigTest.java
* SmsMessageTest.java
* SmsTaskTest.java
* SmsTransactionTest.java
* SmsDatabaseStressTest.java

Use SmsTestSuite to run all of the above or use "mvn test" in command line. 