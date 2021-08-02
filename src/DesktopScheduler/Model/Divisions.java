package DesktopScheduler.Model;


/**
 * This class will create a Division object, these id's are referring to states/territories.
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class Divisions {

    private int divisionId;
    private String divisionName;
    private int countryId;

    /**
     * This method creates a division object.
     * @param divisionId - unique
     * @param divisionName - name of the state/territory
     * @param countryId - id of the country associated with the division
     */
    public Divisions (int divisionId, String divisionName, int countryId){
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;

    }

    //setters/getters
    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * This method is used to format the string output of the division object.
     * @return - formatted string
     */
    @Override
    public String toString(){
        return divisionName;
    }
}
