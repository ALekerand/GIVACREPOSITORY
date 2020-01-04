package com.ARSTM.managedBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ARSTM.model.Ecole;
import com.ARSTM.model.Enseignant;
import com.ARSTM.model.EnseignatStatut;
import com.ARSTM.model.EnseignatStatutId;
import com.ARSTM.model.Etudiants;
import com.ARSTM.model.Filieres;
import com.ARSTM.model.Mention;
import com.ARSTM.model.Nationalites;
import com.ARSTM.model.Pays;
import com.ARSTM.model.Regime;
import com.ARSTM.model.Section;
import com.ARSTM.model.Sexe;
import com.ARSTM.model.Specialite;
import com.ARSTM.model.Statut;
import com.ARSTM.model.Tformation;
import com.ARSTM.model.TypeLogement;
import com.ARSTM.model.UserAuthentication;
import com.ARSTM.model.UserAuthorization;
import com.ARSTM.requetes.RequeteEnseignant;
import com.ARSTM.requetes.RequeteFiliere;
import com.ARSTM.requetes.RequeteInscription;
import com.ARSTM.requetes.RequeteMention;
import com.ARSTM.requetes.RequeteSection;
import com.ARSTM.service.Iservice;

@Component
@Scope("session")
public class InscriptionBean {
	@Autowired
	Iservice service;
	@Autowired
	//RequeteEnseignant   requeteEnseignant;
	RequeteInscription requeteInscription;
	@Autowired
	RequeteFiliere requeteFiliere;
	@Autowired
	RequeteMention requeteMention;
	@Autowired
	RequeteSection requeteSection;
	
	//private Enseignant enseignant = new Enseignant();
	private Etudiants etudiants = new Etudiants();
	private Enseignant selectedEnseignant = new Enseignant();
	//private List listEnseignant = new ArrayList<>();
	private Sexe chooseedSexe = new Sexe();
	private Pays choosedPays =  new Pays();
	private Ecole choosedEcole = new Ecole();
	private Filieres choosedFiliere = new Filieres();
	private Mention choosedMention = new Mention();
	private Section choosedSection = new Section();
	private Regime choosedRegime = new Regime();
	private TypeLogement choosedTypeLogement = new TypeLogement();
	private Tformation choosedTformation = new Tformation();
	private Nationalites choosedNationalites = new Nationalites();
	private List listeSexe = new ArrayList<>();
	private List listePays = new ArrayList<>();
	private List listeEcole = new ArrayList<>();
	private List listeTformation =new ArrayList<>();
	private List listeFiliere = new ArrayList<>();
	private List listeMention = new ArrayList<>();
	private List listeSection = new ArrayList<>();
	private List listeRegime = new ArrayList<>();
	private List listeTypeLogement = new ArrayList<>();
	private List listeNationalites = new ArrayList<>();
	private UserAuthentication userAuthentication = new UserAuthentication();
	private UserAuthorization userAuthorization = new UserAuthorization();
	private int maxMatricule = (int) 0;
	
	private String destination="C:\\GIVAC\\PHOTO";

	// Contrôle de composant
	private CommandButton btnValider = new CommandButton();
	private InputText inputVhOblig = new InputText();
	private OutputLabel outputVhOblig = new OutputLabel();

	public void enregistrer(){
		//enregistrer dans la table UserAuthentication
		/*
		 * userAuthentication.setUsername(enseignant.getUsername());
		 * userAuthentication.setPassword(enseignant.getPassword());
		 * userAuthentication.setEnabled(true);
		 * userAuthentication.setNom(enseignant.getNom());
		 * userAuthentication.setPrenoms(enseignant.getPrenoms());
		 * userAuthentication.setSexe(chooseedSexe);
		 * userAuthentication.setPhone1(enseignant.getPhone1());
		 * userAuthentication.setPhone2(enseignant.getPhone2());
		 * userAuthentication.setPhoto(enseignant.getPhoto());
		 * userAuthentication.setLieuNais(enseignant.getLieuNais());
		 * userAuthentication.setDateNais(enseignant.getDateNais());
		 * userAuthentication.setEmail(enseignant.getEmail());
		 */
		
		//enregistrer das la table Enseignant
		/*
		 * enseignant.setMatriculeEns(maxMatricule);
		 * enseignant.setCodeSexe(chooseedSexe.getCodeSexe());
		 * enseignant.setSpecialite(choosedSpecialite); enseignant.setEnabled(true);
		 * enseignant.setEtatEnseignant(true);
		 * getService().addObject(userAuthentication);
		 * enseignant.setUserAuthentication(userAuthentication);
		 * getService().addObject(enseignant);
		 */

		//enregistrer dans la table Enseigant_Statut		
		/*
		 * EnseignatStatutId enseignatStatutId = new
		 * EnseignatStatutId(enseignant.getUserId(), choosedStatut.getCodeStatut());
		 * EnseignatStatut enseignatStatut = new EnseignatStatut(enseignatStatutId,
		 * enseignant, choosedStatut); getService().addObject(enseignatStatut);
		 */

		//Enregistrer l'autorisation
		userAuthorization.setUserAuthentication(userAuthentication);
		userAuthorization.setRole("ROLE_ENSEIGNANT");
		getService().addObject(userAuthorization);

		actualiserList();
		
		vider(etudiants);
		maxMatricule = (int) 0;
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Enregistrement effcetué!", null));
	}

