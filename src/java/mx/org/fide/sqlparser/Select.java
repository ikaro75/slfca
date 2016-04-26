package mx.org.fide.sqlparser;
import java.util.ArrayList;
import java.util.StringTokenizer;
import mx.org.fide.utilerias.Utilerias;

/**
 *
 * @author Daniel
 */
public final class Select {
    String query;
    String command;
    
    boolean hasTop=false;
    Integer topLimit=0;
    
    ArrayList <SelectElement> fields = new ArrayList <SelectElement>();
    ArrayList <FromElement> from = new ArrayList <FromElement>();
    ArrayList <WhereCondition> where  = new ArrayList <WhereCondition>();
    ArrayList <GroupByElement> groupby = new ArrayList <GroupByElement>();
    ArrayList <HavingElement> having = new ArrayList <HavingElement>();
    ArrayList <OrderByElement> orderby = new ArrayList <OrderByElement>();
    
    public Select(String query) throws SQLParserException {
        this.query= query;
        String token="";
        StringBuilder fieldsToParse=new StringBuilder("");
        StringBuilder tablesToParse=new StringBuilder("");
        StringBuilder conditionsToParse=new StringBuilder("");
        StringBuilder groupToParse=new StringBuilder("");
        StringBuilder havingToParse = new StringBuilder("");
        StringBuilder orderToParse=new StringBuilder("");
        
        if (query==null) {
            throw new SQLParserException("Query is null");
        }
        
        if (query.equals("")) {
            throw new SQLParserException("Query is empty");  
        }
        
        StringTokenizer tokens = new StringTokenizer(query);    
        
        //hace un conteo de los froms y  los wheres
        int tokenOfLastFrom=0;
        int tokenOfLastWhere=0;
        int tokenOfLastOrderBy=0;
        int tokenOfLastGroupBy=0;
        int tokenOfHaving=0;
        int tokenCount=1;
        
        while(tokens.hasMoreTokens())
        {
            token = tokens.nextToken();
            
            if (tokenCount==1 && !token.toLowerCase().equals("select")) 
             throw new SQLParserException("SELECT expected"); 
            
            
            if (tokenCount==2 && token.toLowerCase().equals("top")) 
                 this.hasTop=true;
            
            
            if (tokenCount==3 && this.hasTop) {
                try {
                    this.topLimit=Integer.parseInt(token);
                } catch(Exception e) {
                    throw new SQLParserException("Numeric value for limit of TOP expected"); 
                }    
            }
    
            if (token.toLowerCase().equals("from")) {
                tokenOfLastFrom=tokenCount;
            }
            
            else if (token.toLowerCase().equals("where")) {
                tokenOfLastWhere=tokenCount;
            }
            else if (token.toLowerCase().equals("group") ) {
                if (tokens.hasMoreTokens()) {
                    token = tokens.nextToken();
                    tokenCount++;
                    if (token.toLowerCase().equals("by")) {
                        tokenOfLastGroupBy=tokenCount;
                    }                    
                }
            }
            else if (token.toLowerCase().equals("having") ) {
                tokenOfHaving=tokenCount;
            } 
            else if (token.toLowerCase().equals("order") ) {
                if (tokens.hasMoreTokens()) {
                    token = tokens.nextToken();
                    tokenCount++;
                    if (token.toLowerCase().equals("by")) {
                        tokenOfLastOrderBy=tokenCount;
                    }                    
                }
            }
            
            tokenCount++;
            
        } 
        
        if (tokenOfLastFrom==0)  {
            throw new SQLParserException("FROM expected"); 
        }
        
        tokens = new StringTokenizer(query);
        
        tokenCount=1;
        
        while(tokens.hasMoreTokens()){
            token = tokens.nextToken();
            if (tokenCount==1)
                this.command=token;
            else if (tokenCount==tokenOfLastFrom || 
                     tokenCount==tokenOfLastWhere || 
                     tokenCount==tokenOfLastGroupBy || 
                     tokenCount==tokenOfLastGroupBy+1 ||
                     tokenCount==tokenOfHaving ||
                     tokenCount==tokenOfLastOrderBy || 
                     tokenCount==tokenOfLastOrderBy+1                    
                    ) {
                tokenCount++;
                continue;
            }    
            else if (tokenCount<tokenOfLastFrom) {
                fieldsToParse.append(" ").append(token);
            }
            else if (tokenCount>tokenOfLastFrom && !this.query.toLowerCase().contains("where")) { 
                tablesToParse.append(" ").append(token);
            }
            else if (tokenCount>tokenOfLastFrom && tokenCount<tokenOfLastWhere && this.query.toLowerCase().contains("where")) {
                tablesToParse.append(" ").append(token);
            }
            else if (tokenCount>tokenOfLastWhere && !this.query.toLowerCase().contains("order by")) { 
                conditionsToParse.append(" ").append(token);
            }    
            else if (tokenCount>tokenOfLastWhere && tokenCount< tokenOfLastOrderBy  && this.query.toLowerCase().contains("order by")) { 
                conditionsToParse.append(" ").append(token);
            }
            else if (tokenCount> tokenOfLastGroupBy && tokenCount< tokenOfHaving) { 
                groupToParse.append(" ").append(token);
            }
            else if (tokenCount> tokenOfHaving && tokenCount < tokenOfLastOrderBy) { 
                havingToParse.append(" ").append(token);
            }
            else if (tokenCount>tokenOfLastOrderBy) { 
                orderToParse.append(" ").append(token);
            }
            tokenCount++;
        } 
        
        //Separa en token los elementos internos de cada parte por parsear

        setFields(fieldsToParse.toString());
        setFrom(tablesToParse.toString());
        setWhere(conditionsToParse.toString());
        setGroupBy(groupToParse.toString());
        setHaving(havingToParse.toString());
        
    }

