package maksim.ter;

import java.io.*;
import java.util.*;

public class Main {

    private static void f() throws IOException {
        return;
    }

    public static void main(String[] args) {
        try {
            RandomAccessFile airportsInfoFile = new RandomAccessFile("src/main/resources/airports.csv", "r");
            AutocompleteAirportsService autocompleteAirports = new AutocompleteAirportsService(airportsInfoFile);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Start work");
            String command;
            while (!(command = scanner.nextLine()).equals("end")){
                long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
                autocompleteAirports.searchAirports(
                    command,
                    ((nameAirport, infoAirport) -> System.out.println(String.format("\"%s\"[%s]", nameAirport, infoAirport))),
                    (((countFields, time) -> System.out.println(String.format("Количество найденных строк: %d Время, затраченное на поиск %d мс", countFields, time))))
                );
                long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
                System.out.println(afterUsedMem - beforeUsedMem);
            }
            airportsInfoFile.close();

            } catch(IOException e){
                throw new RuntimeException(e);
            } finally{
            }
        }

    }


