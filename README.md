## Experimental maven multi-project setup for Magarena.

Please note, the JavaFX projects, ``magarena-fx`` and ``magarena-fxml`` require version 8 of Java.

### Clone local copy
Clone repository to local folder named ``magarena-ws``...  
``git clone https://github.com/lodici/magarena-ws.git``  

### Netbeans Configuration
First create a Project Group...   

1. File -> Project Groups.
2. Click _New Group_ to open group dialog.  
3. Enable _Folder of Projects_ and browse to the folder in which the clone was created.  
4. Click _Create group_ and select to open all projects.

#### Point to Java 8
Update each of the following projects to use the java 8 compiler -

- _Magarena JavaFX UI_
- _Magarena JavaFXml UI_
- _Magarena Root_

...for each project...

1. File -> Project Properties
  1. Sources -> Source/Binary Format = 1.8.
  2. Build -> Compile -> Java Platform = 1.8.
     * You will need to create the platform if it does not exist by locating the installation of java 8.  

#### Swing Dependencies
``magarena-swing`` uses the [TinySound](https://github.com/finnkuusisto/TinySound) library which is NOT available from the main Maven repository. To use...
- download jar to local.
- in Netbeans, right click the Swing project -> Dependencies node and select the _Add Dependency_ menu option.
- Enter _Group id = org.kuusisto, artifact = tinysound, version = 1.1.1_ and click _Add_.
- right click the newly added dependency node and select the _Manually install artifact_ option.
- locate the downloaded jar and save.
This should copy the jar into the local maven repository which will be used in subsequent builds.

#### Build root
Right click _Magarena Root_ and select _Build with Dependencies_ to build each project in the correct order automatically downloading any missing dependencies.
