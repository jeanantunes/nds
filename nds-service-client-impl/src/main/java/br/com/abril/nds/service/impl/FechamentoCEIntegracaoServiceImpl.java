package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.StatusCeNDS;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;

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
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ItemChamadaEncalheFornecedorRepository itemChamadaEncalheFornecedorRepository;

	@Transactional
	public List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		return this.fechamentoCEIntegracaoRepository.buscarItensFechamentoCeIntegracao(filtro);
	}
	
	@Transactional
	public byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca) {
		
		List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor = 
				chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
		if(listaChamadaEncalheFornecedor==null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao gerar boleto.");
			
		}
		
		List<BoletoDistribuidor> listaBoletoDistribuidor = 
				gerarCobrancaService.gerarCobrancaBoletoDistribuidor(listaChamadaEncalheFornecedor, tipoCobranca, filtro.getNumeroSemana());
		
		try {
			
			return boletoService.gerarImpressaoBoletosDistribuidor(listaBoletoDistribuidor);
			
		} catch(Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao gerar cobrança em boleto para o Distribuidor: " + e.getMessage());
			
		}
	}
	
	@Override
	@Transactional
	public void fecharCE(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		filtro.setPeriodoRecolhimento(this.obterPeriodoDataRecolhimento(filtro.getSemana()));
		
		List<ChamadaEncalheFornecedor> chamadasFornecedor = 
				chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
		
		if(chamadasFornecedor == null || chamadasFornecedor.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro no processo de confirmação do fechamento de CE integação. Registro não encontrado!");
		}	
		
		for(ChamadaEncalheFornecedor item : chamadasFornecedor){
					
			if(item.getFornecedor() == null){
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro de integridade. Não existe fornecedor associado ao registro!");
			}
			
			filtro.setIdFornecedor(item.getFornecedor().getId());
			
			FechamentoCEIntegracaoConsolidadoDTO consolidado =
					this.fechamentoCEIntegracaoRepository.buscarConsolidadoItensFechamentoCeIntegracao(filtro);
		
			if(consolidado!= null){
				
				//TODO verificar os dados que devem ser persistido nos campos de TOTAL dessa entidade
				item.setTotalVendaApurada(consolidado.getTotalBruto());
			}
			
			item.setStatusCeNDS(StatusCeNDS.FECHADO);
			
			chamadaEncalheFornecedorRepository.alterar(item);
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
		
		filtro.setPeriodoRecolhimento(this.obterPeriodoDataRecolhimento(filtro.getSemana()));
		
		BigInteger qntItens = fechamentoCEIntegracaoRepository.countItensFechamentoCeIntegracao(filtro);
		
		if(qntItens.compareTo(BigInteger.ZERO) == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		FechamentoCEIntegracaoDTO fechamentoCEIntegracaoDTO = new FechamentoCEIntegracaoDTO();
	
		fechamentoCEIntegracaoDTO.setQntItensCE(qntItens.intValue());
		
		fechamentoCEIntegracaoDTO.setItensFechamentoCE( this.buscarItensFechamentoCeIntegracao(filtro));
		
		fechamentoCEIntegracaoDTO.setConsolidado(this.buscarConsolidadoItensFechamentoCeIntegracao(filtro));
		
		fechamentoCEIntegracaoDTO.setSemanaFechada(this.verificarStatusSemana(filtro));
		
		return fechamentoCEIntegracaoDTO;
	}

	@Override
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoConsolidadoDTO buscarConsolidadoItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		filtro.setPeriodoRecolhimento(this.obterPeriodoDataRecolhimento(filtro.getSemana()));
		
		return this.fechamentoCEIntegracaoRepository.buscarConsolidadoItensFechamentoCeIntegracao(filtro);
	}
	
	@Override
	@Transactional
	public void reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		//TODO verificar se existe alguma regra para reabertura de CE
		
		List<ChamadaEncalheFornecedor> chamadasFornecedor = 
				chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
		
		if(chamadasFornecedor!= null && !chamadasFornecedor.isEmpty()){
			
			for(ChamadaEncalheFornecedor item : chamadasFornecedor){
				item.setStatusCeNDS(StatusCeNDS.ABERTO);
				chamadaEncalheFornecedorRepository.alterar(item);
			}
		}
	}
	
	private Intervalo<Date> obterPeriodoDataRecolhimento(String anoSemana) {
		
		Date data = obterDataBase(anoSemana, this.distribuidorService.obterDataOperacaoDistribuidor()); 
		
		Integer semana = Integer.parseInt(anoSemana.substring(4));
		
		Date dataInicioSemana = 
				DateUtil.obterDataDaSemanaNoAno(
					semana, this.distribuidorService.inicioSemana().getCodigoDiaSemana(), data);
			
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		Intervalo<Date> periodoRecolhimento = new Intervalo<Date>(dataInicioSemana, dataFimSemana);
		
		return periodoRecolhimento;
		
	}
	
	private Date obterDataBase(String anoSemana, Date data) {
		
		String ano = anoSemana.substring(0,4);
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.YEAR, Integer.parseInt(ano));
		
		return c.getTime();
	}
	
	@Transactional
	public void atualizarItemChamadaEncalheFornecedor(Long idItemChamadaFornecedor, BigInteger encalhe) {
		
		ItemChamadaEncalheFornecedor item = 
			this.itemChamadaEncalheFornecedorRepository.buscarPorId(idItemChamadaFornecedor);
		
		item.setQtdeDevolucaoInformada(encalhe.longValue());
		
		this.itemChamadaEncalheFornecedorRepository.merge(item);
	}

	
}
