package mx.org.fide.modelo;
import mx.org.fide.archivo.Archivo;
import mx.org.fide.controlador.Sesion;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Clase que gestiona la conexión, la extracción de datos de la base y la ejecución de comandos de inserción, actualización
 * y eliminación 
 * @author Daniel
 */
public class Conexion {

    private String server;
    private String db;
    private String user;
    private String pw;
    private Connection conn;
    private Statement st;
    private CallableStatement cs;
    private ResultSet rs;
    private DbType dbType = null;

    /**
     * Clase interna que define el tipo de base de datos
     */
    public enum DbType {
        /**
         * Base de datos de Microsoft SQL Server 
         */
        MSSQL, 
        /**
         * Base de datos de MySQL
         */
        MYSQL, 
        /**
         * Base de datos de Oracle
         */
        ORACLE 
    };

    /**
     * Contructor con parámetros
     * @param server    Nombre del servidor 
     * @param db
     * @param user
     * @param pw
     * @param dbType
     */
    public Conexion(String server, String db, String user, String pw, DbType dbType) {

        this.server = server;
        this.db = db;
        this.user = user;
        this.pw = pw;
        this.dbType = dbType;

    }

    /**
     * Extrae del archivo de configuración la información relacionada a la conexión para la base de datos
     * @throws Fallo si ocurre algún error de E/S
     */
    
    public Conexion (Sesion sesion) throws Fallo {
        if (sesion.getConfiguracion().getConfiguracionActual()==null) {
            throw new Fallo("No se estableció configuración para la sesión actual, verifique");
        }
        
        LinkedHashMap configuracion = (LinkedHashMap) sesion.getConfiguracion().getParametros().get(sesion.getConfiguracion().getConfiguracionActual());
        this.server = configuracion.get("db_server").toString();
        this.db = configuracion.get("db_name").toString();
        this.user = configuracion.get("db_user").toString();
        this.pw = configuracion.get("db_pw").toString();
        this.dbType = DbType.valueOf(configuracion.get("db_type").toString());
    }
    
    public Conexion() throws Fallo {
  
        System.out.println("*** Entre al archivote de configuración");
        try {
            Archivo archivo = new Archivo();
            archivo.lee("/com/administrax/archivo/configuracion.properties");
            this.server = archivo.getPropiedades().getProperty("server");
            this.db = archivo.getPropiedades().getProperty("db");
            this.user = archivo.getPropiedades().getProperty("user");;
            this.pw = archivo.getPropiedades().getProperty("pw");
            this.dbType = DbType.valueOf(archivo.getPropiedades().getProperty("dbtype"));
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }

    }

    /**
     * Extrae del archivo de configuración la información relacionada a la conexión para la base de datos
     * @param settingsFile  Ruta relativa del archivo de configuración de donde se extraen los datos de conexión
     * @throws Fallo si ocurre algún error de E/S     
     */
    public Conexion(String settingsFile) throws Fallo {
      
        /* Si no han sido cargadas, que la configuración  la recupere del archivo */
        System.out.println("*** Entre al archivote de configuración ".concat("settingsFile"));
        try {
            Archivo archivo = new Archivo();
            archivo.lee("/com/administrax/archivo/".concat("settingsFile").concat(".properties"));
            this.server = archivo.getPropiedades().getProperty("server");
            this.db = archivo.getPropiedades().getProperty("db");
            this.user = archivo.getPropiedades().getProperty("user");;
            this.pw = archivo.getPropiedades().getProperty("pw");
            this.dbType = DbType.valueOf(archivo.getPropiedades().getProperty("dbtype"));
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }

    }
    
    /**
     * Establece la base de datos
     * @param sDb   Nombre de la base de datos
     */
    public void setDb(String sDb) {
        db = sDb;
    }

    /**
     * Recupera el nombre de la base de datos
     * @return  Nombre de la base de datos
     */
    public String getDb() {
        return db;
    }

    /**
     * Recupera el password para conectarse a la base de datos
     * @return  Password
     */
    public String getPw() {
        return pw;
    }

    /**
     * Establece el password para conectarse a la base de datos
     * @param pw Password
     */
    public void setPw(String pw) {
        this.pw = pw;
    }

    /**
     * Devuelve el nombre del servidor de base de datos
     * @return Nombre del servidor
     */
    public String getServer() {
        return server;
    }

    /**
     * Establece el nombre del servidor de base de datos
     * @param server    Nombre del servidor
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Recupera usuario de la base de datos
     * @return Usuario 
     */
    public String getUser() {
        return user;
    }

