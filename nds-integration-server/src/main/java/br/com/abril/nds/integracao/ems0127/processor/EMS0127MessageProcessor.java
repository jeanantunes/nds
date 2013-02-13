package br.com.abril.nds.integracao.ems0127.processor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.MessageProcessor;
import br.com.abril.nds.model.integracao.icd.ChamadaEncalheIcd;
import br.com.abril.nds.model.integracao.icd.ChamadaEncalheItemIcd;
import br.com.abril.nds.repository.AbstractRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

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
		
		if(session == null) {
			session = sessionFactoryIcd.openSession();
		}
		
		return session;
	}
	
	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		List<ChamadaEncalheIcd> encalhes = obterChamadasEncalhe();
		
		tempVar.set(encalhes);
		
	}

	@Override
	public void processMessage(Message message) {
		
		ChamadaEncalheIcd ce = (ChamadaEncalheIcd) message.getBody();
		
		System.out.println("==================== CE ===========================");
		System.out.println(ce.getCePK().getNumeroChamadaEncalhe() +" - "+ ce.getCodigoDistribuidor());
		
		List<ChamadaEncalheItemIcd> itens = obterChamadasEncalheItens(ce);
		
		System.out.println("=================== Itens =========================");
		
		for(ChamadaEncalheItemIcd item : itens) {
			System.out.println(ce.getCePK().getNumeroChamadaEncalhe() +" - "+ item.getItemCePK().getNumeroItem());
		}
		
		System.out.println("===================================================");
		
	}
	
	
	@SuppressWarnings("unchecked")
	private List<ChamadaEncalheIcd> obterChamadasEncalhe() {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select ce ")
			.append("from ChamadaEncalheIcd ce ")
			.append("where ce.tipoStatus = :status");
		
		Query query = this.getSessionIcd().createQuery(hql.toString());
		
		query.setParameter("status", "A");

		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<ChamadaEncalheItemIcd> obterChamadasEncalheItens(ChamadaEncalheIcd ce) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select ceitem ")
			.append("from ChamadaEncalheItemIcd ceitem ")
			.append("where ceitem.itemCePK.numeroChamadaEncalhe = :numeroChamadaEncalhe");
		
		Query query = this.getSessionIcd().createQuery(hql.toString());
		
		query.setParameter("numeroChamadaEncalhe", ce.getCePK().getNumeroChamadaEncalhe());

		return query.list();
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}