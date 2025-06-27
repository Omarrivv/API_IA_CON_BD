# Sistema de APIs de Inteligencia Artificial

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)
![Kubernetes](https://img.shields.io/badge/Kubernetes-v1.33.2-blue.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.5-blue.svg)

Sistema integrado de múltiples servicios de IA (ChatGPT, Meta Llama, Gemini) desarrollado con Spring WebFlux y desplegado en Kubernetes.

## 🚀 Características Principales

- Arquitectura reactiva con Spring WebFlux
- Integración con múltiples servicios de IA
- Base de datos PostgreSQL con R2DBC
- Despliegue en Kubernetes
- Gestión de migraciones con Flyway

## 📋 Requisitos Previos

- Java 17
- Docker
- Kubernetes
- PostgreSQL 17.5+
- Maven

## 🔧 Configuración del Entorno

### Variables de Entorno Necesarias

```yaml
# Base de Datos
DATABASE_URL: r2dbc:postgresql://[USER]:[PASSWORD]@[HOST]:[PORT]/[DB]?sslMode=require
DATABASE_USERNAME: [USERNAME]
DATABASE_PASSWORD: [PASSWORD]

# ChatGPT API
RAPIDAPI_URL: https://chatgpt-42.p.rapidapi.com
RAPIDAPI_HOST: chatgpt-42.p.rapidapi.com
RAPIDAPI_APIKEY: [API_KEY]

# Meta Llama API
LLAMAAPI_URL: https://meta-llama-llama-3-1-8b-instruct.p.rapidapi.com
LLAMAAPI_HOST: meta-llama-llama-3-1-8b-instruct.p.rapidapi.com
LLAMAAPI_APIKEY: [API_KEY]

# Gemini API
GEMINI_API_KEY: [API_KEY]
GEMINI_API_URL: https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent
```

## 🛠️ Instalación y Despliegue

### 1. Clonar el Repositorio

```bash
git clone https://github.com/[tu-usuario]/API_IA_CON_BD.git
cd API_IA_CON_BD
```

### 2. Construir la Imagen Docker

```bash
docker build -t omarweb/spring-webflux:latest .
```

### 3. Desplegar en Kubernetes

```bash
# Crear namespace
kubectl apply -f manifest/namespace.yml

# Crear secrets
kubectl apply -f manifest/secret.yml

# Crear service
kubectl apply -f manifest/service.yml

# Crear deployment
kubectl apply -f manifest/deployment.yml

# Cambiar al namespace
kubectl config set-context --current --namespace=omarrivera
```

## 📡 Endpoints Disponibles

### API General
```
GET    /api/ai           - Lista todas las interacciones
GET    /api/ai/{id}      - Obtiene una interacción específica
POST   /api/ai           - Crea una nueva interacción
PUT    /api/ai/{id}      - Actualiza una interacción
DELETE /api/ai/{id}      - Elimina una interacción
```

### ChatGPT
```
GET    /api/ai/chatgpt           - Lista conversaciones
GET    /api/ai/chatgpt/{id}      - Obtiene conversación
POST   /api/ai/chatgpt           - Nueva conversación
PUT    /api/ai/chatgpt/{id}      - Actualiza conversación
DELETE /api/ai/chatgpt/{id}      - Elimina conversación
GET    /api/ai/chatgpt/inactive  - Lista inactivas
```

### Meta Llama
```
GET    /api/ai/metallama         - Lista interacciones
GET    /api/ai/metallama/{id}    - Obtiene interacción
POST   /api/ai/metallama         - Nueva interacción
PUT    /api/ai/metallama/{id}    - Actualiza interacción
DELETE /api/ai/metallama/{id}    - Elimina interacción
```

### Gemini
```
POST   /api/gemini/chat          - Inicia conversación
GET    /api/gemini/list          - Lista conversaciones
PUT    /api/gemini/delete/{id}   - Desactiva conversación
```

## 📦 Estructura del Proyecto

```
API_IA_CON_BD/
├── src/
│   └── main/
│       ├── java/pe/edu/vallegrande/spring_webflux/
│       │   ├── config/         - Configuraciones
│       │   ├── model/          - Entidades
│       │   ├── repository/     - Repositorios
│       │   ├── rest/           - Controladores
│       │   └── service/        - Servicios
│       └── resources/
│           └── db/migration/   - Migraciones Flyway
├── manifest/
│   ├── deployment.yml
│   ├── namespace.yml
│   ├── secret.yml
│   └── service.yml
└── Dockerfile
```

## 🔐 Seguridad

- Secretos gestionados por Kubernetes
- SSL habilitado para conexiones a BD
- Headers de autenticación para APIs externas

## 📊 Monitoreo

- Logs a nivel DEBUG en desarrollo
- Monitoreo de pods y servicios Kubernetes
- Trazabilidad de peticiones WebClient

## 🔄 CI/CD

- Repositorio: GitHub
- Registro: Docker Hub
- Imagen: omarweb/spring-webflux:latest
- Despliegue: Kubernetes

## 📝 Notas Importantes

1. Asegúrate de tener todas las credenciales necesarias
2. Verifica la conectividad con las APIs externas
3. Monitorea el uso de recursos en Kubernetes
4. Mantén actualizadas las dependencias
5. Revisa los logs regularmente

## 🤝 Contribuir

1. Fork el proyecto
2. Crea tu Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push al Branch (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para detalles

## ✨ Autores

* **Omar Rivera** - *Desarrollo Inicial* - [Omarrivv](https://github.com/Omarrivv)

## 🎯 Estado del Proyecto

El proyecto está en desarrollo activo y se actualiza regularmente.
