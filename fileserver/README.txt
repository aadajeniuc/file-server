There are several solutions to this problem starting from simple sockets, RMI but most reasonable would be SOAP Web Services, REST or to build an FTP server
I implemented a REST server.
Arguments:
* Lightweight - not a lot of extra xml markup
* Human Readable Results
* Easy to build - no toolkits required
* The client can be a simple browser.



INSTALLATION INTRUCTIONS

To install the application   download and install Tomcat 5

find the app.war file under ./out/artifacts/fileserver_war/

rename this file to the context root you require followed by .war

open war file and modify content of web.xml file:
	- find following part of file:
	<context-param>
        <param-name>fileRoot</param-name>
        <param-value>C:\root</param-value>
    </context-param>
    <context-param>
        <param-name>tempDir</param-name>
        <param-value>C:\temp</param-value>
    </context-param>
	
	-modify fileRoot value(it is the folder which plays role of file server root folder)
	-modify tempDir value(a temporary directory used by server)

place the [contextroot].war file into the webapps directory of tomcat and restart tomcat.  

You need to add the security users to tomcat for this application

$CATALINA_HOME/conf/tomcat-users.xml

add a similar entry for each user

 <user name="pepa"   password="password" roles="viewer,manager" />

TEST INSTRUCTION

To run the tests you should have a latest version of ant installed
execute default ant target and find the results under ./build directory