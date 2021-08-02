package DesktopScheduler.Model;

/**
 * This class creates a Customer Object.
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class Customer {

    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionId;
    private int countryId;

    /**
     * This method creates a new customer object.
     * @param customerID - unique id of the customer
     * @param customerName - customer name
     * @param address - customer address
     * @param postalCode - customer postal code
     * @param phoneNumber - customer phone number
     * @param divisionId - division id is the state id
     * @param countryId - id of customers country
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phoneNumber, int divisionId, int countryId){
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionId = divisionId;
        this.countryId = countryId;
    }

    //setters/getters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public void getStateName(int divisionId){

    }

    public int getCountryId(){
        return countryId;
    }

    /**
     * Method is used to format the string output of a customer object.
     * @return - formatted string
     */
    @Override
    public String toString(){
        return "["+customerID+"]"+" "+customerName;
    }

}
