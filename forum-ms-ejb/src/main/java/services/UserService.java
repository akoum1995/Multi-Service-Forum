package services;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import iservices.IDeviceServiceLocal;
import iservices.IUserManagerLocal;
import persistance.Device;
import persistance.User;
import persistance.UserGender;
import utils.Mail;
import utils.Utils;

@Stateless
public class UserService implements IUserManagerLocal
{	
	@PersistenceContext(unitName="forumMS")
	EntityManager entityManager;
	@EJB
	Mail mail;
	@EJB
	IDeviceServiceLocal deviceService;
	public User getUserById(int id)
	{
		return entityManager.find(User.class, id);
	}
	public JsonObject enableUser(String username,String token)
	{
		User user =this.verifToken(username, token);
		if(user!=null)
		{
			user.setToken(null);
			user.setIsEnabled(Boolean.TRUE);
			entityManager.persist(user);
			entityManager.flush();
			return Json.createObjectBuilder().add("succes", "your account has been activated successfully").build();
		}
		else
		{
			return Json.createObjectBuilder().add("error", "your username or your token is not correct").build();
		}
		
	}
	public JsonObject quickSignup(User user,Device device) 
		{
		JsonObjectBuilder errorBuilder=Json.createObjectBuilder();
		if(Utils.emailValidator(user.getEmail()))
		{
			if(this.isExistEmail(user.getEmail()))
			{
				errorBuilder.add("error", "the email address is allready exist");
				return errorBuilder.build();
			}
			else if(this.isExistUsername(user.getUsername()))
			{
				errorBuilder.add("error", "the username is allready exist");
				return errorBuilder.build();
			}
			else if(!this.isValidPassword(user.getPassword()))
			{
				errorBuilder.add("error", "the password is too weak");
				return errorBuilder.build();
			}
		}
		else
		{
			errorBuilder.add("error", "your email address is not valid");
			return errorBuilder.build();
		}
			
			try {
				user.setPassword(Utils.toMD5(user.getPassword()));
			} catch (NoSuchAlgorithmException e) {
				errorBuilder.add("error", "the password is too weak");
				return errorBuilder.build();
				
			}
			user.setToken(Utils.tokenGenerator());
			
			entityManager.persist(user);
			entityManager.flush();
			device.setOwner(user);
			deviceService.addDevice(device);
			JsonObjectBuilder succesBuilder=Json.createObjectBuilder();
			succesBuilder.add("succes", "quick signup successfully completed");
			succesBuilder.add("user_id",user.getId());
			String path = "http://localhost:18080/forum-ms-web/v0/user/enable/"+user.getUsername()+"/"+user.getToken();
			mail.send(
					user.getEmail()
					, "Quick singup"
					,Utils.getValidationEmail(path)
					, "your account has been successfully created please activate it now <br> http://localhost:18080/forum-ms-web/v0/user/enable/"+user.getUsername()+"/"+user.getToken()
					,path
					);
			return succesBuilder.build();
			
		}
	
	public JsonObject login(String usernameOrEmail, String password,Device device)
	{
		try {
			
			password=Utils.toMD5(password);
		} catch (NoSuchAlgorithmException e) {
			return Json.createObjectBuilder().add("error", "your password is not correct").build();
		}
		if(isExistEmail(usernameOrEmail))
		{
			User user =loginEmail(usernameOrEmail,password);
			if(user!=null)
			{
				if(user.getIsEnabled())
				{
					user.setConnected(Boolean.TRUE);
					user.setLastConnect(new Date());
					entityManager.persist(user);
					entityManager.flush();
					device.setOwner(user);
					device.setConnected(Boolean.TRUE);
					device.setLastConnection(new Date());
					deviceService.addDeviceOrSetconnected(device);
					return Json.createObjectBuilder()
							.add("succes", "you have been logged in successfully")
							.add("user_id",user.getId()).build();
							
				}else
				{
					return Json.createObjectBuilder()
							.add("error", "your account is disabled").build();
				}
				

			}else
			{
				return Json.createObjectBuilder().add("error", "your email or your password is not correct").build();
			}
		}
		else
		{
			User user =loginUsername(usernameOrEmail,password);
			if(user!=null)
			{
				if(user.getIsEnabled())
				{
					user.setConnected(Boolean.TRUE);
					user.setLastConnect(new Date());
					entityManager.persist(user);
					entityManager.flush();
					device.setOwner(user);
					device.setConnected(Boolean.TRUE);
					device.setLastConnection(new Date());
					deviceService.addDeviceOrSetconnected(device);
					return Json.createObjectBuilder()
							.add("succes", "you have been logged in successfully")
							.add("user_id",user.getId()).build();
					
				}else
				{
					return Json.createObjectBuilder()
							.add("error", "your account is disabled").build();
				}
			
			}
			else
			{
				return Json.createObjectBuilder().add("error", "your username or your password is not correct").build();
			}
			
		}
	}
	
