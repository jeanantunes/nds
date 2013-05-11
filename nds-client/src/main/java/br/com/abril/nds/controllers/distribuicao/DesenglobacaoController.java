package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DesenglobaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.DesenglobacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDesenglobacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DesenglobacaoService;
import br.com.abril.nds.util.CellModelKeyValue;
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
@Path("/distribuicao/desenglobacao")
public class DesenglobacaoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private DesenglobacaoService desenglobacaoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_DESENGLOBACAO)
	@Path("/index")
	public void desenglobacao(){
		
	}
	
	@Post
	public void pesquisaPrincipal(FiltroDesenglobacaoDTO filtro, String sortorder, String sortname, int page, int rp ){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		session.setAttribute("filtroDesengloba", filtro);
		
		TableModel<CellModelKeyValue<DesenglobacaoDTO>> tableModel = montarCotasDesenglobadas(filtro);
		if (tableModel != null) {
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();			
		}
		
		result.nothing();
	}

	private TableModel<CellModelKeyValue<DesenglobacaoDTO>> montarCotasDesenglobadas(FiltroDesenglobacaoDTO dto) {
		
		Long longValue = null;
		if(dto.getCotaDto().getNumeroCota()!=null){
			longValue = dto.getCotaDto().getNumeroCota().longValue();
		}
		
		List<DesenglobacaoDTO> cotasDesenglobadas = desenglobacaoService.obterDesenglobacaoPorCota(longValue);
		dto.getPaginacao().setQtdResultadosTotal(cotasDesenglobadas.size());
		
		if (cotasDesenglobadas == null || cotasDesenglobadas.isEmpty()) {
			return null;
		}
		
		TableModel<CellModelKeyValue<DesenglobacaoDTO>> tableModel = new TableModel<CellModelKeyValue<DesenglobacaoDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cotasDesenglobadas));
		tableModel.setPage(dto.getPaginacao().getPaginaAtual());
		tableModel.setTotal(dto.getPaginacao().getQtdResultadosTotal());
		return tableModel;
	}

	private void validarFiltroDesenglobacao(FiltroDesenglobacaoDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe um código ou nome.");
	}
	
	@Post
	@Path("/inserirEnglobacao")
	public void inserirEnglobacao(List<DesenglobaVO> desenglobaDTO,String alterando) {		
		
		boolean isOk =false;
		if(StringUtils.isEmpty(alterando)){
			isOk = desenglobacaoService.inserirDesenglobacao(desenglobaDTO, super.getUsuarioLogado());
		}else{
			isOk = desenglobacaoService.alterarDesenglobacao(desenglobaDTO, super.getUsuarioLogado());
		}
		if (!isOk) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Porcentagem supera o limite de 100%");
		}
		result.nothing();
	}
	
	@Get
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		FiltroDesenglobacaoDTO filtro = (FiltroDesenglobacaoDTO) session.getAttribute("filtroDesengloba");
		
		Long longValue = null;
		if(filtro.getCotaDto().getNumeroCota()!=null){
			longValue = filtro.getCotaDto().getNumeroCota().longValue();
		}
		
		List<DesenglobacaoDTO> cotasDesenglobadas = desenglobacaoService.obterDesenglobacaoPorCota(longValue);
			
			FileExporter.to("ENGLOBACAO_DESENGLOBACAO", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					cotasDesenglobadas, DesenglobacaoDTO.class, this.httpResponse);
			result.nothing();
	}
	
	
	@Post
	@Path("/editarDesenglobacao")
	public void editarDesenglobacao(Long cotaNumeroDesenglobada) {
		
		final List<DesenglobacaoDTO> desenglobacaoPorCotaList = desenglobacaoService.obterDesenglobacaoPorCotaDesenglobada(cotaNumeroDesenglobada);
		Cota cotaEnglobadaBean = this.cotaService.obterPorNumeroDaCota(cotaNumeroDesenglobada.intValue());

		final CotaDTO cotaDTO = new CotaDTO();
		
		try {
			BeanUtils.copyProperties(cotaDTO, cotaEnglobadaBean);
			cotaDTO.setNomePessoa(cotaEnglobadaBean.getPessoa().getNome());
		} catch (Exception e) {
		} 

		List<Object> r = new ArrayList<Object>();

		r.add(cotaDTO);
		r.add(desenglobacaoPorCotaList);
		
		result.use(Results.json()).from(r,"result").recursive().serialize();
	}

	@Post
	@Path("/excluirDesenglobacao")
	public void excluirDesenglobacao(Long id) {
		try {
			this.desenglobacaoService.excluirDesenglobacao(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao excluir Desenglobação");
		}
		
		result.use(Results.json()).from("OK").serialize();
	}
	
}


