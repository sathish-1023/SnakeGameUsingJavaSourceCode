/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cls;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author bagong
 */
public class ClassDB {
    private static Connection conn;
    private static String str="";
    public static Connection dbConnect() {
        if (conn==null) {
            try {
                String url=new String();
                String user=new String();
                String password=new String();
                url="jdbc:mysql://localhost:3306/snake";
                user="root";
                password="";
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                conn=DriverManager.getConnection(url,user,password);
                
               // JOptionPane.showMessageDialog(null,"Koneksi Berhasil");
               // str = "Create table score (nama varchar(20) not null,score int(6);";
                //Statement stmt=(Statement)conn.createStatement();
                //stmt.execute(str);
                      //  JOptionPane.showMessageDialog(null,"score table is created");

            }catch (SQLException t) {
                JOptionPane.showMessageDialog(null,"Error in Connecting the snake database");
                System.exit(0);
            }
        }
     return conn;
    }
}
