package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0108.inbound.EMS0108Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.util.DateUtil;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * Estratégia de importação de arquivos referente a Matriz de recolhimento e lançamento.
 * 
 * @author Discover Technology
 *
 */
@Component("importacaoDeArquivoMatrizStrategy")
public class ImportacaoDeArquivoMatrizStrategy implements ImportacaoArquivoStrategy {
	
	@Autowired
	private FixedFormatManager ffm;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
				
		// FIXME implementar a importação de arquivo referente a Matriz
		
		RetornoImportacaoArquivoVO arquivoVO = new RetornoImportacaoArquivoVO();
		arquivoVO.setSucessoNaImportacao(true);
		
		FileReader in = null;
		try {
			in = new FileReader(arquivo);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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

			// TODO: verificar tamanho correto das linhas nos arquivos: difere da definição
//			if (linha.length() != interfaceEnum.getTamanhoLinha().intValue()) {
//				throw new ValidacaoException(TAMANHO_LINHA);
//			}
			
			EMS0108Input  doc = (EMS0108Input) this.ffm.load(EMS0108Input.class, linha);
			
			System.out.println(doc.toString());
			
			//FIXME gravar dados  no banco
		}
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return arquivoVO ;
	}
	
	private void persistirDados(EMS0108Input input, RetornoImportacaoArquivoVO retornoImportacaoArquivoVO){
		
		ProdutoEdicao produtoEdicao = 
				produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(input.getCodigoPublicacao().toString(), 
																				 input.getEdicao());
		if(produtoEdicao == null){
			//TODO erro de produto edição não encontrado
		}
		
		int numeroDias = produtoEdicao.getPeb();
		
		Date dataLcto = input.getDataLancamentoRecolhimentoProduto();
	
		// Soma o número de dias a recolher
		Date dataRec = DateUtil.adicionarDias(dataLcto, numeroDias);			
		
		if(input.getDataLancamentoRecolhimentoProduto().compareTo(new Date()) >= 0){
			
			Lancamento lancamento = new Lancamento();		
			// Insert
			lancamento.setDataCriacao(new Date());
			lancamento.setDataLancamentoDistribuidor(dataLcto);
			lancamento.setDataLancamentoPrevista(dataLcto);
			lancamento.setDataRecolhimentoDistribuidor(dataRec);
			lancamento.setDataRecolhimentoPrevista(dataRec);
			lancamento.setDataStatus(new Date());
			lancamento.setNumeroReprogramacoes(0);
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setReparte(new BigDecimal(0));
			
			lancamentoRepository.adicionar(lancamento);
		}
		
		if (input.getEdicaoRecolhimento() != null
				&& input.getDataLancamentoRecolhimentoProduto().before(new Date())) {
			
			Lancamento lancamento = 
					lancamentoRepository.obterLancamentoProdutoPorDataLancamentoOuDataRecolhimento(produtoEdicao.getProduto().getCodigo(), 
																								   dataRec, 
																								   dataRec);
			if(lancamento == null){
				//TODO erro de lançamento não encontrado
			}
			
			// update
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
