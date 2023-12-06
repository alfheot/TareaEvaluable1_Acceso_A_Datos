import database.GestionDB;
import database.SchemeDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Producto {
    private int id;
    private String title;
    private String descripcion;
    private long precio;

    public Producto(int id, String nombre, String description, int precio){
        this.id = id;
        this.descripcion = description;
    }

    public int getID() { return id; }
    public void setID(int value) { this.id = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getDescription() { return descripcion; }
    public void setDescription(String value) { this.descripcion = value; }



    static public void mostrarProductos(){

        Connection connection = GestionDB.getConnection();
        int idProducto = 0;
        String nombre = "";
        int precioProducto = 0;
        String descripcion = "";


        try {
            String miSolicitud = String.format("SELECT * FROM %s", SchemeDB.DB_TABLA_PRODUCTO);
            PreparedStatement statement = connection.prepareStatement(miSolicitud);
            ResultSet resultSet = statement.executeQuery();
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
            }
            // Cerrar recursos
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void mostrarProductosInferiores600(){

        Connection connection = GestionDB.getConnection();
        int idProducto = 0;
        String nombre = "";
        int precioProducto = 0;
        String descripcion = "";

        try {
            String miSolicitud = String.format("SELECT * FROM %s WHERE %s < 600", SchemeDB.DB_TABLA_PRODUCTO, SchemeDB.TAB_PROD_COL_PRECIO);
            PreparedStatement statement = connection.prepareStatement(miSolicitud);
            ResultSet resultSet = statement.executeQuery();
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
            }
            // Cerrar statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
