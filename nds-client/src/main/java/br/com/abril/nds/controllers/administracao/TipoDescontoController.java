package br.com.abril.nds.controllers.administracao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.TipoDescontoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.TipoDescontoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/tipoDescontoCota")
public class TipoDescontoController {
	
	private Result result;
	
	@Autowired
	private TipoDescontoService tipoDescontoCotaService;
	
	@Autowired
	private CotaService cotaService;
	
	@SuppressWarnings("unused")
	private HttpSession httpSession;
	
	public TipoDescontoController(Result result, HttpSession httpSession) {
		this.result = result; 
		this.httpSession = httpSession;
	}
	
	@Path("/")
	public void index() {
		
		inserirDataAtual();
		
	}
	
	private void inserirDataAtual() {		
		result.include("dataAtual", DateUtil.formatarData(new Date(), "dd/MM/yyyy"));
	}
	
	@Post
	@Path("/novoDescontoGeral")
	public void novoDescontoGeral(String desconto, String dataAlteracao, String usuario){
		try {
			TipoDesconto tipoDesconto = new TipoDesconto();
			BigDecimal descontoFormatado = new BigDecimal(Double.parseDouble(desconto));
			tipoDesconto.setPorcentagem(new BigDecimal(Double.parseDouble(desconto)));
			SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
			Date dataFormatada;
			dataFormatada = sdf.parse(dataAlteracao);
			tipoDesconto.setDataAlteracao(dataFormatada);
			tipoDesconto.setUsuario(usuario);
			tipoDesconto.setEspecificacaoDesconto(EspecificacaoDesconto.GERAL);
			
			atualizarDistribuidor(descontoFormatado);

			this.tipoDescontoCotaService.incluirDescontoGeral(tipoDesconto);		

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}
	
	private void atualizarDistribuidor(BigDecimal desconto) {
		this.tipoDescontoCotaService.atualizarDistribuidores(desconto);
	}
	
	@Post
	@Path("/novoDescontoEspecifico")
	public void novoDescontoEspecifico(String cotaEspecifica, String nomeEspecifico, Long descontoEspecifico, Date dataAlteracaoEspecifico, String usuarioEspecifico){
		
		Cota cotaParaAtualizar = this.tipoDescontoCotaService.obterCota(Integer.parseInt(cotaEspecifica));
		
		atualizarCota(descontoEspecifico, cotaParaAtualizar);

		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}
	
	
	private void atualizarCota(Long descontoEspecifico, Cota cotaParaAtualizar) {
		
	}

	@Path("/pesquisarDescontoGeral")
	public void pesquisarDescontoGeral(String sortorder, String sortname, int page, int rp) throws Exception {
		
		List<TipoDescontoVO> listaDescontoCotaVO = null;
		
		try {			
			listaDescontoCotaVO = tipoDescontoCotaService.obterTipoDescontoGeral();
			} catch (Exception e) {

			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}
		
		if (listaDescontoCotaVO == null || listaDescontoCotaVO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			int qtdeTotalRegistros = listaDescontoCotaVO.size();
		
			TableModel<CellModelKeyValue<TipoDescontoVO>> tableModel =
					new TableModel<CellModelKeyValue<TipoDescontoVO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDescontoCotaVO));
			//tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
	
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

		}
	}

}