    public ArrayList<SelectElement> getFields() {
        return fields;
    }

    public void setFields(ArrayList<SelectElement> fields) {
        this.fields = fields;
    }

    private void setFields(String fieldsToParse) {
        StringBuilder token = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(fieldsToParse,",");    
        do{
            token = new StringBuilder(tokens.nextToken());
            
            //Si se trata de una subconsulta
            if (token.toString().contains("(")) {
               do{ 
                   token.append(tokens.nextToken());
                   
                   if (token.toString().contains(")")) break;
                           
                 }while(tokens.hasMoreTokens());
            }
            
            fields.add(new SelectElement(token.toString()));
        }while(tokens.hasMoreTokens());

    }

    public ArrayList<FromElement> getFrom() {
        return from;
    }

    public void setFrom(ArrayList<FromElement> from) {
        this.from = from;
    }
    
    public final void setFrom(String tablesToParse) {
        StringBuilder token = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(tablesToParse,",");    
        do{
            token = new StringBuilder(tokens.nextToken());
            try {
                from.add(new FromElement(token.toString()));
            } catch(SQLParserException e) {
                e.printStackTrace();
            }
        }while(tokens.hasMoreTokens());
    }

    public ArrayList<WhereCondition> getWhere() {
        return where;
    }

    public void setWhere(ArrayList<WhereCondition> where) {
        this.where = where;
    }

    public void setWhere(String conditionsToParse) throws SQLParserException {
        StringBuilder token;
        StringBuilder pendingCondition = new StringBuilder("");
        WhereCondition condition;
        
        StringTokenizer conditionTokens;
        StringTokenizer tokens = new StringTokenizer(conditionsToParse);
        ArrayList<String> operators = new ArrayList<String>();
        
        operators.add("=");
        operators.add("<>");
        operators.add(">");
        operators.add("<");
        operators.add(">=");
        operators.add("<=");
        operators.add("between");
        operators.add("like");
        operators.add("in");
        
        
        while(tokens.hasMoreTokens()){
            token = new StringBuilder(tokens.nextToken());
            
            if (token.toString().toLowerCase().equals("and") || token.toString().toLowerCase().equals("or")) {
                
                //Verifica el operador de la condición
                for (int i=0;i<operators.size();i++ ) {
                    if (pendingCondition.toString().toLowerCase().contains(operators.get(i) )) {
                        conditionTokens =new StringTokenizer(pendingCondition.toString(),operators.get(i));
                        condition = new WhereCondition(conditionTokens.nextToken());
                        condition.setOperator(operators.get(i));
                        if (conditionTokens.hasMoreTokens()) {
                            condition.setValue(conditionTokens.nextToken());
                        }
                        else
                           throw new SQLParserException("Operator expected"); 
                        
                        condition.setConjuction(token.toString());
                        where.add(condition);
                        pendingCondition = new StringBuilder(""); 
                        break;
                    }     
                }
           
            }
            else 
                pendingCondition.append(token).append(" ");

        }
        
        if (pendingCondition.length()>0 ) {
            for (int i=0;i<operators.size();i++ ) {
                if (pendingCondition.toString().toLowerCase().contains(operators.get(i) )) {
                    conditionTokens =new StringTokenizer(pendingCondition.toString(),operators.get(i));
                    condition = new WhereCondition(conditionTokens.nextToken());
                    condition.setOperator(operators.get(i));
                    if (conditionTokens.hasMoreTokens()) {
                        condition.setValue(conditionTokens.nextToken());
                    }

                    where.add(condition);
                    pendingCondition = new StringBuilder(""); 
                    break;
                }     
            }            
        }
        
    }

