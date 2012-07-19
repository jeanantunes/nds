package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.integracao.model.canonic.EMS0119Input;
import br.com.abril.nds.integracao.service.PeriodicidadeProdutoService;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.NCMRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoFornecedorRepository;
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
	private TipoFornecedorRepository tipoFornecedorRepository;
	
	@Autowired
	private EditorRepository editorRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private NCMRepository ncmRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	/**
	 * Executa processo de importação de produtos
	 */
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

	/**
	 * Cria instancia do input referente à importação de produtos e processa informações
	 * @param Object:input
	 */
	@Override
	public void processarImportacaoDados(Object input) {
		
		EMS0119Input  inputDados = (EMS0119Input) input;
		
		processarDados(inputDados);
	}
	
	/**
	 * Retorna o objepto EMS0108Input com as informações referente a linha do arquivo informada
	 * @param linhaArquivo
	 * @return EMS0108Input
	 */
	private EMS0119Input parseDados(String linhaArquivo){
		
		try{
			
			return (EMS0119Input) this.ffm.load(EMS0119Input.class, linhaArquivo);
			
		}catch (FixedFormatException ef) {
			
			throw new ImportacaoException("Formato das informações contidas na linha do arquivo inválida!");
		}
	}
    
	/**
	 * Valida informações de input
	 * @param EMS0119Input:produtoRoute
	 */
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
	
	/**
	 * Processa informações de input e insere/altera de acordo com as regras de cadastro de produto/produto edição
	 * @param EMS0119Input:input
	 */
	private void processarDados(EMS0119Input input){		

        validaInput(input);
		
        try {

	  		NCM ncm = ncmRepository.obterPorCodigo(49059900l);
	  		if (ncm==null){
	  		    ncm = new NCM();
	  		    
	  		    ncm.setCodigo(49059900l);
	  			ncm.setDescricao("OUTRAS OBRAS CARTOGRAFICAS IMPRESSAS");
	  			ncm.setUnidadeMedida("KG");
	  			
	  			ncmRepository.adicionar(ncm);
	  		}
		  		
	  		Editor editor = editorRepository.obterPorCodigo(input.getCodigoDoEditor());
	  		if (editor==null){
	  		    editor = new Editor();
		  		
	  		    PessoaJuridica pj = new PessoaJuridica();
	  		    pj.setNomeFantasia("Editor");
	  		    pj.setRazaoSocial("Editor");
		        pessoaRepository.adicionar(pj);
		  	    editor.setNome("Editor");
		  	    editor.setAtivo(true);
		  	    editor.setOrigemInterface(false);
		  	    editor.setPessoaJuridica(pj);
		  	    
		  	    editor.setCodigo(input.getCodigoDoEditor());
		  	    
		  	    editorRepository.adicionar(editor);
	  		}
			
	  		TipoProduto tipoProduto = tipoProdutoRepository.obterPorCodigo(input.getTipoDePublicacao());
	  		if (tipoProduto==null){
	  			tipoProduto = new TipoProduto();
	  			
	  		    tipoProduto.setDescricao("IMPORTAÇÃO");
	  		    tipoProduto.setGrupoProduto(GrupoProduto.OUTROS);
	  		    tipoProduto.setNcm(ncm);
	  		    
	  		    tipoProduto.setCodigo(input.getTipoDePublicacao());
	  		    
		  	    tipoProdutoRepository.adicionar(tipoProduto);
	  		}
	  		
	  		Fornecedor fornecedor = fornecedorRepository.obterFornecedorPorCodigo(Integer.parseInt(input.getCodigoFornecedorPublic()));
	  		if (fornecedor==null){
	  			
	  			fornecedor = new Fornecedor();
	  			
	  			TipoFornecedor tipoFornecedor = new TipoFornecedor();
	  			tipoFornecedor.setDescricao("Tipo de Fornecedor");
	  			tipoFornecedor.setGrupoFornecedor(GrupoFornecedor.OUTROS);
	  			tipoFornecedorRepository.adicionar(tipoFornecedor);
	  			
	  		    PessoaJuridica pj = new PessoaJuridica();
	  		    pj.setNomeFantasia("Fornecedor");
	  		    pj.setRazaoSocial("Fornecedor");
		        pessoaRepository.adicionar(pj);
		        
		        fornecedor.setInicioAtividade(Calendar.getInstance().getTime());
		        fornecedor.setOrigem(Origem.MANUAL);
		        fornecedor.setPermiteBalanceamento(true);
		        fornecedor.setSituacaoCadastro(SituacaoCadastro.ATIVO);
		        fornecedor.setTipoFornecedor(tipoFornecedor);
		        fornecedor.setJuridica(pj);
		  	    
		        fornecedor.setCodigoInterface(Integer.parseInt(input.getCodigoFornecedorPublic()));
		  	    
		        fornecedorRepository.adicionar(fornecedor);
	  		}
			
			Produto produto = produtoRepository.obterProdutoPorNomeProdutoOuCodigo(input.getNomeComercial(),input.getCodigoDaPublicacao());
			if (produto==null){
				produto = new Produto();
				
			    produto.setPeb(0);
			    produto.setPeso(BigDecimal.ZERO);
			    produto.setOrigem(Origem.MANUAL);
			    produto.setTipoProduto(tipoProduto);
		  	    produto.setEditor(editor);
			    
		  	    Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		  	    fornecedores.add(fornecedor);
		  	    produto.setFornecedores(fornecedores);
			    input.getCodigoFornecedorPublic();
		  	    
		  	    produto.setCodigo(input.getCodigoDaPublicacao());
		  	    produto.setPacotePadrao(input.getPacotePadrao());
			    produto.setNome(input.getNomeDaPublicacao());
			    produto.setDescricao(input.getNomeDaPublicacao());
			    produto.setPeriodicidade(periodicidadeProdutoService.getPeriodicidadeProdutoAsArchive(input.getPeriodicidade()));
 
		  	    produtoRepository.adicionar(produto);
			}
			else{
				
			    produto.setTipoProduto(tipoProduto);
		  	    produto.setEditor(editor);
			    
		  	    produto.setCodigo(input.getCodigoDaPublicacao());
		  	    produto.setPacotePadrao(input.getPacotePadrao());
			    produto.setNome(input.getNomeComercial());
			    produto.setPeriodicidade(periodicidadeProdutoService.getPeriodicidadeProdutoAsArchive(input.getPeriodicidade()));

				produtoRepository.alterar(produto);
			}
			
			ProdutoEdicao produtoEdicao =produtoEdicaoRepository.obterProdutoEdicaoPorProdutoEEdicaoOuNome(produto, input.getEdicao(),input.getNomeDaPublicacao());
			if (produtoEdicao==null){
				produtoEdicao = new ProdutoEdicao();
				
			    produtoEdicao.setPeb(0);
			    produtoEdicao.setPeso(BigDecimal.ZERO);
			    produtoEdicao.setOrigemInterface(false);
			    produtoEdicao.setProduto(produto);
			    
			    produtoEdicao.setNumeroEdicao(input.getEdicao());
			    produtoEdicao.setCodigo(input.getCodigoDaPublicacao());
			    produtoEdicao.setDesconto(input.getDesconto());
			    produtoEdicao.setPacotePadrao(input.getPacotePadrao());
			    produtoEdicao.setNomeComercial(input.getNomeComercial());
			    produtoEdicao.setAtivo(input.getStatusDaPublicacao());
			    
				produtoEdicaoRepository.adicionar(produtoEdicao);
			}
			else{
				
			    produtoEdicao.setProduto(produto); 
			    
			    produtoEdicao.setNumeroEdicao(input.getEdicao());
			    produtoEdicao.setCodigo(input.getCodigoDaPublicacao());
			    produtoEdicao.setDesconto(input.getDesconto());
			    produtoEdicao.setPacotePadrao(input.getPacotePadrao());
			    produtoEdicao.setNomeComercial(input.getNomeDaPublicacao());
			    produtoEdicao.setAtivo(input.getStatusDaPublicacao());
				
				produtoEdicaoRepository.alterar(produtoEdicao);
			}
			
		} catch (Exception e) {
			throw new ImportacaoException("Erro ao inserir: "+e.getMessage()); 
		}

	}

}
