package br.com.abril.nds.integracao.ems0109.processor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0109Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.integracao.service.PeriodicidadeProdutoService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;

@Component
public class EMS0109MessageProcessor implements MessageProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private PeriodicidadeProdutoService periodicidadeProdutoService;

	@Override
	public void processMessage(Message message) {
		
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		Integer codigoDistribuidor = Integer.parseInt(input.getCodigoDistribuidor()); 
		
		ProdutoEdicao produtoEdicao = null;
				
		
		if (verificarDistribuidor(codigoDistribuidor)) {
			
			Editor editor = this.obterEditorByID(input.getCodigoEditor());
			
			if (editor == null) {
				
				this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.SEM_DOMINIO, "Editor " + input.getCodigoEditor() +  " nao encontrado.");
				throw new RuntimeException("Editor nao encontrado.");
			}
			
			TipoProduto tipoProduto = this.obterTipoProduto(GrupoProduto.REVISTA);
			
			if (tipoProduto == null) {
				
				this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.SEM_DOMINIO, "Tipo Produto REVISTA nao encontrado.");
				throw new RuntimeException("Tipo Produto nao encontrado.");
			} 
			
			produtoEdicao = this.obterProdutoEdicao(input.getCodigoPublicacao());
							
			if (produtoEdicao == null) {

				produtoEdicao = this.criarProdutoEdicaoConformeInput(message, editor, tipoProduto);
				this.entityManager.persist(produtoEdicao);
				
			} else {
				
				produtoEdicao = this.atualizaProdutoEdicaoConformeInput(produtoEdicao, editor, tipoProduto, message);				
			}
			
		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Distribuidor nao encontrato.");
			throw new RuntimeException("Distribuidor incorreto.");
		}
	}

	private boolean verificarDistribuidor(Integer codigoDistribuidor) {
		
		Distribuidor distribuidor = distribuidorService.findDistribuidor();
		
		if (distribuidor != null && distribuidor.getCodigo().equals(codigoDistribuidor)) {
			return true;
		}
		
		return false;
	}
	
	private ProdutoEdicao obterProdutoEdicao(Long codigoPublicacao) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select pe from ProdutoEdicao pe ");
		sql.append(" join fetch pe.produto p ");
		sql.append(" where pe.numeroEdicao = :codigoPublicacao ");
		
		try {
		
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("codigoPublicacao", codigoPublicacao);
			
			return (ProdutoEdicao) query.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		} 
	}
	
	private PeriodicidadeProduto obterPeriodicidadeProduto(Integer periodicidade) {
		
		return this.periodicidadeProdutoService.getPeriodicidadeProdutoAsArchive(periodicidade);
	}
	
	private Editor obterEditorByID(Long codigoEditor) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select editor from Editor editor ");
		sql.append( " where editor.codigo = :codigoEditor ");
		
		try {
			
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("codigoEdito", codigoEditor);
			
			return (Editor) query.getSingleResult();
			
		} catch (NoResultException e) {	
			
			return null;
		} 
	}
	
	private TipoDesconto obterTipoDescontoByCodigo(String codigoTipoDesconto) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select td from TipoDesconto td ");
		sql.append( " where td.codigo = :codigoTipoDesconto ");
		
		try {
			
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("codigoTipoDesconto", codigoTipoDesconto);
			
			return (TipoDesconto) query.getSingleResult();
			
		} catch (NoResultException e) {	
			
			return null;
		} 
	}
	
	private TipoProduto obterTipoProduto(GrupoProduto grupoProduto) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT tp FROM TipoProduto tp ");
		sql.append("WHERE  tp.GrupoProduto = :grupoProduto ");
			
		Query query = this.entityManager.createQuery(sql.toString());;
		query.setParameter("grupoProduto", grupoProduto);
		
		@SuppressWarnings("unchecked")
		List<TipoProduto> tiposProduto = (List<TipoProduto>) query.getSingleResult();
		
		TipoProduto tipoProduto = null;
		
		if (!tiposProduto.isEmpty()) {
			
			tipoProduto = tiposProduto.get(0);

			return tipoProduto;
			
		} else {
			
			return null;
		}
	}	
	
	private Fornecedor obterFornecedor(Integer codigoInterface) {
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
	
	private ProdutoEdicao criarProdutoEdicaoConformeInput(Message message, Editor editor, TipoProduto tipoProduto) {
		
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();	
		Produto produto = new Produto();
		produtoEdicao.setProduto(produto);
		
		PeriodicidadeProduto periodicidadeProduto = this.obterPeriodicidadeProduto(input.getPeriodicidade());
		Fornecedor fornecedor = this.obterFornecedor(input.getCodigoFornecedor());
		TipoDesconto tipoDesconto = this.obterTipoDescontoByCodigo(input.getTipoDesconto());
				
		produtoEdicao.getProduto().setNome(input.getNomePublicacao());
		produtoEdicao.getProduto().setTipoProduto(tipoProduto);
		produtoEdicao.getProduto().setCodigoContexto(input.getContextoPublicacao());
		produtoEdicao.getProduto().setDescricao(input.getNomePublicacao());	
		produtoEdicao.getProduto().setEditor(editor);
		produtoEdicao.getProduto().setPeriodicidade(periodicidadeProduto);
		produtoEdicao.getProduto().addFornecedor(fornecedor);
		
		produtoEdicao.setNumeroEdicao(input.getCodigoPublicacao());
		produtoEdicao.setAtivo(input.isStatus());
		produtoEdicao.setDataDesativacao(input.getDataDesativacao());
		produtoEdicao.setPeb(input.getPeb());
		produtoEdicao.setPacotePadrao(input.getPacotePadrao());
		produtoEdicao.setPeso(input.getPeso());
		
		// FIXME: Corrigir slogan que foi para o Produto
//		produtoEdicao.setSlogan(input.getSlogan());
			
		// FIXME Campo que falta criar
		// produtoEdicao.setTipoDesconto(tipoDesconto);

		return produtoEdicao;
	}
	
	private ProdutoEdicao atualizaProdutoEdicaoConformeInput(ProdutoEdicao produtoEdicao, Editor editor, TipoProduto tipoProduto, Message message) {
		
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		PeriodicidadeProduto periodicidadeProduto = this.obterPeriodicidadeProduto(input.getPeriodicidade());
		Fornecedor fornecedor = this.obterFornecedor(input.getCodigoFornecedor());
		TipoDesconto tipoDesconto = this.obterTipoDescontoByCodigo(input.getTipoDesconto());
		
		if (!produtoEdicao.getProduto().getNome().equals(input.getNomePublicacao())) { 
			
			produtoEdicao.getProduto().setNome(input.getNomePublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Nome da Publicacao para: " + input.getNomePublicacao());
		}
		if (produtoEdicao.getProduto().getTipoProduto() != tipoProduto ) {
			
			produtoEdicao.getProduto().setTipoProduto(tipoProduto);
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Tipo de Publicacao para: " + tipoProduto.getDescricao());
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
		if (produtoEdicao.getProduto().getEditor().getCodigo() != input.getCodigoEditor()) {
			
			produtoEdicao.getProduto().setEditor(editor);
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Editor para: " + editor.getNome());
		}
		if (produtoEdicao.getProduto().getPeriodicidade() != periodicidadeProduto) {
			
			produtoEdicao.getProduto().setPeriodicidade(periodicidadeProduto);
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao da Periodicidade para: " + periodicidadeProduto);
		}
		if (produtoEdicao.getProduto().getFornecedor().getCodigoInterface() != fornecedor.getCodigoInterface()) {
			
			produtoEdicao.getProduto().addFornecedor(fornecedor);
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao de Fornecedor para: " + fornecedor);
		}
		
		if (produtoEdicao.getNumeroEdicao() != input.getCodigoPublicacao()) {
			
			produtoEdicao.setNumeroEdicao(input.getCodigoPublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Codigo da Publicacao para: " + input.getCodigoPublicacao());
		}
		if (produtoEdicao.isAtivo() != input.isStatus()) {
			
			produtoEdicao.setAtivo(input.isStatus());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Status para: " + input.isStatus());
		}
		if (produtoEdicao.getDataDesativacao() != input.getDataDesativacao()) {
			
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
		if (produtoEdicao.getPeso() != input.getPeso()) {
			
			produtoEdicao.setPeso(input.getPeso());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Peso para: " + input.getPeso());
		}
		
		// FIXME: Corrigir slogan que foi para o Produto
		/*if (produtoEdicao.getSlogan().equals(input.getSlogan())) {
			
			produtoEdicao.setSlogan(input.getSlogan());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Slogan para: " + input.getSlogan());
		}*/
		
		
		return produtoEdicao;
	}
}
