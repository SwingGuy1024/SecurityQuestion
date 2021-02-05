# Problem setting error status using Spring Security

In certain cases when my authentication fails, I want it to return a specific HttpStatus error code, but Spring Security always return a 500 instead, with an empty message. I've tried two approaches, and they both fail. I should point out that this is a RESTful interface, not a browser application or any other application with a user interface, so there's no error page. (In my actual application, I want to return a custom status code for an expired JTW token, so the calling service knows to log in again. I understand that it's normally not wise to return much information on a security failure, but the AuthenticationEntryPoint is supposed to let me customize this behavior.)

## Failure mode one: Using AuthenticationEntryPoint

When I throw an AuthenticationException subclass, I expect it to get handled by my AuthenticationEntryPoint implementation, but it never does. Instead, it returns an HttpStatus of 500.

## Failure mode two: Using @ResponseStatus annotation

When I throw an exception annotated with `@ResponseStatus(HttpStatus.EXPECTATION_FAILED)`, I should see an error code of 417. But instead, I see an HttpStatus of 500. It also never logs entry into the `commence()` method of the `
AuthenticationEntryPoint` implementation.

## To Reproduce:

Build and run the server. It has five endpoints, three of which are supposed to fail, and two of which don't fail properly. You can see the five endpoints in the `RestController` class.

None of these five endpoints require any additional input or authentication token. They all use the GET verb, so they should be easy to send
in a tool like `curl` or Postman.

Here are the five end points:

### `/admin/failureOne/`

This one gets caught in the `RequestFilter` class, which detects it and throws a `CredentialsExpiredException`, which extends `
AuthenticationException`. It should get caught by the 'CustomAuthenticationEntryPoint.commence()' method, but it never does. I log a message in the constructor to prove the class does get instantiated. Also, the message in the exception does not show up in the Response Entity

### `/admin/failureTwo/`

This one gets caught in the `RequestFilter` class, which detects it and throws an `ExpectationFailed417Exception`, which is annotated with `@ResponseStatus(HttpStatus.EXPECTATION_FAILED)`. It should return a response with error code 417, but I get 500 instead. Also, the message in the exception does not show up in the Response Entity

### `/view/methodThree/`

This one proves the server is working. It returns the String `"MethodThree"`. This one requires no authentication. **Note:** *This is the only endpoint that doesn't start with `/admin`*

### `/admin/methodFour/`

This one partly proves that security is working. It requires authentication with a role of CUSTOMER. It successfully returns the String `"MethodFour"`.

### `/admin/methodFive/`

This one proves that security is working. It requires authentication with a role of CUSTOMER, but the role gets set to UNKNOWN, so it fails. This is the one failure mode that works correctly, returning a 403. But it does not hit the `CustomAuthenticationEntryPoint` either.

