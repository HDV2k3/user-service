package com.user.identity.configuration;

import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.user.identity.controller.dto.request.IntrospectRequest;
import com.user.identity.service.Impl.AuthenticationServiceImpl;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthenticationServiceImpl authenticationServiceImpl;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            var response = authenticationServiceImpl.introspect(
                    IntrospectRequest.builder().token(token).build());

            if (!response.isValid()) throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
//package com.user.identity.configuration;
//
//import java.text.ParseException;
//import java.util.Objects;
//import javax.crypto.spec.SecretKeySpec;
//
//import com.nimbusds.jwt.SignedJWT;
//import com.user.identity.exception.AppException;
//import com.user.identity.exception.ErrorCode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.stereotype.Component;
//
//import com.nimbusds.jose.JOSEException;
//import com.user.identity.controller.dto.request.IntrospectRequest;
//import com.user.identity.service.Impl.AuthenticationServiceImpl;
//
//@Component
//public class CustomJwtDecoder implements JwtDecoder {
//    /**
//     * Decodes a JWT token by parsing it into a SignedJWT and extracting claims from it.
//     *
//     * @param token the JWT token as a string
//     * @return Jwt a decoded Jwt object containing the token's claims and metadata
//     * @throws JwtException if the token is invalid or cannot be parsed
//     */
//    @Override
//    public Jwt decode(String token) throws JwtException {
//        try {
//            // Parse the token into a SignedJWT object to extract claims
//            SignedJWT signedJWT = SignedJWT.parse(token);
//
//            // Return a Jwt object containing the token, issue time, expiration time, header, and claims
//            return new Jwt(
//                    token,
//                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
//                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
//                    signedJWT.getHeader().toJSONObject(),
//                    signedJWT.getJWTClaimsSet().getClaims()
//            );
//        } catch (ParseException e) {
//            // Throw a JwtException if the token is invalid or cannot be parsed
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }
//    }
//}
