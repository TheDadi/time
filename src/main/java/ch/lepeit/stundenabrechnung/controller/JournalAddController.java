package ch.lepeit.stundenabrechnung.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ch.lepeit.stundenabrechnung.model.Benutzer;
import ch.lepeit.stundenabrechnung.model.Journal;
import ch.lepeit.stundenabrechnung.model.Task;
import ch.lepeit.stundenabrechnung.service.JournalService;
import ch.lepeit.stundenabrechnung.service.LoginService;
import ch.lepeit.stundenabrechnung.service.TaskService;

import java.io.Serializable;

/**
 * ViewController für das Hinzufügen eines Journaleintrages (journalAdd.xhtml)
 * 
 * Stellt eine Funktion zum speichern/erfassen eines Journaleintrages bereit. Es wird eine Liste aller Tasks für das
 * DropDown bereitgestellt.
 * 
 * @author Sven Tschui C910511
 * 
 */
@Named
@RequestScoped
public class JournalAddController implements Serializable {
    

	private static final long serialVersionUID = 7688372061609665368L;
	
	private String bemerkung;

	private Date datum;


	private int plantaverbucht;

	private Double stunden;
	
	private Task task;





	public void setTask(Task task) {
	}


	private Benutzer benutzer;

	private Journal journal;

    @Inject
    private JournalController journalController;

    
    @EJB
    private LoginService loginService;
    
    public Double getStunden() {
		return stunden;
	}


	public void setStunden(Double stunden) {
		this.stunden = stunden;
	}


	@EJB
    private JournalService journalService;

    private List<Task> tasks;

    @EJB
    private TaskService taskService;

    public Journal getJournal() {
        return journal;
    }
    

    public String getTask() {
        if (this.journal == null || this.journal.getTask() == null) {
            return null;
        }

        return task.getName();
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    @PostConstruct
    public void init() {
        // Leeres Journal für das Hinzufpgen-Formular
        this.journal = new Journal();

        this.tasks = this.taskService.getTasks();
    }

    public String save() {
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "save", "save"));
        // Journal speichern
    	
    	Journal journal = new Journal();
    	journal.setBenutzer(loginService.getBenutzer());
  
  
    	journal.setBemerkung(bemerkung);
    	journal.setDatum(datum);
    	journal.setStunden(stunden);
    	journal.setTask(task);
    	
        journalService.save(journal);

        // Neues leeres Journal
        this.journal = new Journal();
        bemerkung= "";
        datum = new Date();
        stunden = null;
        task = new Task();

        
        // Daten der Wochenübersicht neu laden
        this.journalController.reload();
       
        
        

        return null;
    }

  
    public void setTask(String task) {
    	this.task = this.taskService.getTask(task);
    	
        this.journal.setTask(this.taskService.getTask(task));
    }


	public void setJournal(Journal journal) {
		this.journal = journal;
	}
	public Date getDatum() {
		return datum;
	}


	public void setDatum(Date datum) {
		this.datum = datum;
	}


	public int getPlantaverbucht() {
		return plantaverbucht;
	}


	public void setPlantaverbucht(int plantaverbucht) {
		this.plantaverbucht = plantaverbucht;
	}


	public Benutzer getBenutzer() {
		return benutzer;
	}


	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}




	public String getBemerkung() {
		return bemerkung;
	}


	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
    
}
