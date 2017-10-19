/**
 * @author Baris Tanyeri ezybt3@nottingham.ac.uk
 * @version 3.0
 */
package test.events;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * Create event activity
 * This provide a GUI to create an event on the database
 * @Param usr username
 * @Param eventtp event type (which sport)
 * @Param title text box for entering the title of the event
 * @Param date string to hold the date
 * @Param address text box for entering address
 * @Param descript text box for the description of the event
 * @Param time text box for entering time
 * @Param createbut create the event with respect to entered details
 * @Param eventtype string to hold the event type
 * @Param year int that holds current chosen year on date picker
 * @Param month int that holds current chosen month on date picker
 * @Param day int that holds current chosen day on date picker
 * @Param seekevent event type picker (seek bar)
 * @Param dpresult datapicker that is used to get the current date
 * @Param z toast string
 * @Param connectionClass connection parameters from the embedded class
 */
public class CreateEvent extends AppCompatActivity {
    protected String usr;
    protected TextView eventtp;
    protected EditText title;
    protected String date;
    protected EditText address;
    protected EditText descript;
    protected EditText time;
    protected String city;
    protected Button  createbut;
    protected String eventtype;
    private int year;
    private int month;
    private int day;
    private SeekBar seekevent;
    private DatePicker dpResult;
    protected String h = "user";
    protected String z ="";
    protected test.events.ConnectionClass connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Intent Intentextras = getIntent();
        usr = Intentextras.getStringExtra("userid");
        /*************************************************************/
        connectionClass = new test.events.ConnectionClass();
        eventtp = (TextView) findViewById(R.id.Eventview);
        createbut = (Button) findViewById(R.id.createbutton);
        title = (EditText) findViewById(R.id.Title);
        address = (EditText) findViewById(R.id.Address);
        descript = (EditText) findViewById(R.id.Description);
        seekevent = (SeekBar) findViewById(R.id.seekBar);
        time = (EditText)findViewById(R.id.Time);
        /*************************************************************/
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<String> cities = new ArrayList<String>();
        cities.add("Nottingham");
        cities.add("Derby");
        cities.add("London");
        cities.add("Glasgow");
        cities.add("Edinburgh");
        cities.add("Cardiff");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        /**
         * Date picker initilization and string builder
         * This initilizes the date picker widget and puts the data in to date string
         *
         */
        dpResult = (DatePicker) findViewById(R.id.datePicker);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        dpResult.init(year, month, day,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    // when dialog box is closed, below method will be called.
                    public void onDateChanged(DatePicker datePicker, int selectedYear,
                                              int selectedMonth, int selectedDay) {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;

                        // set selected date into string
                        date = (new StringBuilder().append(month + 1)
                                .append("/").append(day).append("/").append(year)
                                .append(" ")).toString();


                        dpResult.init(year, month, day, null);
                    }
                });


        /**
         * Seekbar change listener
         * This shows the event types that can be chosen from the list in the function
         *
         */
        seekevent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        eventtype = "Football";
                        break;
                    case 1:
                        eventtype = "Tennis";
                        break;
                    case 2:
                        eventtype = "Basketball";
                        break;
                    case 3:
                        eventtype = "Fencing";
                        break;
                    case 4:
                        eventtype = "Badminton";
                        break;
                    case 5:
                        eventtype = "Squash";
                        break;
                    case 6:
                        eventtype = "Cricket";
                        break;
                    case 7:
                        eventtype = "Rugby";
                        break;
                }
                eventtp.setText(eventtype.toString());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        /**
         * Event create button onclick listener
         * This puts the data in to the data base and shows a toast
         *
         */
        createbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String query = "insert into Eventinfo (Event_type,Eventname,Eventdate,Eventcity,Eventlocation,Createdby,Descr,TeamA,TeamB) values ('" + eventtype + "','" + title.getText().toString() + "'," +
                                "'" + date + " " + time.getText().toString() + "','" + city + "','" + address.getText().toString() + "','" + usr.toString() + "','" + descript.getText().toString() + "','" + "Team A," + usr.toString() + "','Team B')";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Event Created!";
                        finish();

                    }
                } catch (Exception ex) {
                    z = ex.toString();
                }


                Toast toast = Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT);

                toast.show();


            }
        });
        /**
         * Spinner on click listener
         * This puts the data in the spinner in to a string variable to be used in the app when an item is clicked
         *
         */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                        city =  parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }



}
