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
	public void processarImportacaoDados(Object input){}
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		
		RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO();

		FileReader in = null;
		
		try {
			in = new FileReader(arquivo);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			retorno = new RetornoImportacaoArquivoVO(new String[]{e1.getMessage()},0,null,false);
		}
		
		Scanner scanner = new Scanner(in);
		int linhaArquivo = 0;

		while (scanner.hasNextLine()) {

			String linha = scanner.nextLine();
			linhaArquivo++;

			if (StringUtils.isEmpty(linha) || ((int) linha.charAt(0)  == 26) ) {
				continue;
			} 

			EMS0119Input  produtoRoute = (EMS0119Input) this.ffm.load(EMS0119Input.class, linha);
			
			try{
			    this.insertProduto(produtoRoute);
			    retorno = new RetornoImportacaoArquivoVO(new String[]{"Sucesso"}, linhaArquivo, linha, true);
			}
			catch(Exception e){
				retorno = new RetornoImportacaoArquivoVO(new String[]{e.getMessage()},linhaArquivo,linha,false);
			    break;
			}
		}
		
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			retorno = new RetornoImportacaoArquivoVO(new String[]{e.getMessage()},linhaArquivo,null,false);
		}
		
		return retorno ;
	}

    
	private void validaInput(EMS0119Input produtoRoute) throws Exception{
        
		try {
			
			if(produtoRoute.getDesconto()==null){ 
				throw new Exception("Desconto é Nulo");  
			}
			
			if(produtoRoute.getNomeDaPublicacao()==null){ 
				throw new Exception("Nome é Nulo");
			}
			
			if(periodicidadeProdutoService.getPeriodicidadeProdutoAsArchive(produtoRoute.getPeriodicidade())==null){
            	throw new Exception("Periodicidade é Nulo"); 
			}
			
			if( produtoRoute.getTipoDePublicacao()==null){ 
        	   throw new Exception("Tipo do Produto é Nulo");
			}
			
			if(produtoRoute.getCodigoDoEditor()==null){
	        	throw new Exception("Código do Editor é Nulo");  	
	        }
			
			if(produtoRoute.getPacotePadrao()==null){ 
	        	throw new Exception("Pacote Padrão é Nulo");	
			}
			
			if(produtoRoute.getNomeComercial()==null){
	        	throw new Exception("Nome Comercial é Nulo");	
		    }
		} 
		
        catch (NoResultException e) {
			throw new Exception("Erro ao inserir: "+e.getMessage()); 
		}
	}
	
	
	private void insertProduto(EMS0119Input produtoRoute) throws Exception {		

        validaInput(produtoRoute);
		
	    //NCM POR CODIGO
  		NCM ncm = ncmRepository.obterPorCodigo(123l);
  		if (ncm==null){
  		    ncm = new NCM();
  		    
  		    ncm.setCodigo(123l);
  			ncm.setDescricao("OUTROS");
  			ncm.setUnidadeMedida("KG");
  		}
	  		
	   //EDITOR POR CODIGO
  		Editor editor = editorRepository.buscarPorId(produtoRoute.getCodigoDoEditor());
  		if (editor==null){
  		    editor = new Editor();
	  		
  		    PessoaJuridica pj = new PessoaJuridica();
	        pessoaRepository.adicionar(pj);
	  	    editor.setNome("Editor");
	  	    editor.setAtivo(true);
	  	    editor.setPessoaJuridica(pj);
	  	    
	  	    editor.setCodigo(produtoRoute.getCodigoDoEditor());
  		}
		
	    //TIPO PRODUTO POR ID
  		TipoProduto tipoProduto = tipoProdutoRepository.obterPorCodigo(produtoRoute.getTipoDePublicacao());
  		if (tipoProduto==null){
  			tipoProduto = new TipoProduto();
  			
  		    tipoProduto.setDescricao("TIPO DE PRODUTO");
  		    tipoProduto.setGrupoProduto(GrupoProduto.OUTROS);
  		    
  		    tipoProduto.setCodigo(produtoRoute.getTipoDePublicacao());
  		}
		
		//PRODUTO POR NOME
		Produto produto = produtoRepository.obterProdutoPorNomeProduto(produtoRoute.getNomeComercial());
		if (produto==null){
			produto = new Produto();
			
		    produto.setPacotePadrao(0);
		    produto.setPeb(0);
		    produto.setPeso(BigDecimal.ZERO);
		    
		    produto.setNome(produtoRoute.getNomeDaPublicacao());
		    produto.setPeriodicidade(periodicidadeProdutoService.getPeriodicidadeProdutoAsArchive(produtoRoute.getPeriodicidade()));
		}
		
	    //PRODUTO EDICAO POR NOME
		//numero_edição, produto_id		
		ProdutoEdicao produtoEdicao = null; //per.obterProdutoEdicaoPorNomeProduto(produtoRoute.getNomeComercial());
		if (produtoEdicao==null){
			produtoEdicao = new ProdutoEdicao();
			
			produtoEdicao.setNumeroEdicao(produtoRoute.getEdicao());
		    produtoEdicao.setPeb(0);
		    produtoEdicao.setPeso(BigDecimal.ZERO);
		    
		    produtoEdicao.setDesconto(produtoRoute.getDesconto());
		    produtoEdicao.setPacotePadrao(produtoRoute.getPacotePadrao());
		    produtoEdicao.setNomeComercial(produtoRoute.getNomeComercial());
		    produtoEdicao.setAtivo(produtoRoute.getStatusDaPublicacao());
		}

		try {
			
			ncmRepository.adicionar(ncm);
	  	    editorRepository.adicionar(editor);
	  	    
	  	    tipoProduto.setNcm(ncm);
	  	    tipoProdutoRepository.adicionar(tipoProduto);
	  	    
	  	    produto.setTipoProduto(tipoProduto);
	  	    produto.setEditor(editor);
	  	    produtoRepository.adicionar(produto);
	  	  
	  	    produtoEdicao.setProduto(produto);
			produtoEdicaoRepository.adicionar(produtoEdicao);

			
		} catch (NoResultException e) {
			throw new Exception("Erro ao inserir: "+e.getMessage()); 
		}

	}

}
