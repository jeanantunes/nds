package br.com.abril.nds.integracao.ems0106.processor;

import java.math.BigInteger;
import java.util.Calendar;
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
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
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
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {
		
		EMS0106Input input = (EMS0106Input) message.getBody();
		
		String codigoPublicacao = input.getCodigoPublicacao();
		Long edicao = input.getEdicao();

		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoPublicacao,
				edicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"NAO ENCONTROU Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao);
			return;
		}

		Date dataOperacao = distribuidorService.obter().getDataOperacao();

		Lancamento lancamento = this.getLancamentoPrevistoMaisProximo(produtoEdicao);
		
		Estudo estudo = null;
		
		if (lancamento == null) {
			lancamento = this.novoLancamento(produtoEdicao, null);
		} else {
		
			estudo = lancamento.getEstudo();
	
			if (estudo != null) {
			
				if ((lancamento.getDataLancamentoPrevista().after(dataOperacao))) {
					lancamento = this.novoLancamento(produtoEdicao, lancamento);
					
				} else if (lancamento.getDataLancamentoPrevista().equals(dataOperacao)) {
					
					if (lancamento.getStatus() == StatusLancamento.EXPEDIDO) {
						this.ndsiLoggerFactory.getLogger().logError(message,
								EventoExecucaoEnum.RELACIONAMENTO, 
								"Lancamento para o Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao + " está com STATUS 'EXPEDIDO' e portanto, não gerará ou alterará o estudo!");
						return;
					}

					estudo = lancamento.getEstudo();
					long id = estudo.getId();

					lancamento.setEstudo(null);
					getSession().merge(lancamento);
					
					Query query = getSession().createQuery("DELETE EstudoCota e WHERE e.estudo.id = :id");
					query.setParameter("id", id);
					query.executeUpdate();
		
					estudo = lancamento.getEstudo();
					query = getSession().createQuery("DELETE Estudo e WHERE e.id = :id");
					query.setParameter("id", id);
					query.executeUpdate();
					
					getSession().flush();
					
				}
			
			}
		
		}

		/*if (lancamento == null) {
			lancamento = getLancamentoPrevistoAnteriorMaisProximo(produtoEdicao);
			
			if (lancamento == null) {
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO, 
						"NAO ENCONTROU Lancamento para o Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao);
				return;
			}
		}*/
		
		if (lancamento.getStatus() == StatusLancamento.EXPEDIDO) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Lancamento para o Produto de codigo: " + codigoPublicacao + "/ edicao: " + edicao + " está com STATUS 'EXPEDIDO' e portanto, não gerará ou alterará o estudo!");
			return;
		}
		
		/*estudo = lancamento.getEstudo();
		if (estudo == null) {*/
			
		// Cadastrar novo estudo:
		estudo = new Estudo();
		estudo.setQtdeReparte(BigInteger.valueOf(
				input.getReparteDistribuir()));
		estudo.setDataLancamento(lancamento.getDataLancamentoPrevista());
		estudo.setProdutoEdicao(produtoEdicao);
		estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
		estudo.setDataCadastro(new Date());			
		
		// Associar novo estudo com o lançamento existente:
		lancamento.setEstudo(estudo);
		this.getSession().merge(lancamento);
			
		/*} else {
			
			// Remoção dos EstudoCotas que ficaram desatualizados:
			/*Query query = getSession().createQuery("DELETE EstudoCota e WHERE e.estudo = :estudo");
			query.setParameter("estudo", estudo);
			query.executeUpdate();
			estudo.setEstudoCotas(Collections.<EstudoCota>emptySet());
			
			// Atualizar os dados do Estudo:
			BigInteger qtdeReparteAtual = estudo.getQtdeReparte();
			BigInteger qtdeReparteCorrente = BigInteger.valueOf(
					input.getReparteDistribuir());
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
			
		}*/
		
	}
	
	private Lancamento novoLancamento(ProdutoEdicao produtoEdicao, Lancamento lancamentoParecido) {
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		
		Lancamento lancamento = new Lancamento();
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setAlteradoInteface(true);
		lancamento.setDataCriacao(dataOperacao);
		lancamento.setDataLancamentoDistribuidor(dataOperacao);
		lancamento.setDataLancamentoPrevista(dataOperacao);
		if (lancamentoParecido != null) {
			lancamento.setDataRecolhimentoDistribuidor(lancamentoParecido.getDataRecolhimentoDistribuidor());
			lancamento.setDataRecolhimentoPrevista(lancamentoParecido.getDataRecolhimentoPrevista());
		} else {
			
			int peb = produtoEdicao.getPeb() == 0 ? produtoEdicao.getProduto().getPeb() : produtoEdicao.getPeb();
			if (peb == 0) {
				peb = 10;
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(dataOperacao);
			cal.add(Calendar.DATE, peb); 		

			lancamento.setDataRecolhimentoDistribuidor(cal.getTime());
			lancamento.setDataRecolhimentoPrevista(cal.getTime());
		}
		lancamento.setDataStatus(dataOperacao);
		lancamento.setStatus(StatusLancamento.CONFIRMADO);
		lancamento.setTipoLancamento(TipoLancamento.RELANCAMENTO);
		
		getSession().persist(lancamento);
		getSession().flush();

		return lancamento;
	}

	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao,
			Long edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

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
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataOperacao", dataOperacao);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}

	/*private Lancamento getLancamentoPrevistoAnteriorMaisProximo(
			ProdutoEdicao produtoEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoPrevista < :dataOperacao ");
		sql.append(" ORDER BY lcto.dataLancamentoPrevista DESC");
		
		Query query = getSession().createQuery(sql.toString());
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataOperacao", dataOperacao);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}*/
	
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}