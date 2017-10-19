/**
 * @author Baris Tanyeri ezybt3@nottingham.ac.uk
 * @version 3.0
 */
package test.events;


import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection class that establishes the connection between the SQL server
 * Takes no arguments since these parameters won't change until the server parameters change.
 * Parameters are embedded in the class
 * @Param ip server ip
 * @Param classs SQL connection library
 * @Param db database name
 * @Param un username to database usually "sa"
 * @Param password server admin password
 */
public class ConnectionClass {
    //String ip = "10.154.37.43"; //SCHOOL
    //String ip = "10.95.207.25"; //HOME
    String ip = "192.168.1.23";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "userinfo";
    String un = "sa";
    String password = "hardcore12";
    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}