    /**
     * Establece usuario de la base de datos
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Recupera tipo de base de datos
     * @return  Tipo de base de datos
     */
    public DbType getDbType() {
        return dbType;
    }

    /**
     * Establece tipo de base de datos
     * @param dbType    Tipo de base de datos
     */
    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }
    
    /**
     * Abre conexión a la base de datos
     * @param dbType    Tipo de base de datos
     * @throws Fallo si ocurre algún error al conectarse a la base de datos     
     */
    private String openConnection() throws Fallo {
        try {

            if (db == null) {
                db = "ILCE_frmwrk";
            }

            if (conn == null) {
                switch (this.dbType) {
                    case MSSQL:
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        conn = DriverManager.getConnection("jdbc:sqlserver://".concat(server).concat(";databaseName=").concat(db).concat(";selectMethod=cursor;"), user, pw);
                        break;
                    case MYSQL:
                        Class.forName("com.mysql.jdbc.Driver");
                        conn = DriverManager.getConnection("jdbc:mysql://".concat(server).concat("/").concat(db), user, pw);
                        break;
                    case ORACLE:
                        Class.forName ("oracle.jdbc.OracleDriver");
                        //conn = DriverManager.getConnection("jdbc:oracle:thin:@//".concat(server).concat(":").concat(db), user, pw);
                        conn = DriverManager.getConnection("jdbc:oracle:thin:".concat(user).concat("/").concat(pw).concat("@").concat(server).concat(":").concat(db));
                }
            }

            return "";
        } catch (Exception e) {
            // Fallo en algun momento.
            throw new Fallo(e.getMessage());

        }
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void cierraConexion() {
        try {
            if (conn != null) {
                conn.close();
            }
            conn = null;
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Recupera un resultset a partir de una sentencia SQL
     * @param q         Sentencia SQL compatible con la base de datos
     * @return          Objeto <code>java.sql.Resulset</code>
     * @throws Fallo    Si ocurre algún error relacionado a la base de datos
     */
    public ResultSet getRs(String q) throws Fallo {
        String sResultado;
        try {
            sResultado = openConnection();

            if (!sResultado.equals("")) {
                return null;
            }

            System.out.println(q);

            if (this.dbType == DbType.MSSQL) {
                st = conn.createStatement();                
                st.execute("DBCC FREEPROCCACHE");
            }
            
            if (q.toLowerCase().startsWith("exec")) {
                cs = conn.prepareCall(q);
                rs = cs.executeQuery();
            } else {
                
                rs = st.executeQuery(q);
            }

            return rs;
            
        } catch (SQLException sqlex) {
            System.out.println(sqlex);
            throw new Fallo(sqlex.toString());
            //System.out.println(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.toString());

        }
    }

    /**
     * Recupera un resultset a partir de una sentencia SQL
     * @param q                     Sentencia SQL compatible con la base de datos
     * @param ResultSetType         Tipo de resultset que se requiere
     * @param ResultSetConcurrency  Tipo de concurrencia del resultset requerido    
     * @return                      Objeto <code>java.sql.Resulset</code>
     * @throws Fallo                Si ocurre algún error relacionado a la base de datos
     */
    public ResultSet getRs(String q, int ResultSetType, int ResultSetConcurrency) throws Fallo {
        try {
            String sResultado = openConnection();

            if (!sResultado.equals("")) {
                return null;
            }

            System.out.println(q);
            st = conn.createStatement(ResultSetType,
                    ResultSetConcurrency);

            if (this.dbType == DbType.MSSQL) {
                st.execute("DBCC FREEPROCCACHE");
            }

            rs = st.executeQuery(q);

            return rs;
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.toString());

        } 
    }

    /**
     * Ejecuta una sentencia SQL del tipo INSERT, UPDATE o DELETE
     * @param q         Sentencia SQL válida que contiene INSERT, UPDATE o DELETE
     * @return          Objeto <code>java.sql.Resulset</code> con la llave primaria recién creada en el caso de los INSERTs
     * @throws Fallo    si ocurre algún error relacionado a la base de datos
     */
    public ResultSet execute(String q) throws Fallo {
        try {
            rs = null;
            String sResultado = openConnection();
            Integer numberOfRecords;
            if (!sResultado.equals("")) {
                return rs;
            }

            st = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_UPDATABLE);
            if (this.getDbType()==Conexion.DbType.MSSQL || this.getDbType()==Conexion.DbType.MYSQL)
                if (q.toLowerCase().startsWith("exec")) {
                   cs = conn.prepareCall(q);
                   cs.execute();
                } else {
                    numberOfRecords=st.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);
                }    
            else 
                numberOfRecords=st.executeUpdate(q, new int[] {1});
                 
            
            if (q.startsWith("INSERT")) {
                rs = st.getGeneratedKeys();
            }
            
            if (this.getDbType()==Conexion.DbType.ORACLE)
                st.executeUpdate("commit");
                     
            return rs;

        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.toString());
        }
    }

    /**
     * Recupera los campos de una consulta de la base de datos
     * @param s         Sentencia SQL compatible con la base de datos
     * @return          Objeto <code>ArrayList<Campo></code> con los campos de la consulta
     * @throws Fallo    si ocurre algún error relacionado a la base de datos
     */
    public ArrayList<Campo> getCamposPorConsulta(String s) throws Fallo {
        ArrayList<Campo> aRegistro = new ArrayList<Campo>();
        ResultSet oRs;
        Campo fdCampo;
        try {
            oRs = getRs(s);
            for (int i = 1; i <= oRs.getMetaData().getColumnCount(); i++) {
                fdCampo = new Campo(oRs.getMetaData().getColumnName(i), oRs.getMetaData().getColumnTypeName(i), oRs.getMetaData().isAutoIncrement(i));
                aRegistro.add(fdCampo);
            }
            oRs.close();
        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.toString());
        } finally {
            cierraConexion();
            return aRegistro;
        }
    }

    /**
     * Recupera los datos de la consulta de la base de datos
     * @param s         Sentencia SQL compatible con la base de datos
     * @return          Objeto <code>ArrayList<Campo></code> con los registros de la consulta
     * @throws Fallo    si ocurre algún error relacionado a la base de datos
     */
    public ArrayList<ArrayList> getDatosPorConsulta(String s) throws Fallo {
        ArrayList<ArrayList> aQryData = new ArrayList<ArrayList>();
        ResultSet oRs;
        int nCols;
        try {
            oRs = getRs(s);
            nCols = oRs.getMetaData().getColumnCount();
            while (oRs.next()) {
                ArrayList<Object> aRow = new ArrayList<Object>();
                for (int i = 1; i <= nCols; i++) {
                    aRow.add(rs.getObject(i));
                }
                aQryData.add(aRow);
            }
            oRs.close();
        } catch (Exception e) {
            throw new Fallo(e.toString());
        } finally {
            cierraConexion();
            return aQryData;
        }

    }

    /**
     * Recupera los datos de usuario para efectuar el login al sistema
     * @param sUser     Email del usuario
     * @param sPw       Password
     * @return          La clave del usuario si coinciden los datos del usuario, 0 de otro modo
     * @throws Fallo    si ocurre algún error relacionado a la base de datos
     */
    public int getLogin(String sUser, String sPw) throws Fallo {
        String s = openConnection();

        if (!s.equals("")) {
            return 0;
        }

        s = "SELECT e.clave_empleado FROM be_empleado e, be_perfil_aplicacion pa "
                + "WHERE e.clave_perfil=pa.clave_perfil AND "
                + "e.email='" + sUser.replace("'", "''") + "' AND "
                + "e.password='" + sPw.replace("'", "''") + "' AND "
                + "pa.activo=1";

        int nUsuario = 0;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(s);
            if (rs.next()) {
                nUsuario = rs.getInt("clave_empleado");
            }
            rs.close();
        } catch (Exception e) {
            // Fallo en algun momento.
            throw new Fallo(e.toString());
        } finally {
            cierraConexion();
            return nUsuario;
        }
    }

    /**
     * Valida sentencias SQL
     * @param q        Sentencia SQL compatible con la base de datos 
     * @return         Código XML consentencia SQL resuelta, en caso de que la snetencia tenga errores tambien se incluirán
     * @throws Fallo   si ocurre algún error relacionado a la base de datos 
     */
    public String validateSQL(String q) throws Fallo {
        ResultSet rs=null;

        String sResultado = openConnection();

        if (!sResultado.equals("")) {
            throw new Fallo("Error al conectarse a la base de datos");
        }
        try {
            if (q.toLowerCase().startsWith("select")) {
                rs = this.getRs(q);
            } else {
                throw new Fallo("Se requiere que la consulta empiece con la palabra SELECT");
            }       
            rs.close();

        } catch (SQLException sqlex) {
            throw new Fallo(sqlex.toString());
        } catch (Exception e) {
            throw new Fallo(e.toString());

        } finally {
            cierraConexion();
        }

        return "Sintáxis válida";
    
    }

        
}
