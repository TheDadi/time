package ch.lepeit.stundenabrechnung.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the benutzerrollen database table.
 * 
 */
@Entity
public class Benutzerrollen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="rollen_id")
	private Integer rollenId;

	@Column(name="rollen_name")
	private String rollenName;

	//bi-directional many-to-one association to Benutzer
	@OneToMany(mappedBy="benutzerrollen")
	private List<Benutzer> benutzers;

	public Benutzerrollen() {
	}

	public Integer getRollenId() {
		return this.rollenId;
	}

	public void setRollenId(Integer rollenId) {
		this.rollenId = rollenId;
	}

	public String getRollenName() {
		return this.rollenName;
	}

	public void setRollenName(String rollenName) {
		this.rollenName = rollenName;
	}

	public List<Benutzer> getBenutzers() {
		return this.benutzers;
	}

	public void setBenutzers(List<Benutzer> benutzers) {
		this.benutzers = benutzers;
	}

	public Benutzer addBenutzer(Benutzer benutzer) {
		getBenutzers().add(benutzer);
		benutzer.setBenutzerrollen(this);

		return benutzer;
	}

	public Benutzer removeBenutzer(Benutzer benutzer) {
		getBenutzers().remove(benutzer);
		benutzer.setBenutzerrollen(null);

		return benutzer;
	}

}