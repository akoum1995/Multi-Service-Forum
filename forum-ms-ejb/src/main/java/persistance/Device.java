package persistance;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name="fms_Device")
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Device implements Serializable {

	   

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idDevice;
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name="owner",referencedColumnName="idMember")
	private User owner;
	
	private String os;
	private String browser;
	private String ip;
	private Boolean connected;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastConnection;
	public Device(User owner, String os, String browser, String ip,Boolean connected) {
		super();
		this.owner = owner;
		this.os = os;
		this.browser = browser;
		this.ip = ip;
		this.connected=connected;
		this.lastConnection=new Date();
	}
	public Device() {

	}
	public Integer getIdDevice() {
		return idDevice;
	}
	public void setIdDevice(Integer idDevice) {
		this.idDevice = idDevice;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Boolean getConnected() {
		return connected;
	}
	public void setConnected(Boolean connected) {
		this.connected = connected;
	}
	public Date getLastConnection() {
		return lastConnection;
	}
	public void setLastConnection(Date lastConnection) {
		this.lastConnection = lastConnection;
	}
	
   
}
