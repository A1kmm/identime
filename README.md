Identime
========

Identime is an OpenID web application which will, when completed, offer a way
to create your own public OpenID Provider that works out of the box.

It is still a work in progress; I plan to improve it in the future. However,
it is usable as it is now.

Identime is built to run in a Servlet container; it is based on OpenID4J,
Spring, Hibernate and a number of other Java technologies.

Is there a demo site?
---------------------

Feel free to try it out on [my development site](http://home.amxl.com/identime) -
but be aware that this is only a test site - it doesn't support SSL, 

What is an OpenID Provider?
---------------------------

OpenID Providers validate the identity of users to other sites (called Relying
Parties).

Many existing services provide OpenID Providers, including Google, WordPress,
and so on. However, I am not aware of any stand-alone OpenID Provider software
packages can that be deployed to make your own OpenID Provider.

How can I build and use Identime?
---------------------------------

You will need a working Java Servlet Container; if you don't already have one,
[Apache Tomcat](http://tomcat.apache.org) has been tested and confirmed to work.

You can either download a prebuilt WAR archive for a release (under releases on
github) that you can deploy straight away, or build it yourself using Maven.

If you choose to build it yourself:

Install maven, checkout identime (git clone https://github.com/A1kmm/identime), and then run:

    mvn war:war
    
to build a .war file (which will be built in the targets directory) that you
can deploy using the manager interface of your Servlet container.

Alternatively, if using Tomcat7, you can used the tomcat7:deploy goal (or
tomcat7:redeploy if it is already deployed). You will need to set properties
like the manager username and password you set up in conf/tomcat-users.xml
for Tomcat7.

    mvn tomcat7:redeploy -Dmaven.tomcat.url=http://example.org:8080/manager/text \
       -Dtomcat.username=userwithmanager-scriptrole -Dtomcat.password=thepassword

Note that if you deploy it like this, the URL will be http://example.org:8080/identime/

How do I administer the server?
-------------------------------

The first thing you should do after setting up the server is to register a user,
because the first user to register automatically gets full privileges to change
settings on the server. Go to /register (relative to the path Identime is
deployed at) and sign up.

Now you will need to change the server settings, which you can do at /admin/settings

You will definitely need to set up the base URL - if you deployed to http://example.org:8080/identime/
this should be http://example.org:8080/identime (unless you want to put a
reverse proxy server in front of Identime). It should not end with a /.

You will also want to change the "E-mail server to send e-mails from" to one
you have access to, and the 'From e-mail for e-mails sent out' to suit your
site.

How do I use Identime?
----------------------

Go to / (the root of your server) and, once you are logged in, it will show you
the Claimed ID string that you can use (it should start with http or https if
you have set up your base URL correctly).

Enter this URL into an OpenID Relying Party that you want to log in to, and if
everything is set up correctly, it should redirect you back to your Identime
instance (if not, check your base URL!).

If you aren't logged in, it will ask you to log in. Once logged in, it will ask
you to confirm you want to log in to the Relying Party; hit confirm to log in
at the Relying Party.
 
How do I pronounce Identime?
----------------------------
It is supposed to rhyme with 'Identity' - I-dent-ih-me.
