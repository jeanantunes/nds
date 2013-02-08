package br.com.abril.nds.integracao.ems0127.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0120.outbound.EMS0120Header;
import br.com.abril.nds.integracao.ems0127.outbound.EMS0127Detalhe;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.model.integracao.icd.ChamadaEncalheIcd;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component
public class EMS0127MessageProcessor extends AbstractRepository implements MessageProcessor  {

	@Autowired
	private SessionFactory sessionFactoryIcd;
	
	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private DistribuidorService distribuidorService;

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
		
		List<Object> list = new ArrayList<Object>();
		list.add(new Object());
		
		tempVar.set(list);
		
	}

	@Override
	public void processMessage(Message message) {
		
		List<ChamadaEncalheIcd> chamadasEncalheIcd = obterEncalhe(null);

		for(ChamadaEncalheIcd ceicd : chamadasEncalheIcd) {
			System.out.println(ceicd.getDataEmissao());
		}
		
	}
	
	private String getDetail(List<EMS0127Detalhe> detalhes) {
		StringBuilder stringBuilder = new StringBuilder();
				
		for (EMS0127Detalhe ems0127Detalhe : detalhes) {
			stringBuilder.append(fixedFormatManager.export(ems0127Detalhe));
			stringBuilder.append("\n");
		}
		
		return stringBuilder.toString();
	}
	
	private String getHeader(int detailCount) {
		EMS0120Header outheader = new EMS0120Header();
		
		Date data = new Date();
		outheader.setDataMovimento(data);
		outheader.setHoraMovimento(data);
		outheader.setQtdeRegistrosDetalhe(detailCount);
		
		return fixedFormatManager.export(outheader);
	}
	
	
	private List<ChamadaEncalheIcd> obterEncalhe(List<EMS0127Detalhe> Encalhes) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select ce");
		hql.append(" from ChamadaEncalheIcd ce ");
		
		Query query = this.getSessionIcd().createQuery(hql.toString());

		return query.list();
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}