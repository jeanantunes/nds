package br.com.abril.nds.controllers.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.model.cadastro.CotaBaseCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaBaseCotaService;
import br.com.abril.nds.service.CotaBaseService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cotaBase")
public class CotaBaseController {
	
	@Autowired
	private CotaBaseService cotaBaseService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private CotaBaseCotaService cotaBaseCotaService; 
	
	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_COTA_BASE)
	public void index(){
		
	}
	
	@Post
	@Path("/pesquisarCotaNova")
	public void pesquisarCotaNova(Integer numeroCota){
		tratarFiltroPesquisaCota(numeroCota);
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCota);
		
		boolean existeCotaBase = false;
		if(cotaBase != null){
			existeCotaBase = this.cotaBaseCotaService.isCotaBaseAtiva(cotaBase);			
		}
		
		FiltroCotaBaseDTO filtro = null;
		
		if(existeCotaBase){
			filtro = this.cotaBaseService.obterCotaDoFiltro(cotaBase);
			filtro.setDiasRestantes(calcularDiasRestantes(filtro.getDataInicial(), filtro.getDataFinal()));
		}else{
			filtro = this.cotaBaseService.obterDadosFiltro(cotaBase, false, true, numeroCota);
		}
		
		this.result.use(Results.json()).from(filtro, "result").recursive().serialize();		
	}

	private String calcularDiasRestantes(Date inicial, Date fina) {		
		
		Calendar dtFinal = Calendar.getInstance();
		dtFinal.setTime(fina);
		
		Calendar dtInicial = Calendar.getInstance();
		dtInicial.setTime(inicial);
		
		long m1 = dtFinal.getTimeInMillis();
		long m2 = dtInicial.getTimeInMillis();
		
		Integer diasRestantes = (int) ((m1 - m2) / (24*60*60*1000));
		
		return diasRestantes.toString();
	}

	private void tratarFiltroPesquisaCota(Integer numeroCota) {
		if(numeroCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota inválido!");
		}
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
			
		}else if(cota.getSituacaoCadastro() != SituacaoCadastro.ATIVO){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não está ativa!");
			
		}
	}
	
	@Post
	@Path("/pesquisarCotasBase")
	public void pesquisarCotasBase(Integer numeroCota){
		
		List<CotaBaseDTO> listaCotaBase = obterListaDeCotasBase(numeroCota, null);
		
		int qtdeInicialPadrao = 3;
		
		for (int indice = listaCotaBase.size(); indice < 3 ; indice++) {
			
			CotaBaseDTO produtoVO = new CotaBaseDTO();

			listaCotaBase.add(produtoVO);
		}
		
		TableModel<CellModelKeyValue<CotaBaseDTO>> tableModel =
				new TableModel<CellModelKeyValue<CotaBaseDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaBase));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}

	
	@Post
	@Path("/pesquisarCotasBasePesquisaGeral")
	public void pesquisarCotasBasePesquisaGeral(Integer numeroCota, String sortorder, String sortname, int page, int rp){
		
		CotaBaseDTO dto = new CotaBaseDTO();
		dto.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		List<CotaBaseDTO> listaCotaBase = obterListaDeCotasBase(numeroCota, dto);
		
		if(listaCotaBase.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Nenhum registro encontrado.");
		}
		List<CotaBaseDTO> listaFormatada = new ArrayList<CotaBaseDTO>();
		
		for(CotaBaseDTO cotaBase : listaCotaBase){
			cotaBase.setDiasRestantes(this.calcularDiasRestantes(cotaBase.getDtInicio(), cotaBase.getDtFinal()));
			listaFormatada.add(cotaBase);
		}
		
		
		TableModel<CellModelKeyValue<CotaBaseDTO>> tableModel =
				new TableModel<CellModelKeyValue<CotaBaseDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFormatada));
		
		tableModel.setPage(dto.getPaginacao().getPaginaAtual());

		tableModel.setTotal(dto.getPaginacao().getQtdResultadosTotal());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private List<CotaBaseDTO> obterListaDeCotasBase(Integer numeroCota, CotaBaseDTO dto) {
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCota);
		List<CotaBaseDTO> listaCotaBase = new ArrayList<CotaBaseDTO>();
		if(cotaBase != null){
			listaCotaBase = this.cotaBaseService.obterCotasBases(this.cotaBaseService.obterCotaNova(numeroCota),dto );			
		}
		return listaCotaBase;
	}
	
	@Post
	@Path("/obterCota")
	public void obterCota(Integer numeroCota){
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCota);
		boolean existeCotaBase = false;
		if(cotaBase != null){
			 existeCotaBase = this.cotaBaseCotaService.isCotaBaseAtiva(cotaBase);
		}
		
		FiltroCotaBaseDTO filtro = null;
		
		if(cota.getSituacaoCadastro() != SituacaoCadastro.ATIVO){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não está ativa!");
		}else if(existeCotaBase){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" tem cota base ativa!");
		}else{			
			filtro = this.cotaBaseService.obterDadosFiltro(cotaBase, true, true, numeroCota);			
		}
		
		this.result.use(Results.json()).from(filtro, "result").recursive().serialize();
		
	}
	@Post
	@Path("/excluirCotaBase")
	public void excluirCotaBase(Integer numeroCotaNova, Long idCotaBase){
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCotaNova);
		
		Cota cotaParaDesativar = this.cotaService.obterPorId(idCotaBase);
		
		Long qtdDeCotasBaseAtivas = this.cotaBaseCotaService.quantidadesDeCotasAtivas(cotaBase);
		
		if(qtdDeCotasBaseAtivas >= 2){
			cotaBaseCotaService.desativarCotaBase(cotaBase, cotaParaDesativar);			
		}else{
			throw new ValidacaoException(TipoMensagem.ERROR, "Não pode deixar a cota principal sem cota base.");
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cota excluida com sucesso."), "result").recursive().serialize();
		
	}
	
	@Post
	@Path("/confirmarCotasBase")
	public void confirmarCotasBase(Integer[] numerosDeCotasBase, Integer idCotaNova, BigDecimal indiceAjuste){
		
		Cota cotaNova = this.cotaService.obterPorNumeroDaCota(idCotaNova);	
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(idCotaNova);
		
		if(cotaBase != null){
			
			List<CotaBaseDTO> listaCotaBase = this.cotaBaseService.obterCotasBases(cotaBase, null);		
			
			validarCotasDigitadas(numerosDeCotasBase, listaCotaBase, idCotaNova);
		}
		
		salvar(numerosDeCotasBase, indiceAjuste, cotaNova, cotaBase);			
		
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cota base cadastrada com sucesso!"), "result").recursive().serialize();
		
	}

	private void salvar(Integer[] numerosDeCotasBase, BigDecimal indiceAjuste, Cota cotaNova, CotaBase cotaBaseJaSalva) {
		if(cotaBaseJaSalva == null){
			CotaBase cotaBase = new CotaBase();
			cotaBase.setDataInicio(new Date());
			cotaBase.setDataFim(DateUtil.adicionarDias(new Date(), 180));
			cotaBase.setIndiceAjuste(indiceAjuste);
			cotaBase.setCota(cotaNova);
			this.cotaBaseService.salvar(cotaBase);
			for(Integer cotabBase: numerosDeCotasBase){
				CotaBaseCota cotaBaseCota = new CotaBaseCota();
				Cota cotaBaseParaSalvar = this.cotaService.obterPorNumeroDaCota(cotabBase);
				cotaBaseCota.setCota(cotaBaseParaSalvar);
				cotaBaseCota.setCotaBase(cotaBase);
				cotaBaseCota.setAtivo(true);
				this.cotaBaseCotaService.salvar(cotaBaseCota);
			}			
		}else{			
			cotaBaseJaSalva.setIndiceAjuste(indiceAjuste);			
			this.cotaBaseService.atualizar(cotaBaseJaSalva);
			for(Integer cotabBase: numerosDeCotasBase){
				CotaBaseCota cotaBaseCota = new CotaBaseCota();
				Cota cotaBaseParaSalvar = this.cotaService.obterPorNumeroDaCota(cotabBase);
				cotaBaseCota.setCota(cotaBaseParaSalvar);
				cotaBaseCota.setCotaBase(cotaBaseJaSalva);
				cotaBaseCota.setAtivo(true);
				this.cotaBaseCotaService.salvar(cotaBaseCota);
			}		
			
		}
	}

	private void validarCotasDigitadas(Integer[] numerosDeCotasBase, List<CotaBaseDTO> listaCotaBase, Integer idCotaNova) {
		if(listaCotaBase.size() != 0){
			for(CotaBaseDTO dto : listaCotaBase){
				for(Integer cotaDigitada: numerosDeCotasBase){
					if(dto.getNumeroCota().equals(cotaDigitada)){
						throw new ValidacaoException(TipoMensagem.WARNING, "Cota já cadastrada.");
					}
				}
			}			
		}else{
			for(Integer cotaDigitada: numerosDeCotasBase){
				if(cotaDigitada.equals(idCotaNova)){
					throw new ValidacaoException(TipoMensagem.WARNING, "Cota já cadastrada.");
				}
			}
		}
	}
	
}
