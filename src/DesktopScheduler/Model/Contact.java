package DesktopScheduler.Model;

/**
 * This class creates a contact object.
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class Contact {

    private int contactId;
    private String contactName;
    private String contactEmail;

    /**
     * This method creates a contact object.
     * @param contactId - unique id of the contact
     * @param contactName - contacts name
     * @param contactEmail - contacts email
     */
    public Contact(int contactId, String contactName, String contactEmail){
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    //setters/getters
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Method is used to format the string output of the contact class.
     * @return - formatted contact string
     */
    @Override
    public String toString(){
        return "["+contactId+"]"+" "+contactName;
    }

}
