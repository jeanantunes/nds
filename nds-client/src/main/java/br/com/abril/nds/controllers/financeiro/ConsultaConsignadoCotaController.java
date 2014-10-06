package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.ConsultaConsignadoCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/consultaConsignadoCota")
@Rules(Permissao.ROLE_FINANCEIRO_CONSIGNADO_COTA)
public class ConsultaConsignadoCotaController extends BaseController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA = "filtroConsultaConsignadoCotaController";
	
	private static final String TOTAIS_GERAIS_CONSIGNADO_SESSION = "totaisGeraisConsignado";
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ConsultaConsignadoCotaService consultaConsignadoCota;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private Cota cota = null;
	
	@Path("/")
	public void index(){
		this.carregarComboFornecedores();		
	}
	
	private Cota obterCota(Integer numeroCota){
		cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		return cota;
	}
	
	@Post
	@Path("/buscarTotalGeralCota")
	public void buscarTotalGeralCota(FiltroConsultaConsignadoCotaDTO filtro){

		if(filtro.getIdCota() != null){
			cota = obterCota(filtro.getIdCota().intValue());
			if(cota == null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente.");
			}
			filtro.setIdCota(cota.getId());			
		}

		Map<String, Object> mapaResultado = this.obterTotaisGeraisSessao();
		
		FiltroConsultaConsignadoCotaDTO filtroSessao = (FiltroConsultaConsignadoCotaDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA);
		
		if (mapaResultado == null || !filtroSessao.equals(filtro)){
			
			this.removerTotaisGeraisSessao();
		
			mapaResultado = new HashMap<String, Object>();

			List<TotalConsultaConsignadoCotaDetalhado> totaisFornecedores = null;
			
			BigDecimal totalGeral = BigDecimal.ZERO;
			
			if (filtro.getIdFornecedor() == -1) {
				
				filtro.setIdFornecedor(null);
				
				totaisFornecedores = 
					this.consultaConsignadoCota.buscarTotalDetalhado(filtro);
				
				for(TotalConsultaConsignadoCotaDetalhado tt : totaisFornecedores) {
				    totalGeral = totalGeral.add(tt.getTotal());
				}
				
				mapaResultado.put("totaisFornecedores", totaisFornecedores);
			} else {
			    totalGeral = this.consultaConsignadoCota.buscarTotalGeralDaCota(filtro);
			}
			
			mapaResultado.put("totalGeral", CurrencyUtil.formatarValor(totalGeral));
	
			session.setAttribute(TOTAIS_GERAIS_CONSIGNADO_SESSION, mapaResultado);
		}

		this.result.use(CustomJson.class).put("result", mapaResultado).serialize();
	}
	
	@Post
	@Path("/buscarTotalGeralDetalhado")
	public void buscarTotalGeralDetalhado(FiltroConsultaConsignadoCotaDTO filtro){
		
		this.validarEntrada(filtro);
		
		if(filtro.getIdCota() != null){
			cota = obterCota(filtro.getIdCota().intValue());
			if(cota == null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Cota inesxistente.");
			}
			filtro.setIdCota(cota.getId());
		}
		
		if(filtro.getIdFornecedor() == -1 || filtro.getIdFornecedor() == 0){
			filtro.setIdFornecedor(null);
		}
		
		
		StringBuilder html = new StringBuilder();
		html.append("<table width='190' border='0' cellspacing='1' cellpadding='1' align='right'>");
		List<TotalConsultaConsignadoCotaDetalhado> listaGeralDetalhado = this.consultaConsignadoCota.buscarTotalDetalhado(filtro);
		int cont = 0;
		
		BigDecimal tt = BigDecimal.ZERO;
		
		for(TotalConsultaConsignadoCotaDetalhado total: listaGeralDetalhado){
			tt = tt.add(total.getTotal());
		    html.append("<tr>");
			if(cont==0){
				html.append("<td width='71'><strong>Total:</strong></td>");				
			}else{
				html.append("<td>&nbsp;</td>");
			}
			html.append("<td width='49'><strong>"+total.getNomeFornecedor()+"</strong></td>");
			html.append("<td width='60' align='right'><strong>"+CurrencyUtil.formatarValor(total.getTotal())+"</strong></td>");
			html.append("</tr>");
			cont++;			
			
		}
		
		String totalFormatado = CurrencyUtil.formatarValor(tt);
		
		html.append("<tr> ");
		html.append("<td style='border-top:1px solid #000;'><strong>Total Geral:</strong></td>");
		html.append("<td style='border-top:1px solid #000;'>&nbsp;</td>");
		html.append("<td style='border-top:1px solid #000;' align='right'><strong>"+totalFormatado+"</strong></td>");
		html.append("</tr>");
		
		html.append("</table>");
		
		this.result.use(Results.json()).from(html.toString(), "result").recursive().serialize();
		
	}
	
	@Post
	@Path("pesquisarConsignadoCota")
	public void pesquisarConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro, String sortorder, String sortname, int page, int rp){
	
		cota = obterCota(filtro.getIdCota().intValue());
		if(cota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente.");
		}
		filtro.setIdCota(cota.getId());
				
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		if(filtro.getIdFornecedor() == -1 || filtro.getIdFornecedor() == 0){
			filtro.setIdFornecedor(null);
		}
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> tableModel = efetuarConsultaConsignadoCota(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> efetuarConsultaConsignadoCota(
			FiltroConsultaConsignadoCotaDTO filtro) {
		
		int totalRegistros = this.consultaConsignadoCota.buscarConsignadoCota(filtro, false).size();
		if (totalRegistros == 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		List<ConsultaConsignadoCotaDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarConsignadoCota(filtro, true);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsignadoCota));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	@Post
	@Path("pesquisarMovimentoCotaPeloFornecedor")
	public void pesquisarMovimentoCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro, 
			String sortorder, String sortname, int page, int rp){
		
		if(filtro.getIdCota() != null){
			cota = obterCota(filtro.getIdCota().intValue());
			filtro.setIdCota(cota.getId());			
		}

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		if(filtro.getIdFornecedor() == -1 || filtro.getIdFornecedor() == 0){
			filtro.setIdFornecedor(null);
		}
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> tableModel = 
				efetuarConsultaConsignadoCotaPeloFornecedor(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> efetuarConsultaConsignadoCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro) {
		
		List<ConsultaConsignadoCotaPeloFornecedorDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarMovimentosCotaPeloFornecedor(filtro, true);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>>();
		
		Integer totalRegistros = filtro.getPaginacao().getQtdResultadosTotal();
		
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsignadoCota));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);

		return tableModel;
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaConsignadoCotaDTO filtro = 
				(FiltroConsultaConsignadoCotaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA);
		
		if(filtro.getIdCota() != null){

			Cota cota  = cotaService.obterPorId(filtro.getIdCota());
			
			if(cota!= null && cota.getPessoa()!= null ){
				filtro.setNomeCota(cota.getNumeroCota() + " - " +cota.getPessoa().getNome());
			}
			
		}
		
		if(filtro.getIdFornecedor() != null){
			
			Fornecedor fornecedor = fornecedorService.obterFornecedorPorId(filtro.getIdFornecedor());
			
			if(fornecedor!= null && fornecedor.getJuridica()!= null){

				filtro.setNomeFornecedor(fornecedor.getJuridica().getRazaoSocial());
			}
			
		}else{
			
			filtro.setNomeFornecedor("Todos");
		}
		
		if(filtro.getIdCota() != null) {
				
			List<ConsultaConsignadoCotaDTO> listaConsignadoCota = 
					this.consultaConsignadoCota.buscarConsignadoCota(filtro, false);
			
			if(listaConsignadoCota.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("consignado_cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, 
					listaConsignadoCota, ConsultaConsignadoCotaDTO.class, this.httpResponse);	
			
		}else{
			
			List<ConsultaConsignadoCotaPeloFornecedorDTO> listaConsignadoCota = 
					this.consultaConsignadoCota.buscarMovimentosCotaPeloFornecedor(filtro, false);
			
			if(listaConsignadoCota.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("consignado_cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, 
					listaConsignadoCota, ConsultaConsignadoCotaPeloFornecedorDTO.class, this.httpResponse);
			
		}
		
		result.nothing();
	}
	
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	@Post
	@Path("/buscarCotaPorNumero")
	public void buscarCotaPorNumero(Integer numeroCota) {
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota " + numeroCota + " inexistente.");
		}

		Pessoa pessoa = cota.getPessoa();

		String nomeCota = null;

		if (pessoa instanceof PessoaFisica) {

			nomeCota = ((PessoaFisica) pessoa).getNome();

		} else if (pessoa instanceof PessoaJuridica) {

			nomeCota = ((PessoaJuridica) pessoa).getRazaoSocial();
		}

		this.result.use(Results.json()).from(nomeCota, "result").recursive().serialize();
	}
	
	private void validarEntrada(FiltroConsultaConsignadoCotaDTO filtro) {		
		
		if(filtro.getIdCota() == null && filtro.getIdFornecedor() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um filtro deve ser preenchido!");			
		}
		
	}
	
	private void tratarFiltro(FiltroConsultaConsignadoCotaDTO filtroAtual) {

		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA, filtroAtual);
	}

    private Map<String, Object> obterTotaisGeraisSessao(){
		
		@SuppressWarnings("unchecked")
		 Map<String, Object> totaisFornecedores = ( Map<String, Object>) session.getAttribute(TOTAIS_GERAIS_CONSIGNADO_SESSION);
		
		return totaisFornecedores;
	} 
    
    private void removerTotaisGeraisSessao(){
		
		session.removeAttribute(TOTAIS_GERAIS_CONSIGNADO_SESSION);
	} 
}
