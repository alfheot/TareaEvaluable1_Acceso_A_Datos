package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestionDB {
    static Connection connection; // sólo quiero que exista un objeto Connection, por eso lo hago static, para que no haga falta crear objetos para usarlo, sino que lo use la clase directamente.

    private static void createConnection(){
        try {
            System.out.println(("ENTRA EN EL TRY DE CREATECONNECTION"));

            Class.forName("com.mysql.cj.jdbc.Driver");//Cargamos el driver jdbc para poder conectarnos a la BBDD

            String url = String.format("jdbc:mysql://%s/%s", SchemeDB.HOST, SchemeDB.DB_NAME);
            connection = DriverManager.getConnection(url,"root", "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection(){
        //Creamos la conexión sólo si es Null
        if(connection == null){
            System.out.println(("LA CONEXIÓN SE HA CREADO"));
            createConnection();
        }

        return connection;

    }
}
