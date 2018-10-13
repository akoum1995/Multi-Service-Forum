package iservices;



import java.util.List;

import javax.ejb.Local;
import javax.json.JsonObject;

import persistance.Group;
import persistance.User;


@Local
public interface IGroupServiceLocal 
{
	public JsonObject addGroup(Group group,int creator );
	public Group getGroupById(int id);//to test
	public List<Group> getGroupsByCreator(User creator);//to test
	public JsonObject addUserToGroup(int idGroup,int idUser);//to test
	public JsonObject removeUserFromGroup(int idGroup,int idUser);//to test
	public List<Group> getAllGroup();
	public JsonObject updateGroupName(int idGroup ,String groupName);//to test
	

}
