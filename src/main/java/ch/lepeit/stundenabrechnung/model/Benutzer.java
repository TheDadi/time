package ch.lepeit.stundenabrechnung.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;

/**
 * The persistent class for the benutzer database table.
 * 
 */
@Entity
@NamedQuery(name = "Benutzer.findAll", query = "SELECT b FROM Benutzer b")
public class Benutzer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "benutzer_id")
	private Integer benutzerId;

	private String benutzername;

	private String mailadr;

	private String nachname;

	private String passwort;

	private String vorname;

	// bi-directional many-to-one association to Benutzerrollen
	@ManyToOne
	private Benutzerrollen benutzerrollen;

	// bi-directional many-to-one association to Journal
	@OneToMany(mappedBy = "benutzer")
	private List<Journal> journals;

	//bi-directional many-to-one association to Task
	@OneToMany(mappedBy="benutzer")
	private List<Task> tasks;

	public Benutzer() {
	}

	public Benutzer(String nachname, String vorname, String benutzername,
			String passwort, String email) {
		this();
		this.vorname = vorname;
		this.nachname = nachname;
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.mailadr = email;
	}

	public Integer getBenutzerId() {
		return this.benutzerId;
	}

	public void setBenutzerId(Integer benutzerId) {
		this.benutzerId = benutzerId;
	}

	public String getBenutzername() {
		return this.benutzername;
	}

	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}

	public String getMailadr() {
		return this.mailadr;
	}

	public void setMailadr(String mailadr) {
		this.mailadr = mailadr;
	}

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public Benutzerrollen getBenutzerrollen() {
		return this.benutzerrollen;
	}

	public void setBenutzerrollen(Benutzerrollen benutzerrollen) {
		this.benutzerrollen = benutzerrollen;
	}

	public List<Journal> getJournals() {
		return this.journals;
	}

	public void setJournals(List<Journal> journals) {
		this.journals = journals;
	}

	public Journal addJournal(Journal journal) {
		getJournals().add(journal);
		journal.setBenutzer(this);

		return journal;
	}

	public Journal removeJournal(Journal journal) {
		getJournals().remove(journal);
		journal.setBenutzer(null);

		return journal;
	}
	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task addTask(Task task) {
		getTasks().add(task);
		task.setBenutzer(this);

		return task;
	}

	public Task removeTask(Task task) {
		getTasks().remove(task);
		task.setBenutzer(null);

		return task;
	}

}