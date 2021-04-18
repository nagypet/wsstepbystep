# wsstepbystep

# Contents

* step01: empty project frame
* step02: REST API 
* step03: Swagger documentation 
* step04: Exception handling - ControllerAdvice annotation
* step05: Admin GUI
* step06: Security 1
* step07: Security with roles and permissions
* step08: Making the /authenticate endpoint
* step09: Changing the authentication method of the book controller from Basic to Bearer
* step10: Validation of the REST input parameter
* step11: Turning HTTPS on
* step12: Updating the Swagger docu with authorization methods
* step13: Configuring the management endpoints
* step14: Running the webservice in a docker container
* step15: Integration test
* step16: Persistency
* step17: Persistency - Bidirectional many-to-many relationship
* step18: JPA auditing
* step19: Structuring property files properly
* step20: Improving the health endpoint
* step21: Improving the health endpoint even further
* step22: Monitoring - creating an application specific monitoring endpoint
* step23: Installing Prometheus and Grafana


# Branches

## step01: empty project frame

## step02: REST API 
* empty CRUD operations in the book controller

## step03: Swagger documentation 
* http://localhost:8080/swagger-ui/index.html
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


## step21: Merged with step20

## step22: Monitoring - creating an application specific monitoring endpoint

We already have the metrics and prometheus endpoints out of the box.

https://www.localhost.hu:8080/actuator/metrics
```
{
   "names":[
      "hikaricp.connections",
      "hikaricp.connections.acquire",
      "hikaricp.connections.active",
      "hikaricp.connections.creation",
      "hikaricp.connections.idle",
      "hikaricp.connections.max",
      "hikaricp.connections.min",
      "hikaricp.connections.pending",
      "hikaricp.connections.timeout",
      "hikaricp.connections.usage",
      "http.server.requests",
      "jdbc.connections.active",
      "jdbc.connections.idle",
      "jdbc.connections.max",
      "jdbc.connections.min",
      "jvm.buffer.count",
      "jvm.buffer.memory.used",
      "jvm.buffer.total.capacity",
      "jvm.classes.loaded",
      "jvm.classes.unloaded",
      "jvm.gc.live.data.size",
      "jvm.gc.max.data.size",
      "jvm.gc.memory.allocated",
      "jvm.gc.memory.promoted",
      "jvm.gc.pause",
      "jvm.memory.committed",
      "jvm.memory.max",
      "jvm.memory.used",
      "jvm.threads.daemon",
      "jvm.threads.live",
      "jvm.threads.peak",
      "jvm.threads.states",
      "logback.events",
      "process.cpu.usage",
      "process.start.time",
      "process.uptime",
      "system.cpu.count",
      "system.cpu.usage",
      "tomcat.sessions.active.current",
      "tomcat.sessions.active.max",
      "tomcat.sessions.alive.max",
      "tomcat.sessions.created",
      "tomcat.sessions.expired",
      "tomcat.sessions.rejected"
   ]
}
```