	public void annuler() {
		btnValider.setDisabled(false);
		vider(etudiants);
		actualiserList();
	}

	public void vider(Etudiants objEtudiants) {
		/*
		 * objEnseignant.setDateNais(null); objEnseignant.setLieuNais(null);
		 * objEnseignant.setEmail(null); objEnseignant.setNom(null);
		 * objEnseignant.setPrenoms(null); objEnseignant.setPhone1(null);
		 * objEnseignant.setPhone2(null); objEnseignant.setUsername(null);
		 * objEnseignant.setPassword(null); objEnseignant.setVhObligatoireSemaine(null);
		 */
	}
	

	public void actualiserList(){
		//listEnseignant.clear();
		//listEnseignant = getService().getObjects("Enseignant");
	}
	
	public void chargerFiliere(){
		listeFiliere.clear();
		listeFiliere = requeteFiliere.recupFiliereByEcole(choosedEcole.getCodeEcole(),choosedTformation.getCodeTformation());
	}

	public void chargerMention(){
		listeMention.clear();
		listeMention = requeteMention.recupMentionByEcoleFiliere(choosedFiliere.getCodeFiliere());
	}
	
	
	public void chargerSection(){
		listeSection.clear();
		listeSection = requeteSection.recupSectionByMention(choosedMention.getCodeMention());
	}

	
public void upload(FileUploadEvent event) { 
		       
		try {
		copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
		
		//Mis à jour dans la table enseignant
		//etudiants.setPhoto(destination);
		} catch (IOException e) {
		e.printStackTrace();
		}
		} 
	
	
		public void copyFile(String fileName, InputStream in) {
		try {
		// write the inputStream to a FileOutputStream
		OutputStream out = new FileOutputStream(new File(destination + fileName));
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = in.read(bytes)) != -1) {
		out.write(bytes, 0, read);
		}
		in.close();
		out.flush();
		out.close();
		System.out.println("New file created!");//Clean after
		} catch (IOException e) {
		System.out.println(e.getMessage());//Clean after

		}
		}


	//**************************ACCESSEURS*************************//*

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
	public Etudiants getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(Etudiants etudiants) {
		this.etudiants = etudiants;
	}

	public Enseignant getSelectedEnseignant() {
		return selectedEnseignant;
	}

	public void setSelectedEnseignant(Enseignant selectedEnseignant) {
		this.selectedEnseignant = selectedEnseignant;
	}

	public List getListeSexe() {
		if (listeSexe.isEmpty()) {
			listeSexe = getService().getObjects("Sexe");
		}
		return listeSexe;
	}

	public void setListeSexe(List listeSexe) {
		this.listeSexe = listeSexe;
	}

	public Sexe getChooseedSexe() {

		return chooseedSexe;
	}

	public void setChooseedSexe(Sexe chooseedSexe) {
		this.chooseedSexe = chooseedSexe;
	}

	public Pays getChoosedPays() {
		return choosedPays;
	}

	public void setChoosedPays(Pays choosedPays) {
		this.choosedPays = choosedPays;
	}

	public Nationalites getChoosedNationalites() {
		return choosedNationalites;
	}

	public void setChoosedNationalites(Nationalites choosedNationalites) {
		this.choosedNationalites = choosedNationalites;
	}

	public Ecole getChoosedEcole() {
		return choosedEcole;
	}

	public void setChoosedEcole(Ecole choosedEcole) {
		this.choosedEcole = choosedEcole;
	}

	public Tformation getChoosedTformation() {
		return choosedTformation;
	}

	public void setChoosedTformation(Tformation choosedTformation) {
		this.choosedTformation = choosedTformation;
	}

	public Filieres getChoosedFiliere() {
		return choosedFiliere;
	}

	public void setChoosedFiliere(Filieres choosedFiliere) {
		this.choosedFiliere = choosedFiliere;
	}

	public Mention getChoosedMention() {
		return choosedMention;
	}

	public void setChoosedMention(Mention choosedMention) {
		this.choosedMention = choosedMention;
	}

	public Section getChoosedSection() {
		return choosedSection;
	}

	public void setChoosedSection(Section choosedSection) {
		this.choosedSection = choosedSection;
	}

	public Regime getChoosedRegime() {
		return choosedRegime;
	}

	public void setChoosedRegime(Regime choosedRegime) {
		this.choosedRegime = choosedRegime;
	}

	public TypeLogement getChoosedTypeLogement() {
		return choosedTypeLogement;
	}

	public void setChoosedTypeLogement(TypeLogement choosedTypeLogement) {
		this.choosedTypeLogement = choosedTypeLogement;
	}

	public List getListePays() {
		if (listePays.isEmpty()) {
			listePays = getService().getObjects("Pays");
		}
		return listePays;
	}

	public void setListePays(List listePays) {
		this.listePays = listePays;
	}

	public List getListeNationalites() {
		if (listeNationalites.isEmpty()) {
			listeNationalites = getService().getObjects("Nationalites");
		}
		return listeNationalites;
	}

	public void setListeNationalites(List listeNationalites) {
		this.listeNationalites = listeNationalites;
	}

	public List getListeEcole() {
		if (listeEcole.isEmpty()) {
			listeEcole = getService().getObjects("Ecole");
		}
		return listeEcole;
	}

	public void setListeEcole(List listeEcole) {
		this.listeEcole = listeEcole;
	}

	public List getListeTformation() {
		if (listeTformation.isEmpty()) {
			listeTformation = getService().getObjects("Tformation");
		}
		return listeTformation;
	}

	public void setListeTformation(List listeTformation) {
		this.listeTformation = listeTformation;
	}

	public List getListeFiliere() {
		return listeFiliere;
	}

	public void setListeFiliere(List listeFiliere) {
		this.listeFiliere = listeFiliere;
	}

	public List getListeMention() {
		return listeMention;
	}

	public void setListeMention(List listeMention) {
		this.listeMention = listeMention;
	}

	public List getListeSection() {
		return listeSection;
	}

	public void setListeSection(List listeSection) {
		this.listeSection = listeSection;
	}

	public List getListeRegime() {
		if (listeRegime.isEmpty()) {
			listeRegime = getService().getObjects("Regime");
		}
		return listeRegime;
	}

	public void setListeRegime(List listeRegime) {
		this.listeRegime = listeRegime;
	}

	public List getListeTypeLogement() {
		if (listeTypeLogement.isEmpty()) {
			listeTypeLogement = getService().getObjects("TypeLogement");
		}
		return listeTypeLogement;
	}

	public void setListeTypeLogement(List listeTypeLogement) {
		this.listeTypeLogement = listeTypeLogement;
	}

	public InputText getInputVhOblig() {
		return inputVhOblig;
	}

	public void setInputVhOblig(InputText inputVhOblig) {
		this.inputVhOblig = inputVhOblig;
	}

	public OutputLabel getOutputVhOblig() {
		return outputVhOblig;
	}

	public void setOutputVhOblig(OutputLabel outputVhOblig) {
		this.outputVhOblig = outputVhOblig;
	}

	public UserAuthorization getUserAuthorization() {
		return userAuthorization;
	}

	public void setUserAuthorization(UserAuthorization userAuthorization) {
		this.userAuthorization = userAuthorization;
	}

	public int getMaxMatricule() {
		if (maxMatricule == 0) {
			try {
				maxMatricule = requeteInscription.recupMaxNumetudiant().get(0).getNumetudiant();
				etudiants.setNumetudiant(maxMatricule++);
			} catch (Exception e) {
				maxMatricule = (int) 1;
			}
		}
		return maxMatricule;
	}

	public void setMaxMatricule(int maxMatricule) {
		this.maxMatricule = maxMatricule;
	}
}