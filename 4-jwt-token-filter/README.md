# JWT Filter Implementation

## 1. Generating and Verifying the JWTs

There are multiple libraries which provide us with this functionality. They can be found [here](https://jwt.io/libraries?filter=java).

We will be using `io.jsonwebtoken.jjwt` version `0.12.6` which is currently the latest one.

The code for generating and verifying token is in the `JwtUtil` class.

---

### Tips

- Do code in the order of the tests.
- See `DataLoader` to see how repo is used.
- Be familiar with how to do DB mappings (One to one), (One to Many), (Many to Many) and which table to update for a specific API.
- For example, to update the quantity of the product in your cart, update the mapping table.
- For updates, find the existing one and update that object. **Do not** put your object in since it won't have the auto generated id.
- Learn to use the debug mode.
- For Security Config: need `UserDetailsService`, `PasswordEncoder`, `SecurityFilterChain` (Disable csrf, Stateless session, add filter, add basic auth, `authorizeHttpEndpoints` with roles) and `AuthenticationManager` (get it by `AuthenticationConfiguration config.getAuthenticationManager()`).
- For the filter, get the token from the header, verify the token, get username from the token, then the user for the `userDetailsService`, then set `SecurityContext`.
- For User from database, need `UserEntity` stored in database with username, password and role. This will be taken by implementation of `UserDetails` (or can use the already provided one (`User`)).
- In login API, use the `authenticationManager.authenticate(new UsernameAndPasswordToken)` and check for authentication.
- **IMPORTANT:** Make `/error` endpoint public.