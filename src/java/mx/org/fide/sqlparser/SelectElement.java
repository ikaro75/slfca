/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;

/**
 *
 * @author Daniel
 */
public class SelectElement {
   String element;

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public SelectElement(String element) {
        this.element = element;
    }
   
   
}
