package edu.orangecoastcollege.cs273.nhoang53.todo2day;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// it auto private, DON'T put public because other people may access ur database
class DBHelper extends SQLiteOpenHelper {

    //TASK 1: DEFINE THE DATABASE VERSION, NAME AND TABLE NAME
    static final String DATABASE_NAME = "ToDo2Day";
    private static final String DATABASE_TABLE = "Tasks";
    private static final int DATABASE_VERSION = 1; // version of app when changed database


    //TASK 2: DEFINE THE FIELDS (COLUMN NAMES) FOR THE TABLE
    private static final String KEY_FIELD_ID = "id";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_IS_DONE = "is_done";


    public DBHelper (Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database){
        String table = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIELD_DESCRIPTION + " TEXT, "
                + FIELD_IS_DONE + " INTEGER" + ")";
        database.execSQL (table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }

    // Create a method ato add a brand new task to the database
    public void addTask(Task newTask){
        // Step 1) Create a reference to our database:
        SQLiteDatabase db = this.getWritableDatabase(); // this refer to class DBHelper

        // Step 2) Make a key-value pair for eac value you want to insert
        ContentValues values = new ContentValues();
        //values.put(KEY_FIELD_ID, newTask.getId()); // dont need, cause ID auto create
        values.put(FIELD_DESCRIPTION, newTask.getDescription());
        values.put(FIELD_IS_DONE, newTask.getIsDone());

        // Step 3) insert value into our database
        db.insert(DATABASE_TABLE, null, values);

        // 4) Close database
        db.close();
    }

    public ArrayList<Task> getALlTasks(){
        // 1) Create a refeence to our database
        SQLiteDatabase db = this.getReadableDatabase();

        // 2) Make a new empty ArrayList
        ArrayList<Task> allTask = new ArrayList<>();

        // 3) Query the database for all records (all rows) and all fields (all columns)
        // the return type of query is Cursor
        Cursor results = db.query(DATABASE_TABLE, null, null, null, null, null, null);

        // 4) Loop though the results, create Task objectd, add to the ArrayList
        if(results.moveToFirst())
        {
            do{
                int id = results.getInt(0); // column index
                String description = results.getString(1);
                int isDone = results.getInt(2);

                allTask.add(new Task(id, description, isDone));
            }
            while(results.moveToNext());
        }

        db.close();

        return allTask;
    }

    public void updateTask(Task existingTask)
    {
        // Step 1) Create a reference to our database:
        SQLiteDatabase db = this.getWritableDatabase(); // this refer to class DBHelper

        // Step 2) Make a key-value pair for eac value you want to insert
        ContentValues values = new ContentValues();
        //values.put(KEY_FIELD_ID, newTask.getId()); // dont need, cause ID auto create
        values.put(FIELD_DESCRIPTION, existingTask.getDescription());
        values.put(FIELD_IS_DONE, existingTask.getIsDone());

        String[] values2 = new String[] {existingTask.getDescription(), ""+existingTask.getIsDone()};

        // Step 3) insert value into our database
        db.update(DATABASE_TABLE, values,
                KEY_FIELD_ID + "=?",
                new String[] {String.valueOf(existingTask.getId())});

        // 4) Close database
        db.close();
    }

    public void deleteAllTask()
    {
        // 1) Crete  a reference to our database
        SQLiteDatabase db =  this.getWritableDatabase();
        db.delete(DATABASE_TABLE, null, null);
        db.close();
    }

}
