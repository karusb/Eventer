package test.events;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {


        ConnectionClass connectionClass;
        EditText edtuserid;
        EditText edtpass;
        Button btnlogin;
        Button btnregister;
        ProgressBar pbbar;
        String usrn;
        String pswd;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login2);
            connectionClass = new ConnectionClass();
            edtuserid = (EditText) findViewById(R.id.usr);
            edtpass = (EditText) findViewById(R.id.pwd);
            btnlogin = (Button) findViewById(R.id.signbutton);
            btnregister = (Button)findViewById(R.id.registerbutton);
            pbbar = (ProgressBar) findViewById(R.id.pbbar);
            pbbar.setVisibility(View.GONE);
            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usrn = edtuserid.getText().toString();
                    pswd = edtpass.getText().toString();
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute("");
                }
            });
            btnregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent("test.events.Register");
                    startActivity(i);
                }
            });
        }
        public class DoLogin extends AsyncTask<String,String,String>
        {
            String z = "";
            Boolean isSuccess = false;

            @Override
            protected void onPreExecute() {
                pbbar.setVisibility(View.VISIBLE);
            }
            @Override
            protected void onPostExecute(String r) {
                pbbar.setVisibility(View.GONE);
                Toast.makeText(Login.this, r, Toast.LENGTH_SHORT).show();
                if(isSuccess) {
                    Intent i = new Intent("test.events.MainActivity");
                    Bundle bundle = new Bundle();
                    bundle.putString("username", usrn);
                    bundle.putString("password",pswd);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                if(usrn.trim().equals("")|| pswd.trim().equals(""))
                    z = "Please enter User Id and Password";
                else
                {
                    try {
                        Connection con = connectionClass.CONN();
                        if (con == null) {
                            z = "Error in connection with SQL server";
                        } else {
                            String query = "select * from Username where UserId='" + usrn + "' and password='" + pswd + "'";
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if(rs.next())
                            {
                                z = "Login successfull";
                                isSuccess=true;
                            }
                            else
                            {
                                z = "Invalid Credentials";
                                isSuccess = false;
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        isSuccess = false;
                        z = ex.toString();
                    }
                }
                return z;
            }
        }
    }

