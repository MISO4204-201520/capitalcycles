package com.sofactory.app.configurador;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;
 
@ManagedBean
@ViewScoped
public class BicicletaWizard implements Serializable {
 
    private Bicicleta bicicleta = new Bicicleta();
     
    private boolean skip;
     
    public Bicicleta getBicicleta() {
        return bicicleta;
    }
 
    public void setBicicleta(Bicicleta bicicleta) {
        this.bicicleta = bicicleta;
    }
     
    public void save() {        
        FacesMessage msg = new FacesMessage("Successful", "Welcome :" + bicicleta.getMarca());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
}