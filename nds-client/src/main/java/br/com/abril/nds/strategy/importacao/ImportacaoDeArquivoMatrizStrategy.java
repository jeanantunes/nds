package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.integracao.ems0108.inbound.EMS0108Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.util.DateUtil;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.ParseException;

/**
 * Estratégia de importação de arquivos referente a Matriz de recolhimento e lançamento.
 * 
 * @author Discover Technology
 *
 */
@Component("importacaoDeArquivoMatrizStrategy")
public class ImportacaoDeArquivoMatrizStrategy implements ImportacaoArquivoStrategy {
	
	private static final Logger logger = Logger.getLogger(ImportacaoDeArquivoMatrizStrategy.class);
	
	@Autowired
	private FixedFormatManager ffm;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		
		FileReader in = null;
		try {
			in = new FileReader(arquivo);
		} catch (FileNotFoundException ex) {
			logger.fatal("Erro na leitura de arquivo", ex);
			throw new ImportacaoException(ex.getMessage());
		}
		
		Scanner scanner = new Scanner(in);
		int linhaArquivo = 0;

		while (scanner.hasNextLine()) {

			String linha = scanner.nextLine();
			linhaArquivo++;
			
			// Ignora linha vazia e aquele caracter estranho em formato de seta para direita
			if (StringUtils.isEmpty(linha) ||  ((int) linha.charAt(0)  == 26) ) {
				continue;
			} 
			
			try {
				
				EMS0108Input  input = parseDados(linha);
				
				processarDados(input);
				
			} catch (ImportacaoException e) {
				
				RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO(new String[]{e.getMessage()},linhaArquivo,linha,false);
				logger.error(retorno.toString());
				return retorno; 
			}
		}
		
		try {
			in.close();
		} catch (IOException e) {
			logger.fatal("Erro na leitura de arquivo", e);
			throw new ImportacaoException(e.getMessage());
		}
		
		return new RetornoImportacaoArquivoVO(true) ;
	}
	
	@Override
	public void processarImportacaoDados(Object input) {
		
		EMS0108Input  inputDados = (EMS0108Input) input;
		
		processarDados(inputDados);
	}
	
	/**
	 * 
	 * Retorna o objepto EMS0108Input com as informações referente a linha do arquivo informada
	 * 
	 * @param linhaArquivo
	 * 
	 * @return EMS0108Input
	 */
	private EMS0108Input parseDados(String linhaArquivo){
		
		try{
			
			return (EMS0108Input) this.ffm.load(EMS0108Input.class, linhaArquivo);
		}
		catch (ParseException e) {
			throw new ImportacaoException("Parse das informações contidas na linha do arquivo inválida!");	
		}	
		catch (FixedFormatException ef) {
			
			throw new ImportacaoException("Formato das informações contidas na linha do arquivo inválida!");
		}
	}
	
	/**
	 * 
	 * Efetua o processamento dos dados do arquivo referente a lançamento.
	 * 
	 * @param input - input com os dados para processamento
	 */
	private void processarDados(EMS0108Input input){
		
		ProdutoEdicao produtoEdicao = 
				produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(input.getCodigoPublicacao().toString(), 
																				 input.getEdicao());
		if(produtoEdicao == null){
			throw new ImportacaoException("Produto Edição não encontrado para importação.");
		}
		
		int numeroDias = produtoEdicao.getPeb();
		
		Date dataLcto = input.getDataLancamentoRecolhimentoProduto();
	
		// Define a data de recolhimento do produto 
		Date dataRec = DateUtil.adicionarDias(dataLcto, numeroDias);			
		
		if(input.getDataLancamentoRecolhimentoProduto().compareTo(new Date()) >= 0){
			
			Lancamento lancamento = new Lancamento();		
			
			lancamento.setDataCriacao(new Date());
			lancamento.setDataLancamentoDistribuidor(dataLcto);
			lancamento.setDataLancamentoPrevista(dataLcto);
			lancamento.setDataRecolhimentoDistribuidor(dataRec);
			lancamento.setDataRecolhimentoPrevista(dataRec);
			lancamento.setDataStatus(new Date());
			lancamento.setNumeroReprogramacoes(0);
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setReparte(new BigDecimal(0));
			
			lancamento.setStatus(StatusLancamento.PLANEJADO);
			lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
			
			lancamentoRepository.adicionar(lancamento);
		}
		
		if (input.getEdicaoRecolhimento() != null
				&& input.getDataLancamentoRecolhimentoProduto().before(new Date())) {
			
			Date dataLancamentoPrevista = null;
			Date dataRecolhimento = null;
		
			if(input.getEdicao()!= null){
				dataLancamentoPrevista = input.getDataLancamentoRecolhimentoProduto();
			}
			
			if(input.getDataLancamentoRecolhimentoProduto()!= null){
				dataRecolhimento = input.getDataLancamentoRecolhimentoProduto();
			}
			
			Lancamento lancamento = 
					lancamentoRepository.obterLancamentoProdutoPorDataLancamentoOuDataRecolhimento(produtoEdicao, 
																									dataLancamentoPrevista, 
																									dataRecolhimento);
			if(lancamento == null){
				throw new ImportacaoException("Lançamento não encontrado para importação.");
			}
			
			lancamento.setDataLancamentoDistribuidor(dataLcto);
			lancamento.setDataLancamentoPrevista(dataLcto);
			lancamento.setDataRecolhimentoDistribuidor(dataRec);
			lancamento.setDataRecolhimentoPrevista(dataRec);
			lancamento.setDataStatus(new Date());
			lancamento.setNumeroReprogramacoes(0);
			lancamento.setProdutoEdicao(produtoEdicao);
			
			lancamentoRepository.merge(lancamento);
		}
	}
}
