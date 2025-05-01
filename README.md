# Document Management and Q&A Application

A comprehensive document management system with Q&A capabilities, built with Spring Boot backend and Angular frontend.

## Features

- User Authentication (Register, Login, Logout)
- Role-based Access Control (Admin, Editor, Viewer)
- Document Management (Upload, Search, Delete)
- Basic Q&A Functionality
- Full-text Search
- Asynchronous Processing
- Docker Support
- Comprehensive Testing Suite

## Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- Docker and Docker Compose
- PostgreSQL 15 or higher
- Maven
- Redis 7 or higher

## Project Structure

```
document-management/
├── backend/                 # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/jktech/document_management/
│   │   │   │       ├── config/         # Configuration classes
│   │   │   │       ├── controller/     # REST controllers
│   │   │   │       ├── dto/           # Data Transfer Objects
│   │   │   │       ├── entity/        # JPA entities
│   │   │   │       ├── repository/    # JPA repositories
│   │   │   │       ├── security/      # Security configuration
│   │   │   │       └── service/       # Business logic
│   │   │   └── resources/
│   │   │       ├── application.yml    # Application configuration
│   │   │       ├── application-dev.yml # Development configuration
│   │   │       ├── application-prod.yml # Production configuration
│   │   │       └── schema.sql         # Database schema
│   │   └── test/           # Test classes
│   │       ├── java/
│   │       │   └── com/jktech/document_management/
│   │       │       ├── integration/   # Integration tests
│   │       │       ├── performance/   # Performance tests
│   │       │       └── util/          # Test utilities
│   │       └── resources/
│   │           ├── application-test.yml    # Test configuration
│   │           ├── application-integration.yml # Integration test config
│   │           ├── application-performance.yml # Performance test config
│   │           ├── data.sql            # Test data
│   │           ├── integration-data.sql # Integration test data
│   │           └── performance-data.sql # Performance test data
│   ├── Dockerfile          # Backend Docker configuration
│   └── pom.xml             # Maven dependencies
│
├── frontend/               # Angular frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/ # Angular components
│   │   │   ├── services/   # Angular services
│   │   │   ├── guards/     # Route guards
│   │   │   └── interceptors/ # HTTP interceptors
│   │   └── environments/   # Environment configurations
│   ├── Dockerfile          # Frontend Docker configuration
│   └── package.json        # Node.js dependencies
│
└── docker-compose.yml      # Docker Compose configuration
```

## Setup and Running

### 1. Clone the Repository
```bash
git clone <repository-url>
cd document-management
```

### 2. Backend Setup
```bash
cd backend
mvn clean install
```

### 3. Frontend Setup
```bash
cd frontend
npm install
```

### 4. Database Setup
```bash
# Using Docker Compose
docker-compose up -d postgres redis
```

### 5. Running the Application

#### Development Mode
```bash
# Start backend
cd backend
mvn spring-boot:run -Dspring.profiles.active=dev

# Start frontend
cd frontend
ng serve
```

#### Production Mode (Docker)
```bash
# Build and start all services
docker-compose up --build
```

The application will be available at:
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## Testing

### Backend Tests

#### Unit Tests
```bash
cd backend
mvn test -Dspring.profiles.active=unit
```

#### Integration Tests
```bash
cd backend
mvn test -Dspring.profiles.active=integration
```

#### Performance Tests
```bash
cd backend
mvn test -Dspring.profiles.active=performance
```

### Frontend Tests
```bash
cd frontend
ng test
```

## Test Configurations

### Unit Tests
- Uses H2 in-memory database
- Disables SQL logging
- Configures mock data generation
- Sets up JWT security with test secret

### Integration Tests
- Uses PostgreSQL database
- Enables SQL logging and formatting
- Includes test data for users and documents
- Configures Redis for caching
- Sets up JWT security

### Performance Tests
- Uses PostgreSQL database
- Disables SQL logging
- Includes large dataset for performance testing
- Configures Redis for caching
- Sets up JWT security

## API Documentation

Swagger UI is available at http://localhost:8080/swagger-ui.html when the backend is running.

## Security

- JWT-based authentication
- Role-based access control
- Password encryption using BCrypt
- CORS configuration
- CSRF protection

## Performance Considerations

- Asynchronous document processing
- Full-text search using PostgreSQL
- Caching strategies using Redis
- Batch processing for large uploads
- Performance monitoring and metrics

## Monitoring and Logging

- Spring Boot Actuator for monitoring
- Logback for logging
- Prometheus metrics
- Custom logging configuration

## CI/CD

The project includes GitHub Actions workflows for:
- Automated testing
- Docker image building
- Deployment to staging/production

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Documents Table
```sql
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    file_type VARCHAR(50),
    file_size BIGINT,
    uploaded_by_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uploaded_by_id) REFERENCES users(id)
);

-- Indexes
CREATE INDEX idx_documents_title ON documents(title);
CREATE INDEX idx_documents_uploaded_by ON documents(uploaded_by_id);
CREATE INDEX idx_documents_created_at ON documents(created_at);
CREATE FULLTEXT INDEX idx_documents_content ON documents(content);

-- Trigger for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_documents_updated_at
    BEFORE UPDATE ON documents
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
```

