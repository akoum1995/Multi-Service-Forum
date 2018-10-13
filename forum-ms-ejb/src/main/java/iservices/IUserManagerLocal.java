package iservices;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;
import persistance.Device;
import persistance.User;
import persistance.UserGender;

@Local
public interface IUserManagerLocal 
{
	public User getUserById(int id);
	public User getUserByUsername(String username);
	public User getUserByEmail(String username);
	public JsonObject quickSignup(User user, Device device);
	public JsonObject login(String usernameOrEmail,String password,Device device);
	public JsonObject logout(int iduser,int idDevice);
	public JsonObject isConnected(int id);
	public JsonObject isConnected(String usernameOrEmail);
	public JsonObject enableUser(String username,String token);
	public JsonObject disableUser(int id);
	public JsonObject chnageProfilPicture(int id,String path);
	public JsonObject changePassword(int id,String currentPsd,String NewcurrentPsd);
	public List<User> getAllUser();
	public List<User> getEnabledUsers();
	public List<User> getDisableUsers();
	public JsonObject updateFirstname(User user ,String firstname);
	public JsonObject updateLastname(User user ,String lastname);
	public JsonObject updateBirthDate(User user ,Date BirthDate);
	public JsonObject updatePhoneNumber(User user ,String phoneNumber);
	public JsonObject updateGender(User user ,UserGender gender);
	public List<User> getUsersAbleModerator();//to test
	public List<User> getUsersAbleSectionAdministrator();//to test
	public List<User> getUsersAbleSubSectionAdministrator();//to test
	//public List<User> doYouKnow(int idUser);//to test
	

}
