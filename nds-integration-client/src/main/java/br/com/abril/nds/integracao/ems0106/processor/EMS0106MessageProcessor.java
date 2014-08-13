package br.com.abril.nds.integracao.ems0106.processor;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0106.inbound.EMS0106Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0106MessageProcessor extends AbstractRepository implements MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		distribuidorService.bloqueiaProcessosLancamentosEstudos();
	}
	
	@Override
	public void processMessage(Message message) {
		
		EMS0106Input input = (EMS0106Input) message.getBody();
		
		String codigoPublicacao = input.getCodigoPublicacao();
		Long edicao = input.getEdicao();

		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoPublicacao, edicao);
		
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"NAO ENCONTROU Produto de codigo: " + codigoPublicacao + " edicao: " + edicao);
			return;
		}
			
		Lancamento lancamento = this.getLancamentoPrevistoMaisProximo(produtoEdicao);
		if (lancamento == null) {
			lancamento = getLancamentoPrevistoAnteriorMaisProximo(produtoEdicao);
			
			if (lancamento == null) {
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO, 
						"NAO ENCONTROU Lancamento para o Produto de codigo: " + codigoPublicacao + " edicao: " + edicao);
				return;
			}
		}
		
		//Não continuar linha do arquivo caso esteja com algum desses status
		if (lancamento.getStatus() == StatusLancamento.EXPEDIDO || lancamento.getStatus() == StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO || 
				lancamento.getStatus() == StatusLancamento.FURO || lancamento.getStatus() == StatusLancamento.BALANCEADO_RECOLHIMENTO || 
				lancamento.getStatus() == StatusLancamento.EM_RECOLHIMENTO || lancamento.getStatus() == StatusLancamento.RECOLHIDO ||
				lancamento.getStatus() == StatusLancamento.FECHADO
			) {
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, 
					"Lancamento para o Produto de codigo: " + codigoPublicacao + " edicao: " + edicao + " está com STATUS: " +lancamento.getStatus().getDescricao()+" e portanto, não gerará ou alterará o estudo!");
			return;
		}
		
		Estudo estudo = lancamento.getEstudo();
		if (estudo == null) {
			
			// Cadastrar novo estudo:
			estudo = new Estudo();
			estudo.setQtdeReparte(BigInteger.valueOf(input.getReparteDistribuir()));
			estudo.setDataLancamento(lancamento.getDataLancamentoPrevista());
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
			estudo.setDataCadastro(new Date());			
			
			// Associar novo estudo com o lançamento existente:
			lancamento.setEstudo(estudo);
			
			if(lancamento.getReparte() == null || (lancamento.getReparte() != null && lancamento.getReparte().compareTo(new BigInteger("1")) < 0)) {
				lancamento.setReparte(estudo.getQtdeReparte());
			}
			
			this.getSession().merge(lancamento);
		} else {
			
			//Cenário Lancamento.status in('PLANEJADO', 'CONFIRMADO', 'EM_BALANCEAMENTO', 'BALANCEADO', 'ESTUDO_FECHADO', 'CANCELADO')
			
			for(EstudoCota estudoCota : estudo.getEstudoCotas()){
				
				//Desvincula os ItemNotaEnvios dos EstudoCotas a serem deletados p/ nao apresentar erro de FK
				for(ItemNotaEnvio itemNota : estudoCota.getItemNotaEnvios()){
					
					Query queryNotaItem = getSession().createQuery("delete from ItemNotaEnvio where itemNotaEnvioPK = :itemNotaEnvioPK");
					queryNotaItem.setParameter("itemNotaEnvioPK", itemNota.getItemNotaEnvioPK());
					queryNotaItem.executeUpdate();

					this.ndsiLoggerFactory.getLogger().logWarning(message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Excluído item nota de envio número: "
									+ itemNota.getItemNotaEnvioPK().getNotaEnvio().getNumero()
									+ " data de emissão: "+ itemNota.getItemNotaEnvioPK().getNotaEnvio().getDataEmissao()
									+ " número cota: : " + estudoCota.getCota().getNumeroCota()
									+ " publicação: " + codigoPublicacao);
				}
				
				//Verifica se tem movimento_estoque_cota associado ao estudo_cota
				boolean retornar = false;
				for(MovimentoEstoqueCota movimentosEstoqueCota : estudoCota.getMovimentosEstoqueCota()){
					
					this.ndsiLoggerFactory.getLogger().logError(message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Movimento desta publicação já foi consignado data: "
									+ movimentosEstoqueCota.getData()
									+ " número cota: : " + estudoCota.getCota().getNumeroCota()
									+ " publicação: " + codigoPublicacao +" o estudo da cota não poderá ser excluído.");
					retornar = true;
				}
				
				if(retornar)
					return;
			}
			
			
			// Remoção dos EstudoCotas que ficaram desatualizados:
			Query queryEstudoCota = getSession().createQuery("DELETE EstudoCota e WHERE e.estudo = :estudo");
			queryEstudoCota.setParameter("estudo", estudo);
			queryEstudoCota.executeUpdate();

			estudo.setEstudoCotas(Collections.<EstudoCota>emptySet());
			
			// Atualizar os dados do Estudo:
			BigInteger qtdeReparteAtual = estudo.getQtdeReparte();
			BigInteger qtdeReparteCorrente = BigInteger.valueOf(input.getReparteDistribuir());
			
			if (!qtdeReparteAtual.equals(qtdeReparteCorrente)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao da QUANTIDADE REPARTE do Estudo: "
								+ estudo.getId()
								+ ", do Produto: " + estudo.getProdutoEdicao().getProduto().getCodigo()
								+ " / Edicao: " + estudo.getProdutoEdicao().getNumeroEdicao().toString()
								+ ", de: " + qtdeReparteAtual
								+ " para: " + qtdeReparteCorrente);
				estudo.setQtdeReparte(qtdeReparteCorrente);
			} else {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Remoção dos estudos cotas vinculados ao estudo: "
								+ estudo.getId()
								+ ", do Produto: " + estudo.getProdutoEdicao().getProduto().getCodigo()
								+ " / Edicao: " + estudo.getProdutoEdicao().getNumeroEdicao().toString()
								+ " realizado com sucesso.");
			}
			
			estudo.setDataAlteracao(new Date());
			this.getSession().merge(estudo);
			
			if(lancamento.getReparte() == null || (lancamento.getReparte() != null && lancamento.getReparte().compareTo(new BigInteger("0")) < 1)) {
				lancamento.setReparte(estudo.getQtdeReparte());
			}
			
			this.getSession().merge(lancamento);
			
		}
		
		this.ndsiLoggerFactory.getLogger().logInfo(message,
				EventoExecucaoEnum.INF_DADO_ALTERADO,
				"EstudoCota processado: " + estudo.getId() + " para o Produto de codigo: " + codigoPublicacao + " edicao: " + edicao + 
				" no Lancamento: " + lancamento.getDataLancamentoPrevista().toString() + " Inserido com sucesso!");

	}
	
	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao, Long edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Obtém o Lançamento com data de lançamento mais próximo do dia corrente.
	 *  
	 * @param produtoEdicao
	 * @return
	 */
	private Lancamento getLancamentoPrevistoMaisProximo(
			ProdutoEdicao produtoEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoPrevista >= :dataOperacao ");
		sql.append(" ORDER BY lcto.dataLancamentoPrevista ASC");
		
		Query query = getSession().createQuery(sql.toString());
		
		// Estamos pegando a data atual do servidor devido ao fato da data de operação poder não estar compatível com a do MDC no piloto
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		//Date dataOperacao = new Date();
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataOperacao", dataOperacao);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}

	private Lancamento getLancamentoPrevistoAnteriorMaisProximo(
			ProdutoEdicao produtoEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoPrevista < :dataOperacao ");
		sql.append(" ORDER BY lcto.dataLancamentoPrevista DESC");
		
		Query query = getSession().createQuery(sql.toString());
		
		// Estamos pegando a data atual do servidor devido ao fato da data de operação poder não estar compatível com a do MDC no piloto
		//Date dataOperacao = new Date();
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataOperacao", dataOperacao);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}
	
	
	@Override
	public void posProcess(Object tempVar) {
		distribuidorService.desbloqueiaProcessosLancamentosEstudos();
	}
	
}