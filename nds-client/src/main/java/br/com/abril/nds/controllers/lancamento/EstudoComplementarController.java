package br.com.abril.nds.controllers.lancamento;


import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.client.vo.estudocomplementar.BaseEstudoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EstudoComplementarDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoComplementarService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Date;

import static br.com.caelum.vraptor.view.Results.json;

@Resource
@Path("/lancamento")
public class EstudoComplementarController extends BaseController {

    @Autowired
    private Result result;

    @Autowired
    private HttpServletResponse httpResponse;

    @Autowired
    private CalendarioService calendarioService;

    @Autowired
    private EstudoComplementarService estudoComplementarService;

    @Autowired
    private ProdutoEdicaoService produtoEdicaoService;

    @Autowired
    private EstudoService estudoService;

    @Path("/estudoComplementar")
    public void index(Long estudoId, Long idProdutoEdicao, Long idLancamento, BigInteger reparte, BigInteger reparteDistribuido, BigInteger sobra) {

        String data = DateUtil.formatarDataPTBR(new Date());
        ProdutoEdicao produto = produtoEdicaoService.buscarPorID(idProdutoEdicao);

        result.include("reparteDisponivel", reparte);
        result.include("sobra", sobra);
        result.include("reparteDistribuido", reparteDistribuido);
        result.include("pacotePadrao", produto.getPacotePadrao());
        result.include("data", data);
        result.include("estudoId", estudoId);
        result.include("idProdutoEdicao", idProdutoEdicao);
        result.include("idLancamento", idLancamento);
    }

    @Path("/pesquisaEstudoBase/{estudoBase.id}")
    public void pesquisaEstudoBase(EstudoCotaDTO estudoBase) {

        EstudoComplementarDTO estudo = estudoComplementarService.obterEstudoComplementarPorIdEstudoBase(estudoBase.getId());
        BaseEstudoVO baseEstudo = new BaseEstudoVO();

        baseEstudo.setIdEstudo(estudo.getIdEstudo());
        baseEstudo.setIdEstudoComplementar(estudo.getIdEstudoComplementar());
        baseEstudo.setIdProduto(estudo.getIdProduto());
        baseEstudo.setNomeProduto(estudo.getNomeProduto());

        baseEstudo.setIdEdicao(estudo.getIdEdicao());
        baseEstudo.setNomeClassificacao(estudo.getNomeClassificacao());
        baseEstudo.setIdPublicacao(estudo.getIdPublicacao());
        baseEstudo.setIdPEB(estudo.getIdPEB());
        baseEstudo.setNomeFornecedor(estudo.getNomeFornecedor());
        baseEstudo.setDataLncto(estudo.getDataLncto());
        baseEstudo.setDataRclto(estudo.getDataRclto());
        baseEstudo.setNumeroEdicao(estudo.getNumeroEdicao());
        baseEstudo.setCodigoProduto(estudo.getCodigoProduto());

        result.use(json()).from(baseEstudo).serialize();
    }

    @Path("/gerarEstudo")
    @Post
    public void gerarEstudo(EstudoComplementarVO parametros) {
        if (estudoComplementarService.gerarEstudoComplementar(parametros)) {
            estudoService.setIdLancamentoNoEstudo(parametros.getIdLancamento(), parametros.getIdEstudoComplementar());
        }

        result.nothing();
    }
}
