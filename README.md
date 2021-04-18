# wsstepbystep

# Branches

## step01: empty project frame

## step02: REST API 
* empty CRUD operations in the book controller

## step03: Swagger documentation 
* http://localhost:8080/swagger-ui.html
* http://localhost:8080/v2/api-docs
* OpenAPI with https://editor.swagger.io/

## step04: Exception handling - ControllerAdvice annotation

## step05: Admin GUI

## step06: Security 1

## step07: Security with roles and permissions

## step08: Making the /authenticate endpoint
* generating a JWT token
* signing with the servers private key

## step09: Changing the authentication method of the book controller from Basic to Bearer

## step10: Validation of the REST input parameter

```
{
    "timestamp": "2021-04-11T13:02:05.754+00:00",
    "status": 400,
    "error": [
        "title size must be between 1 and 100! Rejected value: ''",
        "pages must be greater than or equal to 0! Rejected value: '-10'"
    ],
    "path": "uri=/books",
    "exception": {
        "message": "Validation failed for argument [0] in public hu.perit.wsstepbystep.rest.model.ResponseUri hu.perit.wsstepbystep.rest.api.BookController.createBook(hu.perit.wsstepbystep.rest.model.BookParams) with 2 errors: [Field error in object 'bookParams' on field 'title': rejected value []; codes [Size.bookParams.title,Size.title,Size.java.lang.String,Size]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [bookParams.title,title]; arguments []; default message [title],100,1]; default message [size must be between 1 and 100]] [Field error in object 'bookParams' on field 'pages': rejected value [-10]; codes [Min.bookParams.pages,Min.pages,Min.java.lang.Integer,Min]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [bookParams.pages,pages]; arguments []; default message [pages],0]; default message [must be greater than or equal to 0]] ",
        "exceptionClass": "org.springframework.web.bind.MethodArgumentNotValidException",
        "superClasses": [
            "org.springframework.web.bind.MethodArgumentNotValidException",
            "java.lang.Exception",
            "java.lang.Throwable",
            "java.lang.Object"
        ],
        "stackTrace": [
            {
                "classLoaderName": "app",
                "moduleName": null,
                "moduleVersion": null,
                "methodName": "resolveArgument",
                "fileName": "RequestResponseBodyMethodProcessor.java",
                "lineNumber": 139,
                "className": "org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor",
                "nativeMethod": false
            }
        ],
        "cause": null
    }
}
```

## step11: Turning HTTPS on
* generating a self signed certificate

## step12: Updating the Swagger docu with authorization methods

## step13: Configuring the management endpoints

## step14: Running the webservice in a docker container
* Creating docker image as part of the build process
* docker-compose

## step15: Integration test
* Testing the AuthController with @SpringBootTest
* Creating a webservice client interface with Spring Cloud Feign, to programmatically invoke the REST endpoints
* Testing the client interface with an integration test

## step16: Persistency
* docker-compose for PostgreSQL and pgadmin
* script for initializing the db
* Unidirectional, many-to-many relationship


## step17: Persistency - Bidirectional many-to-many relationship

## step18: JPA auditing

```
{
    "id": 13,
    "title": "New York - Budapest Metró",
    "authors": [
        {
            "id": 1,
            "name": "Vámos Miklós"
        }
    ],
    "pages": 240,
    "dateIssued": "2010-03-25",
    "createdBy": "admin",
    "createdAt": "2021-04-17 13:23:17.132",
    "updatedBy": "admin",
    "updatedAt": "2021-04-17 13:23:46.692"
}
```

## step19: Structuring property files properly
* Running the whole thing in docker

I have the following property files in the config dir

```
c:\np\github\wsstepbystep\webservice\src\main\dist\bin\config>dir
 Volume in drive C has no label.
 Volume Serial Number is BEA2-3C2D

 Directory of c:\np\github\wsstepbystep\webservice\src\main\dist\bin\config

2021.04.18  07:47    <DIR>          .
2021.04.18  07:47    <DIR>          ..
2021.04.17  14:16               654 application-default.properties
2021.04.17  09:48               906 application-dev.properties
2021.04.18  07:47               905 application-devdocker.properties
2021.04.16  16:20               670 application-integtest.properties
2021.04.17  14:16             3 991 application.properties
               5 File(s)          7 126 bytes
               2 Dir(s)  335 745 679 360 bytes free
```