## API Documentation

### Authentication

#### Register User
```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123",
    "role": "VIEWER"
}
```

Response:
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "email": "user@example.com",
    "role": "VIEWER"
}
```

#### Login
```http
POST /api/v1/auth/authenticate
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}
```

Response:
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "email": "user@example.com",
    "role": "VIEWER"
}
```

### Document Management

#### Upload Document
```http
POST /api/v1/documents
Content-Type: multipart/form-data
Authorization: Bearer <token>

{
    "title": "Sample Document",
    "file": <file>,
    "fileType": "pdf"
}
```

Response:
```json
{
    "id": 1,
    "title": "Sample Document",
    "fileType": "pdf",
    "fileSize": 1024,
    "uploadedBy": "user@example.com",
    "createdAt": "2024-03-20T10:00:00Z"
}
```

#### Search Documents
```http
GET /api/v1/documents/search?query=keyword&page=0&size=10
Authorization: Bearer <token>
```

Response:
```json
{
    "content": [
        {
            "id": 1,
            "title": "Sample Document",
            "fileType": "pdf",
            "fileSize": 1024,
            "uploadedBy": "user@example.com",
            "createdAt": "2024-03-20T10:00:00Z"
        }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
}
```

## Build and Deployment

### Local Development

1. **Prerequisites Setup**
```bash
# Install Java 17
brew install openjdk@17

# Install Node.js 18
brew install node@18

# Install Docker and Docker Compose
brew install docker docker-compose

# Install PostgreSQL 15
brew install postgresql@15

# Install Redis 7
brew install redis
```

2. **Environment Setup**
```bash
# Clone repository
git clone <repository-url>
cd document-management

# Set up environment variables
cp backend/src/main/resources/application-dev.yml.example backend/src/main/resources/application-dev.yml
cp frontend/src/environments/environment.ts.example frontend/src/environments/environment.ts
```

3. **Database Setup**
```bash
# Start PostgreSQL and Redis
docker-compose up -d postgres redis

# Initialize database
psql -h localhost -U postgres -d document_management -f backend/src/main/resources/schema.sql
```

4. **Build and Run**
```bash
# Backend
cd backend
mvn clean install
mvn spring-boot:run -Dspring.profiles.active=dev

# Frontend
cd frontend
npm install
ng serve
```

### Production Deployment

1. **Docker Deployment**
```bash
# Build and start all services
docker-compose up --build -d

# View logs
docker-compose logs -f
```

2. **Kubernetes Deployment**
```bash
# Apply Kubernetes configurations
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/redis.yaml
kubectl apply -f k8s/backend.yaml
kubectl apply -f k8s/frontend.yaml
kubectl apply -f k8s/ingress.yaml

# Verify deployment
kubectl get all -n document-management
```

3. **AWS Deployment**
```bash
# Configure AWS CLI
aws configure

# Deploy using AWS CDK
cd infrastructure
npm install
cdk deploy
```

### CI/CD Pipeline

1. **GitHub Actions Workflow**
```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build with Maven
        run: mvn clean install
      - name: Build Docker images
        run: docker-compose build
      - name: Push to Docker Hub
        run: |
          echo "${{ secrets.DOCKER_PASSWORD }}" | docker login -u "${{ secrets.DOCKER_USERNAME }}" --password-stdin
          docker-compose push
```

2. **Deployment Environments**
- Development: http://dev.example.com
- Staging: http://staging.example.com
- Production: http://app.example.com

### Monitoring and Maintenance

1. **Logging**
```bash
# View application logs
docker-compose logs -f backend

# View database logs
docker-compose logs -f postgres
```

2. **Backup**
```bash
# Database backup
pg_dump -h localhost -U postgres document_management > backup.sql

# Restore from backup
psql -h localhost -U postgres document_management < backup.sql
```

3. **Performance Monitoring**
```bash
# Access Prometheus metrics
curl http://localhost:8080/actuator/prometheus

# View Grafana dashboard
open http://localhost:3000
```

## Frontend Deployment

The frontend application is deployed on Vercel and can be accessed at:
https://document-management-frontend.vercel.app

### Deployment Steps

1. **Push to GitHub**
```bash
git add .
git commit -m "Initial commit"
git remote add origin <your-github-repo-url>
git push -u origin main
```

2. **Deploy to Vercel**
- Go to [Vercel](https://vercel.com)
- Import your GitHub repository
- Configure the following settings:
   - Framework Preset: Angular
   - Root Directory: frontend
   - Build Command: npm run build
   - Output Directory: dist/document-management
   - Install Command: npm install

3. **Environment Variables**
   Add the following environment variables in Vercel:
```
API_URL=https://your-backend-api-url
```

4. **Custom Domain (Optional)**
- Go to your project settings in Vercel
- Add your custom domain
- Follow the DNS configuration instructions

### Development Deployment
For development deployments, use:
```
https://document-management-frontend-git-dev.vercel.app
```

### Production Deployment
For production deployments, use:
```
https://document-management-frontend.vercel.app
``` 