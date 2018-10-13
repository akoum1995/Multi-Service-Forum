package services;


import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import iservices.IDeviceServiceLocal;
import persistance.Device;
import utils.Mail;
import utils.Utils;


@Stateless
public class DeviceService implements IDeviceServiceLocal {

	@PersistenceContext(unitName="forumMS")
	EntityManager entityManager;
	@EJB
	Mail mail;
	
	public Device getDeviceById(int id) {
		return entityManager.find(Device.class, id);
	}
	public JsonObject addDevice(Device device) {
		if(findDeviceByOsAndBrowser(device))
		{
			
			return Json
					.createObjectBuilder()
					.add("error", "the device is allready exist")
					.build();
		}
		else
		{
			entityManager.persist(device);
			entityManager.flush();
			return Json
					.createObjectBuilder()
					.add("succes", "the device has been successfully added ")
					.build();
		}
	}
	public JsonObject addDeviceOrSetconnected(Device device) {
		Device device2=findDeviceByOsAndBrowserDevice(device);
		
		if(device2==null)
		{
			System.out.println("i'm heeeree help1");
			String logoutpath="http://localhost:18080/forum-ms-web/v0/user/logout/"+device.getOwner().getId()+"/"+device.getIdDevice();
			mail.send(device.getOwner().getEmail(),"new device detected ",Utils.newsignIn(logoutpath, device),device.getOs()+" "+device.getBrowser()+" "+device.getIp(), "");
			System.out.println("i'm heeeree help2");
			entityManager.persist(device);
			return Json
					.createObjectBuilder()
					.add("succes", "the device has been successfully added ")
					.build();
		}
		else
		{
			device2.setConnected(true);
			device2.setLastConnection(new Date());
			entityManager.persist(device2);
			return Json
					.createObjectBuilder()
					.add("error", "the device is allready exist")
					.build();
		}
	}
	
	
	
	
	
	
	
	
	
	
	private boolean findDeviceByOsAndBrowser(Device device)
	{
		long result=(long) entityManager.createQuery(
				  	"SELECT count(d) from Device d WHERE d.owner = :owner and d.os = :os and d.browser = :browser")
					.setParameter("owner", device.getOwner())
					.setParameter("os", device.getOs())
					.setParameter("browser", device.getBrowser())
					.getSingleResult();
		
		if(result==0)
			return false;			
		else
			return true;
	}
	private Device findDeviceByOsAndBrowserDevice(Device device)
	{
		
		try{


			return (Device) entityManager.createQuery(
				  	"SELECT d from Device d WHERE d.owner = :owner and d.os = :os and d.browser = :browser")
					.setParameter("owner", device.getOwner())
					.setParameter("os", device.getOs())
					.setParameter("browser", device.getBrowser())
					.getSingleResult();
		}
			catch (NoResultException nre){
				return null;
			}
	}













}
