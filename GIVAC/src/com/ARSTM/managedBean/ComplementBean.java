package com.ARSTM.managedBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ARSTM.model.AnneesScolaire;
import com.ARSTM.requetes.ReqAnneeScolaire;
import com.ARSTM.service.Iservice;

@Component
@Scope("session")
public class ComplementBean {
	@Autowired
	private Iservice service;
	@Autowired
	ReqAnneeScolaire reqAnneeScolaire;

	private AnneesScolaire anneeScolaire = new AnneesScolaire();
	
	// Contrôle de coposant
	private CommandButton btnValider = new CommandButton();
	private CommandButton btnModifier = new CommandButton();
	private List listeEtudiant = new ArrayList<>();
	
	
	public List getListeEtudiant() {
		return listeEtudiant;
	}

	public void setListeEtudiant(List listeEtudiant) {
		this.listeEtudiant = listeEtudiant;
	}

	public void actualiserList(){
	}

	public void enregistrer() {
		
	}

	public void annuler() {
		btnValider.setDisabled(false);
		btnModifier.setDisabled(true);
		actualiserList();
	}
	
	
	/****** Accesseurs ***********************************/
	public Iservice getService() {
		return service;
	}

	public void setService(Iservice service) {
		this.service = service;
	}

	public CommandButton getBtnValider() {
		return btnValider;
	}

	public void setBtnValider(CommandButton btnValider) {
		this.btnValider = btnValider;
	}

	

	public AnneesScolaire getAnneeScolaire() {
		return anneeScolaire;
	}

	public void setAnneeScolaire(AnneesScolaire anneeScolaire) {
		this.anneeScolaire = anneeScolaire;
	}


	public CommandButton getBtnModifier() {
		return btnModifier;
	}

	public void setBtnModifier(CommandButton btnModifier) {
		this.btnModifier = btnModifier;
	}

	public ReqAnneeScolaire getReqAnneeScolaire() {
		return reqAnneeScolaire;
	}

	public void setReqAnneeScolaire(ReqAnneeScolaire reqAnneeScolaire) {
		this.reqAnneeScolaire = reqAnneeScolaire;
	}
}
