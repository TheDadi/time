package ch.lepeit.stundenabrechnung.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
        this.em.remove(em.find(Journal.class, j.getNr()));
    }

    /**
     * Erstellt eine Liste von gruppierten Journaleinträgen für einen Wochentag.
     * 
     * @param tag
     * Tag, für welchen die Liste erstellt werden soll.
     * @return Liste der gruppierten Journaleinträge
     */
    public List<GroupedJournal> getGroupedJournals(Date tag) {
    	System.out.println("ich bin hier");
    	System.out.println(em +" : em");
        TypedQuery<GroupedJournal> journals = em
                .createQuery(
                        "SELECT new ch.lepeit.stundenabrechnung.model.GroupedJournal(jour.datum, SUM(jour.stunden), jour.task,  min(jour.plantaverbucht)) FROM Journal jour WHERE jour.datum = :tag and jour.benutzer=:benutzer GROUP BY jour.datum, jour.task",
                        GroupedJournal.class);

        journals.setParameter("tag", tag);
        journals.setParameter("benutzer", loginService.getBenutzer());
        System.out.println("nach dem Zugriff");
        System.out.println(journals.getResultList()+"haha");

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
                        "SELECT new ch.lepeit.stundenabrechnung.model.GroupedJournal(j.datum, SUM(j.stunden), j.task, min(j.plantaverbucht)) FROM Journal j WHERE YEAR(j.datum) = :jahr AND MONTH(j.datum) = :monat AND j.task.verbuchbar = 0 and j.benutzer=:benutzer GROUP BY j.datum, j.task ORDER BY j.datum ASC",
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
                        "SELECT new ch.lepeit.stundenabrechnung.model.GroupedJournal(j.datum, SUM(j.stunden), j.task, min(j.plantaverbucht)) FROM Journal j WHERE j.plantaverbucht = 0 AND j.task.verbuchbar = 1 and j.benutzer=:benutzer GROUP BY j.datum, j.task ORDER BY j.task ASC, j.datum ASC",
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
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "save_merge", "save_merge"));
        try{
    	em.persist(j);
        }catch(RuntimeException e)
        {
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, e.getLocalizedMessage(), e.getMessage()));
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), e.getMessage()));
        	throw e; 
        }
        
    }

    /**
     * Speichern/erfassten eines bestehenden Journaleintrages
     * 
     * @param j
     * Der zu speichernde Journaleintrag
     */
    public void update(Journal j) {
        em.merge(j);
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
                        "SELECT j FROM Journal j WHERE j.plantaverbucht = 0 AND j.datum = :datum AND j.task.name = :task and j.benutzer=:benutzer",
                        Journal.class).setParameter("datum", datum).setParameter("task", task).setParameter("benutzer", loginService.getBenutzer()).getResultList();
        for (Journal j : list) {
            j.setPlantaverbucht(true);
            em.persist(j);
        }

    }
}