package ch.lepeit.stundenabrechnung.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.postgresql.util.PSQLException;

import ch.lepeit.stundenabrechnung.model.Task;

/**
 * Service für den zugriff auf die Task Entität
 * 
 * @author Sven Tschui C910511
 * 
 */
@Stateless
public class TaskService {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;

    /**
     * Löschen eines Tasks über dessen Namen
     * 
     * @param name
     * Der Name des zu löschenden Task
     */
    public void delete(int id ) {
//        em.remove(em.createQuery("Select t From task where t.name =:name and t.benutzer = :benutzer",Task.class).setParameter("name", name).setParameter("benutzer", loginService.getBenutzer()).getSingleResult());
    	try{
    		em.remove(em.find(Task.class, id));
    		em.flush();
    		FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich gelöscht!", "Erfolgreich gelöscht!"));
    	}catch(PersistenceException e)
    	{
    		System.out.println("Exeption caught");
    		//e.printStackTrace();
    		FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), e.getMessage()));
    	}
        
    }

    /**
     * Suchen einer Task Entität mit dessen Name (Primärschlüssel)
     * 
     * @param name
     * Name des Tasks
     * @return Gefundener Task oder null
     */
    public Task getTask(int id) {
    	System.out.println("dada"+id);
        return em.find(Task.class, id);
//    	return em.createQuery("Select t From task where t.name =:name and t.benutzer = :benutzer",Task.class).setParameter("name", name).setParameter("benutzer", loginService.getBenutzer()).getSingleResult();
    }

    /**
     * Erstellt eine Liste aller Tasks
     * 
     * @return Liste aller Tasks
     */
    public List<Task> getTasks() {
        return em.createQuery("SELECT t FROM Task t where t.benutzer =:benutzer", Task.class).setParameter("benutzer",loginService.getBenutzer()).getResultList();
    }

    /**
     * Speichern eines Tasks
     * 
     * @param task
     * Der zu speichernde Task
     */
    public void save(Task task) {
    	try{
    		 em.persist(task);
    		 FacesContext.getCurrentInstance().addMessage(null,
    					new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich gespeichert!", "Erfolgreich gespreichert!"));
    		
    	}catch(PersistenceException e)
    	{
    		FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), e.getMessage()));
    	}
       
    }
}
