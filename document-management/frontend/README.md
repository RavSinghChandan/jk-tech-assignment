# Document Management System - Frontend

Angular-based frontend for the Document Management System.

## Features

- User authentication and authorization
- Document upload and management
- Document search functionality
- Responsive design
- Role-based access control

## Prerequisites

- Node.js 16.x or higher
- Angular CLI 15.x or higher
- Docker (optional)

## Setup

1. Install dependencies:
```bash
npm install
```

2. Configure environment:
Create a `src/environments/environment.ts` file with:
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1',
  jwtTokenKey: 'auth_token'
};
```

3. Run the development server:
```bash
ng serve
```

The application will be available at `http://localhost:4200`

## Docker Setup

1. Build the Docker image:
```bash
docker build -t document-management-frontend .
```

2. Run the container:
```bash
docker run -p 4200:80 document-management-frontend
```

## Project Structure

```
src/
├── app/
│   ├── components/
│   │   ├── auth/
│   │   ├── document/
│   │   └── shared/
│   ├── services/
│   ├── guards/
│   ├── interceptors/
│   └── models/
├── assets/
└── environments/
```

## Key Components

- `AuthService`: Handles authentication and JWT token management
- `DocumentService`: Manages document operations
- `AuthGuard`: Protects routes based on authentication status
- `JwtInterceptor`: Adds JWT token to HTTP requests

## API Integration

The frontend communicates with the following endpoints:

### Authentication
- POST `/auth/register` - User registration
- POST `/auth/authenticate` - User login

### Documents
- POST `/documents/upload` - Upload document
- GET `/documents` - Get user's documents
- GET `/documents/search` - Search documents
- DELETE `/documents/{id}` - Delete document

## Development

1. Start the development server:
```bash
ng serve
```

2. Run tests:
```bash
ng test
```

3. Build for production:
```bash
ng build --prod
```

## Docker Deployment

1. Build the production image:
```bash
docker build -t document-management-frontend:prod .
```

2. Run the production container:
```bash
docker run -p 80:80 document-management-frontend:prod
```

## License

This project is licensed under the MIT License. 