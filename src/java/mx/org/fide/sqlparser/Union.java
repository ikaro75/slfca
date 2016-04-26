/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Union {

    ArrayList <Select> selects = new ArrayList <Select>();

    public ArrayList<Select> getSelects() {
        return selects;
    }

    public void setSelects(ArrayList<Select> selects) {
        this.selects = selects;
    }

    public void setSelect(String select) {
        try {
            Select s = new Select (select);
            this.selects.add(s);            
        } catch(SQLParserException e) {
            e.printStackTrace();
        }
    }

    
    
}
