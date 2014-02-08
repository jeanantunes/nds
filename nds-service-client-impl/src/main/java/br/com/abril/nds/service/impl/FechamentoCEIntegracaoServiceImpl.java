package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.assembler.ChamadaEncalheFornecedorDTOAssembler;
import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.chamadaencalhe.ChamadasEncalheFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.MensagemException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.model.planejamento.fornecedor.StatusCeNDS;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.TipoMovimentoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;

@Service
public class FechamentoCEIntegracaoServiceImpl implements FechamentoCEIntegracaoService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FechamentoCEIntegracaoServiceImpl.class);

	private static final String PATH_JASPER_CE_DEVOLUCAO = "/reports/CE_Devolucao_Fornecedor_lote.jasper";
	
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
	private DiferencaEstoqueRepository diferencaEstoqueRepository;
	
	@Autowired
	private ItemChamadaEncalheFornecedorRepository itemChamadaEncalheFornecedorRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired 
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired 
	private TipoMovimentoService tipoMovimentoService;

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
	
    /**
     * Processa diferença do Item da Chamada de Encalhe do Fornecedor
     * 
     * @param itemCE
     * @return Diferenca
     */
    private Diferenca processarDiferencaDeItemCEFornecedor(ItemChamadaEncalheFornecedor itemCE) {
    	
    	Usuario usuario = this.usuarioService.getUsuarioLogado();
    	
    	Long encalhe = itemCE.getQtdeDevolucaoInformada() == null?0l:itemCE.getQtdeDevolucaoInformada();
    	
    	if (encalhe == 0){
    		
    		return null;
    	}
    	
    	Long venda = itemCE.getQtdeVendaApurada() == null?0l:itemCE.getQtdeVendaApurada();
    	
    	Long reparte = itemCE.getQtdeEnviada() == null?0l:itemCE.getQtdeEnviada();
    	
		Long calculoQdeDiferenca = ((venda + encalhe) - reparte);
		
		ProdutoEdicao produtoEdicao = itemCE.getProdutoEdicao();		
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setQtde(BigInteger.valueOf(calculoQdeDiferenca).abs());
		
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
     * Cria Movimento de Estoque de Devolução de Encalhe Fornecedor
     * 
     * @param itemFo
     * @param dataOperacao
     */
    private void gerarMovimentoEstoqueDevolucaoFornecedor(ItemChamadaEncalheFornecedor itemFo, Date dataOperacao){
    	
    	TipoMovimentoEstoque tipoMovimentoEstoque = this.tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE);
    	
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
		BigInteger quantidadeEncalhe = BigInteger.valueOf(itemFo.getQtdeDevolucaoInformada());
		
		if (quantidadeEncalhe.compareTo(BigInteger.ZERO) == 0){
			
			return;
		}
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(dataOperacao, 
				                                           itemFo.getProdutoEdicao().getId(), 
				                                           usuario.getId(), 
				                                           quantidadeEncalhe,
				                                           tipoMovimentoEstoque);
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
			                Map<Long,ItemFechamentoCEIntegracaoDTO> itensAlterados,
			                List<ChamadaEncalheFornecedor> chamadasFornecedor) {
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		for(ChamadaEncalheFornecedor cef : chamadasFornecedor){
			
			BigDecimal totalCreditoApurado = BigDecimal.ZERO;
			BigDecimal totalCreditoInformado = BigDecimal.ZERO;
			BigDecimal totalMargemApurado = BigDecimal.ZERO;
			BigDecimal totalMargemInformado = BigDecimal.ZERO;
			BigDecimal totalVendaApurada = BigDecimal.ZERO;
			BigDecimal totalVendaInformada = BigDecimal.ZERO;
			
			List<ItemChamadaEncalheFornecedor> itensChamadaEncalheFornecedor = 
							itemChamadaEncalheFornecedorRepository.obterItensChamadaEncalheFornecedor(cef.getId());
			
			for(ItemChamadaEncalheFornecedor itemFo : itensChamadaEncalheFornecedor) {
				
				BigInteger qntVenda = BigInteger.valueOf( itemFo.getQtdeVendaInformada() == null ? 0L : itemFo.getQtdeVendaInformada());
				BigInteger qntEncalhe = BigInteger.valueOf( itemFo.getQtdeDevolucaoInformada() == null ? 0L : itemFo.getQtdeDevolucaoInformada());
				
				if(itensAlterados.containsKey(itemFo.getId())){
					
					ItemFechamentoCEIntegracaoDTO itemCE = itensAlterados.get(itemFo.getId());
					
					qntVenda = itemCE.getVenda();
					qntEncalhe = itemCE.getEncalhe();
				}
				
				itemFo = this.atualizarItemCE(itemFo, qntVenda, qntEncalhe);
				
				totalCreditoApurado = totalCreditoApurado.add(itemFo.getValorVendaApurado());
				totalCreditoInformado = totalCreditoInformado.add(itemFo.getValorVendaInformado()); 
				totalMargemApurado = totalMargemApurado.add(itemFo.getValorMargemApurado());
				totalMargemInformado = totalMargemInformado.add(itemFo.getValorMargemInformado());
				totalVendaApurada = totalVendaApurada.add(BigDecimal.valueOf((itemFo.getQtdeDevolucaoApurada() == null) ? 0 : itemFo.getQtdeDevolucaoApurada()));
				totalVendaInformada = totalVendaInformada.add(BigDecimal.valueOf( (itemFo.getQtdeVendaApurada() == null) ? 0 : itemFo.getQtdeVendaApurada()));
				
                this.gerarMovimentoEstoqueDevolucaoFornecedor(itemFo, dataOperacao);	
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
	
	/**
	 * Obtem lista de ID de CE Fornecedor
	 * 
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
     * Verifica se existem itens de CE Fornecedor com Movimento de Estoque de Perda ou Ganho Confirmados
     * 
     * @param chamadasFornecedor
     */
	private boolean existeConfirmacaoPerdasEGanhos(List<ChamadaEncalheFornecedor> chamadasFornecedor){
		
		List<Long> listaIdCEFornecedor = this.obterListaIdCEFornecedor(chamadasFornecedor);
		
		List<ChamadaEncalheFornecedor> listaCEComDiferencaPendente = chamadaEncalheFornecedorRepository.obtemCEFornecedorComDiferencaPendente(listaIdCEFornecedor, 
				                                                                                                                              StatusConfirmacao.CONFIRMADO);
		
		if (listaCEComDiferencaPendente!=null && !listaCEComDiferencaPendente.isEmpty()){
		
		    return true;	
		}
		
		return false;
	}
	
    /**
     * Verifica se existem itens de CE Fornecedor com Movimento de Estoque de Perda ou Ganho Pendente de Confirmação
     * 
     * @param chamadasFornecedor
     */
	private void verificarPendenciaConfirmacaoPerdasEGanhos(List<ChamadaEncalheFornecedor> chamadasFornecedor){
		
		List<Long> listaIdCEFornecedor = this.obterListaIdCEFornecedor(chamadasFornecedor);
		
		List<ChamadaEncalheFornecedor> listaCEComDiferencaPendente = chamadaEncalheFornecedorRepository.obtemCEFornecedorComDiferencaPendente(listaIdCEFornecedor, 
				                                                                                                                              StatusConfirmacao.PENDENTE);
		
		if (listaCEComDiferencaPendente!=null && !listaCEComDiferencaPendente.isEmpty()){
		
		    throw new MensagemException(TipoMensagem.WARNING, "É necessário confirmar Perdas e Ganhos");	
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
		            
		            throw new MensagemException(TipoMensagem.WARNING, "É necessário confirmar Perdas e Ganhos");
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
		
        boolean isPerdaGanhoConfirmado = this.existeConfirmacaoPerdasEGanhos(chamadasFornecedor);
		
		if (!isPerdaGanhoConfirmado){
			
		    this.gerarPerdasEGanhos(chamadasFornecedor);
		}    
	}
	
	/**
	 * Fecha C.E. Integração
	 * 
	 * @param filtro
	 * @param diferencas
	 */
	@Override
	@Transactional(noRollbackForClassName = "MensagemException")
	public void fecharCE(FiltroFechamentoCEIntegracaoDTO filtro, 
			             Map<Long,ItemFechamentoCEIntegracaoDTO> diferencas) {
		
		List<ChamadaEncalheFornecedor> chamadasFornecedor = this.obterChamadasEncalheFornecedor(filtro);
		
		this.processarPerdasEGanhos(chamadasFornecedor);
		
		this.processaCE(filtro,diferencas,chamadasFornecedor);
	}
	
	/**
	 * Salva C.E. Integração
	 * 
	 * @param filtro
	 * @param diferencas
	 */
	@Override
	@Transactional
	public void salvarCE(List<ItemFechamentoCEIntegracaoDTO> itens) {
		
		for(ItemFechamentoCEIntegracaoDTO item : itens){
			
			ItemChamadaEncalheFornecedor itemCE = this.itemChamadaEncalheFornecedorRepository.buscarPorId(item.getIdItemCeIntegracao());
			
			if(itemCE!= null){
				
				this.atualizarItemCE(itemCE, item.getVenda(), item.getEncalhe());
			}
		}
	}
	
	/**
	 * Cria Movimento de Estoque de Estorno de Item da CE Fornecedor
	 * 
	 * @param idProdutoEdicao
	 * @param quantidade
	 * @param itemFo
	 * @param dataOperacao
	 */
    private void gerarMovimentoEstoqueEstornoDevolucaoFornecedor(Long idProdutoEdicao, 
    		                                                     BigInteger quantidade,
    		                                                     Date dataOperacao){
    	
    	TipoMovimentoEstoque tipoMovimentoEstoque = this.tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE);
    	
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
        if (quantidade.compareTo(BigInteger.ZERO) == 0){
			
			return;
		}
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(dataOperacao, 
				                                           idProdutoEdicao, 
				                                           usuario.getId(), 
				                                           quantidade,
				                                           tipoMovimentoEstoque);
    }
    
    /**
	 * Cria Movimento de Estoque de Estorno da Diferenca do Item da CE Fornecedor
	 * 
	 * @param diferenca
	 * @param dataOperacao
	 */
    private void gerarMovimentoEstoqueEstornoDiferenca(Diferenca diferenca,
    		                                           Date dataOperacao){
    	
    	if (diferenca == null){
    		
    		return;
    	}

        diferenca.setStatusConfirmacao(StatusConfirmacao.CANCELADO);
		
		this.diferencaEstoqueRepository.merge(diferenca);
		
		MovimentoEstoque movimentoEstoque = diferenca.getLancamentoDiferenca()!=null?diferenca.getLancamentoDiferenca().getMovimentoEstoque():null;
		
		if (movimentoEstoque == null){
			
			return;
		}
		
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
        TipoMovimentoEstoque tipoMovimentoEstoque = null;
    	
    	TipoDiferenca tipoDiferenca = diferenca.getTipoDiferenca();
    	
    	switch (tipoDiferenca){ 
    	
	    	case PERDA_EM: 
	    		
	    		tipoMovimentoEstoque = this.tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_PERDA_EM);
	    	
	    		break;
	    	case GANHO_EM:
	    	    
	    		tipoMovimentoEstoque = this.tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_GANHO_EM);
	    		
	    		break;	
    	}
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(dataOperacao, 
				                                           movimentoEstoque.getProdutoEdicao().getId(), 
				                                           usuario.getId(), 
				                                           movimentoEstoque.getQtde(),
				                                           tipoMovimentoEstoque);
    }
    
    /**
     * Realiza estorno de movimentos de estoque gerados no fechamento da CE Fornecedor
     * Cancela a diferença (falta/sobra)
     * 
     * @param cef
     * @param dataOperacao
     */
    @Override
    @Transactional
	public void estornarCeIntegracao(ChamadaEncalheFornecedor cef, Date dataOperacao){
		
		for(ItemChamadaEncalheFornecedor item : cef.getItens()){
			
			this.gerarMovimentoEstoqueEstornoDevolucaoFornecedor(item.getProdutoEdicao().getId(), 
					                                             BigInteger.valueOf(item.getQtdeDevolucaoInformada()), 
					                                             dataOperacao);
			
			this.gerarMovimentoEstoqueEstornoDiferenca(item.getDiferenca(), dataOperacao);
		}
	}
	
	private ItemChamadaEncalheFornecedor atualizarItemCE(ItemChamadaEncalheFornecedor item, BigInteger qtdVenda, BigInteger qntEncalhe ){
		
		Long vendaParcial = (qtdVenda == null) ? 0L: qtdVenda.longValue();
		
		Long encalhe = (qntEncalhe == null) ? 0L: qntEncalhe.longValue();
		
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
		
		item.setValorMargemApurado(desconto.divide(new BigDecimal(100)).multiply(item.getValorVendaApurado()));
		item.setValorVendaApurado(item.getValorVendaApurado().subtract(item.getValorMargemApurado()));
		
		item.setValorMargemInformado(item.getValorMargemApurado());
		item.setValorVendaInformado(item.getValorVendaApurado());
			
		return this.itemChamadaEncalheFornecedorRepository.merge(item);

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
		
		fechamentoCEIntegracaoDTO.setConsolidado(this.obterConsolidadoCE(filtro));
		
		fechamentoCEIntegracaoDTO.setSemanaFechada(this.verificarStatusSemana(filtro));
		
		return fechamentoCEIntegracaoDTO;
	}

	@Override
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoConsolidadoDTO buscarConsolidadoItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro, BigDecimal qntVenda) {
		
		ItemChamadaEncalheFornecedor item = itemChamadaEncalheFornecedorRepository.buscarPorId(filtro.getIdItemChamadaEncalheFornecedor());
		
		FechamentoCEIntegracaoConsolidadoDTO consolidadoItem = this.obterConsolidadoCE(filtro);
		
		filtro.setIdItemChamadaEncalheFornecedor(null);
		FechamentoCEIntegracaoConsolidadoDTO consolidadoItens = this.obterConsolidadoCE(filtro);
		
		BigDecimal desconto = obterPercentualDesconto(item);
		
		BigDecimal valorBruto = (item.getPrecoUnitario().multiply(qntVenda));
		BigDecimal valorDesconto = (desconto.divide(new BigDecimal(100)).multiply(valorBruto));
		
		consolidadoItens.setTotalBruto(consolidadoItens.getTotalBruto().subtract(consolidadoItem.getTotalBruto()));
		consolidadoItens.setTotalDesconto(consolidadoItens.getTotalDesconto().subtract(consolidadoItem.getTotalDesconto()));
		
		consolidadoItens.setTotalBruto(consolidadoItens.getTotalBruto().add(valorBruto));
		consolidadoItens.setTotalDesconto(consolidadoItens.getTotalDesconto().add(valorDesconto));
		
		consolidadoItens.setTotalLiquido(consolidadoItens.getTotalBruto().subtract(consolidadoItens.getTotalDesconto()));
		
		return consolidadoItens;
	}
	
	private FechamentoCEIntegracaoConsolidadoDTO obterConsolidadoCE(FiltroFechamentoCEIntegracaoDTO filtro) {
		
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
		
		for(ChamadaEncalheFornecedor cef : chamadasFornecedor){
			
			if(cef.getDataFechamentoNDS().compareTo(dataOperacao)!=0
					|| StatusIntegracao.INTEGRADO.equals(cef.getStatusIntegracao())){
				
				fornecedorSemReabertura.append((cef.getFornecedor().getJuridica()!= null)
						? cef.getFornecedor().getJuridica().getRazaoSocial()
								:"").append(",");
				continue;
			}

			this.estornarCeIntegracao(cef, dataOperacao); 

			cef.setStatusCeNDS(StatusCeNDS.ABERTO);
			
			chamadaEncalheFornecedorRepository.alterar(cef);
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

		atualizarItemCE(item, venda, encalhe);
	}

	private BigDecimal obterPercentualDesconto(ItemChamadaEncalheFornecedor item) {
		
		BigDecimal valorRetorno = produtoEdicaoRepository.obterDescontoLogistica(item.getProdutoEdicao().getId());
		
		if(valorRetorno == null){
			valorRetorno = produtoRepository.obterDescontoLogistica(item.getProdutoEdicao().getProduto().getId());
		}
		
		return (valorRetorno == null)? BigDecimal.ZERO: valorRetorno;
	}
	
	  /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly=true)
    public byte[] gerarImpressaoChamadaEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro) {
        
    	List<ChamadaEncalheFornecedor> chamadasEncalheFornecedor = 
    			chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);

        if(chamadasEncalheFornecedor.isEmpty())
        	throw new ValidacaoException(TipoMensagem.WARNING, "Chamada de Encalhe Fornecedor não encontrada!");
        
        Distribuidor distribuidor = distribuidorService.obter();
        Collection<ChamadasEncalheFornecedorDTO> chamadasEncalheDTO = ChamadaEncalheFornecedorDTOAssembler
                .criarChamadasEncalheFornecedorDTO(chamadasEncalheFornecedor,
                        distribuidor);
        
        return gerarPDFChamadaEncalheFornecedor(chamadasEncalheDTO);
    }
    
    /**
     * Gera o PDF com as chamadas de encalhe recebidas
     * 
     * @param chamadas chamadas de encalhe para geração do PDF
     * @return PDF gerado com as chamadas de encalhe
     */
    private byte[] gerarPDFChamadaEncalheFornecedor(Collection<ChamadasEncalheFornecedorDTO> chamadas) {
       
        try {
            
        	JRDataSource jrDataSource = new JRBeanCollectionDataSource(chamadas);

    		URL url = Thread.currentThread().getContextClassLoader().getResource(PATH_JASPER_CE_DEVOLUCAO);

    		String path = url.toURI().getPath();

    		return JasperRunManager.runReportToPdf(path, new HashMap<String, Object>(), jrDataSource);
        	
        } catch (URISyntaxException | JRException ex) {
            LOGGER.error("Erro gerando PDF Chamada de Encalhe Fornecedor!", ex);
            throw new RuntimeException("Erro gerando PDF Chamada de Encalhe Fornecedor!", ex);
        }
    }
    

}