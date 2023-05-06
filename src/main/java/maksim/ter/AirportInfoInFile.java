package maksim.ter;

public class AirportInfoInFile {

    private final String nameAirport;
    private final int startPointerInFile;
    private final int lengthStringInFile;

    public AirportInfoInFile(String nameAirport, int startPointerInFile, int lengthStringInFile) {
        this.nameAirport = nameAirport;
        this.startPointerInFile = startPointerInFile;
        this.lengthStringInFile = lengthStringInFile;
    }

    public int compareAirportInfoInFileByPointer(AirportInfoInFile infoInFile){
        if (this.startPointerInFile == infoInFile.getStartPointerInFile()){
            return 0;
        }
        return this.startPointerInFile > infoInFile.getStartPointerInFile() ? 1 : -1;
    }

    public String getNameAirport() {
        return nameAirport;
    }

    public int getStartPointerInFile() {
        return startPointerInFile;
    }

    public int getLengthStringInFile() {
        return lengthStringInFile;
    }
}