    private void setGroupBy(String groupByToParse) {
        String token = "";
        StringTokenizer tokens = new StringTokenizer(groupByToParse,",");    
        while(tokens.hasMoreTokens()){
            token = tokens.nextToken();
            groupby.add(new GroupByElement(token));
        }

    }

    public void setHaving(String havingToParse) throws SQLParserException {
        StringBuilder token;
        StringBuilder pendingCondition = new StringBuilder("");
        String tokenAggregateFunction;
        String aggregateFunction;
        String column;
        String value;
        HavingElement condition;
        
        StringTokenizer conditionTokens;
        StringTokenizer tokens = new StringTokenizer(havingToParse);
        ArrayList<String> operators = new ArrayList<String>();
        
        operators.add("=");
        operators.add("<>");
        operators.add(">");
        operators.add("<");
        operators.add(">=");
        operators.add("<=");
        operators.add("between");
        operators.add("like");
        operators.add("in");
        
        
        while(tokens.hasMoreTokens()){
            token = new StringBuilder(tokens.nextToken());
            
            if (token.toString().toLowerCase().equals("and") || token.toString().toLowerCase().equals("or")) {
                
                //Verifica el operador de la condición
                for (int i=0;i<operators.size();i++ ) {
                    if (pendingCondition.toString().toLowerCase().contains(operators.get(i) )) {
                        conditionTokens =new StringTokenizer(pendingCondition.toString(),operators.get(i));
                        
                        //Separa la función agregada de la columna
                        tokenAggregateFunction= conditionTokens.nextToken();
                        
                        if (tokenAggregateFunction.toString().contains("(")) {
                            aggregateFunction=tokenAggregateFunction.toString().substring(0, tokenAggregateFunction.indexOf("(")-1);
                            column = tokenAggregateFunction.toString().substring(tokenAggregateFunction.indexOf("(")+1,tokenAggregateFunction.length()-1);
                        }
                        else {
                            throw new SQLParserException("Aggregate function expected"); 
                        }
                            
                        if (conditionTokens.hasMoreTokens()) 
                            value =conditionTokens.nextToken();
                        else
                            throw new SQLParserException("Value expected"); 
                                
                        condition = new HavingElement(aggregateFunction,column, operators.get(i),value);
                        
                        
                        condition.setConjuction(token.toString());
                        having.add(condition);
                        pendingCondition = new StringBuilder(""); 
                        break;
                    }     
                }
           
            }
            else 
                pendingCondition.append(token);

        }
        
        if (pendingCondition.length()>0 ) {
            for (int i=0;i<operators.size();i++ ) {
                    if (pendingCondition.toString().toLowerCase().contains(operators.get(i) )) {
                        conditionTokens =new StringTokenizer(pendingCondition.toString(),operators.get(i));
                        
                        //Separa la función agregada de la columna
                        tokenAggregateFunction= conditionTokens.nextToken();
                        
                        if (tokenAggregateFunction.toString().contains("(")) {
                            aggregateFunction=tokenAggregateFunction.toString().substring(0, tokenAggregateFunction.indexOf("(")-1);
                            column = tokenAggregateFunction.toString().substring(tokenAggregateFunction.indexOf("(")+1,tokenAggregateFunction.length()-1);
                        }
                        else {
                            throw new SQLParserException("Aggregate function expected"); 
                        }
                            
                        if (conditionTokens.hasMoreTokens()) 
                            value =conditionTokens.nextToken();
                        else
                            throw new SQLParserException("Value expected"); 
                                
                        condition = new HavingElement(aggregateFunction,column, operators.get(i),value);
                        
                        having.add(condition);
                        pendingCondition = new StringBuilder(""); 
                        break;
                    }     
                }       
        }
        
    }

