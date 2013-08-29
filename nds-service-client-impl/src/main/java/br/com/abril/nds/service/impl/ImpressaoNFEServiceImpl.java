package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ImpressaoNFeRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ImpressaoNFEService;

/**
 * @author InfoA2
 */
@Service
public class ImpressaoNFEServiceImpl implements ImpressaoNFEService {

	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private ImpressaoNFeRepository impressaoNFeRepository;

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ImpressaoNFEService#obterProdutosExpedicaoConfirmada(java.util.List)
	 */
	@Transactional
	public List<ProdutoDTO> obterProdutosExpedicaoConfirmada(FiltroImpressaoNFEDTO filtro) {
		
		//Filtra os produtos pelos fornecedores do distribuidor
		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(SituacaoCadastro.ATIVO);
		List<Long> idsFornecedores = new ArrayList<Long>();
		for(Fornecedor f : fornecedores) {
			idsFornecedores.add(f.getId());
		}
		filtro.setIdsFornecedores(idsFornecedores);
		
		List<ProdutoDTO> produtosDTO = new ArrayList<ProdutoDTO>();
		List<Produto> produtos = impressaoNFeRepository.buscarProdutosParaImpressaoNFe(filtro);
		for(Produto p : produtos) {
			ProdutoDTO prod = new ProdutoDTO();
			prod.setCodigoProduto(p.getCodigo());
			prod.setNomeProduto(p.getNome());
			produtosDTO.add(prod);
		}
		
		return produtosDTO;
	}

	@Transactional
	public List<NotasCotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {

		List<NotasCotasImpressaoNfeDTO> cotas = null;
		if(this.distribuidorRepository.obrigacaoFiscal() == null) {
			cotas = impressaoNFeRepository.buscarCotasParaImpressaoNotaEnvio(filtro);
		} else {
			cotas = impressaoNFeRepository.buscarCotasParaImpressaoNFe(filtro);
		}
				
		return cotas;
	}

	@Transactional
	public Integer buscarNFeParaImpressaoTotalQtd(FiltroImpressaoNFEDTO filtro) {
		
		if(this.distribuidorRepository.obrigacaoFiscal() == null) {
			return impressaoNFeRepository.buscarCotasParaImpressaoNotaEnvioQtd(filtro);
		} else {
			return impressaoNFeRepository.buscarCotasParaImpressaoNFeQtd(filtro);
		}
		
	}

	@Transactional
	public List<NotaFiscal> buscarNotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {

		return impressaoNFeRepository.buscarNotasParaImpressaoNFe(filtro);
		
	}

	@Transactional
	public List<NotaEnvio> buscarNotasEnvioParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {
		return impressaoNFeRepository.buscarNotasEnvioParaImpressaoNFe(filtro);
	}


}
