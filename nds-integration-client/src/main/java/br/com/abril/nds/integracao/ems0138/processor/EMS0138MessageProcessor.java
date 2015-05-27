package br.com.abril.nds.integracao.ems0138.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dto.chamadaencalhe.integracao.ChamadaEncalheFornecedorIntegracaoDTO;
import br.com.abril.nds.dto.chamadaencalhe.integracao.ChamadaEncalheFornecedorIntegracaoItemDTO;
import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.integracao.EMS0138NotasCEIntegracao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0138MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0138MessageProcessor.class);

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory; 
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

		List<Object> objs = new ArrayList<Object>();
		Object obj = new Object();
		objs.add(obj);
		
		tempVar.set(objs);
		
	}

	@Override
	public void processMessage(Message message) {
		
		message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), "MySQL : NDS : nds-client : root");
		
		EMS0138NotasCEIntegracao notasCEIntegracao = new EMS0138NotasCEIntegracao(); 
		List<ChamadaEncalheFornecedorIntegracaoDTO> chamadasEncalhe = obterChamadasEncalhe(message);
		
		notasCEIntegracao.setTipoDocumento("EMS0139");
		notasCEIntegracao.setChamadasEncalhe(chamadasEncalhe);
		
		CouchDbClient cdbc = null;
		
		String codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorDinap();
		
		try {
			
			cdbc = this.getCouchDBClient(codigoDistribuidor, true);
			cdbc.save(notasCEIntegracao);
			
		} catch(Exception e) {
			LOGGER.error("Erro executando importação de Chamada Encalhe Prodin.", e);
		} finally {
			if (cdbc != null) {
				cdbc.shutdown();
			}			
		}
				
	}

	@SuppressWarnings("unchecked")
	private List<NotaFiscal> obterNotasFiscais() {
		StringBuilder hql = new StringBuilder();
		hql.append(" select nf ")
			.append("from NotaFiscal nf ")
			.append("where nf.identificacao.tipoNotaFiscal.tipoOperacao = :tipoOperacao ")
			.append("and nf.identificacao.tipoNotaFiscal.grupoNotaFiscal in (:grupoNotaFiscal) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoOperacao", TipoOperacao.SAIDA);
		List<GrupoNotaFiscal> gruposNotaFiscal = new ArrayList<>();
		gruposNotaFiscal.add(GrupoNotaFiscal.NF_DEVOLUCAO_MERCADORIA_RECEBIA_CONSIGNACAO);
		gruposNotaFiscal.add(GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_DISTRIBUICAO);
		query.setParameterList("grupoNotaFiscal", gruposNotaFiscal);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	private List<ChamadaEncalheFornecedorIntegracaoDTO> obterChamadasEncalhe(Message message) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select ce ")
			.append("from ChamadaEncalheFornecedor ce ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		//query.setResultTransformer(new AliasToBeanResultTransformer(ChamadaEncalheFornecedorIntegracaoDTO.class));
		
		List<ChamadaEncalheFornecedor> chamadasEncalheFornecedor = query.list();
		
		List<ChamadaEncalheFornecedorIntegracaoDTO> chamadasEncalheFornecedorDTO = new ArrayList<ChamadaEncalheFornecedorIntegracaoDTO>();
		
		for(ChamadaEncalheFornecedor cef : chamadasEncalheFornecedor) {
			ChamadaEncalheFornecedorIntegracaoDTO cefDTO = new ChamadaEncalheFornecedorIntegracaoDTO();
			cefDTO.setId(cef.getId());
			cefDTO.setNumeroChamadaEncalhe(cef.getNumeroChamadaEncalhe());
			
			for(ItemChamadaEncalheFornecedor icef : cef.getItens()) {
				ChamadaEncalheFornecedorIntegracaoItemDTO cei =  new ChamadaEncalheFornecedorIntegracaoItemDTO();
				
				NotaFiscalSaidaFornecedor nfsf = obterNotaFiscal(icef.getNumeroNotaEnvio());
				
				if(nfsf == null) {
					ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Nota Fiscal Inexistente para a chamada de encalhe: "+ cefDTO.getNumeroChamadaEncalhe());
					continue;
				}
				
				cei.setNumeroChamadaEncalhe(icef.getChamadaEncalheFornecedor().getNumeroChamadaEncalhe());
				cei.setNumeroItem(icef.getNumeroItem());
				cei.setDataEmissaoNotaEnvio(nfsf.getDataEmissao());
				cei.setNumeroAcessoNotaEnvio(nfsf.getChaveAcesso());
				//cei.setSerieNotaEnvio(nfsf.getTipoNotaFiscal().getSerieNotaFiscal()); FIXME: 
				//cei.setTipoModeloNotaEnvio(nfsf.getTipoNotaFiscal().getNopCodigo()); FIXME: Verificar o valor do tipomodelo
				cei.setQuantidadeDevolucaoInformada(icef.getQtdeDevolucaoInformada());
				cei.setQuantidadeEnviada(icef.getQtdeEnviada());
				cei.setQuantidadeVendaInformada(icef.getQtdeVendaInformada());
				
			}
			
			chamadasEncalheFornecedorDTO.add(cefDTO);
			
		}
		
		return chamadasEncalheFornecedorDTO;
		
	}

	private NotaFiscalSaidaFornecedor obterNotaFiscal(Long numeroNotaEnvio) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select nf ")
			.append("from NotaFiscalSaidaFornecedor nf ")
			.append(" where nf.numero = :numeroNota");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroNota", numeroNotaEnvio);
		
		return (NotaFiscalSaidaFornecedor) query.uniqueResult();
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

}
