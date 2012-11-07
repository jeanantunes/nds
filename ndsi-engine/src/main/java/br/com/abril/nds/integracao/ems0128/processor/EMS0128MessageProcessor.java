package br.com.abril.nds.integracao.ems0128.processor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component

public class EMS0128MessageProcessor extends AbstractRepository implements MessageProcessor  {



	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private int itens=1;
	private EMS0128Input input;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		
		input = new EMS0128Input();
		
		input.setCodigoDistribuidor(distribuidorService.obter().getCodigoDistribuidorDinap());
		input.setDataSolicitacao(new Date());
		input.setFormaSolicitacao("CARGA-NDISTRIB");
		input.setSituacaoSolicitacao("SOLICITADO");

		atualizaStatus(input.getCodigoDistribuidor());
		
		Query query = queryMovimentoEstoque();
		
		tempVar.set( query.list() );		
		
			
	}

	
	private void atualizaStatus(String distribuidor) {
		
		CouchDbClient couchDbClient = this.getCouchDBClient(distribuidor);
		
		View view = couchDbClient.view("importacao/porTipoDocumento");
						
		view.key("EMS0128");
		view.includeDocs(true);
		try {
			ViewResult<String, Void, ?> result = view.queryView(String.class, Void.class, EMS0128Input.class);
			for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {						
				
				EMS0128Input doc = (EMS0128Input) row.getDoc();
				
				if (!doc.getSituacaoSolicitacao().equals("SOLICITADO")) {
				
					for ( EMS0128InputItem eitem : doc.getItems()) {
						
						MovimentoEstoque movimento = this.recuperaMovimento(eitem.getIdMovimento());
											
						movimento.setStatusIntegracao(StatusIntegracao.valueOf(eitem.getSituacaoAcerto()));
						movimento.setMotivo(eitem.getDescricaoMotivo());					
						movimento.setNumeroDocumentoAcerto(eitem.getNumeroDocumentoAcerto());
						movimento.setDataEmicaoDocumentoAcerto(eitem.getDataEmicaoDocumentoAcerto());
						movimento.setCodigoOrigemMotivo(eitem.getCodigoOrigemMotivo());
					
						getSession().merge(movimento);
						getSession().flush();
						
					}						
					
					if (doc.getSituacaoSolicitacao().equals("PROCESSADO")) {
						couchDbClient.remove(doc);
					}
				}
			}
		} catch (NoDocumentException ex) {
			
		}


	}

	private MovimentoEstoque recuperaMovimento(Long id) {
		
		return (MovimentoEstoque) getSession().get(MovimentoEstoque.class, id);		
		
	}


	@Override
	public void processMessage(Message message) {
		
		EMS0128InputItem item = new EMS0128InputItem();
		
		MovimentoEstoque me = (MovimentoEstoque)message.getBody();
		
		
		item.setNumSequenciaDetalhe(itens++);
		item.setIdMovimento(me.getId());
		
		GrupoMovimentoEstoque gme = ((TipoMovimentoEstoque)(me).getTipoMovimento()).getGrupoMovimentoEstoque();
		
		if ( gme.equals( GrupoMovimentoEstoque.FALTA_EM ) ) {
			item.setTipoAcerto( 3 );
		} else if ( gme.equals( GrupoMovimentoEstoque.FALTA_DE ) ) {			
			item.setTipoAcerto( 4 );
		} else if ( gme.equals( GrupoMovimentoEstoque.SOBRA_EM ) ) {
			item.setTipoAcerto( 5 );
		} else if ( gme.equals( GrupoMovimentoEstoque.SOBRA_DE ) ) {
			item.setTipoAcerto( 6 );
		}		
		
		item.setCodigoProduto(me.getProdutoEdicao().getProduto().getCodigo());
		item.setNumeroEdicao(me.getProdutoEdicao().getNumeroEdicao());
		item.setQtd(me.getQtde());
		item.setPrecoCapa(me.getProdutoEdicao().getPrecoVenda());
		if (null != me.getProdutoEdicao().getProduto().getDescontoLogistica()) {
			item.setPercentualDesconto(me.getProdutoEdicao().getProduto().getDescontoLogistica().getPercentualDesconto());
		}
		item.setSituacaoAcerto("SOLICITADO");
		
		input.getItems().add(item);
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
		if (!input.getItem().isEmpty()) {
			CouchDbClient cdbc = this.getCouchDBClient(input.getCodigoDistribuidor());
			input.setTipoDocumento("EMS0128");
			
			cdbc.save(input);
			cdbc.shutdown();
			
			Query query = queryMovimentoEstoque();
			
			List<MovimentoEstoque> listMovimentoEstoque = query.list();
			for (MovimentoEstoque me : listMovimentoEstoque ) {
				me.setStatusIntegracao(StatusIntegracao.SOLICITADO);
				me.setDataIntegracao(new Date());
				getSession().merge(me);
			}
		}		
	}

	private Query queryMovimentoEstoque() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT me ");
		sql.append("FROM MovimentoEstoque me ");
		sql.append("JOIN FETCH me.tipoMovimento tm ");
		sql.append("JOIN FETCH me.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto pr ");
		sql.append("WHERE tm.grupoMovimentoEstoque in (:grupoMovimentoEstoque) ");
		sql.append("	and me.statusIntegracao = :statusIntegracao ");
		sql.append("	and me.status = :status ");
		
		Query query = getSession().createQuery(sql.toString());

		query.setParameterList("grupoMovimentoEstoque", (new GrupoMovimentoEstoque[]{ 
				GrupoMovimentoEstoque.SOBRA_EM
				, GrupoMovimentoEstoque.SOBRA_DE
				, GrupoMovimentoEstoque.FALTA_EM
				, GrupoMovimentoEstoque.FALTA_DE
		}) );
		
		query.setParameter("statusIntegracao", StatusIntegracao.NAO_INTEGRADO);
		query.setParameter("status", StatusAprovacao.APROVADO);
		return query;
	}	

}
