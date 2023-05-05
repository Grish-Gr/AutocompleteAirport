package maksim.ter.filters;

import maksim.ter.Operation;

public abstract class Expression<T> {
    protected final Operation operation;
    protected final T value;
    protected final int numColumn;
    protected final int idExpression;

    protected Expression(Operation operation, T value, int numColumn, int idExpression) {
        this.operation = operation;
        this.value = value;
        this.numColumn = numColumn;
        this.idExpression = idExpression;
    }

    public abstract boolean check(String column);

    public int getIdExpression() {
        return idExpression;
    }
}
