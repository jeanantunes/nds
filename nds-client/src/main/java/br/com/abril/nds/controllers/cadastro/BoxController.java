package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.BoxVO;
import br.com.abril.nds.dto.BoxDTO;
import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBox;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/box")
public class BoxController {

	private static final String FILTRO_CONSULTA_BOX = "filtroConsultaBox";
	
	private static final String ID_BOX_DETALHE = "idBoxDetalhe";
	
	@Autowired
	private BoxService boxService;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	private Result result;

	public BoxController(Result result) {
		super();
		this.result = result;
	}

	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_BOX)
	public void index() {
	}

	@Post
	@Path("/busca.json")
	public void busca(Box box, String sortname,
			String sortorder, int rp, int page) {
		
		Integer codigoBox = box.getCodigo();
		
		TipoBox tipoBox = box.getTipoBox();
		
		List<Box> boxs = boxService.busca(codigoBox, tipoBox, sortname,
				Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp, rp);
		
		Long quantidade = boxService.quantidade(codigoBox, tipoBox);
		
		FiltroConsultaBox filtro = new FiltroConsultaBox();
		
		filtro.setCodigoBox(codigoBox);
		filtro.setTipoBox(tipoBox);
		filtro.setOrderBy(sortname);
		filtro.setOrdenacao(Ordenacao.valueOf(sortorder.toUpperCase()));
		filtro.setInitialResult(page*rp - rp);
		filtro.setMaxResults(rp);
		
		this.session.setAttribute(FILTRO_CONSULTA_BOX, filtro);
		
		result.use(FlexiGridJson.class).from(boxs).total(quantidade.intValue()).page(page).serialize();

	}
	@Post
	@Path("/buscaPorId.json")
	public void buscaPorId(long id) {
		/*if (boxService.hasAssociacao(id)) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,"Box está em uso e não pode ser editado."));
		} else {
			Box box = boxService.buscarPorId(id);
			result.use(CustomJson.class).from(box).serialize();
		}*/

		BoxVO box = new BoxVO(boxService.buscarPorId(id), boxService.hasAssociacao(id));
		result.use(CustomJson.class).from(box).serialize();

	}

	@Post
	@Path("/salvar.json")
	public void salvar(Box box) {
		valida(box);
		try {
			boxService.merge(box);
		} catch (UniqueConstraintViolationException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
		} catch (RelationshipRestrictionException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
		}
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Box salvo com Sucesso."),"result").recursive().serialize();
	}

	private void valida(Box box) {
		List<String> listaMensagens = new ArrayList<String>();

		if (box.getCodigo() == null) {
			listaMensagens.add("O preenchimento do campo [Código] é obrigatório.");
		}
		if (StringUtil.isEmpty(box.getNome())) {
			listaMensagens.add("O preenchimento do campo [Nome] é obrigatório.");
		}
		if (box.getTipoBox() == null) {
			listaMensagens.add("O preenchimento do campo [Tipo Box] é obrigatório.");
		}
		
		if (!listaMensagens.isEmpty()) {			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
		}
	}
	
	@Post
	@Path("/remove.json")
	public void remove(long id){
		try {
			boxService.remover(id);
		} catch (RelationshipRestrictionException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
		}
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Box removido com sucesso.")).serialize();
	}
	
	@Post
	@Path("/detalhe.json")
	public void detalhe(long id){
		this.session.setAttribute(ID_BOX_DETALHE, id);
		List<CotaRotaRoteiroDTO> rotaRoteiroDTOs = boxService.obtemCotaRotaRoteiro(id);		
		result.use(FlexiGridJson.class).from(rotaRoteiroDTOs).total(rotaRoteiroDTOs.size()).page(1).serialize();
	}	

	public void exportarDetalhes(FileType fileType) throws IOException {
		
		Long id = (Long) this.session.getAttribute(ID_BOX_DETALHE);
		
		List<CotaRotaRoteiroDTO> rotaRoteiroDTOs = boxService.obtemCotaRotaRoteiro(id);		
		
		FileExporter.to("detalhes-box", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				rotaRoteiroDTOs, CotaRotaRoteiroDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	public void exportarConsulta(FileType fileType) throws IOException {
		
		FiltroConsultaBox filtro = (FiltroConsultaBox) 
				this.session.getAttribute(FILTRO_CONSULTA_BOX);
		
		if(filtro == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "É necessario realizar a pesquisa primeiro."));
		}
		
		List<Box> boxs = 
				boxService.busca(
						filtro.getCodigoBox(), 
						filtro.getTipoBox(), 
						filtro.getOrderBy(), 
						filtro.getOrdenacao(), 
						null,
						null);
		
		List<BoxDTO> listaBoxs = this.convertBoxToBoxDTO(boxs);
		
		FileExporter.to("consulta-box", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				listaBoxs, BoxDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();

		Distribuidor distribuidor = this.distribuidorService.obter();

		if (distribuidor != null) {

			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica()
					.getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica()
					.getCnpj());
		}

		ndsFileHeader.setData(new Date());

		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());

		return ndsFileHeader;
	}

	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();

		usuario.setId(1L);

		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}
	
	
	private List<BoxDTO> convertBoxToBoxDTO(List<Box> listaBox) {
		
		List<BoxDTO> listaBoxDTO = new ArrayList<BoxDTO>();
		
		for (Box box : listaBox) {
			
			BoxDTO boxDTO = new BoxDTO();
			
			boxDTO.setCodigo(box.getCodigo());
			boxDTO.setNome(box.getNome());
			boxDTO.setTipoBox(box.getTipoBox());
			
			listaBoxDTO.add(boxDTO);
		}
		
		return listaBoxDTO;
	}
}
