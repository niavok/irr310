package com.irr310.server.js.objects;

public class Player {

	private final String name;
	private int age;
	private String gender;

	
	public Player(String name) {
		this.name = name;
		System.out.println("Create player name '"+name+"'");
	}

	public void setGender(String gender) {
		this.gender = gender;
		System.out.println("Set gender to '"+name+"' : "+gender);
	}
	
	public void setAge(int age) {
		this.age = age;
		System.out.println("Set age to '"+name+"' : "+age);
	}
	
	public  int getAge() {
		return age; 
	}
}
