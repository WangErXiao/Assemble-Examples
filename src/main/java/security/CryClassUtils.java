package security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by root on 15-4-5.
 */
public class CryClassUtils {
    private static String blank = "            ";
    private static class MyCrypto{
        private Cipher enCipher;
        private Cipher deCipher;
        private SecretKeySpec secretKey;

        public MyCrypto(String algorithm,String transformation, String key,String provider) {
           try {
               secretKey = new SecretKeySpec(key.getBytes(), algorithm);
               if (provider == null) {
                   enCipher = Cipher.getInstance(transformation);
                   deCipher = Cipher.getInstance(transformation);
               }else {
                   enCipher =Cipher.getInstance(transformation,provider);
                   deCipher =Cipher.getInstance(transformation,provider);
               }
               enCipher.init(Cipher.ENCRYPT_MODE,secretKey);
               deCipher.init(Cipher.DECRYPT_MODE,secretKey);
           }catch (Exception e){
               e.printStackTrace();
           }
        }
        public byte[]encrypt(byte[]bytes) {
            try {
                return enCipher.doFinal(bytes);
            } catch (IllegalBlockSizeException e) {
               throw  new RuntimeException(e);
            } catch (BadPaddingException e) {
                throw new RuntimeException(e);
            }
        }
        public  byte[]decrypt(byte[]bytes){
            try {
                byte[] result = deCipher.doFinal(bytes);
                return result;
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
    private String algorithm;
    private String tranformation;
    private String key;
    private String provider;
    private ThreadLocal<MyCrypto> local=new ThreadLocal<MyCrypto>();

    public CryClassUtils(String algorithm, String tranformation, String key, String provider) {
        this.algorithm = algorithm;
        this.tranformation = tranformation;
        this.key = key;
        this.provider = provider;
    }

    private MyCrypto getLocal(){
         MyCrypto myCrypto=local.get();
         if(myCrypto==null){
             myCrypto=new MyCrypto(algorithm, tranformation, key, provider);
             local.set(myCrypto);
         }
         return myCrypto;
    }
    public String encrypt(String s){
        s = blank + s + blank + blank.substring(0, (8 - s.length() % 8) % 8);
        byte[]bytes=getLocal().encrypt(s.getBytes());
        return Base64Utils.encode(bytes);
    }
    public String decrypt(String s){
        if (s == null || s.isEmpty()) {
            throw new NullPointerException("null string");
        }
        try {
            byte[]bs = getLocal().decrypt(Base64Utils.decode(s.toCharArray()));
            String back = null;
            back = new String(bs);
            back = back.substring(8, back.length()).trim();
            return back;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
