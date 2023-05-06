package maksim.ter.filters.expressions;

import maksim.ter.filters.Operation;

public class StringExpression extends Expression<String> {
    public StringExpression(Operation operation, String value, int numColumn, int idExpression) {
        super(operation, value, numColumn, idExpression);
    }

    @Override
    public boolean check(String column) {
        column = column.substring(1, column.length() - 1);
        switch (operation){
            case EQUALS: return column.equals(value);
            case NO_EQUALS: return !column.equals(value);
            case MORE: return column.compareTo(value) > 0;
            case LESS: return column.compareTo(value) < 0;
            default: return false;
        }
    }
}
