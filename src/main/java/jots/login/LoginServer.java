package jots.login;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import jots.db.DatabaseServer;
import jots.net.RSA;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;


public class LoginServer {

    private String host = "0.0.0.0";
    private int port = 7171;

    public LoginServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public LoginServer(int port) {
        this.port = port;
    }

    public LoginServer() {
    }

    private void run() {
        try {
            DatabaseServer db = DatabaseServer.getInstance();
            ServerBootstrap bootstrap = new ServerBootstrap(
                    new NioServerSocketChannelFactory(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool()));
            bootstrap.setOption("child.bufferFactory",
                    new HeapChannelBufferFactory(ByteOrder.LITTLE_ENDIAN));
            bootstrap.setPipeline(new LoginServerPipelineFactory().getPipeline());

            bootstrap.bind(new InetSocketAddress(host, port));
        } catch (Exception ex) {
            Logger.getLogger(LoginServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        try {
            // initialize RSA keys
            RSA.init();
            //start the server
            new LoginServer().run();
        } catch (Exception ex) {
            Logger.getLogger(LoginServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
