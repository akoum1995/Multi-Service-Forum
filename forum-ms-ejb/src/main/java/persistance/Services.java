package persistance;

import java.io.Serializable;
import java.lang.Integer;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: Services
 *
 */
@Entity
@Table(name="fms_service")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Services implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idServices;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="idMember")
	private User creator;
	
	@ManyToOne
	@JoinColumn(name="parentSection",referencedColumnName="idSection")
	private Section parentSection;
	
	private static final long serialVersionUID = 1L;

	public Services() {
		super();
	}   
	public Integer getIdServices() {
		return this.idServices;
	}

	public void setIdServices(Integer idServices) {
		this.idServices = idServices;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public Section getParentSection() {
		return parentSection;
	}
	public void setParentSection(Section parentSection) {
		this.parentSection = parentSection;
	}
   
}
