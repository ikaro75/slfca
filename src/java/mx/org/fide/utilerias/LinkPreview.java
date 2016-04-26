package mx.org.fide.utilerias;

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
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LinkPreview {
    
    public String title;
    private String body;
    public ArrayList<String> images = new ArrayList<String>();
    public String description;
    private int descFlag = 0;
    private String webPage;
    public String ogImage;
    private String error;
    
    public LinkPreview() {
        this.title = "Titulo de la p&aacutegina.";
        this.body = "Body de la p&aacutegina.";
        this.images.clear();
        this.error = "";
        this.description = "Descripci&oacuten de la p&aacutegina.";
        this.ogImage = "";
    }
    
    public String getWebPage(String pageURL) {
        if ((pageURL.contains("#")) || (pageURL.contains("!"))) {
            this.error = "Error Code : 101 - Removiendo simbolos de la URL.(like #,!,etc...)";
            return this.error;
        }
        if ((!pageURL.startsWith("http://")) && (!pageURL.startsWith("https://")))
            pageURL = "http://" + pageURL;
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
                    this.description = e.getMessage();
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
                this.webPage += inputLine;
                if ((this.descFlag != 1) && (this.webPage.toLowerCase().indexOf("name=\"description\"") > 1)) {
                    this.description = inputLine;
                    int metaStart = this.description.toLowerCase().indexOf("name=\"description\"");
                    int metaEnd = this.description.indexOf(">", metaStart);
                    this.description = this.description.substring(metaStart, metaEnd);
                    this.descFlag = 1;
                }
            }
            
            if (this.descFlag == 1) {
                int descStart = this.description.toLowerCase().indexOf("content=\"");
                if (descStart < 1) {
                    descStart = this.description.toLowerCase().indexOf("content=\"");
                }
                int DescEnd = this.description.indexOf("\"", descStart + 9);
                if (DescEnd - (descStart + 9) > 1)
                    this.description = this.description.substring(descStart + 9, DescEnd);
                else
                    this.description = pageURL;
            } else {
                this.description = pageURL;
            }
            
            int ogTempImageStart =this.webPage.toLowerCase().indexOf("property=\"og:image");
            
            if (ogTempImageStart > 1) {
                    int ogImageStart = this.webPage.toLowerCase().indexOf("content=\"",ogTempImageStart)+8;
                    int ogImageEnd = this.webPage.toLowerCase().indexOf(">",ogImageStart);
                    this.ogImage = this.webPage.substring(ogImageStart+1, ogImageEnd-1);
            } 
            
            int titleTempStart = this.webPage.toLowerCase().indexOf("<title");

            if (titleTempStart > 1) {
                int titleStart = this.webPage.toLowerCase().indexOf(">", titleTempStart);
                int titleEnd = this.webPage.toLowerCase().indexOf("</title>");
                
                
                this.title = this.webPage.substring(titleStart + 1, titleEnd);
                
                int BodyTempStart = this.webPage.toLowerCase().indexOf("<body");
                              
                int BodyStart = this.webPage.indexOf(">", BodyTempStart + 1);
                int BodyEnd = this.webPage.toLowerCase().indexOf("</body>");
               

                if ((BodyStart > 1) && (BodyEnd > 1)) {
                    this.body = this.webPage.substring(BodyStart + 6, BodyEnd);
                    if (this.body.toLowerCase().contains("<img")) {
                        int ImageIndex = 0;
                        int ImageCount = 0;
                        int ImageStart = 0;
                        int ImageEnd;
                        String imageTag = "";
                        String imageWidth = "";
                        
                        while (ImageIndex >= 0) {
                            ImageIndex = this.body.toLowerCase().indexOf("<img", ImageIndex + 1);
                            
                            if (ImageIndex==-1) continue;
                            
                            ImageStart = this.body.toLowerCase().indexOf("<img", ImageStart + 1);
                            ImageEnd = this.body.toLowerCase().indexOf(">", ImageStart + 1);
                            
                            imageTag = this.body.substring(ImageStart, ImageEnd + 1);
                            if (imageTag.toLowerCase().indexOf("width=")==-1) {
                                imageWidth="0";
                            } else {
                                imageWidth = imageTag.substring(imageTag.toLowerCase().indexOf("width=")+7,imageTag.indexOf("\"", imageTag.toLowerCase().indexOf("width=")+7));
                            }    
                            
                            if (Integer.parseInt(imageWidth) >= 50 && !this.images.contains(imageTag) ) {    
                                this.images.add(imageTag);
                                int srcStart = this.images.get(ImageCount).toLowerCase().indexOf("src=\"");
                                if (srcStart < 1) {
                                    srcStart = this.images.get(ImageCount).toLowerCase().indexOf("src='");
                                }
                                int srcEnd = this.images.get(ImageCount).toLowerCase().indexOf("\"", srcStart + 5);
                                if (srcEnd < 1) {
                                    srcEnd = this.images.get(ImageCount).toLowerCase().indexOf("'", srcStart + 5);
                                }
                                if (pageURL.endsWith("/")) {
                                    this.images.set(ImageCount,this.images.get(ImageCount).substring(srcStart + 5, srcEnd)); 
                                    
                                    if ((!this.images.get(ImageCount).startsWith("http")) && (!this.images.get(ImageCount).startsWith("https"))) {
                                        this.images.set(ImageCount,(pageURL + this.images.get(ImageCount))) ;
                                        this.images.set(ImageCount,this.images.get(ImageCount).replace("//", "/")) ;
                                    }
                                } else {
                                    this.images.set(ImageCount,this.images.get(ImageCount).substring(srcStart + 5, srcEnd));
                                    if (this.images.get(ImageCount).startsWith("//")) {
                                        this.images.set(ImageCount,("http:" + this.images.get(ImageCount)));
                                    }
                                    String TempDataURL = pageURL;
                                    if (!this.images.get(ImageCount).startsWith("http") && (!this.images.get(ImageCount).startsWith("https"))) {
                                        if ((pageURL.contains(".html")) || (pageURL.contains(".jsp")) || (pageURL.contains(".php")) || (pageURL.contains(".aspx"))) {
                                            int lastSlash = pageURL.lastIndexOf("/");
                                            TempDataURL = pageURL.substring(0, lastSlash);
                                        }
                                        if (this.images.get(ImageCount).startsWith("/")) {
                                            int UpDirectory = TempDataURL.lastIndexOf("/");
                                            TempDataURL = TempDataURL.substring(0, UpDirectory);
                                        } else {
                                            TempDataURL = TempDataURL + "/";
                                        }
                                        this.images.set(ImageCount,(TempDataURL + this.images.get((ImageCount))));
                                    }
                                }
                                
                                if (!this.images.get(ImageCount).startsWith("http://"))
                                    this.images.set(ImageCount,this.images.get(ImageCount).replace("http:/", "http://"));
                                else if (!this.images.get(ImageCount).startsWith("https://"))
                                    this.images.set(ImageCount, this.images.get(ImageCount).replace("https:/", "https://"));                                
                                    ImageCount++;
                                }
                        }   
                    }
                } else {
                    this.error = "Error Code : 102 - Page in not well formed";
                }
            } else {
                this.error = "Error Code : 103 - Page is not pure HTML";
                return this.error;
            }
        } catch (StringIndexOutOfBoundsException e) {
            this.error = "Error Code : 104 - Page in not well formed";
        } catch (NullPointerException e) {
            this.error = "Error Code : 105 - Page in not well formed";
        } catch (UnknownHostException e) {
            this.error = "Error Code : 107 - Not connected to Internet | Wrong URL";
        } catch (MalformedURLException e) {
            this.error = "Error Code : 106 - Wrong URL";
        } catch (SocketException e) {
            this.error = "Error Code : 108 - Connection Error";
        } catch (Exception e) {
            this.error = ("Error Code : 911 - " + e.toString());
            e.printStackTrace();
        }
        return this.error;
    }
}