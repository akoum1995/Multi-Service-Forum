package resources;



import javax.ejb.Stateless;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Stateless
public class GenerateToken {
	
	public String issueToken(String login) 
		{
    	return Jwts.builder().claim("id", login).signWith(SignatureAlgorithm.HS512, "test")//.setExpiration(expiracion)
                .compact();
        }	

}
