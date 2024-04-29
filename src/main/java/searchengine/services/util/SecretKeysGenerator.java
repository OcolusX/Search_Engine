package searchengine.services.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

public class SecretKeysGenerator {

    public static void main(String[] args) {
        System.out.println(generateSecretKey());
        System.out.println(generateSecretKey());
    }

    private static String generateSecretKey() {
        return Encoders.BASE64.encode(Jwts.SIG.HS256.key().build().getEncoded());
    }

}
