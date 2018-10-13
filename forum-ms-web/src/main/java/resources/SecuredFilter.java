package resources;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Secured
public class SecuredFilter implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		System.out.println("request filter invoked...");

		// Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if (authorizationHeader == null || authorizationHeader.startsWith("Bearer ")==false) {
        	    System.out.println("No JWT token !"+authorizationHeader);
        	 //   requestContext.setProperty("auth-failed", true);
        	    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("No JWT token !"+authorizationHeader).build());			
        }
        	else
        	{
        		
        			
		        final String token = authorizationHeader.substring(7); // The part after "Bearer "
		        try {
		        	
					//final String subject = tokenService.validateToken(token);
		        		final String subject = validateToken(token);
					if (subject==null)
					{	
						
						System.out.println("No valid JWT token !"+authorizationHeader);
						requestContext.setProperty("auth-failed", true);
						requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("No valid JWT token !"+authorizationHeader).build());
					}
					else{
						final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
						requestContext.setSecurityContext(new SecurityContext() {

						    @Override
						    public Principal getUserPrincipal() {

						        return new Principal() {

						            @Override
						            public String getName() {
						                return subject;
						            }
						        };
						    }

						    @Override
						    public boolean isUserInRole(String role) {
						        return true;
						    }

						    @Override
						    public boolean isSecure() {
						        return currentSecurityContext.isSecure();
						    }

						    @Override
						    public String getAuthenticationScheme() {
						        return "Bearer";
						    }
						});
					}						
				} catch (Exception e) {			
					requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
				}
		       
	            
        
        }
		}
	    	
	
	
	private String validateToken(String jwt) {
			final Key KEY = MacProvider.generateKey();
			System.out.println("GENERAREKey"+KEY);
			Claims claims = Jwts.parser().setSigningKey("test").parseClaimsJws(jwt).getBody();
			return (String)claims.get("id");
	}
	

}
