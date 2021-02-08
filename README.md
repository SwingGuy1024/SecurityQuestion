# How do I set the error code using Spring Security

*Required Java version: 12+*

In a certain cases when my authentication fails, I want it to return a specific HttpStatus error code, but Spring Security always return a 403 instead, with an empty message, or with no message at all. I've tried several approaches, and they all fail. I should point out that this is a RESTful interface, not a browser application or any other application with a user interface, so there's no error page. (In my actual application, I want to return a custom status code for an expired JTW token, to tell the calling service to log back in. I understand that it's normally not wise to return much information on an authentication failure, but I only get the JwtExpiredToken exception if it's signed correctly, so in this special case I'm not worried about hackers.)

Here's what I've tried:

### Failure Mode One: Using AuthenticationEntryPoint

I tried installing an AuthenticationEntryPoint, by calling this in the `WebSecurityConfigurer`:

    httpSecurity
      .exceptionHandling()
      .authenticationEntryPoint(authenticationEntryPoint)

My request filter throws a CredentialsExpiredException, which I expected to see in the `AuthenticationEntryPoint.commence()` method, but I see a different exception. In fact, I see the same kind of exception, with no message, for all authentication failures. So I can't distinguish between my expired token and any other failure reason.

### Failure Mode Two: Using `@ResponseStatus` annotation

I tried throwing an exception annotated with `@ResponseStatus(HttpStatus.EXPECTATION_FAILED)`, but that returned a status of
   403-FORBIDDEN, with no response body for a message. I wanted to see an error code of 417.
   
### Failure Mode Three: Adding a `@ControllerAdvice` class

I added a @ControllerAdvice class to process the same exception in the previous case, but the server never called its method 
   with the `@ExceptionHandler` annotation.

### Failure Mode Four: Setting a `UsernamePasswordAuthenticationToken`
This one seems to be the closest to the intended usage of the Spring Security framework, but the behavior isn't very helpful. I create a `UsernamePasswordAuthenticationToken` with an empty credentials list. It returns a 403-Forbidden. I can't find a way to tailor either the message or the status code.

## To Reproduce:

Build and run the server. It has six endpoints, Four of which are supposed to fail, and three of which don't fail properly. You can see the code for the six endpoints in the `RestController` class, and you can see the code that makes them fail in the `RequestFilter` class.

None of these six endpoints require any additional input or authentication token. They all use the GET verb, so they should be easy to send in a tool like `curl` or Postman.

Here are the six end points:

### Failure Mode One: `/admin/failureModeOne/`
This will return a 403-Forbidden, and a log statement will show the class of the exception that the AuthenticationEntryPoint received is InsufficientAuthenticationException instead of the CredentialsExpiredException that was thrown by the `RequestFilter` class.

### Failure Modes Two and Three: `/admin/failureModeThree/`

The `RequestFilter` class responds to this one by throwing an `ExpectationFailed417Exception`, which is annotated with `@ResponseStatus(HttpStatus.EXPECTATION_FAILED)`. I want it to return a response with error code 417, but I get 403-Forbidden instead, with no response body. This endpoint illustrates both failure modes two and three. Removing or disabling the `TimoutControllerAdvice` class doesn't change anything.

### Failure Mode Four: `/admin/failureModeFour/`

The `RequestFilter` class catches this and creates a `UsernamePasswordAuthenticationToken` with an emtpy Authorities list. I can't find way
to change the status code or add a message. The returned response looks like this:

    {
      "timestamp": "2021-02-07T08:05:56.316+00:00",
      "status": 403,
      "error": "Forbidden",
      "message": "",
      "path": "/admin/failureModeFour/"
    }

### Method One `/view/methodOne/`

This one proves the server is working. It returns the String `"MethodOne"`. This one requires no authentication. **Note:** *This is the only endpoint that doesn't start with `/admin`*

### Method Two `/admin/methodTwo/`

This one partly proves that security is working. It requires authentication with a role of ADMIN. It successfully returns the String `"MethodTwo"`.

### Method Three: `/admin/methodThree/`

This one proves that security is working. It requires authentication with a role of ADMIN, but the role gets set to UNKNOWN, so it fails. This is the one failure mode that works correctly, returning a 403, with the response body below. But the `authenticationEntryPoint` doesn't get called in this case, because the application doesn't throw an exception.

    {
      "timestamp": "2021-02-07T08:52:47.270+00:00",
      "status": 403,
      "error": "Forbidden",
      "message": "",
      "path": "/admin/methodThree/"
    }
