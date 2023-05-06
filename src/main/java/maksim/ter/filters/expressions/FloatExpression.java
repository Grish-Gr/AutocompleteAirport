package maksim.ter.filters.expressions;

import maksim.ter.filters.Operation;

public class FloatExpression extends Expression<Float> {
    public FloatExpression(Operation operation, Float value, int numColumn, int idExpression) {
        super(operation, value, numColumn, idExpression);
    }

    @Override
    public boolean check(String column) {
        float num = Float.parseFloat(column);
        switch (operation){
            case EQUALS: return num == value;
            case NO_EQUALS: return num != value;
            case MORE: return num > value;
            case LESS: return num < value;
            default: return false;
        }
    }
}
