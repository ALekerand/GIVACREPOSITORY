package com.ARSTM.managedBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ARSTM.model.AnneesScolaire;
import com.ARSTM.model.Diplomes;
import com.ARSTM.model.Etudiants;
import com.ARSTM.model.Inscriptions;
import com.ARSTM.model.Matrimoniales;
import com.ARSTM.model.Niveaux;
import com.ARSTM.model.Santes;
import com.ARSTM.requetes.ReqAnneeScolaire;
import com.ARSTM.requetes.ReqEtudiant;
import com.ARSTM.requetes.RequeteInscription;
import com.ARSTM.service.Iservice;

@Component
@Scope("session")
public class ComplementBean {
	@Autowired
	private Iservice service;
	@Autowired
	ReqAnneeScolaire reqAnneeScolaire;
	@Autowired
	ReqEtudiant reqEtudiant;
	@Autowired
	RequeteInscription requeteInscription;

	private AnneesScolaire anneeScolaire = new AnneesScolaire();
	private Etudiants etudiants = new Etudiants();
	private Matrimoniales choosedMatrimoniales;
	private Santes chooseedsantes = new Santes();
	private Niveaux choosedNiveaux = new Niveaux();
	private Diplomes choosedDiplomes = new Diplomes();
	private String matriculeRecherche;
	private Inscriptions inscriptions = new Inscriptions();
	private Inscriptions selectedInscription = new Inscriptions();

	
	
	// Pour l'upload
	private String destination = "C:/photo/";
	private String cheminFinal ="";
	private	StreamedContent content = new DefaultStreamedContent() ;
	
	
	private List listMatrimonale = new ArrayList<>();
	private List listeSante = new ArrayList<>();
	private List listeNiveaux = new ArrayList<>();
	private List listDiplome = new ArrayList<>();
	private List listEtudiant = new ArrayList<>();
	private List listInscription = new ArrayList<>();
	
	// Contr�le de coposant
	private CommandButton btnValider = new CommandButton();
	private CommandButton btnAnuler = new CommandButton();
	private List listeEtudiant = new ArrayList<>();
	
	
	
	
	// M�thodes
	public void rechercher() {
		try {
			etudiants = reqEtudiant.recupererEtudiantByMlle(matriculeRecherche).get(0);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("======== Message � l'utilisateur");// Clean after
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Recherche infructueuse. Veuillez v�rifier le matricule", null));
		}
		
		if (etudiants!= null) {
			inscriptions = requeteInscription.recupInscriptionByNumEtudiant(etudiants.getNumetudiant()).get(0);
		}
	}
	
	
	public void selectionner() {
		etudiants = selectedInscription.getEtudiants();
	}
	
	public void enregistrer() {
		inscriptions.setEtatComplemnt(true);
		service.updateObject(etudiants);
		annuler();
	}
	
	
	public void annuler() {
		etudiants.setNomEtudiant(null);
		etudiants.setPrenomEtudiant(null);
		etudiants.setDatenais(null);
		etudiants.setLieunais(null);
		etudiants.setSections(null);
		etudiants.setMailEtudiant(null);
		etudiants.setTelEtudiant(null);
		etudiants.setNumcni(null);
		etudiants.setEcoleAncienneEtudiant(null);
		etudiants.setNomPrenomsPere(null);
		etudiants.setFonctionPere(null);
		etudiants.setNomPrenomsMere(null);
		etudiants.setFonctionMere(null);
		etudiants.setNomPrenomsTuteur(null);
		etudiants.setTelTuteur(null);
		etudiants.setNomPrenomsDocteur(null);
		etudiants.setTelDocteur(null);
		
	}
	
	
//************************Pour le traitement de la photo
	
	public void upload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
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
        	cheminFinal = destination + fileName;
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
 // Charger le fichier dans le graphique image
            
            getContent();
            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
       
    }
    
    /****** Accesseurs ***********************************/
    
    //Pour charger le graphiqueImage
    public StreamedContent getContent() {
		
    	
    	if ((cheminFinal.equals(""))) {
    		setCheminFinal(destination + "avatar.jpg");
    		System.out.println("====Valeur chemin final"+getCheminFinal());
    	}
    	
    	try {
			 
 			InputStream is = new FileInputStream(cheminFinal);
 			//is.close();  
 			content	= new DefaultStreamedContent(is);
 			
 		} catch (FileNotFoundException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
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

	

	public AnneesScolaire getAnneeScolaire() {
		return anneeScolaire;
	}

	public void setAnneeScolaire(AnneesScolaire anneeScolaire) {
		this.anneeScolaire = anneeScolaire;
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

	public List getListMatrimonale() {
		listMatrimonale = service.getObjects("Matrimoniales");
		return listMatrimonale;
	}

	public void setListMatrimonale(List listMatrimonale) {
		this.listMatrimonale = listMatrimonale;
	}

	public Matrimoniales getChoosedMatrimoniales() {
		return choosedMatrimoniales;
	}

	public void setChoosedMatrimoniales(Matrimoniales choosedMatrimoniales) {
		this.choosedMatrimoniales = choosedMatrimoniales;
	}

	public Santes getChooseedsantes() {
		return chooseedsantes;
	}

	public void setChooseedsantes(Santes chooseedsantes) {
		this.chooseedsantes = chooseedsantes;
	}

	public List getListeSante() {
		listeSante = service.getObjects("Santes");
		return listeSante;
	}

	public void setListeSante(List listeSante) {
		this.listeSante = listeSante;
	}

	public Niveaux getChoosedNiveaux() {
		return choosedNiveaux;
	}

	public void setChoosedNiveaux(Niveaux choosedNiveaux) {
		this.choosedNiveaux = choosedNiveaux;
	}

	public List getListeNiveaux() {
		listeNiveaux = service.getObjects("Niveaux");
		return listeNiveaux;
	}

	public void setListeNiveaux(List listeNiveaux) {
		this.listeNiveaux = listeNiveaux;
	}

	public Diplomes getChoosedDiplomes() {
		return choosedDiplomes;
	}

	public void setChoosedDiplomes(Diplomes choosedDiplomes) {
		this.choosedDiplomes = choosedDiplomes;
	}

	public List getListDiplome() {
		listDiplome = service.getObjects("Diplomes");
		return listDiplome;
	}

	public void setListDiplome(List listDiplome) {
		this.listDiplome = listDiplome;
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
		System.out.println("========Methode lanc�e");
		listInscription = requeteInscription.recupListeComplement();
		System.out.println("======="+listInscription.size());
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

	
}
