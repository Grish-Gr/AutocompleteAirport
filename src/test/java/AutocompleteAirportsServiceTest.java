import maksim.ter.AutocompleteAirportsService;
import maksim.ter.filters.WrongRequestFilterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AutocompleteAirportsServiceTest {
    @Test
    public void limitTimeSearchTest() throws IOException, WrongRequestFilterException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        AutocompleteAirportsService autocompleteAirports = new AutocompleteAirportsService(reader);
        String filters = "";
        String command = "bo";

        stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
        reader = new BufferedReader(new InputStreamReader(stream));
        autocompleteAirports.searchAirports(
            reader,
            command,
            filters,
            ((nameAirport, infoAirport) -> {}),
            (((countFields, time) -> {
                Assertions.assertTrue(time < 28);
                Assertions.assertEquals(68, countFields);
            }))
        );
    }

    @Test
    public void limitTimeSearchWithFiltersTest() throws IOException, WrongRequestFilterException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        AutocompleteAirportsService autocompleteAirports = new AutocompleteAirportsService(reader);
        String filters = "column[1]>3500";
        String command = "bo";

        stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
        reader = new BufferedReader(new InputStreamReader(stream));
        autocompleteAirports.searchAirports(
            reader,
            command,
            filters,
            ((nameAirport, infoAirport) -> {}),
            (((countFields, time) -> {
                Assertions.assertTrue(time < 18);
                Assertions.assertEquals(39, countFields);
            }))
        );
    }

    @Test
    public void limitTimeSearchAllFieldsTest() throws IOException, WrongRequestFilterException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        AutocompleteAirportsService autocompleteAirports = new AutocompleteAirportsService(reader);
        String filters = "";
        String command = "";

        stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
        reader = new BufferedReader(new InputStreamReader(stream));
        autocompleteAirports.searchAirports(
            reader,
            command,
            filters,
            ((nameAirport, infoAirport) -> {}),
            (((countFields, time) -> {
                Assertions.assertTrue(time < 150);
                Assertions.assertEquals(7184, countFields);
            }))
        );
    }

    @Test
    public void limitTimeAtSearchingRandomSymbolsTest() throws IOException, WrongRequestFilterException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        AutocompleteAirportsService autocompleteAirports = new AutocompleteAirportsService(reader);
        String filters = "";
        String command = "jfklajklfja";

        stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
        reader = new BufferedReader(new InputStreamReader(stream));
        autocompleteAirports.searchAirports(
            reader,
            command,
            filters,
            ((nameAirport, infoAirport) -> {}),
            (((countFields, time) -> {
                Assertions.assertTrue(time < 2);
                Assertions.assertEquals(0, countFields);
            }))
        );
    }
}
