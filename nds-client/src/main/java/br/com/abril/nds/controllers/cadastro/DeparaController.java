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
import br.com.abril.nds.client.vo.DeparaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DeparaDTO;
import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDepara;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Depara;

import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.DeparaService;
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
@Path("/cadastro/depara")
@Rules(Permissao.ROLE_CADASTRO_DEPARA)
public class DeparaController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DeparaController.class);
    
    private static final String FILTRO_CONSULTA_DEPARA = "filtroConsultaDepara";
    
    private static final String ID_DEPARA_DETALHE = "idDeparaDetalhe";
    
    private static final String SORTNAME_DEPARA_DETALHE = "sortnameDeparaDetalhe";
    
    private static final String SORTORDER_DEPARA_DETALHE = "sortorderDeparaDetalhe";
    
    @Autowired
    private DeparaService deparaService;
    
    @Autowired
    private HttpServletResponse httpServletResponse;
    
    @Autowired
    private HttpSession session;
    
    private final Result result;
    
    public DeparaController(final Result result) {
        super();
        this.result = result;
    }
    
    @Path("/")
    public void index() {
    }
    
    @Post
    @Path("/busca.json")
    public void busca(final Depara depara, final String sortname, final String sortorder, final int rp, final int page) {
        
        final String fc = depara.getFc();
        
        final String dinap = depara.getDinap();
        
        final List<Depara> deparas = deparaService.busca(fc, dinap, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page * rp - rp, rp);
        
        final Long quantidade = deparaService.quantidade(fc, dinap);
        
        final FiltroConsultaDepara filtro = new FiltroConsultaDepara();
        
        filtro.setFc(fc);
        filtro.setDinap(dinap);
        filtro.setOrderBy(sortname);
        filtro.setOrdenacao(Ordenacao.valueOf(sortorder.toUpperCase()));
        filtro.setInitialResult(page * rp - rp);
        filtro.setMaxResults(rp);
        
        session.setAttribute(FILTRO_CONSULTA_DEPARA, filtro);
        
        result.use(FlexiGridJson.class).from(deparas).total(quantidade.intValue()).page(page).serialize();
        
    }
    
    @Post
    @Path("/buscaPorId.json")
    public void buscaPorId(final long id) {
        /*
         * if (deparaService.hasAssociacao(id)) { throw new ValidacaoException(new
         * ValidacaoVO
         * (TipoMensagem.ERROR,"Depara está em uso e não pode ser editado.")); }
         * else { Depara depara = deparaService.buscarPorId(id);
         * result.use(CustomJson.class).from(depara).serialize(); }
         */
        
        final DeparaVO depara = new DeparaVO(deparaService.buscarPorId(id), false);
        
       
        
        result.use(CustomJson.class).from(depara).serialize();
        
    }
    
    @Post
    @Path("/salvar.json")
    @Rules(Permissao.ROLE_CADASTRO_DEPARA_ALTERACAO)
    public void salvar(final Depara depara) {
        valida(depara);
        try {
            deparaService.merge(depara);
        } catch (final UniqueConstraintViolationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
        } catch (final RelationshipRestrictionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
        }
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Depara salvo com Sucesso."), "result")
        .recursive().serialize();
    }
    
    private void valida(final Depara depara) {
        final List<String> listaMensagens = new ArrayList<String>();
        
        if (depara.getFc() == null) {
            listaMensagens.add("O preenchimento do campo [FC] é obrigatório.");
        }
        if (StringUtils.isBlank(depara.getDinap())) {
            listaMensagens.add("O preenchimento do campo [Dinap] é obrigatório.");
        }
      
        
        if (!listaMensagens.isEmpty()) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
        }
    }
    
    @Post
    @Path("/remove.json")
   // @Rules(Permissao.ROLE_CADASTRO_DEPARA_ALTERACAO)
    public void remove(final long id) {
        try {
            deparaService.excluirDepara(id);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
        }
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Depara removido com sucesso.")).serialize();
    }
    
   
   
    
    
    
    private List<DeparaDTO> convertDeparaToDeparaDTO(final List<Depara> listaDepara) {
        
        final List<DeparaDTO> listaDeparaDTO = new ArrayList<DeparaDTO>();
        
        for (final Depara depara : listaDepara) {
            
            final DeparaDTO deparaDTO = new DeparaDTO();
            
            deparaDTO.setFc(depara.getFc());
            deparaDTO.setDinap(depara.getDinap());
         
            
            listaDeparaDTO.add(deparaDTO);
        }
        
        return listaDeparaDTO;
    }
}
