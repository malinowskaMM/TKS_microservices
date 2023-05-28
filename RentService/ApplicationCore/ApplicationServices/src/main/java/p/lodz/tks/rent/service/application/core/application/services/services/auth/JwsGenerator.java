package p.lodz.tks.rent.service.application.core.application.services.services.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import javax.enterprise.context.ApplicationScoped;
import java.text.ParseException;

@ApplicationScoped
public class JwsGenerator {

    private static final String SECRET = "f4h9t87t3g473HGufuJ8fFHU4j39j48fmu948cx48cu2j9fj";

    public String generateJws(String payload) throws JOSEException {
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payload));
        JWSSigner signer = new MACSigner(SECRET);
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public boolean verify(String payload) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(payload);
        JWSVerifier verifier = new MACVerifier(SECRET);
        return jwsObject.verify(verifier);
    }


}