	public JsonObject logout(int iduser,int idDevice) {
		User user=getUserById(iduser);
		if(user!=null)
		{
			Boolean isConn = isConnectedPrivate(iduser);
			if(isConn==null)
			{
				return Json.createObjectBuilder().add("error", "you are not logged in yet").build();
			}
			else
			{
				if(isConnectedPrivate(iduser))
				{
					Device device =deviceService.getDeviceById(idDevice);
					user=getUserById(iduser);
					device.setConnected(Boolean.FALSE);
					if(!haveConnectedDevice(user,device.getIdDevice()))
					{
						user.setConnected(Boolean.FALSE);
					}
					
					entityManager.persist(user);
					entityManager.flush();
					return Json.createObjectBuilder().add("succes", "you have been successfully logged out")
							.add("user_id",user.getId())
							.build();
				}
				else
				{
					return Json.createObjectBuilder().add("error", "you are already logged out").build();
				}	
			}
			
		}
		else
			return Json.createObjectBuilder().add("error", "there are no user with ID: "+iduser).build();
		
	}

	public JsonObject changePassword(int id, String currentPsd, String NewcurrentPsd) {
		
		User user=getUserById(id);
		if(user!=null)
		{
			Boolean isConn = isConnectedPrivate(id);
			if(isConn==null)
			{
				return Json.createObjectBuilder().add("error", "you are not logged in yet").build();
			}
			else if(!isConn)
			{
				return Json.createObjectBuilder().add("error", "you are not connected").build();
			}
			else if(!user.getIsEnabled())
			{
				return Json.createObjectBuilder().add("error", "your account is disabled").build();
			}
			else if(!isValidPassword(NewcurrentPsd))
			{
				return Json.createObjectBuilder().add("error", "the password is too weak").build();
			}
			else
			{
				String hashpass="";
				try {
					hashpass = Utils.toMD5(currentPsd);
				} catch (NoSuchAlgorithmException e) {}
				if(!user.getPassword().equals(hashpass))
				{
					return Json.createObjectBuilder().add("error", "your password is not correct").build();
				}
				else
				{
					try {
						user.setPassword(Utils.toMD5(NewcurrentPsd));
					} catch (NoSuchAlgorithmException e) {}
					entityManager.persist(user);
					entityManager.flush();
					return Json.createObjectBuilder()
							.add("succes", "your password has been changed")
							.add("user_id",user.getId())
							.build();
				}
			}	
		}
		else
		{
			return Json.createObjectBuilder().add("error", "there are no user with ID: "+id).build();	
		}
	}
	public JsonObject chnageProfilPicture(int id, String path) {
		User user=getUserById(id);
		if(user!=null)
		{
			Boolean isConn = isConnectedPrivate(id);
			if(isConn==null)
			{
				return Json.createObjectBuilder().add("error", "you are not logged in yet").build();
			}
			else if(!isConn)
			{
				return Json.createObjectBuilder().add("error", "you are not connected").build();
			}
			else if(!user.getIsEnabled())
			{
				return Json.createObjectBuilder().add("error", "your account is disabled").build();
			}else
			{
				user.setImage(path);
				entityManager.persist(user);
				entityManager.flush();
				return Json.createObjectBuilder()
						.add("succes", "your profil picture has been changed")
						.add("user_id",user.getId())
						.build();
			}
		}else
		{
			return Json.createObjectBuilder().add("error", "there are no user with ID: "+id).build();	
		}
	}
	public JsonObject isConnected(int id) {
		User user=getUserById(id);
		if(user!=null)
		{
			Boolean isConn = isConnectedPrivate(id);
			if(isConn==null)
			{
				return Json.createObjectBuilder().add("error", "you are not logged in yet").build();
			}
			else if(isConn)
			{
				return Json.createObjectBuilder()
						.add("succes", "you are connected")
						.add("user_id",user.getId())
						.build();
			}else
			{
				return Json.createObjectBuilder()
						.add("succes", "you are not connected")
						.add("user_id",user.getId())
						.build();
			}
		}else
		{
			return Json.createObjectBuilder().add("error", "there are no user with ID: "+id).build();	
		}
		
	}
	public JsonObject isConnected(String usernameOrEmail) {
		User user = getUserByUsername(usernameOrEmail);
		if(user!=null)
		{
			return isConnected(user.getId());
		}
		else
		{
			user = getUserByEmail(usernameOrEmail);
			if(user!=null)
			{
				return isConnected(user.getId());
			}
			else
			{
				return Json.createObjectBuilder().add("error", "there are no user with username or email: "+usernameOrEmail).build();	
			}
		}
	}
	public JsonObject disableUser(int id) {
		User user = getUserById(id);
		if(user!=null)
		{
			 if(user.getIsEnabled())
				{
				 	user.setIsEnabled(Boolean.FALSE);
				 	user.setConnected(Boolean.FALSE);
					entityManager.persist(user);
					entityManager.flush();
					return Json.createObjectBuilder()
							.add("succes", "you are account has been disabled")
							.add("user_id",user.getId())
							.build();
				}else
				{
					return Json.createObjectBuilder().add("error", "your account is already disabled").build();
				}
		}else
		{
			return Json.createObjectBuilder().add("error", "there are no user with ID: "+id).build();	
		}
		
	}
	
