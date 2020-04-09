package com.ARSTM.managedBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ARSTM.model.AnneesScolaire;
import com.ARSTM.model.Ecolages;
import com.ARSTM.model.Ecole;
import com.ARSTM.model.Filieres;
import com.ARSTM.model.FraisAnnexe;
import com.ARSTM.model.Mention;
import com.ARSTM.model.MentionEcolage;
import com.ARSTM.model.MentionEcolageId;
import com.ARSTM.requetes.ReqAnneeScolaire;
import com.ARSTM.requetes.ReqTypeNationalite;
import com.ARSTM.requetes.RequeteFiliere;
import com.ARSTM.requetes.RequeteMention;
import com.ARSTM.service.Iservice;

@Component
@Scope("session")
public class FraisScolaireBean {
	
	@Autowired
	Iservice service;
	
	@Autowired
	RequeteFiliere requeteFiliere;
	@Autowired
	RequeteMention requeteMention;
	@Autowired
	ReqAnneeScolaire reqAnneeScolaire;
	@Autowired
	ReqTypeNationalite reqTypeNationalite;
	
	private Ecole choosedEcole = new Ecole();
	private List listEcole = new ArrayList<>();
	private Filieres choosedFiliere = new Filieres();
	private List listFiliere = new ArrayList<>();
	private Mention choosedMention = new Mention();
	private List listeMention = new ArrayList<>();
	private BigDecimal fraisExam;
	
	private Ecolages ecolageNation = new Ecolages();
	private Ecolages ecolageNonNation = new Ecolages();
	private FraisAnnexe fraisAnnexe = new FraisAnnexe();
	private FraisAnnexe fraisAnnexeNonNation = new FraisAnnexe();
	private AnneesScolaire anneEncoure = new AnneesScolaire();
	
	@PostConstruct
	public AnneesScolaire recupererAnne(){
		anneEncoure = reqAnneeScolaire.recupererDerniereAnneeScolaire().get(0);
		System.out.println("Année:"+anneEncoure.getLibAnneeScolaire());
		return anneEncoure;
	}
	
	public String enregistrer(){
		enregistrerFrais();
		enregistrerEcolage();
		enregistrerMentionEcolage();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Enregistrement effcetué!", null));
		return "frais_scolaire.xhtml";
	}
	
	
	public void enregistrerFrais(){
		
		//Recuperer ladernière année scolaire et l'affecter dans l'ojet FraisAnnexe
		fraisAnnexe.setAnneesScolaire(anneEncoure);
		fraisAnnexe.setTypenationalite(reqTypeNationalite.recupererTypeNationalite(1).get(0));
		//fraisAnnexe.setMention(choosedMention);
		service.addObject(fraisAnnexe);
	}
	
	
	public void enregistrerEcolage(){
		//Setter les type de nationalite et frais d'exam
		ecolageNation.setTypenationalite(reqTypeNationalite.recupererTypeNationalite(1).get(0));
		ecolageNation.setFraisExam(fraisExam);
		ecolageNonNation.setTypenationalite(reqTypeNationalite.recupererTypeNationalite(2).get(0));
		ecolageNonNation.setFraisExam(fraisExam);
		
		//Faire l'enregistrement
		service.addObject(ecolageNation);
		service.addObject(ecolageNonNation);
	}
	
	public void enregistrerMentionEcolage() {
		System.out.println("VRIFICATION Année:"+anneEncoure.getLibAnneeScolaire());
		
		//Créer les objets MentionEcolage
				//Pour les nationaux
				MentionEcolage mentionEcolageNation = new MentionEcolage();
				MentionEcolageId  menTEcoNatId = new MentionEcolageId(ecolageNation.getCodeEcolage(), choosedMention.getCodeMention());
				mentionEcolageNation.setId(menTEcoNatId);
				mentionEcolageNation.setEcolages(ecolageNation);
				mentionEcolageNation.setMention(choosedMention);
				mentionEcolageNation.setAnneeScolaireEcolage(anneEncoure.getLibAnneeScolaire());
				
				//Pour les non nationaux
				MentionEcolage mentionEcolageNonNation = new MentionEcolage();
				MentionEcolageId  menTEcoNonNatId = new MentionEcolageId(ecolageNonNation.getCodeEcolage(), choosedMention.getCodeMention());
				mentionEcolageNonNation.setId(menTEcoNonNatId);
				mentionEcolageNonNation.setEcolages(ecolageNonNation);
				mentionEcolageNonNation.setMention(choosedMention);
				mentionEcolageNonNation.setAnneeScolaireEcolage(anneEncoure.getLibAnneeScolaire());
				
				service.addObject(mentionEcolageNation);
				service.addObject(mentionEcolageNonNation);
		
	}

	public Iservice getService() {
		return service;
	}
	
	public void chargerFiliere(){
		listFiliere.clear();
		listFiliere = requeteFiliere.recupFiliereByEcole2(choosedEcole.getCodeEcole());
		
		
		
	}
	public void chargerMention(){
		listeMention.clear();
		listeMention = requeteMention.recupMentionByEcoleFiliere(choosedFiliere.getCodeFiliere());
	}
	
	
	
	 public String onFlowProcess(FlowEvent event) {
	            return event.getNewStep();
	    }
	
	
	
	public void setService(Iservice service) {
		this.service = service;
	}
	public Ecole getChoosedEcole() {
		return choosedEcole;
	}
	public void setChoosedEcole(Ecole choosedEcole) {
		this.choosedEcole = choosedEcole;
	}
	public List getListEcole() {
		if (listEcole.isEmpty()) {
			listEcole = getService().getObjects("Ecole");
		}
		return listEcole;
	}
	public void setListEcole(List listEcole) {
		this.listEcole = listEcole;
	}
	public Filieres getChoosedFiliere() {
		return choosedFiliere;
	}
	public void setChoosedFiliere(Filieres choosedFiliere) {
		this.choosedFiliere = choosedFiliere;
	}
	public List getListFiliere() {
		return listFiliere;
	}
	public void setListFiliere(List listFiliere) {
		this.listFiliere = listFiliere;
	}
	public Mention getChoosedMention() {
		return choosedMention;
	}
	public void setChoosedMention(Mention choosedMention) {
		this.choosedMention = choosedMention;
	}
	public List getListeMention() {
		return listeMention;
	}
	public void setListeMention(List listeMention) {
		this.listeMention = listeMention;
	}


	public Ecolages getEcolageNation() {
		return ecolageNation;
	}

	public void setEcolageNation(Ecolages ecolageNation) {
		this.ecolageNation = ecolageNation;
	}

	public Ecolages getEcolageNonNation() {
		return ecolageNonNation;
	}

	public void setEcolageNonNation(Ecolages ecolageNonNation) {
		this.ecolageNonNation = ecolageNonNation;
	}

	public FraisAnnexe getFraisAnnexe() {
		return fraisAnnexe;
	}

	public void setFraisAnnexe(FraisAnnexe fraisAnnexe) {
		this.fraisAnnexe = fraisAnnexe;
	}

	public FraisAnnexe getFraisAnnexeNonNation() {
		return fraisAnnexeNonNation;
	}

	public void setFraisAnnexeNonNation(FraisAnnexe fraisAnnexeNonNation) {
		this.fraisAnnexeNonNation = fraisAnnexeNonNation;
	}

	public BigDecimal getFraisExam() {
		return fraisExam;
	}

	public void setFraisExam(BigDecimal fraisExam) {
		this.fraisExam = fraisExam;
	}


}
