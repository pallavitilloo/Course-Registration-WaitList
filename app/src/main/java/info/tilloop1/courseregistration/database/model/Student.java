package info.tilloop1.courseregistration.database.model;

/*************************************************************************************************
 * The class WaitingList contains the Table and columns to be created in SQLite database.
 * Created by: Pallavi Tilloo
 * Dt: 04/22/2021
 *************************************************************************************************/
public class Student {

    public static final String TABLE_NAME = "WaitingList";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STUDENT_NAME = "name";
    public static final String COLUMN_PRIORITY = "priority";

    private int id;
    private String name;
    private String priority;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_STUDENT_NAME + " TEXT,"
                    + COLUMN_PRIORITY + " TEXT"
                    + ")";

    /********   Constructors   ***************/
    public Student() {
        //Empty constructor
    }

    /**********************************************************************************************
     * Constructor that creates a new instance takes ID, Name and Priority
     * @param id: ID of entry to be created
     * @param name: Student name
     * @param priority: Priority of the student
     */
    public Student(int id, String name, String priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    /********************************************************************************************
     * Getter method for Id
     * @return ID
     */
    public int getId() {
        return id;
    }

    /********************************************************************************************
     * Getter method for Name
     * @return Name
     */
    public String getName() {
        return name;
    }

    /********************************************************************************************
     * Getter method for Priority
     * @return priority
     */
    public String getPriority() {
        return priority;
    }

    /******** Setter methods for private class members ********/

    /********************************************************************************************
     * Set the ID value
     * @param id: ID value to be updated
     */
    public void setId(int id) {
        this.id = id;
    }

    /********************************************************************************************
     * Set the Name value
     * @param name: Value of name to be updated
     */
    public void setName(String name) {
        this.name = name;
    }

    /********************************************************************************************
     * Set the priority
     * @param priority: Priority value to be updated
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }
}

/******************************** End of class Student **************************************/