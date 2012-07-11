package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.model.canonic.EMS0119Input;
import br.com.abril.nds.integracao.service.PeriodicidadeProdutoService;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

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
	private PeriodicidadeProdutoService pps;
	
	@Autowired
	private ProdutoEdicaoRepository per;
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		
		RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO();

		FileReader in = null;
		
		try {
			in = new FileReader(arquivo);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
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
		}
		
		return retorno ;
	}

	
	
	
	
	
    
	private void insertProduto(EMS0119Input produtoRoute) throws Exception {
		
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		
		try {
			
			
			if(produtoRoute.getDesconto()!=null){ 
				produtoEdicao.setDesconto(produtoRoute.getDesconto());
				throw new Exception("Desconto é Nulo");  
			}
			
			if(produtoRoute.getNomeDaPublicacao()!=null){ 
				produtoEdicao.getProduto().setNome(produtoRoute.getNomeDaPublicacao());
				throw new Exception("Nome é Nulo");  
			}
			
			if(pps.getPeriodicidadeProdutoAsArchive(produtoRoute.getPeriodicidade())!=null){
				produtoEdicao.getProduto().setPeriodicidade(pps.getPeriodicidadeProdutoAsArchive(produtoRoute.getPeriodicidade()));
				throw new Exception("Periodicidade é Nulo"); 
			}
			
			if(produtoEdicao.getProduto().getTipoProduto().getId() != produtoRoute.getTipoDePublicacao()){ 
				produtoEdicao.getProduto().getTipoProduto().setId(produtoRoute.getTipoDePublicacao());
				throw new Exception("Tipo do Produto é Nulo");  
			}
			
			if(produtoEdicao.getProduto().getEditor().getCodigo() != produtoRoute.getCodigoDoEditor()){
				produtoEdicao.getProduto().getEditor().setCodigo(produtoRoute.getCodigoDoEditor());
				throw new Exception("Código do Editor é Nulo");  
			}
			
			if(produtoEdicao.getPacotePadrao() != produtoRoute.getPacotePadrao()){ 
				produtoEdicao.setPacotePadrao(produtoRoute.getPacotePadrao());
				throw new Exception("Pacote Padrão é Nulo"); 
			}
			
			if(produtoEdicao.getNomeComercial() !=  produtoRoute.getNomeComercial()){
				produtoEdicao.setNomeComercial(produtoRoute.getNomeComercial());
				throw new Exception("Nome Comercial é Nulo");  
			}
			
			if(produtoEdicao.isAtivo() != produtoRoute.getStatusDaPublicacao()){ 
				produtoEdicao.setAtivo(produtoRoute.getStatusDaPublicacao());
				throw new Exception("Ativo é Nulo"); 
			}
			
			
			per.merge(produtoEdicao);
			
			
		} catch (NoResultException e) {
			throw new Exception("Erro ao inserir: "+e.getMessage()); 
		}

	}

	
	
	public static void main(String args[]) throws FileNotFoundException{
		
		RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO();
		
		ImportacaoDeArquivoProdutoStrategy t = new ImportacaoDeArquivoProdutoStrategy();
		
		File file = new File("C:/PRODUTO.NEW");
           
		/*
		//EXIBIR PARA TESTE
		FileReader in = null;
        in = new FileReader(file);
        Scanner scanner = new Scanner(in);
		int linhaArquivo = 0;
		while (scanner.hasNextLine()) {
			String linha = scanner.nextLine();
			System.out.println(linha);
		}	
		//-----
		*/
		
		try{
		    retorno = t.processarImportacaoArquivo(file);
		}
		catch(Exception e) {
			System.out.println(retorno.toString());
		}
	}
	
}
