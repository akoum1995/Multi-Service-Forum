package resources;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import iservices.IUserManagerLocal;
import persistance.Device;
import persistance.User;
import persistance.UserGender;

@Path("/user")
@RequestScoped
public class UserClient {
	@Inject
	IUserManagerLocal userManager ;
	private final String UPLOADED_FILE_PATH = "E:\\test\\";
	//XXX *********************getUserById*********************************
	@Path("/getUserById/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@PathParam("id") int id)
	{
		User user=userManager.getUserById(id);
		if(user!=null)
		{
			return Response.ok(user).build();
		}
			
		return Response.status(Status.NO_CONTENT).build();
	}
	//XXX **********************getUserByUsername********************************
	@Path("/getUserByUsername/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByUsername(@PathParam("username") String username)
	{
		User user=userManager.getUserByUsername(username);
		if(user!=null)
		{
			return Response.ok(user).build();
		}
			
		return Response.status(Status.NO_CONTENT).build();
	}
	//XXX *************************getUserByEmail*****************************
	@Path("/getUserByEmail/{email}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByEmail(@PathParam("email") String email)
	{
		User user=userManager.getUserByEmail(email);
		if(user!=null)
		{
			return Response.ok(user).build();
		}
			
		return Response.status(Status.NO_CONTENT).build();
	}
	//XXX **************************quickSignup****************************
	@Path("/addUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response quickSignup(
			@Context HttpServletRequest req,
			@QueryParam("email") String email,
			@QueryParam("username") String username,
			@QueryParam("password") String password
			)
	{
		
		User user = new User(email,username,password);
		String tab[]=getOsBrowserUser(req.getHeader("User-Agent"));
		String remoteHost = req.getRemoteHost();
		Device device = new Device(user,tab[0],tab[1],remoteHost,Boolean.FALSE);
		return Response.ok(userManager.quickSignup(user,device)).build();
	}
	//XXX ****************************login**************************
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(
			@Context HttpServletRequest req,
			@QueryParam("usernameOrEmail") String username,
			@QueryParam("password") String password
			)
	{
		GenerateToken generateToken = new GenerateToken();
		String tab[]=getOsBrowserUser(req.getHeader("User-Agent"));
		String remoteHost = req.getRemoteHost();
		Device device = new Device();
		device.setOs(tab[0]);
		device.setBrowser(tab[1]);
		device.setConnected(Boolean.FALSE);
		device.setIp(remoteHost);
		JsonObject jsonObject =userManager.login(username, password,device);
		
		if(jsonObject.containsKey("error"))
		{
			return Response.ok(jsonObject).build();
		}
		else
		{
			jsonObject=jsonObjectToBuilder(jsonObject).add("token", generateToken.issueToken(username)).build();
			return Response.ok(jsonObject).build();
		}
		
	}
	//XXX ************************logout******************************
	@Path("/logout/{idUser}/{idDevice}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(
			@PathParam("idUser") int idUser,
			@PathParam("idDevice") int idDevice
			) {
	    return Response.ok(userManager.logout(idUser,idDevice)).build();
	}
	//XXX **********************isConnected********************************
	@Path("/isConnected/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response isConnected(@PathParam("id") int id)
	{
		return Response.ok(userManager.isConnected(id)).build();
	}
	//XXX **********************isConnected********************************
		@Path("/isConnected/{usernameOrEmail}")
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response isConnected(@PathParam("usernameOrEmail") String usernameOrEmail)
		{
			return Response.ok(userManager.isConnected(usernameOrEmail)).build();
		}
	//XXX ************************enable******************************
	@Path("/enableUser/{username}/{token}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response enableUser(@PathParam("username") String username,@PathParam("token") String token)
	{
		return Response.ok(userManager.enableUser(username, token)).build();
	}
	//XXX ************************enable******************************
	@Path("/disableUser/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response disableUser(@PathParam("id") int id)
	{
		return Response.ok(userManager.disableUser(id)).build();
	}
	//XXX *************************changePicture*****************************
	@POST
	@Path("/changePicture")
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(
			MultipartFormDataInput input
			) {

		String fileName = "";
		int id=0;
		try {
			id = input.getFormDataPart("id", Integer.class, null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		
		for (InputPart inputPart : inputParts) {

		 try {

			MultivaluedMap<String, String> header = inputPart.getHeaders();
			fileName = getFileName(header);

			//convert the uploaded file to inputstream
			InputStream inputStream = inputPart.getBody(InputStream.class,null);

			byte [] bytes = IOUtils.toByteArray(inputStream);

			//constructs upload file path
			fileName = UPLOADED_FILE_PATH + fileName;

			writeFile(bytes,fileName);

		  } catch (IOException e) {
			e.printStackTrace();
		  }

		}
		System.out.println(id);
		return Response.status(200)
		    .entity(userManager.chnageProfilPicture(id, fileName)).build();

	}
	//XXX ************************changePassword******************************
	@Path("/changePassword")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes()
	public Response changePassword(
			@QueryParam("id") int id,
			@QueryParam("currentPwd") String currentPwd,
			@QueryParam("newPwd") String newPwd
			)
	{
		
		return Response.ok(userManager.changePassword(id, currentPwd, newPwd)).build();
	}
	//XXX ***********************getAllUsers******************************
	
	@Path("/getAllUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUser()
	{
		return Response.ok(userManager.getAllUser()).build();
	}
	//XXX ************************getEnabledUsers******************************
	
		@Path("/getEnabledUsers")
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response getEnabledUsers()
		{
			return Response.ok(userManager.getEnabledUsers()).build();
		}
	//XXX ************************getDisableUsers******************************
		
			@Path("/getDisableUsers")
			@GET
			@Produces(MediaType.APPLICATION_JSON)
			public Response getDisableUsers()
			{
				return Response.ok(userManager.getEnabledUsers()).build();
			}
	//XXX ************************updateFirstname******************************
			
			@Path("/updateFirstname")
			@Secured
			@PUT
			@Produces(MediaType.APPLICATION_JSON)
			public Response updateFirstname
					(
							@QueryParam("firstname") String firstname,
							@Context SecurityContext sc
					)
			{
				String usernameOrEmail=sc.getUserPrincipal().getName();
				User user=userManager.getUserByUsername(usernameOrEmail);
				if(user==null)
				{
					user=userManager.getUserByEmail(usernameOrEmail);
				}
				return Response.ok(userManager.updateFirstname(user,firstname)).build();
			}
//XXX ************************updateLastname******************************
			
			@Path("/updateLastname")
			@Secured
			@PUT
			@Produces(MediaType.APPLICATION_JSON)
			public Response updateLastname
					(
							@QueryParam("lastname") String lastname,
							@Context SecurityContext sc
					)
			{
				String usernameOrEmail=sc.getUserPrincipal().getName();
				User user=userManager.getUserByUsername(usernameOrEmail);
				if(user==null)
				{
					user=userManager.getUserByEmail(usernameOrEmail);
				}
				return Response.ok(userManager.updateLastname(user,lastname)).build();
			}
//XXX ************************updateLastname******************************
			
			@Path("/updateBirthDate")
			@Secured
			@PUT
			@Produces(MediaType.APPLICATION_JSON)
			public Response updateBirthDate
					(
							@QueryParam("BirthDate") Date BirthDate,
							@Context SecurityContext sc
					)
			{
				String usernameOrEmail=sc.getUserPrincipal().getName();
				User user=userManager.getUserByUsername(usernameOrEmail);
				if(user==null)
				{
					user=userManager.getUserByEmail(usernameOrEmail);
				}
				return Response.ok(userManager.updateBirthDate(user,BirthDate)).build();
			}	
//XXX ************************updatePhoneNumber******************************
			
			@Path("/updatePhoneNumber")
			@Secured
			@PUT
			@Produces(MediaType.APPLICATION_JSON)
			public Response updatePhoneNumber
					(
							@QueryParam("phoneNumber") String phoneNumber,
							@Context SecurityContext sc
					)
			{
				String usernameOrEmail=sc.getUserPrincipal().getName();
				User user=userManager.getUserByUsername(usernameOrEmail);
				if(user==null)
				{
					user=userManager.getUserByEmail(usernameOrEmail);
				}
				return Response.ok(userManager.updatePhoneNumber(user,phoneNumber)).build();
			}	
//XXX ************************updateGender******************************
			
			@Path("/updateGender")
			@Secured
			@PUT
			@Produces(MediaType.APPLICATION_JSON)
			public Response updateGender
					(
							@QueryParam("userGender") String userGender,
							@Context SecurityContext sc
					)
			{
				String usernameOrEmail=sc.getUserPrincipal().getName();
				User user=userManager.getUserByUsername(usernameOrEmail);
				if(user==null)
				{
					user=userManager.getUserByEmail(usernameOrEmail);
				}
				return Response.ok(userManager.updateGender(user,UserGender.valueOf(userGender))).build();
			}
//XXX ************************getUsersAbleModerator******************************			
			
			@Path("/getUsersAbleModerator")
			@GET
			@Produces(MediaType.APPLICATION_JSON)
			public Response getUsersAbleModerator() 
			{
			    return Response.ok(userManager.getUsersAbleModerator()).build();
			}				
//XXX ************************getUsersAbleSectionAdministrator******************************			
			
			@Path("/getUsersAbleSectionAdministrator")
			@GET
			@Produces(MediaType.APPLICATION_JSON)
			public Response getUsersAbleSectionAdministrator() 
			{
			    return Response.ok(userManager.getUsersAbleSectionAdministrator()).build();
			}				
//XXX ************************getUsersAbleSubSectionAdministrator******************************			
			
			@Path("/getUsersAbleSubSectionAdministrator")
			@GET
			@Produces(MediaType.APPLICATION_JSON)
			public Response getUsersAbleSubSectionAdministrator() 
			{
			    return Response.ok(userManager.getUsersAbleSubSectionAdministrator()).build();
			}				

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//utils
	/**
	 * header sample
	 * {
	 * 	Content-Type=[image/png],
	 * 	Content-Disposition=[form-data; name="file"; filename="filename.extension"]
	 * }
	 **/
	//get uploaded filename, is there a easy way in RESTEasy?
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	//save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
	private String[] getOsBrowserUser(String browserDetails)
	{
	        String  userAgent       =   browserDetails;
	        String  user            =   userAgent.toLowerCase();

	        String os = "";
	        String browser = "";

	        //=================OS=======================
	         if (userAgent.toLowerCase().indexOf("windows") >= 0 )
	         {
	             os = "Windows";
	         } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
	         {
	             os = "Mac";
	         } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
	         {
	             os = "Unix";
	         } else if(userAgent.toLowerCase().indexOf("android") >= 0)
	         {
	             os = "Android";
	         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
	         {
	             os = "IPhone";
	         }else{
	             os = "UnKnown, More-Info: "+userAgent;
	         }
	         //===============Browser===========================
	        if (user.contains("msie"))
	        {
	            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
	            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
	        } else if (user.contains("safari") && user.contains("version"))
	        {
	            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
	        } else if ( user.contains("opr") || user.contains("opera"))
	        {
	            if(user.contains("opera"))
	                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
	            else if(user.contains("opr"))
	                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
	        } else if (user.contains("chrome"))
	        {
	            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
	        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
	        {
	            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
	            browser = "Netscape-?";

	        } else if (user.contains("firefox"))
	        {
	            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
	        } else if(user.contains("rv"))
	        {
	            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
	        } else
	        {
	            browser = "UnKnown, More-Info: "+userAgent;
	        }
	        String[] tabs = {os,browser};
	        return tabs;
	       	
	}
	private JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) {
	    JsonObjectBuilder job = Json.createObjectBuilder();

	    for (Entry<String, JsonValue> entry : jo.entrySet()) {
	    	if(entry!=null&&entry.getKey()!=null&&entry.getValue()!=null)
	    	{
	    		job.add(entry.getKey(), entry.getValue());
	    	}
	    }

	    return job;
	}
	//test
	@Path("/testSecurity")
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response testSecurity(@Context SecurityContext sc)
	{
		sc.getUserPrincipal().getName();
		return Response.ok(sc.getUserPrincipal().getName()).build();
	}
	@Path("/ip")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getIp(@Context HttpServletRequest req) {
	    String tab[]=getOsBrowserUser(req.getHeader("User-Agent"));
	    return Response.ok("OS: "+tab[0]+" Bowser: "+tab[1]).build();
	}
}
