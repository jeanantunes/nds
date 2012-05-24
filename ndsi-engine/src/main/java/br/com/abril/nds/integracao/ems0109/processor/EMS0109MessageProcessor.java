package br.com.abril.nds.integracao.ems0109.processor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.EventoExecucaoEnum;
import br.com.abril.nds.integracao.model.canonic.EMS0109Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;

@Component
public class EMS0109MessageProcessor implements MessageProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;

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
			
			TipoProduto tipoProduto = this.obterTipoProduto("Revista");
			
			if (tipoProduto == null) {
				
				this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.SEM_DOMINIO, "Tipo Produto REVISTA nao encontrado.");
				throw new RuntimeException("Tipo Produto nao encontrado.");
			} 
			
			produtoEdicao = this.obterProdutoEdicao(input.getCodigoPublicacao());
							
			if (produtoEdicao == null) {

				produtoEdicao = this.criarProdutoEdicaoConformeInput(produtoEdicao, input, editor, tipoProduto);
				this.entityManager.persist(produtoEdicao);
				
			} else {
				
				produtoEdicao = this.atualizaProdutoEdicaoConformeInput(produtoEdicao, input, editor, tipoProduto, message);				
			}
			
		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Distribuidor nao encontrato.");
			throw new RuntimeException("Distribuidor incorreto.");
		}
	}

	private boolean verificarDistribuidor(Integer codigoDistribuidor) {
		
		Distribuidor distribuidor = this.obterDistribuidor();
		
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	private Distribuidor obterDistribuidor() {
		
		return this.distribuidorService.findDistribuidor();
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private TipoProduto obterTipoProduto(String descricao) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT tp FROM TipoProduto tp ");
		sql.append("WHERE  tp.descricao = :descricao ");
			
		try {
			
			Query query = this.entityManager.createQuery(sql.toString());;
			
			query.setParameter("descricao", descricao);
			
			return (TipoProduto) query.getSingleResult();
			
		} catch (NoResultException e) {
			return null;			
		} catch (Exception e) {			
			throw new RuntimeException(e);
		}
	}	
	
	private ProdutoEdicao criarProdutoEdicaoConformeInput(ProdutoEdicao produtoEdicao, EMS0109Input input, Editor editor, TipoProduto tipoProduto) {
		produtoEdicao = new ProdutoEdicao();	
		Produto produto = new Produto();
			
		produtoEdicao.setProduto(produto);
				
		produtoEdicao.getProduto().setNome(input.getNomePublicacao());
		produtoEdicao.getProduto().setTipoProduto(tipoProduto);
		produtoEdicao.getProduto().setCodigoContexto(input.getContextoPublicacao());
		produtoEdicao.getProduto().setDescricao(input.getNomePublicacao());	
		produtoEdicao.getProduto().setEditor(editor);
		
		produtoEdicao.setNumeroEdicao(input.getCodigoPublicacao());
		produtoEdicao.setAtivo(input.isStatus());
		produtoEdicao.setDataDesativacao(input.getDataDesativacao());
		produtoEdicao.setPeb(input.getPeb());
		produtoEdicao.setPacotePadrao(input.getPacotePadrao());
		produtoEdicao.setPeso(input.getPeso());
		produtoEdicao.setSlogan(input.getSlogan());
		
		// FIXME: descobrir qual e o codigo do fornecedor.
		// FIXME: campo que nao foi criado ainda.
		// produtoEdicao.setTipoDesconto(input.getTipoDesconto());
				
		// TODO: Aguardado tabela DE - PARA.		
		String periodicidadeArquivo = input.getPeriodicidade();
		produtoEdicao.getProduto().setPeriodicidade(PeriodicidadeProduto.SEMANAL);

		return produtoEdicao;
	}
	
	private ProdutoEdicao atualizaProdutoEdicaoConformeInput(ProdutoEdicao produtoEdicao, EMS0109Input input, Editor editor, TipoProduto tipoProduto, Message message) {
		
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
		if (produtoEdicao.getSlogan().equals(input.getSlogan())) {
			produtoEdicao.setSlogan(input.getSlogan());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Slogan para: " + input.getSlogan());
		}
				
		return produtoEdicao;
	}
}
