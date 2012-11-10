package jots.login;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import jots.db.DatabaseServer;
import jots.net.*;
import jots.packet.out.login.CharacterList;
import jots.packet.out.login.Disconnect;
import jots.packet.out.login.MOTD;
import jots.packet.out.login.OutLoginPacket;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.buffer.LittleEndianHeapChannelBuffer;
import org.jboss.netty.channel.*;

public class LoginServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = Logger.getLogger(
            LoginServerHandler.class.getName());

    @Override
    public void handleUpstream(
            ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            logger.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }
    
    private void helper(Object... n)
    {
        /*int i = 0;
        String s = "";
        for(Object x : n)
        {
                s += (i++ +":"+x.toString()+", ");
            
        }
        logger.info(s);*/
    }
    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        
        //logger.info(">msg received");
        NetworkMessage buf = new NetworkMessage((LittleEndianHeapChannelBuffer) e.getMessage());
        int size = buf.readShort();
        
        long adler = buf.readInt();
        short type = buf.readByte();
        int os = buf.readShort();
        int ver = buf.readShort();
        
        long[] checksum = {buf.readInt(),buf.readInt(),buf.readInt() };
        int[] xtea = new int[4];
        byte[] xkey;
        int[] k;
        this.helper(size, adler, type, os, ver, checksum);
        try {
            byte[] toDecrypt = new byte[128];
            buf.readBytes(toDecrypt);
            byte[] decrypted = RSA.decrypt(toDecrypt);
            //logger.info("DECRYPTED: "+Arrays.toString(decrypted));
            buf.readerIndex(151-128);
            buf.setBytes(151-128, decrypted);
            xkey = new byte[16];
            /*for(int i=0; i<16; i++)
                xkey[i] = buf.getBuffer().readByte();
*/
            
            k = new int[]{buf.getBuffer().readInt(), buf.getBuffer().readInt(), 
                buf.getBuffer().readInt(), buf.getBuffer().readInt()};
            
            XTEA2.unpackInt((int)k[3], xkey, 0);
            XTEA2.unpackInt((int)k[2], xkey, 4);
            XTEA2.unpackInt((int)k[1], xkey, 8);
            XTEA2.unpackInt((int)k[0], xkey, 12);
            XTEA2 xtea2 = new XTEA2(k);
            String test = "01234567";
            byte[] enc = xtea2.mencrypt(test.getBytes());
            System.out.println(new String(enc));
            System.out.println(new String(xtea2.mdecrypt(enc)));
            System.out.println(Arrays.toString(xkey));
            System.out.println(Arrays.toString(k));
            int strLength = buf.readShort();
            byte[] strBuff = new byte[strLength];
            for(int i=0; i<strLength; i++)
                strBuff[i] = (byte)buf.readByte();
            String acc = new String(strBuff);
            strLength = buf.readShort();
            strBuff = new byte[strLength];
            for(int i=0; i<strLength; i++)
                strBuff[i] = (byte)buf.readByte();
            String pass = new String(strBuff);
           // logger.info(">>>CREDENTIALS: "+acc+ ", "+pass);
            
            DatabaseServer db = DatabaseServer.getInstance();
            Connection conn = db.getConnection();
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM accounts WHERE `name` = '"+acc+"' AND `password` = '"+pass+"'");
                
                while(rs.next())
                {
                    
                    System.out.println("ACC: " +rs.getString("name") + " " +rs.getInt("id"));
                }
            }
            catch(SQLException ex) {
                 // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
            finally {
                if(rs != null) {
                    try {
                        rs.close();
                    } catch(SQLException sqlEx ) {}
                }
                rs = null;
                
                if(stmt != null)
                {
                    try {
                        stmt.close();
                    } catch(SQLException sqlEx) {}
                    stmt = null;
                }
            }
            Channel ch = e.getChannel();
          
            
            String[] name = {acc};
            String[] world = {pass};
            int[] ip = {0};
            short[] port = {1};
            
            OutLoginPacket out = new CharacterList(name, world, ip, port);
            out.prepare(k);
            
            
            
            ChannelFuture f = ch.write(out.getBuffer());
            //ChannelFuture f2 = ch.write(motd.getBuffer());
            f.addListener(new ChannelFutureListener() {
                
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                int rc;
               //logger.info(" WRITTEN");
            }
            });
            

        } catch (Exception ex) {
            Logger.getLogger(LoginServerHandler.class.getName()).log(Level.SEVERE, null, ex);
           e.getChannel().close();
        }
        
        
    }
    
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
    ChannelStateEvent e) throws Exception {
        //logger.info("DC");
    }
    
    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.log(Level.WARNING, "Unexpeted exception from downstream",
                e.getCause());
        e.getChannel().close();
    }
}
