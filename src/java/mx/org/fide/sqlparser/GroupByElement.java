/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;

/**
 *
 * @author Daniel
 */
public class GroupByElement {
    String element ;

    public GroupByElement(String groupByToParse) {
        this.element = groupByToParse;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    
}
