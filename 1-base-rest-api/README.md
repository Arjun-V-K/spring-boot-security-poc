#### Application Properties required for h2 database

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

```

#### Access h2 console
Go to http://localhost:8080/h2-console

### My Doubts

#### @Entity vs @Table


#### Which dependency to get Swagger documentation?
// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
Go to http://localhost:8080/swagger-ui/index.html