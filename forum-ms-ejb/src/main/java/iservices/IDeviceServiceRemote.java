package iservices;

import javax.ejb.Remote;
import javax.json.JsonObject;

import persistance.Device;

@Remote
public interface IDeviceServiceRemote {

	public JsonObject addDevice(Device device);
	public JsonObject addDeviceOrSetconnected(Device device);
	public Device getDeviceById(int id);


}
