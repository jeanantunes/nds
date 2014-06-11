package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.FiltroValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO.PublicacaoCuponada;
import br.com.abril.nds.model.cadastro.ValeDesconto;

public interface ValeDescontoRepository extends Repository<ValeDesconto, Long> {

	ValeDescontoVO obterPorId(Long id);
	
	List<ValeDescontoVO> obterPorFiltro(FiltroValeDescontoVO filtroValeDesconto);
	
	Long obterCountPesquisaPorFiltro(FiltroValeDescontoVO filtroValeDesconto);
	
	ValeDescontoVO obterPorCodigo(String codigo);
	
	ValeDescontoVO obterUltimaEdicao(String codigo);
	
	List<PublicacaoCuponada> obterPublicacoesCuponadas(Long idValeDesconto);
	
	List<ValeDesconto> obterPorNome(String nome);
	
	List<ValeDescontoVO> obterPorCodigoOuNome(String filtro);
	
	PublicacaoCuponada obterCuponadasPorCodigoEdicao(String codigo, Long numeroEdicao);
	
	List<PublicacaoCuponada> obterCuponadasPorCodigoOuNome(String filtro);
}
