package mx.org.fide.importacion;

import java.io.BufferedReader;
import java.io.File;
import java.sql.ResultSet;
import java.util.Iterator;
import mx.org.fide.cfe.ValidacionBeneficiario;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author daniel.martinez
 */
public class Excel {

    private File file;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private String registerError = "";
    private Integer renglones = 0;
    private Integer registroActual;

    public Excel(String archivo) throws Fallo {
        try {
            this.file = new File(archivo);

            //Create Workbook instance holding reference to .xlsx file
            this.workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            this.sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        }
    }

    public Integer getRenglones() {
        return renglones;
    }

    public void setRenglones(Integer renglones) {
        this.renglones = renglones;
    }

    public Integer getRegistroActual() {
        return registroActual;
    }

    public void setRegistroActual(Integer registroActual) {
        this.registroActual = registroActual;
    }

    public void importaBeneficiariosPadronAmpliado(Importacion importacion, DefinicionImportacion definicionImportacion, Usuario usuario) throws Fallo{
        //Iterate through each rows one by one
        Conexion oDb = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
        Consulta importacionDetalle = null;
        ResultSet rs;
        BufferedReader bufferReader = null;
        String linea;
        String consultaResultante = "";
        String rpu = "";
        String idControl = "";
        String nombre = "";
        ValidacionBeneficiario validacionBeneficiario = new ValidacionBeneficiario();
        try {
            Integer i = 0;
            Iterator<Row> rowIterator = this.sheet.iterator();
            //Hace la comparación de las columnas segun el modelo de importación
            this.registroActual = 1;
            importacionDetalle = new Consulta(714, "insert", "0", "", null, usuario);
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                
                if (this.registroActual==1) {
                    this.registroActual++;
                    continue;
                }
                if (this.registroActual==138) {
                    System.out.println("Hold on");
                }    
                for (DefinicionImportacionDetalle definicionCampo : definicionImportacion.getDefinicionImportacionDetalles()) {
                    Cell cell = row.getCell(definicionCampo.getColumna()-1);
                    importacionDetalle.setPk("");
                    importacionDetalle.getCampos().get("mensaje_error").setValor("");
                    importacionDetalle.getCampos().get("consulta_resultante").setValor("");
                    importacionDetalle.getCampos().get("clave_importacion").setValor(importacion.getClaveImportacion().toString());
                    importacionDetalle.getCampos().get("renglon").setValor(this.registroActual.toString()); //Por insertar
                    importacionDetalle.getCampos().get("clave_estatus").setValor("0");
                    //Se valida la obligatoriedad
                    if (!definicionCampo.getOpcional() && cell == null) {
                        importacionDetalle.getCampos().get("mensaje_error").setValor("El campo <".concat(definicionCampo.getCampo().concat("> es obligatorio")));
                        break;
                    }

                    //Se valida la obligatoriedad
                    if (!definicionCampo.getOpcional() && cell.getCellType()==Cell.CELL_TYPE_BLANK) {
                        importacionDetalle.getCampos().get("mensaje_error").setValor("El campo <".concat(definicionCampo.getCampo().concat("> es obligatorio")));
                        break;
                    }

                    //Se valida la existencia del campo
                    if (definicionCampo.getCampo().equals("fide_beneficiario.rpu")) {
                        //Valida el RPU en SICOM
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                rpu = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_STRING:
                                rpu = cell.getStringCellValue();
                                break;
                        }
                        
                    } else if (definicionCampo.getCampo().equals("fide_punto_entrega.id_control")) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                idControl = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_STRING:
                                idControl = cell.getStringCellValue();
                                break;
                        }
                    } else if (definicionCampo.getCampo().equals("fide_beneficiario.nombre") && cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                nombre = cell.getStringCellValue().toUpperCase();
                                break;
                            case Cell.CELL_TYPE_STRING:
                                nombre = cell.getStringCellValue().toUpperCase();
                                break;
                        }
                    }
                    
                    i++;
                }
                try {
                    importacionDetalle.getCampos().get("rpu").setValor(rpu);
                    importacionDetalle.getCampos().get("id_control").setValor(idControl);
                    importacionDetalle.getCampos().get("nombre").setValor(nombre);
                    validacionBeneficiario.valida(rpu, idControl, 3, usuario);
                    importacionDetalle.getCampos().get("clave_estatus").setValor(validacionBeneficiario.getClaveEstatusImportacionDetalle().toString());
                    importacionDetalle.getCampos().get("importado").setValor("0");
                             
                    if (importacionDetalle.getCampos().get("mensaje_error").getValor().equals("")) {

                        importacionDetalle.getCampos()
                                .get("consulta_resultante")
                                .setValor(new StringBuilder("IF NOT EXISTS(SELECT * FROM fide_beneficiario WHERE rpu='").append(validacionBeneficiario.getRpu()).append("' AND clave_tipo_padron=2) BEGIN INSERT INTO fide_beneficiario (rpu,nombre,clave_estado,clave_municipio,")
                                        .append("poblacion,direccion,clave_punto,")
                                        .append("clave_tipo_padron,clave_estado_entrega,clave_municipio_entrega,clave_localidad_entrega,")
                                        .append("fecha_entrega,fecha_registro,")
                                        .append("clave_estatus,clave_carpeta)")
                                        .append("VALUES ('")
                                        .append(validacionBeneficiario.getRpu()).append("','")
                                        .append(validacionBeneficiario.getNombre().replaceAll("'","''")).append("',")
                                        .append(validacionBeneficiario.getClaveEstado()).append(",")
                                        .append(validacionBeneficiario.getClaveMunicipio()).append(",'")
                                        .append(validacionBeneficiario.getPoblacion().replaceAll("'","''")).append("','")
                                        .append(validacionBeneficiario.getDireccion().replaceAll("'","''")).append("',")
                                        .append(validacionBeneficiario.getClavePunto()).append(",2,")
                                        .append(validacionBeneficiario.getClaveEstadoEntrega()).append(",")
                                        .append(validacionBeneficiario.getClaveMunicipioEntrega()).append(",")
                                        .append(validacionBeneficiario.getClaveLocalidadEntrega()).append(",'")
                                        .append(validacionBeneficiario.getFechaEntrega())
                                        .append("',GETDATE(),1, (SELECT clave_carpeta FROM fide_carpeta WHERE clave_punto=")
                                        .append(validacionBeneficiario.getClavePunto()).append(" AND clave_tipo_carpeta=2)) END")
                                        .toString());

                    }
                } catch (Exception ex) {
                    importacionDetalle.getCampos().get("rpu").setValor(rpu);
                    importacionDetalle.getCampos().get("id_control").setValor(idControl);
                    importacionDetalle.getCampos().get("nombre").setValor(validacionBeneficiario.getNombre()!=null?validacionBeneficiario.getNombre().replaceAll("'","''"):nombre);
                    importacionDetalle.getCampos().get("mensaje_error").setValor(ex.getMessage());
                    importacionDetalle.getCampos().get("clave_estatus").setValor(validacionBeneficiario.getClaveEstatusImportacionDetalle().toString());
                }
                
                importacionDetalle.insert(true);
                rpu="";
                idControl="";
                nombre="";
                this.registroActual++;
                importacion.setRegistrosProcesados(registroActual);
                
                //Guarda el avance
                oDb.execute("UPDATE fide_importacion SET clave_estatus=2, registros_por_procesar=registros_por_procesar-".concat(registroActual.toString()).concat(",registros_procesados=").concat(this.registroActual.toString()).concat(" WHERE clave_importacion=").concat(importacion.getClaveImportacion().toString()));
            }
            
            oDb.execute("UPDATE fide_importacion SET clave_estatus=3 WHERE clave_importacion=".concat(importacion.getClaveImportacion().toString()));
           
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            try {
              this.workbook.close();
            } catch(Exception ex) {
                
            }
        }
    }
}