https://www.localhost.hu:8080/actuator/prometheus
```
# HELP jvm_threads_daemon_threads The current number of live daemon threads
# TYPE jvm_threads_daemon_threads gauge
jvm_threads_daemon_threads 21.0
# HELP system_cpu_usage The "recent cpu usage" for the whole system
# TYPE system_cpu_usage gauge
system_cpu_usage 0.0
# HELP hikaricp_connections Total connections
# TYPE hikaricp_connections gauge
hikaricp_connections{pool="HikariPool-1",} 10.0
# HELP jvm_memory_max_bytes The maximum amount of memory in bytes that can be used for memory management
# TYPE jvm_memory_max_bytes gauge
jvm_memory_max_bytes{area="nonheap",id="CodeHeap 'profiled nmethods'",} 1.2288E8
jvm_memory_max_bytes{area="heap",id="G1 Survivor Space",} -1.0
jvm_memory_max_bytes{area="heap",id="G1 Old Gen",} 5.36870912E8
jvm_memory_max_bytes{area="nonheap",id="Metaspace",} -1.0
jvm_memory_max_bytes{area="nonheap",id="CodeHeap 'non-nmethods'",} 5898240.0
jvm_memory_max_bytes{area="heap",id="G1 Eden Space",} -1.0
jvm_memory_max_bytes{area="nonheap",id="Compressed Class Space",} 1.073741824E9
jvm_memory_max_bytes{area="nonheap",id="CodeHeap 'non-profiled nmethods'",} 1.2288E8
# HELP jvm_threads_states_threads The current number of threads having NEW state
# TYPE jvm_threads_states_threads gauge
jvm_threads_states_threads{state="runnable",} 8.0
jvm_threads_states_threads{state="blocked",} 0.0
jvm_threads_states_threads{state="waiting",} 17.0
jvm_threads_states_threads{state="timed-waiting",} 5.0
jvm_threads_states_threads{state="new",} 0.0
jvm_threads_states_threads{state="terminated",} 0.0
# HELP process_start_time_seconds Start time of the process since unix epoch.
# TYPE process_start_time_seconds gauge
process_start_time_seconds 1.61872741584E9
# HELP hikaricp_connections_creation_seconds_max Connection creation time
# TYPE hikaricp_connections_creation_seconds_max gauge
hikaricp_connections_creation_seconds_max{pool="HikariPool-1",} 0.0
# HELP hikaricp_connections_creation_seconds Connection creation time
# TYPE hikaricp_connections_creation_seconds summary
hikaricp_connections_creation_seconds_count{pool="HikariPool-1",} 0.0
hikaricp_connections_creation_seconds_sum{pool="HikariPool-1",} 0.0
# HELP jvm_gc_memory_promoted_bytes_total Count of positive increases in the size of the old generation memory pool before GC to after GC
# TYPE jvm_gc_memory_promoted_bytes_total counter
jvm_gc_memory_promoted_bytes_total 1.8002944E7
# HELP jvm_buffer_total_capacity_bytes An estimate of the total capacity of the buffers in this pool
# TYPE jvm_buffer_total_capacity_bytes gauge
jvm_buffer_total_capacity_bytes{id="mapped",} 0.0
jvm_buffer_total_capacity_bytes{id="direct",} 169210.0
# HELP hikaricp_connections_active Active connections
# TYPE hikaricp_connections_active gauge
hikaricp_connections_active{pool="HikariPool-1",} 0.0
# HELP jdbc_connections_active Current number of active connections that have been allocated from the data source.
# TYPE jdbc_connections_active gauge
jdbc_connections_active{name="dataSource",} 0.0
# HELP jvm_gc_memory_allocated_bytes_total Incremented for an increase in the size of the (young) heap memory pool after one GC to before the next
# TYPE jvm_gc_memory_allocated_bytes_total counter
jvm_gc_memory_allocated_bytes_total 3.85875968E8
# HELP hikaricp_connections_idle Idle connections
# TYPE hikaricp_connections_idle gauge
hikaricp_connections_idle{pool="HikariPool-1",} 10.0
# HELP tomcat_sessions_active_current_sessions  
# TYPE tomcat_sessions_active_current_sessions gauge
tomcat_sessions_active_current_sessions 0.0
# HELP system_cpu_count The number of processors available to the Java virtual machine
# TYPE system_cpu_count gauge
system_cpu_count 8.0
# HELP jvm_memory_committed_bytes The amount of memory in bytes that is committed for the Java virtual machine to use
# TYPE jvm_memory_committed_bytes gauge
jvm_memory_committed_bytes{area="nonheap",id="CodeHeap 'profiled nmethods'",} 1.5990784E7
jvm_memory_committed_bytes{area="heap",id="G1 Survivor Space",} 0.0
jvm_memory_committed_bytes{area="heap",id="G1 Old Gen",} 5.6623104E7
jvm_memory_committed_bytes{area="nonheap",id="Metaspace",} 8.8436736E7
jvm_memory_committed_bytes{area="nonheap",id="CodeHeap 'non-nmethods'",} 2555904.0
jvm_memory_committed_bytes{area="heap",id="G1 Eden Space",} 5.8720256E7
jvm_memory_committed_bytes{area="nonheap",id="Compressed Class Space",} 1.2054528E7
jvm_memory_committed_bytes{area="nonheap",id="CodeHeap 'non-profiled nmethods'",} 5439488.0
# HELP jvm_gc_max_data_size_bytes Max size of long-lived heap memory pool
# TYPE jvm_gc_max_data_size_bytes gauge
jvm_gc_max_data_size_bytes 5.36870912E8
# HELP jvm_classes_loaded_classes The number of classes that are currently loaded in the Java virtual machine
# TYPE jvm_classes_loaded_classes gauge
jvm_classes_loaded_classes 15819.0
# HELP hikaricp_connections_max Max connections
# TYPE hikaricp_connections_max gauge
hikaricp_connections_max{pool="HikariPool-1",} 10.0
# HELP tomcat_sessions_expired_sessions_total  
# TYPE tomcat_sessions_expired_sessions_total counter
tomcat_sessions_expired_sessions_total 0.0
# HELP jvm_buffer_memory_used_bytes An estimate of the memory that the Java virtual machine is using for this buffer pool
# TYPE jvm_buffer_memory_used_bytes gauge
jvm_buffer_memory_used_bytes{id="mapped",} 0.0
jvm_buffer_memory_used_bytes{id="direct",} 169210.0
# HELP jvm_buffer_count_buffers An estimate of the number of buffers in the pool
# TYPE jvm_buffer_count_buffers gauge
jvm_buffer_count_buffers{id="mapped",} 0.0
jvm_buffer_count_buffers{id="direct",} 10.0
# HELP jvm_threads_peak_threads The peak live thread count since the Java virtual machine started or peak was reset
# TYPE jvm_threads_peak_threads gauge
jvm_threads_peak_threads 30.0
# HELP process_uptime_seconds The uptime of the Java virtual machine
# TYPE process_uptime_seconds gauge
process_uptime_seconds 380.901
# HELP jvm_classes_unloaded_classes_total The total number of classes unloaded since the Java virtual machine has started execution
# TYPE jvm_classes_unloaded_classes_total counter
jvm_classes_unloaded_classes_total 1.0
# HELP hikaricp_connections_timeout_total Connection timeout total count
# TYPE hikaricp_connections_timeout_total counter
hikaricp_connections_timeout_total{pool="HikariPool-1",} 0.0
# HELP jdbc_connections_idle Number of established but idle connections.
# TYPE jdbc_connections_idle gauge
jdbc_connections_idle{name="dataSource",} 10.0
# HELP jdbc_connections_min Minimum number of idle connections in the pool.
# TYPE jdbc_connections_min gauge
jdbc_connections_min{name="dataSource",} 10.0
# HELP logback_events_total Number of error level events that made it to the logs
# TYPE logback_events_total counter
logback_events_total{level="warn",} 1.0
logback_events_total{level="debug",} 1.0
logback_events_total{level="error",} 0.0
logback_events_total{level="trace",} 0.0
logback_events_total{level="info",} 3.0
# HELP jvm_memory_used_bytes The amount of used memory
# TYPE jvm_memory_used_bytes gauge
jvm_memory_used_bytes{area="nonheap",id="CodeHeap 'profiled nmethods'",} 1.3789696E7
jvm_memory_used_bytes{area="heap",id="G1 Survivor Space",} 0.0
jvm_memory_used_bytes{area="heap",id="G1 Old Gen",} 3.4326728E7
jvm_memory_used_bytes{area="nonheap",id="Metaspace",} 8.612576E7
jvm_memory_used_bytes{area="nonheap",id="CodeHeap 'non-nmethods'",} 1295616.0
jvm_memory_used_bytes{area="heap",id="G1 Eden Space",} 1.3631488E7
jvm_memory_used_bytes{area="nonheap",id="Compressed Class Space",} 1.1102224E7
jvm_memory_used_bytes{area="nonheap",id="CodeHeap 'non-profiled nmethods'",} 4186624.0
# HELP tomcat_sessions_created_sessions_total  
# TYPE tomcat_sessions_created_sessions_total counter
tomcat_sessions_created_sessions_total 0.0
# HELP jvm_gc_pause_seconds Time spent in GC pause
# TYPE jvm_gc_pause_seconds summary
jvm_gc_pause_seconds_count{action="end of major GC",cause="System.gc()",} 7.0
jvm_gc_pause_seconds_sum{action="end of major GC",cause="System.gc()",} 0.428
jvm_gc_pause_seconds_count{action="end of minor GC",cause="Metadata GC Threshold",} 1.0
jvm_gc_pause_seconds_sum{action="end of minor GC",cause="Metadata GC Threshold",} 0.007
jvm_gc_pause_seconds_count{action="end of minor GC",cause="G1 Evacuation Pause",} 6.0
jvm_gc_pause_seconds_sum{action="end of minor GC",cause="G1 Evacuation Pause",} 0.099
# HELP jvm_gc_pause_seconds_max Time spent in GC pause
# TYPE jvm_gc_pause_seconds_max gauge
jvm_gc_pause_seconds_max{action="end of major GC",cause="System.gc()",} 0.059
jvm_gc_pause_seconds_max{action="end of minor GC",cause="Metadata GC Threshold",} 0.0
jvm_gc_pause_seconds_max{action="end of minor GC",cause="G1 Evacuation Pause",} 0.0
# HELP jvm_gc_live_data_size_bytes Size of long-lived heap memory pool after reclamation
# TYPE jvm_gc_live_data_size_bytes gauge
jvm_gc_live_data_size_bytes 3.4326728E7
# HELP tomcat_sessions_active_max_sessions  
# TYPE tomcat_sessions_active_max_sessions gauge
tomcat_sessions_active_max_sessions 0.0
# HELP hikaricp_connections_pending Pending threads
# TYPE hikaricp_connections_pending gauge
hikaricp_connections_pending{pool="HikariPool-1",} 0.0
# HELP hikaricp_connections_acquire_seconds Connection acquire time
# TYPE hikaricp_connections_acquire_seconds summary
hikaricp_connections_acquire_seconds_count{pool="HikariPool-1",} 2.0
hikaricp_connections_acquire_seconds_sum{pool="HikariPool-1",} 0.0028594
# HELP hikaricp_connections_acquire_seconds_max Connection acquire time
# TYPE hikaricp_connections_acquire_seconds_max gauge
hikaricp_connections_acquire_seconds_max{pool="HikariPool-1",} 0.0
# HELP tomcat_sessions_alive_max_seconds  
# TYPE tomcat_sessions_alive_max_seconds gauge
tomcat_sessions_alive_max_seconds 0.0
# HELP hikaricp_connections_usage_seconds Connection usage time
# TYPE hikaricp_connections_usage_seconds summary
hikaricp_connections_usage_seconds_count{pool="HikariPool-1",} 2.0
hikaricp_connections_usage_seconds_sum{pool="HikariPool-1",} 0.002
# HELP hikaricp_connections_usage_seconds_max Connection usage time
# TYPE hikaricp_connections_usage_seconds_max gauge
hikaricp_connections_usage_seconds_max{pool="HikariPool-1",} 0.0
# HELP jdbc_connections_max Maximum number of active connections that can be allocated at the same time.
# TYPE jdbc_connections_max gauge
jdbc_connections_max{name="dataSource",} 10.0
# HELP hikaricp_connections_min Min connections
# TYPE hikaricp_connections_min gauge
hikaricp_connections_min{pool="HikariPool-1",} 10.0
# HELP http_server_requests_seconds  
# TYPE http_server_requests_seconds summary
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics",} 0.0090145
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 0.2302241
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/**",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/**",} 0.0720974
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator",} 2.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator",} 0.0186887
# HELP http_server_requests_seconds_max  
# TYPE http_server_requests_seconds_max gauge
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics",} 0.0090145
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/**",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator",} 0.0142562
# HELP jvm_threads_live_threads The current number of live threads including both daemon and non-daemon threads
# TYPE jvm_threads_live_threads gauge
jvm_threads_live_threads 30.0
# HELP tomcat_sessions_rejected_sessions_total  
# TYPE tomcat_sessions_rejected_sessions_total counter
tomcat_sessions_rejected_sessions_total 0.0
# HELP process_cpu_usage The "recent cpu usage" for the Java Virtual Machine process
# TYPE process_cpu_usage gauge
process_cpu_usage 0.2067119764619553
```