	public User getUserByUsername(String username) {
		return this.getUserByUsernamePrivate(username);
	}
	public User getUserByEmail(String email) {
		return this.getUserByEmailPrivate(email);
	}
	public List<User> getAllUser() {
		
		try
		{
			
			TypedQuery<User> query =
			entityManager.createQuery
			("SELECT u from User u", User.class);
			List<User> results = query.getResultList();
			return results;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	public List<User> getEnabledUsers() {
		try
		{
			
			TypedQuery<User> query =
			entityManager.createQuery
			("SELECT u from User u where u.isEnabled=true", User.class);
			List<User> results = query.getResultList();
			return results;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	public List<User> getDisableUsers() {
		try
		{
			
			TypedQuery<User> query =
			entityManager.createQuery
			("SELECT u from User u where u.isEnabled=false", User.class);
			List<User> results = query.getResultList();
			return results;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	public JsonObject updateFirstname(User user, String firstname) {
		if(user!=null)
		{
			 if(user.getIsEnabled())
				{
				 	user.setFirstName(firstname);
					entityManager.persist(user);
					entityManager.flush();
					return Json.createObjectBuilder()
							.add("succes", "the username has been update successfully")
							.add("user_id",user.getId())
							.build();
				}else
				{
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}
		}else
		{
			return Json.createObjectBuilder().add("error", "user does not exist").build();	
		}
		
	}
	public JsonObject updateLastname(User user, String lastname) {
		if(user!=null)
		{
			 if(user.getIsEnabled())
				{
				 	user.setLastName(lastname);
					entityManager.persist(user);
					entityManager.flush();
					return Json.createObjectBuilder()
							.add("succes", "the lastname has been update successfully")
							.add("user_id",user.getId())
							.build();
				}else
				{
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}
		}else
		{
			return Json.createObjectBuilder().add("error", "user does not exist").build();	
		}
	}
	public JsonObject updateBirthDate(User user, Date BirthDate) {
		if(user!=null)
		{
			 if(user.getIsEnabled())
				{
				 	user.setBirthDate(BirthDate);
					entityManager.persist(user);
					entityManager.flush();
					return Json.createObjectBuilder()
							.add("succes", "the birth date has been update successfully")
							.add("user_id",user.getId())
							.build();
				}else
				{
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}
		}else
		{
			return Json.createObjectBuilder().add("error", "user does not exist").build();	
		}
	}
	public JsonObject updatePhoneNumber(User user, String phoneNumber) {
		if(user!=null)
		{
			 if(user.getIsEnabled())
				{
				 	user.setPhoneNumber(phoneNumber);
					entityManager.persist(user);
					entityManager.flush();
					return Json.createObjectBuilder()
							.add("succes", "the phone number has been update successfully")
							.add("user_id",user.getId())
							.build();
				}else
				{
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}
		}else
		{
			return Json.createObjectBuilder().add("error", "user does not exist").build();	
		}
	}
	public JsonObject updateGender(User user, UserGender gender) {
		if(user!=null)
		{
			 if(user.getIsEnabled())
				{
				 	user.setGender(gender);
					entityManager.persist(user);
					entityManager.flush();
					return Json.createObjectBuilder()
							.add("succes", "the gender has been update successfully")
							.add("user_id",user.getId())
							.build();
				}else
				{
					return Json.createObjectBuilder().add("error", "your account is disabled").build();
				}
		}else
		{
			return Json.createObjectBuilder().add("error", "user does not exist").build();	
		}
	}
	public List<User> getUsersAbleModerator() {
		try
		{
			
			TypedQuery<User> query =
			entityManager.createQuery
			("SELECT u from User u where u.points>5000", User.class);
			List<User> results = query.getResultList();
			return results;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	public List<User> getUsersAbleSectionAdministrator() {
		try
		{
			
			TypedQuery<User> query =
			entityManager.createQuery
			("SELECT u from User u where u.points>7000", User.class);
			List<User> results = query.getResultList();
			return results;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	public List<User> getUsersAbleSubSectionAdministrator() {
		try
		{
			
			TypedQuery<User> query =
			entityManager.createQuery
			("SELECT u from User u where u.points>10000", User.class);
			List<User> results = query.getResultList();
			return results;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean haveConnectedDevice(User u,int idDevice)
	{
		long result=(long) entityManager.createQuery(
				  "SELECT count(d) from Device d WHERE d.owner = :owner"
				  + " and d.connected = :connected and d.idDevice <> :idDevice")
				  .setParameter("owner", u)
				  .setParameter("connected", Boolean.TRUE)
				  .setParameter("idDevice", idDevice)
				  .getSingleResult();
		
		if(result==0)
			return false;			
		else
			return true;
	}
	
	private User loginEmail(String email, String password)
	{
		try
		{
			User result=(User) entityManager.createQuery(
					  "SELECT u from User u WHERE u.email = :email and u.password = :password")
						.setParameter("email", email)
						.setParameter("password", password)
						.getSingleResult();
				return result;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	private User getUserByUsernamePrivate(String username)
	{
		try
		{
			User result=(User) entityManager.createQuery(
					  "SELECT u from User u WHERE u.username = :username")
						.setParameter("username", username)
						.getSingleResult();
				return result;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	private User getUserByEmailPrivate(String email)
	{
		try
		{
			User result=(User) entityManager.createQuery(
					  "SELECT u from User u WHERE u.email = :email")
						.setParameter("email", email)
						.getSingleResult();
				return result;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	private User loginUsername(String username, String password)
	{
		try
		{
			User result=(User) entityManager.createQuery(
					  "SELECT u from User u WHERE u.username = :username and u.password = :password")
						.setParameter("username", username)
						.setParameter("password", password)
						.getSingleResult();
				return result;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	
	
	private boolean isExistEmail(String email)
	{
		long result=(long) entityManager.createQuery(
				  "SELECT count(u) from User u WHERE u.email = :email").
				  setParameter("email", email).getSingleResult();
		
		if(result==0)
			return false;			
		else
			return true;
	}
	private boolean isExistUsername(String username)
	{
		long result=(long) entityManager.createQuery(
				  "SELECT count(u) from User u WHERE u.username = :username").
				  setParameter("username", username).getSingleResult();
		if(result==0)
			return false;			
		else
			return true;
	}
	private Boolean isConnectedPrivate(int id)
	{
		Boolean result=(Boolean) entityManager.createQuery(
				  "SELECT u.connected from User u WHERE u.idMember = :id").
				  setParameter("id", id).getSingleResult();
			return result;			
	
	}
	private boolean isValidPassword(String password)
	{
		if(password.length()<5)
		return false;
		else
		return true;
	}	
	private User verifToken(String username,String token)
	{
		try
		{
			User result=(User) entityManager.createQuery(
					  "SELECT u from User u WHERE u.username = :username and u.token = :token")
						.setParameter("username", username)
						.setParameter("token", token)
						.getSingleResult();
				return result;	
		}
		catch(NoResultException e)
		{
			return null;
		}
				


	}






	
	

	
		
	}
	

