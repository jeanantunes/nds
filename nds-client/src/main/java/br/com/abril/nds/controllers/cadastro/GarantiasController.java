package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.service.GarantiaService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/fiador")
public class GarantiasController {

	public static final String LISTA_GARANTIAS_SALVAR_SESSAO = "garantiasSalvarSessao";
	
	public static final String LISTA_GARANTIAS_REMOVER_SESSAO = "garanitasRemoverSessao";
	
	@Autowired
	private GarantiaService garantiaService;
	
	private Result result;
	
	private HttpSession httpSession;
	
	public class GarantiaCadastrada{
		private Garantia garantia;
		
		private Integer referencia;

		public Garantia getGarantia() {
			return garantia;
		}

		public void setGarantia(Garantia garantia) {
			this.garantia = garantia;
		}

		public Integer getReferencia() {
			return referencia;
		}

		public void setReferencia(Integer referencia) {
			this.referencia = referencia;
		}
	}
	
	public GarantiasController(Result result, HttpSession httpSession){
		this.result = result;
		this.httpSession = httpSession;
	}
	
	@Post
	public void obterGarantiasFiador(String sortname, String sortorder){
		List<GarantiaCadastrada> listaGarantiasSessao = this.obterListaGarantiasSessao();
		
		Long idFiador = (Long) this.httpSession.getAttribute(FiadorController.ID_FIADOR_EDICAO);
		
		if (idFiador != null){
			
			Set<Long> idsIgnorar = new HashSet<Long>();
			
			for (GarantiaCadastrada ga : listaGarantiasSessao){
				
				if (ga.getReferencia() != null){
					
					idsIgnorar.add(ga.getReferencia().longValue());
				}
			}
			
			idsIgnorar.addAll(this.obterListaGarantiasRemoverSessao());
			
			List<Garantia> listaGarantiaFiador = 
					this.garantiaService.obterGarantiasFiador(idFiador, idsIgnorar);
			
			for (Garantia g : listaGarantiaFiador){
				GarantiaCadastrada garantiaCadastrada = new GarantiaCadastrada();
				garantiaCadastrada.setGarantia(g);
				garantiaCadastrada.setReferencia(g.getId().intValue());
				
				listaGarantiasSessao.add(garantiaCadastrada);
			}
		}
		
		if (sortname != null) {

			sortorder = sortorder == null ? "asc" : sortorder;

			Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
			
			PaginacaoUtil.ordenarEmMemoria(listaGarantiasSessao, ordenacao, sortname).toArray();
		}
		
		
		
		this.result.use(Results.json()).from(this.getTableModelListaGarantias(listaGarantiasSessao), "result").recursive().serialize();
	}
	
	@Post
	public void adicionarGarantia(Garantia garantia, Integer referencia){
		
		this.validarDadosEntrada(garantia);
		
		List<GarantiaCadastrada> listaGarantiasSessao = this.obterListaGarantiasSessao();
		
		boolean novaGarantia = true;
		
		for (int index = 0 ; index < listaGarantiasSessao.size() ; index++){
			if (listaGarantiasSessao.get(index).getReferencia().equals(referencia)){
				listaGarantiasSessao.get(index).setGarantia(garantia);
				novaGarantia = false;
				break;
			}
		}
		
		if (novaGarantia){
			
			Garantia gar = null;
			
			if (referencia != null){
				gar = this.garantiaService.buscarGarantiaPorId(referencia.longValue());
			}
			
			GarantiaCadastrada novaGarantiaCadastrada = new GarantiaCadastrada();
			
			if (gar != null){
				
				novaGarantiaCadastrada.setReferencia(gar.getId().intValue());
				garantia.setId(gar.getId());
			} else {
				
				novaGarantiaCadastrada.setReferencia((int) new Date().getTime());
			}
			
			listaGarantiasSessao.add(novaGarantiaCadastrada);
			
			novaGarantiaCadastrada.setGarantia(garantia);
		} 
		
		this.httpSession.setAttribute(LISTA_GARANTIAS_SALVAR_SESSAO, listaGarantiasSessao);
		
		this.obterGarantiasFiador(null, null);
	}
	