Cool. But now we want to implement an application specific monitoring endpoint. Let's assume we want to monitor the count of books created. All we have to do, is to implement a service that will provide the count of books.

```
@Slf4j
@Service
public class MetricsProviderService
{
    @Autowired
    private BookRepo bookRepo;

    private TimeoutLatch timeoutLatch = new TimeoutLatch();

    public double getBookCount()
    {
        try
        {
            long bookCount = AsyncExecutor.invoke(this::getTotalBookCount, null);
            return (double) bookCount;
        }
        catch (TimeoutException ex)
        {
            this.timeoutLatch.setClosed();
            log.error(String.format("getTotalBookCount() did not complete within %d ms! The database is not reachable or slow!",
                SysConfig.getMetricsProperties().getTimeoutMillis()));
        }

        return 0.0;
    }


    private long getTotalBookCount()
    {
        if (this.timeoutLatch.isClosed())
        {
            return 0;
        }

        return bookRepo.count();
    }
}
```

Now, we have to include the new metric in the Micrometer metrics service.

```
@Service
@Getter
@Slf4j
public class MicrometerMetricsService
{

    private final List<HealthIndicator> healthIndicators;
    private final List<HealthIndicator> healthIndicatorsDatabase;

    public MicrometerMetricsService(MeterRegistry registry, MetricsProviderService myMetricProvider,
        HealthContributorRegistry healthContributorRegistry, HealthIndicatorDatabase healthIndicatorDatabase)
    {
        final String METRIC_BOOK_COUNT = Constants.SUBSYSTEM_NAME.toLowerCase() + ".bookcount";
        final String METRIC_HEALTH = Constants.SUBSYSTEM_NAME.toLowerCase() + ".health";
        final String METRIC_HEALTH_DB = Constants.SUBSYSTEM_NAME.toLowerCase() + ".health.db";

        // Book count
        Gauge.builder(METRIC_BOOK_COUNT, myMetricProvider, MetricsProviderService::getBookCount).description(
            "The current count of books").baseUnit("pcs").register(registry);

        // Health indicators
        this.healthIndicators = healthContributorRegistry.stream() //
            .map(c -> this.getIndicatorFromContributor(c)) //
            .collect(Collectors.toList());
        Gauge.builder(METRIC_HEALTH, healthIndicators, MicrometerMetricsService::healthToCode) //
            .description("The current value of the composite health endpoint").register(registry);

        // DB-health indicator
        this.healthIndicatorsDatabase = List.of(healthIndicatorDatabase);
        Gauge.builder(METRIC_HEALTH_DB, healthIndicatorsDatabase, MicrometerMetricsService::healthToCode).description(
            "The current value of the db health endpoint").register(registry);
    }

    private HealthIndicator getIndicatorFromContributor(NamedContributor<HealthContributor> namedContributor)
    {
        log.debug(String.format("Using health contributor: '%s'", namedContributor.getName()));

        HealthContributor contributor = namedContributor.getContributor();
        if (contributor instanceof HealthIndicator)
        {
            return (HealthIndicator) contributor;
        }

        if (contributor instanceof CompositeHealthContributor)
        {
            CompositeHealthContributor compositeHealthContributor = (CompositeHealthContributor) contributor;
            for (NamedContributor<HealthContributor> elementOfComposite : compositeHealthContributor)
            {
                return getIndicatorFromContributor(elementOfComposite);
            }
        }

        throw new UnexpectedConditionException();
    }

    private static int healthToCode(List<HealthIndicator> indicators)
    {
        for (HealthIndicator indicator : indicators)
        {
            Status status = indicator.health().getStatus();
            if (Status.DOWN.equals(status))
            {
                return 0;
            }
        }

        return 1;
    }
}
```

