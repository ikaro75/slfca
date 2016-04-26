package mx.org.fide.cfe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Trama {
    
    private String pageURL = "http://10.7.7.12/WebServices/Recibos/TraeRecibosFIDE/TreRecibo.asmx/ConsultarTrama?rpu=";
    private String rpu;
    private StringBuilder content = new StringBuilder("");
    private String error;  
    
    public Trama(String rpu) {
        StringBuilder content = new StringBuilder("");
        this.rpu = rpu;
        String pageURL = this.pageURL.concat(rpu);
        
        try {
            URL page;
            if (pageURL.startsWith("https://")) {
                TrustManager[] trustAllCerts = { 
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
                };
                try {
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                } catch (Exception e) {
                    this.error = e.getMessage();
                }
                pageURL = pageURL.replace("https://", "");
                page = new URL("https", pageURL, 443, "/");
            } else {
                page = new URL(pageURL);
            }
            URLConnection dataConn = page.openConnection();
            boolean connectionOK = dataConn.getAllowUserInteraction();
            if ((!connectionOK) && (!pageURL.startsWith("http://"))) {
                pageURL = "https://" + pageURL;
                page = new URL(pageURL);
                dataConn = page.openConnection();
            }
            dataConn.addRequestProperty("User-Agent", "Mozilla/4.76");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(dataConn.getInputStream()));
            String inputLine;
//            ReadWebpage rw;
            while ((inputLine = in.readLine()) != null) {
                if (!inputLine.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?>"))
                    this.content.append(inputLine);
            }
            
            if (this.content.equals(this.rpu)) 
                throw new Error("No se encontró el beneficiario indicado");
           
        } catch (StringIndexOutOfBoundsException e) {
            this.error = "Error 104 - Los datos del SICOM no están correctamente formados";
        } catch (NullPointerException e) {
            this.error = "Error 105 - Los datos del SICOM no están correctamente formados";
        } catch (UnknownHostException e) {
            this.error = "Error 107 - Verifique su conexión a internet | la URL proporcionada par SICOM no es valida";
        } catch (MalformedURLException e) {
            this.error = "Error 106 - La URL de SICOM no es válida";
        } catch (SocketException e) {
            this.error = "Error 108 - Error de conexión a SICOM";
        } catch (Exception e) {
            this.error = ("Error 911 - " + e.toString());
            e.printStackTrace();
        }
        
        this.content = new StringBuilder(this.content.toString().replaceAll("<string xmlns=\"http://recibos.cfe.gob.mx/webservices/wsTraeRecibo/\">", "").replaceAll("</string>", ""));
        
    }

    public String getRpu() {
        return rpu;
    }

    public void setRpu(String rpu) {
        this.rpu = rpu;
    }
        
    public String getTipoFacturacion() {
        return this.content.toString().substring(21, 23);
    }
    
    public String getNombreBeneficiario() {
        return this.content.toString().substring(41, 71);
    }
    
    public String getDireccion() {
        return this.content.toString().substring(71,101);
    }
    
    public String getZona() {
        return this.content.toString().substring(28,30);
    }
    
    public String getPoblacion() {
        return this.content.toString().substring(101,121);
    }
    
    public String getClaveEstado() {
       return this.content.toString().substring(126,127);
    }
    
    public String getClaveMunicipio() {
        return this.content.toString().substring(127,130);
     }
    
    public String getTarifa() {
        return this.content.toString().substring(130,132);
    }
    
    public String getDAC() {
        return this.content.toString().substring(6961,6962);
    }
    
    public String getEstatus() {
        return this.content.toString().substring(1,3);
    }
    
    public String getCuenta() {
        return this.content.toString().substring(24,41);
    }
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
      public static void main(String[] args) {
       
        try {
            String rpus="587950803691,587950803704,588910900096,822060105031,822061205071,761100701678,761140201961,761140201970,761060603988,761060604011";
            
            for (int i=0; i<rpus.split(",").length;i++) {
                Trama t = new Trama(rpus.split(",")[i]);
                System.out.print("\nRPU : ".concat(t.getRpu()).concat("\n"));
                System.out.print("Nombre : ".concat(t.getNombreBeneficiario()).concat("\n"));
                System.out.print("Clave Estado : ".concat(t.getClaveEstado()).concat("\n"));
                System.out.print("Clave Municipio : ".concat(t.getClaveMunicipio()).concat("\n"));
                System.out.print("Direccion : ".concat(t.getDireccion()).concat("\n"));
                System.out.print("Poblacion : ".concat(t.getPoblacion()).concat("\n"));
                System.out.print("Tipo de facturacion : ".concat(t.getTipoFacturacion()).concat("\n"));
                System.out.print("Estatus : ".concat(t.getEstatus()).concat("\n"));                
            }
           
            
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            Logger.getLogger(Trama.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}