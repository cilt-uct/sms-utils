ABOUT THE SMS RSF WINDOWS
=========================

The ui is currently in a separate Eclipse project. It will be merged into the sms tool project later on. 
They are currently stand-alone RSF windows that run in jetty.


STARTING UP JETTY
=================

* Make sure the sms_ui project is built using: mvn clean install
* Start up jetty using: mvn jetty:run
* To change the port, edit the <port> tag in pom.xml


SMS TEST WINDOW
===============
The sms test window is used to send a single message to the gateway. It is used to test the smpp service. This window will later 
be changed for sending messages to a sakai group. When a message is send from the window, the jetty log and the simulator log should 
be monitored to see the response. The smpp service will report the gateway message id in the debug panel. 
Url: http://localhost:8080/sms_ui/faces/sms_test


SMS CONFIG WINDOW
=================

SMS BILLING WINDOW
==================

SMS TASK ADMIN WINDOW
=====================