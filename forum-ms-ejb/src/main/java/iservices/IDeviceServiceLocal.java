package iservices;

import javax.ejb.Local;
import javax.json.JsonObject;

import persistance.Device;

@Local
public interface IDeviceServiceLocal {

	public JsonObject addDevice(Device device);
	public JsonObject addDeviceOrSetconnected(Device device);
	public Device getDeviceById(int id);


}
