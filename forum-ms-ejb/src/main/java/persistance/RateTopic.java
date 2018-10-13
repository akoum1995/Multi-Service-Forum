package persistance;

import java.io.Serializable;
import java.lang.Integer;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: RateTopic
 *
 */
@Entity
@Table(name="fms_rate_topic")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class RateTopic implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idRateTopic;
	
	@ManyToOne
	@JoinColumn(name="reactedUser",referencedColumnName="idMember")
	private User reactedUser;
	
	@ManyToOne
	@JoinColumn(name="topic",referencedColumnName="idTopic")
	private Topic topic;
	
	private static final long serialVersionUID = 1L;

	public RateTopic() {
		super();
	}   
	public Integer getIdRateTopic() {
		return this.idRateTopic;
	}

	public void setIdRateTopic(Integer idRateTopic) {
		this.idRateTopic = idRateTopic;
	}
	public User getReactedUser() {
		return reactedUser;
	}
	public void setReactedUser(User reactedUser) {
		this.reactedUser = reactedUser;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
   
}
