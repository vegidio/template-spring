# Template Spring

A modern Spring Boot 4.0 template project showcasing best practices and latest features.

## 🚀 Modern Features (Spring Boot 4)

This template demonstrates the latest Spring Boot 4 features and best practices:

### **RFC 9457 Problem Details API**

- Standardized error responses using `ProblemDetail`
- Replaces custom error handling with industry-standard format
- Better client error handling and debugging

### **Virtual Threads (Project Loom)**

- Enabled for improved concurrency and scalability
- Better resource utilization with lightweight threads
- Requires Java 21+

### **Docker Compose Support**

- Automatic PostgreSQL container management in development
- Zero-configuration local development environment
- Run `./gradlew bootRun` and it starts the database automatically

### **Observability & Monitoring**

- Spring Boot Actuator with health checks, metrics, and Prometheus support
- Custom health indicators for better monitoring
- Kubernetes-ready liveness and readiness probes
- Access metrics at: http://localhost:3000/actuator

### **Modern HTTP Client**

- RestClient API (Spring 6.1+) for synchronous HTTP calls
- Works seamlessly with Virtual Threads
- Fluent API with better type safety than RestTemplate
- JDK HttpClient as the underlying engine

### **Structured JSON Logging**

- Production-ready JSON log format
- Better integration with log aggregation tools (ELK, Splunk, etc.)
- Includes timestamp, level, thread, logger, message, and exceptions

### **Enhanced Security**

- Spring Security 6+ modern configuration
- JWT-based authentication with EC256 signatures
- Argon2 password hashing
- Method-level security with `@PreAuthorize`
- Public access for health endpoints and API documentation

### **GraphQL Support**

- Spring for GraphQL integration
- Schema-first approach
- Auto-generated schema from code

### **Modern Kotlin**

- Kotlin 2.3.10 with K2 compiler
- Idiomatic Kotlin DSL for Spring Security and configuration
- Lambda invoke dynamic for better performance

## 🏃 Running

### Local Development (with Docker Compose)

```bash
./gradlew bootRun
```

The application will automatically:

1. Start a PostgreSQL container
2. Connect to the database
3. Run migrations
4. Start the server on http://localhost:3000

### Docker Production Build

```bash
docker run --rm -t \
    -e PG_HOST=<host> \
    -e PG_PORT=<port> \
    -e PG_USERNAME=<username> \
    -e PG_PASSWORD=<password> \
    -p 3004:3000 \
    vegidio/tpl-spring
```

## 📊 Endpoints

- **API**: http://localhost:3000/api/v1
- **GraphQL**: http://localhost:3000/graphql
- **API Docs**: http://localhost:3000/docs
- **Actuator**: http://localhost:3000/actuator
- **Health**: http://localhost:3000/actuator/health
- **Metrics**: http://localhost:3000/actuator/metrics

## 🔐 Credentials

- E-mail: `vegidio@gmail.com` / Password: `password1`
- E-mail: `sna3009@gmail.com` / Password: `password2`

## 🛠️ Tech Stack

- **Spring Boot 4.0.0** - Latest Spring framework
- **Kotlin 2.3.10** - Modern JVM language with K2 compiler
- **Java 21** - LTS version with Virtual Threads support
- **PostgreSQL 17** - Relational database
- **Spring Security 6** - Authentication & authorization
- **Spring Data JPA** - Database access
- **Spring for GraphQL** - GraphQL API
- **SpringDoc OpenAPI** - API documentation
- **Actuator** - Monitoring and observability
- **Docker** - Containerization
- **Gradle with Kotlin DSL** - Build tool

## 👨🏾‍💻 Author

Vinicius Egidio ([vinicius.io](http://vinicius.io))
