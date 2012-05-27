package br.com.abril.nds.integracao.ems0110.processor;

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
import br.com.abril.nds.integracao.model.canonic.EMS0110Input;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;

@SuppressWarnings("unused")
@Component
public class EMS0110MessageProcessor implements MessageProcessor {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Override
	public void processMessage(Message message){
				
		if (verificarDistribuidor(message)) {
			
			ProdutoEdicao edicao = this.findProdutoEdicao(message);
			
			if (edicao == null) {
				
				Produto produto = this.findProduto(message);
				
				this.criarProdutoEdicaoConformeInput(produto, message);
				
			} else {
				
				this.atualizaProdutoEdicaoConformeInput(edicao, message);				
			}
			
		} else {
			
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Distribuidor nao encontrato.");
			throw new RuntimeException("Distribuidor nao encontrado.");
		}
	}
	
	
	private boolean verificarDistribuidor(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();
		
		Integer codigoDistribuidorSistema = (Integer) message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR);
		Integer codigoDistribuidorArquivo = Integer.parseInt(input.getCodigoDistribuidor()); 
			
		if (codigoDistribuidorSistema.equals(codigoDistribuidorArquivo)) {
			
			return true;
		}
		
		return false;
	}
	
	private ProdutoEdicao findProdutoEdicao(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT pe FROM ProdutoEdicao pe JOIN FETCH pe.produto p ");
		sql.append("WHERE pe.numeroEdicao = :numeroEdicao ");
		sql.append(" AND  p.codigo = :codigo ");
				
		Query query = this.entityManager.createQuery(sql.toString());
		
		query.setParameter("numeroEdicao", input.getEdicaoProd());
		query.setParameter("codigo", input.getCodProd());
		
		@SuppressWarnings("unchecked")
		List<ProdutoEdicao> produtoEdicoes = (List<ProdutoEdicao>) query.getResultList();
				
		if (!produtoEdicoes.isEmpty()) {
			
			ProdutoEdicao produtoEdicao = null;
			
			for (ProdutoEdicao produtoEdicao2 : produtoEdicoes) {
				
				if (produtoEdicao2.getNumeroEdicao().equals(input.getEdicaoProd())) {
					
					produtoEdicao = produtoEdicao2;
				}				
			}
			
			return produtoEdicao;
			
		} else {
			
			return null;
		}
	}
	
	private Produto findProduto(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT p FROM Produto p ");
		sql.append("WHERE p.codigo = :codigo ");
		
		try{
			
			Query query = this.entityManager.createQuery(sql.toString());
			
			query.setParameter("codigo", input.getCodProd());
			
			return (Produto) query.getSingleResult();
			
		} catch(NoResultException e) {
			
			// Nao encontrou o Produto. Realiza Log
			ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.HIERARQUIA,"Codigo PRODUTO " +  input.getCodProd() + " nao cadastrado.");
			throw new RuntimeException("Produto nao encontrado.");
		}		
	}

	private void criarProdutoEdicaoConformeInput(Produto produto, Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();
		
		ProdutoEdicao edicao = new ProdutoEdicao();
		Dimensao dimensao = new Dimensao();
		Brinde brinde = new Brinde();
		
		if (!produto.getCodigoContexto().equals(input.getContextoProd())) {
			
			produto.setCodigoContexto(input.getContextoProd());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Codigo Contexto Produto para: " + input.getContextoProd());
		}		
		if (!produto.getTipoProduto().getCodigoNBM().equals(input.getCodNBM())) {
			
			produto.getTipoProduto().setCodigoNBM(input.getCodNBM());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Codigo NBM para: " + input.getCodNBM());
		}
		
		edicao.setProduto(produto);
		
		dimensao.setLargura(input.getLargura());
		dimensao.setComprimento(input.getComprimento());
		dimensao.setEspessura(input.getExpessura());
		edicao.setDimensao(dimensao);
		
		brinde.setDescricao(input.getDescBrinde());
		brinde.setPermiteVendaSeparada(input.getCondVendeSeparado());
		edicao.setBrinde(brinde);
		
		edicao.setCodigoDeBarras(input.getCodBarra());
		edicao.setNumeroEdicao(input.getEdicaoProd());
		edicao.setDesconto(input.getTipoDesconto());
		edicao.setPacotePadrao(input.getPactPadrao());
		edicao.setPeb(input.getPeb());
		edicao.setPeso(input.getPesoUni());
		edicao.setPossuiBrinde(input.isContemBrinde());
		edicao.setDataDesativacao(input.getDataDesativacao());
		edicao.setChamadaCapa(input.getChamadaCapa());
		this.entityManager.persist(edicao);
	}
	
	private void atualizaProdutoEdicaoConformeInput(ProdutoEdicao edicao, Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		if (!edicao.getProduto().getCodigoContexto().equals(input.getContextoProd())) {
			
			edicao.getProduto().setCodigoContexto(input.getContextoProd());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Codigo Contexto Produto para: " + input.getContextoProd());
		}		
		if (!edicao.getProduto().getTipoProduto().getCodigoNBM().equals(input.getCodNBM())) {
			
			edicao.getProduto().getTipoProduto().setCodigoNBM(input.getCodNBM());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Codigo NBM para: " + input.getCodNBM());
		}
		
				
		if (!edicao.getDimensao().getLargura().equals(input.getLargura())) {
			
			edicao.getDimensao().setLargura(input.getLargura());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao Largura para: " + input.getLargura());
		}
		if (!edicao.getDimensao().getComprimento().equals(input.getComprimento())) {
			
			edicao.getDimensao().setComprimento(input.getComprimento());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao Comprimento para: " + input.getComprimento());
		}
		if (!edicao.getDimensao().getEspessura().equals(input.getExpessura())) {
			
			edicao.getDimensao().setEspessura(input.getExpessura());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao Espessura para: " + input.getExpessura());
		}	
			
		
		if (!edicao.getBrinde().getPermiteVendaSeparada() != input.getCondVendeSeparado()) {
			
			edicao.getBrinde().setPermiteVendaSeparada(input.getCondVendeSeparado());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao Permite Venda Separada para: " + input.getCondVendeSeparado());
		}
		if (!edicao.getBrinde().getDescricao().equals(input.getDescBrinde())) {
			
			edicao.getBrinde().setDescricao(input.getDescBrinde());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao Descricao Brinde para: " + input.getDescBrinde());
		}			
				
		
		if (!edicao.getChamadaCapa().equals(input.getChamadaCapa())) {
			
			edicao.setChamadaCapa(input.getChamadaCapa());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao Chamada Capa para: " + input.getChamadaCapa());
		}
		if (edicao.isPossuiBrinde() != input.isContemBrinde()) {
			
			edicao.setPossuiBrinde(input.isContemBrinde());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao Possui Brinde para: " + input.isContemBrinde());
		}
		if (!edicao.getDesconto().equals(input.getTipoDesconto())) {
			
			edicao.setDesconto(input.getTipoDesconto());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Tipo Desconto para: " + input.getTipoDesconto());
		}
		if (edicao.getCodigoDeBarras().equals(input.getCodBarra())) {
			
			edicao.setCodigoDeBarras(input.getCodBarra());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Codigo de Barra para: " + input.getCodBarra());
		}
		if (edicao.getNumeroEdicao() != input.getEdicaoProd()) {
			
			edicao.setNumeroEdicao(input.getEdicaoProd());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Numero da Publicacao para: " + input.getEdicaoProd());
		}
		if (edicao.getDataDesativacao() != input.getDataDesativacao()) {
			
			edicao.setDataDesativacao(input.getDataDesativacao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao da Data de Desativacao para: " + input.getDataDesativacao());
		}
		if (edicao.getPeb() != input.getPeb()){
			
			edicao.setPeb(input.getPeb());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do PEB para: " + input.getPeb());
		}
		if(edicao.getPacotePadrao() != input.getPactPadrao()){ 
			
			edicao.setPacotePadrao(input.getPactPadrao());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Pacote Padrao para: " + input.getPactPadrao());
		}	
		if (edicao.getPeso() != input.getPesoUni()) {
			
			edicao.setPeso(input.getPesoUni());
			this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, 
					"Atualizacao do Peso para: " + input.getPesoUni());
		}
	}
}