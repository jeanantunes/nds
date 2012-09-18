package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ConsultaConsignadoCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/consultaConsignadoCota")
public class ConsultaConsignadoCotaController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA = "filtroConsultaConsignadoCotaController";
	
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
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private Cota cota = null;
	
	@Path("/")
	@Rules(Permissao.ROLE_FINANCEIRO_CONSIGNADO_COTA)
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
				throw new ValidacaoException(TipoMensagem.WARNING, "Cota inesxistente.");
			}
			filtro.setIdCota(cota.getId());			
		}
		
		if(filtro.getIdFornecedor() == -1 || filtro.getIdFornecedor() == 0){
			filtro.setIdFornecedor(null);
		}
		
		BigDecimal totalGeral = this.consultaConsignadoCota.buscarTotalGeralDaCota(filtro);
		String totalFormatado = CurrencyUtil.formatarValor(totalGeral);
		
		this.result.use(Results.json()).from(totalFormatado, "result").recursive().serialize();
		
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
		for(TotalConsultaConsignadoCotaDetalhado total: listaGeralDetalhado){
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
		BigDecimal totalGeral = this.consultaConsignadoCota.buscarTotalGeralDaCota(filtro);
		String totalFormatado = CurrencyUtil.formatarValor(totalGeral);
		
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
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inesxistente.");
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
		
		List<ConsultaConsignadoCotaDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarConsignadoCota(filtro, true);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>>();
		
		Integer totalRegistros = this.consultaConsignadoCota.buscarTodasMovimentacoesPorCota(filtro, false);
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsignadoCota));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	@Post
	@Path("pesquisarMovimentoCotaPeloFornecedor")
	public void pesquisarMovimentoCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro, String sortorder, String sortname, int page, int rp){
		
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
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> tableModel = efetuarConsultaConsignadoCotaPeloFornecedor(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> efetuarConsultaConsignadoCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro) {
		
		List<ConsultaConsignadoCotaPeloFornecedorDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarMovimentosCotaPeloFornecedor(filtro, true);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>>();
		
		Integer totalRegistros = this.consultaConsignadoCota.buscarTodasMovimentacoesPorCota(filtro, false);
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
		
		FiltroConsultaConsignadoCotaDTO filtro = (FiltroConsultaConsignadoCotaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA);
		
		if(filtro.getIdFornecedor() == null){
			List<ConsultaConsignadoCotaDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarConsignadoCota(filtro, false);
			
			if(listaConsignadoCota.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("consignado_cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaConsignadoCota, ConsultaConsignadoCotaDTO.class, this.httpResponse);	
			
		}else{
			
			List<ConsultaConsignadoCotaPeloFornecedorDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarMovimentosCotaPeloFornecedor(filtro, false);
			
			if(listaConsignadoCota.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("consignado_cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					listaConsignadoCota, ConsultaConsignadoCotaPeloFornecedorDTO.class, this.httpResponse);
			
		}
		
		
		
		result.nothing();
	}
	
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
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
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente.");			
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

		FiltroConsultaConsignadoCotaDTO filtroSession = (FiltroConsultaConsignadoCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA, filtroAtual);
	}
	
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Lazaro Jornaleiro");
		return usuario;
	}

}
