package ch.lepeit.stundenabrechnung.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ch.lepeit.stundenabrechnung.model.GroupedJournal;
import ch.lepeit.stundenabrechnung.model.Journal;

/**
 * Service für den Zugriff auf (gruppierte) Journaleinträge.
 * 
 * @author Sven Tschui C910511
 * 
 */
@Stateless
public class JournalService {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;

    /**
     * Löschen eines Journaleintrages
     * 
     * @param j
     */
    public void delete(Journal j) {
    	try{
    		this.em.remove(em.find(Journal.class, j.getNr()));
    		FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich gelöscht!", "Erfolgreich gelöscht!"));
    	}catch(Exception e)
    	{
    		FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), e.getMessage()));
    	}
        
        
    }

    /**
     * Erstellt eine Liste von gruppierten Journaleinträgen für einen Wochentag.
     * 
     * @param tag
     * Tag, für welchen die Liste erstellt werden soll.
     * @return Liste der gruppierten Journaleinträge
     */
    public List<GroupedJournal> getGroupedJournals(Date tag) {
 
        TypedQuery<GroupedJournal> journals = em
                .createQuery(
                        "SELECT new ch.lepeit.stundenabrechnung.model.GroupedJournal(jour.datum, SUM(jour.stunden), jour.task) FROM Journal jour WHERE jour.datum = :tag and jour.benutzer=:benutzer GROUP BY jour.datum, jour.task",
                        GroupedJournal.class);

        journals.setParameter("tag", tag);
        journals.setParameter("benutzer", loginService.getBenutzer());

        return journals.getResultList();
    }

    /**
     * Erstellt eine Liste von Journaleinträgen für einen Wochentag.
     * 
     * @param tag
     * Tag, für welchen die Liste erstellt werden soll.
     * @return Liste der Journaleinträge
     */
    public List<Journal> getJournals(Date tag) {
        TypedQuery<Journal> journals = em.createQuery("SELECT j FROM Journal j WHERE j.datum = :tag and and j.benutzer=:benutzer", Journal.class);

        journals.setParameter("tag", tag);
        journals.setParameter("benutzer", loginService.getBenutzer());

        return journals.getResultList();
    }
    /**
     * Erstellt eine Liste von Journaleinträgen für einen Wochentag.
     * 
     * @param tag
     * Tag, für welchen die Liste erstellt werden soll.
     * @return Liste der Journaleinträge
     */
    public List<Journal> getJournals() {
        TypedQuery<Journal> journals = em.createQuery("SELECT j FROM Journal j WHERE j.benutzer=:benutzer ORDER BY nr Desc", Journal.class);
        journals.setParameter("benutzer", loginService.getBenutzer());

        return journals.getResultList();
    }

    /**
     * Erstellt eine Liste von gruppierten Journaleinträgen, welche mit einem nicht verbuchbaren Task verknüpft sind,
     * für einen Wochentag.
     * 
     * @param tag
     * Tag, für welchen die Liste erstellt werden soll.
     * @return Liste der gruppierten Journaleinträge
     */
    public List<GroupedJournal> getNichtVerbuchbarGroupedJournals(Date monat) {
        TypedQuery<GroupedJournal> journals = em
                .createQuery(
                        "SELECT new ch.lepeit.stundenabrechnung.model.GroupedJournal(j.datum, SUM(j.stunden), j.task) FROM Journal j WHERE YEAR(j.datum) = :jahr AND MONTH(j.datum) = :monat AND j.task.verbuchbar = FALSE and j.benutzer=:benutzer GROUP BY j.datum, j.task ORDER BY j.datum ASC",
                        GroupedJournal.class);

        Calendar c = new GregorianCalendar();
        c.setTime(monat);

        journals.setParameter("jahr", c.get(Calendar.YEAR));
        journals.setParameter("monat", c.get(Calendar.MONTH) + 1); // + 1 Da Calendar Monate von 0 aus zählt
        journals.setParameter("benutzer", loginService.getBenutzer());

        return journals.getResultList();
    }

    /**
     * Erstellt eine Liste von gruppierten Journaleinträgen welche nicht verbucht sind.
     * 
     * @return Liste der nicht verbuchten gruppierten Journaleinträge
     */
    public List<GroupedJournal> getNichtVerbuchteGroupedJournals() {
        TypedQuery<GroupedJournal> journals = em
                .createQuery(
                        "SELECT new ch.lepeit.stundenabrechnung.model.GroupedJournal(j.datum, SUM(j.stunden), j.task) FROM Journal j WHERE j.plantaverbucht = FALSE AND j.task.verbuchbar = TRUE and j.benutzer=:benutzer GROUP BY j.datum, j.task ORDER BY j.task ASC, j.datum ASC",
                        GroupedJournal.class);
        journals.setParameter("benutzer", loginService.getBenutzer());

        return journals.getResultList();
    }

    /**
     * Speichern/erfassten eines neuen Journaleintrages
     * 
     * @param j
     * Der zu speichernde Journaleintrag
     
     */
	public void save(Journal j) {
    
    	em.persist(j);
    	
        
    }

    /**
     * Speichern/erfassten eines bestehenden Journaleintrages
     * 
     * @param j
     * Der zu speichernde Journaleintrag
     */
    public void update(Journal j) {
    	try{
        em.merge(j);
    	FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich gespeichert!", "Erfolgreich gespeichert!"));
    	}catch(Exception e )
        {
        	System.out.println(e.getMessage());
        	FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), e.getMessage()));
        }
    }

    /**
     * Ändern des verbucht Status aller Journaleinträge mit einem bestimmten Task an einem bestimmten Tag
     * 
     * @param datum
     * Tag, von welchem die Einträge auf verbucht gesetzt werden sollen
     * @param task
     * Task, von welchem die Einträge auf verbucht gesetzt werden sollen
     */
    public void verbuchen(Date datum, String task) {
        List<Journal> list = em
                .createQuery(
                        "SELECT j FROM Journal j WHERE j.plantaverbucht = FALSE AND j.datum = :datum AND j.task.name = :task and j.benutzer=:benutzer",
                        Journal.class).setParameter("datum", datum).setParameter("task", task).setParameter("benutzer", loginService.getBenutzer()).getResultList();
        for (Journal j : list) {
            j.setPlantaverbucht(true);
            em.persist(j);
        }

    }
}