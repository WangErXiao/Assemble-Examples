package security;

/**
 * Created by root on 15-4-5.
 */
public class CryptixCryptoLoader {
    static{
        java.security.Security.addProvider(new cryptix.jce.provider.CryptixCrypto());
    }
}
