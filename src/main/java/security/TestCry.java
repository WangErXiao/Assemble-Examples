package security;

/**
 * Created by yaozb on 15-4-5.
 */
public class TestCry {
    public static void main(String[]args){
        CryptixCryptoLoader cryptixCryptoLoader=new CryptixCryptoLoader();
        CryClassUtils utils=new CryClassUtils("Blowfish","Blowfish/CBC/NoPadding","wangerxiao","CryptixCrypto");
        //CryClassUtils utils=new CryClassUtils("AES","AES/CBC/NoPadding","wangerxiao","CryptixCrypto");
        String encryptStr=utils.encrypt("hello world");
        System.out.println(encryptStr);
        System.out.println(utils.decrypt(encryptStr));
    }
}
