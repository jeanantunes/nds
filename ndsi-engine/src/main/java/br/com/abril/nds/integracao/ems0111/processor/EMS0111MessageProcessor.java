package br.com.abril.nds.integracao.ems0111.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0111Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

/**
 * @author Jones.Costa
 * @version 1.0
 */

@Component
public class EMS0111MessageProcessor extends AbstractRepository implements
		MessageProcessor {
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");  

	// METODO PARA AJUSTAR A INTERFACE AO ENUM
	public TipoLancamento parseTipo(String tipo) {
		if (tipo.equalsIgnoreCase("LAN"))
			return TipoLancamento.LANCAMENTO;
		if (tipo.equalsIgnoreCase("SUP"))
			return TipoLancamento.SUPLEMENTAR;
		if (tipo.equalsIgnoreCase("REL"))
			return TipoLancamento.RELANCAMENTO;
		if (tipo.equalsIgnoreCase("PAR"))
			return TipoLancamento.PARCIAL;
		return null;
	}

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {

		EMS0111Input input = (EMS0111Input) message.getBody();
		if (input == null) {
			this.ndsiLoggerFactory.getLogger().logError(
					message, EventoExecucaoEnum.ERRO_INFRA, "NAO ENCONTROU o Arquivo");
			return;
		}
		
		
		// Validar Produto/Edicao
		final String codigoProduto = input.getCodigoProduto();
		final Long edicao = input.getEdicaoProduto();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
				edicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ codigoProduto
							+ " e Edicao: " + edicao
							+ " na tabela produto_edicao");
			return;
		}
		
		
		// Verificação de alteração do Preço Previsto para o ProdutoEdiçao:
		final BigDecimal precoPrevisto = input.getPrecoPrevisto();
		if (!produtoEdicao.getPrecoPrevisto().equals(precoPrevisto)) {
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteracao do Preco Previsto do Produto: "
							+ codigoProduto
							+ " e Edicao: " + edicao
							+ " , de: " + produtoEdicao.getPrecoPrevisto() 
							+ "para: " + precoPrevisto);
			produtoEdicao.setPrecoPrevisto(precoPrevisto);
			this.getSession().merge(produtoEdicao);
		}
		
		
		final Date dataLancamento = input.getDataLancamento();
		final Date dataGeracaoArquivo = input.getDataGeracaoArquivo();
		Lancamento lancamento = this.getLancamentoPrevistoMaisProximo(
				produtoEdicao, dataLancamento, dataGeracaoArquivo);
		if (lancamento == null ) {
			
			// Cadastrar novo lançamento
			lancamento = new Lancamento();
			
			// Cálcular data de recolhimento
			Calendar calRecolhimento = Calendar.getInstance();
			calRecolhimento.setTime(input.getDataLancamento());
			calRecolhimento.add(Calendar.DAY_OF_MONTH, produtoEdicao.getPeb());
			final Date dataRecolhimento = calRecolhimento.getTime();

			// Data da Operação do sistema:
			final Date dataOperacao = distribuidorService.obter().getDataOperacao();
			lancamento.setDataStatus(dataOperacao);
			
			lancamento.setId(null);
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setDataLancamentoPrevista(input.getDataLancamento());
			lancamento.setTipoLancamento(parseTipo(input.getTipoLancamento()));
			lancamento.setReparte(BigInteger.valueOf(input.getRepartePrevisto()));
			lancamento.setStatus(StatusLancamento.TRANSMITIDO);
			lancamento.setRepartePromocional(BigInteger.valueOf(input.getRepartePromocional()));// confirmado
			lancamento.setDataCriacao(new Date());// confirmado
			lancamento.setDataLancamentoDistribuidor(input.getDataLancamento());// confirmado
			lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);// confirmado
			lancamento.setDataRecolhimentoPrevista(dataRecolhimento);// confirmado
			lancamento.setExpedicao(null);// default
			lancamento.setHistoricos(null);// default
			lancamento.setRecebimentos(null);// default
			lancamento.setNumeroReprogramacoes(null);// confirmado
			lancamento.setSequenciaMatriz(null);// confirmado				

			// EFETIVAR INSERCAO NA BASE
			getSession().persist(lancamento);			
		} else {
			
			/*
			 * Atualizar Lançamento:
			 * 
			 * 01) Atualizar os atributos (e 'logar'):
			 * - Reparte Previsto; Reparte Promocional; Tipo Lançamento; 
			 * Data Lançamento Previsto;
			 */
			final BigInteger repartePrevisto = BigInteger.valueOf(
					input.getRepartePrevisto());
			if (!lancamento.getReparte().equals(repartePrevisto)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao do REPARTE PREVISTO do Produto: "
								+ codigoProduto
								+ " e Edicao: " + edicao
								+ " , de: " + lancamento.getReparte() 
								+ "para: " + repartePrevisto);
				lancamento.setReparte(repartePrevisto);
			}
			
			final BigInteger repartePromocional = BigInteger.valueOf(
					input.getRepartePromocional());
			if (!lancamento.getRepartePromocional().equals(repartePromocional)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao do REPARTE PROMOCIONAL do Produto: "
								+ codigoProduto
								+ " e Edicao: " + edicao
								+ " , de: " + lancamento.getRepartePromocional() 
								+ "para: " + repartePromocional);
				lancamento.setRepartePromocional(repartePromocional);
			}
			
			final TipoLancamento tipoLancamento = 
					this.parseTipo(input.getTipoLancamento());  
			if (!lancamento.getTipoLancamento().equals(tipoLancamento)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao do TIPO LANCAMENTO do Produto: "
								+ codigoProduto
								+ " e Edicao: " + edicao
								+ " , de: " + lancamento.getTipoLancamento().getDescricao() 
								+ "para: " + tipoLancamento.getDescricao());
				lancamento.setTipoLancamento(tipoLancamento);
			}
			
			
			// Remover a hora, minuto, segundo e milissegundo para comparação:
			final Date dtLancamentoAtual = this.normalizarDataSemHora(
					lancamento.getDataLancamentoPrevista());
			final Date dtLancamentoNovo = this.normalizarDataSemHora(dataLancamento);
			if (!dtLancamentoAtual.equals(dtLancamentoNovo)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao da DATA LANCAMENTO PREVISTO do Produto: "
								+ codigoProduto
								+ " e Edicao: " + edicao
								+ " , de: " + simpleDateFormat.format(
										dtLancamentoAtual)
								+ "para: " + simpleDateFormat.format(
										dtLancamentoNovo));
				lancamento.setDataLancamentoPrevista(dtLancamentoNovo);
			}
			
			
			// Atualizar lançamento
			final Date dtLancamentoDistribuidor = 
					lancamento.getDataLancamentoDistribuidor();
			
			
			
			final StatusLancamento status = lancamento.getStatus();
			
			
		}
		
		
		

		if (null != produtoEdicao) {
			// SE EXISTIR PRODUTO/EDICAO NA TABELA
			// VERIFICAR SE EXISTE LANCAMENTO CADASTRADO PARA O PRODUTO/EDICAO
				
			produtoEdicao.setPrecoVenda(input.getPrecoPrevisto());			
			
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT lcto FROM Lancamento lcto ");
			sql.append(" WHERE ");
			sql.append("		lcto.produtoEdicao = :produtoEdicao");
			sql.append(" AND   ");
			sql.append(" 		lcto.dataLancamentoPrevista = :dataLancamento   ");

			Query query = getSession().createQuery(sql.toString());
			query.setParameter("produtoEdicao", produtoEdicao);
			query.setParameter("dataLancamento", input.getDataLancamento());

//			Lancamento lancamento = (Lancamento) query.uniqueResult();
			if (null != lancamento) {

				// VERIFICAR SE OS CAMPOS ESTAO DESATUALIZADOS
				// CASO NECESSARIO, ATUALIZAR OS CAMPOS

				if (!lancamento.getDataLancamentoPrevista().equals(
						input.getDataLancamento())) {
					lancamento.setDataLancamentoPrevista(input
							.getDataLancamento());
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao da data de lancamento: "
									+ input.getDataLancamento());
				}

				if (!lancamento.getTipoLancamento().equals(
						parseTipo(input.getTipoLancamento())))
					;
				{

					lancamento.setTipoLancamento(parseTipo(input
							.getTipoLancamento()));
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do tipo de lancamento: "
									+ input.getTipoLancamento());

				}

				if (lancamento.getReparte().longValue() != input.getRepartePrevisto())
					;
				{
					lancamento.setReparte( new BigInteger( input.getRepartePrevisto().toString() ));
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do Reparte Previsto: "
									+ input.getRepartePrevisto());

				}

				if (lancamento.getRepartePromocional().longValue() != input
						.getRepartePromocional())
					;
				{
					lancamento.setRepartePromocional( new BigInteger( input
							.getRepartePromocional().toString()));

					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do Reparte Promocional: "
									+ input.getRepartePromocional());

				}

			}

		}

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
	 * Obtém o Lançamento com a data de lançamento mais próximo da data de 
	 * lançamento previsto.
	 *  
	 * @param produtoEdicao
	 * @param dataLancamento Data de Lançamento da Edição.
	 * @param dataGeracaoArquivo Data de Geração do Arquivo.
	 * 
	 * @return
	 */
	private Lancamento getLancamentoPrevistoMaisProximo(
			ProdutoEdicao produtoEdicao, Date dataLancamento, 
			Date dataGeracaoArquivo) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoPrevista > :dataGeracaoArquivo ");
		sql.append("      AND lcto.dataLancamentoPrevista = :dataLancamento ");
		sql.append(" ORDER BY lcto.dataLancamentoPrevista ASC");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataGeracaoArquivo", dataGeracaoArquivo);
		query.setDate("dataLancamento", dataLancamento);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}
	
	/**
	 * Normaliza uma data, para comparações, zerando os valores de hora (hora,
	 * minuto, segundo e milissendo).
	 * 
	 * @param dt
	 * 
	 * @return
	 */
	private Date normalizarDataSemHora(Date dt) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
	
		
	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void posProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
}
