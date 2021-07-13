package app.core.couponExpirationDailyJob;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import app.core.exception.CouponSystemException;
import app.core.service.JobService;

@Component
public class CouponExpirationDailyJob implements Runnable {

	@Value("${coupon.expiration.daily.job.time:1}")
	private long time; // in milliseconds
	private Thread thread;
	private boolean quit;
	@Autowired
	private JobService jobService;

	public CouponExpirationDailyJob() {
	}

	@PostConstruct
	private void init() {
		this.thread = new Thread(this);
		this.quit = false;
		time = TimeUnit.MINUTES.toMillis(time);
		this.thread.start();
		System.out.println("DailyJob is on AIR");
	}

	@PreDestroy
	public void stopMonitor() {
		this.quit = true;
		thread.interrupt();
		System.out.println("DailyJob is about to end");
	}

	public boolean isStopMonitorRequested() {
		return this.quit;
	}

	/**
	 * runs every 24 hours get all the coupons in the DB and check if the end date
	 * is passed. if the end date is passed then delete it from DB
	 */
	@Override
	public void run() {
		System.out.println("monitor thread is running");
		while (!isStopMonitorRequested()) {
			try {
				try {

					long count = jobService.deleteExpiredCoupons(LocalDate.now());
					System.out.println("---------------------------------------- " + count
							+ " coupons were deleted by the daily job");

					System.out.println("job thread sleeping");
					Thread.sleep(time);
				} catch (InterruptedException e) {
					throw new CouponSystemException(e.getMessage(), e);
				}

			} catch (CouponSystemException e) {
				if (!e.getMessage().contentEquals("sleep interrupted"))
					System.out.println(e.getMessage());
			}

		}
		System.out.println("Main CouponExpiration thread is down");
	}

}
