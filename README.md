# Signing (sign in/out/up):


## Description:
	A microservice handles registration and authentication.

----
### 1- Config File:
	    a- IP / Port : for external system
	    b- Rotation Policy (when to remove from Redis Cache) : 
		      i. Time Based : eg. every 20 min
		      ii. Retrieval (when data retrieved from Redis remove it assuming that it was read by user)	
		      iii. mixed
	    c. DataBaseConfigPath :
		      i. IP
		      ii. Port
		      iii. Credentials
		      iv. Name
	    d. Redis Config
	      	i. Host

### 2- External System:
	    As was specified in web com.linkedin.replica.services.
	
### 4- Database: 
	    Use mysql database to store the user credentials (email & password) and Arandodb in storing user's profile.


			
