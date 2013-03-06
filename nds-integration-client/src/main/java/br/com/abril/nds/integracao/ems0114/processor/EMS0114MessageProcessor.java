package br.com.abril.nds.integracao.ems0114.processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
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
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS0114MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");  

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0114Input input = (EMS0114Input) message.getBody();

		// Validar Produto/Edicao
		final String codigoProduto = input.getCodProd();
		final Long edicao = input.getEdicao();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
				edicao, message);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ codigoProduto + " e Edicao: " + edicao
							+ " na tabela produto_edicao");
			return;
		}		

		
		final Date dataGeracaoArquivo = input.getDataGeracaoArq();
		Lancamento lancamento = this.getLancamentoRecolhimentoMaisProximo(
				produtoEdicao, dataGeracaoArquivo);
		if (lancamento == null) {
			
			lancamento = this.getLancamentoAnteriorRecolhimentoMaisProximo(produtoEdicao, dataGeracaoArquivo);
			
			if (lancamento == null) {
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"SEM LANCAMENTOS com RECOLHIMENTO para Produto: "
								+ codigoProduto + " e Edicao: " + edicao);
				return;
			}
		}
		
		if (lancamento.getStatus() == StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO || lancamento.getStatus() == StatusLancamento.BALANCEADO_RECOLHIMENTO) {
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.ERRO_INFRA,
					String.format( "Registro não será atualizado pois já está em balanceamento / balanceado. Data de recolhimento: %1$s Produto: %2$s Edicao: %3$s.", sdf.format(input.getDataRecolhimento()), input.getCodProd(), input.getEdicao().toString() ));
			return;
		}
		
		final Date dtRecolhimentoDistribuidor = this.normalizarDataSemHora(
				lancamento.getDataRecolhimentoDistribuidor());
		final Date dtRecolhimentoArquivo = this.normalizarDataSemHora(
				input.getDataRecolhimento());
		if (!dtRecolhimentoDistribuidor.equals(dtRecolhimentoArquivo)) {
			
			final Date dtRecolhimentoPrevista = this.normalizarDataSemHora(
					lancamento.getDataRecolhimentoPrevista());
			if (!dtRecolhimentoPrevista.equals(dtRecolhimentoArquivo)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao da DATA RECOLHIMENTO PREVISTA do Produto: "
								+ codigoProduto + " e Edicao: " + edicao
								+ " , de: " + simpleDateFormat.format(
										dtRecolhimentoPrevista)
								+ "para: " + simpleDateFormat.format(
										dtRecolhimentoArquivo));
				lancamento.setDataRecolhimentoPrevista(dtRecolhimentoArquivo);
				lancamento.setAlteradoInteface(true);
				this.getSession().merge(lancamento);
			}
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteracao da DATA RECOLHIMENTO DISTRIBUIDOR do Produto: "
							+ codigoProduto + " e Edicao: " + edicao
							+ " , de: " + simpleDateFormat.format(
									lancamento.getDataRecolhimentoDistribuidor())
							+ "para: " + simpleDateFormat.format(
									dtRecolhimentoArquivo));
			lancamento.setDataRecolhimentoDistribuidor(dtRecolhimentoArquivo);
			lancamento.setAlteradoInteface(true);
			this.getSession().merge(lancamento);
			
		}
		
	}

	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * @param message 
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao,
			Long edicao, Message message) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			this.ndsiLoggerFactory.getLogger().logError(message
					, EventoExecucaoEnum.HIERARQUIA
					, "produto.codigo: " + codigoPublicacao.toString() 
						+ ", produtoEdicao.numeroEdicao:" 
						+ edicao.toString() + " Erro:" + e.getMessage());


			throw new RuntimeException(e);
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
		// TODO Auto-generated method stub
	}
	
}