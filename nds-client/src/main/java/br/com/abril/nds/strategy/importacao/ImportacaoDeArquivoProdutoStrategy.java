package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import javax.persistence.NoResultException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.integracao.model.canonic.EMS0119Input;
import br.com.abril.nds.integracao.service.PeriodicidadeProdutoService;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.repository.NCMRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * Estratégia de importação de arquivos referente a Produtos.
 * 
 * @author Discover Technology
 *
 */
@Component("importacaoDeArquivoProdutoStrategy")
public class ImportacaoDeArquivoProdutoStrategy implements ImportacaoArquivoStrategy {

	@Autowired
	private FixedFormatManager ffm;
	
	@Autowired
	private PeriodicidadeProdutoService periodicidadeProdutoService;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;
	
	@Autowired
	private EditorRepository editorRepository;
	
	@Autowired
	private NCMRepository ncmRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Override
	@Transactional
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {

		FileReader in = null;
		
		try {
			in = new FileReader(arquivo);
		} catch (FileNotFoundException ex) {
			throw new ImportacaoException(ex.getMessage());
		}
		
		Scanner scanner = new Scanner(in);
		int linhaArquivo = 0;

		while (scanner.hasNextLine()) {

			String linha = scanner.nextLine();
			linhaArquivo++;

			if (StringUtils.isEmpty(linha) || ((int) linha.charAt(0)  == 26) ) {
				continue;
			} 
			
			try{
				
				EMS0119Input input = this.parseDados(linha);
				
				processarDados(input);
			}
			catch(Exception e){
				return new RetornoImportacaoArquivoVO(new String[]{e.getMessage()},linhaArquivo,linha,false);
			}
		}
		
		try {
			in.close();
		} catch (IOException ex) {
			throw new ImportacaoException(ex.getMessage());
		}
		
		return new RetornoImportacaoArquivoVO(new String[]{"Sucesso"},linhaArquivo,null,true);
	}

	@Override
	public void processarImportacaoDados(Object input) {
		
		EMS0119Input  inputDados = (EMS0119Input) input;
		
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
	private EMS0119Input parseDados(String linhaArquivo){
		
		try{
			
			return (EMS0119Input) this.ffm.load(EMS0119Input.class, linhaArquivo);
			
		}catch (FixedFormatException ef) {
			
			throw new ImportacaoException("Formato das informações contidas na linha do arquivo inválida!");
		}
	}
    
	private void validaInput(EMS0119Input produtoRoute) {
        
		try {
			
			if(produtoRoute.getDesconto()==null){ 
				throw new ImportacaoException("Desconto é Nulo");  
			}
			
			if(produtoRoute.getNomeDaPublicacao()==null){ 
				throw new ImportacaoException("Nome é Nulo");
			}
			
			if(periodicidadeProdutoService.getPeriodicidadeProdutoAsArchive(produtoRoute.getPeriodicidade())==null){
            	throw new ImportacaoException("Periodicidade é Nulo"); 
			}
			
			if( produtoRoute.getTipoDePublicacao()==null){ 
        	   throw new ImportacaoException("Tipo do Produto é Nulo");
			}
			
			if(produtoRoute.getCodigoDoEditor()==null){
	        	throw new ImportacaoException("Código do Editor é Nulo");  	
	        }
			
			if(produtoRoute.getPacotePadrao()==null){ 
	        	throw new ImportacaoException("Pacote Padrão é Nulo");	
			}
			
			if(produtoRoute.getNomeComercial()==null){
	        	throw new ImportacaoException("Nome Comercial é Nulo");	
		    }
		} 
		
        catch (NoResultException e) {
			throw new ImportacaoException("Erro ao inserir: "+e.getMessage()); 
		}
	}
	
	private void processarDados(EMS0119Input input){		

        validaInput(input);
		
        try {

		    //NCM POR CODIGO
	  		NCM ncm = ncmRepository.obterPorCodigo(123l);
	  		if (ncm==null){
	  		    ncm = new NCM();
	  		    
	  		    ncm.setCodigo(123l);
	  			ncm.setDescricao("OUTROS");
	  			ncm.setUnidadeMedida("KG");
	  			
	  			ncmRepository.adicionar(ncm);
	  		}
	  		else{
	  			
	  			ncmRepository.alterar(ncm);
	  		}
		  		
		    //EDITOR POR CODIGO
	  		Editor editor = editorRepository.buscarPorId(input.getCodigoDoEditor());
	  		if (editor==null){
	  		    editor = new Editor();
		  		
	  		    PessoaJuridica pj = new PessoaJuridica();
		        pessoaRepository.adicionar(pj);
		  	    editor.setNome("Editor");
		  	    editor.setAtivo(true);
		  	    editor.setPessoaJuridica(pj);
		  	    
		  	    editor.setCodigo(input.getCodigoDoEditor());
		  	    
		  	    editorRepository.adicionar(editor);
	  		}
	  		else{
	  			
	  			editorRepository.alterar(editor);
	  		}
			
		    //TIPO PRODUTO POR ID
	  		TipoProduto tipoProduto = tipoProdutoRepository.obterPorCodigo(input.getTipoDePublicacao());
	  		if (tipoProduto==null){
	  			tipoProduto = new TipoProduto();
	  			
	  		    tipoProduto.setDescricao("TIPO DE PRODUTO");
	  		    tipoProduto.setGrupoProduto(GrupoProduto.OUTROS);
	  		    tipoProduto.setNcm(ncm);
	  		    
	  		    tipoProduto.setCodigo(input.getTipoDePublicacao());
	  		    
		  	    tipoProdutoRepository.adicionar(tipoProduto);
	  		}
	  		else{
	  			
	  			tipoProdutoRepository.alterar(tipoProduto);
	  		}
			
			//PRODUTO POR NOME
			Produto produto = produtoRepository.obterProdutoPorNomeProduto(input.getNomeComercial());
			if (produto==null){
				produto = new Produto();
				
			    produto.setPacotePadrao(0);
			    produto.setPeb(0);
			    produto.setPeso(BigDecimal.ZERO);
			    produto.setTipoProduto(tipoProduto);
		  	    produto.setEditor(editor);
			    
			    produto.setNome(input.getNomeDaPublicacao());
			    produto.setPeriodicidade(periodicidadeProdutoService.getPeriodicidadeProdutoAsArchive(input.getPeriodicidade()));
			   
		  	    produtoRepository.adicionar(produto);
			}
			else{
				
				produtoRepository.alterar(produto);
			}
			
		    //PRODUTO EDICAO POR CODIGO DO PRODUTO E NUMERO EDICAO
			ProdutoEdicao produtoEdicao =produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(produto.getCodigo(), input.getEdicao());
			if (produtoEdicao==null){
				produtoEdicao = new ProdutoEdicao();
				
				produtoEdicao.setNumeroEdicao(input.getEdicao());
			    produtoEdicao.setPeb(0);
			    produtoEdicao.setPeso(BigDecimal.ZERO);
			    produtoEdicao.setProduto(produto);
			    
			    produtoEdicao.setDesconto(input.getDesconto());
			    produtoEdicao.setPacotePadrao(input.getPacotePadrao());
			    produtoEdicao.setNomeComercial(input.getNomeComercial());
			    produtoEdicao.setAtivo(input.getStatusDaPublicacao());
			    
				produtoEdicaoRepository.adicionar(produtoEdicao);
			}
			else{
				
				produtoEdicaoRepository.alterar(produtoEdicao);
			}
			
		} catch (Exception e) {
			throw new ImportacaoException("Erro ao inserir: "+e.getMessage()); 
		}

	}

}
