package br.com.abril.nds.integracao.ems0110.processor;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.EMS0110Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.EventoExecucaoEnum;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.pdv.PDV;

@SuppressWarnings("unused")
@Component
public class EMS0110MessageProcessor implements MessageProcessor {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Override
	public void processMessage(Message message){
		EMS0110Input input = (EMS0110Input) message.getBody();
		
		Distribuidor distribuidor = this.obterDistribuidor();
		
		if (distribuidor == null) {
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Distribuidor nao encontrato.");
			throw new RuntimeException("Distribuidor nao encontrado.");
		}
		
		StringBuilder sql = new StringBuilder();
		ProdutoEdicao edicao = null;
		
		sql.append("SELECT pe  ");
		sql.append("FROM ProdutoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("     pe.numeroEdicao = :numeroEdicao ");
		sql.append("  AND   p.codigo = :codigo ");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("numeroEdicao", input.getEdicaoProd());
		query.setParameter("codigo", input.getCodProd());
		
		@SuppressWarnings("unchecked")
		List<ProdutoEdicao> produtoEdicoes = (List<ProdutoEdicao>) query.getResultList();
			
		if (produtoEdicoes.isEmpty()) {
			
			Produto produto = null;
			Dimensao dimensao = new Dimensao();
			Brinde brinde = new Brinde();
			sql = new StringBuilder();
			
			sql.append("SELECT p  ");
			sql.append("FROM Produto p ");
			sql.append("WHERE ");
			sql.append(" p.codigo = :codigo ");
			
			query = entityManager.createQuery(sql.toString());
			query.setParameter("codigo", input.getCodProd());
			
			try{
				
				produto = (Produto) query.getSingleResult();
				
			} catch(NoResultException e) {
				
				// Nao encontrou o Produto. Realizar Log
				// Passar para a proxima linha
				ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.HIERARQUIA,"Codigo PRODUTO " +  input.getCodProd() + " nao cadastrado.");
				e.printStackTrace();
				
				return;
			}
			
			edicao = new ProdutoEdicao();
			
			produto.setCodigoContexto(input.getContextoProd());
//			produto.setNbm(input.getCodNBM());
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
			edicao.setPrecoVenda(null);
			edicao.setDesconto(input.getTipoDesconto());
			edicao.setPacotePadrao(input.getPactPadrao());
			edicao.setPeb(input.getPeb());
			edicao.setPeso(input.getPesoUni());
			edicao.setPossuiBrinde(input.isContemBrinde());
			edicao.setDataDesativacao(input.getDataDesativacao());
			edicao.setChamadaCapa(input.getChamadaCapa());
			
			entityManager.persist(edicao);
			
		} else {
			
			for (ProdutoEdicao edicao2 : produtoEdicoes) {
				
				if (edicao2.getNumeroEdicao().equals(input.getEdicaoProd())) {
					
					edicao = edicao2;
				}				
			}
			
			ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Atualizacao do ProdutoEdicao " + edicao.getId());
			
			edicao.getProduto().setCodigoContexto(input.getContextoProd());
//			edicao.getProduto().setNbm(input.getCodNBM());
			
			edicao.getDimensao().setLargura(input.getLargura());
			edicao.getDimensao().setComprimento(input.getComprimento());
			edicao.getDimensao().setEspessura(input.getExpessura());
				
			edicao.getBrinde().setDescricao(input.getDescBrinde());
			edicao.getBrinde().setPermiteVendaSeparada(input.getCondVendeSeparado());
				
			edicao.setCodigoDeBarras(input.getCodBarra());
			edicao.setNumeroEdicao(input.getEdicaoProd());
			edicao.setDesconto(input.getTipoDesconto());
			edicao.setPacotePadrao(input.getPactPadrao());
			edicao.setPeb(input.getPeb());
			edicao.setPeso(input.getPesoUni());
			edicao.setPossuiBrinde(input.isContemBrinde());
			edicao.setDataDesativacao(input.getDataDesativacao());
			edicao.setChamadaCapa(input.getChamadaCapa());		
		}
	}
	
	private Distribuidor obterDistribuidor() {
		
		return this.distribuidorService.findDistribuidor();
	}
}