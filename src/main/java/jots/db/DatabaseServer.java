package jots.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import javax.sql.DataSource;

public class DatabaseServer {
    private DataSource myDataSource;
    public static final int POOLSIZE = 10;
    public static final String CONNSTR = "jdbc:mysql://localhost/tfs?user=root";
    Queue <Connection> free = new LinkedList<Connection>();
    
    static DatabaseServer instance = null;
    public static DatabaseServer getInstance()
    {
        if(instance == null)
            instance = new DatabaseServer();
        return instance;
    }
    private DatabaseServer()
    {
        Connection conn;
        for(int i=0; i<10; i++)
        {
            try {
                conn = DriverManager.getConnection(CONNSTR);
                free.add(conn);
            }
            catch(SQLException ex)
            {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
        System.out.println("Created 10 connections.");
    }
    
    public Connection getConnection() throws Exception
    {
        
        if(!free.isEmpty())
            return free.poll();
        else
            throw new Exception("No connections in pool");
    }
    
    public void returnConnection(Connection conn)
    {
        free.add(conn);
    }
    
    
}
