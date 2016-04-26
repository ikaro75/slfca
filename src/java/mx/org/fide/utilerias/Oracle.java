
package mx.org.fide.utilerias;

import java.sql.ResultSet;
import java.sql.SQLException;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Fallo;

/**
 *
 * @author ILCE
 */
public class Oracle {

    public Oracle() {
    }
    
    public void DropSequencer() throws SQLException {
        Conexion cx=null; 
        ResultSet rs;
        try {
            cx = new Conexion();
            //1. Extraer todos las tablas de la base CE BE
            String s = "SELECT sequence_name FROM ALL_SEQUENCES where sequence_owner='CECYTEM'";
            rs = cx.getRs(s);
            
            while (rs.next()) {
               //2. Borrar todos los objetos sequence de esas tablas
                s="DROP SEQUENCE IF EXISTS "+rs.getString("sequence_name");
                //System.out.println(s);
                cx.execute(s);
               
            }
            
        } catch(Fallo f) {
            
        } finally {
            cx.cierraConexion();
        }
    }
    
    public void CreateSequencer() throws SQLException{
        Conexion cx=null; 
        int max,contador=1;
        ResultSet rs;
        ResultSet rs1;
        try {
            cx = new Conexion();
            //Extraer nombre de las tablas
            String s = "SELECT substr(table_name,1,3) as abr_name, table_name, column_name FROM SYS.ALL_TAB_COLS WHERE owner='CECYTEM' AND column_id=1";
            rs = cx.getRs(s);
            
            
            while (rs.next()) {
                
                if (rs.getString("table_name").equals("CE_PLANTEL_ESPECIALIDAD")) continue;
                if (rs.getString("table_name").equals("BE_TIPO_ACCION")) continue;
                
                String table = rs.getString("table_name").replaceAll("A", "").replaceAll("E", "").replaceAll("I", "").replaceAll("O", "").replaceAll("U", "");
                String campo = rs.getString("column_name").replaceAll("A", "").replaceAll("E", "").replaceAll("I", "").replaceAll("O", "").replaceAll("U", "");
                //Extraer el maximo de cada PK
                s = "SELECT nvl(max("+ rs.getString("column_name") + "),0)+1 as max_value FROM "+ rs.getString("table_name");
                rs1 = cx.getRs(s);
                
                if (rs1.next()){
                    max = rs1.getInt("max_value");
                
                //Crear los objetos sequence utilizando el valor MAX de las llaves primarias
                s = "CREATE SEQUENCE " + rs.getString("abr_name") + rs.getString("column_name") + contador + " START WITH " + max + " INCREMENT BY 1 NOMAXVALUE";
                System.out.println(contador+" "+s);
                cx.execute(s);
                
               //Creacion de trigger
                s ="create trigger " 
                   .concat(table)                   
                   .concat(campo)     
                   .concat("_PK")
                   .concat("\n before insert on ").concat(rs.getString("table_name"))
                   .concat("\nfor each row \n")
                   .concat("begin \n")
                   .concat("if inserting then \n")
                   .concat("if :NEW.")
                   .concat(rs.getString("column_name"))
                   .concat(" is null then \n")
                   .concat("select ").concat( rs.getString("abr_name") + rs.getString("column_name") + contador).concat(".nextval into :new.").concat(rs.getString("column_name")).concat(" from dual;\n")
                   .concat("end if; \n")
                   .concat("end if; \n")
                   .concat("end;");                
                System.out.println(contador+" "+s);
                cx.execute(s);
                
                contador++;
                }
            }
            
        } catch(Fallo f) {
            System.out.print("Error:" + f.getMessage());
        } finally {
            cx.cierraConexion();
        }
    }
    
    public void formaMaker(){
        
    }
    
   public static void main(String [] args) throws SQLException{
       System.out.println("Hola");
       
       Oracle o = new Oracle();
       o.CreateSequencer();
       
       //1. crear la 
   } 
}