    public ArrayList<HavingElement> getHaving() {
        return having;
    }
    
    public ArrayList<OrderByElement> getOrderby() {
        return orderby;
    }

    public void setOrderby(ArrayList<OrderByElement> orderby) {
        this.orderby = orderby;
    }

    public void setOrderby(String orderByToParse) {
        StringBuilder token = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(orderByToParse,",");    
        while(tokens.hasMoreTokens()){
            token = new StringBuilder(tokens.nextToken());  
            orderby.add(new OrderByElement(token.toString()));
        }      
    }  
   
    public void replaceOrderBy(String newOrderBy) {
        orderby.clear();
        this.setOrderby(newOrderBy);
    }

    public ArrayList<GroupByElement> getGroupby() {
        return groupby;
    }

    public void setGroupby(ArrayList<GroupByElement> groupby) {
        this.groupby = groupby;
    }
    
    public String reassemble () {
        StringBuilder s = new  StringBuilder();
        s.append(this.command);
        for (int i=0; i< this.fields.size();i++) {
            s.append(this.fields.get(i));
            
            if (i<this.fields.size()-1) 
                s.append(",");
       }
            
       s.append(" FROM ");

       for (int i=0; i< this.from.size();i++) {
            s.append(this.from.get(i));
            
            if (i<this.from.size()-1) 
                s.append(",");
       }
       
       if (this.where.size()>0) {
           s.append(" WHERE ");

           for (int i=0; i< this.where.size();i++) {
                s.append(this.where.get(i).column).append(this.where.get(i).operator).append(this.where.get(i).value);

                if (this.where.get(i).conjuction!=null) 
                    s.append(this.where.get(i).conjuction);
           }
       }

       if (this.orderby.size()>0) {
           s.append(" ORDER BY ");

           for (int i=0; i< this.orderby.size();i++) {
                s.append(this.orderby.get(i));

                if (i<this.orderby.size()-1) 
                    s.append(",");
           }
       }
       
       if (this.groupby.size()>0) {
           s.append(" GROUP BY ");

           for (int i=0; i< this.groupby.size();i++) {
                s.append(this.groupby.get(i));

                if (i<this.groupby.size()-1) 
                    s.append(",");
           }
       }
         
       return s.toString();
                
    }
    
    @Override
    public String toString() {
        StringBuilder s = new  StringBuilder();
        int i=0;
        s.append("SELECT {\n").append(" query=").append(query).append("\n command=").append(command).append("\n fields=[\n");
        
        for (i=0; i<fields.size();i++) {
            s.append(" field ").append(i).append(" = ").append(fields.get(i).element).append("\n");
        }
        
        s.append(" ]\n");
        s.append(" from=[\n");
        
        for (i=0; i<from.size();i++) {
            s.append(" table ").append(i).append(" = ").append(from.get(i).getTable()).append("\n");
        }
        
        s.append(" ]\n");
        s.append(" where=[\n");

        for (i=0; i<where.size();i++) {
            s.append(" condition ").append(i).append(" = ").append(where.get(i).getColumn()).append(" ").append(where.get(i).getOperator()).append(where.get(i).getValue()).append("\n");
        }

        s.append(" ]\n");
        s.append(" group by=[\n");

        for (i=0; i<groupby.size();i++) {
            s.append(" group by criteria").append(i).append(" = ").append(groupby.get(i)).append("\n");
        }

        s.append(" ]\n");
        s.append(" order by=[\n");

        for (i=0; i<orderby.size();i++) {
            s.append(" order criteria ").append(i).append(" = ").append(orderby.get(i)).append("\n");
        }

        s.append(" ]\n}"); 
        
        return s.toString();
    }
    
    
}
