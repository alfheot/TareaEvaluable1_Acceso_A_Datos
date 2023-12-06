import database.GestionDB;
import database.SchemeDB;

import java.sql.*;
import java.util.ArrayList;

public class Pedido {
    private int idDelPedido;

    static int numeroDeProductoVendido = comprobarNumeroPedidos();
    //private String descripcion;
    private int precioTotal = 0;
    static int numPedido = comprobarNumeroPedidos();

    static ArrayList<Integer> idsProductosPedido;

    public Pedido(ArrayList <String[]> listado){
        idDelPedido = ++numPedido;
        crearPedido(listado);

    }

    public int getPrecioTotal(ArrayList<String[]> listado){
        for(String [] producto: listado){
            precioTotal += Integer.parseInt(producto[3]);
        }
        return precioTotal;
    }

    public void crearPedido(ArrayList<String[]> listado){

        Connection connection = GestionDB.getConnection();



        precioTotal = getPrecioTotal(listado);

        for(String [] producto: listado){
            int id = Integer.parseInt(producto[0]);
            String descripcion = producto[2];
            int precio = Integer.parseInt(producto[3]);


            try {

                PreparedStatement preparedStatement = connection.prepareStatement(
                        (String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUE(?,?,?,?,?)",
                                SchemeDB.DB_TABLA_PEDIDO,
                                SchemeDB.TAB_PED_COL_NUM_PRO_VENDIDO,
                                SchemeDB.TAB_PED_COL_ID,
                                SchemeDB.TAB_PED_COL_ID_PRODUCTO,
                                SchemeDB.TAB_PED_COL_DESCRIPCION,
                                SchemeDB.TAB_PED_COL_PRECIO_TOTAL)));
                preparedStatement.setInt(1, ++numeroDeProductoVendido);
                preparedStatement.setInt(2,idDelPedido);
                preparedStatement.setInt(3,id);
                preparedStatement.setString(4,descripcion);
                preparedStatement.setInt(5,precioTotal);

                preparedStatement.execute();

            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }



    }

    public int getId() {
        return idDelPedido;
    }





    public static int getNumPedido() {
        return numPedido;
    }

    public static int comprobarNumeroPedidos() {
        Connection connection = GestionDB.getConnection();
        int total = 0;
        try {
            // Crear la consulta SQL para contar las filas en la tabla de empleados
            String consulta = String.format("SELECT COUNT(*) FROM %s", SchemeDB.DB_TABLA_PEDIDO);

            // Preparar la declaración SQL
            PreparedStatement preparedStatement = connection.prepareStatement(consulta);

            // Ejecutar la consulta y obtener el resultado
            ResultSet resultSet = preparedStatement.executeQuery();

            // Obtener el número de filas del resultado
            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }

            // Cerrar recursos
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(("Total Pedidos Hechos: " + total));
        return total;
    }

    static public void mostrarPedidos(){

        Connection connection = GestionDB.getConnection();
        int idPedido = 0;
        int precioTotalPedido = 0;
        int idProducto = 0;

        try {
            String miSolicitud = String.format("SELECT * FROM %s", SchemeDB.DB_TABLA_PEDIDO);
            PreparedStatement statement = connection.prepareStatement(miSolicitud);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Pedidos:");
            System.out.println("ID Pedido -- Precio Total");

            while (resultSet.next()){
                idPedido = resultSet.getInt(SchemeDB.TAB_PED_COL_ID);
                idProducto = resultSet.getInt(SchemeDB.TAB_PED_COL_ID_PRODUCTO);
                precioTotalPedido = resultSet.getInt(SchemeDB.TAB_PED_COL_PRECIO_TOTAL);
                System.out.println(String.format(
                        "Pedido Nº: %s - ID del Producto: %s - Importe Total: %s"
                        ,idPedido
                        ,idProducto
                        ,precioTotalPedido
                ));
            }
            // Cerrar recursos
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
