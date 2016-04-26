/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;

/**
 *
 * @author Daniel
 */
public class OrderByElement {
    String element;

    public OrderByElement(String element) {
        this.element = element;
    }
   
    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }
    
}
