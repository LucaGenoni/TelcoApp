package services;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.TblBill;
import entities.TblOrder;
import entities.TblUser;
import exceptions.OrderAlreadyPaidException;

@Stateless
public class BillService{
	@PersistenceContext(unitName = "telcoUser")
	private EntityManager em;

	public BillService() {
	}

	// create the bill
	public TblBill createBill(int ido, byte result) throws PersistenceException, OrderAlreadyPaidException{
		TblOrder o = em.find(TblOrder.class, ido);
		// in DB there are triggers that change its state and find is not able to fetch it. (I tested it)
        em.refresh(o);
		if (o.getIsValid()==1) throw new OrderAlreadyPaidException("Order already paid");
		
		TblBill b = new TblBill();
		b.setBillDate(new Date());
		b.setResult(result);
		b.setTblOrder(o);
		
        try {
            em.persist(b);
            em.flush();
            return b;
        } catch (PersistenceException e) {
            throw e;
        }
    }
	
}
