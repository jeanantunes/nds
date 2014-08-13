package br.com.abril.nds.integracao.ems0132.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.ems0132.outbound.EMS0132Output;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * Componente geracao do arquivo de Lancamentos.
 * 
 * @author Discover Technology
 */
@Component

public class EMS0132MessageProcessor extends AbstractRepository implements MessageProcessor  {

	
	@Autowired
	private FixedFormatManager fixedFormatManager; 
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	/**
	 * Value - LANP
	 */
	private static final String CODIGO_LANP = "LANP";
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		Distribuidor distribuidor = this.obterDistribuidor();
		
		if (distribuidor == null) {
			this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Distribuidor nao encontrato.");
			throw new RuntimeException("Distribuidor não encontrado.");
		}
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		if (dataOperacao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Data de operacao invalida.");
			throw new RuntimeException("Data de operacao invalida.");
		}
		
		List<Lancamento> listaEstudo = this.buscarLancamentosPorDataOperacao(message, dataOperacao);
		
		this.escreverArquivo(message, distribuidor, listaEstudo);
	}
	
	/**
	 * Busca os lancamentos.
	 * 
	 * @param message
	 * @param dataOperacao
	 * @return List<Estudo>
	 */
	@SuppressWarnings("unchecked")
	
	private List<Lancamento> buscarLancamentosPorDataOperacao(Message message, Date dataOperacao) {
		
		
		StringBuilder hql = new StringBuilder()
		.append("SELECT l ")
		.append("FROM Lancamento l ")
		.append("WHERE l.id ")
		.append("IN (SELECT lc.id FROM Lancamento lc ")
		.append("JOIN l.produtoEdicao pe ")
		.append("JOIN pe.produto p ")
		.append("JOIN p.fornecedores f ")
		.append("WHERE l.dataLancamentoDistribuidor = :dataOperacao)");
		
		try {
		
			Query query = this.getSession().createQuery(hql.toString());
					
			query.setParameter("dataOperacao", dataOperacao);
			
			return query.list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Metodo que gera e escreve o arquivo.
	 * 
	 * @param message
	 * @param distribuidor
	 * @param listaEstudo
	 */
	private void escreverArquivo(Message message, Distribuidor distribuidor, List<Lancamento> listaEstudo) {
		
		try {
						
			
			if (listaEstudo == null || listaEstudo.isEmpty()) {
				this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Nenhum dado encontrado para geracao do arquivo.");
				throw new RuntimeException("Nenhum dado encontrado para geracao do arquivo.");
			}
			
			Calendar dataAtual = Calendar.getInstance();			
			
			File arquivo = new File(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue()) + "/LANCAMENTO.NEP");
			
			PrintWriter writer = 
				new PrintWriter(
					new FileWriter(arquivo));
				
			for (Lancamento lancamento : listaEstudo) {
				
				EMS0132Output output = new EMS0132Output();

				if (lancamento != null) {

					Produto produto = lancamento.getProdutoEdicao().getProduto();
					
					for (Fornecedor fornecedor : produto.getFornecedores()) {

						output.setCodigoDistribuidor( Long.valueOf( distribuidor.getCodigoDistribuidorDinap() ));						
						output.setDataGeracaoArquivo(dataAtual.getTime());
						output.setHotaGeracaoArquivo(dataAtual.getTime());
						output.setMnemonicoTabela(CODIGO_LANP);
						output.setCodigoContexto(produto.getCodigoContexto());
						output.setCodigoFornecedor(fornecedor.getCodigoInterface());
						output.setCodigoProduto( lancamento.getProdutoEdicao().getProduto().getCodigo() );
						output.setNumeroEdicao(lancamento.getProdutoEdicao().getNumeroEdicao());
						output.setNumeroLancamento( 0L );
						output.setNumeroFase(0);
						output.setDataLancamento(lancamento.getDataLancamentoDistribuidor());
					}
				}
				
				writer.println(this.fixedFormatManager.export(output));
			}

			writer.flush();
			writer.close();
			
		} catch (IOException e) {	
			this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.ERRO_INFRA, "Nao foi possivel criar o arquivo.");
			throw new RuntimeException("Nao foi possivel criar o arquivo.");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * Busca o Distribuidor.
	 * 
	 * @return Distribuidor.
	 */
	private Distribuidor obterDistribuidor() {
		
		return this.distribuidorService.obter();
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
