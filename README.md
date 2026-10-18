# Tarae-U1-Persistencia-de-Datos 
Ejercicio Práctico basado en el uso de ORM

1. Descripción del proyecto:

Se desarrolló una aplicación de escritorio que nos ayuda a gestionar productos de una tienda, en esto caso tomamos como referencias a Tiendas TUTI.

2. Objetivo:

Aplicar lo aprendido tras investigar sobre el ORM mediante Hibernate, utilizando una base de datos y una interfaz gráfica para gestionar los productos.

Las Operaciones CRUD para este ejercicio son:

- Create (Crear): registrar nuevos productos y agregarlos al catálogo ingresando, código, nombre, categoría, precio, stock y sucursal. Esta información se guarda en la base de datos mediante Hibernate.
- Read (Leer): Permite ver todos los productos que el usuario registra y buscarlos por su nombre.  
- Update (Actualizar): Permite seleccionar un producto de la tabla para actualizar los precios, las cantidades disponibles y la información de los productos.
- Delete (Eliminar): Permite seleccionar un producto y eliminarlo para despues actualizar la tabla.  

La aplicación se desarrolló en Java Netbeans y utiliza Hibernate como herramienta ORM con el fin de que se nos facilite la comunicacón entre el programa y la base de datos. Jakarta Persistence fue utilizada para definir la entidad del producto, H2 DataBase la cual fue la base de datos que se usó para crera la interfaz gráfica.

3. Estructura del proyecto:

Consta de 4 clases principales:

Main.java: encargado de iniciar la aplicación y mostrar la ventana principal

Producto.java: representa los productos y define sus atributos como el código, nombre, categoría, precio, stock y sucursal.

HibernateUtil.java: encargado de configurar la conexión con la base de datos y administra las sesiones de Hibernate.

ProductoForm.java: esta clase contiene la interfaz gráfica y los métodos para realizar las operaciones CRUD.

4. Interfaz gráfica:

La aplicación tiene campos de entrada para los datos de cada producto, botones para ejecutar las operaciones CRUD y una tabla donde se ven los registros almacenados.

5. Hibernate:

Nos permite trabajar con los productos como objetos Java, sin la necesidad de escribir manualmente todas las instrucciones SQL para las operaciones básicas.
La clase Producto tiene la anotación @Entity, mientras que @Id identifica su clave primaria y @GeneratedValue permite generar automáticamente el identificador.

Para guardar, consultar, actualizar o eliminar productos, la aplicación utiliza sesiones y transacciones de Hibernate.

6. Ejecución del proyecto:

- Abrir el proyecto en NetBeans.
- Verificar que las dependenciad de Hibernate y H2 estén configuradas.
- Ejecutar clase Main.java.

7. Conclusión:

Con el desarrollo de este ejercicio se puso en práctica el uso de Hibernate como una herramienta ORM para poder comprender cómo se pueden gestionar datos desde una aplicación Java.
   
















