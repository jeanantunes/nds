package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.CalculaParcelasVO;
import br.com.abril.nds.client.vo.FormaCobrancaDefaultVO;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.client.vo.NegociacaoDividaVO;
import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCalculaParcelas;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO.OrdenacaoColunaParametrosCobranca;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.financeiro.TipoNegociacao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/negociacaoDivida")
@Rules(Permissao.ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA)
public class NegociacaoDividaController extends BaseController {
	
	private static final String FILTRO_NEGOCIACAO_DIVIDA = "FILTRO_NEGOCIACAO_DIVIDA";

	private static final String ID_ULTIMA_NEGOCIACAO = "ID_ULTIMA_NEGOCIACAO";
	
	@Autowired
	private NegociacaoDividaService negociacaoDividaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private DescontoService descontoService;
	
	private Result result;
	
	@Autowired
	private FormaCobrancaService formaCobrancaService;
	
	@Autowired
	private ParametroCobrancaCotaService parametroCobrancaCotaService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	
	public NegociacaoDividaController(Result result) {
		super();
		this.result = result;
	}

	@Path("/")
	public void index(){
		
		Integer qntdParcelas = this.distribuidorService.negociacaoAteParcelas();
		
		qntdParcelas = (qntdParcelas == null ? 0 : qntdParcelas);
		
		List<Integer> parcelas = new ArrayList<Integer>();
		
		for(int i = 1; i <= qntdParcelas; i++){
			parcelas.add(i);
		}
		
		FiltroConsultaBancosDTO  filtro = new FiltroConsultaBancosDTO();
		filtro.setAtivo(true);
		
		List<Banco> bancos = this.bancoService.obterBancos(filtro);

		this.result.include("qntdParcelas", parcelas);
		this.result.include("bancos", bancos);

		this.session.setAttribute(ID_ULTIMA_NEGOCIACAO, null);
	}

	@Post
	public void atualizarFormaCobranca(Integer numeroCota, boolean isNegociacaoAvulsa) {
		
		List<FormaCobrancaDefaultVO> formaCobranca = this.obterFormaCobrancaNegociacao(numeroCota, isNegociacaoAvulsa);
		
		this.result.use(Results.json()).from(formaCobranca, "result").recursive().serialize();
	}
	
