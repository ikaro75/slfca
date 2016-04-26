/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;
import java.util.StringTokenizer;
/**
 *
 * @author Daniel
 */
public class FromElement {
    String table;
    String alias;

    public FromElement(String element) throws SQLParserException {
        boolean aliased=false;
        String token;
        StringTokenizer tokens = new StringTokenizer(element);
        int i=1;
        do {
            token=tokens.nextToken();
            if (i==1) {
                this.table=token;
            } 
            else if (i==2 && !token.toLowerCase().equals("as")) {
                this.alias=token;
            }
            else if (i==2 && token.toLowerCase().equals("as")) {
                aliased=true;
            }
            else if (i==3 && aliased) {
                this.alias=token;
            } 
            
            if (i==3 && !aliased && this.alias==null) {
                throw new SQLParserException("Alias expected");
            }
            
            if (i==4) {
                throw new SQLParserException(", expected");
            }
            
            i++;
        } while(tokens.hasMoreTokens());

    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }


    
    
}
