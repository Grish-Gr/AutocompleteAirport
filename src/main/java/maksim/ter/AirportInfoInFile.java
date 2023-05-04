package maksim.ter;

public class AirportInfoInFile {

    private final String nameAirport;
    private final long startPointerInFile;
    private final int lengthStringInFile;

    public AirportInfoInFile(String nameAirport, long startPointerInFile, int lengthStringInFile) {
        this.nameAirport = nameAirport;
        this.startPointerInFile = startPointerInFile;
        this.lengthStringInFile = lengthStringInFile;
    }

    public String getNameAirport() {
        return nameAirport;
    }

    public long getStartPointerInFile() {
        return startPointerInFile;
    }

    public int getLengthStringInFile() {
        return lengthStringInFile;
    }
}
