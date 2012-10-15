package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.MatrizLancamentoService;

/**
 * @author InfoA2
 */
@Service
public class ImpressaoNFEServiceImpl implements ImpressaoNFEService {

	@Autowired
	private MatrizLancamentoService matrizLancamentoService;

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ImpressaoNFEService#obterProdutosExpedicaoConfirmada(java.util.List)
	 */
	@Override
	public List<ProdutoLancamentoDTO> obterProdutosExpedicaoConfirmada(List<Fornecedor> fornecedores, Date data) {
		List<Long> idsFornecedores = new ArrayList<Long>();
		for (Fornecedor fornecedor : fornecedores) {
			idsFornecedores.add(fornecedor.getId());
		}

		FiltroLancamentoDTO filtroLancamento = new FiltroLancamentoDTO(new Date(), idsFornecedores);
		
		// Retorna uma lista de produtos da data apontada no service
		return matrizLancamentoService.obterMatrizLancamento(filtroLancamento, false).getMatrizLancamento().get(data);
	}

}