	private List<FormaCobrancaDefaultVO> obterFormaCobrancaNegociacao(Integer numeroCota, boolean isNegociacaoAvulsa) {
		
		List<FormaCobrancaDefaultVO> tiposCobranca = new ArrayList<FormaCobrancaDefaultVO>();
		List<FormaCobrancaDTO> tiposCobrancaDTO = new ArrayList<FormaCobrancaDTO>();
		List<ParametroCobrancaVO> parametrosCobrancaVO = new ArrayList<ParametroCobrancaVO>();
		
		Cota cota = cotaService.obterPorNumeroDaCota(numeroCota);
		
		//String fonecedorCota = cota.getParametroCobranca().getFornecedorPadrao().getJuridica().getNomeFantasia().toUpperCase();
	
		PaginacaoVO paginacaoVO = new PaginacaoVO();
		FiltroParametrosCobrancaDTO filtroAtual = new FiltroParametrosCobrancaDTO();
		filtroAtual.setOrdenacaoColuna(OrdenacaoColunaParametrosCobranca.TIPO_COBRANCA);
		filtroAtual.setPaginacao(paginacaoVO);
		
		if (isNegociacaoAvulsa) {
			
			//tiposCobranca = this.formaCobrancaService.obterFormaCobrancaDefault();
			//tiposCobrancaDTO = parametroCobrancaCotaService.obterDadosFormasCobrancaPorCota(cota.getId());
			
			parametrosCobrancaVO =politicaCobrancaService.obterDadosPoliticasCobranca(filtroAtual);

		} else {

			tiposCobranca = this.parametroCobrancaCotaService.obterFormaCobrancaCotaDefault(numeroCota);
			
			if (tiposCobranca == null || tiposCobranca.isEmpty()){
			   

				parametrosCobrancaVO =politicaCobrancaService.obterDadosPoliticasCobranca(filtroAtual);
				
			    //tiposCobranca = this.formaCobrancaService.obterFormaCobrancaDefault();
				//tiposCobrancaDTO = parametroCobrancaCotaService.obterDadosFormasCobrancaPorCota(cota.getId());
			}
		}
		
		if (tiposCobranca == null || tiposCobranca.isEmpty()){
			tiposCobranca = new ArrayList<FormaCobrancaDefaultVO>();
		}
		
		
		
		/*
		
		FormaCobrancaDefaultVO formaCobrancaDefaultVO;
		
		if (tiposCobrancaDTO != null || !tiposCobrancaDTO.isEmpty()){
		 for(FormaCobrancaDTO voDTO:tiposCobrancaDTO){
			 
			 if(fonecedorCota.equals(voDTO.getFornecedor().toUpperCase())){
			 
				 formaCobrancaDefaultVO= new FormaCobrancaDefaultVO();
			 
			     formaCobrancaDefaultVO = new FormaCobrancaDefaultVO();
			     formaCobrancaDefaultVO.setIdBanco(voDTO.getIdBanco());
			     formaCobrancaDefaultVO.setNomeBanco(voDTO.getNomeBanco());
			     formaCobrancaDefaultVO.setTipoCobranca(TipoCobranca.valueOf(voDTO.getTipoPagto().toUpperCase()));
			
			     tiposCobranca.add(formaCobrancaDefaultVO);
			 
			 }
		 }
		}
		*/
		
		FormaCobrancaDefaultVO formaCobrancaDefaultVO;
		Banco banco;
		String tipoCobrancaString;
		
		if (parametrosCobrancaVO != null || !parametrosCobrancaVO.isEmpty()){
			 for(ParametroCobrancaVO voDTO:parametrosCobrancaVO){
				 
				 //if(fonecedorCota.equals(voDTO.getFornecedor().toUpperCase())){
				 
					 formaCobrancaDefaultVO= new FormaCobrancaDefaultVO();
					 
					 if(voDTO.getBanco()!=null && !voDTO.getBanco().trim().equals("")){
					  banco = bancoService.obterbancoPorNome(voDTO.getBanco());
				      formaCobrancaDefaultVO.setIdBanco(banco.getId());
				      formaCobrancaDefaultVO.setNomeBanco(banco.getNome());
					 }
					 
					 tipoCobrancaString = voDTO.getForma().replace(' ', '_').toUpperCase();
				     formaCobrancaDefaultVO.setTipoCobranca(TipoCobranca.valueOf(tipoCobrancaString));
				     
				     if(formaCobrancaDefaultVO.getTipoCobranca()== null){
				    	 if(tipoCobrancaString.equalsIgnoreCase(TipoCobranca.BOLETO_EM_BRANCO.name())){
				    		 formaCobrancaDefaultVO.setTipoCobranca(TipoCobranca.BOLETO_EM_BRANCO); 
				    	 } //BOLETO_EM_BRANCO
				     }
				     // BOLETO_EM_BRANCO
				     tiposCobranca.add(formaCobrancaDefaultVO);
				 
				 //}
			 }
		}
		return tiposCobranca;
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaNegociacaoDivida filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(ID_ULTIMA_NEGOCIACAO, null);
		
		filtro.setPaginacaoVO(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.session.setAttribute(FILTRO_NEGOCIACAO_DIVIDA, filtro);
		
		NegociacaoDividaPaginacaoDTO dto = this.negociacaoDividaService.obterDividasPorCotaPaginado(filtro);
		
		List<NegociacaoDividaDTO> listaNegociacaoDivida = dto.getListaNegociacaoDividaDTO();
		
		if (listaNegociacaoDivida.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<NegociacaoDividaVO> listDividas = new ArrayList<NegociacaoDividaVO>();
		for (NegociacaoDividaDTO negociacao : listaNegociacaoDivida) {
			listDividas.add(new NegociacaoDividaVO(negociacao));
		}
		
		this.result.use(
				FlexiGridJson.class).from(
						listDividas).total(
								dto.getQuantidadeRegistros().intValue()).page(page).serialize();
	}
	
	
	@Path("/pesquisarDetalhes.json")
	public void pesquisarDetalhes(Long idCobranca) {
		
		List<NegociacaoDividaDetalheVO> listDividas = negociacaoDividaService.obterDetalhesCobranca(idCobranca);
		
		BigDecimal total = BigDecimal.ZERO;
		for (NegociacaoDividaDetalheVO d : listDividas){
			
			d.setValor(d.getValor());
			
			if (d.getTipoMovimentoFinanceiro().getOperacaoFinaceira() == OperacaoFinaceira.CREDITO){
				total = total.subtract(d.getValor());
			} else {
				total = total.add(d.getValor());
			}
		}
		
		TableModel<CellModelKeyValue<NegociacaoDividaDetalheVO>> tableModel = 
				new TableModel<CellModelKeyValue<NegociacaoDividaDetalheVO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listDividas));
		tableModel.setPage(1);
		tableModel.setTotal(listDividas.size());
		
		Object[] dados = new Object[2];
		dados[0] = tableModel;
		dados[1] = total.setScale(2, RoundingMode.HALF_UP);
		
		result.use(Results.json()).from(dados, "result").recursive().serialize();
	}
	
