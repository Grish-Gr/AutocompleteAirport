package maksim.ter;

public enum Operation {
    EQUALS("="),
    NO_EQUALS("<>"),
    MORE(">"),
    LESS("<");

    private final String expression;

    Operation(String expression) {
        this.expression = expression;
    }

    public static Operation fromExpression(String expression) throws BadRequestFilter {
        switch (expression){
            case "=": return EQUALS;
            case "<>": return NO_EQUALS;
            case ">": return MORE;
            case "<": return LESS;
            default: throw new BadRequestFilter();
        }
    }

    public String getExpression() {
        return expression;
    }
}
