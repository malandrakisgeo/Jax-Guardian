# Java-EE-Security
A customized Java EE security module for Jax-RS endpoints.

Goal: Secure a JAX-RS RESTful API with a Java-EE compliant security module.
Once a user successfully logs in with a username and a password, a token will be generated and used for Authorization, 
as well as a JSESSIONID for the particular session.

If the token is used by another machine while the session is active, the token will be regarded as compromised and be invalidated.

I started working on it to get more familiar with the Java EE security standards, and perhaps help others with this as well.

The project is under construction. So far it has been tested and works on Wildfly 25. 

