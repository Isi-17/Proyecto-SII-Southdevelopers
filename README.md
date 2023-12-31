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

Cada microservicio cuenta con un conjunto de casos de prueba desarrolladas con JUnit 5. Los casos de prueba se encuentran en los siguientes ficheros:

- `userManagementTest.java`: casos de prueba para el microservicio de Gestión de Usuarios.
- `StudentManegementApplicationTests.java`: casos de prueba para el microservicio de Gestión del Estudiantado.

# Tarea 3 - Frontend para la aplicación

En esta tarea, el grupo **SouthDevelopers** ha desarrollado una aplicación Angular para cada microservicio del proyecto. Cada aplicación Angular implementa un conjunto de requisitos específicos relacionados con el microservicio correspondiente.

La aplicación Angular está ubicada en el mismo repositorio que el microservicio de Gestión de Usuarios, en una carpeta que contiene la cadena "frontend". El repositorio contiene los siguientes elementos:
- Los microservicios implementados en Spring necesarios para respaldar la ejecución de la aplicación Angular. Estos microservicios ya fueron entregados en la tarea T2, pero podrían haber sufrido modificaciones para corregir posibles errores.
- Una aplicación Angular que conste de uno o varios componentes y al menos un servicio que se conecte con el microservicio implementado en Spring.
- Un conjunto de casos de prueba para cada componente, con al menos tres pruebas por componente y un mínimo de cinco pruebas por cada aplicación.

## Microservicio de Gestión de Usuarios

Para este microservicio, se ha implementado la funcionalidad de login y reseteo de contraseña. La aplicación Angular permite a un usuario iniciar sesión utilizando sus credenciales y obtener un token JWT para acceder a los servicios. Además, se proporciona la opción de resetear la contraseña en caso de que el usuario la haya olvidado. La nueva contraseña generada se muestra en el registro de actividad del microservicio.


## Microservicio de Gestión del Estudiantado

Para este microservicio, se ha implementado el CRUD (Crear, Leer, Actualizar, Eliminar) de institutos. La aplicación Angular permite visualizar los institutos registrados en el sistema, editar su información, añadir nuevos institutos y eliminarlos según sea necesario.

## Casos de prueba

Cada microservicio cuenta con un conjunto de casos de prueba desarrollados con el framework de pruebas de Angular.

### Microservicio de Gestión de Usuarios

Los casos de prueba para el microservicio de Gestión de Usuarios se encuentran en los siguientes ficheros:

- `app.component.spec.ts`
  * El nombre es 'userAngular'
  * Se crea la app
- `users.service.spec.ts`
  * El servicio se crea correctamente
- `login.component.spec.ts`
  * Se debe ejecutar `forgotPassword()` cuando el botón submit se pulsa el botón para recuperar la contraseña
  * Se debe ejecutar `login()` cuando el botón sunmit se pulsa el botón de submit
  * Se crea correctamente el componenete
  * Debe aparecer el formulario vacío
  * Debe generar correctamente la página de login
- `forgotPassword.component.spec.ts`
  * Se crea correctamente el componente
  * Debe aparecer el formulario vacío
  * Debe generar correctamente la página de recuperación de contraseña
  * Se debe ejecutar `forgotPassword()` cuando el botón se pulsa

### Microservicio de Gestión de Institutos

Los casos de prueba para el microservicio de Gestión de Institutos se encuentran en los siguientes ficheros:

- `app.component.spec.ts`
  * Al darle al botón de eliminar el instituto se elimina
  * Se debe ejecutar `addInstituto()` cuando el boton add se pulsa
  * Se debe ejecutar `obtainInstitutos()` cuando el botón search se pulsa
  * Deberíamos obtener el instituto al buscar por el id
  * Deberíamos obtener todos los institutos al darle a obtener todos, después de buscar por id
- `institutos.service.spec.ts`
  * El service se crea correctamente
- `formulario-instituto.component.spec.ts`
  * Debe aparecer el formulario con los datos del instituto y con el texto editar instituto
  * Debe generar correctamente el formulario con los inputs y botones necesarios
  * Debe aparecer el formulario vacío y con el texto añadir instituto







