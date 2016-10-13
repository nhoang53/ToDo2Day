package edu.orangecoastcollege.cs273.nhoang53.todo2day;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper database;
    private List<Task> taskList; // List is bigger than ArrayList
    private TaskListAdapter taskListAdapter;

    private EditText taskEditText;
    private ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FOR NOW, (temporary) delete the old database, then create a new one
        //this.deleteDatabase(DBHelper.DATABASE_NAME);

        // Let's make a DBHelper reference
        database = new DBHelper(this);

        //
        //database.addTask(new Task("Testing task", 0));
        //database.addTask(new Task("Testing task 5", 1));

        // Let's get all the Tasks from database and print them with Log.i()
        taskList = database.getALlTasks();

        // List's create our custom task list adapter
        // (We want to associate the adapter with the context, the layout and the List)
        // connect checkBox and task
        taskListAdapter = new TaskListAdapter(this, R.layout.task_item, taskList);

        // Connect the ListView with our layout
        taskListView = (ListView) findViewById(R.id.taskListView);

        // Associate the adapter with the list view
        taskListView.setAdapter(taskListAdapter);

        // Connect EditText with our layout
        taskEditText = (EditText) findViewById(R.id.taskEditText);
    }

    public void addTask(View view) // add to database and taskList
    {
        String description = taskEditText.getText().toString();
        if(description.isEmpty()){
            Toast.makeText(this, "Task description cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else{
            // Let make a new Task
            Task newTask = new Task(description, 0);
            // Let's add the Task to the list adapter
            taskListAdapter.add(newTask);

            // Let's add the Task to the database
            database.addTask(newTask);

            taskEditText.setText("");
        }
    }

    public void changeTaskStatus(View view)
    {
        // convert view to CheckBox
        if(view instanceof  CheckBox) {
            CheckBox selectedCheckBox = (CheckBox) view;
            Task selectedTask = (Task) selectedCheckBox.getTag();
            selectedTask.setIsDone(selectedCheckBox.isChecked() ? 1 : 0);

            // Update the selectedTask in the database
            database.updateTask(selectedTask);
        }
    }

    public void clearAllTasks(View view)
    {
        // Clear the List
        taskList.clear();
        // Delete all the records (Tasks) in the database
        database.deleteAllTask();

        // Tell the TaskListAdapter update itself
        taskListAdapter.notifyDataSetChanged();
        //taskListAdapter = new TaskListAdapter(this, R.layout.task_item, taskList);
    }
}
