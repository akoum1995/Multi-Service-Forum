package persistance;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: Section
 *
 */
@Entity
@Table(name="fms_section")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Section implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idSection;
	
	@ManyToOne
	@JoinColumn(name="creator",referencedColumnName="idMember")
	private User creator;
	@OneToOne
	@JoinColumn(name="administrator",referencedColumnName="idMember")
	private User administrator;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="parentSection")
	private Set<Services> servicesAsParent;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="parentSection")
	private Set<SubSection> subSectionsAsParent;
	
	private static final long serialVersionUID = 1L;

	public Section() {
		super();
	}   
	public Integer getIdSection() {
		return this.idSection;
	}

	public void setIdSection(Integer idSection) {
		this.idSection = idSection;
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
	public Set<Services> getServicesAsParent() {
		return servicesAsParent;
	}
	public void setServicesAsParent(Set<Services> servicesAsParent) {
		this.servicesAsParent = servicesAsParent;
	}
	public Set<SubSection> getSubSectionsAsParent() {
		return subSectionsAsParent;
	}
	public void setSubSectionsAsParent(Set<SubSection> subSectionsAsParent) {
		this.subSectionsAsParent = subSectionsAsParent;
	}
	
   
}
