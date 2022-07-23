package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.time.DateUtils;

import entities.TblAlert;
import entities.TblBill;
import entities.TblOptional;
import entities.TblOrder;
import entities.TblPackage;
import entities.TblPeriod;
import entities.TblSchedule;
import entities.TblUser;
import exceptions.CorruptedOrderException;
import exceptions.NoSuchOrderException;
import exceptions.OrderAlreadyPaidException;

@Stateless
public class OrderService {
	@PersistenceContext(unitName = "telcoUser")
	private EntityManager em;

	public OrderService() {
	}

	// create a subscription
	public TblOrder createSubscription(Date startDate, int idperiod, int idsrvpkg, List<Integer> idopts) throws CorruptedOrderException {
		TblPeriod period = em.find(TblPeriod.class, idperiod);
		TblPackage srvpkg = em.find(TblPackage.class, idsrvpkg);
		List<TblOptional> opts = new ArrayList<TblOptional>();		
		if (idopts != null)
			for (int id : idopts)
				opts.add(em.find(TblOptional.class, id));
		
		// check integrity of the subscription
		if (!srvpkg.getTblPeriods().contains(period))
			throw new CorruptedOrderException("The period is not included in the package");
		for (TblOptional opt : opts)
			if (!srvpkg.getTblOptionals().contains(opt))
				throw new CorruptedOrderException("An optional is not included in the package");
		
		// compute the price
		double price = 0;
		if (idopts != null) 
			price += opts.stream().mapToDouble(TblOptional::getFee).sum();
		price += period.getFee();
		price *= period.getMonths();
		
		return new TblOrder(price, startDate, opts, srvpkg, period);
	}

	// create an order from a subscription
	public TblOrder createOrder(TblUser u, TblOrder sub) throws PersistenceException{
		// fetch informations
		TblUser user = em.find(TblUser.class, u.getPK_Users());

		sub.setCreationDate(new Date());
		sub.setTblUser(user);
		user.addTblOrder(sub);
		try {
			em.persist(user);
			em.flush();
			return sub;
		} catch (PersistenceException e) {
			throw e;
		}
	}

	// fetch a single order
	public TblOrder findRejectOrder(int ido, int idUser) throws PersistenceException, NoSuchOrderException {
		TblOrder o = em.find(TblOrder.class, ido);
		if (o.getTblUser().getPK_Users() == idUser && o.getIsValid() == 0)
			return o;
		throw new NoSuchOrderException("No such order to buy");
	}


	// create the bill and
	public TblBill createBill(int ido, int idu, byte result)
			throws PersistenceException, OrderAlreadyPaidException, NoSuchOrderException {
		TblOrder o = em.find(TblOrder.class, ido);
		TblUser u = em.find(TblUser.class, o.getTblUser().getPK_Users());
		Date datePayment = new Date();
		// integrity checks
		if (u.getPK_Users() != idu)
			throw new NoSuchOrderException("No such order to pay for u");
		if (o.getIsValid() == 1)
			throw new OrderAlreadyPaidException("Order already paid");

		TblBill b = new TblBill(datePayment, result, o);
		o.addTblBill(b);
		o.setIsValid(result);
		
		// business rule on bills
		long FailedAttempts = em.createNamedQuery("TblBill.findAllOfRejectedOrderOfUser", TblOrder.class)
				.setParameter(1, u).getResultStream()
				.count();
		if (result == 0) {
			u.setIsInsolvent((byte) 1);
			if (FailedAttempts >= 3) {
				double amount = em.createNamedQuery("TblOrder.findAllRejectedOfUser", TblOrder.class)
						.setParameter(1, u).getResultList().stream()
						.mapToDouble(TblOrder::getPrice).sum();
				TblAlert a = new TblAlert(amount, u, u.getUsername(), u.getEmail(), datePayment);
				u.addTblAlert(a);
			}
		} else {
			if (FailedAttempts == 0)
				u.setIsInsolvent((byte) 0);
			TblSchedule schedule = new TblSchedule(o.getStartDate(),
					DateUtils.addMonths(o.getStartDate(), o.getTblPeriod().getMonths()), o, u);
			u.addTblSchedule(schedule);
		}
		
		try {
			em.persist(u);
			em.flush();
			return b;
		} catch (PersistenceException e) {
			throw e;
		}
	}
}
