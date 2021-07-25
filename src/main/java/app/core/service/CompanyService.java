package app.core.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exception.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

@Service
@Transactional
@Scope(value = "prototype")
public class CompanyService extends ClientService {

	private int companyID;
	@Value("${file.upload-dir}")
	private String storageDir;
	private Path fileStoragePath;

	@Autowired
	public CompanyService(CompanyRepository companyRepository, CustomerRepository customerRepository,
			CouponRepository couponRepository) {
		super(companyRepository, customerRepository, couponRepository);
	}

	@PostConstruct
	public void init() {
		this.fileStoragePath = Paths.get(this.storageDir).toAbsolutePath();
		System.out.println(this.fileStoragePath);

		try {
			// create the directory
			Files.createDirectories(fileStoragePath);
		} catch (IOException e) {
			throw new RuntimeException("could not create directory", e);
		}
	}

	public String storeFile(MultipartFile file) {
		String fileName = file.getOriginalFilename();

		System.out.println("getOriginalFilename " + file.getOriginalFilename());
		System.out.println("getName " + file.getName());
		System.out.println("getResource " + file.getResource());
		System.out.println("getContentType " + file.getContentType());
		System.out.println("getClass " + file.getClass());
		if (fileName.contains("..")) {
			throw new RuntimeException("file name contains ilegal caharacters");
		}
		// copy the file to the destination directory (if already exists replace)
		try {
			Path targetLocation = this.fileStoragePath.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return "pics/" + fileName;
		} catch (IOException e) {
			throw new RuntimeException("storing file " + fileName + " failed", e);
		}
	}

//	https://i.ibb.co/xXcX2r5/fluffy-Dog.jpg
//		https://i.ibb.co/f47Qnwq/clock-And-Wall.jpg

	@Override
	public boolean login(String email, String password) {
		Company company = companyRepository.findByEmailIgnoreCaseAndPassword(email, password);
		if (company != null) {
			companyID = company.getId();
			return true;
		}
		return false;

	}

	public Coupon addCoupon(Coupon coupon) throws CouponSystemException {
		System.out.println("1111111111111111111111111111111111");
		validateCoupon(coupon);
		System.out.println("222222222222222222222222222222222");
		try {
			System.out.println("=======================addCoupon================");
			coupon.setCompany(getCompanyDetails());

			if (!findByTitleAndCompanyId(coupon)) {
				Optional<Company> opt = companyRepository.findById(companyID);
				if (opt.isPresent()) {

					opt.get().addCoupon(coupon);
					Coupon couponFromDB = couponRepository.findByTitleAndCompanyId(coupon.getTitle(), companyID);
					return couponFromDB;
				}
				throw new CouponSystemException("Company does not exist");
			}

			throw new CouponSystemException("There is already a coupon with the same title for this company");
		} catch (Exception e) {
			throw new CouponSystemException(e.getMessage(), e);
		}
	}

	public Coupon updateCoupon(Coupon coupon) throws CouponSystemException {
		validateCoupon(coupon);

		coupon.setCompany(getCompanyDetails());
		System.out.println(coupon);
		if (findByTitleAndCompanyIdAndIdIsNot(coupon))
			throw new CouponSystemException("There is already a coupon with the same title in this company");

		Coupon otherCoupon = findCouponById(coupon.getId());
		if (coupon != null) {

			if (coupon.getCompany().getId() != otherCoupon.getCompany().getId())
				throw new CouponSystemException("you can't change the company id");

			otherCoupon.setAmount(coupon.getAmount());
			otherCoupon.setCategory(coupon.getCategory());
			otherCoupon.setDescription(coupon.getDescription());
			otherCoupon.setEndDate(coupon.getEndDate());
			otherCoupon.setImageName(coupon.getImageName());
			otherCoupon.setPrice(coupon.getPrice());
			otherCoupon.setStartDate(coupon.getStartDate());
			otherCoupon.setTitle(coupon.getTitle());

			return couponRepository.save(otherCoupon);
		}
		throw new CouponSystemException("Problems at updateCoupon: Could not find coupon with id: " + coupon.getId());
	}

	public void deleteCoupon(int couponId) throws CouponSystemException {
		Coupon coupon = findCouponById(couponId);
		if (coupon != null) {
			couponRepository.delete(coupon);
			System.out.println("Coupon with id :" + couponId + " deleted");
		} else
			throw new CouponSystemException("Coupon with id :" + couponId + " not exsits");
	}

	public List<Coupon> getAllCoupons() {
		System.out.println("getAllCoupons");
		List<Coupon> coupons = couponRepository.findByCompanyId(companyID);
		if (coupons.isEmpty())
			return coupons;
		return coupons;
	}

	public List<Coupon> getAllCouponsByCategory(Category category) {
		return couponRepository.findByCompanyIdAndCategory(companyID, category);
	}

	public List<Coupon> getAllCouponsByprice(double price) {
		return couponRepository.findByCompanyIdAndPriceLessThanEqual(companyID, price);
	}

	/**
	 * @param coupon
	 * @return true if there is a match to coupon title and the company id that
	 *         holds the coupon
	 */
	public boolean findByTitleAndCompanyId(Coupon coupon) {
		System.out.println("findByTitleAndCompanyId");
		return couponRepository.findByTitleAndCompanyId(coupon.getTitle(), coupon.getCompany().getId()) != null;
	}

	public boolean findByTitleAndCompanyIdAndIdIsNot(Coupon coupon) {
		System.out.println("findByTitleAndCompanyIdAndIdIsNot");
		return couponRepository.findByTitleAndCompanyIdAndIdIsNot(coupon.getTitle(), coupon.getCompany().getId(),
				coupon.getId()) != null;
	}

	public Coupon findCouponById(int id) {
		Optional<Coupon> opt = couponRepository.findById(id);
		if (opt.isPresent()) {
			Coupon coupon = opt.get();
			System.out.println("findCouponById " + coupon);
			return coupon;
		}
		return null;
	}

	public Company getCompanyDetails() {
		Optional<Company> opt = companyRepository.findById(companyID);
		if (opt.isPresent())
			return opt.get();
		return null;
	}

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	/**
	 * check if the values of the coupon are legal amount bigger than one , price
	 * bigger or equal to zero and end date is not before now or before the start
	 * date
	 * 
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void validateCoupon(Coupon coupon) throws CouponSystemException {
		System.out.println("validateCoupon");
		coupon.convertDatesFromStringToLocalDate();
		System.out.println("5");
		
		System.out.println(coupon.toString());

		if (coupon.getPrice() < 0) {
			throw new CouponSystemException("price cannot be less than zero");
		}
		if (coupon.getAmount() < 1) {
			throw new CouponSystemException("Amount cannot be less than one");
		}
		LocalDate end = coupon.getEndDate();
		if (end.isBefore(LocalDate.now()))
			throw new CouponSystemException("End date cannot be in the past");
		LocalDate start = coupon.getStartDate();
		if (end.isBefore(start))
			throw new CouponSystemException("End date cannot be before the start date");
	}

}
