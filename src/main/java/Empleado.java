import database.GestionDB;
import database.SchemeDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Empleado {
    private int id;
    private String nombre;
    private String apellidos;
    private String correo;

    static int idEmp = comprobarNumeroEmpleados(); // la 1ª vez que se ejecuta el programa es 0
                                                  //  el resto comprueba en la bbdd cuantos empleados hay



    public Empleado(String nombre,  String apellidos, String correo){

        this.id = ++idEmp;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        agregarEmpleado();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public static int getIdEmp() {
        return idEmp;
    }

    public void agregarEmpleado(){
        Connection connection = GestionDB.getConnection();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement
                            (String.format("INSERT INTO %s (%s,%s,%s,%s) VALUE (?,?,?,?)",
                    SchemeDB.DB_TABLA_EMPLEADO,
                    SchemeDB.TAB_EMP_COL_ID,
                    SchemeDB.TAB_EMP_COL_NOMBRE,
                    SchemeDB.TAB_EMP_COL_APELLIDOS,
                    SchemeDB.TAB_EMP_COL_CORREO));
            preparedStatement.setInt(1,this.id);
            preparedStatement.setString(2,this.nombre);
            preparedStatement.setString(3, this.apellidos);
            preparedStatement.setString(4, this.correo);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static int comprobarNumeroEmpleados() {
        Connection connection = GestionDB.getConnection();
        int total = 0;
        try {
            // Crear la consulta SQL para contar las filas en la tabla de empleados
            String consulta = String.format("SELECT COUNT(*) FROM %s", SchemeDB.DB_TABLA_EMPLEADO);

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
        System.out.println(("Total de empleados: " + total));
        return total;
    }

    public static void mostrarEmpleados(){
        Connection connection = GestionDB.getConnection();
        int id = 0;
        String nombre = "";
        String apellidos = "";
        String correo = "";
        try {
            // Crear la consulta SQL para contar las filas en la tabla de empleados
            String miSolicitud = String.format("SELECT * FROM %s", SchemeDB.DB_TABLA_EMPLEADO);
            // Preparar la declaración SQL
            PreparedStatement statement = connection.prepareStatement(miSolicitud);
            // Ejecutar la consulta y obtener el resultado
            ResultSet resultSet = statement.executeQuery();

            System.out.println("EMPLEADOS:");
            System.out.println("ID Empleado -- Nombre  --  Apellidos -- Correo ");


            while (resultSet.next()){
                id= resultSet.getInt(SchemeDB.TAB_EMP_COL_ID);
                nombre = resultSet.getString(SchemeDB.TAB_EMP_COL_NOMBRE);
                apellidos = resultSet.getString(SchemeDB.TAB_EMP_COL_APELLIDOS);
                correo = resultSet.getString(SchemeDB.TAB_EMP_COL_CORREO);
                System.out.println(String.format(
                        " %s  - - - -  %s - - - -  %s - - - -  %s"
                        ,id
                        ,nombre
                        ,apellidos
                        ,correo
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
