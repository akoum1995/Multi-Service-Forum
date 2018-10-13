package persistance;

import java.io.Serializable;
import java.lang.Integer;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Entity implementation class for Entity: Ticket
 *
 */
@Entity
@Table(name="fms_ticket")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Ticket implements Serializable {

	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idTicket;
	
	@OneToOne
	@JoinColumn(name="participant",referencedColumnName="idMember")
	private User participant;
	
	@ManyToOne
	@JoinColumn(name="event",referencedColumnName="idEvent")
	private Event event;
	
	private static final long serialVersionUID = 1L;

	public Ticket() {
		super();
	}   
	public Integer getIdTicket() {
		return this.idTicket;
	}

	public void setIdTicket(Integer idTicket) {
		this.idTicket = idTicket;
	}
	public User getParticipant() {
		return participant;
	}
	public void setParticipant(User participant) {
		this.participant = participant;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
   
}
