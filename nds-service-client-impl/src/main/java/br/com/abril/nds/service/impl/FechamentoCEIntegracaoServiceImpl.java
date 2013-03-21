package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.FechamentoCEIntegracaoVO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.StatusCeNDS;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;

@Service
public class FechamentoCEIntegracaoServiceImpl implements FechamentoCEIntegracaoService {
	
	@Autowired
	private FechamentoCEIntegracaoRepository fechamentoCEIntegracaoRepository;

	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private ChamadaEncalheFornecedorRepository chamadaEncalheFornecedorRepository;
	
	@Autowired
	private BoletoService boletoService;

	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;

	@Transactional
	public List<ItemFechamentoCEIntegracaoDTO> buscarFechamentoEncalhe(FiltroFechamentoCEIntegracaoDTO filtro) {
		return this.fechamentoCEIntegracaoRepository.buscarConferenciaEncalhe(filtro);
	}
	
	@Transactional
	public byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca) {
		
		Long numeroSemana = Long.parseLong(filtro.getSemana().substring(4));
		
		List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor = chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(null, numeroSemana.intValue(), null);
		
		if(listaChamadaEncalheFornecedor==null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao gerar boleto.");
			
		}
		
		List<BoletoDistribuidor> listaBoletoDistribuidor = gerarCobrancaService.gerarCobrancaBoletoDistribuidor(listaChamadaEncalheFornecedor, tipoCobranca, numeroSemana.intValue());
		
		try {
			
			return boletoService.gerarImpressaoBoletosDistribuidor(listaBoletoDistribuidor);
			
		} catch(Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao gerar cobrança em boleto para o Distribuidor: " + e.getMessage());
			
		}
		
	}
	
	@Override
	@Transactional
	public void fecharCE(String[] listaEncalhePronta, String[] listaIdProdutoEdicaoPronta, String idFornecedor, Integer numeroSemana) {
		Long encalhe = null;
		ProdutoEdicao produtoEdicao = null;
		
		for(int cont = 0; cont < listaEncalhePronta.length; cont++){
			encalhe = Long.parseLong(listaEncalhePronta[cont]);
			produtoEdicao = produtoEdicaoService.obterProdutoEdicao(Long.parseLong(listaIdProdutoEdicaoPronta[cont]), false);
		//	this.fechamentoCEIntegracaoRepository.fecharCE(encalhe, produtoEdicao, (idFornecedor.equals("-1") ? null : new Long(idFornecedor)),numeroSemana);
		}
	}

	@Override
	@Transactional
	public boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro) {		 
		return this.fechamentoCEIntegracaoRepository.verificarStatusSemana(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoDTO obterCEIntegracaoFornecedor(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		BigInteger qntItens = fechamentoCEIntegracaoRepository.countItensFechamentoCeIntegracao(filtro);
		
		if(qntItens.compareTo(BigInteger.ZERO) == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		FechamentoCEIntegracaoDTO fechamentoCEIntegracaoDTO = new FechamentoCEIntegracaoDTO();
	
		fechamentoCEIntegracaoDTO.setQntItensCE(qntItens.intValue());
		
		fechamentoCEIntegracaoDTO.setItensFechamentoCE( this.buscarFechamentoEncalhe(filtro));
		
		fechamentoCEIntegracaoDTO.setConsolidado(this.buscarFechamentoEncalheTotal(filtro));
		
		fechamentoCEIntegracaoDTO.setSemanaFechada(this.verificarStatusSemana(filtro));
		
		return fechamentoCEIntegracaoDTO;
	}

	@Override
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoConsolidadoDTO buscarFechamentoEncalheTotal(FiltroFechamentoCEIntegracaoDTO filtro) {
		return this.fechamentoCEIntegracaoRepository.buscarConferenciaEncalheTotal(filtro);
	}
	
	@Override
	@Transactional
	public void reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		ChamadaEncalheFornecedor encalheFornecedor = 
				chamadaEncalheFornecedorRepository.obterChamadaEncalheFornecedor(filtro.getIdFornecedor(), 
																				 filtro.getAnoCorrente().intValue(), 
																				 filtro.getNumeroSemana().intValue());
		if(encalheFornecedor!= null){
			encalheFornecedor.setStatusCeNDS(StatusCeNDS.ABERTO);
			chamadaEncalheFornecedorRepository.alterar(encalheFornecedor);
		}
	}
	
}
