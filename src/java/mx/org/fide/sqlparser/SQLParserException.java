/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;

/**
 *
 * @author Daniel
 */
public class SQLParserException extends Exception {

    SQLParserException(){}
    
    SQLParserException(String message){
        super(message);
    }
}
