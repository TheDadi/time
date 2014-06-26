package ch.lepeit.stundenabrechnung.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ch.lepeit.stundenabrechnung.model.Journal;
import ch.lepeit.stundenabrechnung.model.Task;
import ch.lepeit.stundenabrechnung.service.JournalService;
import ch.lepeit.stundenabrechnung.service.TaskService;

@Named
@SessionScoped
public class KorrekturEditController implements Serializable, Observer {
    private static final long serialVersionUID = 20120524L;

    private Journal journal;

    @EJB
    private JournalService journalService;

    @Inject
    private KorrekturController korrekturController;

    private List<Task> tasks;

    @EJB
    private TaskService taskService;

    @PreDestroy
    public void destruct() {
        this.korrekturController.deleteObserver(this);
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournalService(JournalService journalService) {
		this.journalService = journalService;
	}
    
    public void setJournal(Journal journal) {
		this.journal = journal;
	}
    
    public int getTask() {
        if (journal == null || journal.getTask() == null) {
            return 0;
        }

        return journal.getTask().getTask_id();
    }

    public List<Task> getTasks() {
    	this.tasks = this.taskService.getTasks();
        return this.tasks;
    }

    @PostConstruct
    public void init() {
        this.tasks = taskService.getTasks();

        this.korrekturController.addObserver(this);
    }

    public String remove() {
    	FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich gelöscht!", "Erfolgreich gelöscht!"));

    	System.out.println("digi");
        journalService.delete(this.journal);
        this.journal = null;
		return null;
    }

    public String save() {

        journalService.update(this.journal);
		return null;
    }

    public void setTask(int task) {
        this.journal.setTask(taskService.getTask(task));
    }

    @Override
    public void update(Observable o, Object arg) {
    	System.out.println("update Journal");
        this.journal = korrekturController.getSelectedItem();
    }
    
}
