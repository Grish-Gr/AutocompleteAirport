package maksim.ter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class RequestFilterUtil {

    private String expression;
    private ScriptEngine scriptEngine;

    public RequestFilterUtil() {
        expression = expression.replace("<>", "!=");
        expression = expression.replace("=", "==");
        this.expression = expression;
        this.scriptEngine = new ScriptEngineManager().getEngineByName("ECMAScript");
    }

    public boolean checkAirportOnFilters(String[] columns, String expression) throws ScriptException {
        for (int i =0; i < columns.length; i++){
            this.expression = expression.replace(String.format("column[%d]", i + 1), columns[i]);
        }
        Object res = scriptEngine.eval(expression);
        if (res instanceof Boolean){
            return (boolean) res;
        } else {
            return (int) res == 1;
        }
    }
}
