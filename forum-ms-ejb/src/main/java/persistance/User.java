package persistance;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;



/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name="fms_member")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class User implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idMember;

	@ManyToMany(fetch = FetchType.EAGER,mappedBy="groupMembres")
	private Set<Group> groupsAsMembre;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="creator")
	private Set<Group> groupsAsCreator;

	@OneToMany(fetch = FetchType.EAGER,mappedBy="owner")
	private Set<Device> Devices;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="creator")
	private Set<Section> sectionsAsCreator;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="creator")
	private Set<Topic> topicsAsCreator;
	@OneToMany(fetch = FetchType.EAGER,mappedBy="creator")
	private Set<SubSection> subSectionsAsCreator;
	
	@ManyToMany(fetch = FetchType.EAGER,mappedBy="moderators")
	private Set<SubSection> subSectionsAsModerator;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="creator")
	private Set<Services> servicesAsCreator;
	
	@OneToOne(fetch = FetchType.EAGER,mappedBy="administrator")
	private Section sectionAsAdministrator;
	
	@OneToOne(fetch = FetchType.EAGER,mappedBy="participant")
	private Ticket ticket;
	
	@OneToOne(fetch = FetchType.EAGER,mappedBy="administrator")
	private SubSection subSectionAsAdministrator;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="owner")
	private Set<ActivityHistory> myHistories;
	
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="creator")
	private Set<Event> myEvents;
	
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="reactedUser")
	private Set<RateTopic> myReactions;
	
	private static final long serialVersionUID = 1L;
	
	
	@Column(unique = true,nullable = false)
	private String email;
	@Column(unique = true,nullable = false)
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRole role;
	private Boolean isEnabled;
	private String firstName;
	private String lastName;
	@Temporal(TemporalType.TIMESTAMP)
	private Date birthDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastConnect;
	// account creation date
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	private String image;
	private String phoneNumber;
	private UserGender gender;
	private Boolean connected;
	private String token;
	private int points;
	
	
	public User() {
	}  
	
	public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = UserRole.MEMBER;
		this.isEnabled = Boolean.FALSE;
		this.creationDate = new Date();
		this.points=50;
	}
 
	public Integer getId() {
		return this.idMember;
	}

	public void setId(Integer id) {
		this.idMember = id;
	}
	public Integer getIdMember() {
		return idMember;
	}
	public void setIdMember(Integer idMember) {
		this.idMember = idMember;
	}
	public Set<Group> getGroupsAsMembre() {
		return groupsAsMembre;
	}
	public void setGroupsAsMembre(Set<Group> groupsAsMembre) {
		this.groupsAsMembre = groupsAsMembre;
	}
	public Set<Group> getGroupsAsCreator() {
		return groupsAsCreator;
	}
	public void setGroupsAsCreator(Set<Group> groupsAsCreator) {
		this.groupsAsCreator = groupsAsCreator;
	}
	public Set<Section> getSectionsAsCreator() {
		return sectionsAsCreator;
	}
	public void setSectionsAsCreator(Set<Section> sectionsAsCreator) {
		this.sectionsAsCreator = sectionsAsCreator;
	}
	public Set<Topic> getTopicsAsCreator() {
		return topicsAsCreator;
	}
	public void setTopicsAsCreator(Set<Topic> topicsAsCreator) {
		this.topicsAsCreator = topicsAsCreator;
	}
	public Set<SubSection> getSubSectionsAsCreator() {
		return subSectionsAsCreator;
	}
	public void setSubSectionsAsCreator(Set<SubSection> subSectionsAsCreator) {
		this.subSectionsAsCreator = subSectionsAsCreator;
	}
	public Set<SubSection> getSubSectionsAsModerator() {
		return subSectionsAsModerator;
	}
	public void setSubSectionsAsModerator(Set<SubSection> subSectionsAsModerator) {
		this.subSectionsAsModerator = subSectionsAsModerator;
	}
	public Set<Services> getServicesAsCreator() {
		return servicesAsCreator;
	}
	public void setServicesAsCreator(Set<Services> servicesAsCreator) {
		this.servicesAsCreator = servicesAsCreator;
	}
	public Section getSectionAsAdministrator() {
		return sectionAsAdministrator;
	}
	public void setSectionAsAdministrator(Section sectionAsAdministrator) {
		this.sectionAsAdministrator = sectionAsAdministrator;
	}
	public Ticket getTicket() {
		return ticket;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	public SubSection getSubSectionAsAdministrator() {
		return subSectionAsAdministrator;
	}
	public void setSubSectionAsAdministrator(SubSection subSectionAsAdministrator) {
		this.subSectionAsAdministrator = subSectionAsAdministrator;
	}
	public Set<ActivityHistory> getMyHistories() {
		return myHistories;
	}
	public void setMyHistories(Set<ActivityHistory> myHistories) {
		this.myHistories = myHistories;
	}
	public Set<Event> getMyEvents() {
		return myEvents;
	}
	public void setMyEvents(Set<Event> myEvents) {
		this.myEvents = myEvents;
	}
	public Set<RateTopic> getMyReactions() {
		return myReactions;
	}
	public void setMyReactions(Set<RateTopic> myReactions) {
		this.myReactions = myReactions;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public UserGender getGender() {
		return gender;
	}
	public void setGender(UserGender gender) {
		this.gender = gender;
	}

	public Date getLastConnect() {
		return lastConnect;
	}

	public void setLastConnect(Date lastConnect) {
		this.lastConnect = lastConnect;
	}

	public Boolean getConnected() {
		return connected;
	}

	public void setConnected(Boolean connected) {
		this.connected = connected;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Set<Device> getDevices() {
		return Devices;
	}

	public void setDevices(Set<Device> devices) {
		Devices = devices;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
   
}
