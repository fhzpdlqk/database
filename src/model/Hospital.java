package model;

public class Hospital {
	private int id;
	private String name;
	private String address;
	private String phone;
	
	public Hospital(int id, String name, String address, String phone) {
		this.setId(id);
		this.setName(name);
		this.setAddress(address);
		this.setPhone(phone);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
