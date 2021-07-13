package app.core.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author gilsh
 * Define the company class 
 */
//asdasd
@Entity
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true , nullable = false)
	private  String name;
	@Column(unique = true , nullable = false)
	private  String email;
	@Column(nullable = false)
	private  String password;
	@JsonIgnore
	@OneToMany(targetEntity = Coupon.class,mappedBy = "company", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	private List<Coupon> coupons;

	public Company(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public Company(int id, String name, String email, String password, ArrayList<Coupon> coupons) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.coupons = coupons;
	}

	public Company(int id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Company() {
	}
	
	public void addCoupon(Coupon coupon) {
		if(this.coupons==null)
			coupons = new ArrayList<Coupon>();
		coupon.setCompany(this);
		coupons.add(coupon);
		System.out.println("addCoupon");
		System.out.println(coupon);
		
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}
	
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
	}
	
	
	

}
