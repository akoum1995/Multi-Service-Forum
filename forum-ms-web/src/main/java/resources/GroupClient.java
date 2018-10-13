package resources;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import iservices.IGroupServiceLocal;
import persistance.Group;


@Path("/group")
@RequestScoped
public class GroupClient {
	@Inject
	IGroupServiceLocal groupManager ;
	@Path("/addGroup")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes()
	public Response addGroup(
			@QueryParam("idUser") int idUser,
			@QueryParam("groupName") String groupName
			)
	{
		Group group = new Group(groupName);
		return Response.ok(groupManager.addGroup(group, idUser)).build();
	
	}
	@Path("/getAllGroups")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllGroups() 
	{
	    return Response.ok(groupManager.getAllGroup()).build();
	}
}
