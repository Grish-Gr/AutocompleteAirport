package maksim.ter.filters;

import maksim.ter.BadRequestFilter;
import maksim.ter.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FilterSearchRequest {

    private final HashMap<Integer, Expression<?>> simpleExpression;
    private final HashMap<Integer, LogicExpression> logicExpressions;
    private int countLevel;

    public FilterSearchRequest(String expressionFilter) throws BadRequestFilter {
        this.simpleExpression = new HashMap<>();
        this.logicExpressions = new HashMap<>();

        int level = 0;
        int idExpression = 0;
        boolean isPrevExpression = false;
        Expression<?> prevExpression = null;
        LogicExpression prevLogicExpression = null;
        for (int i = 0; i < expressionFilter.length(); i++){
            if (level < 0){
                throw new BadRequestFilter();
            }
            if (isPrevExpression){
                if (expressionFilter.charAt(i) == '&' || expressionFilter.charAt(i) == '|'){
                    isPrevExpression = false;
                } else {
                    continue;
                }
            }
            if (expressionFilter.charAt(i) == '(') {
                countLevel = level;
                level++;
                continue;
            }
            if (expressionFilter.charAt(i) == ')'){
                level--;
                continue;
            }
            if (expressionFilter.charAt(i) == 'c'){
                idExpression++;
                Expression<?> currentExpression = getExpression(expressionFilter.substring(i), idExpression, level);
                simpleExpression.put(idExpression, currentExpression);
                if (prevExpression != null){
                    idExpression++;
                    if (prevLogicExpression != null){
                        logicExpressions.put(idExpression, new LogicExpression(
                            idExpression,
                            prevLogicExpression.getIdExpression(),
                            currentExpression.idExpression,
                            expressionFilter.charAt(i - 1),
                            level
                        ));
                    } else {
                        prevLogicExpression = new LogicExpression(
                            idExpression,
                            prevExpression.idExpression,
                            currentExpression.idExpression,
                            expressionFilter.charAt(i - 1),
                            level
                        );
                        logicExpressions.put(idExpression, prevLogicExpression);
                    }
                }
                prevExpression = currentExpression;
                isPrevExpression = true;
            }
        }
    }

    public boolean checkFieldOnFilter(String[] columns){
        HashMap<Integer, Boolean> computedValues = new HashMap<>();
        boolean resultFilter = false;
        for (Map.Entry<Integer, Expression<?>> entry: simpleExpression.entrySet()){
            resultFilter = entry.getValue().check(columns[entry.getValue().numColumn - 1]);
            computedValues.put(entry.getKey(), resultFilter);
        }
        if (logicExpressions.size() < 1){
            return resultFilter;
        }
        for (int lvl = countLevel; lvl >= 0; lvl--){
            for (Map.Entry<Integer, LogicExpression> entry: logicExpressions.entrySet()){
                if (entry.getValue().getLevel() != lvl){
                    continue;
                }
                boolean res = entry.getValue().getOperator() == '&'
                    ? computedValues.get(entry.getValue().getIdExpression1()) && computedValues.get(entry.getValue().getIdExpression2())
                    : computedValues.get(entry.getValue().getIdExpression1()) || computedValues.get(entry.getValue().getIdExpression2());
                resultFilter = res;
                computedValues.put(entry.getKey(), res);
            }
        }
        return resultFilter;
    }

    private Expression<?> getExpression(String allExpression, int idExpression, int level) throws BadRequestFilter {
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
