/**
 * @author Baris Tanyeri ezybt3@nottingham.ac.uk
 * @version 3.0
 */
package test.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
/**
 * Settings activity
 * This represents the settings activity visually to the user
 */
public class Settings extends AppCompatActivity {
    protected String city;
    protected String usrn;
    protected Button update;
    protected TextView username;
    protected test.events.ConnectionClass connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent Intentextras = getIntent();
        usrn = Intentextras.getStringExtra("userid");
        /*************************************************************/
        connectionClass = new test.events.ConnectionClass();
        Spinner spinner = (Spinner) findViewById(R.id.spinner3);
        update = (Button)findViewById(R.id.updatebutton);
        username =(TextView)findViewById(R.id.usernamesettings);
        /*************************************************************/
        List<String> cities = new ArrayList<String>();
        cities.add("Nottingham");
        cities.add("Derby");
        cities.add("London");
        cities.add("Glasgow");
        cities.add("Edinburgh");
        cities.add("Cardiff");
        username.setText(usrn);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
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
        /**
         * User update in database
         * This updates the user information in database
         * Only city is implemented
         *
         */
       update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {String z;
               try {
                   Connection con = connectionClass.CONN();

                   if (con == null) {
                       z = "Error in connection with SQL server";
                   } else {

                       String query = "UPDATE Username\n" +
                               "SET City='"+city.toString()+"'"+
                               "WHERE UserId='"+usrn.toString()+"';";
                       PreparedStatement preparedStatement = con.prepareStatement(query);
                       preparedStatement.executeUpdate();
                       z = "Success";
                   }
               } catch (Exception ex) {
                   z = ex.toString();
               }
               Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show();

           }
       });
    }

}
