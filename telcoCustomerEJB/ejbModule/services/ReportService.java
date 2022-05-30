package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.TblAlert;
import entities.TblOrder;
import entities.TblUser;
import entities.Vw_Report134;
import entities.Vw_Report2;
import entities.Vw_Report6Best;

@Stateless
public class ReportService {
	@PersistenceContext(unitName = "telcoUser")
	private EntityManager em;

	public ReportService() {
	}

	public List<Vw_Report134> findAllVw_Report134() throws PersistenceException {
		return em.createNamedQuery("Vw_Report134.findAll", Vw_Report134.class).getResultList();
	}

	public List<Vw_Report2> findAllVw_Report2() throws PersistenceException {
		return em.createNamedQuery("Vw_Report2.findAll", Vw_Report2.class).getResultList();
	}

	// Report 5
	public List<TblUser> findAllInsolventTblUser() throws PersistenceException {
		return em.createNamedQuery("TblUser.findAllInsolvent", TblUser.class).getResultList();
	}

	// Report 5
	public List<TblOrder> findAllRejectedTblOrder() throws PersistenceException {
		return em.createNamedQuery("TblOrder.findAllRejected", TblOrder.class).getResultList();
	}

	// Report 5
	public List<TblAlert> findAllTblAlert() throws PersistenceException {
		return em.createNamedQuery("TblAlert.findAll", TblAlert.class).getResultList();
	}

	public List<Vw_Report6Best> findAllVw_Report6Best() throws PersistenceException {
		return em.createNamedQuery("Vw_Report6Best.findAll", Vw_Report6Best.class).getResultList();
	}

}
