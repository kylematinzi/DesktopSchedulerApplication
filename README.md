## DesktopScheduler

This program is built and designed using JavaFX along with MySQL to create a desktop scheduling application. This 
application allows user's across multiple time zones to add/update/delete customers and add/update/delete appointments
for each customer. From the login window a user may choose to log in or to create a new user. If a user's computer
is set to French the login screen and alerts will be displayed in French. If an incorrect username and password is
entered an alert will be provided to the user. When appointments are created the application will not allow a customer
to have overlapping appointments or schedule outside of business hours. The application will also display three individual
reports. First is a report for each contact and how many appointments they have scheduled, second is a report of the
type of appointments scheduled and how many for that month, and third is a breakdown of all the users and how many 
appointments each has scheduled.

## What I Learned

- How to implement a simple username password verification
- Appending user login attempts to a .txt file including timestamp and pass/fail
- Advanced exception control to handle incorrect user input
- Effective use of lambda expressions
- Implementing localization API to support users in different geographical regions
- Implementing Date/Time API to support users in different time zones
- How to query/update/delete SQL data using java
