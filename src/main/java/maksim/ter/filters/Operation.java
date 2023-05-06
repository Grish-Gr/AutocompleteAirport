package maksim.ter.filters;

public enum Operation {
    EQUALS,
    NO_EQUALS,
    MORE,
    LESS;

    public static Operation fromExpression(String expression) throws WrongRequestFilterException {
        switch (expression){
            case "=": return EQUALS;
            case "<>": return NO_EQUALS;
            case ">": return MORE;
            case "<": return LESS;
            default: throw new WrongRequestFilterException();
        }
    }
}
