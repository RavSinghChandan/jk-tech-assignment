# Document Management System

A full-stack document management system with Spring Boot backend and Angular frontend.

## Features

### Backend (Spring Boot)
- JWT-based authentication
- Document upload and management
- Support for PDF, DOCX, and TXT files
- Full-text search functionality
- Role-based access control
- Swagger API documentation

### Frontend (Angular)
- Modern, responsive UI
- User authentication and authorization
- Document upload and management
- Document search functionality
- Role-based access control

## Prerequisites

- Java 17 or higher
- Node.js 16.x or higher
- Angular CLI 15.x or higher
- MySQL 8.0 or higher
- Docker and Docker Compose (optional)

## Local Development Setup

### Backend Setup

1. Create MySQL database:
```sql
CREATE DATABASE document_management;
```

2. Update database configuration in `src/main/resources/application.properties`

3. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

### Frontend Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Configure environment:
Create `src/environments/environment.ts` with:
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1',
  jwtTokenKey: 'auth_token'
};
```

3. Run development server:
```bash
ng serve
```

## Docker Deployment

1. Build and start all services:
```bash
docker-compose up --build
```

2. Access the application:
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## Project Structure

```
.
├── frontend/                 # Angular frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── services/
│   │   │   │   └── models/
│   │   │   └── environments/
│   │   └── Dockerfile
│   └── nginx.conf
├── src/                      # Spring Boot backend
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── Dockerfile
└── docker-compose.yml
```

## API Documentation

Once the backend is running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Testing

### Backend Tests
```bash
mvn test
```

### Frontend Tests
```bash
cd frontend
ng test
```

## Security

The application uses JWT for authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

## License

This project is licensed under the MIT License. 