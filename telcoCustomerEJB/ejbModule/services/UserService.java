package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.TblOrder;
import entities.TblSchedule;
import entities.TblUser;
import exceptions.CredentialsException;
import exceptions.RoleException;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "telcoUser")
	private EntityManager em;

	public UserService() {
	}

	// Sign up
	public TblUser createUser(String username, String email, String password, int role)
			throws PersistenceException, CredentialsException, RoleException {
		List<TblUser> uList = em.createNamedQuery("TblUser.findByUsername", TblUser.class).setParameter(1, username)
				.getResultList();
		if (!uList.isEmpty())
			throw new CredentialsException("User already exists");
		if (role != 0 && role != 1)
			throw new RoleException("No such role");

		TblUser u = new TblUser();
		u.setUsername(username);
		u.setPassword(password);
		u.setEmail(email);
		u.setRole((byte) role);
		try {
			em.persist(u);
			em.flush();
			return u;
		} catch (PersistenceException e) {
			throw e;
		}
	}

	// Sign in
	public TblUser checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<TblUser> uList = null;
		try {
			uList = em.createNamedQuery("TblUser.checkSignIn", TblUser.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();

		} catch (PersistenceException e) {
			throw new CredentialsException("Wrong credentals");
		}
		if (uList.isEmpty())
			throw new CredentialsException("Incorrect username or passoword.");
		if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("Multiple users with the same credentials");
	}

	// weak authentication
	public void checkIfEmployee(int idUser) throws RoleException {
		TblUser u = em.find(TblUser.class, idUser);
		if (u.getRole() != 1)
			throw new RoleException("Not an employee");
	}

	// if updated
	public TblUser findUser(int idUser) {
		return em.find(TblUser.class, idUser);
	}

	public List<TblSchedule> findScheduleOfUser(int idUser) {
		TblUser u =  em.find(TblUser.class, idUser);
		return u.getTblSchedules();
	}
	public List<TblOrder> findAllRejectedOrdersOfUser(int idUser) throws PersistenceException {
		TblUser u = em.find(TblUser.class, idUser);
		return em.createNamedQuery("TblOrder.findAllRejectedOfUser", TblOrder.class).setParameter(1, u).getResultList();
//		Query q = em.createNamedQuery("TblOrder.findAllRejected", TblOrder.class);
//		return q.getResultList();
	}
	
}
