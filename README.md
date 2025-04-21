# Todo List API con Autenticación JWT

Esta es una API para la gestión de tareas, diseñada con **Java 23**, **Spring Boot**, y **PostgreSQL** como base de datos. Incluye autenticación basada en **JWT**, un sistema de roles y permisos personalizables, notificaciones por correo electrónico, y funcionalidades avanzadas para la gestión de tareas y subtareas.

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalados los siguientes programas:

- **Java 23** o superior
- **Maven** (para construir el proyecto)
- **PostgreSQL** (o cualquier base de datos compatible con JDBC)
- **Podman** o **Docker** (opcional, para ejecutar el proyecto en contenedores)
- (Opcional) **Git** para clonar el repositorio

---

## Instalación

### 1. Clonar el repositorio

Clona este repositorio en tu máquina local:

```bash
git clone https://github.com/tu_usuario/tu_repositorio.git
cd tu_repositorio
```
### 2. Configuracion
#### Local
 - Si es local crea la base de datos en PostgresSQL con el nombre "todo"
 - Crea un archivo .env en la carpeta raiz del proyecto y pega las siguientes variables de entorno
  ```
  # Configuración de JWT
  #CLAVE SECRETA PARA CREAR LOS TOKENS LA PUEDES CAMBIAR POR UNA CON IGUAL NUMERO DE CARACTERES
  JWT_SECRET=pK7@Lm9$zXv2Ej5#Qw8RtY3*Gb6Fn4!Hd0Cs1A
  #TIEMPO EN EL QUE PUEDE DURAR UN TOKEN EN MILISEGUNDOS
  JWT_EXPIRATION=900000
  #TIEMPO EN EL QUE PUEDE DURAR UN TOKEN DE LARGA DURACION MILISEGUNDOS
  JWT_EXPIRATION_REFRESH=604800000
  
  # Configuración de la base de datos
  DATA_SOURCE_URL=jdbc:postgresql://localhost:5432/todo
  DATA_SOURCE_USERNAME=postgres
  DATA_SOURCE_PASSWORD=postgres
  
  # Configuración del correo electrónico SMTP
  #SMTP DE GMAIL pon tus datos
  MAIL_ADDRESS=tu_correo@gmail.com
  MAIL_PASSWORD=tu_contraseña_de_aplicación
  ```
- Ejecuta mvn spring-boot:run
####Podman o Docker
- Si es por podman o docker configurar las variables en el archivo podman-compose.yml
- Ejecutar podman-compose up --build
