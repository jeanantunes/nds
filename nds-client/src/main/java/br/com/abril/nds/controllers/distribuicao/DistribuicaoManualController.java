package br.com.abril.nds.controllers.distribuicao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.dto.EstudoDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.model.planejamento.TipoGeracaoEstudo;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.upload.XlsUploaderUtils;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicaoManual")
@Resource
public class DistribuicaoManualController extends BaseController {

    @Autowired
    private Result result;
    
    @Autowired
    private CotaService cotaService;

    @Autowired
    private EstudoService estudoService;
    
    @Autowired
    private ProdutoEdicaoService produtoEdicaoService;

    @Autowired
    private LancamentoService lancamentoService;
    
    @Autowired
    private MatrizDistribuicaoService matrizDistribuicaoService;
    
	@Autowired
	private HttpSession session;
    
    @Path("/")
    public void index(ProdutoDistribuicaoVO produto, ProdutoDistribuicaoVO produtoDistribuicaoVO) {
    	result.include("produto", produto);
    	session.setAttribute(ProdutoDistribuicaoVO.class.getName(), produtoDistribuicaoVO);
    }
    
    @Post
    @Path("/consultarCotaPorNumero")
    public void consultarCotaPorNumero(Integer numeroCota) throws Exception {
		CotaDTO cotaDTO = new CotaDTO();
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		if (cota == null) {
		    throw new ValidacaoException(TipoMensagem.WARNING, "Não foi encontrada nenhuma cota com este número.");
		} else {
		    cotaDTO.setNumeroCota(cota.getNumeroCota());
		    cotaDTO.setIdCota(cota.getId());
		    cotaDTO.setNomePessoa(cota.getPessoa().getNome());
		    cotaDTO.setStatus(cota.getSituacaoCadastro());
		}
		result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
    }

    @Post
    @Path("/consultarCotaPorNome")
    public void consultarCotaPorNome(String nomeCota) {
		List<ItemAutoComplete> lista = new ArrayList<>();
		for (CotaDTO cota : this.cotaService.obterPorNomeAutoComplete(nomeCota)) {
		    ItemAutoComplete item = new ItemAutoComplete(cota.getNumeroCota().toString(), cota.getNomePessoa(), cota);
		    lista.add(item);
		}
		this.result.use(Results.json()).from(lista, "result").include("value", "chave").serialize();
    }
	    
    @Post
    @Path("/gravarEstudo")
    public void gravarEstudo(EstudoDTO estudoDTO, List<EstudoCotaDTO> estudoCotasDTO) throws Exception {
    	EstudoGerado estudo = new EstudoGerado();
		estudo.setProdutoEdicao(new ProdutoEdicao(estudoDTO.getProdutoEdicaoId()));
		estudo.setReparteDistribuir(BigInteger.valueOf(estudoDTO.getReparteDistribuir()));
		estudo.setQtdeReparte(BigInteger.valueOf(estudoDTO.getReparteDistribuido()));
		estudo.setDataCadastro(new Date());
		estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
		estudo.setLiberado(false);
		estudo.setUsuario(getUsuarioLogado());
		estudo.setTipoGeracaoEstudo(TipoGeracaoEstudo.MANUAL);
		
		try {
		    estudo.setDataLancamento(new SimpleDateFormat("dd/MM/yyyy").parse(estudoDTO.getDataLancamento()));
		} catch (ParseException e) {
		    throw new Exception("Data de lançamento em formato incorreto.");
		}
		
		for (EstudoCotaDTO cotaDTO : estudoCotasDTO) {
		    EstudoCotaGerado estudoCota = new EstudoCotaGerado();
		    estudoCota.setCota(new Cota(cotaDTO.getIdCota()));
		    estudoCota.setQtdePrevista(cotaDTO.getQtdeEfetiva());
		    estudoCota.setQtdeEfetiva(cotaDTO.getQtdeEfetiva());
		    estudoCota.setReparte(cotaDTO.getQtdeEfetiva());
		    estudoCota.setReparteInicial(cotaDTO.getQtdeEfetiva());
		    estudoCota.setClassificacao(ClassificacaoCota.InclusaoManualCotas.getCodigo());
		    estudoCota.setTipoEstudo(TipoEstudoCota.NORMAL);
		    estudo.getEstudoCotas().add(estudoCota);
		}
		estudoService.gravarEstudo(estudo);
		estudoService.setIdLancamentoNoEstudo(estudoDTO.getLancamentoId(), estudo.getId());
		
		removeItensDuplicadosMatrizDistribuicao();
		
		this.matrizDistribuicaoService.atualizarPercentualAbrangencia(estudo.getId());
		
		result.use(Results.json()).from(estudo.getId(), "result").serialize();
    }
    
    @Post
	@Path("/uploadArquivoLoteDistbManual")
	public void uploadArquivoEmLote(UploadedFile excelFileFixacao) throws FileNotFoundException, IOException{
    	
    	
    	
    	
    	
//		List<FixacaoReparteDTO> listaRegistrosInvalidosExcel=null;
//		List<FixacaoReparteDTO> listaFixacaoExcel = XlsUploaderUtils.getBeanListFromXls(FixacaoReparteDTO.class, excelFileFixacao);
//
//		if (!isListaVazia(listaFixacaoExcel)) {
//			
//			listaRegistrosInvalidosExcel = obterListaInvalidos(listaFixacaoExcel);
//			listaFixacaoExcel.removeAll(listaRegistrosInvalidosExcel);
//			
//			for (FixacaoReparteDTO fixacaoReparteDTO : listaFixacaoExcel) {
//				FixacaoReparte fixacaoReparte = fixacaoReparteService.adicionarFixacaoReparte(fixacaoReparteDTO);
//				if(fixacaoReparte.getCotaFixada().getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {
//                    getErrosUpload().add(
//                            "- Fixação inserida, status da Cota: " + SituacaoCadastro.SUSPENSO.toString() + ". (Cota["
//                                + fixacaoReparteDTO.getCotaFixadaString() + "] Produto["
//                                + fixacaoReparteDTO.getProdutoFixado() + "]).");
//				}
//			}
//			
//			if (listaRegistrosInvalidosExcel.isEmpty() && getErrosUpload().isEmpty()) {
//				result.use(Results.json()).from(SUCCESS_MSG, "result").recursive().serialize();
//			} else {
//				result.use(Results.json()).from(
//						new ValidacaoVO(TipoMensagem.WARNING, getMsgErroUpload()), 
//						"result").recursive().serialize();
//			}
//			
//		} else {
//			result.use(Results.json()).from(
//new ValidacaoVO(TipoMensagem.WARNING, "Arquivo está vazio."),
//					"result").recursive().serialize();
//		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Arquivo está vazio."),"result").recursive().serialize();
	}
    
    private void removeItensDuplicadosMatrizDistribuicao() {
    	
    	ProdutoDistribuicaoVO vo = (ProdutoDistribuicaoVO)session.getAttribute(ProdutoDistribuicaoVO.class.getName());
    	MatrizDistribuicaoController matrizDistribuicaoController = new MatrizDistribuicaoController();
    	matrizDistribuicaoController.setSession(session);
    	matrizDistribuicaoController.removeItemListaDeItensDuplicadosNaSessao(vo.getIdLancamento(), vo.getIdCopia());
		session.removeAttribute(ProdutoDistribuicaoVO.class.getName());
    }
}