	@Path("/obterQuantidadeParcelas.json")
	public void obterQuantidadeParcelas(FiltroCalculaParcelas filtro) {		
		this.result.use(Results.json()).from(negociacaoDividaService.obterQuantidadeParcelasConformeValorMinimo(filtro), "result").recursive().serialize();
	}
	
	@Path("/calcularParcelas.json")
	public void calcularParcelas(FiltroCalculaParcelas filtro) {		
		this.result.use(Results.json()).from(negociacaoDividaService.calcularParcelas(filtro), "result").recursive().serialize();
	}
	
	@Path("/recalcularParcelas.json")
	public void recalcularParcelas(FiltroCalculaParcelas filtro, List<CalculaParcelasVO> parcelas) {		
		this.result.use(Results.json()).from(negociacaoDividaService.recalcularParcelas(filtro,parcelas), "result").recursive().serialize();
	}
	
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaNegociacaoDivida filtro = (FiltroConsultaNegociacaoDivida) this.session.getAttribute(FILTRO_NEGOCIACAO_DIVIDA);
		
		List<NegociacaoDividaDTO> listDividas = negociacaoDividaService.obterDividasPorCota(filtro);
		
		FileExporter.to("consulta-box", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, 
				listDividas, NegociacaoDividaDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA_ALTERACAO)
	public void confirmarNegociacao(boolean porComissao, BigDecimal comissaoUtilizar, 
			TipoCobranca tipoCobranca, TipoFormaCobranca tipoFormaCobranca, List<DiaSemana> diasSemana,
			Integer diaInicio, Integer diaFim, boolean negociacaoAvulsa, boolean isentaEncargos,
			Integer ativarAposPagar, List<ParcelaNegociacao> parcelas, List<Long> idsCobrancas, Long idBanco,
			BigDecimal valorDividaComissao,boolean recebeCobrancaPorEmail){
		
		Long idNegociacao = (Long) this.session.getAttribute(ID_ULTIMA_NEGOCIACAO);
		
		if (idNegociacao != null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Negociação já efetuada.");
		}
		
		FiltroConsultaNegociacaoDivida filtro = (FiltroConsultaNegociacaoDivida) 
				this.session.getAttribute(FILTRO_NEGOCIACAO_DIVIDA);
		
		FormaCobranca formaCobranca = null;
		
		TipoNegociacao tipoNegociacao = negociacaoAvulsa ? 
				TipoNegociacao.PAGAMENTO_AVULSO : TipoNegociacao.PAGAMENTO;

		if (porComissao){
			
			parcelas = null;
			
			tipoNegociacao = TipoNegociacao.COMISSAO;
			
		} else {
			
			formaCobranca = new FormaCobranca();
			formaCobranca.setTipoCobranca(tipoCobranca);
			formaCobranca.setTipoFormaCobranca(tipoFormaCobranca);
			formaCobranca.setRecebeCobrancaEmail(recebeCobrancaPorEmail);
			
			Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota = new HashSet<ConcentracaoCobrancaCota>();
			if (diasSemana != null){
				for (DiaSemana codigoDia : diasSemana){
					
					ConcentracaoCobrancaCota concentracao = new ConcentracaoCobrancaCota();
					concentracao.setDiaSemana(codigoDia);
					concentracao.setFormaCobranca(formaCobranca);
					
					concentracaoCobrancaCota.add(concentracao);
				}
			}
			
			formaCobranca.setConcentracaoCobrancaCota(concentracaoCobrancaCota);
			comissaoUtilizar = null;
			
			if (diaInicio != null){
			
				List<Integer> diasMes = new ArrayList<Integer>();
				diasMes.add(diaInicio);
				
				if (diaFim != null){
					
					diasMes.add(diaFim);
				}
				
				formaCobranca.setDiasDoMes(diasMes);
			}
		}
		
		idNegociacao = this.negociacaoDividaService.criarNegociacao(
				filtro.getNumeroCota(), 
				parcelas, 
				tipoNegociacao,
				valorDividaComissao,
				idsCobrancas, 
				this.getUsuarioLogado(), 
				negociacaoAvulsa, 
				ativarAposPagar, 
				comissaoUtilizar, 
				isentaEncargos,
				formaCobranca,
				idBanco);
		
		this.session.setAttribute(ID_ULTIMA_NEGOCIACAO, idNegociacao);
		
		final String emailCota = this.cotaService.obterEmailCota(filtro.getNumeroCota());
		
		if (recebeCobrancaPorEmail && emailCota != null){
		
    		try{
    		    
    		    final List<byte[]> dados = new ArrayList<byte[]>();
    		    
    		    if (valorDividaComissao != null){
    		    
        		    dados.add(this.negociacaoDividaService.imprimirNegociacao(idNegociacao, valorDividaComissao.toString()));
    		    }
    		    
    		    if (negociacaoAvulsa && parcelas != null && !parcelas.isEmpty()){
    		        
    		        dados.addAll(this.negociacaoDividaService.gerarBoletosNegociacao(idNegociacao));
    		    }
    		    
    		    final byte[] arquivo = PDFUtil.mergePDFs(dados);
    		    
    		    final AnexoEmail anexoEmail = new AnexoEmail("negociacao_divida", arquivo, TipoAnexo.PDF);
    		    
    		    this.emailService.enviar("Negociação de Dívida", 
    		            "Segue em anexo documentos pertinentes.", new String[]{emailCota}, anexoEmail);
    		    
    		} catch(Exception e){
    		    
    		    this.result.use(Results.json()).from(new ValidacaoVO(
    	                TipoMensagem.WARNING, "Negociação efetuada, erro ao enviar e-mail: " + e.getMessage()), 
    	                "result").recursive().serialize();
    		    
    		    return;
    		}
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(
		        TipoMensagem.SUCCESS, "Negociação efetuada."), "result").recursive().serialize();
	}

