package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseEstudoDetalhesDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;
import br.com.abril.nds.service.AnaliseParcialService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/analise/parcial")
public class AnaliseParcialController extends BaseController {

    @Autowired
    private Validator validator;

    @Autowired
    private Result result;

    @Autowired
    private AnaliseParcialService analiseParcialService;

    @Autowired
    private HttpServletResponse httpResponse;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private ProdutoEdicaoService produtoEdicaoService;

    @Autowired
    private DistribuicaoVendaMediaRepository distribuicaoVendaMediaRepository;

    @Autowired
    private HttpSession session;

    @Path("/")
    public void index(Long id, Long faixaDe, Long faixaAte, String modoAnalise) {

        EstudoCota estudo = analiseParcialService.buscarPorId(id);
        Lancamento lancamento = lancamentoService.obterPorId(estudo.getEstudo().getLancamentoID());

        if (modoAnalise == null) {
            result.include("tipoExibicao", "NORMAL");
            session.setAttribute("modoAnalise", "NORMAL");
        } else {
            result.include("tipoExibicao", modoAnalise);
            session.setAttribute("modoAnalise", modoAnalise);
        }
        result.include("lancamento", lancamento);
        result.include("estudoCota", estudo);
        result.include("faixaDe", faixaDe);
        result.include("faixaAte", faixaAte);
        ClassificacaoCota[] vetor = ClassificacaoCota.values();
        Arrays.sort(vetor, new Comparator<ClassificacaoCota>() {

	    @Override
	    public int compare(ClassificacaoCota o1, ClassificacaoCota o2) {
		return o1.getTexto().compareToIgnoreCase(o2.getTexto());
	    }
        });
        result.include("classificacaoCotaList", vetor);
        result.forwardTo("/WEB-INF/jsp/distribuicao/analiseParcial.jsp");
    }

    @Path("/detalhes")
    public void detalhes(Long id) {
        ProdutoEdicao produtoEdicao = produtoEdicaoService.buscarPorID(id);
        List<AnaliseEstudoDetalhesDTO> lista = analiseParcialService.buscarDetalhesAnalise(produtoEdicao);

        TableModel<CellModelKeyValue<AnaliseEstudoDetalhesDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
        table.setPage(1);
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }

    @Post
    public void historicoEdicoesBase(Long[] idsProdutoEdicao) {
        List<AnaliseEstudoDetalhesDTO> list = analiseParcialService.historicoEdicoesBase(idsProdutoEdicao);

        result.use(Results.json()).withoutRoot().from(list).recursive().serialize();
    }

