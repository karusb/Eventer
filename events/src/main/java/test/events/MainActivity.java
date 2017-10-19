/**
 * @author Baris Tanyeri ezybt3@nottingham.ac.uk
 * @version 3.0
 */
package test.events;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main activity
 * This represents the Main screen of the app visually to the user
 * @Param location users city
 * @Param username virtual string to hold username
 * @Param locationview text view to show the city
 * @Param pbbar progress bar , shows when background process running
 * @Param 1stpro list that will later call another layout
 * @Param create create button
 * @Param refresh refresh button
 * @Param settings settings button
 * @Param connectionClass connection parameters from the embedded class
 */
public class MainActivity extends AppCompatActivity {
    ConnectionClass connectionClass;


    protected String location;
    protected String username;
    protected TextView locationview;
    protected ProgressBar pbbar;
    protected ListView lstpro;
    protected String proid;
    protected Button create;
    protected ImageButton refresh;
    protected ImageButton settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*************************************************************/
        Intent Intentextras = getIntent();
        String usrn = Intentextras.getStringExtra("username");
        /*************************************************************/
        TextView usrnview = (TextView) findViewById(R.id.usrnview);
        username = usrn;
        usrnview.setText(usrn);
        /*************************************************************/
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.VISIBLE);
        lstpro = (ListView) findViewById(R.id.events);
        locationview = (TextView)findViewById(R.id.location);
        refresh = (ImageButton)findViewById(R.id.refreshbut);
        create = (Button)findViewById(R.id.createbut);
        settings = (ImageButton)findViewById(R.id.settings);
        proid = "";
        connectionClass = new ConnectionClass();
        /*************************************************************/

        FillList fillList = new FillList();
        fillList.execute();
        pbbar.setVisibility(View.GONE);

        /* Button onclick listeners
        * Create: Create event button calls create event activity puts user data
        * Refresh : Refresh button fills the list again
        * Settings : Settings button calls the user settings activity puts user data
        *
        * */
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("test.events.CreateEvent");
                Bundle bundle = new Bundle();
                bundle.putString("userid", username.toString());
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillList fillList = new FillList();
                fillList.execute();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("test.events.Settings");
                Bundle bundle = new Bundle();
                bundle.putString("userid",username.toString());
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }
    /**
     * FillList
     * This class extends an asynctask to execute background threads and fills the list on the main screen
     *
     */
    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            locationview.setText(location);
            String[] from = { "A", "B", "C" ,"D","E"};
            int[] views = { R.id.lblproid, R.id.lblproname,R.id.Iblprodate };
            final SimpleAdapter ADA = new SimpleAdapter(MainActivity.this,
                    prolist, R.layout.eventmp, from,
                    views);
            lstpro.setAdapter(ADA);
            /**
             * OnItemClickListener
             * This determines what happens when an item on listview is clicked.
             *
             */
            lstpro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);

                    String eventid  = (String) obj.get("E"); // Get event ID

                    Intent i = new Intent("test.events.Event");
                    Bundle bundle = new Bundle();
                    bundle.putString("eventid", eventid.toString());
                    bundle.putString("userid",username.toString());
                    i.putExtras(bundle); // Send user and event data to activity
                    startActivity(i);
                }
            });
        }
        /**
         * Background task
         * This queries database in the background and puts the data in list view to be shown to the user
         *Returns toast text,
         */
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();

                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query2 = "select * from Username where UserId='"+username.toString()+"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs2 = stmt.executeQuery(query2);
                    while(rs2.next()!=true); // Wait until its not NULL.
                    location = rs2.getString("City"); // Put the city information in the app
                    String query = "select * from Eventinfo where Eventcity='"+location.toString()+"'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();


                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("Event_type"));
                        datanum.put("B", rs.getString("Eventname"));
                        datanum.put("C", rs.getString("Eventdate"));
                        datanum.put("E", rs.getString("Id"));
                        prolist.add(datanum);

                    }
                    z = "Success";
                }
            } catch (Exception ex) {
                z = ex.toString();
            }
            return z;
        }
    }
}
