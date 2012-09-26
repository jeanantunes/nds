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

import br.com.abril.nds.integracao.ems0132.outbound.EMS0132Output;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.impl.AbstractRepository;
import br.com.abril.nds.util.DateUtil;

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
			throw new RuntimeException("Distribuidor nao encontrado.");
		}
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		if (dataOperacao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Data de operacao invalida.");
			throw new RuntimeException("Data de operacao invalida.");
		}
		
		List<Estudo> listaEstudo = this.buscarLancamentosPorDataOperacao(message, dataOperacao);
		
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
	
	private List<Estudo> buscarLancamentosPorDataOperacao(Message message, Date dataOperacao) {
		
		String hql = " select estudo from Estudo estudo  ";
		hql += " join fetch estudo.produtoEdicao produtoEdicao ";
		hql += " join fetch produtoEdicao.produto produto ";
		hql += " join fetch estudo.lancamentos lancamentos ";
		hql += " where lancamentos.dataLancamentoDistribuidor = :dataOperacao ";
		
		try {
		
			Query query = this.getSession().createQuery(hql);
					
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
	private void escreverArquivo(Message message, Distribuidor distribuidor, List<Estudo> listaEstudo) {
		
		try {
						
			
			if (listaEstudo == null || listaEstudo.isEmpty()) {
				this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Nenhum dado encontrado para geracao do arquivo.");
				throw new RuntimeException("Nenhum dado encontrado para geracao do arquivo.");
			}
			
			Calendar dataAtual = Calendar.getInstance();			
			
			File arquivo = new File(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue()) + "/LANCAMENTO.NEW");
			
			PrintWriter writer = 
				new PrintWriter(
					new FileWriter(arquivo));
				
			for (Estudo estudo : listaEstudo) {
				
				EMS0132Output output = new EMS0132Output();

				if (estudo != null) {

					Produto produto = estudo.getProdutoEdicao().getProduto();
					
					for (Fornecedor fornecedor : produto.getFornecedores()) {

						output.setCodigoDistribuidor( Long.valueOf( distribuidor.getCodigoDistribuidorDinap() ));						
						output.setDataGeracaoArquivo(dataAtual.getTime());
						output.setHotaGeracaoArquivo(dataAtual.getTime());
						output.setMnemonicoTabela(CODIGO_LANP);
						output.setCodigoContexto(produto.getCodigoContexto());
						output.setCodigoFornecedor(fornecedor.getCodigoInterface());
						output.setCodigoProduto( estudo.getProdutoEdicao().getProduto().getCodigo() );
						output.setNumeroEdicao(estudo.getProdutoEdicao().getNumeroEdicao());
						output.setNumeroLancamento( 0L );
						output.setNumeroFase(0);
						output.setDataLancamento(estudo.getDataLancamento());
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
