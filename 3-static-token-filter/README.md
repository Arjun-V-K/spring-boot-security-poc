# Static Token Authentication

Create a filter which gets the token, checks it and sets SecurityContext
Disable other authentication providers like BasicAuthenticationFilter

TODO: I think there is a way to create an AuthenticationProvider which does the functionality, find out

Currently there is only endpoint level authorization. We can do method level as well.
