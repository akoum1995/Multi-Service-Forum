package services;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import iservices.IGroupServiceLocal;
import iservices.IUserManagerLocal;
import persistance.Group;
import persistance.User;


@Stateless
public class GroupService implements IGroupServiceLocal
{	
	@PersistenceContext(unitName="forumMS")
	EntityManager entityManager;
	@EJB
	IUserManagerLocal userService;

	@Override
	public JsonObject addGroup(Group group,int creator) {
		
		User user = userService.getUserById(creator);
		if(user!=null)
		{
			List<Group> listGroup =getGroupsByCreator(user);
			if(listGroup!=null)
			{
				if(isGroupExistByName(listGroup,group.getName()))
				{
					
					return Json
							.createObjectBuilder()
							.add("error", "the gourp with name "+group.getName()+" is already exist")
							.build();
				}else
				{
					group.setCreator(user);
					entityManager.persist(group);
					entityManager.flush();
					return Json
							.createObjectBuilder()
							.add("succes", "the gourp has been successfully added ")
							.build();
				}
			}else
			{
				group.setCreator(user);
				entityManager.persist(group);
				entityManager.flush();
				return Json
						.createObjectBuilder()
						.add("succes", "the gourp has been successfully added ")
						.build();
			}
			
		}
		else
		{
			return Json.createObjectBuilder().add("error", "there are no user with ID: "+creator).build();
		}
		
		
	}

	
	public Group getGroupById(int id) {
		return entityManager.find(Group.class, id);
	}

	public JsonObject updateGroupName(int idGroup, String groupName) {
		
		Group group = getGroupById(idGroup);
		
		if(group!=null)
		{
			User user = group.getCreator();
			List<Group> listGroup =getGroupsByCreator(user);
			if(isGroupExistByName(listGroup,groupName))
			{
				
				return Json
						.createObjectBuilder()
						.add("error", "the gourp with name "+group.getName()+" is already exist")
						.build();
			}else
			{
				if(groupName.length()>0)
				{
					group.setName(groupName);
					entityManager.persist(group);
					entityManager.flush();	
					return Json.createObjectBuilder().add("succes", "the group name has been updated").build();
				}
				else
				{
					return Json.createObjectBuilder().add("error", groupName+" is not valid as name of group").build();
				}	
			}
			
		}
		else
		{
			return Json.createObjectBuilder().add("error", "there are no group with ID: "+idGroup).build();
		}
		
	}
	public JsonObject addUserToGroup(int idGroup, int idUser) {
		User user = userService.getUserById(idUser);
		Group group = getGroupById(idGroup);
		if(user!=null)
		{
			if(group!=null)
			{
				if(isUserExistInGroup(group,idUser))
				{
					return Json.createObjectBuilder().add("error", "the user is already joining the group").build();
				}
				else
				{
					if(group.getGroupMembres()!=null)
					{
						Set<User> userList = group.getGroupMembres();
						userList.add(user);
						group.setGroupMembres(userList);
						entityManager.persist(group);
						entityManager.flush();	
					}
					else
					{
						Set<User> userList =  new HashSet<User>() ;
						userList.add(user);
						group.setGroupMembres(userList);
						entityManager.persist(group);
						entityManager.flush();		
					}
					
					return Json.createObjectBuilder().add("succes", "the user has been successfully added ").build();
				}
			}
			else
			{
				return Json.createObjectBuilder().add("error", "there are no group with ID: "+idGroup).build();	
			}	
		}
		else
		{
			return Json.createObjectBuilder().add("error", "there are no user with ID: "+idUser).build();	
		}
	}

	public JsonObject removeUserFromGroup(int idGroup, int idUser) {
		User user = userService.getUserById(idUser);
		Group group = getGroupById(idGroup);
		if(user!=null)
		{
			if(group!=null)
			{
				if(isUserExistInGroup(group,idUser))
				{
					Set<User> userList = group.getGroupMembres();
					userList.remove(user);
					group.setGroupMembres(userList);
					entityManager.persist(group);
					entityManager.flush();
					return Json.createObjectBuilder().add("succes", "the user has been successfully removed").build();
				}
				else
				{
					
					return Json.createObjectBuilder().add("error", "there are no user with ID: "+idUser+" in this group").build();
				}
			}
			else
			{
				return Json.createObjectBuilder().add("error", "there are no group with ID: "+idGroup).build();	
			}	
		}
		else
		{
			return Json.createObjectBuilder().add("error", "there are no user with ID: "+idUser).build();	
		}
	}

	public List<Group> getAllGroup() {
		try
		{
			
			TypedQuery<Group> query =
			entityManager.createQuery
			("SELECT g from Group g", Group.class);
			List<Group> results = query.getResultList();
			return results;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}


	public List<Group> getGroupsByCreator(User creator) {
			
		try
		{
			
			TypedQuery<Group> query =
			entityManager.createQuery
			("SELECT g from Group g WHERE g.creator = :creator", Group.class)
			.setParameter("creator", creator);
			List<Group> results = query.getResultList();
			return results;	
		}
		catch(NoResultException e)
		{
			return null;
		}
	}
	private boolean isGroupExistByName(List<Group> listGroup,String name)
	{
		for (Group group : listGroup) {
			if(group.getName().equalsIgnoreCase(name))
			{
				return true;	
			}
		}
		return false;
	}
	private boolean isUserExistInGroup(Group gourp,int userId)
	{
		for (User user : gourp.getGroupMembres()) {
			if(user.getId()==userId)
			{
				return true;	
			}
		}
		return false;
	}





	
		
	}
	