That's it! Let's check the list of metrics again.

```
{
   "names":[
      "hikaricp.connections",
      "hikaricp.connections.acquire",
      "hikaricp.connections.active",
      "hikaricp.connections.creation",
      "hikaricp.connections.idle",
      "hikaricp.connections.max",
      "hikaricp.connections.min",
      "hikaricp.connections.pending",
      "hikaricp.connections.timeout",
      "hikaricp.connections.usage",
      "http.server.requests",
      "jdbc.connections.active",
      "jdbc.connections.idle",
      "jdbc.connections.max",
      "jdbc.connections.min",
      "jvm.buffer.count",
      "jvm.buffer.memory.used",
      "jvm.buffer.total.capacity",
      "jvm.classes.loaded",
      "jvm.classes.unloaded",
      "jvm.gc.live.data.size",
      "jvm.gc.max.data.size",
      "jvm.gc.memory.allocated",
      "jvm.gc.memory.promoted",
      "jvm.gc.pause",
      "jvm.memory.committed",
      "jvm.memory.max",
      "jvm.memory.used",
      "jvm.threads.daemon",
      "jvm.threads.live",
      "jvm.threads.peak",
      "jvm.threads.states",
      "logback.events",
      "process.cpu.usage",
      "process.start.time",
      "process.uptime",
      "system.cpu.count",
      "system.cpu.usage",
      "tomcat.sessions.active.current",
      "tomcat.sessions.active.max",
      "tomcat.sessions.alive.max",
      "tomcat.sessions.created",
      "tomcat.sessions.expired",
      "tomcat.sessions.rejected",
      "wsstepbystep.bookcount",
      "wsstepbystep.health"
   ]
}
```

https://www.localhost.hu:8080/actuator/metrics/wsstepbystep.bookcount
```
{
   "name":"wsstepbystep.bookcount",
   "description":"The current count of books",
   "baseUnit":"pcs",
   "measurements":[
      {
         "statistic":"VALUE",
         "value":5
      }
   ],
   "availableTags":[
      
   ]
}
```

Also we have included the health status as well, so that it can be monitored with Prometheus.

## step23: Installing Prometheus and Grafana

Building the images:

```
c:\np\github\wsstepbystep>gradlew clean doI -x test
```

Starting the docker swarm:

```
c:\np\github\wsstepbystep\docker-compose\dev>coU --all
```

Grafana: localhost:3000, admin/admin
Prometheus: localhost:9090

The port of the webservice application had to changed to 8400, because 8080 was already occuped by cAdvisor.

Enjoy!

