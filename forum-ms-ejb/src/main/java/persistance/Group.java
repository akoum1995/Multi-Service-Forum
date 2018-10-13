package persistance;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * Entity implementation class for Entity: Group
 *
 */
@Entity
@Table(name="fms_group")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Group implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idGroup;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="groupMembres",referencedColumnName="idMember")
	private Set<User> groupMembres;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="idMember")
	private User creator;
	
	private static final long serialVersionUID = 1L;

	public Group() {
		super();
	} 
	public Group(String name) {
		this.name = name;
		this.creationDate=new Date();
	}
	public Integer getIdGroup() {
		return this.idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}
	public Set<User> getGroupMembres() {
		return groupMembres;
	}
	public void setGroupMembres(Set<User> groupMembres) {
		this.groupMembres = groupMembres;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
   
}
