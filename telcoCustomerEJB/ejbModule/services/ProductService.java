package services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import entities.TblOptional;
import entities.TblPackage;
import entities.TblPeriod;
import entities.TblService;
import entities.TblUser;
import exceptions.IntegrityOptionalException;
import exceptions.IntegrityPackageException;
import exceptions.RoleException;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "telcoUser")
	private EntityManager em;

	public ProductService() {
	}

	// get one packages
	public TblPackage findSrvpackage(int id) throws PersistenceException {
		return em.find(TblPackage.class, id);
	}

	// get all packages
	public List<TblPackage> findAllPackages() throws PersistenceException {
		return em.createNamedQuery("TblPackage.findAll", TblPackage.class).getResultList();
	}

	public List<TblService> findAllServices() throws PersistenceException {
		return em.createNamedQuery("TblService.findAll", TblService.class).getResultList();
	}

	public List<TblPeriod> findAllPeriods() throws PersistenceException {
		return em.createNamedQuery("TblPeriod.findAll", TblPeriod.class).getResultList();
	}

	public List<TblOptional> findAllOptionals() throws PersistenceException {
		return em.createNamedQuery("TblOptional.findAll", TblOptional.class).getResultList();
	}

	// create package need auth
	public TblPackage createSrvpackage(String name, List<Integer> idservices, List<Integer> idperiods,
			List<Integer> idoptionals, int idUser)
			throws PersistenceException, RoleException, IntegrityPackageException {
		TblUser u = em.find(TblUser.class, idUser);
		ArrayList<TblService> services = new ArrayList<>();
		ArrayList<TblPeriod> periods = new ArrayList<>();
		ArrayList<TblOptional> optionals = new ArrayList<>();
		for (int id : idservices)
			services.add(em.find(TblService.class, id));
		for (int id : idperiods)
			periods.add(em.find(TblPeriod.class, id));
		for (int id : idoptionals)
			optionals.add(em.find(TblOptional.class, id));

		// checks
		if (u.getRole() != 1)
			throw new RoleException("A non employee cannot create a new package");
		if (!em.createNamedQuery("TblPackage.findByName", TblUser.class).setParameter(1, name).getResultList()
				.isEmpty())
			throw new IntegrityPackageException("Package already exists");
		try {
			services.stream().collect(Collectors.groupingBy(TblService::getType)).forEach((k, v) -> {
				if (v.size() > 1)
					throw new RuntimeException("You can add only one service per type");
			});
			periods.stream().collect(Collectors.groupingBy(TblPeriod::getMonths)).forEach((k, v) -> {
				if (v.size() > 1)
					throw new RuntimeException("You can add only one period with distinct duration");
			});
		} catch (Exception e) {
			throw new IntegrityPackageException(e.getMessage());
		}

		TblPackage p = new TblPackage();
		p.setName(name);
		p.setTblServices(services);
		p.setTblPeriods(periods);
		p.setTblOptionals(optionals);
		try {
			em.persist(p);
			em.flush();
			return p;
		} catch (PersistenceException e) {
			throw e;
		}
	}
	
	// create optional need auth
	public TblOptional createOptional(String name, double fee, int idUser) throws PersistenceException, RoleException,IntegrityOptionalException {
		TblUser u = em.find(TblUser.class, idUser);
		// checks
		if (u.getRole() != 1)
			throw new RoleException("A non employee cannot create a new optional");
		if (!em.createNamedQuery("TblOptional.findByName", TblUser.class).setParameter(1, name).getResultList()
				.isEmpty())
			throw new IntegrityOptionalException("Package already exists");
		TblOptional o = new TblOptional();
		o.setName(name);
		o.setFee(fee);
		try {
			em.persist(o);
			em.flush();
			return o;
		} catch (PersistenceException e) {
			throw e;
		}
	}

}
