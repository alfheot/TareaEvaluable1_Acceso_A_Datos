import database.GestionDB;
import database.SchemeDB;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

public class Producto_Fav extends Producto {

    ArrayList<Integer[]> favoritos = new ArrayList<>();
    Integer[] ids = new Integer[2];
    private int id_fav = 0;
    static int totalFavoritos = 0;
    public Producto_Fav(int id, String nombre, String descripcion, int precio){
        super(id, nombre, descripcion, precio);
        id_fav = ++totalFavoritos;
        ids[0] = id;
        ids[1] = id_fav;
        favoritos.add(ids);
        rellenarTablaFavoritos();

    }

    public void rellenarTablaFavoritos(){

        Connection connection = GestionDB.getConnection();

        int idProducto = 0;
        String nombre = "";
        int precioProducto = 0;
        String descripcion = "";

        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM %s", SchemeDB.DB_TABLA_PRODUCTOS_FAV));
            resultSet.next();
            int rowCount = resultSet.getInt(1);

            if (rowCount == 0) {

                for(int i = 0; i < favoritos.size(); i++){
                    String miSolicitud2 = String.format("INSERT INTO %s (%s, %s) VALUE (?,?)",
                            SchemeDB.DB_TABLA_PRODUCTOS_FAV,
                            SchemeDB.TAB_PROD_COL_ID_FAV,
                            SchemeDB.TAB_PROD_COL_ID);
                    PreparedStatement preparedStatement = connection.prepareStatement(miSolicitud2);
                    preparedStatement.setInt(1,favoritos.get(i)[0]);
                    preparedStatement.setInt(2,favoritos.get(i)[1]);
                    preparedStatement.execute();

                    // Cerrar el statement

                    preparedStatement.close();
                }
            }
            else{
                System.out.println("La BBDD ya estaba cargada");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
