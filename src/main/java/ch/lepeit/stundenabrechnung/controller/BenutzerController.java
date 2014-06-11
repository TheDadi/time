package ch.lepeit.stundenabrechnung.controller;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Size;

import org.primefaces.context.RequestContext;

import ch.lepeit.stundenabrechnung.model.Benutzer;
import ch.lepeit.stundenabrechnung.pass.PasswordHash;
import ch.lepeit.stundenabrechnung.service.LoginService;

@ManagedBean
@SessionScoped
public class BenutzerController {

    @Size(min=1,max=45,message="Benutzername darf nicht leer sein!")
    private String benutzername;

    @Size(min=8,max=45,message="Passwort muss mindestens 8 Zeichen enthalten!")
    private String passwort;

    @Size(min=1, max=45,message="Vorname darf nicht leer sein!")
    private String vorname;
    @Size(min=1, max=45,message="Nachname darf nicht leer sein!")
    private String nachname;
    @Size(min=1, max=45,message="E-Mail Adresse darf nicht leer sein!")
    private String email;

    @EJB
    private LoginService loginService;

    private boolean online=false;

    public String getBenutzername() {
        return benutzername;
    }
    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }
    public String getPasswort() {
        return passwort;
    }
    public void setPasswort(String passwort) {
    	PasswordHash hash = new PasswordHash(passwort);
        this.passwort = hash.getPassword();
    }

    public String login(){
        if(loginService.getUserLogin(getBenutzername(), getPasswort())){
        	   online = true;
               benutzername="";
               passwort="";
               return "journal.xhtml";
        }else{
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Benutzername oder Passwort falsch", "Benutzername oder Passwort falsch"));
        	RequestContext context = RequestContext.getCurrentInstance();
        	context.execute("PF('dlg1').show();");
        	return "index.xhtml";
        }
    }

    public String logoff(){
        loginService.setBenutzer(null);
        online = false;
        return "logout.xhtml";
    }
    
    public String register(){
        if(loginService.registerUser(new Benutzer(nachname, vorname, benutzername, passwort, email))){
        	

        online = true;
        nachname = "";
        vorname="";
        benutzername="";
        passwort="";
        email="";        System.out.println("warum");
        return "journal.xhtml";

        }else{
        	
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Benutzername bereits verwendet", "Benutzername bereits verwendet"));
        	RequestContext context = RequestContext.getCurrentInstance();
        	context.execute("PF('dlg2').show();");
        	return "index.xhtml";
        }
    }
    
    public boolean isOnline() {
        return online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
    public String getVorname() {
        return vorname;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public LoginService getLoginService() {
		return loginService;
	}
	public String getNachname() {
        return nachname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
}
