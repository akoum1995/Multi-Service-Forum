package persistance;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: Topic
 *
 */
@Entity
@Table(name="fms_topic")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Topic implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idTopic;
	
	@ManyToOne
	@JoinColumn(name="parentSubSection",referencedColumnName="idSubSection")
	private SubSection parentSubSection;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="idMember")
	private User creator;
	
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="topic")
	private Set<RateTopic> reactions;
	
	private static final long serialVersionUID = 1L;

	public Topic() {
		super();
	}   
	public Integer getIdTopic() {
		return this.idTopic;
	}

	public void setIdTopic(Integer idTopic) {
		this.idTopic = idTopic;
	}
	public SubSection getParentSubSection() {
		return parentSubSection;
	}
	public void setParentSubSection(SubSection parentSubSection) {
		this.parentSubSection = parentSubSection;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public Set<RateTopic> getReactions() {
		return reactions;
	}
	public void setReactions(Set<RateTopic> reactions) {
		this.reactions = reactions;
	}
   
}
