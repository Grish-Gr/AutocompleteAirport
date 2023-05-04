package maksim.ter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class AutocompleteAirportsService {

    private final List<String> namesAirports;
    private final HashMap<String, AirportInfoInFile> airportInformation;
    private final RandomAccessFile airportsInfoFile;

    public AutocompleteAirportsService(RandomAccessFile airportsInfoFile) throws IOException {
        this.airportsInfoFile = airportsInfoFile;
        this.namesAirports = new ArrayList<>();
        this.airportInformation = new HashMap<>();

        long currentPointer;
        while ((currentPointer = airportsInfoFile.getFilePointer()) != airportsInfoFile.length()){
            String[] data = airportsInfoFile.readLine().split(",");
            String nameAirport = data[1].substring(1, data[1].length() - 1);
            int lengthStringInFile = (int) (airportsInfoFile.getFilePointer() - currentPointer - 1);
            namesAirports.add(nameAirport.toLowerCase());
            airportInformation.put(
                nameAirport.toLowerCase(),
                new AirportInfoInFile(nameAirport, currentPointer, lengthStringInFile)
            );
        }
        Collections.sort(namesAirports);
    }

    public void searchAirports(String nameAirport, ActionWithSearchAirport action, ActionAfterSearchingAirports afterAction) throws IOException {
        long start = new Date().getTime();
        int i = Collections.binarySearch(namesAirports, nameAirport.toLowerCase());
        if (i < 0) {
            i = -(i + 1);
        }
        int count = 0;
        for (byte[] buffer = new byte[airportInformation.get(namesAirports.get(i)).getLengthStringInFile()];
             namesAirports.get(i).startsWith(nameAirport.toLowerCase());
             i++, buffer = new byte[airportInformation.get(namesAirports.get(i)).getLengthStringInFile()]
        ) {
            airportsInfoFile.seek(airportInformation.get(namesAirports.get(i)).getStartPointerInFile());
            airportsInfoFile.read(buffer);
            action.action(airportInformation.get(namesAirports.get(i)).getNameAirport(), new String(buffer));
            count++;
        }
        afterAction.action(count, new Date().getTime() - start);
    }
}

interface ActionWithSearchAirport{
    void action(String nameAirport, String infoAirport);
}

interface ActionAfterSearchingAirports{
    void action(int countFields, long time);
}