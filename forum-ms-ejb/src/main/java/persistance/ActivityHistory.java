package persistance;

import java.io.Serializable;
import java.lang.Integer;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: ActivityHistory
 *
 */
@Entity
@Table(name="fms_activity_history")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class ActivityHistory implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idActivityHistory;
	
	@ManyToOne
	@JoinColumn(name="owner",referencedColumnName="idMember")
	private User owner;
	
	private static final long serialVersionUID = 1L;

	public ActivityHistory() {
		super();
	}   
	public Integer getIdActivityHistory() {
		return this.idActivityHistory;
	}

	public void setIdActivityHistory(Integer idActivityHistory) {
		this.idActivityHistory = idActivityHistory;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
   
}
