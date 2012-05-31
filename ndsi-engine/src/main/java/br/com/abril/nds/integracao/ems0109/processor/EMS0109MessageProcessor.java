package br.com.abril.nds.integracao.ems0109.processor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0109Input;
import br.com.abril.nds.integracao.service.PeriodicidadeProdutoService;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;

@Component
public class EMS0109MessageProcessor implements MessageProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private PeriodicidadeProdutoService periodicidadeProdutoService;

	@Override
	public void processMessage(Message message) {
	
		// Distribuidor unico para todo sistema		
		if (verificarDistribuidor(message)) {
			
			Editor editor = this.findEditorByID(message);
			
			TipoProduto tipoProduto = this.findTipoProduto(GrupoProduto.REVISTA, message);
			
			ProdutoEdicao produtoEdicao = this.findProdutoEdicao(message);
							
			if (produtoEdicao == null) {

				this.criarProdutoEdicaoConformeInput(message, editor, tipoProduto);
								
			} else {
				
				this.atualizaProdutoEdicaoConformeInput(produtoEdicao, editor, tipoProduto, message);				
			}
			
		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, 
					"Distribuidor nao encontrato.");
			
			throw new RuntimeException("Distribuidor incorreto.");
		}
	}

	private boolean verificarDistribuidor(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		Integer codigoDistribuidorSistema = (Integer) message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue());
		Integer codigoDistribuidorArquivo = Integer.parseInt(input.getCodigoDistribuidor()); 
			
		if (codigoDistribuidorSistema != null && codigoDistribuidorSistema.equals(codigoDistribuidorArquivo)) {
			
			return true;
		}
		
		return false;
	}
	
	private ProdutoEdicao findProdutoEdicao(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select pe from ProdutoEdicao pe ");
		sql.append(" join fetch pe.produto p ");
		sql.append(" where pe.numeroEdicao = :codigoPublicacao ");
		
		try {
		
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("codigoPublicacao", input.getCodigoPublicacao());
			
			return (ProdutoEdicao) query.getSingleResult();
			
		} catch (NoResultException e) {
			
			return null;
		} 
	}
	
	private PeriodicidadeProduto findPeriodicidadeProduto(Integer periodicidade) {
		
		return this.periodicidadeProdutoService.getPeriodicidadeProdutoAsArchive(periodicidade);
	}
	
	private Editor findEditorByID(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select editor from Editor editor ");
		sql.append( " where editor.codigo = :codigoEditor ");
		
		try {
			
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("codigoEditor", input.getCodigoEditor());
			
			return (Editor) query.getSingleResult();
			
		} catch (NoResultException e) {	
			
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.SEM_DOMINIO, 
					"Editor " + input.getCodigoEditor() +  " nao encontrado.");
			
			throw new RuntimeException("Editor nao encontrado.");
		} 
	}
	
	private DescontoLogistica findDescontoLogisticaByCodigoTipoDesconto(String codigoTipoDesconto) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select d from DescontoLogistica d ");
		sql.append( " where d.tipoDesconto = :codigoTipoDesconto ");
		
		try {
			
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("codigoTipoDesconto", codigoTipoDesconto);
			
			return (DescontoLogistica) query.getSingleResult();
			
		} catch (NoResultException e) {	
			
			return null;
		} 
	}
	
	private TipoProduto findTipoProduto(GrupoProduto grupoProduto, Message message) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT tp FROM TipoProduto tp ");
		sql.append("WHERE  tp.grupoProduto = :grupoProduto ");
			
		Query query = this.entityManager.createQuery(sql.toString());;
		query.setParameter("grupoProduto", grupoProduto);
		
		@SuppressWarnings("unchecked")
		List<TipoProduto> tiposProduto = (List<TipoProduto>) query.getResultList();
		
		TipoProduto tipoProduto = null;
		
		if (!tiposProduto.isEmpty()) {
			
			tipoProduto = tiposProduto.get(0);

			return tipoProduto;
			
		} else {
			
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.SEM_DOMINIO, 
					"Tipo Produto REVISTA nao encontrado.");
			
			throw new RuntimeException("Tipo Produto nao encontrado.");
		}
	}	
	
	private Fornecedor findFornecedor(Integer codigoInterface) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT f FROM Fornecedor f ");
		sql.append("WHERE  f.codigoInterface = :codigoInterface ");
		
		try {
			
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("codigoInterface", codigoInterface);
			
			return (Fornecedor) query.getSingleResult();
			
		} catch (NoResultException e) {	
			
			return null;
		} 
	}
	
	private void criarProdutoEdicaoConformeInput(Message message, Editor editor, TipoProduto tipoProduto) {
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();	
		Produto produto = new Produto();
				
		PeriodicidadeProduto periodicidadeProduto = this.findPeriodicidadeProduto(input.getPeriodicidade());
		Fornecedor fornecedor = this.findFornecedor(input.getCodigoFornecedor());
		DescontoLogistica descontoLogistica = this.findDescontoLogisticaByCodigoTipoDesconto(input.getTipoDesconto());

		produto.setTipoProduto(tipoProduto);
		produto.setNome(input.getNomePublicacao());
		produto.setCodigoContexto(input.getContextoPublicacao());
		produto.setDescricao(input.getNomePublicacao());	
		produto.setEditor(editor);
		produto.setPeriodicidade(periodicidadeProduto);
		produto.setSlogan(input.getSlogan());
		produto.setPeb(input.getPeb());
		produto.setPeso(input.getPeso());
		produto.setPacotePadrao(input.getPacotePadrao());
		
		if ( fornecedor != null ) {
			
			produto.addFornecedor(fornecedor);
		}
		
		this.entityManager.persist(produto); 
		
		produtoEdicao.setProduto(produto);
		produtoEdicao.setNumeroEdicao(input.getCodigoPublicacao());
		produtoEdicao.setAtivo(input.isStatus());
		produtoEdicao.setDataDesativacao(input.getDataDesativacao());
//		produtoEdicao.setPeb(input.getPeb());
//		produtoEdicao.setPacotePadrao(input.getPacotePadrao());
//		produtoEdicao.setPeso(input.getPeso());

		if ( descontoLogistica != null ) {
			
//			produtoEdicao.setDescontoLogistica(descontoLogistica);
			
		}
		
		this.entityManager.persist(produtoEdicao);
		

		
	}
	
	private void atualizaProdutoEdicaoConformeInput(ProdutoEdicao produtoEdicao, Editor editor, TipoProduto tipoProduto, Message message) {
		
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		PeriodicidadeProduto periodicidadeProduto = this.findPeriodicidadeProduto(input.getPeriodicidade());
		Fornecedor fornecedor = this.findFornecedor(input.getCodigoFornecedor());
		DescontoLogistica descontoLogistica = this.findDescontoLogisticaByCodigoTipoDesconto(input.getTipoDesconto());
		
		if (produtoEdicao.getProduto().getTipoProduto() != tipoProduto ) {
			
			produtoEdicao.getProduto().setTipoProduto(tipoProduto);
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Tipo de Publicacao para: " + tipoProduto.getDescricao());
		}
		if (!produtoEdicao.getProduto().getNome().equals(input.getNomePublicacao())) { 
			
			produtoEdicao.getProduto().setNome(input.getNomePublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Nome da Publicacao para: " + input.getNomePublicacao());
		}
		if (produtoEdicao.getProduto().getCodigoContexto() != input.getContextoPublicacao()) {
			
			produtoEdicao.getProduto().setCodigoContexto(input.getContextoPublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Contexto Publicacao para: " + input.getContextoPublicacao());
		}
		if (!produtoEdicao.getProduto().getDescricao().equals(input.getNomePublicacao())) {
		
			produtoEdicao.getProduto().setDescricao(input.getNomePublicacao());	
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao da Descricao para: " + input.getNomePublicacao());
		}
		if (!produtoEdicao.getProduto().getEditor().getCodigo().equals(input.getCodigoEditor())) {
			
			produtoEdicao.getProduto().setEditor(editor);
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Editor para: " + editor.getNome());
		}
		if (produtoEdicao.getProduto().getPeriodicidade() != periodicidadeProduto) {
			
			produtoEdicao.getProduto().setPeriodicidade(periodicidadeProduto);
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao da Periodicidade para: " + periodicidadeProduto);
		}
		if (!produtoEdicao.getProduto().getSlogan().equals(input.getSlogan())) {
			
			produtoEdicao.getProduto().setSlogan(input.getSlogan());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Slogan para: " + input.getSlogan());
		}
		if (produtoEdicao.getProduto().getPeb() != input.getPeb()){
			
			produtoEdicao.setPeb(input.getPeb());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do PEB para: " + input.getPeb());
		}
		if (produtoEdicao.getProduto().getPeso().equals(input.getPeso())) {
			
			produtoEdicao.setPeso(input.getPeso());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Peso para: " + input.getPeso());
		}
		
		
		if ( fornecedor != null )  {
			
			if (produtoEdicao.getProduto().getFornecedor().getCodigoInterface() != fornecedor.getCodigoInterface()) {
				
				produtoEdicao.getProduto().addFornecedor(fornecedor);
				
				this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
						"Atualizacao de Fornecedor para: " + fornecedor);
			}
		}
		
		
		if (!produtoEdicao.getNumeroEdicao().equals(input.getCodigoPublicacao())) {
			
			produtoEdicao.setNumeroEdicao(input.getCodigoPublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Codigo da Publicacao para: " + input.getCodigoPublicacao());
		}
		if (produtoEdicao.isAtivo() != input.isStatus()) {
			
			produtoEdicao.setAtivo(input.isStatus());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Status para: " + input.isStatus());
		}
		if (!produtoEdicao.getDataDesativacao().equals(input.getDataDesativacao())) {
			
			produtoEdicao.setDataDesativacao(input.getDataDesativacao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao da Data de Desativacao para: " + input.getDataDesativacao());
		}
		if (produtoEdicao.getPeb() != input.getPeb()){
			
			produtoEdicao.setPeb(input.getPeb());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do PEB para: " + input.getPeb());
		}
		if(produtoEdicao.getPacotePadrao() != input.getPacotePadrao()){ 
			
			produtoEdicao.setPacotePadrao(input.getPacotePadrao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Pacote Padrao para: " + input.getPacotePadrao());
		}	
		if (!produtoEdicao.getPeso().equals(input.getPeso())) {
			
			produtoEdicao.setPeso(input.getPeso());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Peso para: " + input.getPeso());
		}

		if ( descontoLogistica != null ) {
			
			/*if (!produtoEdicao.getDescontoLogistica().equals(descontoLogistica)) {

				produtoEdicao.setDescontoLogistica(descontoLogistica);
				
				this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
						"Atualizacao do Tipo Desconto para: " + descontoLogistica.getTipoDesconto());
						
			}*/
		}
	}
}
