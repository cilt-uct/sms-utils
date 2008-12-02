ABOUT THE SMS RSF WINDOWS
=========================
The ui is currently in a separate Eclipse project. It will be merged into the sms tool project later on. 
They are currently stand-alone RSF windows that run in jetty. Sakai stylesheets has been attached. Jetty is used during the
development process for its startup speed.


STARTING UP JETTY
=================
* Make sure the sms_ui project is built using: mvn clean install
* Start up jetty using: mvn jetty:run
* To change the port, edit the <port> tag in pom.xml


SMS TEST WINDOW
===============
The sms test window is used to send a single message to the gateway. It is used to test the smpp service. This window will later 
be changed to send messages to a Sakai group. When a message is send from the window, the jetty log and the simulator log should 
be monitored to see the response. The smpp service will report the gateway message id in the debug panel. 
Url: http://localhost:8080/sms_ui/faces/sms_test


SMS CONFIG WINDOW
=================


SMS ACCOUNT ADMIN WINDOW
========================
This will list all sms accounts in the current Sakai site. Accounts can be added and changed. 
Sakai permissions will be added in delivery 7. System wide accounts will be handled by the same window.


SMS TASK ADMIN WINDOW
=====================
List all tasks for the current site, or system wide. Tasks can be sorted on all columns and the search 
filter can be applied to quickly find records.