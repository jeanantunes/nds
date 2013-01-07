package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.repository.BoletoDistribuidorRepository;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class FechamentoCEIntegracaoServiceImpl implements FechamentoCEIntegracaoService {
	
	@Autowired
	private FechamentoCEIntegracaoRepository fechamentoCEIntegracaoRepository;

	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private BoletoDistribuidorRepository boletoDistribuidorRepository;
	
	@Autowired
	private ChamadaEncalheFornecedorRepository chamadaEncalheFornecedorRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Transactional
	public List<FechamentoCEIntegracaoDTO> buscarFechamentoEncalhe(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		return this.fechamentoCEIntegracaoRepository.buscarConferenciaEncalhe(filtro);
	}
	
	@Override
	public List<FechamentoCEIntegracaoDTO> calcularVenda(List<FechamentoCEIntegracaoDTO> listaFechamento) {
		List<FechamentoCEIntegracaoDTO> lista = new ArrayList<FechamentoCEIntegracaoDTO>();
		int sequencial = 1;
		for(FechamentoCEIntegracaoDTO dto: listaFechamento){
			dto.setVenda(dto.getReparte().subtract(dto.getEncalhe()));
			double valorDaVenda = dto.getVenda().doubleValue() * dto.getPrecoCapa().doubleValue();
			dto.setvalorVendaFormatado(CurrencyUtil.formatarValor(valorDaVenda));
			dto.setSequencial(sequencial);
			sequencial++;
			lista.add(dto);
		}
		return lista;		
	}
	
	@Transactional
	public byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca) {
		
		Long numeroSemana = filtro.getSemana();
		
		List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor = chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(null, numeroSemana.intValue(), null);
		
		if(listaChamadaEncalheFornecedor==null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao gerar boleto.");
			
		}
		
		List<BoletoDistribuidor> listaBoletoDistribuidor = gerarCobrancaService.gerarCobrancaBoletoDistribuidor(listaChamadaEncalheFornecedor, tipoCobranca);
		
		try {
			
			return boletoService.gerarImpressaoBoletosDistribuidor(listaBoletoDistribuidor);
			
		} catch(Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao gerar cobrança em boleto para o Distribuidor");
			
		}
		
	}
	
	@Override
	@Transactional
	public void fecharCE(Long encalhe, ProdutoEdicao produtoEdicao) {
		this.fechamentoCEIntegracaoRepository.fecharCE(encalhe, produtoEdicao); 
		
	}

	@Override
	@Transactional
	public boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro) {		 
		return this.fechamentoCEIntegracaoRepository.verificarStatusSemana(filtro);
	}

	
	
}
