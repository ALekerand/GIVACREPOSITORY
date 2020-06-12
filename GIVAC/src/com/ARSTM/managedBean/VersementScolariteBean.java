package com.ARSTM.managedBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ARSTM.model.AnneesScolaire;
import com.ARSTM.model.Diplomes;
import com.ARSTM.model.Ecolages;
import com.ARSTM.model.EtablScolarite;
import com.ARSTM.model.Etudiants;
import com.ARSTM.model.FraisAnnexe;
import com.ARSTM.model.Inscriptions;
import com.ARSTM.model.Matrimoniales;
import com.ARSTM.model.Mention;
import com.ARSTM.model.Nationalites;
import com.ARSTM.model.Niveaux;
import com.ARSTM.model.Regime;
import com.ARSTM.model.Santes;
import com.ARSTM.model.VersementScolarite;
import com.ARSTM.requetes.ReqAnneeScolaire;
import com.ARSTM.requetes.ReqEcolage;
import com.ARSTM.requetes.ReqEtudiant;
import com.ARSTM.requetes.ReqFraisAnnexes;
import com.ARSTM.requetes.RequeteInscription;
import com.ARSTM.service.Iservice;

@Component
@Scope("session")
public class VersementScolariteBean {
	@Autowired
	private Iservice service;
	@Autowired
	ReqAnneeScolaire reqAnneeScolaire;
	@Autowired
	ReqEtudiant reqEtudiant;
	@Autowired
	RequeteInscription requeteInscription;
	@Autowired
	ReqEcolage reqEcolage;
	@Autowired
	ReqFraisAnnexes reqFraisAnnexes;

	private AnneesScolaire anneEncoure = new AnneesScolaire();
	private Etudiants etudiants = new Etudiants();
	private String matriculeRecherche;
	private Inscriptions inscriptions = new Inscriptions();
	private Inscriptions selectedInscription = new Inscriptions();
	private Mention  mention = new Mention();
	private Ecolages ecolage = new Ecolages();
	private Nationalites nationalites = new Nationalites();
	private Regime regime= new Regime();
	private FraisAnnexe fraisAnnexe = new FraisAnnexe();
	private EtablScolarite etablScolarite = new EtablScolarite();
	private VersementScolarite versementScolarite = new VersementScolarite();
	private boolean etatReduction;

	
	// Pour l'upload
	private String destination = "C:/photo/";
	private String cheminFinal ="";
	private	StreamedContent content = new DefaultStreamedContent() ;
	
	private List listEtudiant = new ArrayList<>();
	private List listInscription = new ArrayList<>();
	
	// Contr�le de coposant
	private CommandButton btnValider = new CommandButton();
	private CommandButton btnAnuler = new CommandButton();
	private List listeEtudiant = new ArrayList<>();
	
	
	// M�thodes
	@PostConstruct
	public AnneesScolaire recupererAnne(){
		//Charger l'ann�e scolaire en cours
		anneEncoure = reqAnneeScolaire.recupererDerniereAnneeScolaire().get(0);
		return anneEncoure;
	}
	
	public void rechercher() throws FileNotFoundException {
		//annuler();
		try {
			etudiants = reqEtudiant.recupererEtudiantByMlle(matriculeRecherche).get(0);
		} catch (IndexOutOfBoundsException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Recherche infructueuse. Veuillez v�rifier le matricule", null));
		}
		
		if (etudiants.getMle()!= null) {
			inscriptions = requeteInscription.recupInscriptionByNumEtudiant(etudiants.getNumetudiant(),anneEncoure.getCodeAnnees()).get(0);
			mention = inscriptions.getSection().getMention();
			
			//Ecolage
			chargerPhoto();
		}
	}
	
	
	public void chargerfrais() {
		//Ecolage concern�
		//ecolage = reqEcolage.recupEcolage(mention.getCodeMention(), anneEncoure.getCodeAnnees());
		
		//Frais annexes concern�s
		fraisAnnexe = reqFraisAnnexes.recupFraisAnexByTypeNation(anneEncoure.getCodeAnnees(), 1);
		
		etablScolarite.setAnneesScolaire(anneEncoure);
		etablScolarite.setFraisAssuranceSco(new BigDecimal(fraisAnnexe.getFraisAssurance()));
		etablScolarite.setFraisElearningSco(new BigDecimal(fraisAnnexe.getFraisElearning()));
		etablScolarite.setAutreFraisSco(new BigDecimal(fraisAnnexe.getAutreFrais()));
		etablScolarite.setDateEchance1Sco(ecolage.getDateEchance1());
		etablScolarite.setDateEchance2Eco(ecolage.getDateEchance2());
		etablScolarite.setDateEchance3Sco(ecolage.getDateEchance3());
		etablScolarite.setDateEchance4Eco(ecolage.getDateEchance4());
		etablScolarite.setDateEtablissementSco(new Date());
		//etablScolarite.setDateReduction(dateReduction);
		etablScolarite.setEtudiants(etudiants);


		
	}
	
	
	public void selectionner() throws FileNotFoundException {
		inscriptions = selectedInscription;
		etudiants = selectedInscription.getEtudiants();
		chargerPhoto();
	}
	
