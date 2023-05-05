package maksim.ter.filters;

import maksim.ter.BadRequestFilter;
import maksim.ter.Operation;
import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.Map;

public class FilterSearchV2 {

    private final HashMap<Integer, Expression<?>> simpleExpression;
    private String expression;
    private int countLevel;

    public FilterSearchV2(String expressionFilter) throws BadRequestFilter {
        this.simpleExpression = new HashMap<>();
        StringBuilder exp = new StringBuilder();
        int idExpression = 0;
        for (int i = 0; i < expressionFilter.length(); i++){
            switch (expressionFilter.charAt(i)){
                case 'c': {
                    idExpression++;
                    Expression<?> currentExpression = getExpression(expressionFilter.substring(i), idExpression);
                    simpleExpression.put(idExpression, currentExpression);
                    exp.append(idExpression);
                    break;
                }
                case '&': {
                    exp.append(" && ");
                    break;
                }
                case '|': {
                    exp.append(" || ");
                    break;
                }
                case ')':
                case '(':{
                    exp.append(expressionFilter.charAt(i));
                    break;
                }
            }
        }
        this.expression = exp.toString();
    }

    public boolean checkFieldOnFilter(String[] columns){
        String expressionFilter = expression;
        if (simpleExpression.size() == 1){
            Expression<?> exp =  simpleExpression.get(1);
            return exp.check(columns[exp.numColumn - 1]);
        }
        for (Map.Entry<Integer, Expression<?>> entry: simpleExpression.entrySet()){
            expressionFilter = expressionFilter.replace(
                String.valueOf(entry.getValue().idExpression),
                String.valueOf(entry.getValue().check(columns[entry.getValue().numColumn - 1])));
        }
        return (boolean) MVEL.eval(expressionFilter);
    }

    private Expression<?> getExpression(String allExpression, int idExpression) throws BadRequestFilter {
        int startNum = allExpression.indexOf('[') + 1;
        int endNum = allExpression.indexOf(']');
        int numColumn = Integer.parseInt(allExpression.substring(startNum, endNum));
        Operation operation = allExpression.startsWith("<>", endNum + 1)
            ? Operation.NO_EQUALS
            : Operation.fromExpression(allExpression.substring(endNum + 1, endNum + 2));
        int startIndexValue = allExpression.startsWith("<>", endNum + 1) ? endNum + 3 : endNum + 2;
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
        return expression.substring(startIndex + 1, startIndex + 1 + expression.substring(startIndex + 1).indexOf("\""));
    }

}
