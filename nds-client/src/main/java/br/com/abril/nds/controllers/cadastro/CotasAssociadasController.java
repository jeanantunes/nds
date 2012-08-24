package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FiadorService;
import br.com.abril.nds.util.CellModel;
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
public class CotasAssociadasController {

	public static final String LISTA_COTAS_ASSOCIADAS_SALVAR_SESSAO = "cotasAssociadasSalvarSessao";
	
	public static final String LISTA_COTAS_ASSOCIADAS_REMOVER_SESSAO = "cotasAssociadasRemoverSessao";
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FiadorService fiadorService;
	
	private Result result;
	
	private HttpSession httpSession;
	
	public class AssociacaoCota{
		private Integer numeroCota;
		
		private String nomeCota;
		
		private Long referencia;

		public Integer getNumeroCota() {
			return numeroCota;
		}

		public void setNumeroCota(Integer numeroCota) {
			this.numeroCota = numeroCota;
		}

		public String getNomeCota() {
			return nomeCota;
		}

		public void setNomeCota(String nomeCota) {
			this.nomeCota = nomeCota;
		}

		public Long getReferencia() {
			return referencia;
		}

		public void setReferencia(Long referencia) {
			this.referencia = referencia;
		}
	}
	
	public CotasAssociadasController(Result result, HttpSession httpSession){
		this.result = result;
		this.httpSession = httpSession;
	}
	
	@Post
	public void obterAssociacoesCotaFiador(String sortname, String sortorder){
		List<AssociacaoCota> listaAssociacaoSessao = this.obterListaAssociacaoSalvar();
		
		Long idFiador = (Long) this.httpSession.getAttribute(FiadorController.ID_FIADOR_EDICAO);
		
		List<AssociacaoCota> listaExibir = new ArrayList<CotasAssociadasController.AssociacaoCota>();
		
		listaExibir.addAll(listaAssociacaoSessao);
		
		if (idFiador != null){
			//buscar no server
			List<Cota> listaAssociacaoCotaFiador = 
					this.fiadorService.obterCotasAssociadaFiador(idFiador, this.obterListaAssociacaoRemover());
			
			for (Cota cota : listaAssociacaoCotaFiador){
				AssociacaoCota associacaoCota = new AssociacaoCota();
				associacaoCota.setNumeroCota(cota.getNumeroCota());
				associacaoCota.setReferencia(cota.getId());
				if (cota.getPessoa() instanceof PessoaFisica){
					associacaoCota.setNomeCota(((PessoaFisica) cota.getPessoa()).getNome());
				} else if (cota.getPessoa() instanceof PessoaJuridica){
					associacaoCota.setNomeCota(((PessoaJuridica) cota.getPessoa()).getRazaoSocial());
				}
				
				listaExibir.add(associacaoCota);
			}
		}
		
		if (sortname != null) {

			sortorder = sortorder == null ? "asc" : sortorder;

			Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
			
			PaginacaoUtil.ordenarEmMemoria(listaExibir, ordenacao, sortname);
		}
		
		this.result.use(Results.json()).from(getTableModelListaAssociacao(listaExibir), "result").recursive().serialize();
	}
	
	@Post
	public void pesquisarNomeCotaPorNumeroCota(Integer numeroCota){
		
		String nome = null;
		if (numeroCota != null){
			
			nome = this.cotaService.obterNomeResponsavelPorNumeroDaCota(numeroCota);
		}
		
		this.result.use(Results.json()).withoutRoot().from(nome != null ? nome : "").recursive().serialize();
	}
	
	@Post
	public void adicionarAssociacaoCota(Integer numeroCota, String nomeCota){
		
		if (numeroCota == null || nomeCota == null || nomeCota.trim().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota é obrigatório.");
		}
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if(cota.getFiador() != null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Esta cota já possui um fiador.");
		}
		
		List<AssociacaoCota> listaAssociacao = this.obterListaAssociacaoSalvar();
		
		boolean add = true;
		
		for (AssociacaoCota associacaoCota : listaAssociacao){
			if (associacaoCota.getNumeroCota().equals(numeroCota)){
				add = false;
				break;
			}
		}
		
		if (add){
			
			Long idFiador = (Long) this.httpSession.getAttribute(FiadorController.ID_FIADOR_EDICAO);
			
			if (idFiador != null){
				
				add = !this.fiadorService.verificarAssociacaoFiadorCota(idFiador, numeroCota, this.obterListaAssociacaoRemover());
			}
		}
		
		if (add){
			
			AssociacaoCota associacaoCota = new AssociacaoCota();
			associacaoCota.setReferencia(new Long((int)new Date().getTime()));
			associacaoCota.setNumeroCota(numeroCota);
			associacaoCota.setNomeCota(nomeCota);
			
			listaAssociacao.add(associacaoCota);
			
			this.httpSession.setAttribute(LISTA_COTAS_ASSOCIADAS_SALVAR_SESSAO, listaAssociacao);
		}
		
		this.obterAssociacoesCotaFiador(null, null);
	}
	
	@Post
	public void removerAssociacaoCota(Long referencia){
		List<AssociacaoCota> listaAssociacaoSessao = this.obterListaAssociacaoSalvar();
		
		AssociacaoCota associacaoCotaRemover = null;
		
		for (int index = 0 ; index < listaAssociacaoSessao.size() ; index++){
			if (listaAssociacaoSessao.get(index).getReferencia().equals(referencia)){
				associacaoCotaRemover = listaAssociacaoSessao.remove(index);
				
				this.httpSession.setAttribute(LISTA_COTAS_ASSOCIADAS_SALVAR_SESSAO, listaAssociacaoSessao);
				break;
			}
		}
		
		Set<Long> setAssociacaoRemover = this.obterListaAssociacaoRemover();
		if (associacaoCotaRemover != null && associacaoCotaRemover.getNumeroCota() != null){
			
			setAssociacaoRemover.add(associacaoCotaRemover.getReferencia().longValue());
		} else {
			
			setAssociacaoRemover.add(referencia.longValue());
		}
		
		this.httpSession.setAttribute(LISTA_COTAS_ASSOCIADAS_REMOVER_SESSAO, setAssociacaoRemover);
		
		this.obterAssociacoesCotaFiador(null, null);
	}
	
	@SuppressWarnings("unchecked")
	private List<AssociacaoCota> obterListaAssociacaoSalvar(){
		List<AssociacaoCota> listaAssociacao = (List<AssociacaoCota>) 
				this.httpSession.getAttribute(LISTA_COTAS_ASSOCIADAS_SALVAR_SESSAO);
		
		if (listaAssociacao == null){
			listaAssociacao = new ArrayList<AssociacaoCota>();
		}
		
		return listaAssociacao;
	}
	
	@SuppressWarnings("unchecked")
	private Set<Long> obterListaAssociacaoRemover(){
		Set<Long> listaAssociacao = (Set<Long>) 
				this.httpSession.getAttribute(LISTA_COTAS_ASSOCIADAS_REMOVER_SESSAO);
		
		if (listaAssociacao == null){
			listaAssociacao = new HashSet<Long>();
		}
		
		return listaAssociacao;
	}
	
	private TableModel<CellModel> getTableModelListaAssociacao(Collection<AssociacaoCota> lista) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		for (AssociacaoCota associacaoCota : lista) {
			
			CellModel cellModel = new CellModel(
				associacaoCota.getReferencia().intValue(), 
				associacaoCota.getNumeroCota(),
				associacaoCota.getNomeCota()
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaCellModel.size()); 

		return tableModel;
	}
}
