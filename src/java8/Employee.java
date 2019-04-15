package java8;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Employee {
	private int employeeId;
	private String name;
	private Date birthDate;
	private Gender gender;
	
	public Employee( int employeeId, String name, Date birthDate, Gender gender ) {
		super();
		this.employeeId = employeeId;
		this.name = name;
		this.birthDate = birthDate;
		this.gender = gender;
	}

	public boolean isMale() {
		return this.gender == Gender.MALE;
	}
	
	public boolean isFemale() {
		return this.gender == Gender.FEMALE;
	}
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		StringBuilder str = null;
		str = new StringBuilder();
		str.append( "Employee id : " + getEmployeeId() + " Name : " + getName() + " Birthdate : " + dateFormat.format( getBirthDate() ) );
		
		return str.toString();
	}
	
	public static enum Gender {
		MALE,
		FEMALE
	}
}
