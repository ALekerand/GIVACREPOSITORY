package com.ARSTM.requetes;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ARSTM.model.Enseignant;
import com.ARSTM.model.Etudiants;
import com.ARSTM.model.Inscriptions;

@Transactional
@Component
public class RequeteInscription {
	@Autowired
	SessionFactory sessionFactory;
	
	
	public List<Etudiants> recupMaxNumetudiant(){
		String query = "SELECT `etudiants`.*, `etudiants`.`NUMETUDIANT` FROM `etudiants` ORDER BY `etudiants`.`NUMETUDIANT` DESC";
	List list = getSessionFactory().getCurrentSession().createSQLQuery(query).addEntity(Etudiants.class).list();		
	return list;
	}
	
	
	public List<Inscriptions> recupInscriptionByNumEtudiant(int numeroEtudiant){
		String query = "SELECT * FROM `inscriptions` WHERE inscriptions.NUMETUDIANT = '"+numeroEtudiant+"'";
	List list = getSessionFactory().getCurrentSession().createSQLQuery(query).addEntity(Inscriptions.class).list();		
	return list;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public List<Inscriptions> recupListeComplement(int codeAnneeScolaire){
	String query = "SELECT `inscriptions`.* FROM `inscriptions` WHERE ((`inscriptions`.`ETAT_COMPLEMNT` = '0') AND (`inscriptions`.`CODE_ANNEES` = '"+codeAnneeScolaire+"'))";
	List list = getSessionFactory().getCurrentSession().createSQLQuery(query).addEntity(Inscriptions.class).list();		
	return list;
	}
	
	public List<Inscriptions> recupListeEtabScolarite(int codeAnneeScolaire){
		String query = "SELECT `inscriptions`.* FROM `inscriptions` WHERE ((`inscriptions`.`ETAT_ETAB_SCOLARITE` = '0') AND (`inscriptions`.`CODE_ANNEES` = '"+codeAnneeScolaire+"'))";
		List list = getSessionFactory().getCurrentSession().createSQLQuery(query).addEntity(Inscriptions.class).list();		
		return list;
		}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


}
