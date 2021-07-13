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
import app.core.entities.CouponImage;
import app.core.exception.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;
import app.core.service.menu.CompanyServiceMenu;

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
		// TODO Auto-generated constructor stub
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

	@Override
	public boolean login(String email, String password) {
		Company company = companyRepository.findByEmailIgnoreCaseAndPassword(email, password);
		if (company != null) {
			companyID = company.getId();
			return true;
		}
		return false;

	}

	public Coupon convertCouponImage(CouponImage couponImage) throws CouponSystemException {
		System.out.println("convertCouponImageToCoupon");
		System.out.println(couponImage);
		try {
			LocalDate start = LocalDate.parse(couponImage.getStartDate().toString());
			LocalDate end = LocalDate.parse(couponImage.getEndDate().toString());
			Coupon coupon = new Coupon(couponImage.getCategory(), couponImage.getTitle(), couponImage.getDescription(),
					start, end, couponImage.getAmount(), couponImage.getPrice(), null);
			coupon.setId(couponImage.getId());
			if (couponImage.getImage() != null) {
				coupon.setImageName(storeFile(couponImage.getImage()));
			}
			// check if coupon is added or updated: add id = 0 | updated id != 0

			// coupon is being added:
			else if (coupon.getId() == 0) {
				coupon.setImageName("no_image");
			}
			// coupon is being updated:
			else {
				Optional<Coupon> opt = couponRepository.findById(coupon.getId());
				if (opt.isPresent()) {
					coupon.setImageName(opt.get().getImageName());
				}
			}

			return coupon;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CouponSystemException("We have problem at convertCouponImage ", e);
		}
	}

	public Coupon addCoupon(CouponImage couponImage) throws CouponSystemException {
		try {

			validateCoupon(couponImage);

			System.out.println("=======================addCoupon================");
			Coupon coupon = convertCouponImage(couponImage);
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

	public Coupon updateCoupon(CouponImage couponImage) throws CouponSystemException {

		validateCoupon(couponImage);

		Coupon otherCoupon = convertCouponImage(couponImage);
		otherCoupon.setCompany(getCompanyDetails());
		System.out.println(otherCoupon);
		if (findByTitleAndCompanyIdAndIdIsNot(otherCoupon))
			throw new CouponSystemException("There is already a coupon with the same title in this company");

		Coupon coupon = findCouponById(otherCoupon.getId());
		if (coupon != null) {

			if (coupon.getCompany().getId() != otherCoupon.getCompany().getId())
				throw new CouponSystemException("you can't change the company id");

			coupon.setAmount(otherCoupon.getAmount());
			coupon.setCategory(otherCoupon.getCategory());
			coupon.setDescription(otherCoupon.getDescription());
			coupon.setEndDate(otherCoupon.getEndDate());
			coupon.setImageName(otherCoupon.getImageName());
			coupon.setPrice(otherCoupon.getPrice());
			coupon.setStartDate(otherCoupon.getStartDate());
			coupon.setTitle(otherCoupon.getTitle());

			return couponRepository.save(coupon);
		}
		throw new CouponSystemException(
				"Problems at updateCoupon: Could not find coupon with id: " + otherCoupon.getId());
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
	public void validateCoupon(CouponImage coupon) throws CouponSystemException {

		if (coupon.getPrice() < 0) {
			throw new CouponSystemException("price cannot be less than zero");
		}
		if (coupon.getAmount() < 1) {
			throw new CouponSystemException("Amount cannot be less than one");
		}
		LocalDate end = LocalDate.parse(coupon.getEndDate());
		if (end.isBefore(LocalDate.now()))
			throw new CouponSystemException("End date cannot be in the past");
		LocalDate start = LocalDate.parse(coupon.getStartDate());
		if (end.isBefore(start))
			throw new CouponSystemException("End date cannot be before the start date");
	}

	public static void menu(CompanyService service, Scanner scanner) throws CouponSystemException {

		boolean flag = true;
		int num = 0;

		while (flag) {
			System.out.println("for adding a coupon press ..................................1");
			System.out.println("for update a coupon press ..................................2");
			System.out.println("for delete coupon press ....................................3");
			System.out.println("for watch all coupons of company press .....................4");
			System.out.println("for view coupons from specific category of company press ...5");
			System.out.println("for watch all coupons of company until max price ...........6");
			System.out.println("for watch company data .....................................7");
			System.out.println("for exit press .............................................8\n");

			try {
				num = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				num = -1;
			}
			System.out.println();
			switch (num) {
			case 1: {
				// service.addCoupon(CompanyServiceMenu.addCouponMenu(service.getCompanyDetails(),scanner));
				System.out.println("hey");
				break;
			}
			case 2: {
				break;
			}
			case 3: {
				try {
					service.deleteCoupon(CompanyServiceMenu.deleteCouponMenu(scanner));
				} catch (CouponSystemException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case 4: {
				try {
					CompanyServiceMenu.watchAllCouponsMenu(service);
				} catch (CouponSystemException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case 5: {
				try {
					CompanyServiceMenu.watchCouponByCategoryMenu(service, scanner);
				} catch (CouponSystemException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case 6: {
				try {
					CompanyServiceMenu.watchCouponByMaxPriceMenu(service, scanner);
				} catch (CouponSystemException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case 7: {
				try {
					CompanyServiceMenu.getCompanyDetails(service);
				} catch (CouponSystemException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case 8: {
				flag = false;
				break;
			}
			default:
				System.out.println("enter numbers between 1-8\n");
			}

		}
		System.out.println("exit company menu\n");
	}
}
