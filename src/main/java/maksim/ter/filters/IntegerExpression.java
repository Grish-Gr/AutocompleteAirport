package maksim.ter.filters;

import maksim.ter.Operation;

public class IntegerExpression extends Expression<Integer>{

    public IntegerExpression(Operation operation, Integer value, int numColumn, int idExpression) {
        super(operation, value, numColumn, idExpression);
    }

    @Override
    public boolean check(String column) {
        int num = Integer.parseInt(column);
        switch (operation){
            case EQUALS: return num == value;
            case NO_EQUALS: return num != value;
            case MORE: return num > value;
            case LESS: return num < value;
            default: return false;
        }
    }
}
