package br.com.abril.nds.integracao.ems0127.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0127.outbound.EMS0127Detalhe;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.model.integracao.icd.ChamadaEncalheIcd;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS0127MessageProcessor extends AbstractRepository implements MessageProcessor  {

	@Autowired
	private SessionFactory sessionFactoryIcd;
	
	protected Session getSessionIcd() {
		
		Session session = null;
		try {
			session = sessionFactoryIcd.getCurrentSession();
		} catch(Exception e) {
			
		}
		
		if(session == null)
			session = sessionFactoryIcd.openSession();
		
		return session;
	}
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		
		List<ChamadaEncalheIcd> chamadasEncalheIcd = obterChamadasEncalhe();
		
		tempVar.set(chamadasEncalheIcd);
		
	}

	@Override
	public void processMessage(Message message) {
		
		ChamadaEncalheIcd ce = (ChamadaEncalheIcd) message.getBody();

		System.out.println(ce.getDataEmissao());
		
		/*for(ChamadaEncalheIcd ceicd : chamadasEncalheIcd) {
			System.out.println(ceicd.getDataEmissao());
		}*/
		
	}
	
	@SuppressWarnings("unchecked")
	private List<ChamadaEncalheIcd> obterChamadasEncalhe() {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select ce")
			.append(" from ChamadaEncalheIcd ce ")
			.append("where ce.tipoStatus = :status ");
		
		Query query = this.getSessionIcd().createQuery(hql.toString());
		
		query.setParameter("status", "A");

		return query.list();
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}