	private void validarDadosEntrada(Garantia garantia) {
		
		List<String> msgsValidacao = new ArrayList<String>();
		
		if (garantia == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Valor R$ é obrigatório.");
		}
		
		if (garantia.getValor() == null){
			msgsValidacao.add("Valor R$ é obrigatório.");
		}
		
		if (garantia.getDescricao() == null || garantia.getDescricao().trim().isEmpty()){
			msgsValidacao.add("Descrição é obrigatório.");
		}
		
		if (!msgsValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgsValidacao));
		}
	}

	@Post
	public void editarGarantia(Integer referencia){
		
		Garantia garantia = null;
		
		for (GarantiaCadastrada garantiaCadastrada : this.obterListaGarantiasSessao()){
			if (garantiaCadastrada.getReferencia().equals(referencia)){
				garantia = garantiaCadastrada.getGarantia();
				break;
			}
		}
		
		if (garantia == null){
			garantia = this.garantiaService.buscarGarantiaPorId(referencia.longValue());
		}
		
		this.result.use(Results.json()).from(garantia != null ? garantia : "", "result").serialize();
	}
	
	public void excluirGarantia(Integer referencia){
		
		List<GarantiaCadastrada> garantiasSalvar = this.obterListaGarantiasSessao();
		
		GarantiaCadastrada garantiaCadastradaRemover = null;
		
		for (int index = 0 ; index < garantiasSalvar.size() ; index++){
			if (garantiasSalvar.get(index).getReferencia().equals(referencia)){
				garantiaCadastradaRemover = garantiasSalvar.remove(index);
				
				this.httpSession.setAttribute(LISTA_GARANTIAS_SALVAR_SESSAO, garantiasSalvar);
				break;
			}
		}
		
		Set<Long> garantiasRemover = this.obterListaGarantiasRemoverSessao();
		
		if (garantiaCadastradaRemover != null && garantiaCadastradaRemover.getGarantia().getId() != null){
			
			garantiasRemover.add(garantiaCadastradaRemover.getGarantia().getId());
		} else {
			
			garantiasRemover.add(referencia.longValue());
		}
		
		this.httpSession.setAttribute(LISTA_GARANTIAS_REMOVER_SESSAO, garantiasRemover);
		
		this.obterGarantiasFiador(null, null);
	}
	
	@SuppressWarnings("unchecked")
	private List<GarantiaCadastrada> obterListaGarantiasSessao(){
		List<GarantiaCadastrada> listaGarantiasSessao = 
				(List<GarantiaCadastrada>) this.httpSession.getAttribute(LISTA_GARANTIAS_SALVAR_SESSAO);
		
		if (listaGarantiasSessao == null){
			listaGarantiasSessao = new ArrayList<GarantiaCadastrada>();
		}
		
		return listaGarantiasSessao;
	}
	
	@SuppressWarnings("unchecked")
	private Set<Long> obterListaGarantiasRemoverSessao(){
		Set<Long> listaGarantiasSessao = 
				(Set<Long>) this.httpSession.getAttribute(LISTA_GARANTIAS_REMOVER_SESSAO);
		
		if (listaGarantiasSessao == null){
			listaGarantiasSessao = new HashSet<Long>();
		}
		
		return listaGarantiasSessao;
	}
	
	private TableModel<CellModel> getTableModelListaGarantias(List<GarantiaCadastrada> listaGarantias) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (GarantiaCadastrada garantiaCadastrada : listaGarantias) {
			
			CellModel cellModel = new CellModel(
				garantiaCadastrada.getReferencia(), 
				garantiaCadastrada.getGarantia().getDescricao(),
				CurrencyUtil.formatarValor(garantiaCadastrada.getGarantia().getValor())
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaCellModel.size());

		return tableModel;
	}
}