    @Path("/abrirAnaliseFaixa")
    public void abrirAnaliseFaixa(Long estudo, Long faixaDe, Long faixaAte) {
        AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
        queryDTO.setEstudoId(estudo);

        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);

        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = monta(lista);
        table.setPage(1);
        table.setTotal(50);
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
        result.forwardTo("/WEB-INF/jsp/distribuicao/analiseParcial.jsp");
    }

    @Path("/carregarDetalhesPdv")
    public void carregarDetalhesPdv(Integer numeroCota, Long estudoId) {
        List<PdvDTO> lista = analiseParcialService.carregarDetalhesPdv(numeroCota, estudoId);

        TableModel<CellModelKeyValue<PdvDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
        table.setPage(1);
        table.setTotal(lista.size());
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }

    @Post
    public void carregarDetalhesCota(Integer numeroCota, String codigoProduto) {
        CotaDTO cotaDTO = analiseParcialService.buscarDetalhesCota(numeroCota, codigoProduto);

        result.use(Results.json()).withoutRoot().from(cotaDTO).recursive().serialize();
    }

    public void percentualAbrangencia(Long estudoId) {
        BigDecimal percentualAbrangencia = analiseParcialService.calcularPercentualAbrangencia(estudoId);
        result.use(Results.json()).withoutRoot().from(percentualAbrangencia).serialize();
    }

    @Path("/init")
    public void init(Long id, String sortname, String sortorder, String filterSortName, Double filterSortFrom, Double filterSortTo, String elemento,
                     Long faixaDe, Long faixaAte, List<EdicoesProdutosDTO> edicoesBase, String modoAnalise, String codigoProduto, Long numeroEdicao, String numeroCotaStr) {

        AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
        queryDTO.setSortName(sortname);
        queryDTO.setSortOrder(sortorder);
        queryDTO.setFilterSortName(filterSortName);
        queryDTO.setFilterSortFrom(filterSortFrom);
        queryDTO.setFilterSortTo(filterSortTo);
        queryDTO.setElemento(elemento);
        queryDTO.setEdicoesBase(edicoesBase);
        queryDTO.setEstudoId(id);
        queryDTO.setFaixaDe(faixaDe);
        queryDTO.setFaixaAte(faixaAte);
        queryDTO.setModoAnalise(session.getAttribute("modoAnalise").toString());
        queryDTO.setCodigoProduto(codigoProduto);
        queryDTO.setNumeroEdicao(numeroEdicao);
        queryDTO.setNumeroCotaStr(numeroCotaStr);

        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);

        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = monta(lista);
        table.setPage(1);
        table.setTotal(50);
        validator.onErrorUse(Results.json()).withoutRoot().from(table).recursive().serialize();
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }

    @Path("/cotasQueNaoEntraramNoEstudo/filtrar")
    public void filtrar(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {

        List<CotaQueNaoEntrouNoEstudoDTO> lista = new ArrayList<>();

        if (queryDTO.possuiNome()) {
            queryDTO.setNome(PessoaUtil.removerSufixoDeTipo(queryDTO.getNome()));
        }

        if (queryDTO.getEstudo() != null && queryDTO.getEstudo() > 0) {
            lista = analiseParcialService.buscarCotasQueNaoEntraramNoEstudo(queryDTO);
        }

        TableModel<CellModelKeyValue<CotaQueNaoEntrouNoEstudoDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(lista)));
        table.setPage(1);
        table.setTotal(50);
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }

    @Path("/carregarEdicoesBaseEstudo")
    public void carregarEdicoesBaseEstudo(Long estudoId) {
        result.use(Results.json()).from(analiseParcialService.carregarEdicoesBaseEstudo(estudoId), "edicoesBase").recursive().serialize();
    }

    @Path("/mudarReparte")
    public void mudarReparte(Long numeroCota, Long estudoId, Long variacaoDoReparte) {
        analiseParcialService.atualizaReparte(estudoId, numeroCota, variacaoDoReparte);
        result.nothing();
    }

    @Post("/mudarReparteLote")
    public void mudarReparteLote(Long estudoId, List<CotaQueNaoEntrouNoEstudoDTO> cotas) {

        for (CotaQueNaoEntrouNoEstudoDTO cota : cotas) {
            analiseParcialService.atualizaReparte(estudoId, cota.getNumeroCota(), cota.getQuantidade().longValue());
            analiseParcialService.atualizaClassificacaoCota(estudoId, cota.getNumeroCota());
        }

        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.")).recursive().serialize();
    }

    @Path("/liberar")
    public void liberar(Long id) {
        analiseParcialService.liberar(id);
        result.nothing();
    }

    private TableModel<CellModelKeyValue<AnaliseParcialDTO>> monta(List<AnaliseParcialDTO> lista) {
        TableModel<CellModelKeyValue<AnaliseParcialDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(new ArrayList<>(lista)));
        return table;
    }

    @Get("/exportar")
    public void exportar(FileType fileType, Long id) throws IOException {

        AnaliseParcialQueryDTO queryDTO = new AnaliseParcialQueryDTO();
        queryDTO.setEstudoId(id);

        List<AnaliseParcialDTO> lista = analiseParcialService.buscaAnaliseParcialPorEstudo(queryDTO);

        if (lista.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
        }

        FileExporter.to("AJUSTE_REPARTE", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, lista, AnaliseParcialDTO.class,
                this.httpResponse);

        result.nothing();
    }

    @Post("/pesquisarProdutoEdicao")
    public void pesquisarProdutoEdicao(String codigoProduto, String nomeProduto, Long edicao) {
        List<ProdutoEdicaoVendaMediaDTO> edicoes = distribuicaoVendaMediaRepository.pesquisar(codigoProduto, nomeProduto, edicao);
        TableModel<CellModelKeyValue<ProdutoEdicaoVendaMediaDTO>> table = new TableModel<>();
        table.setRows(CellModelKeyValue.toCellModelKeyValue(edicoes));
        table.setTotal(edicoes.size());
        table.setPage(1);
        result.use(Results.json()).withoutRoot().from(table).recursive().serialize();
    }

    @Post
    public void defineRepartePorPDV(Long estudoId, Integer numeroCota, List<PdvDTO> reparteMap) {
        analiseParcialService.defineRepartePorPDV(estudoId, numeroCota, reparteMap);
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.")).recursive().serialize();
    }
}
