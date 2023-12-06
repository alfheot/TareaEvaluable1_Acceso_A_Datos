package database;

public interface SchemeDB {
    String HOST = "127.0.0.1";
    String DB_NAME = "almacen";


    String DB_TABLA_PRODUCTO = "Productos";
    String DB_TABLA_PRODUCTOS_FAV = "Productos_Fav";
    String TAB_PROD_COL_ID_FAV = "id_fav";
    String TAB_PROD_COL_ID = "id_producto";
    String TAB_PROD_COL_NOMBRE = "nombre";
    String TAB_PROD_COL_DESCRIPCION = "descripcion";
    String TAB_PROD_COL_CANTIDAD = "cantidad";
    String TAB_PROD_COL_PRECIO = "precio";
    String DB_TABLA_EMPLEADO = "Empleados";
    String TAB_EMP_COL_ID = "id_empleado";
    String TAB_EMP_COL_NOMBRE = "nombre";
    String TAB_EMP_COL_APELLIDOS = "apellidos";
    String TAB_EMP_COL_CORREO = "correo";

    //String DB_TABLA_PEDIDO = "Pedidos";
    String DB_TABLA_PEDIDO = "PedidosBuenos";
    String TAB_PED_COL_NUM_PRO_VENDIDO = "num_prod_vendidos";
    String TAB_PED_COL_DESCRIPCION = "descripcion";
    String TAB_PED_COL_PRECIO_TOTAL = "precio_total";
    String TAB_PED_COL_ID = "id_pedido";
    String TAB_PED_COL_ID_PRODUCTO= "id_producto";

}