When starting the webservice application without defining the active profile, the spring framework will load application.properties and application-default.properties. We also have to know, that there is a possibility to define the active profile(s) in a property file. However in one of the recent versions of spring boot this feature is switched off by default, we can reactivate it, like this:

application.properties
```
# To enable spring.profiles.include
spring.config.use-legacy-processing=true
```

Now put all the standard, I mean environment independent configuration into application.properties. You can use application-default.properties to define a default profile, so that you the webservice always starts without any JVM parameter.

application-default.properties
```
spring.profiles.include=dev
```

The dev profile in application-dev.properties is good for running the webservice in the IDE ore starting it from a command line.

application-dev.properties
```
datasource.postgres.db-type=postgresql
datasource.postgres.host=localhost
datasource.postgres.db-name=wsstepbystep
datasource.postgres.username=postgres
datasource.postgres.encrypted-password=1tvmTu8Ya8A=
datasource.postgres.ddl-auto=validate
```

Of course when running the database and the webservice in docker, the localhost will not work, so we have to define another parameter in `datasource.postgres.host`. Our application-devdocker.properties looks like this:
```
datasource.postgres.db-type=postgresql
datasource.postgres.host=postgres
datasource.postgres.db-name=wsstepbystep
datasource.postgres.username=postgres
datasource.postgres.encrypted-password=1tvmTu8Ya8A=
datasource.postgres.ddl-auto=validate
```

We are using the host `postgres` which is the host name of the database withing the docker swarm.

Now in the docker-compose.yml we define the `WEBSERVICE_OPTS` environment variable to activate the devdocker profile.

```
    #####################################################################################                 
    webservice:
    #####################################################################################                 
        container_name: webservice
        image: webservice
        environment:
              - JAVA_OPTS=
              - WEBSERVICE_OPTS=-Dspring.profiles.active=devdocker
        ports:
            - "8080:8080"
        networks: 
            - back-tier-net
            - monitoring
        hostname: 'webservice'

```

That way spring will load application.properties and devdocker.properties. See the log:

```
c:\np\github\wsstepbystep\docker-compose\dev>docker ps
CONTAINER ID        IMAGE                   COMMAND                  CREATED             STATUS              PORTS                    NAMES
cf0ed0d285b3        webservice              "sh ./webservice"        20 seconds ago      Up 4 seconds        0.0.0.0:8080->8080/tcp   webservice
8393a610098e        thajeztah/pgadmin4      "python ./usr/local/…"   21 seconds ago      Up 6 seconds        0.0.0.0:5400->5050/tcp   pgadmin
55406095a312        postgres:10.13-alpine   "docker-entrypoint.s…"   22 seconds ago      Up 7 seconds        0.0.0.0:5432->5432/tcp   postgres

c:\np\github\wsstepbystep\docker-compose\dev>docker logs -f webservice

 __        ______        _                   _                     _
 \ \      / / ___|   ___| |_ ___ _ __       | |__  _   _       ___| |_ ___ _ __
  \ \ /\ / /\___ \  / __| __/ _ \ '_ \ _____| '_ \| | | |_____/ __| __/ _ \ '_ \
   \ V  V /  ___) | \__ \ ||  __/ |_) |_____| |_) | |_| |_____\__ \ ||  __/ |_) |
    \_/\_/  |____/  |___/\__\___| .__/      |_.__/ \__, |     |___/\__\___| .__/
                                |_|                |___/                  |_|
                                       project

Alpine 11.0.10+11-alpine-r0
Spring-Boot: 2.4.5
webservice: 1.0.0-SNAPSHOT

Author: Peter Nagy <nagy.peter.home@gmail.com>

2021-04-18 07:50:31.936 INFO  --- [main           ] h.p.w.WsstepbystepApplication   55 : Starting WsstepbystepApplication v1.0.0-SNAPSHOT using Java 11.0.10 on webservice with PID 1 (/usr/src/webservice/lib/webservice-1.0.0-SNAPSHOT.jar started by root in /usr/src/webservice/bin)
2021-04-18 07:50:31.944 DEBUG --- [main           ] h.p.w.WsstepbystepApplication   56 : Running with Spring Boot v2.4.5, Spring v5.3.6
2021-04-18 07:50:31.944 INFO  --- [main           ] h.p.w.WsstepbystepApplication  679 : The following profiles are active: devdocker
2021-04-18 07:50:32.037 DEBUG SPR [main           ] igDataEnvironmentPostProcessor 252 : Switching to legacy config file processing [[ConfigurationProperty@57abad67 name = spring.config.use-legacy-processing, value = 'true', origin = URL [file:config/application.properties] - 90:37]]
2021-04-18 07:50:32.039 DEBUG SPR [main           ] .ConfigFileApplicationListener 252 : Activated activeProfiles devdocker
2021-04-18 07:50:32.040 DEBUG SPR [main           ] .ConfigFileApplicationListener 252 : Loaded config file 'file:./config/application.properties' (file:./config/application.properties)
2021-04-18 07:50:32.040 DEBUG SPR [main           ] .ConfigFileApplicationListener 252 : Loaded config file 'file:./config/application-devdocker.properties' (file:./config/application-devdocker.properties) for profile devdocker
2021-04-18 07:50:36.380 INFO  SPR [main           ] o.s.b.w.e.t.TomcatWebServer    108 : Tomcat initialized with port(s): 8080 (https)
2021-04-18 07:50:37.319 DEBUG --- [main           ] h.p.w.d.p.PostgresDbConfig      81 : creating DataSource for 'postgres'
2021-04-18 07:50:37.351 INFO  --- [main           ] h.p.s.s.d.d.ConnectionParam     81 : jdbc:postgresql://postgres:5432/wsstepbystep
2021-04-18 07:50:42.859 WARN  SPR [main           ] figuration$JpaWebConfiguration 221 : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2021-04-18 07:50:45.382 INFO  SPR [main           ] o.s.b.w.e.t.TomcatWebServer    220 : Tomcat started on port(s): 8080 (https) with context path ''
2021-04-18 07:50:46.098 INFO  --- [main           ] h.p.w.WsstepbystepApplication   61 : Started WsstepbystepApplication in 14.893 seconds (JVM running for 15.794)
2021-04-18 07:51:25.954 DEBUG SPR [nio-8080-exec-6] .a.d.DaoAuthenticationProvider 199 : Authenticated user
2021-04-18 07:51:26.023 DEBUG --- [nio-8080-exec-6] .s.s.l.AbstractInterfaceLogger 166 : HTTP headers: authorization=***;user-agent=PostmanRuntime/7.26.10;accept=*/*;postman-token=a3f672fb-6057-49a8-a684-e970777df4c1;host=localhost:8080;accept-encoding=gzip, deflate, br;connection=keep-alive;cookie=JSESSIONID=47FCA9FF0324D22EB5BE30D271872E9E;
2021-04-18 07:51:26.024 DEBUG --- [nio-8080-exec-6] .s.s.l.AbstractInterfaceLogger  57 : >>> | 172.21.0.1 | null | user: admin | host: webservice | system: auth-controller | eventId: 1 | event: authenticateUsingGET | null 
2021-04-18 07:51:26.682 DEBUG --- [nio-8080-exec-6] .s.s.l.AbstractInterfaceLogger 166 : HTTP headers: authorization=***;user-agent=PostmanRuntime/7.26.10;accept=*/*;postman-token=a3f672fb-6057-49a8-a684-e970777df4c1;host=localhost:8080;accept-encoding=gzip, deflate, br;connection=keep-alive;cookie=JSESSIONID=47FCA9FF0324D22EB5BE30D271872E9E;
2021-04-18 07:51:26.683 DEBUG --- [nio-8080-exec-6] .s.s.l.AbstractInterfaceLogger  73 : <<< | 172.21.0.1 | null | user: admin | host: webservice | system: auth-controller | eventId: 1 | event: authenticateUsingGET | SUCCESS
```

