package br.com.abril.nds.controllers.lancamento;


import static br.com.caelum.vraptor.view.Results.json;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.estudocomplementar.BaseEstudoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.controllers.distribuicao.MatrizDistribuicaoController;
import br.com.abril.nds.dto.EstudoComplementarDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoComplementarService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/lancamento")
public class EstudoComplementarController extends BaseController {

    @Autowired
    private Result result;
    
    @Autowired
    private HttpSession session;

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
    public void index(Long estudoId, Long idProdutoEdicao, Long idLancamento, BigInteger reparte, BigInteger reparteDistribuido, BigInteger sobra, Long idCopia) {

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
        result.include("idCopia", idCopia);
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
        baseEstudo.setSegmentoDoProduto(estudo.getTipoSegmentoProduto());

        result.use(json()).from(baseEstudo).serialize();
    }

    @Path("/gerarEstudo")
    @Post
    public void gerarEstudo(EstudoComplementarVO parametros) throws Exception {
    	
    	Long qtdEstudoParaLancamento = 0L;
    	Date dataLanctoFormatada;
    	
    	try {
    		dataLanctoFormatada = new SimpleDateFormat("dd/MM/yyyy").parse(parametros.getDataLancamento());
		} catch (ParseException e) {
			 throw new Exception("Data de lançamento em formato incorreto.");
		}

    	qtdEstudoParaLancamento = estudoService.countEstudosPorLancamento(parametros.getIdLancamento(), dataLanctoFormatada);
    	
    	if(qtdEstudoParaLancamento >= 3){
    		throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Este lançamento já possui o máximo de 3 estudos gerados."));
    	}
    	
    	Long idEstudo = this.estudoComplementarService.gerarEstudoComplementar(parametros);
    	
        estudoService.setIdLancamentoNoEstudo(parametros.getIdLancamento(), parametros.getIdEstudoComplementar());

        this.removerItemSessao(parametros.getIdLancamento(), parametros.getIdCopia());
        
        result.use(CustomJson.class).put("result", idEstudo).serialize();
    }
    
    private void removerItemSessao(Long idLancamento, Long idCopia) {
    	
    	if (idCopia == null) {
    		
    		return;
    	}
    	
    	List<ProdutoDistribuicaoVO> lista = this.obterListaDeItensDuplicadosNaSessao();
    	
    	ProdutoDistribuicaoVO produtoDistribuicaoRemover = null;
    	
    	for (ProdutoDistribuicaoVO produtoDistribuicao : lista) {
    		
    		if (idLancamento.equals(produtoDistribuicao.getIdLancamento().longValue())
    				&& idCopia.equals(produtoDistribuicao.getIdCopia().longValue())) {
    			
    			produtoDistribuicaoRemover = produtoDistribuicao;
    		}
    	}

    	if (produtoDistribuicaoRemover != null) {
    	
    		lista.remove(produtoDistribuicaoRemover);
    	}
    }
    
    @SuppressWarnings("unchecked")
    private List<ProdutoDistribuicaoVO> obterListaDeItensDuplicadosNaSessao() {
    	
        return (List<ProdutoDistribuicaoVO>) session.getAttribute(MatrizDistribuicaoController.LISTA_DE_DUPLICACOES);
    }
    
}
