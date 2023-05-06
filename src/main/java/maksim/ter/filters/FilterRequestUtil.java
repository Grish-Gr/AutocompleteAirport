package maksim.ter.filters;

import maksim.ter.filters.expressions.Expression;
import maksim.ter.filters.expressions.FloatExpression;
import maksim.ter.filters.expressions.IntegerExpression;
import maksim.ter.filters.expressions.StringExpression;
import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.Map;

public class FilterRequestUtil {

    private final HashMap<Integer, Expression<?>> simpleExpression;
    private final String expression;
    private int countLevel;

    public FilterRequestUtil(String expressionFilter) throws WrongRequestFilterException {
        this.simpleExpression = new HashMap<>();
        StringBuilder exp = new StringBuilder();
        int idExpression = 0;
        int openParenthesis = 0;
        int closeParenthesis = 0;
        for (int i = 0; i < expressionFilter.length(); i++){
            if (expressionFilter.charAt(i) == 'c'){
                idExpression++;
                Expression<?> currentExpression = getExpression(expressionFilter.substring(i), idExpression);
                simpleExpression.put(idExpression, currentExpression);
                exp.append(idExpression);
            } else if (expressionFilter.charAt(i) == '&'){
                exp.append(" && ");
            } else if (expressionFilter.charAt(i) == '|'){
                exp.append(" || ");
            } else if (expressionFilter.charAt(i) == '('){
                exp.append(expressionFilter.charAt(i));
                openParenthesis++;
            } else if (expressionFilter.charAt(i) == ')'){
                exp.append(expressionFilter.charAt(i));
                closeParenthesis++;
            }
        }
        if (idExpression == 0){
            throw new WrongRequestFilterException("Запрос не содержит логических выражений (column[n]['>', '<', '=', '<>'][value])");
        }
        if (openParenthesis != closeParenthesis){
            throw new WrongRequestFilterException("Запрос содержит неправильно расставленные скобки");
        }
        this.expression = exp.toString();
    }

    private Expression<?> getExpression(String allExpression, int idExpression) throws WrongRequestFilterException {
        try{
            int startNum = allExpression.indexOf('[') + 1;
            int endNum = allExpression.indexOf(']');

            int numColumn = Integer.parseInt(allExpression.substring(startNum, endNum));
            Operation operation = allExpression.startsWith("<>", endNum + 1)
                ? Operation.NO_EQUALS
                : Operation.fromExpression(allExpression.substring(endNum + 1, endNum + 2));
            int startIndexValue = allExpression.startsWith("<>", endNum + 1) ? endNum + 3 : endNum + 2;
            if (0 > numColumn || numColumn > 14){
                throw new WrongRequestFilterException(String.format("Фильтрация по %d не возможна, так как не входит в диапозон колонок 1-13", numColumn));
            }
            switch (numColumn){
                case 1:
                case 9:
                case 10: return new IntegerExpression(
                    operation,
                    getIntegerValue(startIndexValue, allExpression),
                    numColumn,
                    idExpression);
                case 7:
                case 8: return new FloatExpression(
                    operation,
                    getFloatValue(startIndexValue, allExpression),
                    numColumn,
                    idExpression);
                default: return new StringExpression(
                    operation,
                    getStringValue(startIndexValue, allExpression),
                    numColumn,
                    idExpression
                );
            }
        }catch (WrongRequestFilterException exception){
            throw exception;
        }
        catch (Exception exception){
            throw new WrongRequestFilterException();
        }
    }

    public boolean checkFieldOnFilter(String[] columns){
        String expressionFilter = expression;
        if (simpleExpression.size() == 1){
            Expression<?> exp =  simpleExpression.get(1);
            return exp.check(columns[exp.getNumColumn() - 1]);
        }
        for (Map.Entry<Integer, Expression<?>> entry: simpleExpression.entrySet()){
            expressionFilter = expressionFilter.replace(
                String.valueOf(entry.getValue().getIdExpression()),
                String.valueOf(entry.getValue().check(columns[entry.getValue().getNumColumn() - 1])));
        }
        return (boolean) MVEL.eval(expressionFilter);
    }

    private int getIntegerValue(int startIndex, String expression){
        StringBuilder num = new StringBuilder();
        for (char ch: expression.substring(startIndex).toCharArray()){
            if (Character.isDigit(ch)){
                num.append(ch);
            } else {
                break;
            }
        }
        return Integer.parseInt(num.toString());
    }

    private float getFloatValue(int startIndex, String expression){
        StringBuilder num = new StringBuilder();
        boolean isDoth = false;
        for (char ch: expression.substring(startIndex).toCharArray()){
            if (Character.isDigit(ch) && !isDoth){
                num.append(ch);
            } else if (ch == '.') {
                isDoth = true;
            } else {
                break;
            }
        }
        return Float.parseFloat(num.toString());
    }

    private String getStringValue(int startIndex, String expression){
        return expression.substring(startIndex + 1, startIndex + 1 + expression.substring(startIndex + 1).indexOf("'"));
    }

}
