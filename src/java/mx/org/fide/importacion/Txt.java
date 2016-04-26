/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.importacion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.org.fide.modelo.Conexion;
import mx.org.fide.modelo.Consulta;
import mx.org.fide.modelo.Fallo;
import mx.org.fide.modelo.Usuario;

/**
 *
 * @author daniel.martinez
 */
public class Txt {
    private FileReader txt;
    private Integer renglones =0;
    private Integer registroActual;
    
    public Txt(String txt) throws FileNotFoundException {
        this.txt = new FileReader(txt);
    }
    
    public Integer getRenglones() {
        String line;
        BufferedReader reader = null;
        this.renglones = 0;
        try { 
            reader = new BufferedReader(this.txt);
            while ((line = reader.readLine()) != null) {
                this.renglones++;
            }
        } catch (Exception e) {
             // do something
        }
        
        return this.renglones;
    }

    public Integer getRegistroActual() {
        return registroActual;  
    }

    public void setRegistroActual(Integer registroActual) {
        this.registroActual = registroActual;
    }
    
    public void validaImportacionAvancesDICONSA(Importacion importacion,DefinicionImportacion definicionImportacion, Usuario usuario) throws Fallo {
        Conexion oDb = new Conexion(usuario.getCx().getServer(), usuario.getCx().getDb(), usuario.getCx().getUser(), usuario.getCx().getPw(), usuario.getCx().getDbType());
        Consulta importacionDetalle = null;
        ResultSet rs;
        BufferedReader bufferReader = null;   
        String linea;
        String consultaResultante = "";
        String rpu = "";
        String fechaLectura = "";
        String fechaCarga = "";
        
        try {
            bufferReader = new BufferedReader(this.txt);
            importacionDetalle = new Consulta(714,"insert","0","",null,usuario);

            //Coloca el indicador en el el registro en donde se quedó
            if (this.registroActual>0) {
                Integer i=0;
                while ((linea = bufferReader.readLine()) != null)   {
                      i++;
                      if (i>=this.registroActual)
                          break;
                }
            }
            
            while ((linea = bufferReader.readLine()) != null)   {
                
                String [] aCampos = linea.split(definicionImportacion.getCaracterSeparador().equals("|")?"\\".concat(definicionImportacion.getCaracterSeparador()):definicionImportacion.getCaracterSeparador());
                
                for (DefinicionImportacionDetalle definicionCampo:definicionImportacion.getDefinicionImportacionDetalles() ) {
                    
                    importacionDetalle = new Consulta(714,"insert","0","",null, usuario);
                    importacionDetalle.getCampos().get("clave_importacion").setValor(importacion.getClaveImportacion().toString());
                    importacionDetalle.getCampos().get("clave_estatus").setValor("1"); //Por insertar
                    importacionDetalle.getCampos().get("renglon").setValor(this.registroActual.toString()); //Por insertar
                    
                    //Se valida que exista la columna
                    if (aCampos.length-1<definicionCampo.getColumna() && !definicionCampo.getOpcional()) {
                        importacionDetalle.getCampos().get("error").setValor("No se encontró la columna ".concat(definicionCampo.getColumna().toString()));
                        break;
                    }
                    
                    //Se valida que el tipo de datos sea correcto
                    if (definicionCampo.getTipoDato().equals("int") || definicionCampo.getTipoDato().equals("float") || definicionCampo.getTipoDato().equals("bit")) {
                        try {
                            Integer n = Integer.parseInt(aCampos[definicionCampo.getColumna()]);
                        } catch(Exception ex) {
                            importacionDetalle.getCampos().get("error").setValor("El campo <".concat(definicionCampo.getCampo().concat("> requiere un número")));
                            break;
                        }
                    } else if (definicionCampo.getTipoDato().equals("smalldatetime")) {
                        try {
                            Date d = new SimpleDateFormat("dd/MM/yyyy").parse(aCampos[definicionCampo.getColumna()]);
                        } catch(Exception ex) {
                            importacionDetalle.getCampos().get("error").setValor("El campo <".concat(definicionCampo.getCampo().concat("> requiere una fecha")));
                            break;
                        }
                    }
                    
                    //Se valida la obligatoriedad
                    if (!definicionCampo.getOpcional() && aCampos[definicionCampo.getColumna()].equals("")) {
                       importacionDetalle.getCampos().get("error").setValor("El campo <".concat(definicionCampo.getCampo().concat("> es obligatorio"))); 
                       break;
                    }
                        
                    //Se valida la existencia del campo
                    if (definicionCampo.getCampo().equals("fide_beneficiario.rpu")) {
                        rs = oDb.getRs("SELECT clave_estatus FROM fide_beneficiario WHERE rpu='".concat(aCampos[definicionCampo.getColumna()].replace("'","''")).concat("'"));
                        
                        if (!rs.next()) {
                           importacionDetalle.getCampos().get("error").setValor("El RPU ".concat(aCampos[definicionCampo.getColumna()]).concat(" no se encuentra en el padrón de beneficiarios")); 
                           break;            
                        } else {
                            if (rs.getInt("clave_estatus")==2) {
                                importacionDetalle.getCampos().get("error").setValor("Ya se entregaron lámparas al RPU ".concat(aCampos[definicionCampo.getColumna()]).concat(" no se encuentra en el padrón de beneficiarios"));                                 
                                break;
                            } else {
                                rpu = aCampos[definicionCampo.getColumna()];
                        }
                    }
                    }
                    
                    if (definicionCampo.getCampo().equals("fide_beneficiario.fecha_lectura")) {
                        fechaLectura = aCampos[definicionCampo.getColumna()];
                    }
                    
                    if (definicionCampo.getCampo().equals("fide_beneficiario.fecha_carga")) {
                        fechaCarga = aCampos[definicionCampo.getColumna()];
                    }
                      
                }
                
                if (importacionDetalle.getCampos().get("error").getValor().equals("")) {
                    importacionDetalle.getCampos().get("consulta_resultante").setValor("UPDATE fide_beneficiario SET fecha_lectura='".concat(fechaLectura).concat("',fecha_carga='").concat(fechaCarga).concat(" WHERE rpu='").concat(rpu).concat("'"));
                }    
                
                importacionDetalle.insert(true);
                this.registroActual++;
                importacion.setRegistrosProcesados(registroActual);
                //Guarda el avance
                oDb.execute("UPDATE fide_importacion SET clave_estatus=2, registros_procesados=".concat(this.registroActual.toString()).concat(" WHERE clave_importacion=").concat(importacion.getClaveImportacion().toString()));
                
            }
            
            //Cambia el estatus de la importación a concluida
            oDb.execute("UPDATE fide_importacion SET clave_estatus=3 WHERE clave_importacion=".concat(importacion.getClaveImportacion().toString()));
            
        } catch (Exception e) {
            throw new Fallo(e.getMessage());
        } finally {
            try {
                //Close the buffer reader
                bufferReader.close();
                this.txt.close();
            } catch (IOException ex) {
                Logger.getLogger(Txt.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
    
    }
}
