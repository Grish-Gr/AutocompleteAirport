package maksim.ter.filters;

public class LogicExpression {

    private final int idExpression;
    private final int idExpression1;
    private final int idExpression2;
    private final char operator;
    private final int level;

    public LogicExpression(int idExpression, int idExpression1, int idExpression2, char operator, int level) {
        this.idExpression = idExpression;
        this.idExpression1 = idExpression1;
        this.idExpression2 = idExpression2;
        this.operator = operator;
        this.level = level;
    }

    public int getIdExpression1() {
        return idExpression1;
    }

    public int getIdExpression2() {
        return idExpression2;
    }

    public int getIdExpression() {
        return idExpression;
    }

    public char getOperator() {
        return operator;
    }

    public int getLevel() {
        return level;
    }
}
