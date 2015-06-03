package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.BoxVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BoxDTO;
import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBox;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/box")
@Rules(Permissao.ROLE_CADASTRO_BOX)
public class BoxController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BoxController.class);
    
    private static final String FILTRO_CONSULTA_BOX = "filtroConsultaBox";
    
    private static final String ID_BOX_DETALHE = "idBoxDetalhe";
    
    private static final String SORTNAME_BOX_DETALHE = "sortnameBoxDetalhe";
    
    private static final String SORTORDER_BOX_DETALHE = "sortorderBoxDetalhe";
    
    @Autowired
    private BoxService boxService;
    
    @Autowired
    private HttpServletResponse httpServletResponse;
    
    @Autowired
    private HttpSession session;
    
    private final Result result;
    
    public BoxController(final Result result) {
        super();
        this.result = result;
    }
    
    @Path("/")
    public void index() {
    }
    
    @Post
    @Path("/busca.json")
    public void busca(final Box box, final String sortname, final String sortorder, final int rp, final int page) {
        
        final Integer codigoBox = box.getCodigo();
        
        final TipoBox tipoBox = box.getTipoBox();
        
        final List<Box> boxs = boxService.busca(codigoBox, tipoBox, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page * rp - rp, rp);
        
        final Long quantidade = boxService.quantidade(codigoBox, tipoBox);
        
        final FiltroConsultaBox filtro = new FiltroConsultaBox();
        
        filtro.setCodigoBox(codigoBox);
        filtro.setTipoBox(tipoBox);
        filtro.setOrderBy(sortname);
        filtro.setOrdenacao(Ordenacao.valueOf(sortorder.toUpperCase()));
        filtro.setInitialResult(page * rp - rp);
        filtro.setMaxResults(rp);
        
        session.setAttribute(FILTRO_CONSULTA_BOX, filtro);
        
        result.use(FlexiGridJson.class).from(boxs).total(quantidade.intValue()).page(page).serialize();
        
    }
    
    @Post
    @Path("/buscaPorId.json")
    public void buscaPorId(final long id) {
        /*
         * if (boxService.hasAssociacao(id)) { throw new ValidacaoException(new
         * ValidacaoVO
         * (TipoMensagem.ERROR,"Box está em uso e não pode ser editado.")); }
         * else { Box box = boxService.buscarPorId(id);
         * result.use(CustomJson.class).from(box).serialize(); }
         */
        
        final BoxVO box = new BoxVO(boxService.buscarPorId(id), boxService.hasAssociacao(id));
        
        box.getBox().setRoteirizacao(null);
        
        result.use(CustomJson.class).from(box).serialize();
        
    }
    
    @Post
    @Path("/salvar.json")
    @Rules(Permissao.ROLE_CADASTRO_BOX_ALTERACAO)
    public void salvar(final Box box) {
        valida(box);
        try {
            boxService.merge(box);
        } catch (final UniqueConstraintViolationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
        } catch (final RelationshipRestrictionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
        }
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Box salvo com Sucesso."), "result")
        .recursive().serialize();
    }
    
    private void valida(final Box box) {
        final List<String> listaMensagens = new ArrayList<String>();
        
        if (box.getCodigo() == null) {
            listaMensagens.add("O preenchimento do campo [Código] é obrigatório.");
        }
        if (StringUtils.isBlank(box.getNome())) {
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
    @Rules(Permissao.ROLE_CADASTRO_BOX_ALTERACAO)
    public void remove(final long id) {
        try {
            boxService.remover(id);
        } catch (final RelationshipRestrictionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
        }
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Box removido com sucesso.")).serialize();
    }
    
    @Post
    @Path("/detalhe.json")
    public void detalhe(final long id, final String sortname, final String sortorder) {
        
        session.setAttribute(ID_BOX_DETALHE, id);
        
        session.setAttribute(SORTNAME_BOX_DETALHE, sortname);
        
        session.setAttribute(SORTORDER_BOX_DETALHE, sortorder);
        
        final List<CotaRotaRoteiroDTO> rotaRoteiroDTOs = boxService.obtemCotaRotaRoteiro(id, sortname, sortorder);
        
        result.use(FlexiGridJson.class).from(rotaRoteiroDTOs).total(rotaRoteiroDTOs.size()).page(1).serialize();
    }
    
    public void exportarDetalhes(final FileType fileType) throws IOException {
        
        final Long id = (Long) session.getAttribute(ID_BOX_DETALHE);
        
        final String sortname = (String) session.getAttribute(SORTNAME_BOX_DETALHE);
        
        final String sortorder = (String) session.getAttribute(SORTORDER_BOX_DETALHE);
        
        final List<CotaRotaRoteiroDTO> rotaRoteiroDTOs = boxService.obtemCotaRotaRoteiro(id, sortname, sortorder);
        
        FileExporter.to("detalhes-box", fileType).inHTTPResponse(this.getNDSFileHeader(), null, rotaRoteiroDTOs,
                CotaRotaRoteiroDTO.class, httpServletResponse);
        
        result.use(Results.nothing());
    }
    
    public void exportarConsulta(final FileType fileType) throws IOException {
        
        final FiltroConsultaBox filtro = (FiltroConsultaBox) session.getAttribute(FILTRO_CONSULTA_BOX);
        
        if (filtro == null) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
                    "É necessario realizar a pesquisa primeiro."));
        }
        
        final List<Box> boxs = boxService.busca(filtro.getCodigoBox(), filtro.getTipoBox(), filtro.getOrderBy(), filtro
                .getOrdenacao(), null, null);
        
        final List<BoxDTO> listaBoxs = this.convertBoxToBoxDTO(boxs);
        
        FileExporter.to("consulta-box", fileType).inHTTPResponse(this.getNDSFileHeader(), null, listaBoxs,
                BoxDTO.class, httpServletResponse);
        
        result.use(Results.nothing());
    }
    
    private List<BoxDTO> convertBoxToBoxDTO(final List<Box> listaBox) {
        
        final List<BoxDTO> listaBoxDTO = new ArrayList<BoxDTO>();
        
        for (final Box box : listaBox) {
            
            final BoxDTO boxDTO = new BoxDTO();
            
            boxDTO.setCodigo(box.getCodigo());
            boxDTO.setNome(box.getNome());
            boxDTO.setTipoBox(box.getTipoBox());
            
            listaBoxDTO.add(boxDTO);
        }
        
        return listaBoxDTO;
    }
}
