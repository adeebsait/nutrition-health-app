package com.example.finalyear.common.data.firebasepojo;

public class Info{
	private String firstName;
	private String lastName;
	private String gender;
	private String dob;
	private Weight weight;
	private Height height;

	public Info(){

	}

	public Info(String firstName, String lastName, String gender, String dob, Weight weight, Height height) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dob = dob;
		this.weight = weight;
		this.height = height;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public String getGender(){
		return gender;
	}

	public String getDob(){
		return dob;
	}

	public Weight getWeight(){
		return weight;
	}

	public Height getHeight(){
		return height;
	}


	@Override
	public String toString() {
		return "Info{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", gender='" + gender + '\'' +
				", dob='" + dob + '\'' +
				", weight=" + weight +
				", height=" + height +
				'}';
	}
}
