//package app.core.entities;
//
//import java.io.Serializable;
//import org.springframework.web.multipart.MultipartFile;
//import app.core.entities.Coupon.Category;
//
//public class CouponImage implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//	
//	private Category category;
//	private String title;
//	private String description;
//	private String startDate;
//	private String endDate;
//	private int amount;
//	private int id;
//	private double price;
//	private MultipartFile image;
//
//	
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	@Override
//	public String toString() {
//		return "CouponImage [category=" + category + ", title=" + title + ", description=" + description
//				+ ", startDate=" + startDate + ", endDate=" + endDate + ", amount=" + amount + ", price=" + price
//				+ ", image=" + image + "]";
//	}
//
//	public CouponImage() {
//	}
//
//	public CouponImage(Category category, String title, String description, String startDate, String endDate,
//			int amount, double price, Company company) {
//		this.category = category;
//		this.title = title;
//		this.description = description;
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.amount = amount;
//		this.price = price;
//	}
//
//	public Category getCategory() {
//		return category;
//	}
//
//	public void setCategory(Category category) {
//		this.category = category;
//	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public String getStartDate() {
//		return startDate;
//	}
//
//	public void setStartDate(String startDate) {
//		this.startDate = startDate;
//	}
//
//	public String getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(String endDate) {
//		this.endDate = endDate;
//	}
//
//	public int getAmount() {
//		return amount;
//	}
//
//	public void setAmount(int amount) {
//		this.amount = amount;
//	}
//
//	public double getPrice() {
//		return price;
//	}
//
//	public void setPrice(double price) {
//		this.price = price;
//	}
//
//	public MultipartFile getImage() {
//		return image;
//	}
//
//	public void setImage(MultipartFile image) {
//		this.image = image;
//	}
//
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
//
//
//}
