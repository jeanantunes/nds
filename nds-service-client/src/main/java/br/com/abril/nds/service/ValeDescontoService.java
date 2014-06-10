package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.FiltroValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO.PublicacaoCuponada;
import br.com.abril.nds.model.cadastro.ValeDesconto;

public interface ValeDescontoService {

	void salvar(ValeDesconto valeDesconto);
	
	void remover(Long id);
	
	ValeDescontoVO obterPorId(Long id);
	
	ValeDescontoVO sugerirProximaEdicao(String codigo);
	
	List<ValeDescontoVO> obterPorFiltro(FiltroValeDescontoVO filtroValeDesconto);
	
	Long obterCountPesquisaPorFiltro(FiltroValeDescontoVO filtroValeDesconto);
	
	ValeDescontoVO obterPorCodigo(String codigo);
	
	List<ValeDesconto> obterPorNome(String nome);
	
	List<ValeDescontoVO> obterPorCodigoOuNome(String filtro);
	
	List<ValeDesconto> obterTodos();
	
	PublicacaoCuponada obterCuponadasPorCodigoEdicao(String codigo, Long numeroEdicao);
	
	List<PublicacaoCuponada> obterCuponadasPorCodigoOuNome(String filtro);
}
