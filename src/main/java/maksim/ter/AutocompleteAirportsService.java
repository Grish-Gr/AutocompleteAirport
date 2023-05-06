package maksim.ter;


import maksim.ter.filters.WrongRequestFilterException;
import maksim.ter.filters.FilterRequestUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AutocompleteAirportsService {

    private final List<String> namesAirports;
    private final HashMap<String, AirportInfoInFile> airportInformation;

    public interface ActionWithSearchedAirport {
        void action(String nameAirport, String infoAirport);
    }

    public interface ActionAfterSearchingAirports{
        void action(int countFields, long time);
    }

    public AutocompleteAirportsService(BufferedReader airportsInfoFile) throws IOException {
        this.namesAirports = new ArrayList<>();
        this.airportInformation = new HashMap<>();

        int currentPointer = 0;
        while (airportsInfoFile.ready()){
            String line = airportsInfoFile.readLine();
            String[] data = line.split(",");
            String nameAirport = data[1].substring(1, data[1].length() - 1);
            int lengthStringInFile = line.length();
            namesAirports.add(nameAirport.toLowerCase() + data[0]);
            airportInformation.put(
                nameAirport.toLowerCase() + data[0],
                new AirportInfoInFile(nameAirport, currentPointer, lengthStringInFile)
            );
            currentPointer += lengthStringInFile + 1;
        }
        Collections.sort(namesAirports);
    }

    public void searchAirports(
        BufferedReader airportsInfoFile,
        String somethingSymbols,
        String filters,
        ActionWithSearchedAirport action,
        ActionAfterSearchingAirports afterAction
    ) throws IOException, WrongRequestFilterException {
        long start = new Date().getTime();
        int lengthPointer = 0;
        int count = 0;
        boolean notFilter = filters.isBlank();
        FilterRequestUtil requestFilter = notFilter ? null : new FilterRequestUtil(filters);

        List<AirportInfoInFile> infoInFiles = searchNamesAirportByWords(somethingSymbols);
        for (AirportInfoInFile infoInFile: infoInFiles.stream().sorted(AirportInfoInFile::compareAirportInfoInFileByPointer).collect(Collectors.toList())){
            airportsInfoFile.skip(infoInFile.getStartPointerInFile() - lengthPointer);
            String infoAirport = airportsInfoFile.readLine();
            if (notFilter || requestFilter.checkFieldOnFilter(infoAirport.split(","))){
                action.action(infoInFile.getNameAirport(), infoAirport);
                count++;
            }
            lengthPointer = infoInFile.getStartPointerInFile() + infoInFile.getLengthStringInFile() + 1;
        }
        afterAction.action(count, new Date().getTime() - start);
    }

    private List<AirportInfoInFile> searchNamesAirportByWords(String somethingSymbols){
        int ind = Collections.binarySearch(namesAirports, somethingSymbols.toLowerCase());
        ind  = ind < 0 ? -(ind + 1) : ind;
        List<AirportInfoInFile> infoInFiles = new ArrayList<>();
        for (; ind < namesAirports.size() && namesAirports.get(ind).startsWith(somethingSymbols.toLowerCase()); ind++) {
            infoInFiles.add(airportInformation.get(namesAirports.get(ind)));
        }
        return infoInFiles;
    }
}