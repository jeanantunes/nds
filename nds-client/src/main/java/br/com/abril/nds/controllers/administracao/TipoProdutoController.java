package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.TipoProdutoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TipoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.StringUtil;
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
@Path("/administracao/tipoProduto")
public class TipoProdutoController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TipoProdutoController.class);
    
    @Autowired
    private Result result;
    
    @Autowired
    private HttpServletResponse response;
    
    @Autowired
    private TipoProdutoService tipoProdutoService;
    
    @Autowired
    private HttpSession session;
    
    private static List<ItemDTO<Long, String>> listaNcm = new ArrayList<ItemDTO<Long, String>>();
    
    private static final String FILTRO = "filtro";
    
    private void montarComboNcm() {
        final List<NCM> ncmAll = tipoProdutoService.obterListaNCM();
        for (final NCM item : ncmAll) {
            listaNcm.add(new ItemDTO<Long, String>(item.getCodigo(), item.getCodigo() + "-" + item.getDescricao()));
        }
    }
    
    @Path("/")
    public void index() {
        
        listaNcm.clear();
        this.montarComboNcm();
        result.include("listaNcm", listaNcm);
        
    }
    
    @Post("/busca.json")
    public void busca(final String descricao, final Long codigo, final String codigoNCM, final String codigoNBM,
            final String sortname, final String sortorder, final int rp, final int page) {
        
        final List<TipoProduto> listaTipoProdutos = tipoProdutoService.busca(descricao, codigo, codigoNCM, codigoNBM,
                sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page * rp - rp, rp);
        
        final Long quantidade = tipoProdutoService.quantidade(descricao, codigo, codigoNCM, codigoNBM);
        
        // Gera filtro usado na exportação de arquivo
        this.gerarFiltro(codigo, codigoNBM, codigoNCM, descricao, Ordenacao.valueOf(sortorder.toUpperCase()), sortname);
        
        if (listaTipoProdutos.isEmpty()) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum tipo de produto foi encontrado"));
        }
        
        result.use(FlexiGridJson.class).noReference().from(listaTipoProdutos).total(quantidade.intValue()).page(page)
        .exclude("listaProdutos").serialize();
    }
    
    @Post("/getById.json")
    public void buscaPorId(final Long id) {
        
        final TipoProduto tipoProduto = tipoProdutoService.buscaPorId(id);
        
        final TipoProdutoVO tipoProdutoVO = new TipoProdutoVO(tipoProduto.getId().toString(),
                tipoProduto.getNcm() != null ? tipoProduto.getNcm().getId().toString() : "", tipoProduto.getCodigo()
                        .toString(), tipoProduto.getDescricao(), tipoProduto.getNcm() != null ? tipoProduto.getNcm()
                                .getCodigo().toString() : "", tipoProduto.getCodigoNBM(), tipoProduto.getGrupoProduto());
        
        result.use(Results.json()).from(tipoProdutoVO, "tipoProduto").serialize();
    }
    
    @Post("/salva.json")
    public void salvar(final TipoProduto tipoProduto) {
        
        final Long codigoNcm = tipoProduto.getNcm().getCodigo();
        NCM ncm = null;
        if (codigoNcm != null && codigoNcm > 0) {
            ncm = tipoProdutoService.obterNCMporCodigo(codigoNcm);
        }
        
        if (ncm == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Código NCM] é obrigatório.");
        }
        
        tipoProduto.setNcm(ncm);
        
        this.valida(tipoProduto);
        
        tipoProdutoService.merge(tipoProduto);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Tipo de Produto salvo com Sucesso."),
                "result").recursive().serialize();
    }
    
    @Post("/remove.json")
    public void remove(final long id) {
        
        try {
            tipoProdutoService.remover(id);
        } catch (final UniqueConstraintViolationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
        }
        
        result.use(Results.json()).from("OK").serialize();
    }
    
    @Post("/getCodigoSugerido.json")
    public void getCodigoSugerido() {
        final String codigo = tipoProdutoService.getCodigoSugerido();
        
        result.use(Results.json()).from(codigo, "codigo").serialize();
    }
    
    /**
     * Exporta os dados da pesquisa.
     * 
     * @param fileType - tipo de arquivo
     * 
     * @throws IOException Exceção de E/S
     */
    public void exportar(final FileType fileType) throws IOException {
        
        final FiltroTipoProdutoDTO filtro = (FiltroTipoProdutoDTO) session.getAttribute(FILTRO);
        
        final List<TipoProduto> listaTipoProduto = tipoProdutoService.busca(filtro.getDescricao(), filtro.getCodigo(),
                filtro.getCodigoNCM(), filtro.getCodigoNBM(), filtro.getSortname(), filtro.getSortorder(), -1, -1);
        
        final List<TipoProdutoDTO> listaTipoProdutoDTO = this.toDTO(listaTipoProduto);
        
        FileExporter.to("tipo-produto", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null,
                listaTipoProdutoDTO, TipoProdutoDTO.class, response);
    }
    
    private void valida(final TipoProduto tipoProduto) {
        
        final List<String> listaMensagens = new ArrayList<String>();
        
        if (tipoProduto == null || StringUtil.isEmpty(tipoProduto.getDescricao())) {
            listaMensagens.add("O preenchimento do campo [Tipo de Produto] é obrigatório.");
        }
        
        if (tipoProduto == null || tipoProduto.getCodigo() == null) {
            listaMensagens.add("O preenchimento do campo [Código] é obrigatório.");
        }
        
        if (!listaMensagens.isEmpty()) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
        }
    }
    
    private List<TipoProdutoDTO> toDTO(final List<TipoProduto> listaTipoProduto) {
        
        final List<TipoProdutoDTO> lista = new ArrayList<TipoProdutoDTO>();
        
        if (listaTipoProduto != null) {
            for (final TipoProduto tipoProduto : listaTipoProduto) {
                
                final TipoProdutoDTO tipoProdutoDTO = new TipoProdutoDTO();
                
                tipoProdutoDTO.setCodigo(tipoProduto.getCodigo());
                tipoProdutoDTO.setCodigoNBM(tipoProduto.getCodigoNBM());
                tipoProdutoDTO.setCodigoNCM(tipoProduto.getNcm().getCodigo());
                tipoProdutoDTO.setDescricao(tipoProduto.getDescricao());
                
                lista.add(tipoProdutoDTO);
            }
        }
        return lista;
    }
    
    /**
     * Gera um filtro de pesquisa para ser usado na exportação de arquivos.
     * 
     * @param codigo
     * @param codigoNBM
     * @param codigoNCM
     * @param descricao
     * @param sortorder
     * @param sortname
     * @return
     */
    private FiltroTipoProdutoDTO gerarFiltro(final Long codigo, final String codigoNBM, final String codigoNCM,
            final String descricao, final Ordenacao sortorder, final String sortname) {
        
        final FiltroTipoProdutoDTO filtro = new FiltroTipoProdutoDTO(codigo, codigoNCM, codigoNBM, descricao,
                sortorder, sortname);
        
        session.setAttribute(FILTRO, filtro);
        
        return filtro;
    }
}
