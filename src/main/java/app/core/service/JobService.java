package app.core.service;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.repositories.CouponRepository;

@Service
@Transactional
public class JobService {

	private CouponRepository couponRepository;
	@Autowired
	public JobService(CouponRepository couponRepository) {
		this.couponRepository= couponRepository;
	}
	
	public long deleteExpiredCoupons(LocalDate date) {
		return couponRepository.deleteByEndDateIsBefore(date);
	}
	
}
