package ch.lepeit.stundenabrechnung.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ch.lepeit.stundenabrechnung.model.Benutzer;
import ch.lepeit.stundenabrechnung.pass.PasswordHash;

@Stateless
public class LoginService {

	@PersistenceContext
	private EntityManager em;
	private Benutzer benutzer;

	/**
	 * Löschen eines Journaleintrages
	 * 
	 * @param j
	 */

	public Boolean getUserLogin(String benutzername, String passwort) {

		TypedQuery<Benutzer> tq = em.createQuery(
				"SELECT be FROM Benutzer be WHERE be.benutzername=:user ",
				Benutzer.class);
		tq.setParameter("user", benutzername);
		try {

			if (passwort.equals(tq.getSingleResult().getPasswort())) {
				this.setBenutzer(tq.getSingleResult());
				return true;
			} else {
				return false;
			}
		} catch (NoResultException e) {
			return false;
		}

	}

	public Boolean registerUser(Benutzer benutzer) {

		TypedQuery<Benutzer> tq = em.createQuery(
				"Select be From Benutzer be Where be.benutzername =:user",
				Benutzer.class);
		tq.setParameter("user", benutzer.getBenutzername());

		try {
			Benutzer benutzer1 = tq.getSingleResult();
			return false;
		} catch (NoResultException e) {
			this.benutzer = em.merge(benutzer);
			return true;
		}

	}

	public Benutzer getBenutzer() {
		return benutzer;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}
}
