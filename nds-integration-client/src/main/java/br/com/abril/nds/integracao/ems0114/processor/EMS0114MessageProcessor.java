package br.com.abril.nds.integracao.ems0114.processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0114Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.SemanaUtil;

@Component
public class EMS0114MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");  

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
	}

	@Override
	public void processMessage(Message message) {

		EMS0114Input input = (EMS0114Input) message.getBody();

		// Validar Produto/Edicao
		final String codigoProduto = input.getCodProd();
		final Long edicao = input.getEdicao();
		ProdutoEdicao produtoEdicao = null; 
		
		try {
			produtoEdicao = produtoEdicaoRepository.obterMaxProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, edicao);
		} catch (Exception e) {
			this.ndsiLoggerFactory.getLogger().logError(message
					, EventoExecucaoEnum.HIERARQUIA
					, "produto.codigo: " + codigoProduto.toString() 
						+ ", produtoEdicao.numeroEdicao:"
						+ edicao.toString() + " Erro:" + e.getMessage());
			
			return;
		}
		
		
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Produto "
					+ codigoProduto + " Edição " + edicao
					+" não encontrado.");
			return;
		}		

		
		final Date dataGeracaoArquivo = input.getDataGeracaoArq();
		
		Lancamento lancamento = this.getLancamentoRecolhimentoMaisProximo(produtoEdicao, dataGeracaoArquivo);
		
		if (lancamento == null) {
			
			lancamento = this.getLancamentoAnteriorRecolhimentoMaisProximo(produtoEdicao, dataGeracaoArquivo);
			
			if (lancamento == null) {
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"SEM LANCAMENTOS com RECOLHIMENTO para Produto "
								+ codigoProduto + " Edição " + edicao);
				return;
			}
		}
		
		if (lancamento.getStatus() == StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO
				|| lancamento.getStatus() == StatusLancamento.BALANCEADO_RECOLHIMENTO
				|| lancamento.getStatus() == StatusLancamento.EM_RECOLHIMENTO
				|| lancamento.getStatus() == StatusLancamento.RECOLHIDO
				|| lancamento.getStatus() == StatusLancamento.FECHADO) {
			
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.ERRO_INFRA,
					String.format( "Registro não será atualizado pois já está em processo de recolhimento. Data de recolhimento %1$s Produto %2$s Edição %3$s", sdf.format(input.getDataRecolhimento()), input.getCodProd(), input.getEdicao().toString() ));
			return;
		}
		
		final Date dtRecolhimentoDistribuidor = this.normalizarDataSemHora(lancamento.getDataRecolhimentoDistribuidor());
		
    //   nao usar a data de recolhimento, e sim a data do primeiro dia da semana  para  obter a matriz de recolhimento
	//	 final Date dtRecolhimentoArquivo = recolhimentoService.obterDataRecolhimentoValido(this.normalizarDataSemHora(input.getDataRecolhimento()),produtoEdicao.getProduto().getFornecedor().getId());
	//   obter a data do  primeiro dia da semana e  usar esta data para procurar a data de recolhimento
		
        int inicioSemanaRecolhimento = distribuidorService.inicioSemanaRecolhimento().getCodigoDiaSemana();
		Date dataRecolhimentoInicioSemana = SemanaUtil.obterDataInicioSemana(inicioSemanaRecolhimento, 
				this.normalizarDataSemHora(input.getDataRecolhimento()));
		final Date dtRecolhimentoArquivo = recolhimentoService.obterDataRecolhimentoValido(dataRecolhimentoInicioSemana,produtoEdicao.getProduto().getFornecedor().getId());

		if (!dtRecolhimentoDistribuidor.equals(dtRecolhimentoArquivo)) {
			
			final Date dtRecolhimentoPrevista = this.normalizarDataSemHora(
					lancamento.getDataRecolhimentoPrevista());
			
			if (!dtRecolhimentoPrevista.equals(dtRecolhimentoArquivo)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração da DATA RECOLHIMENTO PREVISTA"
								+ " de " + DateUtil.formatarDataPTBR(dtRecolhimentoPrevista)
								+ " para " + DateUtil.formatarDataPTBR(dtRecolhimentoArquivo)
								+ " Produto "+codigoProduto 
								+ " Edição " + edicao);
				lancamento.setDataRecolhimentoPrevista(dtRecolhimentoArquivo);
				lancamento.setAlteradoInteface(true);
				this.getSession().merge(lancamento);
			}
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da DATA RECOLHIMENTO DISTRIBUIDOR"
							+ " de " + DateUtil.formatarDataPTBR(dtRecolhimentoDistribuidor)
							+ " para " + DateUtil.formatarDataPTBR(dtRecolhimentoArquivo)
							+ " Produto "+codigoProduto 
							+ " Edição " + edicao);
			lancamento.setDataRecolhimentoDistribuidor(dtRecolhimentoArquivo);
			lancamento.setAlteradoInteface(true);
			this.getSession().merge(lancamento);
			
			this.lancamentoService.atualizarRedistribuicoes(lancamento, dtRecolhimentoArquivo, true);
			
			this.tratarParciais(lancamento, message, codigoProduto, edicao);
		}
	}

	private boolean tratarParciais(Lancamento lancamento, Message message, String codigoProduto, Long edicao) {
		
		try {
			
			PeriodoLancamentoParcial periodoLancamentoParcial = lancamento.getPeriodoLancamentoParcial();
			
			if (periodoLancamentoParcial != null) {
				
				this.parciaisService.reajustarRedistribuicoes(
					periodoLancamentoParcial,
					lancamento.getDataLancamentoDistribuidor(),
					lancamento.getDataRecolhimentoDistribuidor());
			}
		
			return false;
			
		} catch (Exception e) {
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					String.format("Erro ao processar as parcias para o Produto %1$s Edição %2$s. " + e.getMessage(),
								  codigoProduto, edicao));
			
			return true;
		}
	}
	
	/**
	 * Obtém o Lançamento com a data de recolhimento mais próximo da data de 
	 * recolhimento desejado.
	 *  
	 * @param produtoEdicao
	 * @param dataGeracaoArquivo Data de Geração do Arquivo.
	 * 
	 * @return
	 */
	private Lancamento getLancamentoRecolhimentoMaisProximo(
			ProdutoEdicao produtoEdicao, Date dataGeracaoArquivo) {
		
		Criteria criteria = this.getSession().createCriteria(Lancamento.class);

		criteria.add(Restrictions.gt("dataRecolhimentoPrevista", dataGeracaoArquivo));
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		criteria.add(Restrictions.eq("tipoLancamento", TipoLancamento.LANCAMENTO));
		criteria.addOrder(Order.asc("dataRecolhimentoPrevista"));
		
		criteria.setFetchSize(1);
		criteria.setMaxResults(1);
		
		return (Lancamento) criteria.uniqueResult();
	}

	private Lancamento getLancamentoAnteriorRecolhimentoMaisProximo(
			ProdutoEdicao produtoEdicao, Date dataGeracaoArquivo) {
		
		Criteria criteria = this.getSession().createCriteria(Lancamento.class);

		criteria.add(Restrictions.le("dataRecolhimentoPrevista", dataGeracaoArquivo));
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		criteria.add(Restrictions.eq("tipoLancamento", TipoLancamento.LANCAMENTO));
		criteria.addOrder(Order.desc("dataRecolhimentoPrevista"));
		
		criteria.setFetchSize(1);
		criteria.setMaxResults(1);
		
		return (Lancamento) criteria.uniqueResult();
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
	public void posProcess(Object tempVar) {
	}
}