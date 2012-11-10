package jots.net;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;

public class RSA_1 {

    private static final Logger logger = Logger.getLogger(
            RSA.class.getName());
    static BigInteger otServerP = new BigInteger("14299623962416399520070177382898895550795403345466153217470516082934737582776038882967213386204600674145392845853859217990626450972452084065728686565928113", 10);
    static BigInteger otServerQ = new BigInteger("7630979195970404721891201847792002125535401292779123937207447574596692788513647179235335529307251350570728407373705564708871762033017096809910315212884101", 10);
    static BigInteger otServerD = new BigInteger("46730330223584118622160180015036832148732986808519344675210555262940258739805766860224610646919605860206328024326703361630109888417839241959507572247284807035235569619173792292786907845791904955103601652822519121908367187885509270025388641700821735345222087940578381210879116823013776808975766851829020659073", 10);
    static BigInteger otServerM = new BigInteger("109120132967399429278860960508995541528237502902798129123468757937266291492576446330739696001110603907230888610072655818825358503429057592827629436413108566029093628212635953836686562675849720620786279431090218017681061521755056710823876476444260558147179707119674283982419152118103759076030616683978566631413", 10);
    static BigInteger otServerE = new BigInteger("65537", 10);
    static BigInteger otServerDP = new BigInteger("11141736698610418925078406669215087697114858422461871124661098818361832856659225315773346115219673296375487744032858798960485665997181641221483584094519937", 10);
    static BigInteger otServerDQ = new BigInteger("4886309137722172729208909250386672706991365415741885286554321031904881408516947737562153523770981322408725111241551398797744838697461929408240938369297973", 10);
    static BigInteger otServerInverseQ = new BigInteger("5610960212328996596431206032772162188356793727360507633581722789998709372832546447914318965787194031968482458122348411654607397146261039733584248408719418", 10);
    static BigInteger cipM = new BigInteger("132127743205872284062295099082293384952776326496165507967876361843343953435544496682053323833394351797728954155097012103928360786959821132214473291575712138800495033169914814069637740318278150290733684032524174782740134357629699062987023311132821016569775488792221429527047321331896351555606801473202394175817", 10);
    static BigInteger cipE = new BigInteger("65537", 10);
    static boolean init = false;
    static KeyFactory keyFactory;
    static RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(otServerM, otServerE);
    static RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(otServerM, otServerD);
    static Cipher cipher;
    static RSAPublicKey pubKey;
    static RSAPrivateKey privKey;
    //gnu.crypto.key.rsa.GnuRSAPrivateKey priv = new gnu.crypto.key.rsa.GnuRSAPrivateKey(otServerP, otServerQ, otServerM, otServerD);
    //gnu.crypto.key.rsa.GnuRSAPublicKey pub = new gnu.crypto.key.rsa.GnuRSAPublicKey(cipM, cipM)

    public static void init() throws Exception {
        if (init == true) {
            return;
        }
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());


        cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");

        keyFactory = KeyFactory.getInstance("RSA", "BC");
        pubKeySpec = new RSAPublicKeySpec(otServerM, otServerE);
        privKeySpec = new RSAPrivateKeySpec(otServerM, otServerD);

        pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
        privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

        init = true;
    }

    public static void encrypt(byte[] buffer, int position) {
        try {
            init();
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] temp = new byte[128];
            System.out.println(temp.length + " " + position + " " + buffer.length);
            System.arraycopy(buffer, position, temp, 0, 128);

            temp = cipher.doFinal(temp);
            System.arraycopy(temp, 0, buffer, position, 128);

        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] decrypt(byte[] buffer) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, Exception {
        init();
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        // if(length-position != 128)
        //    throw new Exception ("Message is not 128 length: "+buffer.length+" given:"+length+" position: "+position);
       
        byte[] tmp = new byte[128];
        tmp = cipher.doFinal(buffer);
        
        return tmp;


    }

    public static byte[] getPaddedValue(BigInteger value) {
        byte[] result = value.toByteArray();
        int length = (1024 >> 3);
        if (result.length >= length) {
            return result;
        }
        byte[] padded = new byte[length];
        System.arraycopy(result, 0, padded, (length - result.length),
                result.length);

        return padded;
    }
}
