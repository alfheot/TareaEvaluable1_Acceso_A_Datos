import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import database.GestionDB;
import database.SchemeDB;
import org.json.JSONArray;
import org.json.JSONObject;


public class Main {


    public static void main(String[] args) {

        System.out.println("Nº de empleados: "+Empleado.idEmp);

        Boolean condicion = true;
        int eleccion = 0;

        Connection connection = GestionDB.getConnection();

        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 9){

            System.out.println("Elige una opción:\n");
            System.out.println("1.- Cargar BBDD Productos.");
            System.out.println("2.- Introducir Empleado");
            System.out.println("3.- Introducir Pedido");
            System.out.println("4.- Mostrar todos los empleados");
            System.out.println("5.- Mostrar todos los productos");
            System.out.println("6.- Mostrar todos los pedidos");
            System.out.println("7.- Mostrar productos de menos de 600€");
            System.out.println("8.- Rellenar tabla favoritos y mostrarla en consola");
            System.out.println("9.- Salir del programa");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion){

               case 1: {

                   try {

                       Statement statement = connection.createStatement();
                       //------LAS LÍNEAS 4 SIGUIENTES NO HE SABIDO HACERLAS POR MI CUENTA
                       //------PARA CARGAR LA BBDD SOLO LA 1ª VEZ, CUANDO NO HAY DATOS
                       ResultSet resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM %s", SchemeDB.DB_TABLA_PRODUCTO));
                       resultSet.next();
                       int rowCount = resultSet.getInt(1);

                       if (rowCount == 0) {
                           URL url = new URL("https://dummyjson.com/products");
                           HttpURLConnection connectionUrlProductos = (HttpURLConnection) url.openConnection();
                           BufferedReader br = new BufferedReader(new InputStreamReader(connectionUrlProductos.getInputStream()));

                           //Para garantizar que leemos bien y por completo el JSON:
                           StringBuffer stringBuffer = new StringBuffer();
                           String linea = "";
                           while((linea = br.readLine()) != null){
                               stringBuffer.append(linea);
                           }

                           //Convierto en un objeto JSON el string
                           //Pego la dependencia org.json en pom.xml
                           //Botón derecho, Maven, Reload Project para que la reconozca
                           //Lo importo y ya puedo usar JSONObject

                           JSONObject jo = new JSONObject(stringBuffer.toString());
                           System.out.println(jo);
                           System.out.println("----------------------------------");

                           JSONArray ja = jo.getJSONArray("products");
                           System.out.println(ja);
                           System.out.println("----------------------------------");

                           int id = 0;
                           String nombre = "";
                           String descripcion = "";
                           int cantidad= 0;
                           int precio = 0;

                           for(int i = 0; i < ja.length(); i++){

                               JSONObject jo2 = ja.getJSONObject(i);



                               id = jo2.getInt("id");
                               nombre = jo2.getString("title");
                               descripcion = jo2.getString("description");
                               cantidad = jo2.getInt("stock");
                               precio = jo2.getInt("price");

                               //System.out.println(String.format("Id: %s Nombre: %s Cantidad: %s Precio: %s",id, nombre,cantidad,precio));

                               //Preparo la orden que le voy a pasar a la BBDD
                               PreparedStatement preparedStatement =
                                       connection.prepareStatement(
                                               String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUE(?,?,?,?,?)",
                                                       SchemeDB.DB_TABLA_PRODUCTO,
                                                       SchemeDB.TAB_PROD_COL_ID,
                                                       SchemeDB.TAB_PROD_COL_NOMBRE,
                                                       SchemeDB.TAB_PROD_COL_DESCRIPCION,
                                                       SchemeDB.TAB_PROD_COL_CANTIDAD,
                                                       SchemeDB.TAB_PROD_COL_PRECIO)
                                       );

                               //Le aseguro que la orden tiene los tipos que espera:
                               preparedStatement.setInt(1, id);
                               preparedStatement.setString(2, nombre);
                               preparedStatement.setString(3, descripcion);
                               preparedStatement.setInt(4, cantidad);
                               preparedStatement.setInt(5, precio);

                               //Ahora ejecutamos la solicitud a la BBDD
                               preparedStatement.execute();

                           }
                           break;
                       }
                       else{
                           System.out.println("La BBDD ya estaba cargada");
                           main(new String[0]);

                           break;
                       }
                   } catch (FileNotFoundException e) {
                       System.out.println("Ups");
                       System.out.println(e.getMessage());
                       throw new RuntimeException(e);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   } catch (SQLException e) {
                       throw new RuntimeException(e);
                   }

               }
                case 2:{

/*                    new Empleado("manolo", "fasdf", "afd");
                    new Empleado("mario", "asdf", "email");
                    new Empleado("olga", "adfasf", "3q3asdfemail");
                    break;*/

/*
                    System.out.println("Introduce el nombre:");
                    String nombre = sc.nextLine();

                    System.out.println("Introduce los apellidos:");
                    String apellidos = sc.nextLine();

                    System.out.println("Introduce el email");
                    String email = sc.nextLine();

                    System.out.println("nnnnnn");
                    new Empleado(nombre, apellidos, email);
                    System.out.println("nnnn");
                    break;
*/
                    while(condicion){
                        System.out.println("Introduce el nombre");
                        String nombre = sc.nextLine();

                        System.out.println("Introduce los apellidos");
                        String apellidos = sc.nextLine();

                        System.out.println("Introduce el email");
                        String email = sc.nextLine();

                        new Empleado(nombre, apellidos, email);

                        System.out.println("Empleado introducido correctamente");
                        System.out.println("Elige una opción:");
                        System.out.println("1.- Cargar otro empleado.");
                        System.out.println("2.- Volver al menú principal");
                        eleccion = sc.nextInt();
                        sc.nextLine();

                        if(eleccion == 1){
                            condicion = true;
                        }if(eleccion == 2){
                            condicion = false;
                            break;
                        }
                    }
                    break;
                }

