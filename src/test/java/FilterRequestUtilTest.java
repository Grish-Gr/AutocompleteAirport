import maksim.ter.filters.FilterRequestUtil;
import maksim.ter.filters.WrongRequestFilterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FilterRequestUtilTest {

    @Test
    public void simpleExpressionTest() throws WrongRequestFilterException {
        String[] data = new String[] {
            "6122","\"Khankala Air Base\"","\"Grozny\"","\"Russia\"","\"GRV\"","\"URMG\"","43.298099517822266",
            "45.78409957885742","548","3","\"N\"","\"Europe/Moscow\"","\"airport\"","\"OurAirports\""
        };

        FilterRequestUtil filterRequest = new FilterRequestUtil(
            "column[1]>3000 & column[4]=\"Russia\""
        );

        Assertions.assertTrue(filterRequest.checkFieldOnFilter(data));
    }

    @Test
    public void expressionTest() throws WrongRequestFilterException {
        String[] data = new String[] {
            "6122","\"Khankala Air Base\"","\"Grozny\"","\"Russia\"","\"GRV\"","\"URMG\"","43.298099517822266",
            "45.78409957885742","548","3","\"N\"","\"Europe/Moscow\"","\"airport\"","\"OurAirports\""
        };

        FilterRequestUtil filterRequest = new FilterRequestUtil(
            "column[1]<1000 | ((column[4]=\"Russia\" & column[8]<44.2) | column[11]=\"N\")"
        );

        Assertions.assertTrue(filterRequest.checkFieldOnFilter(data));
    }

    @Test
    public void wrongExpressionNotEqualsParenthesisTest() throws WrongRequestFilterException {
        String[] data = new String[] {
            "6122","\"Khankala Air Base\"","\"Grozny\"","\"Russia\"","\"GRV\"","\"URMG\"","43.298099517822266",
            "45.78409957885742","548","3","\"N\"","\"Europe/Moscow\"","\"airport\"","\"OurAirports\""
        };

        Assertions.assertThrows(WrongRequestFilterException.class, () -> {
            FilterRequestUtil filterRequest = new FilterRequestUtil(
                "column[1]<1000 | ((column[4]=\"Russia\" & column[8]<44.2) | column[11]=\"N\"))"
            );
        });
    }

    @Test
    public void wrongExpressionNotEqualsExpressionTest() throws WrongRequestFilterException {
        String[] data = new String[] {
            "6122","\"Khankala Air Base\"","\"Grozny\"","\"Russia\"","\"GRV\"","\"URMG\"","43.298099517822266",
            "45.78409957885742","548","3","\"N\"","\"Europe/Moscow\"","\"airport\"","\"OurAirports\""
        };

        Assertions.assertThrows(WrongRequestFilterException.class, () -> {
            FilterRequestUtil filterRequest = new FilterRequestUtil(
                "column[1]"
            );
        });
    }
}
