/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.fide.sqlparser;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Daniel
 */
public class SQLParser {
    public boolean isUnion;
    String query;
    Union union;
    Select select;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }
    
    public SQLParser(String query) {
        this.isUnion=false;
        this.query = query;
        String token;
        StringBuilder completeElement=new StringBuilder("");
        StringTokenizer tokens = new StringTokenizer(query);
        Union u=null;
        
        do{
            token = tokens.nextToken();   
            if (token.toLowerCase().equals("union") ) {
                u = new Union();
                u.setSelect(completeElement.toString());
                completeElement=new StringBuilder("");
                this.isUnion=true;
            }
            else {
                completeElement.append(" ").append(token);
            }
                
        }while(tokens.hasMoreTokens());
        
        if (u==null) {
            try {
                select = new Select(completeElement.toString());
            } catch(SQLParserException e) {
                e.printStackTrace();
            }
        }
            
        
    }

    @Override
    public String toString() {
        StringBuilder s= new StringBuilder();
        int i=1;
        if (this.isUnion) {
            for (Select select: this.union.getSelects()) {
                s.append(select.command.toUpperCase()).append(" ");
                i=1;
                for (SelectElement se: select.getFields()) {
                    s.append(se.getElement());
                    if (i<select.getFields().size()) {
                        s.append(",");
                    }
                    i++;        
                }
                
                s.append(" FROM ");
                i=1;
                for (FromElement fe: select.getFrom()) {
                    s.append(fe.getTable());
                    
                    if (!fe.getAlias().equals(""))  {
                        s.append(" ").append(fe.getAlias());
                    }
                    
                    if (i<select.getFrom().size()) {
                        s.append(",");
                    }
                    i++;        
                }  
                
                if (!select.where.isEmpty()) {
                    s.append( " WHERE ");
                    i=1;
                    for (WhereCondition w: select.getWhere()) {
                        s.append(w.getColumn()).append(w.getOperator()).append(w.getValue());

                        if (i<select.where.size() && w.getConjuction()!=null) {
                            s.append(" ").append(w.conjuction);
                        }
                        i++;        
                    }                       
                }

                if (!select.groupby.isEmpty()) {
                    s.append( " GROUP BY ");
                    i=1;
                    for (GroupByElement gby: select.getGroupby()) {
                        s.append(gby.getElement());

                        if (i<select.groupby.size()) {
                            s.append(",");
                        }
                        i++;        
                    }                       
                }

                if (!select.having.isEmpty()) {
                    s.append( " HAVING ");
                    i=1;
                    for (HavingElement h: select.getHaving()) {
                        s.append(h.getAggregateFunction()).append("(").append(h.getColumn()).append(")").append(h.getConjuction()).append(h.getValue());

                        if (i<select.having.size() && h.getConjuction()!=null) {
                            s.append(" ").append(h.conjuction);
                        }
                        i++;        
                    }                       
                }
               if (!select.orderby.isEmpty()) {
                    s.append( " ORDER BY ");
                    i=1;
                    for (OrderByElement obe: select.getOrderby()) {
                        s.append(obe.getElement());

                        if (i<select.orderby.size()) {
                            s.append(",");
                        }
                        i++;        
                    }                       
                }
            } 
        } else {
            Select select =this.getSelect();
            s.append(select.command.toUpperCase()).append(" ");
            i=1;
            
            for (SelectElement se: select.getFields()) {
                    s.append(se.getElement());
                    if (i<select.getFields().size()) {
                        s.append(",");
                    }
                    i++;        
            }
                
            s.append(" FROM ");
            i=1;
            for (FromElement fe: select.getFrom()) {
                s.append(fe.getTable());

                if (fe.getAlias()!=null)  {
                    s.append(" ").append(fe.getAlias());
                }

                if (i<select.getFrom().size()) {
                    s.append(",");
                }
                i++;        
            }  

            if (!select.where.isEmpty()) {
                s.append( " WHERE ");
                i=1;
                for (WhereCondition w: select.getWhere()) {
                    s.append(w.getColumn()).append(w.getOperator()).append(w.getValue());

                    if (i<select.getWhere().size() && w.getConjuction()!=null) {
                        s.append(" ").append(w.conjuction);
                    }
                    i++;        
                }                       
            }

            if (!select.groupby.isEmpty()) {
                s.append( " GROUP BY ");
                i=1;
                for (GroupByElement gby: select.getGroupby()) {
                    s.append(gby.getElement());

                    if (i<select.getGroupby().size()) {
                        s.append(",");
                    }
                    i++;        
                }                       
            }

            if (!select.having.isEmpty()) {
                s.append( " HAVING ");
                i=1;
                for (HavingElement h: select.getHaving()) {
                    s.append(h.getAggregateFunction()).append("(").append(h.getColumn()).append(")").append(h.getConjuction()).append(h.getValue());

                    if (i<select.getHaving().size() && h.getConjuction()!=null) {
                        s.append(" ").append(h.conjuction);
                    }
                    i++;        
                }                       
            }
           if (!select.orderby.isEmpty()) {
                s.append( " ORDER BY ");
                i=1;
                for (OrderByElement obe: select.getOrderby()) {
                    s.append(obe.getElement());

                    if (i<select.getOrderby().size()) {
                        s.append(",");
                    }
                    i++;        
                }                       
            }            
        }
        
        return s.toString();
    }
    


public static void main(String args[]) {
     SQLParser p;
     p= new SQLParser("select clave_cliente from proyecto");
     System.out.println(" *** Ejercicio 1 ***");
     System.out.println(p.select.toString());
     System.out.println(p.toString());
     p= new SQLParser("select clave_cliente from proyecto where clave_proyecto=1");
     System.out.println(" *** Ejercicio 2 ***");     
     System.out.println(p.select.toString());
     System.out.println(p.toString());
     System.out.println(" *** Ejercicio 3 ***");
     p= new SQLParser("select app.clave_aplicacion, app.aplicacion, ea.estatus as clave_estatus from aplicacion app, estatus_aplicacion ea where app.clave_estatus = ea.clave_estatus");
     System.out.println(p.select.toString());
     System.out.println(p.toString());
}

}