                case 3:{
                    //System.out.println("Vas a agregar 3 pedidos a la vez");
                    // No se me ocurría otro modo para no tener que crear otro objeto Connection en Empleado que mandarlo desde aquí
                    //Pedido p1 = new Pedido(1,"Una cosa 1 muy bonita", 120);
                    //Pedido p2 = new Pedido(2,"Una cosa 2 muy bonita", 220);
                    //Pedido p3 = new Pedido(3,"Una cosa 3 muy bonita", 340);
                    ArrayList<String[]> cestaProductos = new ArrayList<>();
                    System.out.println("¿Cuántos artículos incluye el pedido?");
                    int articulos = sc.nextInt();
                    System.out.println("ARTICULOS "+articulos);
                    int elegido = 0;
                    for(int i= 1; i <= articulos; i++){
                        System.out.println("Elija el id del 1 al 30 del artículo " + i);
                        elegido = sc.nextInt();

                        String consulta = "SELECT * FROM productos WHERE id_producto = ?";
                        try (PreparedStatement statement = connection.prepareStatement(consulta)) {
                            statement.setInt(1, elegido);

                            try (ResultSet resultSet = statement.executeQuery()) {
                                if (resultSet.next()) {
                                    // Procesar los resultados (puedes acceder a las columnas por nombre o índice)
                                    //int id = resultSet.getInt("id");
                                    String nombre = resultSet.getString("nombre");
                                    String descripcion = resultSet.getString("descripcion");
                                    int precio = resultSet.getInt("precio");
                                    System.out.println("PRECIO ARTICULO: " + precio);

                                    String[]producto = {String.valueOf(elegido), nombre,descripcion,String.valueOf(precio)};

                                    cestaProductos.add(producto);

                                    // Realizar las acciones necesarias con la información del artículo
                                    //System.out.println("ID: " + id + ", Nombre: " + nombre);
                                } else {
                                    System.out.println("No se encontró ningún artículo con el ID: " + elegido);
                                }
                            }
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    new Pedido(cestaProductos);
                    break;
                }
                case 4: {
                        Empleado.mostrarEmpleados();
                    break;
                }
                case 5: {
                    Producto.mostrarProductos();
                    break;
                }
                case 6: {
                        Pedido.mostrarPedidos();
                    break;
                }
                case 7: {
                    Producto.mostrarProductosInferiores600();
                    break;
                }
                case 8: {

                         //connection = GestionDB.getConnection();

                        int idProducto = 0;
                        String nombre = "";
                        int precioProducto = 0;
                        String descripcion = "";

                        try {
                            String miSolicitudDos = String.format("SELECT * FROM %s WHERE %s >1000", SchemeDB.DB_TABLA_PRODUCTO, SchemeDB.TAB_PROD_COL_PRECIO);
                            PreparedStatement preparedStatement = connection.prepareStatement(miSolicitudDos);
                            ResultSet resultSet = preparedStatement.executeQuery();
                            System.out.println("PRODUCTOS:");
                            System.out.println("ID  - - - Nombre - - - - Precio - - - - Descripción");

                            while (resultSet.next()){
                                idProducto = resultSet.getInt(SchemeDB.TAB_PROD_COL_ID);
                                nombre = resultSet.getString(SchemeDB.TAB_PROD_COL_NOMBRE);
                                precioProducto = resultSet.getInt(SchemeDB.TAB_PROD_COL_PRECIO);
                                descripcion = resultSet.getString(SchemeDB.TAB_PROD_COL_DESCRIPCION);
                                System.out.println(String.format(
                                        " %s - - - - %s - - - - %s - - - - %s"
                                        ,idProducto
                                        ,nombre
                                        ,precioProducto
                                        ,descripcion
                                ));
                                new Producto_Fav(idProducto, nombre,descripcion, precioProducto);
                            }
                            //Producto_Fav.rellenarTablaFavoritos();

                            // Cerrar recursos
                            resultSet.close();
                            preparedStatement.close();

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    break;
                }
                default: {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Fin del Programa");

                }

            }
        }


        sc.close();

    }


}











