package maksim.ter;

import maksim.ter.filters.WrongRequestFilterException;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            AutocompleteAirportsService autocompleteAirports = new AutocompleteAirportsService(reader);
            Scanner scanner = new Scanner(System.in);
            String command;
            System.out.println("Напишите фильтры для поиска или завершите программу (\"!quit\"):");
            while (!(command = scanner.nextLine()).equals("!quit")){
                stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("airports.csv");
                reader = new BufferedReader(new InputStreamReader(stream));
                System.out.println("Напишите начальные символы для поиска:");
                try{
                    autocompleteAirports.searchAirports(
                        reader,
                        scanner.nextLine(),
                        command,
                        ((nameAirport, infoAirport) -> System.out.println(String.format("\"%s\"[%s]", nameAirport, infoAirport))),
                        (((countFields, time) -> System.out.println(String.format("Количество найденных строк: %d Время, затраченное на поиск %d мс", countFields, time))))
                    );
                } catch (WrongRequestFilterException badRequestFilter){
                    System.out.println(badRequestFilter.getMessage());
                }
                System.out.println("Напишите фильтры для поиска или завершите программу (\"!quit\"):");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