	public void enregistrer() throws FileNotFoundException {
		//Enregistrement du complement
		inscriptions.setEtatComplemnt(true);
		etudiants.setPhotoEtudiant(cheminFinal);
		service.updateObject(etudiants);
		service.updateObject(inscriptions);
		
		//Vider la page 
		annuler();
		
		//Actualiser la liste des complements � faire
		getListInscription();
	}
	
	
	public void annuler() throws FileNotFoundException {
		etudiants.setNomEtudiant(null);
		etudiants.setPrenomEtudiant(null);
		//etudiants.setSections(null);
		etudiants.setTelEtudiant(null);
		inscriptions.setSection(null);
		matriculeRecherche = "";
		viderPhoto();
		
	}
	
	
public StreamedContent viderPhoto() throws FileNotFoundException {
    		setCheminFinal(destination + "avatar.jpg");
 			InputStream is = new FileInputStream(cheminFinal);
 			//is.close();  
 			content	= new DefaultStreamedContent(is);
		  return content;	 
	}
	
	
//************************Pour le traitement de la photo
	
	public void upload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Photo valid�e!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        // Do what you want with the file
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
 
    public void copyFile(String fileName, InputStream in) {
        try {
        //lE CHEMIN
        	cheminFinal = destination + fileName;
            OutputStream out = new FileOutputStream(new File(destination + fileName));
 
            int read = 0;
            byte[] bytes = new byte[1024];
 
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
 
            in.close();
            out.flush();
            out.close();
            
 // Charger le fichier dans le graphique image
            getContent();
            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
       
    }
    
    
    public void chargerPhoto() throws FileNotFoundException {
    	cheminFinal = getEtudiants().getPhotoEtudiant();
    	InputStream is = new FileInputStream(cheminFinal);
			//is.close();  
			content	= new DefaultStreamedContent(is);
			
    	
    }
    
    /****** Accesseurs ***********************************/
    
    //Pour charger le graphiqueImage
    public StreamedContent getContent() {
		  return content;	 
	}
    
	public void setContent(StreamedContent content) {
		this.content = content;
	}

	public String getCheminFinal() {
		return cheminFinal;
	}

	public void setCheminFinal(String cheminFinal) {
		this.cheminFinal = cheminFinal;
	}
	
	
	public List getListeEtudiant() {
		return listeEtudiant;
	}

	public void setListeEtudiant(List listeEtudiant) {
		this.listeEtudiant = listeEtudiant;
	}

	public void actualiserList(){
	}

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


	public ReqAnneeScolaire getReqAnneeScolaire() {
		return reqAnneeScolaire;
	}

	public void setReqAnneeScolaire(ReqAnneeScolaire reqAnneeScolaire) {
		this.reqAnneeScolaire = reqAnneeScolaire;
	}

	public Etudiants getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(Etudiants etudiants) {
		this.etudiants = etudiants;
	}

	
	public Nationalites getNationalites() {
		return nationalites;
	}

	public void setNationalites(Nationalites nationalites) {
		this.nationalites = nationalites;
	}

	public Regime getRegime() {
		return regime;
	}

	public void setRegime(Regime regime) {
		this.regime = regime;
	}

	public String getMatriculeRecherche() {
		return matriculeRecherche;
	}



	public void setMatriculeRecherche(String matriculeRecherche) {
		this.matriculeRecherche = matriculeRecherche;
	}



	public Inscriptions getInscriptions() {
		return inscriptions;
	}



	public void setInscriptions(Inscriptions inscriptions) {
		this.inscriptions = inscriptions;
	}

	public List getListEtudiant() {
		return listEtudiant;
	}

	public void setListEtudiant(List listEtudiant) {
		this.listEtudiant = listEtudiant;
	}

	public CommandButton getBtnAnuler() {
		return btnAnuler;
	}


	public void setBtnAnuler(CommandButton btnAnuler) {
		this.btnAnuler = btnAnuler;
	}


	public List getListInscription() {
		listInscription.clear();
		listInscription = requeteInscription.recupListeEtabScolarite(anneEncoure.getCodeAnnees());
		//System.out.println("Taille du fichier:"+listInscription.size());
		return listInscription;
	}


	public void setListInscription(List listInscription) {
		this.listInscription = listInscription;
	}


	public Inscriptions getSelectedInscription() {
		return selectedInscription;
	}


	public void setSelectedInscription(Inscriptions selectedInscription) {
		this.selectedInscription = selectedInscription;
	}

	public Mention getMention() {
		return mention;
	}

	public void setMention(Mention mention) {
		this.mention = mention;
	}

	public EtablScolarite getEtablScolarite() {
		return etablScolarite;
	}

	public void setEtablScolarite(EtablScolarite etablScolarite) {
		this.etablScolarite = etablScolarite;
	}


	public VersementScolarite getVersementScolarite() {
		return versementScolarite;
	}

	public void setVersementScolarite(VersementScolarite versementScolarite) {
		this.versementScolarite = versementScolarite;
	}

	public boolean isEtatReduction() {
		return etatReduction;
	}

	public void setEtatReduction(boolean etatReduction) {
		this.etatReduction = etatReduction;
	}

}
