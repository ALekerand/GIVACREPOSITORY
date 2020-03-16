package com.ARSTM.managedBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ARSTM.model.Anneeconcours;
import com.ARSTM.model.AnneesScolaire;
import com.ARSTM.model.Diplomes;
import com.ARSTM.model.Ecole;
import com.ARSTM.model.Enseignant;
import com.ARSTM.model.EnseignatStatut;
import com.ARSTM.model.EnseignatStatutId;
import com.ARSTM.model.Etudiants;
import com.ARSTM.model.Filieres;
import com.ARSTM.model.Inscriptions;
import com.ARSTM.model.Matrimoniales;
import com.ARSTM.model.Mention;
import com.ARSTM.model.Nationalites;
import com.ARSTM.model.Niveaux;
import com.ARSTM.model.Pays;
import com.ARSTM.model.Regime;
import com.ARSTM.model.Santes;
import com.ARSTM.model.Section;
import com.ARSTM.model.Sexe;
import com.ARSTM.model.Specialite;
import com.ARSTM.model.Statut;
import com.ARSTM.model.Tformation;
import com.ARSTM.model.TypeLogement;
import com.ARSTM.model.UserAuthentication;
import com.ARSTM.model.UserAuthorization;
import com.ARSTM.requetes.ReqAnneeScolaire;
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
	
	@Autowired
	ReqAnneeScolaire reqAnneeScolaire;
	
	
	private Etudiants etudiants = new Etudiants();
	private Enseignant selectedEnseignant = new Enseignant();
	private Diplomes choosedDiplome = new Diplomes();
	private Sexe chooseedSexe = new Sexe();
	private Pays choosedPays =  new Pays();
	private Ecole choosedEcole = new Ecole();
	private Filieres choosedFiliere = new Filieres();
	private Mention choosedMention = new Mention();
	private Section choosedSection = new Section();
	private Regime choosedRegime = new Regime();
	private Matrimoniales choosedMatrimoniale = new Matrimoniales();
	private Santes choosedSante =new Santes();
	private Niveaux choosedNiveau = new Niveaux();
	private TypeLogement choosedTypeLogement = new TypeLogement();
	private Tformation choosedTformation = new Tformation();
	private Nationalites choosedNationalites = new Nationalites();
	private Inscriptions inscriptions = new Inscriptions();
	
	public Matrimoniales getChoosedMatrimoniale() {
		return choosedMatrimoniale;
	}

	public void setChoosedMatrimoniale(Matrimoniales choosedMatrimoniale) {
		this.choosedMatrimoniale = choosedMatrimoniale;
	}

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
	private List listeNiveau = new ArrayList<>();
	private List listeSante = new ArrayList<>();
	private List listeMatrimoniale = new ArrayList<>();
	private List listeDiplome =  new ArrayList<>();
	private UserAuthentication userAuthentication = new UserAuthentication();
	private UserAuthorization userAuthorization = new UserAuthorization();
	private int maxMatricule = (int) 0;
	
	private AnneesScolaire anneEncoure = new AnneesScolaire();
	
	@PostConstruct
	public AnneesScolaire recupererAnne(){
		anneEncoure = reqAnneeScolaire.recupererDerniereAnneeScolaire().get(0);
		return anneEncoure;
	}
	
	private String destination="C:\\GIVAC\\PHOTO";

	// Contrôle de composant
	private CommandButton btnValider = new CommandButton();
	private InputText inputVhOblig = new InputText();
	private OutputLabel outputVhOblig = new OutputLabel();
	
	
	public void enregistrerTout() {
		enregistrer();
		enregistrerInscription();
		vider(etudiants);
		
		
	}
	
	public void enregistrerInscription() {
		System.out.println("--------section:"+choosedSection.getAbrevSection());//Clen after
		inscriptions.setSection(choosedSection);
		System.out.println("--------Regime:"+choosedRegime.getLibRegime());//Clen after
		inscriptions.setEtudiants(etudiants);
		inscriptions.setAnneesScolaire(anneEncoure);
		inscriptions.setRegime(choosedRegime);
		getService().addObject(inscriptions);
		
		
	}

	public void enregistrer(){
		//enregistrer das la table Etudiants
		
		  etudiants.setNumetudiant(maxMatricule);
		  etudiants.setNomEtudiant(getEtudiants().getNomEtudiant().toUpperCase());
		  etudiants.setMatrimoniales(choosedMatrimoniale);
		  etudiants.setNationalites(choosedNationalites);
		  etudiants.setSexe(chooseedSexe);
		  etudiants.setSantes(choosedSante);
		  etudiants.setNiveaux(choosedNiveau);
		  etudiants.setPays(choosedPays);
		  etudiants.setSantes(choosedSante);
		  etudiants.setDiplomes(choosedDiplome);
		  		  
		  getService().addObject(etudiants);

		//maxMatricule = (int) 0;
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Enregistrement effcetué!", null));
	}
	

	public void annuler() {
		btnValider.setDisabled(false);
		vider(etudiants);
	}

	public void vider(Etudiants objEtudiants) {
		
		  objEtudiants.setDatenais(null);
		  objEtudiants.setLieunais(null);
		  objEtudiants.setPrenomEtudiant(null);
		  objEtudiants.setNomEtudiant(null);
		  objEtudiants.setDiplomes(null);
		  objEtudiants.setMatrimoniales(null);
		  objEtudiants.setNbfreres(null);
		  objEtudiants.setNbsoeurs(null);
		  objEtudiants.setSantes(null);
		  objEtudiants.setSexe(null);
		  objEtudiants.setSections(null);
		  objEtudiants.setTelEtudiant(null);
		  objEtudiants.setNationalites(null);
		  objEtudiants.setNiveaux(null);
		 
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

	public Diplomes getChoosedDiplome() {
		return choosedDiplome;
	}

	public void setChoosedDiplome(Diplomes choosedDiplome) {
		this.choosedDiplome = choosedDiplome;
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

	public Santes getChoosedSante() {
		return choosedSante;
	}

	public void setChoosedSante(Santes choosedSante) {
		this.choosedSante = choosedSante;
	}

	public Niveaux getChoosedNiveau() {
		return choosedNiveau;
	}

	public void setChoosedNiveau(Niveaux choosedNiveau) {
		this.choosedNiveau = choosedNiveau;
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

	public List getListeNiveau() {
		if (listeNiveau.isEmpty()) {
			listeNiveau = getService().getObjects("Niveaux");
		}
		return listeNiveau;
	}

	public void setListeNiveau(List listeNiveau) {
		this.listeNiveau = listeNiveau;
	}

	public List getListeMatrimoniale() {
		if (listeMatrimoniale.isEmpty()) {
			listeMatrimoniale = getService().getObjects("Matrimoniales");
		}
		return listeMatrimoniale;
	}

	public void setListeMatrimoniale(List listeMatrimoniale) {
		this.listeMatrimoniale = listeMatrimoniale;
	}

	public List getListeDiplome() {
		if (listeDiplome.isEmpty()) {
			listeDiplome = getService().getObjects("Diplomes");
		}
		return listeDiplome;
	}

	public void setListeDiplome(List listeDiplome) {
		this.listeDiplome = listeDiplome;
	}

	public List getListeSante() {
		if (listeSante.isEmpty()) {
			listeSante = getService().getObjects("Santes");
		}
		return listeSante;
	}

	public void setListeSante(List listeSante) {
		this.listeSante = listeSante;
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

	public Inscriptions getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(Inscriptions inscriptions) {
		this.inscriptions = inscriptions;
	}
}