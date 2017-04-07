# SYSC3010 - Fire Monitor

## Navigating the directory
-The android application and its components are present in the base directory of the GIT repository under the FlameMonitorApp folder. All the -application’s activity and class files can be seen in the following directory:
`SYSC3010/FlameMonitorApp/app/src/main/java/com/example/hassaan/flamemonitorapp/`
-Similarly, the layouts and resources for each of the activities can be viewed in the following directory:
`SYSC3010/FlameMonitorApp/app/src/main/res/`

-The server and database java classes are placed in two different locations within the GIT repository. In the base directory, there is a ServerRunner folder containing the server.java and database.java files along with a @serverrunner.txt text file (used for compiling the two files together). The two classes are also present in the src/database and bin/database directories. These can be launched using Eclipse as it has the Eclipse Java Project format. Also in the project src is a databaseTest.java file which is used for running the tests for the database.

-In the base directory, there is also a DesktopApp folder containing the FireGUI.java file and an Images folder, this is the desktop application. The final component of the system, the sensor script, is a python file in the base directory of the GIT repository called Sender.py.

## How to setup the Fire Monitor system
1. The system is designed with the server and database on the standalone, therefore the server and database need to be started there. Starting up the server is slightly complex. Since the server is dependent on the database, the two classes need to be compiled together in order to avoid throwing an error. Therefore, instead of doing “javac server.java”, we place the class names within a text file named “serverRunner.txt”. We then compile the files simultaneously using the command “javac @serverRunner.txt”. After compiling, the server is run using the “java server” command. After starting the server, a message is displayed that indicates the IP and port it is listening on. Any commands received and/or responded to are printed to the terminal. One must simply monitor the terminal output to see what the server is doing (these statements can be disabled by setting debugging to false in server.java).
2. Starting the sensor script is quite easy, just use the ”python Sender.py” command. The script immediately starts polling the sensors and waiting for a fire. Like the server, the sensor script prints what it’s currently detecting to the terminal, in addition to other operations that it performs (notifications it sends and acknowledge packets it receives). All these logging statements can be disabled by setting debugging to false in Sender.py.
3. The desktop app is is placed on the desktop and started with the “javac FireGUI.java” command. 
4. The android app can be opened with Android Studio and launched to either a physical device or run in the simulator. (NOTE: The video stream and notification do not work in the simulator).
