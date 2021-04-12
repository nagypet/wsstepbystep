# wsstepbystep

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
