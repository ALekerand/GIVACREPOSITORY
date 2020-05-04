package com.ARSTM.requetes;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ARSTM.model.Nationalites;

@Transactional
@Component
public class ReqNationalite {
	@Autowired
	SessionFactory sessionFactory;
	
	public Nationalites recupNationIvoirirnne(){
	String query = "SELECT * FROM `nationalites` WHERE co`CODENATIONALITE`='1'";
	Nationalites object = (Nationalites) getSessionFactory().getCurrentSession().createSQLQuery(query).addEntity(Nationalites.class).uniqueResult();		
	return object;
	}
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
