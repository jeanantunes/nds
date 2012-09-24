package br.com.abril.nds.integracao.ems0127.processor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0120.outbound.EMS0120Header;
import br.com.abril.nds.integracao.ems0127.outbound.EMS0127Detalhe;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.repository.impl.AbstractRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component

public class EMS0127MessageProcessor extends AbstractRepository implements MessageProcessor  {

	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private DistribuidorService distribuidorService;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		// OBTEM DETALHES
		List<EMS0127Detalhe> detalhes = obterEncalhe(null);
		
		// IMPRIMIR HEADER		
		System.out.println(getHeader(detalhes.size()));
		
		// IMPRIMIR DETALHES
		System.out.println(getDetail(detalhes));
		
		// IMPRIMIR O FOOTER
		///xxxxx
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
	
	
	private List<EMS0127Detalhe> obterEncalhe(List<EMS0127Detalhe> Encalhes) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select new br.com.abril.nds.integracao.ems0127.outbound.EMS0127Detalhe( ");
		hql.append(" p.codigo as codProduto, ce.dataRecolhimento as diaRecolhimento) ");
		hql.append("   from Lancamento l ");
		hql.append("   join l.produtoEdicao pe ");
		hql.append("   join pe.produto p ");
		hql.append("   join pe.chamadaEncalhes ce ");
		
		// FIXME: Verificar se isto esta certo!!!
		//hql.append("   where pe.numeroEdicao = :codigoPublicacao ");

		Query query = this.getSession().createQuery(hql.toString());
		/*,
				EMS0127Detalhe.class);*/

		// FIXME: Verificar se isto esta certo!!!
		// query.setParameter("codigoPublicacao", codigoPublicacao);

		return query.list();
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}