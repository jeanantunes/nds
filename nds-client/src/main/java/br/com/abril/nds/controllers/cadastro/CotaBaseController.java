package br.com.abril.nds.controllers.cadastro;

import java.math.BigDecimal;
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
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaBaseCotaService;
import br.com.abril.nds.service.CotaBaseService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
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
		
		Long existeCotaBaseCota = cotaBaseCotaService.verificarExistenciaCotaBaseCota(this.cotaService.obterPorNumeroDaCota(numeroCota));
		FiltroCotaBaseDTO filtro = null;
		if(existeCotaBaseCota != null){
			filtro = this.cotaBaseService.obterDadosFiltro(numeroCota, false);			
		}
		
		this.result.use(Results.json()).from(filtro, "result").recursive().serialize();		
	}

	private void tratarFiltroPesquisaCota(Integer numeroCota) {
		if(numeroCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota inválido!");
		}
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
			
		}
	}
	
	@Post
	@Path("/pesquisarCotasBase")
	public void pesquisarCotasBase(Integer numeroCota){
		
		List<CotaBaseDTO> listaCotaBase = this.cotaBaseService.obterCotasBases(this.cotaService.obterPorNumeroDaCota(numeroCota));
		
		int qtdeInicialPadrao = 3;
		
		for (int indice = listaCotaBase.size(); indice < 3 ; indice++) {
			
			CotaBaseDTO produtoVO = new CotaBaseDTO();

			listaCotaBase.add(produtoVO);
		}
		
		TableModel<CellModelKeyValue<CotaBaseDTO>> tableModel =
				new TableModel<CellModelKeyValue<CotaBaseDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaBase));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("/obterCota")
	public void obterCota(Integer numeroCota){
		
		FiltroCotaBaseDTO filtro = this.cotaBaseService.obterDadosFiltro(numeroCota, true);		
		
		this.result.use(Results.json()).from(filtro, "result").recursive().serialize();
		
	}
	@Post
	@Path("/excluirCotaBase")
	public void excluirCotaBase(Integer numeroCotaNova, Long idCotaBase){		
		System.out.println(numeroCotaNova + idCotaBase);		
	}
	
	@Post
	@Path("/confirmarCotasBase")
	public void confirmarCotasBase(Integer[] numerosDeCotasBase, Integer idCotaNova, BigDecimal indiceAjuste){
		List<CotaBaseDTO> listaCotaBase = this.cotaBaseService.obterCotasBases(this.cotaService.obterPorNumeroDaCota(idCotaNova));
		
		validarCotasDigitadas(numerosDeCotasBase, listaCotaBase, idCotaNova);
		
		salvar(numerosDeCotasBase, idCotaNova, indiceAjuste);
		
	}

	private void salvar(Integer[] numerosDeCotasBase, Integer idCotaNova, BigDecimal indiceAjuste) {
		CotaBase cotaBase = new CotaBase();
		cotaBase.setDataInicio(new Date());
		cotaBase.setDataFim(DateUtil.adicionarDias(new Date(), 180));
		cotaBase.setIndiceAjuste(indiceAjuste);
		this.cotaBaseService.salvar(cotaBase);
		for(Integer cotabBase: numerosDeCotasBase){
			CotaBaseCota cotaBaseCota = new CotaBaseCota();
			Cota cotaBaseParaSalvar = this.cotaService.obterPorNumeroDaCota(cotabBase);
			cotaBaseCota.setCota(cotaBaseParaSalvar);
			cotaBaseCota.setCotaBase(cotaBase);
			cotaBaseCota.setAtivo(true);
			this.cotaBaseCotaService.salvar(cotaBaseCota);
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
