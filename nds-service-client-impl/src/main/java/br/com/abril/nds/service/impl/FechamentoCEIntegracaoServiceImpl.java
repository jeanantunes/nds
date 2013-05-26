package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
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
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.model.planejamento.fornecedor.StatusCeNDS;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
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
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Transactional
	public List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		return this.fechamentoCEIntegracaoRepository.buscarItensFechamentoCeIntegracao(filtro);
	}
	
	@Transactional
	public byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca) {
		
		List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor = 
				chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
		if(listaChamadaEncalheFornecedor == null) {
			
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
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		for(ChamadaEncalheFornecedor cef : chamadasFornecedor){
			
			// Essa validação deverá ser feita somente se houverem outros fornecedores vindos do Prodin.
			/*if(cef.getFornecedor() == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro de integridade. Não existe fornecedor associado ao registro!");
			}*/
			
			BigDecimal totalCreditoApurado = BigDecimal.ZERO;
			BigDecimal totalCreditoInformado = BigDecimal.ZERO;
			BigDecimal totalMargemApurado = BigDecimal.ZERO;
			BigDecimal totalMargemInformado = BigDecimal.ZERO;
			BigDecimal totalVendaApurada = BigDecimal.ZERO;
			BigDecimal totalVendaInformada = BigDecimal.ZERO;
			
			List<ItemChamadaEncalheFornecedor> itensChamadaEncalheFornecedor = 
							itemChamadaEncalheFornecedorRepository.obterItensChamadaEncalheFornecedor(cef.getId(), filtro.getPeriodoRecolhimento());
			
			for(ItemChamadaEncalheFornecedor itemFo : itensChamadaEncalheFornecedor) {
				
				List<ItemFechamentoCEIntegracaoDTO> itemFechamentoCEIntegracaoDTO = null;
				if(itemFo.getValorVendaApurado() == null || itemFo.getValorVendaApurado() != null && itemFo.getValorVendaApurado() == BigDecimal.ZERO) {
					filtro.setIdItemChamadaEncalheFornecedor(itemFo.getId());
					itemFechamentoCEIntegracaoDTO = fechamentoCEIntegracaoRepository.buscarItensFechamentoCeIntegracao(filtro);
					filtro.setIdItemChamadaEncalheFornecedor(null);
					ItemFechamentoCEIntegracaoDTO item = itemFechamentoCEIntegracaoDTO.get(0);
					
					BigDecimal desconto = obterPercentualDesconto(itemFo);
					
					itemFo.setValorVendaApurado(item.getPrecoCapa().multiply(new BigDecimal(item.getVenda())));
					itemFo.setValorVendaInformado(item.getPrecoCapa().multiply(new BigDecimal(item.getVenda())));
					itemFo.setValorMargemApurado(desconto.divide(new BigDecimal(100)).multiply(itemFo.getPrecoUnitario()).multiply(itemFo.getValorVendaApurado()));
					itemFo.setValorVendaApurado(itemFo.getPrecoUnitario().multiply(itemFo.getValorVendaApurado()).subtract(itemFo.getValorMargemApurado()));
					
					itemFo.setValorMargemInformado(itemFo.getValorMargemApurado());
					itemFo.setValorVendaInformado(itemFo.getValorVendaApurado());
					
				}
				
				totalCreditoApurado = totalCreditoApurado.add(itemFo.getValorVendaApurado());
				totalCreditoInformado = totalCreditoInformado.add(itemFo.getValorVendaInformado()); 
				totalMargemApurado = totalMargemApurado.add(itemFo.getValorMargemApurado());
				totalMargemInformado = totalMargemInformado.add(itemFo.getValorMargemInformado());
				totalVendaApurada = totalVendaApurada.add(BigDecimal.valueOf((itemFo.getQtdeDevolucaoApurada() == null) ? 0 : itemFo.getQtdeDevolucaoApurada()));
				totalVendaInformada = totalVendaInformada.add(BigDecimal.valueOf( (itemFo.getQtdeVendaApurada() == null) ? 0 : itemFo.getQtdeVendaApurada()));
				
				itemFo = this.atualizarItem(itemFo.getQtdeDevolucaoInformada(), itemFo.getQtdeVendaInformada(), itemFo);
				
			}
			
			cef.setTotalCreditoApurado(totalCreditoApurado);
			cef.setTotalCreditoInformado(totalCreditoInformado);
			cef.setTotalMargemApurado(totalMargemApurado);
			cef.setTotalMargemInformado(totalMargemInformado);
			cef.setTotalVendaApurada(totalVendaApurada);
			cef.setTotalVendaInformada(totalVendaInformada);
			cef.setStatusCeNDS(StatusCeNDS.FECHADO);
			cef.setDataFechamentoNDS(dataOperacao);
			
			chamadaEncalheFornecedorRepository.alterar(cef);
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
	public String reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		List<ChamadaEncalheFornecedor> chamadasFornecedor = 
				chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
		
		if(chamadasFornecedor == null || chamadasFornecedor.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Não foram encontrados itens para reabertura!");
		}	
		
		StringBuilder fornecedorSemReabertura = new StringBuilder();
		
		for(ChamadaEncalheFornecedor item : chamadasFornecedor){
			
			if(item.getDataFechamentoNDS().compareTo(dataOperacao)!=0
					|| StatusIntegracao.INTEGRADO.equals(item.getStatusIntegracao())){
				
				fornecedorSemReabertura.append((item.getFornecedor().getJuridica()!= null)
						? item.getFornecedor().getJuridica().getRazaoSocial()
								:"").append(",");
				continue;
			}
			
			item.setStatusCeNDS(StatusCeNDS.ABERTO);
			chamadaEncalheFornecedorRepository.alterar(item);
		}
		
		return montarMensagemFornecedorSemReabertura(fornecedorSemReabertura);
	}

	private String montarMensagemFornecedorSemReabertura(StringBuilder nomesFornecedores) {
		
		if(nomesFornecedores.length()==0){
			return null;
		}
		
		StringBuilder mensagem = new StringBuilder();
		
		mensagem.append(" Os itens associados ao fornecedor [")
				.append( nomesFornecedores.delete(nomesFornecedores.length()-1,nomesFornecedores.length()))
				.append(" ] não puderam  ser reabertos,")
				.append(" pois a data de operação do distribuidor é diferente da data de fechamento da CE ou a interface de integração já processou os dados!");
		
		return mensagem.toString();
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
	public void atualizarItemChamadaEncalheFornecedor(Long idItemChamadaFornecedor, BigInteger encalhe, BigInteger venda) {
		
		ItemChamadaEncalheFornecedor item = 
			this.itemChamadaEncalheFornecedorRepository.buscarPorId(idItemChamadaFornecedor);

		atualizarItem(encalhe.longValue(),venda.longValue(),item);
	}

	private ItemChamadaEncalheFornecedor atualizarItem(Long encalhe, Long vendaParcial, ItemChamadaEncalheFornecedor item) {
		
		encalhe = (encalhe == null) ? 0 : encalhe;
		vendaParcial = (vendaParcial == null) ? 0 : vendaParcial;
		
		if( RegimeRecolhimento.PARCIAL.equals(item.getRegimeRecolhimento())){
			
			item.setQtdeVendaApurada(vendaParcial);
		}
		else{
			
			item.setQtdeVendaApurada(item.getQtdeEnviada() - encalhe);
		}
		
		item.setQtdeDevolucaoInformada(encalhe);
		item.setQtdeDevolucaoApurada(item.getQtdeDevolucaoInformada());
		item.setQtdeVendaInformada(item.getQtdeVendaApurada());
		item.setQtdeDevolucaoParcial(0L);
		item.setValorVendaApurado(item.getPrecoUnitario().multiply(new BigDecimal(item.getQtdeVendaApurada())));
		
		BigDecimal desconto = obterPercentualDesconto(item);
		
		item.setValorMargemApurado(desconto.divide(new BigDecimal(100)).multiply(item.getPrecoUnitario()).multiply(item.getValorVendaApurado()));
		item.setValorVendaApurado(item.getPrecoUnitario().multiply(item.getValorVendaApurado()).subtract(item.getValorMargemApurado()));
		
		item.setValorMargemInformado(item.getValorMargemApurado());
		item.setValorVendaInformado(item.getValorVendaApurado());
			
		return this.itemChamadaEncalheFornecedorRepository.merge(item);
	}

	private BigDecimal obterPercentualDesconto(ItemChamadaEncalheFornecedor item) {
		
		BigDecimal valorRetorno = produtoEdicaoRepository.obterDescontoLogistica(item.getProdutoEdicao().getId());
		
		if(valorRetorno == null){
			valorRetorno = produtoRepository.obterDescontoLogistica(item.getProdutoEdicao().getProduto().getId());
		}
		
		return (valorRetorno == null)? BigDecimal.ZERO: valorRetorno;
	}

	
}
