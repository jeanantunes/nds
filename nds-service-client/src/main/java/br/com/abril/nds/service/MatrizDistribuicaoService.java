package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.dto.filtro.FiltroDistribuicaoDTO;

public interface MatrizDistribuicaoService {
	

	public TotalizadorProdutoDistribuicaoVO obterMatrizDistribuicao(FiltroDistribuicaoDTO filtro);
	
	public void reabrirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao);
	
	public void excluirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao);
	
	public Long confirmarCopiarProporcionalDeEstudo(CopiaProporcionalDeDistribuicaoVO vo);
	
	public void finalizarMatrizDistribuicao(FiltroDistribuicaoDTO FiltroDistribuicaoDTO, List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs);
	
	public void reabrirMatrizDistribuicao(List<ProdutoDistribuicaoVO> produtosDistribuicao); 
	
	public void reabrirMatrizDistribuicaoTodosItens(FiltroDistribuicaoDTO filtro); 
	
	public void duplicarLinhas(ProdutoDistribuicaoVO prodDistribVO);

	public void finalizarMatrizDistribuicaoTodosItens(FiltroDistribuicaoDTO filtro, List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs);

    public ProdutoDistribuicaoVO obterMatrizDistribuicaoPorEstudo(BigInteger id);

    public void removeEstudo(Long idEstudo);
    
    public void atualizarPercentualAbrangencia(Long estudoId);

    }
