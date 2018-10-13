package persistance;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: SubSection
 *
 */
@Entity
@Table(name="fms_sub_section")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class SubSection implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idSubSection;
	
	@ManyToOne
	@JoinColumn(name="parentSection",referencedColumnName="idSection")
	private Section parentSection;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="idMember")
	private User creator;
	
	@OneToOne
	@JoinColumn(name="administrator",referencedColumnName="idMember")
	private User administrator;
	
	@ManyToMany
	@JoinColumn(name="moderators",referencedColumnName="idMember")
	private Set<User> moderators;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="parentSubSection")
	private Set<Topic> topicsList;
	
	private static final long serialVersionUID = 1L;

	public SubSection() {
		super();
	}   
	public Integer getIdSubSection() {
		return this.idSubSection;
	}

	public void setIdSubSection(Integer idSubSection) {
		this.idSubSection = idSubSection;
	}
	public Section getParentSection() {
		return parentSection;
	}
	public void setParentSection(Section parentSection) {
		this.parentSection = parentSection;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public User getAdministrator() {
		return administrator;
	}
	public void setAdministrator(User administrator) {
		this.administrator = administrator;
	}
	public Set<User> getModerators() {
		return moderators;
	}
	public void setModerators(Set<User> moderators) {
		this.moderators = moderators;
	}
	public Set<Topic> getTopicsList() {
		return topicsList;
	}
	public void setTopicsList(Set<Topic> topicsList) {
		this.topicsList = topicsList;
	}
	
}
