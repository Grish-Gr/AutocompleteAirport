package maksim.ter.filters;

public class WrongRequestFilterException extends Exception{
    public WrongRequestFilterException(String message){
        super(message);
    }
    public WrongRequestFilterException() {
        super("Неправильно введены фильтры. Используйте првильный синтаксис для фильтров (\"column[1]>3000 & column[5]='ENG'\")");
    }
}
