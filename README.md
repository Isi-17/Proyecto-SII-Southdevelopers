# Tarea 1 - Entidades JPA y generación de esquema de base de datos

En esta tarea, el grupo **SouthDevelopers** ha creado dos microservicios utilizando Java Persistence API (JPA) y Maven en un proyecto de un solo módulo. El objetivo de esta entrega es proporcionar las entidades JPA correctamente anotadas con sus métodos de apoyo, incluyendo getters y setters, equals y hashCode, y toString.

## Microservicio de Gestión de Usuarios

Este microservicio se encarga de permitir a los usuarios de la aplicación cambiar sus datos de contacto, cambiar su contraseña y generar un token de autenticación para acceder a los servicios web. El microservicio cuenta con las siguientes entidades JPA:

- User: entidad que representa a un usuario de la aplicación con sus respectivos atributos.

- UserManagementApplication: clase principal del microservicio de gestión de usuarios que contiene la lógica para crear, actualizar, buscar y eliminar usuarios de la base de datos.

## Microservicio de Gestión del Estudiantado

Este microservicio se encarga de importar información de alumnos a partir de archivos CSV o Excel, obtener el listado de alumnos con la posibilidad de filtrarlos y ordenarlos por algunos campos. Además, la información de los alumnos se contextualiza en función del año. En el curso vigente, se puede eliminar y regenerar el listado, pero pasada una fecha, se bloquea.

Las entidades JPA del microservicio de Gestión del Estudiantado son las siguientes:

- Enrolment: entidad que representa la matrícula de un alumno en una asignatura en un determinado año.

- SpecialNeeds: entidad que representa las necesidades especiales de un alumno con sus respectivos atributos.

- Student: entidad que representa a un alumno con sus respectivos atributos.

- StudentManagementApplication: clase principal del microservicio de gestión del estudiantado que contiene la lógica para importar información de alumnos, obtener el listado de alumnos y gestionar las matrículas y necesidades especiales de los alumnos.

- Subject: entidad que representa una asignatura con sus respectivos atributos.

## Generación de esquema de base de datos

El proyecto cuenta con una clase principal que genera las instrucciones DDL para generar el esquema de la base de datos al ejecutar la clase principal. Esto se realiza automáticamente mediante el plugin `maven-compiler-plugin` de Maven.

# Tarea 2 - Implementación de microservicios

En esta tarea, el grupo **SouthDevelopers** ha implementado los microservicios de Gestión de Usuarios y Gestión del Estudiantado utilizando el framework Spring Boot. Cada microservicio se encuentra en su propio proyecto Maven, los cuales incluyen:

- Las entidades JPA necesarias, las cuales fueron entregadas en la tarea 1.
- Los repositorios de Spring Data JPA necesarios.
- Los servicios implementando la capa de negocio.
- Los controladores REST implementando la capa de presentación.
- Un conjunto de casos de prueba desarrolladas con JUnit 5 para probar el microservicio.

## Microservicio de Gestión de Usuarios

El microservicio de Gestión de Usuarios cuenta con la siguiente estructura de paquetes:

- `src.main.java.com.uma.southdevelopers.controllers`: contiene los controladores REST para la gestión de usuarios.
- `src.main.java.com.uma.southdevelopers.dto`: contiene las clases de transferencia de datos para la gestión de usuarios.
- `src.main.java.com.uma.southdevelopers.entities`: contiene las entidades JPA para la gestión de usuarios.
- `src.main.java.com.uma.southdevelopers.repositories`: contiene los repositorios de Spring Data JPA para la gestión de usuarios.
- `src.main.java.com.uma.southdevelopers.security`: contiene la configuración de seguridad para la gestión de usuarios.
- `src.main.java.com.uma.southdevelopers.service`: contiene los servicios para la gestión de usuarios.

La clase `UserManagementApplication.java` contiene la clase principal del microservicio de Gestión de Usuarios. En `UserController.java` se encuentra la lógica para crear, actualizar, buscar y eliminar usuarios de la base de datos.

## Microservicio de Gestión del Estudiantado

El microservicio de Gestión del Estudiantado cuenta con la siguiente estructura de paquetes:

- `src.main.java.com.uma.southdevelopers.controllers`: contiene los controladores REST para la gestión de institutos, materias y estudiantado.
- `src.main.java.com.uma.southdevelopers.dto`: contiene las clases de transferencia de datos para la gestión del estudiantado.
- `src.main.java.com.uma.southdevelopers.entities`: contiene las entidades JPA para la gestión del estudiantado.
- `src.main.java.com.uma.southdevelopers.repositories`: contiene los repositorios de Spring Data JPA para la gestión del estudiantado.
- `src.main.java.com.uma.southdevelopers.service`: contiene los servicios para la gestión del estudiantado.

La clase `StudentManagementApplication.java` contiene la clase principal del microservicio de Gestión del Estudiantado. En `StudentController.java` se encuentra la lógica para importar información de alumnos, obtener el listado de alumnos y gestionar las matrículas y necesidades especiales de los alumnos.

## Casos de prueba

Cada microservicio cuenta con un conjunto de casos de prueba desarrolladas con JUnit 5. Los casos de prueba se encuentran en las siguientes clases:

- `userManagementTest.java`: casos de prueba para el microservicio de Gestión de Usuarios.
- `StudentManegementApplicationTests.java`: casos de prueba para el microservicio de Gestión del Estudiantado.

