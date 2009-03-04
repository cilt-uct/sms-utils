ABOUT THE SMPP SIMULATOR
========================
This Open Source simulator from Selenium can simulate real life scenarios like the the random failing of message deliveries.
See http://www.seleniumsoftware.com/user-guide.htm. The simulator will be used during development, but a real smmp gateway must
be available during the final phase of the project.


GENERAL NOTES:
======
* The simulator will run on port 2775 on Windows or Linux. If required, the port can be changed with SMPP_PORT in smppsim.props.
* It is advised to run the simulator on a separate machine to emulate real life conditions. 
* When running the smpp service, make sure of the simulator ip address with SMSCadress in smpp.properties.


HOW TO RUN THE SIMULATOR
========================
* Start simulator by running startsmppsim.bat or startsmppsim.sh


Simulating incoming (MO) messages
=================================
* Edit deliver_messages.csv, first field is mobile number, second field is the sourceAddress as set up in smpp.properties and last field is the actual body.
* Edit the "MO SERVICE" section in smppsim.props and change DELIVERY_MESSAGES_PER_MINUTE. Make zero to disable MO
* MO messages can also be injected via the web interface of the simulator running at http://localhost:88/ 