// copia seg 29 nov a las 10:08hs
/*
import java.io.*;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.Scanner;

        import database.GestionDB;
        import database.SchemeDB;
        import org.json.JSONArray;
        import org.json.JSONObject;


public class Main {

    public static void main(String[] args) {

        Connection connection = GestionDB.getConnection();

        System.out.println("Elige una opción:\n");
        System.out.println("1.- Cargar BBDD Productos.");
        System.out.println("2.- Introducir Empleado");
        System.out.println("3.- Introducir Pedido");
        System.out.println("4.- Mostrar todos los empleados");
        System.out.println("5.- Mostrar todos los productos");
        System.out.println("6.- Mostrar todos los pedidos");
        System.out.println("7.- Salir del programa");

        Scanner sc = new Scanner(System.in);

        int opcion = sc.nextInt();

        while (opcion != 7){

            switch (opcion){
                case 1: {

                }
            }
        }


        try {
            URL url = new URL("https://dummyjson.com/products");
            HttpURLConnection connectionUrlProductos = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connectionUrlProductos.getInputStream()));

            //Para garantizar que leemos bien y por completo el JSON:
            StringBuffer stringBuffer = new StringBuffer();
            String linea = "";
            while((linea = br.readLine()) != null){
                stringBuffer.append(linea);
            }

            //Convierto en un objeto JSON el string
            //Pego la dependencia org.json en pom.xml
            //Botón derecho, Maven, Reload Project para que la reconozca
            //Lo importo y ya puedo usar JSONObject

            JSONObject jo = new JSONObject(stringBuffer.toString());
            System.out.println(jo);
            System.out.println("----------------------------------");

            JSONArray ja = jo.getJSONArray("products");
            System.out.println(ja);
            System.out.println("----------------------------------");

            int id = 0;
            String nombre = "";
            String descripcion = "";
            int cantidad= 0;
            int precio = 0;


            for(int i = 0; i < ja.length(); i++){

                JSONObject jo2 = ja.getJSONObject(i);

                id = jo2.getInt("id");
                nombre = jo2.getString("title");
                descripcion = jo2.getString("description");
                cantidad = jo2.getInt("stock");
                precio = jo2.getInt("price");

                System.out.println(String.format("Id: %s Nombre: %s Cantidad: %s Precio: %s",id, nombre,cantidad,precio));

                //Preparo la orden que le voy a pasar a la BBDD
                PreparedStatement preparedStatement =
                        connection.prepareStatement(
                                String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUE(?,?,?,?,?)",
                                        SchemeDB.DB_TABLA_PRODUCTO,
                                        SchemeDB.TAB_PROD_COL_ID,
                                        SchemeDB.TAB_PROD_COL_NOMBRE,
                                        SchemeDB.TAB_PROD_COL_DESCRIPCION,
                                        SchemeDB.TAB_PROD_COL_CANTIDAD,
                                        SchemeDB.TAB_PROD_COL_PRECIO)
                        );

                //Le aseguro que la orden tiene los tipos que espera:
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, nombre);
                preparedStatement.setString(3, descripcion);
                preparedStatement.setInt(4, cantidad);
                preparedStatement.setInt(5, precio);

                //Ahora ejecutamos la solicitud a la BBDD
                preparedStatement.execute();

            }


        } catch (FileNotFoundException e) {
            System.out.println("Ups");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Connection connection = GestionDB.getConnection();

        //PreparedStatement preparedStatement = connection.prepareStatement();

        Empleado e1 = new Empleado("Vicente", "Sacristán Vic", "manolozumbon@gmail.com");
        Empleado e2 = new Empleado("Alberto", "López Sánchez", "manolozumbon@gmail.com");
        Empleado e3 = new Empleado("Agustín", "Herráez Chispa", "manolozumbon@gmail.com");
        Empleado e4 = new Empleado("María", "Nuez Jom", "manolozumbon@gmail.com");
*/
/*        System.out.println(e1.getId());
        System.out.println(e2.getId());
        System.out.println(e3.getId());
        System.out.println(e4.getId());*//*


        Pedido p5 = new Pedido(5,"Una cosa 5 muy bonita", 120);
        Pedido p6 = new Pedido(6,"Una cosa 6 muy bonita", 220);
        Pedido p7 = new Pedido(7,"Una cosa 7 muy bonita", 340);

        sc.close();

    }
}*/
