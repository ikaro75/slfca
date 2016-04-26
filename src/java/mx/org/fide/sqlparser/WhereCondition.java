/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;

/**
 *
 * @author Daniel
 */
public class WhereCondition {
    String column;
    String operator;
    String value;
    String conjuction;

    public WhereCondition(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
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

    public String getConjuction() {
        return conjuction;
    }

    public void setConjuction(String conjuction) {
        this.conjuction = conjuction;
    }
    
    
}
