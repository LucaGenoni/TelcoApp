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
	public TblOrder createSubscription(Date startDate, int idperiod, int idsrvpkg, List<Integer> idopts) {
		TblPeriod period = em.find(TblPeriod.class, idperiod);
		TblPackage srvpkg = em.find(TblPackage.class, idsrvpkg);
		List<TblOptional> opts = new ArrayList<TblOptional>();
		double price = 0;
		if (idopts != null) {
			for (int id : idopts)
				opts.add(em.find(TblOptional.class, id));
			price = opts.stream().mapToDouble(TblOptional::getFee).sum();
		}
		price += period.getFee();
		price *= period.getMonths();
		TblOrder sub = new TblOrder(new Date(), price, startDate, opts, srvpkg, period);
		return sub;
	}

	// create an order
	public TblOrder createOrder(TblUser u, TblOrder o) throws PersistenceException, CorruptedOrderException {
		// fetch informations
		TblUser user = em.find(TblUser.class, u.getPK_Users());
		TblPackage srvpkg = em.find(TblPackage.class, o.getTblPackage().getPK_Packages());
		TblPeriod period = em.find(TblPeriod.class, o.getTblPeriod().getPK_Periods());
		List<TblOptional> opts_untrusted = o.getTblOptionals();
		List<TblOptional> opts = new ArrayList<TblOptional>();
		if (opts_untrusted != null)
			for (TblOptional opt : o.getTblOptionals())
				opts.add(em.find(TblOptional.class, opt.getPK_Optionals()));

		// check integrity of the order to be placed
		if (!srvpkg.getTblPeriods().contains(period))
			throw new CorruptedOrderException("The period is not included in the package");
		for (TblOptional opt : opts)
			if (!srvpkg.getTblOptionals().contains(opt))
				throw new CorruptedOrderException("An optional is not included in the package");

		// create the order
		TblOrder oDB = new TblOrder(o.getCreationDate(), o.getPrice(), o.getStartDate(), opts, srvpkg, period);
		oDB.setTblUser(user);
		user.addTblOrder(oDB);
		try {
			em.persist(user);
			em.flush();
			return oDB;
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

	public List<TblOrder> findAllRejectedOrdersOfUser(int idUser) throws PersistenceException {
		TblUser u = em.find(TblUser.class, idUser);
		return em.createNamedQuery("TblOrder.findAllRejectedOfUser", TblOrder.class).setParameter(1, u).getResultList();
//		Query q = em.createNamedQuery("TblOrder.findAllRejected", TblOrder.class);
//		return q.getResultList();
	}

	// create the bill
//	public TblBill createBill(int ido, byte result) throws PersistenceException, OrderAlreadyPaidException {
//		TblOrder o = em.find(TblOrder.class, ido);
//		// in DB there are triggers that change its state and find is not able to fetch
//		// it. (I tested it)
//		em.refresh(o);
//		if (o.getIsValid() == 1)
//			throw new OrderAlreadyPaidException("Order already paid");
//
//		TblBill b = new TblBill();
//		b.setBillDate(new Date());
//		b.setResult(result);
//		b.setTblOrder(o);
//
//		try {
//			em.persist(b);
//			em.flush();
//			return b;
//		} catch (PersistenceException e) {
//			throw e;
//		}
//	}

	// create the bill and
	public TblBill createBillHere(int ido, int idu, byte result)
			throws PersistenceException, OrderAlreadyPaidException, NoSuchOrderException {
		TblOrder o = em.find(TblOrder.class, ido);
		TblUser u = em.find(TblUser.class, o.getTblUser().getPK_Users());
		Date datePayment = new Date();
		// integrity checks
		if (u.getPK_Users() != idu)
			throw new NoSuchOrderException("No such order to pay");
		if (o.getIsValid() == 1)
			throw new OrderAlreadyPaidException("Order already paid");

		TblBill b = new TblBill(datePayment, result, o);
		o.addTblBill(b);

		// business rule on bills
		o.setIsValid(result);
		if (result == 0) {
			u.setIsInsolvent((byte) 1);
			if (3 <= em.createNamedQuery("TblBill.findAllRejectedOfRejected", TblOrder.class).getResultStream()
					.count()) {
				TblAlert a = new TblAlert(
						findAllRejectedOrdersOfUser(u.getPK_Users()).stream().mapToDouble(TblOrder::getPrice).sum(), u,
						u.getUsername(), u.getEmail(), datePayment);
				u.addTblAlert(a);
			}
		} else {
			if (findAllRejectedOrdersOfUser(u.getPK_Users()).stream().count() == 0)
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
