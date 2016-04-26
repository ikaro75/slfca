/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;

/**
 *
 * @author Daniel
 */
public class HavingElement {
    String aggregateFunction;
    String column;
    String operator;
    String value;
    String conjuction;

    public HavingElement(String aggregateFunction, String column, String operator, String value) {
        this.aggregateFunction = aggregateFunction;
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    
    public String getAggregateFunction() {
        return aggregateFunction;
    }

    public void setAggregateFunction(String aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
   
    public String getConjuction() {
        return conjuction;
    }

    public void setConjuction(String conjuction) {
        this.conjuction = conjuction;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    
}
