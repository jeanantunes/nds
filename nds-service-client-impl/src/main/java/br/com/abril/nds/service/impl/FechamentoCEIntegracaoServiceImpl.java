package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.model.planejamento.fornecedor.StatusCeNDS;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;

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
	private UsuarioService usuarioService;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
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
				gerarCobrancaService.gerarCobrancaBoletoDistribuidor(listaChamadaEncalheFornecedor, tipoCobranca, Integer.valueOf(filtro.getSemana()));
		
		try {
			
			return boletoService.gerarImpressaoBoletosDistribuidor(listaBoletoDistribuidor);
			
		} catch(Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao gerar cobrança em boleto para o Distribuidor: " + e.getMessage());
			
		}
	}
	
    private ItemChamadaEncalheFornecedor atualizarItem(ItemChamadaEncalheFornecedor item, 
    		                                           ItemFechamentoCEIntegracaoDTO itemDTO) {

    	Long encalhe = 0l;
    	
	    Long vendaParcial = 0l;
	    
    	if (itemDTO!=null){
    		
    	    encalhe = itemDTO.getEncalhe() == null?0l:itemDTO.getEncalhe().longValue();
    	
    	    vendaParcial = itemDTO.getVenda() == null?0l:itemDTO.getVenda().longValue();
    	}
    	else{
    		
    		encalhe = item.getQtdeDevolucaoInformada() == null?0l:item.getQtdeDevolucaoInformada();
        	
    	    vendaParcial = item.getQtdeVendaApurada() == null?0l:item.getQtdeVendaApurada();
    	}
    	
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
    
    /**
     * Processa diferença do Item da Chamada de Encalhe do Fornecedor
     * 
     * @param itemCE
     * @return Diferenca
     */
    private Diferenca processarDiferencaDeItemCEFornecedor(ItemChamadaEncalheFornecedor itemCE) {
    	
    	Usuario usuario = this.usuarioService.getUsuarioLogado();
    	
    	Long encalhe = itemCE.getQtdeDevolucaoInformada() == null?0l:itemCE.getQtdeDevolucaoInformada();
    	
    	Long venda = itemCE.getQtdeVendaApurada() == null?0l:itemCE.getQtdeVendaApurada();
    	
    	Long reparte = itemCE.getQtdeEnviada() == null?0l:itemCE.getQtdeEnviada();
    	
		Long calculoQdeDiferenca = ((venda + encalhe) - reparte);
		
		ProdutoEdicao produtoEdicao = itemCE.getProdutoEdicao();		
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setQtde(BigInteger.valueOf(calculoQdeDiferenca));
		
		diferenca.setResponsavel(usuario);
		
		diferenca.setProdutoEdicao(produtoEdicao);
		
		if(diferenca.getQtde().compareTo(BigInteger.ZERO ) < 0 ){
			
			diferenca.setTipoDiferenca(TipoDiferenca.PERDA_EM);
			
			diferenca = diferencaEstoqueService.lancarDiferenca(diferenca, TipoEstoque.PERDA);
			
		} else if(diferenca.getQtde().compareTo(BigInteger.ZERO) > 0){		
			
			diferenca.setTipoDiferenca(TipoDiferenca.GANHO_EM);
			
			diferenca = diferencaEstoqueService.lancarDiferenca(diferenca, TipoEstoque.GANHO);
		}
		else{
			
			return null;
		}

		return diferenca;
	}
    
    /**
     * Obtem lista de Chamadas de Encalhe Fornecedor
     * 
     * @param filtro
     * @return List<ChamadaEncalheFornecedor>
     */
    private List<ChamadaEncalheFornecedor> obterChamadasEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro){
    	
		filtro.setPeriodoRecolhimento(this.obterPeriodoDataRecolhimento(filtro.getSemana()));
		
		List<ChamadaEncalheFornecedor> chamadasFornecedor = chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
		
		if(chamadasFornecedor == null || chamadasFornecedor.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro no processo de confirmação do fechamento de CE integação. Registro não encontrado!");
		}
		
		return chamadasFornecedor;
    }

    /**
     * Processa C.E. Integração
     * 
     * @param filtro
     * @param diferencas
     * @param chamadasFornecedor
     * @param fechamento
     */
	private void processaCE(FiltroFechamentoCEIntegracaoDTO filtro, 
			                Map<Long,ItemFechamentoCEIntegracaoDTO> diferencas,
			                List<ChamadaEncalheFornecedor> chamadasFornecedor,
			                boolean fechamento) {
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		for(ChamadaEncalheFornecedor cef : chamadasFornecedor){
			
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
				
				if(itemFo.getValorVendaApurado() == null || itemFo.getValorVendaApurado() == BigDecimal.ZERO) {
					
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
				
				itemFo = this.atualizarItem(itemFo, diferencas.get(itemFo.getId()));
				
			}
			
			cef.setTotalCreditoApurado(totalCreditoApurado);
			cef.setTotalCreditoInformado(totalCreditoInformado);
			cef.setTotalMargemApurado(totalMargemApurado);
			cef.setTotalMargemInformado(totalMargemInformado);
			cef.setTotalVendaApurada(totalVendaApurada);
			cef.setTotalVendaInformada(totalVendaInformada);
			cef.setStatusCeNDS(fechamento?StatusCeNDS.FECHADO:StatusCeNDS.ABERTO);
			cef.setDataFechamentoNDS(dataOperacao);
			
			chamadaEncalheFornecedorRepository.alterar(cef);
		}
	}
	
	/**
	 * Obtem lista de ID de CE Fornecedor
	 * @param chamadasFornecedor
	 * @return List<Long>
	 */
	private List<Long> obterListaIdCEFornecedor(List<ChamadaEncalheFornecedor> chamadasFornecedor){
		
		List<Long> listaId = new ArrayList<Long>();
		
		for(ChamadaEncalheFornecedor item : chamadasFornecedor){
			
			listaId.add(item.getId());
		}
	
		return listaId;
	}
	
    /**
     * Verifica se existem itens de CE Fornecedor com Movimento de Estoque de Perda ou Ganho Pendente de Confirmação
     * 
     * @param chamadasFornecedor
     */
	private void verificarPendenciaConfirmacaoPerdasEGanhos(List<ChamadaEncalheFornecedor> chamadasFornecedor){
		
		List<Long> listaIdCEFornecedor = this.obterListaIdCEFornecedor(chamadasFornecedor);
		
		List<ChamadaEncalheFornecedor> listaCEComDiferencaPendente = chamadaEncalheFornecedorRepository.obtemCEFornecedorComDiferencaPendente(listaIdCEFornecedor);
		
		if (listaCEComDiferencaPendente!=null && !listaCEComDiferencaPendente.isEmpty()){
		
		    throw new ValidacaoException(TipoMensagem.WARNING, "É necessário confirmar Perdas e Ganhos");	
		}
	}
	
	/**
     * Cria Movimento Estoque de Perda ou Ganho Pendente e Amarra com Item de CE Fornecedor
     * 
     * @param chamadasFornecedor
     */
	private void gerarPerdasEGanhos(List<ChamadaEncalheFornecedor> chamadasFornecedor){
		
		for (ChamadaEncalheFornecedor cef : chamadasFornecedor){
			
			for(ItemChamadaEncalheFornecedor item : cef.getItens()){

				Diferenca diferenca = this.processarDiferencaDeItemCEFornecedor(item);

				if (diferenca != null){
				
		            item.setDiferenca(diferenca);
		        
		            this.itemChamadaEncalheFornecedorRepository.merge(item);
		            
		            throw new ValidacaoException(TipoMensagem.WARNING, "É necessário confirmar Perdas e Ganhos");
				}
		    }
		}		
	}
	
	/**
	 * Verifica se existem pendencias de Perdas e Ganhos
	 * Lanca Perdas e Ganhos
	 * 
	 * @param chamadasFornecedor
	 */
	private void processarPerdasEGanhos(List<ChamadaEncalheFornecedor> chamadasFornecedor){
		
		this.verificarPendenciaConfirmacaoPerdasEGanhos(chamadasFornecedor);
		
		this.gerarPerdasEGanhos(chamadasFornecedor);
	}
	
	/**
	 * Fecha C.E. Integração
	 * 
	 * @param filtro
	 * @param diferencas
	 */
	@Override
	@Transactional(noRollbackForClassName = "ValidacaoException")
	public void fecharCE(FiltroFechamentoCEIntegracaoDTO filtro, 
			             Map<Long,ItemFechamentoCEIntegracaoDTO> diferencas) {
		
		List<ChamadaEncalheFornecedor> chamadasFornecedor = this.obterChamadasEncalheFornecedor(filtro);
		
		this.processarPerdasEGanhos(chamadasFornecedor);
		
		this.processaCE(filtro, 
				        diferencas, 
				        chamadasFornecedor,
				        true);
	}
	
	/**
	 * Salva C.E. Integração
	 * 
	 * @param filtro
	 * @param diferencas
	 */
	@Override
	@Transactional
	public void salvarCE(FiltroFechamentoCEIntegracaoDTO filtro, 
			             Map<Long,ItemFechamentoCEIntegracaoDTO> diferencas) {
		
		List<ChamadaEncalheFornecedor> chamadasFornecedor = this.obterChamadasEncalheFornecedor(filtro);
		
		this.processaCE(filtro, 
				        diferencas, 
				        chamadasFornecedor, 
				        false);
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
		
		Integer inicioSemana = this.distribuidorService.inicioSemana().getCodigoDiaSemana();
		
		Integer anoBase = SemanaUtil.getAno(anoSemana);
		
		Integer numeroSemana = SemanaUtil.getSemana(anoSemana);
		
		Date dataInicioSemana = SemanaUtil.obterDataDaSemanaNoAno(
			numeroSemana, inicioSemana, anoBase);
		
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		Intervalo<Date> periodoRecolhimento = new Intervalo<Date>(dataInicioSemana, dataFimSemana);
		
		return periodoRecolhimento;
		
	}

	@Transactional
	public void atualizarItemChamadaEncalheFornecedor(Long idItemChamadaFornecedor, BigInteger encalhe, BigInteger venda) {
		
		ItemChamadaEncalheFornecedor item = 
			this.itemChamadaEncalheFornecedorRepository.buscarPorId(idItemChamadaFornecedor);

		atualizarItem(item, null);
	}

	private BigDecimal obterPercentualDesconto(ItemChamadaEncalheFornecedor item) {
		
		BigDecimal valorRetorno = produtoEdicaoRepository.obterDescontoLogistica(item.getProdutoEdicao().getId());
		
		if(valorRetorno == null){
			valorRetorno = produtoRepository.obterDescontoLogistica(item.getProdutoEdicao().getProduto().getId());
		}
		
		return (valorRetorno == null)? BigDecimal.ZERO: valorRetorno;
	}

	
}