package maksim.ter;

import maksim.ter.filters.FilterSearchRequest;
import maksim.ter.filters.FilterSearchV2;

import javax.script.ScriptException;
import java.io.*;
import java.util.*;

public class Main {

    private static void f() throws IOException {
        return;
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            RandomAccessFile airportsInfoFile = new RandomAccessFile(scanner.nextLine(), "r");
            AutocompleteAirportsService autocompleteAirports = new AutocompleteAirportsService(airportsInfoFile);

            System.out.println("Start work");
            String command;
            while (!(command = scanner.nextLine()).equals("end")){
                long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
                autocompleteAirports.searchAirports(
                    scanner.nextLine(),
                    command,
                    ((nameAirport, infoAirport) -> System.out.println(String.format("\"%s\"[%s]", nameAirport, infoAirport))),
                    (((countFields, time) -> System.out.println(String.format("Количество найденных строк: %d Время, затраченное на поиск %d мс", countFields, time))))
                );
                long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            }
            airportsInfoFile.close();

        } catch (BadRequestFilter badRequestFilter){
        } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }


