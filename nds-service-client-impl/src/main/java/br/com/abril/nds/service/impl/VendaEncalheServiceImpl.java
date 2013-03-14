package br.com.abril.nds.service.impl;

import java.awt.Image;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ItemSlipVendaEncalheDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.SlipVendaEncalheDTO;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.estoque.VendaProduto;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.repository.VendaProdutoEncalheRepository;
import br.com.abril.nds.service.ControleNumeracaoSlipService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.VendaEncalheService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.MathUtil;

/**
 * 
 * Classe de implementação de serviços referentes a vendas de encalhe
 * 
 * @author Discover Technology
 * 
 */

@Service
public class VendaEncalheServiceImpl implements VendaEncalheService {

	private static final String TITULO_COMPROVANTE_ENCALHE = "Comprovante Venda Encalhe";

	private static final String TITULO_COMPROVANTE_SUPLEMENTAR = "Comprovante Venda Suplementar";

	private static final String TITULO_SLIP_ENCALHE = "Slip Venda Encalhe";

	private static final String TITULO_SLIP_SUPLEMENTAR = "Slip Venda Suplementar";
	
	private static final String PATH_COMPROVANTE_VENDA_IREPORT = "/reports/slipComprovanteVendaEncalheSuplementar.jasper";

	private static final String NOME_SLIP_VENDA_IREPORT = "slipVendaEncalheSuplementar.jasper";
	
	private static final String NOME_COMPROVANTE_VENDA_IREPORT = "slipComprovanteVendaEncalheSuplementar.jasper";

	@Autowired
	private VendaProdutoEncalheRepository vendaProdutoRepository;

	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;

	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;

	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;

	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;

	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;

	@Autowired
	private ControleNumeracaoSlipService controleNumeracaoSlipServiceImpl;

	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private GerarCobrancaService cobrancaService;
		
	private Image logoDistribuidor;
	
	private SlipVendaEncalheDTO obterDadosSlipVenda(VendaProduto... vendas){
		
		SlipVendaEncalheDTO slipVendaEncalhe = new SlipVendaEncalheDTO();

		if (vendas != null) {

			List<ItemSlipVendaEncalheDTO> itensVendaEncalhe = new ArrayList<ItemSlipVendaEncalheDTO>();

			Integer quantidadeTotalVista = 0;
			BigDecimal valorTotalVista = BigDecimal.ZERO;
			Integer quantidadeTotalPrazo = 0;
			BigDecimal valorTotalPrazo = BigDecimal.ZERO;

			for (VendaProduto itemVE : vendas) {

				if (FormaComercializacao.CONSIGNADO.equals(itemVE.getTipoComercializacaoVenda())) {

					quantidadeTotalPrazo += itemVE.getQntProduto().intValue();
					valorTotalPrazo = valorTotalPrazo.add(itemVE.getValorTotalVenda());

				} else {

					quantidadeTotalVista += itemVE.getQntProduto().intValue();
					valorTotalVista = valorTotalVista.add(itemVE.getValorTotalVenda());
				}

				itensVendaEncalhe.add(getItemSlipVendaEncalheDTO(itemVE));
			}

			slipVendaEncalhe = getSlipVendaEncalheDTO(vendas[0]);

			slipVendaEncalhe.setQuantidadeTotalVista(quantidadeTotalVista.toString());
			slipVendaEncalhe.setValorTotalVista(CurrencyUtil.formatarValor(valorTotalVista));

			slipVendaEncalhe.setQuantidadeTotalPrazo(quantidadeTotalPrazo.toString());
			slipVendaEncalhe.setValorTotalPrazo(CurrencyUtil.formatarValor(valorTotalPrazo));

			slipVendaEncalhe.setQuantidadeTotalGeral((quantidadeTotalVista + quantidadeTotalPrazo)+ "");
			slipVendaEncalhe.setValorTotalGeral(CurrencyUtil.formatarValor(valorTotalVista.add(valorTotalPrazo)));

			slipVendaEncalhe.setListaItensSlip(itensVendaEncalhe);
		}

		return slipVendaEncalhe;
	}

	private SlipVendaEncalheDTO getSlipVendaEncalheDTO(VendaProduto itemVE) {

		SlipVendaEncalheDTO slipVendaEncalhe = new SlipVendaEncalheDTO();

		slipVendaEncalhe.setNomeCota(itemVE.getCota().getPessoa().getNome());
		slipVendaEncalhe.setNumeroCota(itemVE.getCota().getNumeroCota().toString());
		
		Box box = itemVE.getCota().getBox();
		
		if(box!= null){
			slipVendaEncalhe.setNumeroBox(itemVE.getCota().getBox().getCodigo().toString());
			slipVendaEncalhe.setDescricaoBox(itemVE.getCota().getBox().getTipoBox().name());
		}
		else{
			slipVendaEncalhe.setNumeroBox("");
			slipVendaEncalhe.setDescricaoBox("");
		}
		
		slipVendaEncalhe.setData(DateUtil.formatarDataPTBR(itemVE.getDataVenda()));
		slipVendaEncalhe.setHora(DateUtil.formatarData(itemVE.getHorarioVenda(), "HH:mm"));
		slipVendaEncalhe.setUsuario(itemVE.getUsuario().getNome());

		return slipVendaEncalhe;
	}

	private ItemSlipVendaEncalheDTO getItemSlipVendaEncalheDTO(VendaProduto itemVE) {

		ItemSlipVendaEncalheDTO item = new ItemSlipVendaEncalheDTO();

		item.setCodigo(itemVE.getProdutoEdicao().getProduto().getCodigo());
		item.setProduto(itemVE.getProdutoEdicao().getProduto().getNome());
		item.setEdicao(itemVE.getProdutoEdicao().getNumeroEdicao().toString());
		item.setQuantidade(itemVE.getQntProduto().toString());

		BigDecimal precoVenda = itemVE.getProdutoEdicao().getPrecoVenda();
		
		BigDecimal percentualDesconto = 
				descontoService.obterValorDescontoPorCotaProdutoEdicao(null,itemVE.getCota(), itemVE.getProdutoEdicao());
		
		BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);

		item.setPreco(CurrencyUtil.formatarValor(precoVenda.subtract(valorDesconto)));
		item.setTotal(CurrencyUtil.formatarValor(itemVE.getValorTotalVenda()));

		return item;
	}

	/**
	 * Gera um relatório à partir de um Objeto com atributos e listas definidas
	 * 
	 * @param list
	 * @param pathJasper
	 * @return Array de bytes do relatório gerado
	 * @throws JRException
	 * @throws URISyntaxException
	 */
	private byte[] gerarDocumentoIreport(SlipVendaEncalheDTO slipVendaEncalhe,
										 String pathJasper, TipoVendaEncalhe tipoVenda) throws JRException, URISyntaxException {
		
		Map<String, Object> parameters = obterParametrosRelatorioSlipVenda(slipVendaEncalhe, tipoVenda);

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(slipVendaEncalhe.getListaItensSlip());

		URL url = Thread.currentThread().getContextClassLoader().getResource(pathJasper);

		String path = url.toURI().getPath();

		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	private byte[] gerarDocumentoSlipDeVenda(TipoVendaEncalhe tipoVenda, SlipVendaEncalheDTO... slipVendaEncalhes) throws URISyntaxException{
		
		List<JasperPrint> toPrint = new ArrayList<JasperPrint>(slipVendaEncalhes.length);
	        
        for (SlipVendaEncalheDTO slipVendaEncalhe : slipVendaEncalhes) {
        	
        	Map<String, Object> parameters = obterParametrosRelatorioSlipVenda(slipVendaEncalhe,tipoVenda);
        	
    		JRDataSource dataSource = new  JRBeanCollectionDataSource(slipVendaEncalhe.getListaItensSlip());
    		
            toPrint.add(JasperUtil.fillReport(NOME_SLIP_VENDA_IREPORT,parameters,dataSource));
        }
        
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, toPrint);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
        
        try {
            exporter.exportReport();
            return baos.toByteArray();
        } catch (JRException ex) {
   
            throw new RuntimeException(ex);
	    }
	}

	private Map<String, Object> obterParametrosRelatorioSlipVenda(SlipVendaEncalheDTO slipVendaEncalhe, TipoVendaEncalhe tipoVenda){
		
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("NUMERO_COTA", slipVendaEncalhe.getNumeroCota());
		parameters.put("NOME_COTA", slipVendaEncalhe.getNomeCota());
		parameters.put("CODIGO_BOX", slipVendaEncalhe.getNumeroBox());
		parameters.put("DESC_BOX", slipVendaEncalhe.getDescricaoBox());
		parameters.put("DATA_VENDA", slipVendaEncalhe.getData());
		parameters.put("HORA_VENDA", slipVendaEncalhe.getHora());
		parameters.put("USUARIO", slipVendaEncalhe.getUsuario());
		parameters.put("QNT_TOTAL_A_VISTA",slipVendaEncalhe.getQuantidadeTotalVista());
		parameters.put("VALOR_TOTAL_A_VISTA",slipVendaEncalhe.getValorTotalVista());
		parameters.put("QNT_TOTAL_A_PRAZO",slipVendaEncalhe.getQuantidadeTotalPrazo());
		parameters.put("VALOR_TOTAL_A_PRAZO",slipVendaEncalhe.getValorTotalPrazo());
		parameters.put("QNT_TOTAL_GERAL",slipVendaEncalhe.getQuantidadeTotalGeral());
		parameters.put("VALOR_TOTAL_GERAL",slipVendaEncalhe.getValorTotalGeral());
		
		String nomeDistribuidor = this.distribuidorService.obterRazaoSocialDistribuidor();
	
		parameters.put("NOME_DISTRIBUIDOR",nomeDistribuidor);
		parameters.put("LOGO_DISTRIBUIDOR", getLogoDistribuidor());
		
		if(TipoVendaEncalhe.ENCALHE.equals(tipoVenda)){
			
			parameters.put("TITULO_RELATORIO",TITULO_SLIP_ENCALHE);
			parameters.put("TITULO_RELATORIO_COMPROVANTE",TITULO_COMPROVANTE_ENCALHE);
		}
		else{
			
			parameters.put("TITULO_RELATORIO",TITULO_SLIP_SUPLEMENTAR);
			parameters.put("TITULO_RELATORIO_COMPROVANTE",TITULO_COMPROVANTE_SUPLEMENTAR);
		}
		
		if (slipVendaEncalhe.getNumeroSlip() != null) {

			parameters.put("NUM_SLIP", slipVendaEncalhe.getNumeroSlip());
		}
		
		return parameters;
	}

	@Override
	@Transactional(readOnly = true)
	public VendaEncalheDTO buscarVendaEncalhe(Long idVendaEncalhe) {

		VendaEncalheDTO vendaEncalheDTO = vendaProdutoRepository.buscarVendaProdutoEncalhe(idVendaEncalhe);
		
		if(vendaEncalheDTO == null){
			throw new ValidacaoException(TipoMensagem.ERROR,"Venda não encontrada!");
		}
		
		if(vendaEncalheDTO!= null){
			
			ProdutoEdicao produtoEdicao =  
					produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(vendaEncalheDTO.getCodigoProduto(), 
																					 vendaEncalheDTO.getNumeroEdicao().longValue());
			
			if(produtoEdicao!= null){
				
				EstoqueProduto estoqueProduto = estoqueProdutoRespository.buscarEstoquePorProduto(produtoEdicao.getId());
				
				BigInteger qntProduto = getQntProdutoEstoque(vendaEncalheDTO.getTipoVendaEncalhe(), estoqueProduto);
			
				vendaEncalheDTO.setQntDisponivelProduto(qntProduto);
			}
			
		}

		return vendaEncalheDTO;
	}

	private byte[] gerarArquivoSlipVenda(TipoVendaEncalhe tipoVenda,SlipVendaEncalheDTO vendas) {
		
		byte[] relatorio = null;

		try {

			relatorio = this.gerarDocumentoIreport(vendas,PATH_COMPROVANTE_VENDA_IREPORT,tipoVenda);
			
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Erro ao gerar Slip de Venda de Encalhe.");
		}

		return relatorio;
	}

	@Override
	@Transactional
	public byte[] efetivarVendaEncalhe(List<VendaEncalheDTO> vendaEncalheDTO,Long numeroCota, Date dataVencimentoDebito, Usuario usuario) {

		List<VendaProduto> vendasEfetivadasEncalhe = new ArrayList<VendaProduto>();
		List<VendaProduto> vendasEfetivadasSuplementar = new ArrayList<VendaProduto>();
		
		Date horarioVenda = new Date();
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		int qtdDiasEncalheAtrasadoAceitavel = this.distribuidorService.qtdDiasEncalheAtrasadoAceitavel();
		
		for (VendaEncalheDTO vnd : vendaEncalheDTO) {
			
			vnd.setHorarioVenda(horarioVenda);
			
			if(TipoVendaEncalhe.ENCALHE.equals(vnd.getTipoVendaEncalhe())){
				
				vendasEfetivadasEncalhe.add(
						this.processarVendaEncalhe(
								vnd, numeroCota,dataVencimentoDebito, usuario, 
								dataOperacao, qtdDiasEncalheAtrasadoAceitavel));
			}
			else{
				
				vendasEfetivadasSuplementar.add(
						this.processarVendaEncalhe(
								vnd, numeroCota,dataVencimentoDebito, usuario,
								dataOperacao, qtdDiasEncalheAtrasadoAceitavel));
			}
		}
		
		byte[] relatorio = gerarComprovanteVenda(vendasEfetivadasEncalhe,vendasEfetivadasSuplementar);

		return relatorio;
	}

	private byte[] gerarComprovanteVenda(List<VendaProduto> vendasEfetivadasEncalhe,List<VendaProduto> vendasEfetivadasSuplementar) {
		
		byte[] relatorio = null;
		
		if(!vendasEfetivadasEncalhe.isEmpty() && !vendasEfetivadasSuplementar.isEmpty()){
			
			List<JasperPrint> toPrint = new ArrayList<JasperPrint>(2);
	        
			SlipVendaEncalheDTO slipEncalhe =  obterDadosSlipVenda(vendasEfetivadasEncalhe.toArray(new VendaProduto[] {}));
	        Map<String, Object> parametersEncalhe = obterParametrosRelatorioSlipVenda(slipEncalhe,TipoVendaEncalhe.ENCALHE);	
	    	JRDataSource dataSourceEncalhe = new  JRBeanCollectionDataSource(slipEncalhe.getListaItensSlip());
	    	
	    	SlipVendaEncalheDTO slipSuplementar = obterDadosSlipVenda(vendasEfetivadasSuplementar.toArray(new VendaProduto[] {}));
	    	Map<String, Object> parametersSplementar = obterParametrosRelatorioSlipVenda(slipSuplementar,TipoVendaEncalhe.SUPLEMENTAR);	
	    	JRDataSource dataSourceSuplementar = new  JRBeanCollectionDataSource(slipSuplementar.getListaItensSlip());
	    	
	        toPrint.add(JasperUtil.fillReport(NOME_COMPROVANTE_VENDA_IREPORT,parametersEncalhe,dataSourceEncalhe));
	        toPrint.add(JasperUtil.fillReport(NOME_COMPROVANTE_VENDA_IREPORT,parametersSplementar,dataSourceSuplementar));
	        
	        JRPdfExporter exporter = new JRPdfExporter();
	        
	        exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, toPrint);
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        
	        exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
	        
	        try {
	            
	        	exporter.exportReport();	        
	        	relatorio = baos.toByteArray();
	            
	        } catch (JRException ex) {
	   
	        	throw new ValidacaoException(TipoMensagem.ERROR,"Erro no processamento da Venda de Encalhe.");
		    }
		
		}
		else{
			
			if(!vendasEfetivadasEncalhe.isEmpty()){
				
				relatorio = 
						gerarArquivoSlipVenda(TipoVendaEncalhe.ENCALHE,
											  obterDadosSlipVenda(vendasEfetivadasEncalhe.toArray(new VendaProduto[] {})));
			}
			else if (!vendasEfetivadasSuplementar.isEmpty()){
				
				relatorio = 
						gerarArquivoSlipVenda(TipoVendaEncalhe.SUPLEMENTAR,
											  obterDadosSlipVenda(vendasEfetivadasSuplementar.toArray(new VendaProduto[] {})));
			}
		}

		if (relatorio == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro no processamento da Venda de Encalhe.");
		}
		
		return relatorio;
	}

	/**
	 * Processa venda de encalhe em função do seu tipo (Encalhe/Suplementar)
	 * 
	 * @param vnd
	 * @param numeroCota
	 * @param dataVencimentoDebito
	 * @param usuario
	 * @param dataOperacao
	 * @param qtdDiasEncalheAtrasadoAceitavel
	 */
	private VendaProduto processarVendaEncalhe(VendaEncalheDTO vnd,Long numeroCota, Date dataVencimentoDebito, Usuario usuario,
			Date dataOperacao, int qtdDiasEncalheAtrasadoAceitavel) {

		if (TipoVendaEncalhe.ENCALHE.equals(vnd.getTipoVendaEncalhe())) {

			return criarVendaEncalhe(vnd, numeroCota, dataVencimentoDebito,usuario,
					dataOperacao, qtdDiasEncalheAtrasadoAceitavel);
			
		} else {

			return criarVendaSuplementar(vnd, numeroCota, dataVencimentoDebito,usuario,
					dataOperacao, qtdDiasEncalheAtrasadoAceitavel);
		}
	}

	/**
	 * Cria venda de encalhe
	 * 
	 * @param vnd
	 * @param numeroCota
	 * @param dataVencimentoDebito
	 * @param usuario
	 * @param dataOperacao
	 * @param qtdDiasEncalheAtrasadoAceitavel
	 */
	private VendaProduto criarVendaEncalhe(VendaEncalheDTO vnd,Long numeroCota, Date dataVencimentoDebito, Usuario usuario,
			Date dataOperacao, int qtdDiasEncalheAtrasadoAceitavel) {

		ProdutoEdicao produtoEdicao =
				produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(vnd.getCodigoProduto(), vnd.getNumeroEdicao());
		
		if (isVendaConsignadoCota(produtoEdicao, dataOperacao, qtdDiasEncalheAtrasadoAceitavel) && isConsignadoVendaEncalhe(produtoEdicao)) {

			return criarVendaEncalheConsignado(vnd, numeroCota,dataVencimentoDebito, usuario, produtoEdicao);
			
		} else {

			return criarVendaEncalheContaFirme(vnd, numeroCota, dataVencimentoDebito,usuario, produtoEdicao);
			
		}
		
	}
	
	private VendaProduto criarVendaEncalheConsignado(VendaEncalheDTO vnd,Long numeroCota, Date dataVencimentoDebito, 
			 									 	 Usuario usuario,ProdutoEdicao produtoEdicao) {
		
		VendaProduto vendaProduto = getVendaProduto(vnd, numeroCota, usuario,dataVencimentoDebito, produtoEdicao);

		BigInteger qntProduto = vendaProduto.getQntProduto();

		MovimentoEstoqueCota movimentoEstoqueCota =
						 gerarMovimentoCompraConsignadoCota(produtoEdicao.getId(), vendaProduto.getCota().getId(), 
															usuario.getId(), qntProduto,TipoVendaEncalhe.ENCALHE);
		
		movimentoEstoqueCota.setValoresAplicados(vendaProduto.getValoresAplicados());
		movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
		
		MovimentoEstoque movimentoEstoque = gerarMovimentoEstoqueVendaEncalheDistribuidor(vnd, usuario, produtoEdicao);
		
		gerarMovimentoChamadaEncalheCota(produtoEdicao, vendaProduto.getCota(),qntProduto);

		vendaProduto.setMovimentoEstoque(new HashSet<MovimentoEstoque>());
		vendaProduto.getMovimentoEstoque().add(movimentoEstoque);
		vendaProduto.setTipoComercializacaoVenda(FormaComercializacao.CONSIGNADO);
		vendaProduto.setTipoVenda(TipoVendaEncalhe.ENCALHE);

		return vendaProdutoRepository.merge(vendaProduto);
	}
	
	private ValoresAplicados obterValoresAplicados(Cota cota, ProdutoEdicao produtoEdicao){
		
		BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
		
		BigDecimal percentualDesconto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null,cota, produtoEdicao);
		
		BigDecimal valorDoDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);
		
		ValoresAplicados valoresAplicados = new ValoresAplicados();
		
		valoresAplicados.setValorDesconto(percentualDesconto);
		valoresAplicados.setPrecoComDesconto(precoVenda.subtract(valorDoDesconto));
		valoresAplicados.setPrecoVenda(produtoEdicao.getPrecoVenda());
		
		return valoresAplicados;
	}
	
	private VendaProduto criarVendaEncalheContaFirme(VendaEncalheDTO vnd,Long numeroCota, Date dataVencimentoDebito, 
												 Usuario usuario,ProdutoEdicao produtoEdicao) {
		
		VendaProduto vendaProduto = getVendaProduto(vnd, numeroCota, usuario,dataVencimentoDebito, produtoEdicao);

		MovimentoEstoque movimentoEstoque = gerarMovimentoEstoqueVendaEncalheDistribuidor(vnd, usuario, produtoEdicao);

		List<MovimentoFinanceiroCota> movimentoFinanceiro = gerarMovimentoFinanceiroCotaDebito(dataVencimentoDebito, vendaProduto);
		
		vendaProduto.setMovimentoEstoque(new HashSet<MovimentoEstoque>());
		vendaProduto.getMovimentoEstoque().add(movimentoEstoque);
		vendaProduto.setMovimentoFinanceiro(new HashSet<MovimentoFinanceiroCota>());
		vendaProduto.getMovimentoFinanceiro().addAll(movimentoFinanceiro);
		vendaProduto.setTipoComercializacaoVenda(FormaComercializacao.CONTA_FIRME);
		vendaProduto.setTipoVenda(TipoVendaEncalhe.ENCALHE);

		return vendaProdutoRepository.merge(vendaProduto);
	}

	private MovimentoEstoque gerarMovimentoEstoqueVendaEncalheDistribuidor(VendaEncalheDTO vnd, Usuario usuario, ProdutoEdicao produtoEdicao) {
		
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.VENDA_ENCALHE);

		if (tipoMovimento == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,"Não foi encontrado tipo de movimento de estoque para venda de encalhe!");
		}

		MovimentoEstoque movimentoEstoque =
				movimentoEstoqueService.gerarMovimentoEstoque(produtoEdicao.getId(), usuario.getId(),vnd.getQntProduto(), tipoMovimento);
		
		return movimentoEstoque;
	}

	/**
	 * Cria venda de encalhe suplementar
	 * 
	 * @param vnd
	 * @param numeroCota
	 * @param dataVencimentoDebito
	 * @param usuario
	 */
	private VendaProduto criarVendaSuplementar(VendaEncalheDTO vnd,Long numeroCota, 
			Date dataVencimentoDebito, Usuario usuario, Date dataOperacao, int qtdDiasEncalheAtrasadoAceitavel) {

		ProdutoEdicao produtoEdicao = 
				produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(vnd.getCodigoProduto(), vnd.getNumeroEdicao());
			
		if (isVendaSuplementarConsignadoCota(produtoEdicao, dataOperacao, qtdDiasEncalheAtrasadoAceitavel)) {

			return criarVendaSuplementarConsignado(vnd, numeroCota,dataVencimentoDebito, usuario, produtoEdicao);
			
		} else {

			return criarVendaSuplementarContaFirme(vnd, numeroCota,dataVencimentoDebito, usuario, produtoEdicao);
		}
	}

	/**
	 * Cria uam venda de encalhe suplementar com pagamento a vista
	 * 
	 * @param vnd
	 * @param numeroCota
	 * @param dataVencimentoDebito
	 * @param usuario
	 * @param produtoEdicao
	 */
	private VendaProduto criarVendaSuplementarContaFirme(VendaEncalheDTO vnd,Long numeroCota, Date dataVencimentoDebito, 
													 Usuario usuario,ProdutoEdicao produtoEdicao) {
		
		
		VendaProduto vendaProduto = getVendaProduto(vnd, numeroCota, usuario,dataVencimentoDebito, produtoEdicao);

		MovimentoEstoque movimentoEstoque = 
				gerarMovimentoEstoqueVendaSuplementarDistribuidor(produtoEdicao.getId(), vendaProduto.getCota().getId(),
																  usuario.getId(),vendaProduto.getQntProduto());
		
		List<MovimentoFinanceiroCota> movimentoFinanceiro = 
				gerarMovimentoFinanceiroCotaDebito(dataVencimentoDebito, vendaProduto);
		
		vendaProduto.setMovimentoEstoque(new HashSet<MovimentoEstoque>());
		vendaProduto.getMovimentoEstoque().add(movimentoEstoque);
		vendaProduto.setMovimentoFinanceiro(new HashSet<MovimentoFinanceiroCota>());
		vendaProduto.getMovimentoFinanceiro().addAll(movimentoFinanceiro);
		vendaProduto.setTipoComercializacaoVenda(FormaComercializacao.CONTA_FIRME);
		vendaProduto.setTipoVenda(TipoVendaEncalhe.SUPLEMENTAR);

		return vendaProdutoRepository.merge(vendaProduto);
	}

	/**
	 * Cria uma venda de encalhe suplementar consignado
	 * 
	 * @param vnd
	 * @param numeroCota
	 * @param dataVencimentoDebito
	 * @param usuario
	 * @param produtoEdicao
	 */
	private VendaProduto criarVendaSuplementarConsignado(VendaEncalheDTO vnd,Long numeroCota, Date dataVencimentoDebito, 
														 Usuario usuario,ProdutoEdicao produtoEdicao) {
		
		VendaProduto vendaProduto = getVendaProduto(vnd, numeroCota, usuario,dataVencimentoDebito, produtoEdicao);
		
		BigInteger qntProduto = vendaProduto.getQntProduto();
		
		MovimentoEstoqueCota movimentoEstoqueCota = 
				gerarMovimentoCompraConsignadoCota(produtoEdicao.getId(), vendaProduto.getCota().getId(), 
										   		   usuario.getId(), qntProduto,TipoVendaEncalhe.SUPLEMENTAR);
		
		movimentoEstoqueCota.setValoresAplicados(vendaProduto.getValoresAplicados());
		movimentoEstoqueCota = movimentoEstoqueCotaRepository.merge(movimentoEstoqueCota);
		
		MovimentoEstoque movimentoEstoque = 
				gerarMovimentoEstoqueVendaSuplementarDistribuidor(produtoEdicao.getId(), vendaProduto.getCota().getId(),
																  usuario.getId(), qntProduto);

		gerarMovimentoChamadaEncalheCota(produtoEdicao, vendaProduto.getCota(),qntProduto);

		vendaProduto.setMovimentoEstoque(new HashSet<MovimentoEstoque>());
		vendaProduto.getMovimentoEstoque().add(movimentoEstoque);
		vendaProduto.setTipoComercializacaoVenda(FormaComercializacao.CONSIGNADO);
		vendaProduto.setTipoVenda(TipoVendaEncalhe.SUPLEMENTAR);

		return vendaProdutoRepository.merge(vendaProduto);
	}

	/**
	 * Cria movimento financeiro da cota da venda de produto informada.
	 * 
	 * @param dataVencimentoDebito
	 * @param vendaProduto
	 */
	private List<MovimentoFinanceiroCota> gerarMovimentoFinanceiroCotaDebito(Date dataVencimentoDebito, VendaProduto vendaProduto) {

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = 
				tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.COMPRA_ENCALHE_SUPLEMENTAR);

		if (tipoMovimentoFinanceiro == null) {
			throw new ValidacaoException(
					TipoMensagem.ERROR,
					"Não foi encontrado tipo de movimento financeiro de DEBITO cadastrado no sistema!");
		}

		List<MovimentoFinanceiroCota> movimentoFinanceiro = 
				criarMovimentoFinanceiroDebito(tipoMovimentoFinanceiro, dataVencimentoDebito,
												vendaProduto.getValorTotalVenda(), vendaProduto.getCota(),
												vendaProduto.getUsuario());
		
		return movimentoFinanceiro;
	}

	/**
	 * 
	 * Cria movimento de estoque do distribuidor, com o tipo de movimento
	 * VENDA_ENCALHE_SUPLEMENTAR
	 * 
	 * @param idProdutoEdicao
	 * @param idCota
	 * @param idUsuario
	 * @param qntProduto
	 */
	private MovimentoEstoque gerarMovimentoEstoqueVendaSuplementarDistribuidor(Long idProdutoEdicao, Long idCota, Long idUsuario,BigInteger qntProduto) {

		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR);

		if (tipoMovimento == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Não foi encontrado tipo de movimento de estoque para venda de encalhe suplementar!");
		}

		MovimentoEstoque movimentoEstoque = 
				movimentoEstoqueService.gerarMovimentoEstoque(idProdutoEdicao, idUsuario, qntProduto,tipoMovimento);

		return movimentoEstoque;
	}
	
	/**
	 * Cria movimento de estoque da cota, com o tipo de movimento
	 * COMPRA_SUPLEMENTAR
	 * 
	 * @param idProdutoEdicao
	 * @param idCota
	 * @param idUsuario
	 * @param qntProduto
	 */
	private MovimentoEstoqueCota gerarMovimentoCompraConsignadoCota(Long idProdutoEdicao,Long idCota, Long idUsuario, BigInteger qntProduto, TipoVendaEncalhe tipoVenda) {

		GrupoMovimentoEstoque grupoMovimentoEstoque = null;
		
		if(TipoVendaEncalhe.SUPLEMENTAR.equals(tipoVenda)){
			
			grupoMovimentoEstoque = GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR;
		}
		else{
			
			grupoMovimentoEstoque = GrupoMovimentoEstoque.COMPRA_ENCALHE;
		}
		
		TipoMovimentoEstoque tipoMovimentoEstoqueCota = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(grupoMovimentoEstoque);

		if (tipoMovimentoEstoqueCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Não foi encontrado tipo de movimento de estoque para compra de encalhe suplementar!");
		}

		return movimentoEstoqueService.gerarMovimentoCota(null, idProdutoEdicao,idCota, idUsuario, qntProduto, tipoMovimentoEstoqueCota);
	}

	/**
	 * Cria movimento de estoque da cota, com o tipo de movimento
	 * COMPRA_SUPLEMENTAR
	 * 
	 * @param idProdutoEdicao
	 * @param idCota
	 * @param idUsuario
	 * @param qntProduto
	 */
	private void gerarMovimentoEstornoCompraConsignadoCota(Long idProdutoEdicao,Long idCota, Long idUsuario, BigInteger qntProduto, TipoVendaEncalhe tipoVenda) {
		
		GrupoMovimentoEstoque grupoMovimentoEstorno = null;
		
		if(TipoVendaEncalhe.SUPLEMENTAR.equals(tipoVenda)){
			
			grupoMovimentoEstorno = GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR;
		}
		else{
			
			grupoMovimentoEstorno = GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE;
		}
		
		TipoMovimentoEstoque tipoMovimentoEstoqueCota = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(grupoMovimentoEstorno);

		if (tipoMovimentoEstoqueCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Não foi encontrado tipo de movimento de estoque para estorno de compra de encalhe suplementar!");
		}

		movimentoEstoqueService.gerarMovimentoCota(null, idProdutoEdicao,idCota, idUsuario, qntProduto, tipoMovimentoEstoqueCota);
	}

	/**
	 * Cria movimento na chamada de encalhe da cota atualizando a quantidade de
	 * produto a ser conferida
	 * 
	 * @param produtoEdicao
	 * @param cota
	 * @param qntProduto
	 */
	private void gerarMovimentoChamadaEncalheCota(ProdutoEdicao produtoEdicao,Cota cota, BigInteger qntProduto) {

		ChamadaEncalhe chamadaEncalhe = 
				chamadaEncalheRepository.obterPorNumeroEdicaoEMaiorDataRecolhimento(produtoEdicao,TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		if(chamadaEncalhe == null){
			return;
		}
		
		ChamadaEncalheCota chamadaEncalheCota = 
				chamadaEncalheCotaRepository.buscarPorChamadaEncalheECota(chamadaEncalhe.getId(),cota.getId());

		if (chamadaEncalheCota == null) {
			chamadaEncalheCota = new ChamadaEncalheCota();
			chamadaEncalheCota.setCota(cota);
			chamadaEncalheCota.setPostergado(false);
			chamadaEncalheCota.setFechado(false);
			chamadaEncalheCota.setQtdePrevista(BigInteger.ZERO);
			chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
		}

		BigInteger qntPrevista = chamadaEncalheCota.getQtdePrevista();
		chamadaEncalheCota.setQtdePrevista(qntPrevista.add(qntProduto));

		chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
	}

	/**
	 * Verifica se ja foi realizado conferencia de encalhe para o produto edição
	 * na data de operação
	 * 
	 * @param produtoEdicao - produto edição
	 * 
	 * @return boolean
	 */
	private boolean isVendaConsignadoCota(ProdutoEdicao produtoEdicao, Date dataOperacao, 
			int qtdDiasEncalheAtrasadoAceitavel) {
		
		ChamadaEncalhe chamadaEncalhe = 
				chamadaEncalheRepository.obterPorNumeroEdicaoEMaiorDataRecolhimento(produtoEdicao,TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		if (chamadaEncalhe == null) {
			return false;
		}

		Date dataPermitidaParaConsignado = 
				DateUtil.adicionarDias(chamadaEncalhe.getDataRecolhimento(), qtdDiasEncalheAtrasadoAceitavel);

		return (dataOperacao.compareTo(dataPermitidaParaConsignado) < 0);
	}

	private VendaProduto getVendaProduto(VendaEncalheDTO vendaDTO,Long numeroCota, 
										Usuario usuario, Date dataVencimentoDebito,
										ProdutoEdicao produtoEdicao) {

		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota.intValue());
		
		ValoresAplicados valoresAplicados = this.obterValoresAplicados(cota, produtoEdicao);
		
		VendaProduto venda = new VendaProduto();

		venda.setCota(cota);
		venda.setProdutoEdicao(produtoEdicao);
		venda.setUsuario(getUsuarioSincronizado(usuario.getId()));
		venda.setDataVencimentoDebito(dataVencimentoDebito);
		venda.setDataVenda(new Date());
		venda.setHorarioVenda(vendaDTO.getHorarioVenda());
		venda.setQntProduto(vendaDTO.getQntProduto());
		venda.setTipoVenda(vendaDTO.getTipoVendaEncalhe());
		venda.setValorTotalVenda(valoresAplicados.getPrecoComDesconto().multiply(new BigDecimal(vendaDTO.getQntProduto())));
		venda.setValoresAplicados(valoresAplicados);
		
		return venda;
	}

	@Override
	@Transactional
	public void excluirVendaEncalhe(Long idVendaEncalhe) {

		VendaProduto vendaProduto = vendaProdutoRepository.buscarPorId(idVendaEncalhe);
		
		if(cobrancaService.verificarCobrancasGeradasNaDataVencimentoDebito(vendaProduto.getDataVencimentoDebito(), vendaProduto.getCota().getId())){
			throw new ValidacaoException(TipoMensagem.WARNING,"Venda não pode ser excluida! Já foi gerado cobrança para venda.");
		}
		
		MovimentoEstoque movimento = 
					processarAtualizcaoMovimentoEstoqueDistribuidor(vendaProduto.getQntProduto(), 
																	BigInteger.ZERO, 
																	vendaProduto.getProdutoEdicao().getId(), 
																	vendaProduto.getUsuario().getId(), 
																	vendaProduto.getTipoVenda());
		
		if (FormaComercializacao.CONSIGNADO.equals(vendaProduto.getTipoComercializacaoVenda())) {
			
			// Atualiza estoque da cota
			gerarMovimentoEstornoCompraConsignadoCota(vendaProduto.getProdutoEdicao().getId(), vendaProduto.getCota().getId(),
												   	  vendaProduto.getUsuario().getId(),vendaProduto.getQntProduto(),
												      vendaProduto.getTipoVenda());

			// Atualiza a chamada de encalhe do produto edição referente a cota
			processaAtualizacaoChamadaEncalheCotaVendaCancelada(vendaProduto);
		}

		vendaProduto.getMovimentoEstoque().add(movimento);
		
		vendaProduto.setMovimentoEstoque(null);
		vendaProduto.setMovimentoFinanceiro(null);
		vendaProdutoRepository.remover(vendaProduto);
	}

	/**
	 * Atualiza a chamada de encalhe do produto edição da cota refrente uma
	 * venda cancelada
	 * 
	 * @param vendaProduto
	 */
	private void processaAtualizacaoChamadaEncalheCotaVendaCancelada(VendaProduto vendaProduto) {

		ChamadaEncalhe chamadaEncalhe = 
				chamadaEncalheRepository.obterPorNumeroEdicaoEMaiorDataRecolhimento(vendaProduto.getProdutoEdicao(),
																					TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		if(chamadaEncalhe == null){
			return;
		}
		
		ChamadaEncalheCota chamadaEncalheCota = 
				chamadaEncalheCotaRepository.buscarPorChamadaEncalheECota(chamadaEncalhe.getId(),vendaProduto.getCota().getId());

		if (chamadaEncalheCota != null) {

			BigInteger qnProduto = chamadaEncalheCota.getQtdePrevista().subtract(vendaProduto.getQntProduto());

			if (BigInteger.ZERO.compareTo(qnProduto) == 0) {

				chamadaEncalheCotaRepository.remover(chamadaEncalheCota);
			} else {

				BigInteger qntPrevistaChamadaEncalhe = chamadaEncalheCota.getQtdePrevista();

				qntPrevistaChamadaEncalhe = qntPrevistaChamadaEncalhe.subtract(vendaProduto.getQntProduto());
				
				chamadaEncalheCota.setQtdePrevista(qntPrevistaChamadaEncalhe);

				chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
			}
		}
	}

	/**
	 * Atualiza a chamada de encalhe do produto edição da cota refrente a edição
	 * de uma venda
	 * 
	 * @param vendaProduto
	 * @param qntProdutoNovo
	 */
	private void processarAtualizacaoChamadaEncalheCota(VendaProduto vendaProduto, BigInteger qntProdutoNovo) {

		ChamadaEncalhe chamadaEncalhe = 
				chamadaEncalheRepository.obterPorNumeroEdicaoEMaiorDataRecolhimento(vendaProduto.getProdutoEdicao(),TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		if(chamadaEncalhe == null){
			return;
		}
		
		ChamadaEncalheCota chamadaEncalheCota = 
				chamadaEncalheCotaRepository.buscarPorChamadaEncalheECota(chamadaEncalhe.getId(),vendaProduto.getCota().getId());

		if (chamadaEncalheCota != null) {

			BigInteger qnProduto = chamadaEncalheCota.getQtdePrevista().subtract(vendaProduto.getQntProduto());
			
			qnProduto = qnProduto.add(qntProdutoNovo);

			chamadaEncalheCota.setQtdePrevista(qnProduto);
			
			chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
		}
	}

	@Override
	@Transactional
	public byte[] alterarVendaEncalhe(VendaEncalheDTO vendaEncalheDTO,Date dataVencimentoDebito, Usuario usuario) {

		VendaProduto vendaProduto = vendaProdutoRepository.buscarPorId(vendaEncalheDTO.getIdVenda());

		ProdutoEdicao produtoEdicao = vendaProduto.getProdutoEdicao();

		BigInteger qntAtualProduto = vendaProduto.getQntProduto();
		
		BigInteger qntNovaProduto = vendaEncalheDTO.getQntProduto();

		BigDecimal valorVendaAtual = vendaProduto.getValorTotalVenda();

		BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
		
		BigDecimal percentualDesconto = 
				descontoService.obterValorDescontoPorCotaProdutoEdicao(null,vendaProduto.getCota(), produtoEdicao);
		
		BigDecimal valorDesconto = 
				MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);

		BigDecimal valorVendaNovo = 
				precoVenda.subtract(valorDesconto).multiply(new BigDecimal(qntNovaProduto));

		if (qntAtualProduto.compareTo(qntNovaProduto) != 0) {

			MovimentoEstoque movimento = 
						processarAtualizcaoMovimentoEstoqueDistribuidor(qntAtualProduto, qntNovaProduto, 
																		vendaProduto.getProdutoEdicao().getId(), 
																		usuario.getId(),
																		vendaProduto.getTipoVenda());
			
			vendaProduto.getMovimentoEstoque().add(movimento);
			
			if(FormaComercializacao.CONTA_FIRME.equals(vendaProduto.getTipoComercializacaoVenda())){
				
				List<MovimentoFinanceiroCota> movimentoFinanceiro = 
						processarMovimentoFinanceiro(valorVendaAtual, valorVendaNovo, vendaProduto, usuario);
				
				vendaProduto.getMovimentoFinanceiro().addAll(movimentoFinanceiro);
				
			}else {

				processarAtualizacaoMovimentoEstoqueCota(qntAtualProduto,qntNovaProduto, vendaProduto.getProdutoEdicao().getId(), 
														usuario.getId(), vendaProduto.getCota().getId(), vendaProduto.getTipoVenda());

				processarAtualizacaoChamadaEncalheCota(vendaProduto,qntNovaProduto);
			}
		}
		
		vendaProduto.setQntProduto(vendaEncalheDTO.getQntProduto());
		vendaProduto.setValorTotalVenda(valorVendaNovo);
		vendaProduto.setDataVencimentoDebito(dataVencimentoDebito);
		vendaProduto.setDataVenda(new Date());
		vendaProduto.setHorarioVenda(new Date());
		vendaProduto.setUsuario(getUsuarioSincronizado(usuario.getId()));

		vendaProduto = vendaProdutoRepository.merge(vendaProduto);

		byte[] relatorio = 
				gerarArquivoSlipVenda(vendaProduto.getTipoVenda(),obterDadosSlipVenda(vendaProduto));

		if (relatorio == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro no processamento da Venda de Encalhe.");
		}

		return relatorio;
	}

	private List<MovimentoFinanceiroCota> processarMovimentoFinanceiro(BigDecimal valorVendaAtual, BigDecimal valorVendaNovo,
																	   VendaProduto vendaProduto, Usuario usuario) {

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = null;
		BigDecimal valorVendaDebitoCredito = BigDecimal.ZERO;

		// Se o valor atual da venda for menor que o valor novo gerar debito
		if (valorVendaNovo.compareTo(valorVendaAtual) > 0) {

			valorVendaDebitoCredito = valorVendaNovo.subtract(valorVendaAtual);

			tipoMovimentoFinanceiro = 
					tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.DEBITO);
		}
		// Se o valor atual da venda for menor que o valor novo gerar credito
		else if (valorVendaNovo.compareTo(valorVendaAtual) < 0) {

			valorVendaDebitoCredito = valorVendaAtual.subtract(valorVendaNovo);

			tipoMovimentoFinanceiro = 
					tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.CREDITO);
		}

		if (tipoMovimentoFinanceiro == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Não foi encontrado tipo de movimento financeiro de DEBITO/CREDITO cadastrado no sistema!");
		}

		List<MovimentoFinanceiroCota> movimento = 
				criarMovimentoFinanceiroDebito(tipoMovimentoFinanceiro,vendaProduto.getDataVencimentoDebito(),
												valorVendaDebitoCredito, vendaProduto.getCota(), usuario);
		
		return movimento;
	}

	private Usuario getUsuarioSincronizado(Long idUsuario) {

		return usuarioRepository.buscarPorId(idUsuario);
	}

	private List<MovimentoFinanceiroCota> criarMovimentoFinanceiroDebito(TipoMovimentoFinanceiro tipoMovimentoFinanceiro,
																		Date dataVencimentoDebito, BigDecimal valorTotalVenda, Cota cota,
																		Usuario usuario) {

		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();

		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();

		movimentoFinanceiroCotaDTO.setCota(cota);
		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
		movimentoFinanceiroCotaDTO.setUsuario(usuario);
		movimentoFinanceiroCotaDTO.setValor(valorTotalVenda);
		movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacao);
		movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
		movimentoFinanceiroCotaDTO.setDataVencimento(dataVencimentoDebito);
		movimentoFinanceiroCotaDTO.setDataAprovacao(dataOperacao);
		movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacao);
		movimentoFinanceiroCotaDTO.setObservacao("Venda de Encalhe");
		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
		movimentoFinanceiroCotaDTO.setLancamentoManual(true);
		movimentoFinanceiroCotaDTO.setFornecedor( (cota.getParametroCobranca()!= null)? cota.getParametroCobranca().getFornecedorPadrao():null);

		return movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
	}

	private MovimentoEstoque processarAtualizcaoMovimentoEstoqueDistribuidor(BigInteger qntProdutoAtual, BigInteger qntProdutoNovo,
																 Long idProdutoEdicao, Long idUsuario, TipoVendaEncalhe tipoVenda) {

		BigInteger quantidadeProdutoAlterada = BigInteger.ZERO;
		TipoMovimentoEstoque tipoMovimento = null;
		
		GrupoMovimentoEstoque grupoMovimentoEstrnoVenda = null;
		GrupoMovimentoEstoque grupoMovimentoVenda= null;
		
		if(TipoVendaEncalhe.SUPLEMENTAR.equals(tipoVenda)){
			
			grupoMovimentoEstrnoVenda = GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR;
			grupoMovimentoVenda = GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR;
		}
		else{
			
			grupoMovimentoEstrnoVenda = GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE;
			grupoMovimentoVenda = GrupoMovimentoEstoque.VENDA_ENCALHE;
		}
		
		// Se a quantidade de produto nova informada for maior que a quantidade
		// atual, gera movimento de venda de encalhe
		if (qntProdutoNovo.compareTo(qntProdutoAtual) > 0) {

			quantidadeProdutoAlterada = qntProdutoNovo.subtract(qntProdutoAtual);

			tipoMovimento = 
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(grupoMovimentoVenda);
		}

		// Se a quantidade de produto nova informada for menor que a quantidade
		// atual, gera movimento de estorno de venda de encalhe
		else if (qntProdutoNovo.compareTo(qntProdutoAtual) < 0) {

			quantidadeProdutoAlterada = qntProdutoAtual.subtract(qntProdutoNovo);

			tipoMovimento = 
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(grupoMovimentoEstrnoVenda);
		}

		if (tipoMovimento == null) {
			throw new ValidacaoException(
					TipoMensagem.ERROR,
					"Não foi encontrado tipo de movimento de estoque para venda e estorno de encalhe!");
		}

		MovimentoEstoque movimentoEstoque = 
				movimentoEstoqueService.gerarMovimentoEstoque(idProdutoEdicao, idUsuario,quantidadeProdutoAlterada, tipoMovimento);

		return movimentoEstoque;
	}

	private void processarAtualizacaoMovimentoEstoqueCota(BigInteger qntProdutoAtual, BigInteger qntProdutoNovo,
														  Long idProdutoEdicao, Long idUsuario, Long idCota, TipoVendaEncalhe tipoVenda) {
		
		BigInteger quantidadeProdutoAlterada = BigInteger.ZERO;
		TipoMovimentoEstoque tipoMovimento = null;
		// Se a quantidade de produto nova informada for maior que a quantidade
		// atual, gera movimento de venda de encalhe
		
		GrupoMovimentoEstoque grupoMovimentoEstrnoCompra = null;
		GrupoMovimentoEstoque grupoMovimentoCompra= null;
		
		if(TipoVendaEncalhe.SUPLEMENTAR.equals(tipoVenda)){
			
			grupoMovimentoEstrnoCompra = GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR;
			grupoMovimentoCompra = GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR;
		}
		else{
			
			grupoMovimentoEstrnoCompra = GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE;
			grupoMovimentoCompra = GrupoMovimentoEstoque.COMPRA_ENCALHE;
		}
		
		if (qntProdutoNovo.compareTo(qntProdutoAtual) > 0) {

			quantidadeProdutoAlterada = qntProdutoNovo.subtract(qntProdutoAtual);
			
			tipoMovimento = 
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(grupoMovimentoCompra);
		}

		// Se a quantidade de produto nova informada for menor que a quantidade
		// atual, gera movimento de estorno de venda de encalhe
		else if (qntProdutoNovo.compareTo(qntProdutoAtual) < 0) {

			quantidadeProdutoAlterada = qntProdutoAtual.subtract(qntProdutoNovo);
			
			tipoMovimento = 
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(grupoMovimentoEstrnoCompra);
		}

		if (tipoMovimento == null) {
			throw new ValidacaoException(
					TipoMensagem.ERROR,
					"Não foi encontrado tipo de movimento de estoque para compra e estorno de encalhe suplementar!");
		}

		movimentoEstoqueService.gerarMovimentoCota(null, idProdutoEdicao,idCota, idUsuario, quantidadeProdutoAlterada, tipoMovimento);
	}

	@Override
	@Transactional(readOnly=true)
	public VendaEncalheDTO buscarProdutoComEstoque(String codigoProduto,Long numeroEdicao, Long numeroCota){
		
		VendaEncalheDTO vendaEncalheDTO = new VendaEncalheDTO();
		
		ProdutoEdicao produtoEdicao  =  produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		if(produtoEdicao!= null){
			
			EstoqueProduto estoqueProduto = estoqueProdutoRespository.buscarEstoquePorProduto(produtoEdicao.getId());
			
			if(estoqueProduto == null){
				throw new ValidacaoException(TipoMensagem.WARNING,"Não existe produto disponível em estoque para venda!");
			}
			
			TipoVendaEncalhe tipoVendaEncalhe= this.obterTipoVenda(estoqueProduto);
			
			BigInteger qntProduto = this.getQntProdutoEstoque(tipoVendaEncalhe, estoqueProduto);
			
			if(qntProduto!= null && qntProduto.compareTo(BigInteger.ZERO)>0){
			    
				vendaEncalheDTO.setQntDisponivelProduto(qntProduto);
				vendaEncalheDTO.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
				vendaEncalheDTO.setNomeProduto(produtoEdicao.getProduto().getNome());
				vendaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
				
				Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota.intValue());
				
				BigDecimal descontoProduto = descontoService.obterValorDescontoPorCotaProdutoEdicao(null, cota, produtoEdicao);
		
				BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
        
				BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, descontoProduto);
				
				vendaEncalheDTO.setPrecoDesconto(precoVenda.subtract(valorDesconto));				
				vendaEncalheDTO.setCodigoBarras(produtoEdicao.getCodigoDeBarras());
				vendaEncalheDTO.setFormaVenda(this.obterFormaComercializacaoVenda(produtoEdicao,tipoVendaEncalhe));
				vendaEncalheDTO.setTipoVendaEncalhe(tipoVendaEncalhe);
			}
			else{
				throw new ValidacaoException(TipoMensagem.WARNING,"Não existe produto disponível em estoque para venda!");
			}
		}

		return vendaEncalheDTO;
	}

	private FormaComercializacao obterFormaComercializacaoVenda(ProdutoEdicao produtoEdicao,TipoVendaEncalhe tipoVendaEncalhe) {
		
		FormaComercializacao formaComercializacao = null;
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		int qtdDiasEncalheAtrasadoAceitavel = this.distribuidorService.qtdDiasEncalheAtrasadoAceitavel();
		
		if(TipoVendaEncalhe.SUPLEMENTAR.equals(tipoVendaEncalhe)){
			
			if (isVendaSuplementarConsignadoCota(produtoEdicao, dataOperacao, qtdDiasEncalheAtrasadoAceitavel)){
				
				formaComercializacao = FormaComercializacao.CONSIGNADO;
			}
			else{
				
				formaComercializacao = FormaComercializacao.CONTA_FIRME;
			}
		}
		else{
			
			if(this.isConsignadoVendaEncalhe(produtoEdicao) && 
					this.isVendaConsignadoCota(produtoEdicao, dataOperacao, qtdDiasEncalheAtrasadoAceitavel)){
				
				formaComercializacao = FormaComercializacao.CONSIGNADO;
				
			}else{
				
				formaComercializacao = FormaComercializacao.CONTA_FIRME;
			}
		}
		
		return formaComercializacao;
	}
	
	private boolean isVendaSuplementarConsignadoCota(ProdutoEdicao produtoEdicao, 
			Date dataOperacao, int qtdDiasEncalheAtrasadoAceitavel){
		
		List<ChamadaEncalhe> chamadas = 
				chamadaEncalheRepository.obterChamadaEncalhePorProdutoEdicao(
						produtoEdicao, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO) ;
		
		if(chamadas.isEmpty()){
			return true;
		}
		
		return isVendaConsignadoCota(produtoEdicao, dataOperacao, qtdDiasEncalheAtrasadoAceitavel);
	}
	
	private boolean isConsignadoVendaEncalhe(ProdutoEdicao produtoEdicao) {
		
		if(!produtoEdicao.isParcial()){
			return false;
		}
		
		Lancamento lancamento =  lancamentoService.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
		
		if(lancamento == null || lancamento.getPeriodoLancamentoParcial() == null){
			return false;
		}
		
		PeriodoLancamentoParcial periodo = lancamento.getPeriodoLancamentoParcial();
		
		if(TipoLancamentoParcial.FINAL.equals(periodo.getTipo())){
			return false;
		}
		
		return true;
	}

	private TipoVendaEncalhe obterTipoVenda(EstoqueProduto estoqueProduto){
		
		if(estoqueProduto.getQtdeSuplementar() == null){
			return  TipoVendaEncalhe.ENCALHE;
		}
		
		if(estoqueProduto.getQtdeSuplementar().compareTo(BigInteger.ZERO) <= 0){
			
			return  TipoVendaEncalhe.ENCALHE;		
				
		}
		else{
				
			return TipoVendaEncalhe.SUPLEMENTAR;
		}

	}
	
	private BigInteger getQntProdutoEstoque(TipoVendaEncalhe tipoVendaEncalhe, EstoqueProduto estoqueProduto){
		
		BigInteger qntProduto =  BigInteger.ZERO;
		
		if(estoqueProduto!= null ){
			
			if(TipoVendaEncalhe.ENCALHE.equals(tipoVendaEncalhe)
					&& estoqueProduto.getQtdeDevolucaoEncalhe()!= null
					&& estoqueProduto.getQtdeDevolucaoEncalhe().compareTo(BigInteger.ZERO) > 0){
			
				qntProduto = estoqueProduto.getQtdeDevolucaoEncalhe();
			}
			else if (TipoVendaEncalhe.SUPLEMENTAR.equals(tipoVendaEncalhe)
					&& estoqueProduto.getQtdeSuplementar()!= null
					&& estoqueProduto.getQtdeSuplementar().compareTo(BigInteger.ZERO) > 0){
				
				qntProduto = estoqueProduto.getQtdeSuplementar();
			}
		}

		return qntProduto;
	}

	@Override
	@Transactional
	public byte[] geraImpressaoVenda(FiltroVendaEncalheDTO filtro) {
		
		List<VendaProduto> vendasAgrupadasCotaPeriodo = vendaProdutoRepository.buscarCotaPeriodoVenda(filtro);
		
		List<SlipVendaEncalheDTO> itensRelatorio = new ArrayList<>(); 
		
		for(VendaProduto item : vendasAgrupadasCotaPeriodo){
			
			filtro.setNumeroCota(item.getCota().getNumeroCota());
			filtro.setHorarioVenda(item.getHorarioVenda());
			filtro.setPeriodoInicial(item.getDataVenda());
			filtro.setPeriodoFinal(item.getDataVenda());
			
			List<VendaProduto> vendas = vendaProdutoRepository.buscarVendasEncalhe(filtro);
			
			if (vendas != null && !vendas.isEmpty()) {
				
				SlipVendaEncalheDTO slipVendaEncalheDTO = this.obterDadosSlipVenda(vendas.toArray(new VendaProduto[] {}));
				
				Long numeroSlip = controleNumeracaoSlipServiceImpl.obterProximoNumeroSlip(TipoSlip.SLIP_VENDA_ENCALHE);
				
				slipVendaEncalheDTO.setNumeroSlip((numeroSlip != null) ? numeroSlip.toString() : "");
				
				itensRelatorio.add(slipVendaEncalheDTO);
			}
		}

		byte[] retorno = null;

		if (itensRelatorio != null && !itensRelatorio.isEmpty()) {

			String mensagemErro = (TipoVendaEncalhe.ENCALHE.equals(filtro.getTipoVendaEncalhe())) 
								?"Erro ao gerar Slip de Venda de Encalhe."
										:"Erro ao gerar Slip de Venda de Suplementar.";
			try {
				
				retorno = gerarDocumentoSlipDeVenda(filtro.getTipoVendaEncalhe(), itensRelatorio.toArray(new SlipVendaEncalheDTO[]{}));
					
			} catch (Exception e) {
					throw new ValidacaoException(TipoMensagem.WARNING,mensagemErro);
			}
		}

		return retorno;
	}

	@Override
	@Transactional(readOnly = true)
	public List<VendaEncalheDTO> buscarVendasProduto(FiltroVendaEncalheDTO filtro) {

		return vendaProdutoRepository.buscarVendasEncalheDTO(filtro);
	}

	@Override
	@Transactional(readOnly = true)
	public Long buscarQntVendasProduto(FiltroVendaEncalheDTO filtro) {

		return vendaProdutoRepository.buscarQntVendaEncalhe(filtro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public byte[] geraImpressaoComprovanteVenda(Long idVenda) {
		
		VendaProduto vendaProduto = vendaProdutoRepository.buscarPorId(idVenda);
		
		if(vendaProduto == null){
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro no processamento da Venda de Encalhe.");
		}
		byte[] relatorio = gerarArquivoSlipVenda(vendaProduto.getTipoVenda(),obterDadosSlipVenda(vendaProduto));

		if (relatorio == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro no processamento da Venda de Encalhe.");
		}

		return relatorio;
	}
	
	private Image getLogoDistribuidor(){
		
		if(this.logoDistribuidor == null){
			this.logoDistribuidor = JasperUtil.getImagemRelatorio(parametrosDistribuidorService.getLogotipoDistribuidor());
		}
		
		return this.logoDistribuidor;
	}

}
