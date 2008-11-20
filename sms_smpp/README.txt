HOW TO TEST THE SMPP SERVICE
============================

1.  Start up the smpp simulator 
1.1 Build the smpp-sim project by running: mvn clean install
1.2 Start simulator by running starttestservers.bat or starttestservers.sh
1.3 The simulator can also be run on a separate machine

2.  Run the unit tests
2.1 Build the smpp project: mvn clean install
2.2 Check that the simulator and test are running on the same port:
2.3 See SMSCport in smpp.properties and SMPP_PORT in smppsim.props
2.2 Run SmppThreadingTest.java as a JUnit Test
