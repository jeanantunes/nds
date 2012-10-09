package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.VisaoEstoqueConferenciaCegaVO;
import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.VisaoEstoqueTransferenciaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("estoque/visaoEstoque")
public class VisaoEstoqueController {
	
	private static final String FILTRO_VISAO_ESTOQUE = "FILTRO_VISAO_ESTOQUE";
	private static final String LISTA_CONFERENCIA_CEGA = "LISTA_CONFERENCIA_CEGA";
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private VisaoEstoqueService visaoEstoqueService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	
	private Result result;
	
	public VisaoEstoqueController(Result result) {
		super();
		this.result = result;
	}

	
	@Path("/")
	@Rules(Permissao.ROLE_ESTOQUE_VISAO_DO_ESTOQUE)
	public void index()
	{
		List<Fornecedor> listFornecedores = fornecedorService.obterFornecedores();
		result.include("listFornecedores", listFornecedores);
		result.include("dataAtual", DateUtil.formatarDataPTBR(new Date()));
	}
	
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaVisaoEstoque filtro) {
		
		this.session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
		
		List<VisaoEstoqueDTO> listVisaoEstoque = visaoEstoqueService.obterVisaoEstoque(filtro);
		result.use(FlexiGridJson.class).from(listVisaoEstoque).total(listVisaoEstoque.size()).serialize();
	}
	
	
	@Path("/pesquisarDetalhe.json")
	public void pesquisarDetalhe(FiltroConsultaVisaoEstoque filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
		
		List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
		result.use(FlexiGridJson.class).from(listDetalhe).total(listDetalhe.size()).page(page).serialize();
	}
	
	
	@Path("/transferir")
	public void transferir(FiltroConsultaVisaoEstoque filtro) {
		
		TipoEstoque entrada  = Util.getEnumByStringValue(TipoEstoque.values(), filtro.getTipoEstoqueSelecionado());
		TipoEstoque saida = Util.getEnumByStringValue(TipoEstoque.values(), filtro.getTipoEstoque());

		filtro.setGrupoMovimentoEntrada(this.getGrupoMovimentoTransferencia(entrada, true));
		filtro.setGrupoMovimentoSaida(this.getGrupoMovimentoTransferencia(saida, false));

		visaoEstoqueService.transferirEstoque(filtro, this.getUsuario());
		
		this.pesquisar(filtro);
	}
	
	private GrupoMovimentoEstoque getGrupoMovimentoTransferencia(TipoEstoque tipoEstoque, boolean isEntrada) {
		
		switch(tipoEstoque) {
		
		case LANCAMENTO:
			return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO : 
							   GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO;
		case SUPLEMENTAR:
			return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR :
							   GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR;
		case RECOLHIMENTO:
			return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO :
							   GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO;
		case PRODUTOS_DANIFICADOS:
			return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS :
							   GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS;
		default:
			return null;
		}
	}
	
	
	@Path("/inventario")
	public void inventario(FiltroConsultaVisaoEstoque filtro) {
	
		
		
		
		result.use(Results.json()).from(filtro, "result").serialize();
	}
	
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaVisaoEstoque filtro = (FiltroConsultaVisaoEstoque) this.session.getAttribute(FILTRO_VISAO_ESTOQUE);
		
		List<VisaoEstoqueDTO> listVisaoEstoque = visaoEstoqueService.obterVisaoEstoque(filtro);
		
		FileExporter.to("visao-estoque", fileType).inHTTPResponse(
				this.getNDSFileHeader(filtro.getDataMovimentacao()), null, null,
				listVisaoEstoque, VisaoEstoqueDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/exportarDetalhe")
	public void exportarDetalhe(FileType fileType) throws IOException {
		
		FiltroConsultaVisaoEstoque filtro = (FiltroConsultaVisaoEstoque) this.session.getAttribute(FILTRO_VISAO_ESTOQUE);
		
		List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
		Class clazz = VisaoEstoqueDetalheDTO.class;
		
		if (listDetalhe != null && !listDetalhe.isEmpty() && listDetalhe.get(0) instanceof VisaoEstoqueDetalheJuramentadoDTO) {
			clazz = VisaoEstoqueDetalheJuramentadoDTO.class;
		}
		
		FileExporter.to("visao-estoque", fileType).inHTTPResponse(
				this.getNDSFileHeader(filtro.getDataMovimentacao()), null, null,
				listDetalhe, clazz,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	@Path("/gerarDadosConferenciaCega")
	public void gerarDadosConferenciaCega(FiltroConsultaVisaoEstoque filtro) throws IOException {
		
		List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
		
		List<VisaoEstoqueConferenciaCegaVO> listaExport = new ArrayList<VisaoEstoqueConferenciaCegaVO>();
		
		for(VisaoEstoqueDetalheDTO dto : listDetalhe) {
			
			VisaoEstoqueConferenciaCegaVO vo = new VisaoEstoqueConferenciaCegaVO(dto);
			
			if (filtro.getListaTransferencia() != null) {
				for(VisaoEstoqueTransferenciaDTO dtoTela : filtro.getListaTransferencia()) {
					if(dtoTela.getProdutoEdicaoId().longValue() == dto.getProdutoEdicaoId().longValue()) {
						vo.setEstoque(dtoTela.getQtde().toString());
						break;
					}
				}
			}
			
			listaExport.add(vo);
		}
		
		this.session.setAttribute(LISTA_CONFERENCIA_CEGA, listaExport);
		result.use(Results.nothing());
	}
	
	
	@SuppressWarnings("unchecked")
	@Path("/exportarConferenciaCega")
	public void exportarConferenciaCega(FileType fileType) throws IOException {
		
		List<VisaoEstoqueConferenciaCegaVO> listaExport = (List<VisaoEstoqueConferenciaCegaVO>) this.session.getAttribute(LISTA_CONFERENCIA_CEGA);
		
		FileExporter.to("visao-estoque-conferencia-cega", fileType).inHTTPResponse(
				this.getNDSFileHeader(new Date()), null, null,
				listaExport, VisaoEstoqueConferenciaCegaVO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	private NDSFileHeader getNDSFileHeader(Date data) {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor != null) {
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}

		ndsFileHeader.setData(data);
		ndsFileHeader.setNomeUsuario(getUsuario().getNome());
		return ndsFileHeader;
	}
	
	
	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}
}
