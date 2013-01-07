package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.FechamentoCEIntegracaoVO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public interface FechamentoCEIntegracaoService {
	
	List<FechamentoCEIntegracaoDTO> buscarFechamentoEncalhe(FiltroFechamentoCEIntegracaoDTO filtro);
	
	//List<FechamentoCEIntegracaoDTO> calcularVenda(List<FechamentoCEIntegracaoDTO> listaFechamento);
	
	void fecharCE(Long encalhe, ProdutoEdicao produtoEdicao);

	boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro);
	
	byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca);

	FechamentoCEIntegracaoVO construirFechamentoCEIntegracaoVO(FiltroFechamentoCEIntegracaoDTO filtro);

}
