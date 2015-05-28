package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalFornecedor;
import br.com.abril.nds.model.fiscal.OrigemItemMovFechamentoFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemMovFechamentoFiscalFechamentoCEI;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.model.planejamento.fornecedor.StatusCeNDS;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.LancamentoParcialService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.TipoMovimentoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.Util;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.itextpdf.text.pdf.PdfPage;

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
	
	@Autowired 
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private LancamentoParcialService lancamentoParcialService;
	
	@Transactional(readOnly=true)
	public List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		filtro.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtro));
		
		return this.fechamentoCEIntegracaoRepository.buscarItensFechamentoCeIntegracao(filtro);
	}
	
	@Transactional(readOnly=true)
	public List<ItemFechamentoCEIntegracaoDTO> buscarItensFechamentoSemCEIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		return this.fechamentoCEIntegracaoRepository.buscarItensFechamentoSemCEIntegracao(filtro);
	}
	
	@Transactional
	public byte[] gerarCobrancaBoletoDistribuidor(FiltroFechamentoCEIntegracaoDTO filtro, TipoCobranca tipoCobranca) {
		
		filtro.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtro));
		
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
    private Diferenca processarDiferencaDeItemCEFornecedor(
    		ItemChamadaEncalheFornecedor itemCE, 
    		Usuario usuario) {
    	
		ProdutoEdicao produtoEdicao = itemCE.getProdutoEdicao();		
		
		BigInteger estoque = BigInteger.ZERO;
		
		if(produtoEdicao!= null 
				&& produtoEdicao.getEstoqueProduto()!= null){
			
			EstoqueProduto estoqueProduto = produtoEdicao.getEstoqueProduto();
			
			estoque = estoqueProduto.getQtde()
					.add(estoqueProduto.getQtdeDevolucaoEncalhe()
							.add(estoqueProduto.getQtdeSuplementar()));
		}
		
		BigInteger qntInformada = BigInteger.valueOf(Util.nvl(itemCE.getQtdeDevolucaoInformada(),0L));
		
		BigInteger calculoQdeDiferenca = qntInformada.subtract(estoque);
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setResponsavel(usuario);
		
		diferenca.setProdutoEdicao(produtoEdicao);
		
		if(calculoQdeDiferenca.compareTo(BigInteger.ZERO ) < 0 ){
			
			diferenca.setTipoDiferenca(TipoDiferenca.PERDA_EM);
			
			diferenca.setTipoEstoque(TipoEstoque.PERDA);
			
			diferenca.setQtde(calculoQdeDiferenca.abs());
			
		} else if(calculoQdeDiferenca.compareTo(BigInteger.ZERO) > 0){		
			
			diferenca.setTipoDiferenca(TipoDiferenca.GANHO_EM);
			
			diferenca.setTipoEstoque(TipoEstoque.GANHO);
			
			diferenca.setQtde(calculoQdeDiferenca.abs());
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
    private List<ChamadaEncalheFornecedor> obterChamadasEncalheFornecedor(final FiltroFechamentoCEIntegracaoDTO filtro) {
    	
        if (filtro.getSemana() == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Semana é obrigatório");
        }
        
		filtro.setPeriodoRecolhimento(this.recolhimentoService.getPeriodoRecolhimento(Integer.parseInt(filtro.getSemana())));
		
		filtro.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtro));
		
		final List<ChamadaEncalheFornecedor> chamadasFornecedor = chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
		
		if(chamadasFornecedor == null || chamadasFornecedor.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro no processo de confirmação do fechamento de CE integação. Registro não encontrado!");
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
    	
		BigInteger quantidadeEncalhe = BigInteger.valueOf(itemFo.getQtdeDevolucaoInformada());
		
		if (quantidadeEncalhe.compareTo(BigInteger.ZERO) == 0) {
			
			return;
		}
		
		TipoMovimentoEstoque tipoMovimentoEstoque = this.tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE);
    	
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(itemFo.getProdutoEdicao().getId(), 
				                                           usuario.getId(), 
				                                           quantidadeEncalhe,
				                                           tipoMovimentoEstoque,
				                                           Origem.TRANSFERENCIA_DEVOLUCAO_FORNECEDOR);
    }
    
    /**
     * Processa C.E. Integração
     * 
     * @param filtro
     * @param diferencas
     * @param chamadasFornecedor
     * @param fechamento
     */
	private void processaCE(FiltroFechamentoCEIntegracaoDTO filtro, Map<Long, ItemFechamentoCEIntegracaoDTO> itensAlterados, List<ChamadaEncalheFornecedor> chamadasFornecedor) {
		
		final Usuario usuario = this.usuarioService.getUsuarioLogado();
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		TipoMovimentoEstoque tipoMovimentoPerda = null; 
		TipoMovimentoEstoque tipoMovimentoSobraEmReparte = null;
		
		for(ChamadaEncalheFornecedor cef : chamadasFornecedor) {
			
			BigDecimal totalCreditoApurado = BigDecimal.ZERO;
			BigDecimal totalCreditoInformado = BigDecimal.ZERO;
			BigDecimal totalMargemApurado = BigDecimal.ZERO;
			BigDecimal totalMargemInformado = BigDecimal.ZERO;
			BigDecimal totalVendaApurada = BigDecimal.ZERO;
			BigDecimal totalVendaInformada = BigDecimal.ZERO;
			
			List<ItemChamadaEncalheFornecedor> itensChamadaEncalheFornecedor = itemChamadaEncalheFornecedorRepository.obterItensChamadaEncalheFornecedor(cef.getId());
			
			for(ItemChamadaEncalheFornecedor itemFo : itensChamadaEncalheFornecedor) {
				
				EstoqueProduto estoqueProduto = itemFo.getProdutoEdicao().getEstoqueProduto();
				
				itemFo = this.atualizarItemCE(itemFo, itensAlterados, estoqueProduto);
				
				this.validarEncalheOuVendaInformado(itemFo);
				
				this.processarEstoqueProduto(itemFo.getProdutoEdicao(), estoqueProduto);
				
				if(!RegimeRecolhimento.PARCIAL.equals(itemFo.getRegimeRecolhimento())) {
					
					this.efetuarTransferenciaEstoqueProduto(itemFo.getProdutoEdicao(), usuario);
				}
				
				Diferenca diferenca = this.processarDiferenca(itemFo, usuario, tipoMovimentoPerda, tipoMovimentoSobraEmReparte);
				
				if(diferenca!= null) {
					
					itemFo.setDiferenca(diferenca);
				}
				
				itemFo = this.itemChamadaEncalheFornecedorRepository.merge(itemFo);
				
				if(!RegimeRecolhimento.PARCIAL.equals(itemFo.getRegimeRecolhimento())) {
					
					this.gerarMovimentoEstoqueDevolucaoFornecedor(itemFo, dataOperacao);
				}
				
				totalCreditoApurado = totalCreditoApurado.add(itemFo.getValorVendaApurado());
				totalCreditoInformado = totalCreditoInformado.add(itemFo.getValorVendaInformado()); 
				totalMargemApurado = totalMargemApurado.add(itemFo.getValorMargemApurado());
				totalMargemInformado = totalMargemInformado.add(itemFo.getValorMargemInformado());
				totalVendaApurada = totalVendaApurada.add(BigDecimal.valueOf(Util.nvl(itemFo.getQtdeDevolucaoApurada(), 0L)));
				totalVendaInformada = totalVendaInformada.add(BigDecimal.valueOf(Util.nvl(itemFo.getQtdeVendaApurada(), 0L)));	
				
				//TODO: Ajustar os movimentos fiscais ao fechar a CE Integracao				
				List<OrigemItemMovFechamentoFiscal> listaOrigemMovsFiscais = new ArrayList<>();
				listaOrigemMovsFiscais.add(new OrigemItemMovFechamentoFiscalFechamentoCEI());

				MovimentoFechamentoFiscal mff = new MovimentoFechamentoFiscalFornecedor();
				mff.setOrigemMovimentoFechamentoFiscal(listaOrigemMovsFiscais);
        		mff.setQtde(BigInteger.valueOf(itemFo.getQtdeEnviada() - itemFo.getQtdeDevolucaoApurada()));
				
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
	 * Valida se a o encalhe ou venda informado ultrapassa o valo de reparte do item da chamada de encalhe fornecedor
	 * 
	 * @param itemFo
	 */
	private void validarEncalheOuVendaInformado(ItemChamadaEncalheFornecedor itemFo) {
		
		if(!RegimeRecolhimento.PARCIAL.equals(itemFo.getRegimeRecolhimento())) {
			
			Long qnrDevolucaoInformada = Util.nvl(itemFo.getQtdeDevolucaoInformada(),0L);
			
			if(qnrDevolucaoInformada > itemFo.getQtdeEnviada()) {
				
				StringBuilder msgValidacao = new StringBuilder();
				
				msgValidacao.append("A quantidade de encalhe do produto[")
							.append(itemFo.getProdutoEdicao().getProduto().getCodigo()).append(" - ")
							.append(itemFo.getProdutoEdicao().getProduto().getNome()).append(" - ")
							.append(itemFo.getProdutoEdicao().getNumeroEdicao())
							.append(" ] não pode exceder a quantidade do reparte!");
				
				throw new ValidacaoException(TipoMensagem.WARNING, msgValidacao.toString());
			}
		}
	}

	/**
	 * Verifica se existe estoque produto, caso não exista o mesmo é criado
	 * @param produtoEdicao
	 */
	private void processarEstoqueProduto(ProdutoEdicao produtoEdicao, EstoqueProduto estoqueProduto) {
		
		if(estoqueProduto == null) {
			
			if(produtoEdicao.getId() == null) {
				produtoEdicao = 
						produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
								produtoEdicao.getProduto().getCodigo(), produtoEdicao.getNumeroEdicao());
			}
			
			estoqueProduto = new EstoqueProduto();
			estoqueProduto.setProdutoEdicao(produtoEdicao);
			estoqueProduto = estoqueProdutoRespository.merge(estoqueProduto);
		}
	}

	private void efetuarTransferenciaEstoqueProduto(ProdutoEdicao produtoEdicao, Usuario usuario) {
		
		estoqueProdutoService.processarTransferenciaEntreEstoques(
				produtoEdicao.getId(),
				TipoEstoque.LANCAMENTO, 
				TipoEstoque.DEVOLUCAO_ENCALHE, 
				usuario.getId());
		
		estoqueProdutoService.processarTransferenciaEntreEstoques(
				produtoEdicao.getId(),
				TipoEstoque.SUPLEMENTAR, 
				TipoEstoque.DEVOLUCAO_ENCALHE, 
				usuario.getId());
		
	}

	/**
	 * Fecha C.E. Integração
	 * 
	 * @param filtro
	 * @param diferencas
	 */
	@Override
	@Transactional
	public void fecharCE(FiltroFechamentoCEIntegracaoDTO filtro, Map<Long,ItemFechamentoCEIntegracaoDTO> itensAlterados) {
		
		final List<ChamadaEncalheFornecedor> chamadasFornecedor = this.obterChamadasEncalheFornecedor(filtro);
		
		this.processaCE(filtro, itensAlterados, chamadasFornecedor);
	}

	
	
	private Diferenca processarDiferenca(ItemChamadaEncalheFornecedor item, 
										 Usuario usuario,
										 TipoMovimentoEstoque tipoMovimentoPerda,
										 TipoMovimentoEstoque tipoMovimentoSobraEmReparte){
		Diferenca diferenca = null;
		
		if(!RegimeRecolhimento.PARCIAL.equals(item.getRegimeRecolhimento())) {
			
			diferenca = this.processarDiferencaDeItemCEFornecedor(item,usuario);

			if (diferenca != null) {
				
				if(TipoEstoque.PERDA.equals(diferenca.getTipoEstoque())) {
					
					if(tipoMovimentoPerda == null){
						tipoMovimentoPerda = 
								tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.PERDA_EM_DEVOLUCAO);
					}
					
					MovimentoEstoque movimentoEstoque = 
							movimentoEstoqueService.gerarMovimentoEstoque(
							item.getProdutoEdicao().getId(),
							usuario.getId(), 
							diferenca.getQtde(), 
							tipoMovimentoPerda,
							Origem.TRANSFERENCIA_PERDA_EM_DEVOLUCAO_ENCALHE_FORNECEDOR);
					
					diferenca = diferencaEstoqueService.lancarDiferencaFechamentoCEIntegracao(diferenca,movimentoEstoque, StatusAprovacao.PERDA);
					
				} else {
					
					if(tipoMovimentoSobraEmReparte == null) { 
						tipoMovimentoSobraEmReparte = tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.SOBRA_EM_DEVOLUCAO);
					}
					
					MovimentoEstoque movimentoEstoque =  
							movimentoEstoqueService.gerarMovimentoEstoque(
							item.getProdutoEdicao().getId(),
							usuario.getId(), diferenca.getQtde(), 
							tipoMovimentoSobraEmReparte);
					
					diferenca = diferencaEstoqueService.lancarDiferencaFechamentoCEIntegracao(diferenca,movimentoEstoque, StatusAprovacao.GANHO);
				}
			}
		}
		return diferenca;
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
		
		for(ItemFechamentoCEIntegracaoDTO item : itens) {
			
			ItemChamadaEncalheFornecedor itemCE = this.itemChamadaEncalheFornecedorRepository.buscarPorId(item.getIdItemCeIntegracao());
			
			itemCE.setQtdeVendaInformada(Util.nvl(item.getVenda(), 0L).longValue());
			itemCE.setQtdeDevolucaoInformada(Util.nvl(item.getEncalhe(), 0L).longValue());
			
			if(itemCE != null) {
				
				itemCE = this.atualizarItemCE(itemCE, null, null);
				
				this.validarEncalheOuVendaInformado(itemCE);
				
				this.itemChamadaEncalheFornecedorRepository.merge(itemCE);
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
    		                                                     Date dataOperacao,
    		                                                     Usuario usuario){
    	
        if (quantidade.compareTo(BigInteger.ZERO) == 0){
			
			return;
		}
		
        TipoMovimentoEstoque tipoMovimentoEstoqueEstorno = 
    			this.tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_DEVOLUCAO_ENCALHE_FORNECEDOR);
    	
		this.movimentoEstoqueService.gerarMovimentoEstoque(idProdutoEdicao, 
				                                           usuario.getId(), 
				                                           quantidade,
				                                           tipoMovimentoEstoqueEstorno,
				                                           Origem.TRANSFERENCIA_ESTORNO_DEVOLUCAO_FORNECEDOR);
    }
    
    /**
	 * Cria Movimento de Estoque de Estorno da Diferenca do Item da CE Fornecedor
	 * 
	 * @param diferenca
	 * @param dataOperacao
	 */
    @SuppressWarnings("incomplete-switch")
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
		
    	TipoDiferenca tipoDiferenca = diferenca.getTipoDiferenca();
    	
    	Origem origem = null;
    	
    	TipoMovimentoEstoque tipoMovimentoEstoque = 
    			this.tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_DEVOLUCAO_ENCALHE_FORNECEDOR);
    	
    	switch (tipoDiferenca){ 
    	
	    	case PERDA_EM: 

	    		origem = Origem.TRANSFERENCIA_ESTORNO_PERDA_EM_DEVOLUCAO_ENCALHE_FORNECEDOR;
	    		
	    		break;
	    	case GANHO_EM:
	    	    	    		
	    		origem = Origem.TRANSFERENCIA_ESTORNO_SOBRA_DEVOLUCAO_FORNECEDOR;
	    		
	    		break;	
    	}
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(movimentoEstoque.getProdutoEdicao().getId(), 
				                                           usuario.getId(), 
				                                           movimentoEstoque.getQtde(),
				                                           tipoMovimentoEstoque,
				                                           origem);
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
		
    	Usuario usuario = usuarioService.getUsuarioLogado();
    	
		for(ItemChamadaEncalheFornecedor item : cef.getItens()){
			
			this.gerarMovimentoEstoqueEstornoDevolucaoFornecedor(item.getProdutoEdicao().getId(), 
					                                             BigInteger.valueOf(item.getQtdeDevolucaoInformada()), 
					                                             dataOperacao,
					                                             usuario);
			
			this.gerarMovimentoEstoqueEstornoDiferenca(item.getDiferenca(), dataOperacao);
		}
	}
	
	private ItemChamadaEncalheFornecedor atualizarItemCE(ItemChamadaEncalheFornecedor item,
			Map<Long,ItemFechamentoCEIntegracaoDTO> itensAlterados, EstoqueProduto estoqueProduto) {
		
		if(item == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Item de Fechamento inválido.");
		}
		
		Long qntVenda = Util.nvl(item.getQtdeVendaInformada(), 0L);
		Long qntEncalhe = item.getQtdeDevolucaoInformada();
		
		if(itensAlterados != null && itensAlterados.containsKey(item.getId())) {
			
			ItemFechamentoCEIntegracaoDTO itemCE = itensAlterados.get(item.getId());
			
			qntVenda = Util.nvl(itemCE.getVenda(), 0L).longValue();
			qntEncalhe = Util.nvl(itemCE.getEncalhe(), 0L).longValue();
		} else {
			
			if(qntEncalhe == null) {
				
				if(estoqueProduto != null) {

					qntEncalhe = Util.nvl(estoqueProduto.getQtde(), BigInteger.ZERO)
								.add(Util.nvl(estoqueProduto.getQtdeSuplementar(), BigInteger.ZERO)
										.add(Util.nvl(estoqueProduto.getQtdeDevolucaoEncalhe(), BigInteger.ZERO)))
											.longValue();
				} else {
					
					qntEncalhe = 0L;
				}
			}
		}
		
		if(Arrays.asList(RegimeRecolhimento.PARCIAL, RegimeRecolhimento.FINAL).contains(item.getRegimeRecolhimento())) {
			
			item.setQtdeVendaApurada(qntVenda);
		} else {
			
			item.setQtdeVendaApurada(item.getQtdeEnviada() - qntEncalhe);
		}
		
		item.setQtdeDevolucaoInformada(qntEncalhe);
		item.setQtdeDevolucaoApurada(item.getQtdeDevolucaoInformada());
		item.setQtdeVendaInformada(item.getQtdeVendaApurada());
		item.setQtdeDevolucaoParcial(0L);
		item.setValorVendaApurado(item.getPrecoUnitario().multiply(new BigDecimal(item.getQtdeVendaApurada())));
		
		BigDecimal desconto = obterPercentualDesconto(item);
		
		item.setValorMargemApurado(desconto.divide(new BigDecimal(100)).multiply(item.getValorVendaApurado()));
		item.setValorMargemInformado(item.getValorMargemApurado());
		item.setValorVendaInformado(item.getValorVendaApurado());
		
		return item;
	}
	
	@Override
	@Transactional
	public boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro) {		 
		return this.fechamentoCEIntegracaoRepository.verificarStatusSemana(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoDTO obterCEIntegracaoFornecedor(final FiltroFechamentoCEIntegracaoDTO filtro) {
		
	    if (filtro.getSemana() == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Semana é obrigatório");
        }
	    
	    filtro.setPeriodoRecolhimento(this.recolhimentoService.getPeriodoRecolhimento(Integer.parseInt(filtro.getSemana())));
	    filtro.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtro));
		
	    BigInteger qntItens = BigInteger.ZERO;
	    
	    final FechamentoCEIntegracaoDTO fechamentoCEIntegracaoDTO = new FechamentoCEIntegracaoDTO();
	    
	    switch (filtro.getComboCeIntegracao()) {
		
			case "COM":
			
				qntItens = fechamentoCEIntegracaoRepository.countItensFechamentoCeIntegracao(filtro);
				fechamentoCEIntegracaoDTO.setItensFechamentoCE(this.buscarItensFechamentoCeIntegracao(filtro));
				break;

			case "SEM":
				
				qntItens = fechamentoCEIntegracaoRepository.countItensFechamentoSemCeIntegracao(filtro);
				
				if(qntItens == null) {
					
					throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
				}
				
				fechamentoCEIntegracaoDTO.setItensFechamentoCE(this.buscarItensFechamentoSemCEIntegracao(filtro));
				
				break;

			default:
			break;
		
		}
	    
	    fechamentoCEIntegracaoDTO.setConsolidado(this.obterConsolidadoCE(filtro));
	    		
		if(qntItens.compareTo(BigInteger.ZERO) == 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		fechamentoCEIntegracaoDTO.setQntItensCE(qntItens.intValue());
		
		fechamentoCEIntegracaoDTO.setSemanaFechada(this.verificarStatusSemana(filtro));
		
		return fechamentoCEIntegracaoDTO;
	}

	@Override
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoConsolidadoDTO buscarConsolidadoItensFechamentoCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro, BigDecimal qntVenda) {
		
		filtro.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtro));
		
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
	
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoConsolidadoDTO obterConsolidadoCE(final FiltroFechamentoCEIntegracaoDTO filtro) {
		
	    if (filtro.getSemana() == null){
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Semana é obrigatório");
        }
        
        filtro.setPeriodoRecolhimento(this.recolhimentoService.getPeriodoRecolhimento(Integer.parseInt(filtro.getSemana())));
        
        filtro.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtro));
		
        if(filtro.getComboCeIntegracao().equals("SEM")) {        	
        	return this.fechamentoCEIntegracaoRepository.buscarConsolidadoSemCeIntegracao(filtro);
        } else {
        	return this.fechamentoCEIntegracaoRepository.buscarConsolidadoItensFechamentoCeIntegracao(filtro);        	
        }
        
	}

	@Override
	@Transactional
	public String reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		filtro.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtro));
		
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
        
    	List<Long> fornecedores = chamadaEncalheFornecedorRepository.obterIdentificadorFornecedoresChamadasEncalheFornecedor(filtro);
    	
    	if(fornecedores == null || fornecedores.isEmpty())
        	throw new ValidacaoException(TipoMensagem.WARNING, "Chamada de Encalhe Fornecedor não encontrada!");
        
    	Distribuidor distribuidor = distribuidorService.obter();
    	
    	if(fornecedores.size() > 1){
    		
    		return this.agruparArquivosCEFornecedor(filtro, fornecedores, distribuidor);
    	}
    	else{
    		
    		filtro.setIdFornecedor(fornecedores.get(0));
    		
    		filtro.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtro));
    		
    		List<ChamadaEncalheFornecedor> chamadasEncalheFornecedor = 
        			chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
            
            Collection<ChamadasEncalheFornecedorDTO> chamadasEncalheDTO = ChamadaEncalheFornecedorDTOAssembler
            		.criarChamadasEncalheFornecedorDTO(chamadasEncalheFornecedor,distribuidor);
    		
    		return this.gerarPDFChamadaEncalheFornecedor(chamadasEncalheDTO);
    	}
    }

	private byte[] agruparArquivosCEFornecedor(FiltroFechamentoCEIntegracaoDTO filtro, List<Long> fornecedores,
			Distribuidor distribuidor) {
		
		List<InputStream> arquivosCE = new ArrayList<>();
		
		for(Long item : fornecedores){
			
			filtro.setCodigoDistribuidorFornecdor(item.intValue());
			
			List<ChamadaEncalheFornecedor> chamadasEncalheFornecedor = 
					chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(filtro);
		    
		    Collection<ChamadasEncalheFornecedorDTO> chamadasEncalheDTO = ChamadaEncalheFornecedorDTOAssembler
		    		.criarChamadasEncalheFornecedorDTO(chamadasEncalheFornecedor,distribuidor);
		    
		    arquivosCE.add(new ByteArrayInputStream(this.gerarPDFChamadaEncalheFornecedor(chamadasEncalheDTO)));
		}
		
		try {
			
			return PDFUtil.concatPDFs(arquivosCE,false,PdfPage.LANDSCAPE);
		
		} catch (Exception e) {

			 LOGGER.error("Erro gerando PDF Chamada de Encalhe Fornecedor!", e);
	         throw new RuntimeException("Erro gerando PDF Chamada de Encalhe Fornecedor!", e);
		}
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
    
    @Override
    @Transactional(readOnly=true)
    public FechamentoCEIntegracaoDTO obterDiferencaCEIntegracaoFornecedor(
    		final FiltroFechamentoCEIntegracaoDTO filtroCE,
    		final Map<Long,ItemFechamentoCEIntegracaoDTO> itensAlteradosFechamento) {
    	
        if (filtroCE.getSemana() == null){
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Semana é obrigatório");
        }
        
        filtroCE.setPeriodoRecolhimento(
                this.recolhimentoService.getPeriodoRecolhimento(Integer.parseInt(filtroCE.getSemana())));
        
        filtroCE.setCodigoDistribuidorFornecdor(this.getCodigoFornecedorInterface(filtroCE));
		
    	List<ItemFechamentoCEIntegracaoDTO> itensFechamento = 
    			fechamentoCEIntegracaoRepository.buscarItensFechamentoCeIntegracaoComDiferenca(filtroCE);
    	
    	if(itensFechamento == null || itensFechamento.isEmpty()){
    		return new FechamentoCEIntegracaoDTO();
    	}
    	
    	itensFechamento = this.filtrarItensComDiferenca(itensFechamento,itensAlteradosFechamento);
    	
		FechamentoCEIntegracaoDTO fechamentoCEIntegracaoDTO = new FechamentoCEIntegracaoDTO();
	
		fechamentoCEIntegracaoDTO.setItensFechamentoCE(itensFechamento);
		
		fechamentoCEIntegracaoDTO.setQntItensCE(itensFechamento.size());
		
    	return fechamentoCEIntegracaoDTO;
    }

	@SuppressWarnings("unchecked")
	private List<ItemFechamentoCEIntegracaoDTO> filtrarItensComDiferenca(
			final List<ItemFechamentoCEIntegracaoDTO> itensFechamento,
			final Map<Long, ItemFechamentoCEIntegracaoDTO> itensAlteradosFechamento) {
		
		if(itensFechamento == null || itensFechamento.isEmpty()){
			return new ArrayList<>();
		}
       
		final Predicate<ItemFechamentoCEIntegracaoDTO> itemFechamentoPredicate = 
				new Predicate<ItemFechamentoCEIntegracaoDTO>() {
           @Override
           public boolean apply(final ItemFechamentoCEIntegracaoDTO item) {
               
        	   if(itensAlteradosFechamento!= null){
        		  
        		   if (itensAlteradosFechamento.containsKey(item.getIdItemCeIntegracao())){
        			   
        			   ItemFechamentoCEIntegracaoDTO itemAlterado = 
        					   itensAlteradosFechamento.get(item.getIdItemCeIntegracao());
        			   
        			   item.setDiferenca(itemAlterado.getDiferenca());
        			   item.setEncalhe(itemAlterado.getEncalhe());
        		   }
        	   }
        	   
        	   return !(item.getDiferenca().compareTo(BigInteger.ZERO) == 0) ;
           }
		};
       
		final Collection<ItemFechamentoCEIntegracaoDTO> filteredCollection =
               Collections2.filter(itensFechamento, itemFechamentoPredicate);
       
		return (List<ItemFechamentoCEIntegracaoDTO>)
				((filteredCollection != null) 
						? Lists.newArrayList(filteredCollection) 
								: new ArrayList<>());
	}
	
	private Integer getCodigoFornecedorInterface(FiltroFechamentoCEIntegracaoDTO filtro){
		
		if(filtro.getIdFornecedor()!= null){
			
			return fornecedorService.obterCodigoInterfacePorID(filtro.getIdFornecedor());
		}
		
		return null;
	}

	@Override
	@Transactional
	public void fecharSemCE(List<ItemFechamentoCEIntegracaoDTO> itens) {
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		for (ItemFechamentoCEIntegracaoDTO itemSemCe : itens) {
			
			 ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(itemSemCe.getCodigoProduto(), itemSemCe.getNumeroEdicao());
			
			this.gerarMovimentoEstoqueDevolucaoSemCEIntegracao(itemSemCe, dataOperacao, produtoEdicao);
		}
		
	}
	
	/**
     * Cria Movimento de Estoque de Devolução de Encalhe Fornecedor
     * 
     * @param itemFo
     * @param dataOperacao
     */
    private void gerarMovimentoEstoqueDevolucaoSemCEIntegracao(ItemFechamentoCEIntegracaoDTO itemSemCe, Date dataOperacao, ProdutoEdicao produtoEdicao){
    	
		BigInteger quantidadeEncalhe = itemSemCe.getEncalhe();
		
		if (quantidadeEncalhe.compareTo(BigInteger.ZERO) == 0) {
			
			return;
		}
		
		TipoMovimentoEstoque tipoMovimentoEstoque = this.tipoMovimentoService.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.DEVOLUCAO_ENCALHE);
    	
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
		this.movimentoEstoqueService.gerarMovimentoEstoque(produtoEdicao.getId(), 
				                                           usuario.getId(), 
				                                           quantidadeEncalhe,
				                                           tipoMovimentoEstoque,
				                                           Origem.TRANSFERENCIA_DEVOLUCAO_FORNECEDOR);
    }
    
	
}