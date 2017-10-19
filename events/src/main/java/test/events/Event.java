/**
 * @author Baris Tanyeri ezybt3@nottingham.ac.uk
 * @version 3.0
 */
package test.events;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Event activity
 * This represents the individual event screen of the app visually to the user
 * @Param eventloc  string to hold event city
 * @Param eventid string to hold id of the event
 * @Param eventde string to hold event notes
 * @Param eventaddress strint to hold event address
 * @Param eventlocation event city text view
 * @Param eventdateview event date text view
 * @Param eventdesc event description text view
 * @Param eventaddr event address text view
 * @Param pbbar progress bar , shows when background process running
 * @Param 1stpro list that will later call another layout
 * @Param joinA join team A button
 * @Param joinB join team B button
 * @Param userid string to hold username
 * @Param connectionClass connection parameters from the embedded class
 */
public class Event extends AppCompatActivity {
    protected ProgressBar pbbar;
    protected ConnectionClass connectionClass;
    protected ListView lstpro;
    protected TextView eventlocation;
    protected String eventid;
    protected String eventloc;
    protected String eventde;
    protected String eventaddress;
    protected Button joinA;
    protected Button joinB;
    protected String userid;
    protected String[] ArrayA ;
    protected String[] ArrayB;
    protected String eventdate;
    protected TextView eventdateview;
    protected TextView eventdesc;
    protected TextView eventaddr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*get intent data*/
        Intent Intentextras = getIntent();
        eventid = Intentextras.getStringExtra("eventid");
        userid = Intentextras.getStringExtra("userid");
        setSupportActionBar(toolbar);

        /*************************************************************/
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.VISIBLE);
        lstpro = (ListView) findViewById(R.id.event);
        eventlocation = (TextView) findViewById(R.id.Eventlocation);
        eventdateview = (TextView)findViewById(R.id.eventdate);
        eventdesc = (TextView)findViewById(R.id.eventdesc);
        eventaddr = (TextView)findViewById(R.id.eventaddress);
        joinA = (Button)findViewById(R.id.joina);
        joinB = (Button)findViewById(R.id.joinb);
        connectionClass = new ConnectionClass();
        /*************************************************************/
        FillList fillList = new FillList();
        fillList.execute();
        pbbar.setVisibility(View.GONE);
        /**
         * JoinA button listener
         * This executes the query to join the user to team A in database thus in app
         *
         */
        joinA.setOnClickListener(new View.OnClickListener() {
            String z;
            @Override
            public void onClick(View v) {

        try {
            Connection con = connectionClass.CONN();

            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                String teamalist="";
                for(int i=0;i<ArrayA.length;i++)teamalist = teamalist + ArrayA[i]+",";
                String query = "UPDATE Eventinfo\n" +
                        "SET TeamA='"+teamalist+userid.toString()+"'"+
                        "WHERE Id='"+eventid.toString()+"';";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.executeUpdate();
                z = "Success";
            }
        } catch (Exception ex) {
            z = ex.toString();
        }
        Toast.makeText(Event.this, z, Toast.LENGTH_SHORT).show();

                FillList fillList = new FillList();
                fillList.execute();
            }
        });
        /**
         * JoinB button listener
         * This executes the query to join the user to team B in database thus in app
         *
         */
        joinB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String z;
                try {
                    Connection con = connectionClass.CONN();

                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String teamalist="";
                        for(int i=0;i<ArrayB.length;i++)teamalist = teamalist + ArrayB[i]+",";
                        String query = "UPDATE Eventinfo\n" +
                                "SET TeamB='"+teamalist+userid.toString()+"'"+
                                "WHERE Id='"+eventid.toString()+"';";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Success";
                    }
                } catch (Exception ex) {
                    z = ex.toString();
                }
                Toast.makeText(Event.this, z, Toast.LENGTH_SHORT).show();
                FillList fillList = new FillList();
                fillList.execute();
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

            List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

            @Override
            protected void onPreExecute() {
                pbbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {

                Toast.makeText(Event.this, r, Toast.LENGTH_SHORT).show();
                eventlocation.setText(eventloc);
                eventdateview.setText(eventdate);
                eventdesc.setText(eventde);
                eventaddr.setText(eventaddress);
                String[] from = {"A", "B"};
                int[] views = {R.id.lblproname, R.id.lblprodesc,};
                final SimpleAdapter ADA = new SimpleAdapter(Event.this,
                        prolist, R.layout.eventindividual, from,
                        views);
                lstpro.setAdapter(ADA);
                pbbar.setVisibility(View.GONE);
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

                        String query = "select * from Eventinfo where Id='" + eventid + "'";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        ArrayList data1 = new ArrayList();
                        while(rs.next()!=true);
                        eventloc = rs.getString("Eventname");
                        eventdate = rs.getString("Eventdate");
                        eventde = rs.getString("Descr");
                        eventaddress = rs.getString("Eventlocation");
                        ArrayA = rs.getString("TeamA").split(",");
                        ArrayB = rs.getString("TeamB").split(",");

                        int limit=0;
                        /**
                         * Array fillers with conditions
                         * This puts the data from the arrays to the list
                         */
                        if(ArrayA.length>ArrayB.length)limit=((int) ArrayA.length);
                        else limit=(int)ArrayB.length;
                        for (int i =0;i<limit;i++) {
                            Map<String, String> datanum = new HashMap<String, String>();
                            if(i<ArrayA.length)datanum.put("A", ArrayA[i].toString());
                            if(i<ArrayB.length)datanum.put("B", ArrayB[i].toString());
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
