package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
public class ImportacaoDeArquivoMatrizStrategy extends ImportacaoAbstractStrategy implements ImportacaoArquivoStrategy {
	
	@Autowired
	private FixedFormatManager ffm;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		
		return processarArquivo(arquivo);
	}
	
	@Override
	public void processarImportacaoDados(Object input) {
		
		processarDados(input);
	}
	
	/**
	 * 
	 * Retorna o objepto EMS0108Input com as informações referente a linha do arquivo informada
	 * 
	 * @param linhaArquivo
	 * 
	 * @return EMS0108Input
	 */
	@Override
	protected EMS0108Input parseDados(String linhaArquivo){
		
		try{
			
			return (EMS0108Input) this.ffm.load(EMS0108Input.class, linhaArquivo);
		}
		catch (ParseException e) {
			throw new ImportacaoException(MENSAGEM_ERRO_PARSE_DADOS);	
		}	
		catch (FixedFormatException ef) {
			
			throw new ImportacaoException(MENSAGEM_ERRO_FORMATO_DADOS);
		}
	}
	
	/**
	 * 
	 * Efetua o processamento dos dados do arquivo referente a lançamento.
	 * 
	 * @param input - input com os dados para processamento
	 */
	@Override
	protected void processarDados(Object inputDados) {
		
		EMS0108Input  input = (EMS0108Input) inputDados;
		
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
		
		if(input.getDataLancamentoRecolhimentoProduto().compareTo(dataCriacaoArquivo) >= 0){
			
			Lancamento lancamento = 
					lancamentoRepository.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(produtoEdicao,dataLcto, dataLcto);	
			
			if(lancamento == null){
				
				lancamento = new Lancamento();	
			}
			
			lancamento.setDataCriacao(new Date());
			lancamento.setDataLancamentoDistribuidor(dataLcto);
			lancamento.setDataLancamentoPrevista(dataLcto);
			lancamento.setDataRecolhimentoDistribuidor(dataRec);
			lancamento.setDataRecolhimentoPrevista(dataRec);
			lancamento.setDataStatus(new Date());
			lancamento.setNumeroReprogramacoes(0);
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setReparte(BigInteger.valueOf(0));
			
			lancamento.setStatus(StatusLancamento.PLANEJADO);
			lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
				
			lancamentoRepository.adicionar(lancamento);

		}
		
		if (input.getEdicaoRecolhimento() != null
				&& input.getDataLancamentoRecolhimentoProduto().before(dataCriacaoArquivo)) {
			
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