Just perfect!

## step20: Improving the health endpoint

Let's see what we already have. We switch on the db part of the health management endpoint: `management.health.db.enabled=true`

https://www.localhost.hu:8080/actuator/health
```
{
   "status":"UP",
   "components":{
      "db":{
         "status":"UP",
         "details":{
            "database":"PostgreSQL",
            "validationQuery":"isValid()"
         }
      },
      "diskSpace":{
         "status":"UP",
         "details":{
            "total":511455899648,
            "free":335739858944,
            "threshold":10485760,
            "exists":true
         }
      },
      "ping":{
         "status":"UP"
      }
   }
}
```

Cool. This is what we got from the spring framework for free. This is almost perfect, but there is one major drawback. The health endpoint might be called by the load balancer to check if the service is up and running. This has  a frequency of 5 seconds, so the health endpoint must be executed within a couple of milliseconds, otherwise the load balancer will assume, our service is down. We have a similar case when monitoring the service with prometheus. Prometheus polling our service in every 15 seconds. Now, the database timeout is generally higher to allow slow running queries to execute. I am using generally 120 seconds or 90 seconds depending on the slowest query. As a consequence, if the database is down, e.g. we have a network outage, we will not receive an answer from the database any sooner then the value of the database timeout. This is a problem, in case of the health endpoint. We just need an answer from the database within let's say 2 seconds. If there is no answer within 2 seconds, we have to signal a database down status. So we take an easy query to check the database and execute it asynchronously. Let's see how?

First of all we set `management.health.db.enabled=false` again, and implement the class `HealthIndicatorDatabase`.

```
@Component
@Slf4j
public class HealthIndicatorDatabase extends AbstractHealthIndicator
{
    @Autowired
    private NativeQueryRepo nativeQueryRepo;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception
    {
        LocalDateTime timestamp = LocalDateTime.now();
        builder.withDetail("Timestamp", timestamp.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_JACKSON_TIMESTAMPFORMAT)));

        try
        {
            boolean dbUpAndRunning = AsyncExecutor.invoke(this::checkDbUpAndRunning, null);
            if (dbUpAndRunning)
            {
                builder.up();
                builder.withDetail("Status", "Database server is up and running");
            }
            else
            {
                builder.down();
                builder.withDetail("Status", "Database server cannot be reached!");
            }
        }
        catch (RuntimeException ex)
        {
            log.error(String.format("Health check failed: %s", ex));
            builder.down();
            builder.withDetail("error", ex);
        }
        catch (TimeoutException ex)
        {
            log.error("Health check failed: the database server cannot be reached!");
            builder.down();
            builder.withDetail("Status", "Database server cannot be reached!");
        }
    }


    private boolean checkDbUpAndRunning()
    {
        Object result = this.nativeQueryRepo.getSingleResult("SELECT 1", false);
        return result instanceof Integer && ((Integer) result).equals(1);
    }
}
```

Now our health endpoint provides the following information.

```
{
   "status":"UP",
   "components":{
      "diskSpace":{
         "status":"UP",
         "details":{
            "total":511455899648,
            "free":335724470272,
            "threshold":10485760,
            "exists":true
         }
      },
      "healthIndicatorDatabase":{
         "status":"UP",
         "details":{
            "Timestamp":"2021-04-18 09:54:34.076",
            "Status":"Database server is up and running"
         }
      },
      "ping":{
         "status":"UP"
      }
   }
}
```

Now if I stop the docker container of the database, let's see if anything changes?

```
{
   "status":"DOWN",
      "diskSpace":{
         "status":"UP",
         "details":{
            "total":511455899648,
            "free":335726088192,
            "threshold":10485760,
            "exists":true
         }
      },
      "healthIndicatorDatabase":{
         "status":"DOWN",
         "details":{
            "Timestamp":"2021-04-18 09:58:41.721",
            "Status":"Database server cannot be reached!"
         }
      },
      "ping":{
         "status":"UP"
      }
   }
}
```

And yes, the database status changes to DOWN. That's pretty much it.

