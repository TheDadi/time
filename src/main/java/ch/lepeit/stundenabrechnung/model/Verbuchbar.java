package ch.lepeit.stundenabrechnung.model;

/**
 * Model Verbuchbar
 * 
 * @author Sven Tschui C910511
 * 
 */
public class Verbuchbar {
    private int verbuchbar;

    private double zeit;

    public Verbuchbar() {

    }

    public Verbuchbar(boolean verbuchbar, double zeit) {
        this.setVerbuchbar(verbuchbar);
        this.zeit = zeit;
    }
    
    
    public Verbuchbar(int verbuchbar, double zeit) {
        this.verbuchbar = verbuchbar;
        this.zeit = zeit;
    }

    public double getZeit() {
        return zeit;
    }

    public boolean isVerbuchbar() {
        if(this.verbuchbar == 0)
     	   return false;
        else
     	   return true;
      
    }

    public void setVerbuchbar(boolean verbuchbar) {
    	if(verbuchbar)
    		this.verbuchbar = 1;
    	else
			this.verbuchbar = 0;
    }

    public void setZeit(double zeit) {
        this.zeit = zeit;
    }
}