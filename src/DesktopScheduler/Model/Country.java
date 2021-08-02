package DesktopScheduler.Model;


/**
 * This class creates a country object.
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class Country {

    private int countryId;
    private String countryName;

    /**
     * This method creates a country object.
     * @param countryId - unique id of the country
     * @param countryName - name of the country
     */
    public Country(int countryId, String countryName){
        this.countryId = countryId;
        this.countryName = countryName;

    }

    //setters/getters
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    /**
     * Method is used to format the string output of the country object.
     * @return - formatted string
     */
    @Override
    public String toString(){
        return countryName;
    }
}
