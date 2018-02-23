# Signup-in-out
### Create config file:
```
development.driver=com.mysql.jdbc.Driver
development.username=root
development.password=1234 
development.url=jdbc:mysql://localhost/linkedin
```
### Common errors:
> Instrumentation error

```
Maven Projects --> Plugins --> activeJdbc --> activeJdbc:instrument --> Run Maven Build
```

### Keep in mind:
- Surround each exception with try/catch.
- Document each function (/** + Enter).
- Use logger FATAL/WARNING/INFO. (log4j)
- WARNING in case the exception don't affect the major entities (DB, server, cache).
- FATAL (DB, server, cache).



