/**
 * @author Baris Tanyeri ezybt3@nottingham.ac.uk
 * @version 3.0
 */
package test.events;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
/**
 * Register activity
 * This represents the register activity visually to the user
 * @Param usr username
 * @Param password password of the user
 * @Param password2 password check
 * @Param name users real name
 * @Param email users email
 * @Param phone users phone
 * @Param city virtual string that holds the city chosen on spinner
 * @Param z toast text
 * @Param connectionClass connection parameters from the embedded class
 */
public class Register extends AppCompatActivity {
    protected EditText usr;
    protected EditText password;
    protected EditText password2;
    protected EditText name;
    protected EditText email;
    protected EditText phone;
    protected String city;
    protected String h = "user";
    protected String z ="";
    test.events.ConnectionClass connectionClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usr = (EditText) findViewById(R.id.editText);
        connectionClass = new test.events.ConnectionClass();
        Button Button = (Button) findViewById(R.id.button);
        password = (EditText)findViewById(R.id.pwd);
        password2 = (EditText)findViewById(R.id.pwd2);
        name = (EditText)findViewById(R.id.Name);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        /*************************************************************/
        //Spinner list
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
         * Register button onclick listener
         * This inserts the data in to the database
         *
         */
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        if (password.getText().toString().equals(password2.getText().toString())) {
                            String query = "insert into Username (UserId,Password,Name,Mail,Phone,City) values ('" + usr.getText().toString() + "','" + password.getText().toString() + "'," +
                                    "'" + name.getText().toString() + "','" + email.getText().toString() + "','" + phone.getText().toString() + "','" + city.toString() + "')";
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();
                            z = "Registration Complete";
                            finish();
                        } else z = "Password Mismatch";
                    }
                } catch (Exception ex) {
                    z = ex.toString();
                }


                Toast toast = Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT);

                toast.show();


            }
        });
        /**
         * Spinner onclick listener
         * This sets the city to the selected city in spinner list
         *
         */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                city = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }


}

