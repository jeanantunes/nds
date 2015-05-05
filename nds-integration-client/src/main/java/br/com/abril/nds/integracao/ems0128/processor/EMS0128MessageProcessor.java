package br.com.abril.nds.integracao.ems0128.processor;

import java.util.ArrayList;
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
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.AtualizacaoEstoqueGFS;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0128MessageProcessor extends AbstractRepository implements MessageProcessor  {

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
		
		
		
		CouchDbClient couchDbClient = this.getCouchDBClient(distribuidor, true);
		
		View view = couchDbClient.view("importacao/porTipoDocumento");
		
		view.startKey(new Object[] {RouteInterface.EMS0128.getName(), null});
		view.endKey(RouteInterface.EMS0128.getName(), "");

		view.includeDocs(true);
		try {
			ViewResult<Object[], Void, ?> result = view.queryView(Object[].class, Void.class, EMS0128Input.class);
			for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {						
				
				EMS0128Input doc = (EMS0128Input) row.getDoc();
				
                if (doc != null && doc.getSituacaoSolicitacao() != null && !doc.getSituacaoSolicitacao().equals("SOLICITADO")) {
				
					List<EMS0128InputItem> itemsRemove = new ArrayList<EMS0128InputItem>();
					for (EMS0128InputItem eitem : doc.getItems()) {
						
						MovimentoEstoque movimento = this.recuperaMovimento(eitem.getIdMovimento());
							
						movimento.setMotivo(eitem.getDescricaoMotivo());
						movimento.setNumeroDocumentoAcerto(eitem.getNumeroDocumentoAcerto());
						movimento.setDataEmicaoDocumentoAcerto(eitem.getDataEmissaoDocumentoAcerto());
						movimento.setCodigoOrigemMotivo(eitem.getCodigoOrigemMotivo());
						
						LancamentoDiferenca lancamentoDiferenca = recuperarLancamentoDiferenca(movimento.getId());
						
						StatusIntegracao statusIntegracao = StatusIntegracao.obterPelaDescricao(eitem.getSituacaoAcerto());
						
						if (StatusIntegracao.REJEITADO.equals(statusIntegracao)
								|| StatusIntegracao.DESPREZADO.equals(statusIntegracao)) {
							
							TipoDiferenca tipoDiferenca = lancamentoDiferenca.getDiferenca().getTipoDiferenca();
							
							StatusAprovacao statusAprovacao;
							
							if (tipoDiferenca.isFalta() || tipoDiferenca.isFaltaParaCota()) {
								
								statusAprovacao = StatusAprovacao.PERDA;
								
							} else {
								
								statusAprovacao = StatusAprovacao.GANHO;
							}
							
							movimento.setStatus(statusAprovacao);

							lancamentoDiferenca.setStatus(statusAprovacao);
								
							getSession().merge(lancamentoDiferenca);
							
							this.criarAtualizacaoEstoqueGFS(movimento, lancamentoDiferenca.getDiferenca());
							
							itemsRemove.add(eitem);
							
						} else if (StatusIntegracao.LIBERADO.equals(statusIntegracao)) {
							
							this.criarAtualizacaoEstoqueGFS(movimento, lancamentoDiferenca.getDiferenca());
							
							itemsRemove.add(eitem);
						}
						
						movimento.setStatusIntegracao(statusIntegracao);
						
						getSession().merge(movimento);
						getSession().flush();
					}						
										
					if(!itemsRemove.isEmpty()) {
						
						doc.getItems().removeAll(itemsRemove);
						
						if (doc != null && (doc.getItems() == null || doc.getItems().isEmpty())) {
							couchDbClient.remove(doc);
							doc = null;
						} else {
							couchDbClient.update(doc);
						}

					}
					
					if (doc != null && (doc.getItems() == null || doc.getItems().isEmpty())) {
						couchDbClient.remove(doc);
					}

				}
			}
		} catch (NoDocumentException ex) {
			
		}
	}
	
	private void criarAtualizacaoEstoqueGFS(MovimentoEstoque movimentoEstoque, Diferenca diferenca) {
		
		AtualizacaoEstoqueGFS atualizacaoEstoqueGFS = recuperarAtualizacaoEstoqueGFS(movimentoEstoque, diferenca);
		
		if(atualizacaoEstoqueGFS == null){
			atualizacaoEstoqueGFS = new AtualizacaoEstoqueGFS();
			atualizacaoEstoqueGFS.setMovimentoEstoque(movimentoEstoque);
			atualizacaoEstoqueGFS.setDiferenca(diferenca);
		}
		
		atualizacaoEstoqueGFS.setDataAtualizacao(new Date());
		
		getSession().merge(atualizacaoEstoqueGFS);
		getSession().flush();
	}

	private MovimentoEstoque recuperaMovimento(Long id) {
		
		return (MovimentoEstoque) getSession().get(MovimentoEstoque.class, id);		
		
	}
	
	private AtualizacaoEstoqueGFS recuperarAtualizacaoEstoqueGFS(MovimentoEstoque movimentoEstoque, Diferenca diferenca){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT atualizacaoEstoqueGFS ");
		sql.append(" FROM AtualizacaoEstoqueGFS atualizacaoEstoqueGFS  ");
		sql.append(" JOIN atualizacaoEstoqueGFS.movimentoEstoque me ");
		sql.append(" JOIN atualizacaoEstoqueGFS.diferenca de ");
		sql.append(" where me.id =:idMovimentoEstoque ");
		sql.append(" and de.id =:idDiferenca ");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("idMovimentoEstoque", movimentoEstoque.getId());
		query.setParameter("idDiferenca", diferenca.getId());
		
		return (AtualizacaoEstoqueGFS) query.uniqueResult();
	}
	
	private LancamentoDiferenca recuperarLancamentoDiferenca(Long idMovimentoEstoque){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT lancamentoDiferenca ");
		sql.append(" FROM LancamentoDiferenca lancamentoDiferenca  ");
		sql.append(" JOIN lancamentoDiferenca.movimentoEstoque me ");
		sql.append(" where me.id =:idMovimentoEstoque ");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("idMovimentoEstoque", idMovimentoEstoque);
		
		return (LancamentoDiferenca) query.uniqueResult();
	}

	
	@Override
	public void processMessage(Message message) {
		
		
		EMS0128InputItem item = new EMS0128InputItem();
		
		MovimentoEstoque me = (MovimentoEstoque) message.getBody();

		item.setNumSequenciaDetalhe(itens++);
		item.setIdMovimento(me.getId());
		
		GrupoMovimentoEstoque gme = ((TipoMovimentoEstoque)(me).getTipoMovimento()).getGrupoMovimentoEstoque();
		
		if ( gme.equals( GrupoMovimentoEstoque.FALTA_EM ) 
				|| gme.equals( GrupoMovimentoEstoque.FALTA_EM_DIRECIONADA_PARA_COTA ) ) {
			item.setTipoAcerto( 3 );
		} else if ( gme.equals( GrupoMovimentoEstoque.FALTA_DE ) ) {			
			item.setTipoAcerto( 4 );
		} else if ( gme.equals( GrupoMovimentoEstoque.SOBRA_EM ) 
				|| gme.equals( GrupoMovimentoEstoque.SOBRA_EM_DIRECIONADA_PARA_COTA ) 
				|| gme.equals( GrupoMovimentoEstoque.SOBRA_EM_COTA )) {
			item.setTipoAcerto( 5 );
		} else if ( gme.equals( GrupoMovimentoEstoque.SOBRA_DE ) 
				|| gme.equals( GrupoMovimentoEstoque.SOBRA_DE_DIRECIONADA_PARA_COTA ) ) {
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
	@SuppressWarnings("unchecked")
	public void posProcess(Object tempVar) {

		
		if (!input.getItem().isEmpty()) {
			CouchDbClient cdbc = this.getCouchDBClient(input.getCodigoDistribuidor(), true);
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
		sql.append("FROM LancamentoDiferenca lancamentoDiferenca  ");
		sql.append("JOIN lancamentoDiferenca.movimentoEstoque me ");
		sql.append("JOIN FETCH me.tipoMovimento tm ");
		sql.append("JOIN FETCH me.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto pr ");		
		sql.append("WHERE tm.grupoMovimentoEstoque in (:grupoMovimentoEstoque) ");
		sql.append("	and me.statusIntegracao = :statusIntegracao ");
		sql.append("	and me.status = :status ");
		sql.append("	and lancamentoDiferenca.status in (:statusLancamentoDiferenca) ");
		sql.append("	and pr.origem in (:origemProduto) ");
		
		Query query = getSession().createQuery(sql.toString());

		query.setParameterList("grupoMovimentoEstoque", (new GrupoMovimentoEstoque[]{ 
				GrupoMovimentoEstoque.SOBRA_EM
				, GrupoMovimentoEstoque.SOBRA_DE
				, GrupoMovimentoEstoque.FALTA_EM_DIRECIONADA_PARA_COTA
				, GrupoMovimentoEstoque.FALTA_EM
				, GrupoMovimentoEstoque.FALTA_DE
				, GrupoMovimentoEstoque.SOBRA_DE_DIRECIONADA_PARA_COTA
				, GrupoMovimentoEstoque.SOBRA_EM_DIRECIONADA_PARA_COTA
		}) );
		
		query.setParameterList("origemProduto", (new Origem[]{ Origem.INTERFACE, Origem.PRODUTO_SEM_CADASTRO }) );
		
		query.setParameter("statusIntegracao", StatusIntegracao.NAO_INTEGRADO);
		query.setParameter("status", StatusAprovacao.APROVADO);
		query.setParameterList("statusLancamentoDiferenca", new StatusAprovacao[] {StatusAprovacao.APROVADO});
		
		return query;
	}
	
	
}