	@Post
	public void buscarComissaoCota(){
		
		BigDecimal comissao = this.descontoService.obterComissaoParametroDistribuidor();
		
		Integer numeroCota = 
				((FiltroConsultaNegociacaoDivida)this.session.getAttribute(FILTRO_NEGOCIACAO_DIVIDA))
				.getNumeroCota();
		
		List<Object> valoresDesconto = new ArrayList<Object>();

		if (comissao != null && BigDecimal.ZERO.compareTo(comissao) != 0) {

			valoresDesconto.add(comissao);
		}

		//forma cobrança 'default' da cota
		FormaCobranca formaDefault = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalCota(numeroCota);
		
		if (formaDefault == null){
			
			formaDefault = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		}
		
		valoresDesconto.add(formaDefault.getTipoCobranca());
		
		this.result.use(Results.json()).from(valoresDesconto, "result").recursive().serialize();
	}
	
	public void imprimirNegociacao(String valorDividaSelecionada) throws Exception{
		
		Long idNegociacao = (Long) this.session.getAttribute(ID_ULTIMA_NEGOCIACAO);
		
		if (idNegociacao == null){
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, "É necessário confirmar a negociação antes de imprimir.");
		}
		
		byte[] arquivo = this.negociacaoDividaService.imprimirNegociacao(idNegociacao, valorDividaSelecionada);
		
		this.httpServletResponse.setContentType("application/pdf");
		this.httpServletResponse.setHeader("Content-Disposition", "attachment; filename=negociacao_"+idNegociacao+".pdf");
		this.httpServletResponse.getOutputStream().write(arquivo);
		this.httpServletResponse.getOutputStream().close();
		
		this.result.use(Results.nothing());
	}
	
	public void imprimirRecibo() throws IOException {
		
		Long idNegociacao = (Long) this.session.getAttribute(ID_ULTIMA_NEGOCIACAO);
		
		if (idNegociacao == null){
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, "É necessário confirmar a negociação antes de imprimir.");
		}
		
		List<byte[]> arquivos = this.negociacaoDividaService.imprimirRecibos(idNegociacao);
		
		this.httpServletResponse.setContentType("application/pdf");
		this.httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=recibo.pdf");
		
		byte[] arquivo = PDFUtil.mergePDFs(arquivos);

		OutputStream output = this.httpServletResponse.getOutputStream();
		output.write(arquivo);

		this.httpServletResponse.getOutputStream().close();

		this.result.use(Results.nothing());		
	}
	
	public void imprimirBoletos() throws IOException{
		
		Long idNegociacao = (Long) this.session.getAttribute(ID_ULTIMA_NEGOCIACAO);
		
		if (idNegociacao == null){
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, "É necessário confirmar a negociação antes de imprimir.");
		}
		
		List<byte[]> lista = this.negociacaoDividaService.gerarBoletosNegociacao(idNegociacao);
		
		byte[] arquivo = PDFUtil.mergePDFs(lista);
		
		this.httpServletResponse.setContentType("application/pdf");
		
		this.httpServletResponse.setHeader("Content-Disposition", "attachment; filename=boletos.pdf");
		
		OutputStream output = this.httpServletResponse.getOutputStream();
		
		output.write(arquivo);
		
		this.httpServletResponse.getOutputStream().close();
		
		result.use(Results.nothing());
	}
}