# Spring Security

## 1. Add Spring Security dependency

The auto configuration has the following filter. Enable DEBUG logging to get it.
Will secure any request with filters: DisableEncodeUrlFilter, WebAsyncManagerIntegrationFilter, SecurityContextHolderFilter, HeaderWriterFilter, CsrfFilter, LogoutFilter, UsernamePasswordAuthenticationFilter, DefaultResourcesFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter, BasicAuthenticationFilter, RequestCacheAwareFilter, SecurityContextHolderAwareRequestFilter, AnonymousAuthenticationFilter, ExceptionTranslationFilter, AuthorizationFilter
Http Basic authenatation is enabled by default (username and password)
Password is autogenerated in the console
Using generated security password: 5fa1b065-98e3-4afc-ba10-fdd20ecd24f8
Global AuthenticationManager configured with UserDetailsService bean with name inMemoryUserDetailsManager

## 2. Adding our Users

To override the default behaviour, we need to provide a UserDetailsService and PasswordEncoder bean which are used by AuthenticationProvider
This is done in the SecurityConfig file

Given below is the minimum code to add our own users.
```java
@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder().username("user").password("password").authorities("ROLE_user").build();
        UserDetails user2 = User.builder().username("admin").password("password").authorities("ROLE_user").build();
        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

## 3. Getting the users from database

### A. User entity to store in database
### B. SecurityUser which implements UserDetails
### C. UserServiceImpl which implements UserDetailsService

New configuration would look like this
```java
@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```
Keep in mind if the UserDetailsImpl has @Component annotation already, then it is not need to be exposed as a bean in the configuration.

## 4. Lets forget about the CSRF token and Session details and make a Stateless REST API

To change out filter chain, we need to provide our own SecurityFilterChain object.
New configuration would look like this:
```java
@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());
        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());
        return http.build();
    }
}
```
Now all our endpoints are authenticated by basic authentication.
The actual logic of checking if username and password matching and setting of SecurityContext is done automatically by the default AuthenticationProvider


## 5. Role based access

Two ways to do role based access
### Endpoint-level authorization
```java
@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());
        http.authorizeHttpRequests(c -> c
                .requestMatchers("/api/product/**").hasRole("user")
                .anyRequest().hasRole("admin")
        );
        return http.build();
    }
}
```

### Method level

@EnableMethodSecurity in the configuration
and
@PreAuthorize("hasRole('admin')")
