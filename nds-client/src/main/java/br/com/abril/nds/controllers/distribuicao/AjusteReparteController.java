package br.com.abril.nds.controllers.distribuicao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.distribuicao.AjusteReparte;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.AjusteReparteService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/ajusteReparte")
public class AjusteReparteController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private AjusteReparteService ajusteService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public AjusteReparteController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE)
	public void index(){
		this.carregarComboMotivoStatusCota();
	}
	
	@Post
	@Path("/novoAjuste")
	public void salvarAjuste (AjusteReparteDTO ajusteDTO){
		
		Date dataEHora = new Date();
		Timestamp data = new Timestamp(dataEHora.getTime());

		AjusteReparte ajuste = new AjusteReparte();
		
		ajuste.setCota(this.cotaService.obterPorNumeroDaCota(ajusteDTO.getNumeroCota()));
		ajuste.setAjusteAplicado(ajusteDTO.getAjusteAplicado());
		ajuste.setDataAlteracao(data);
		ajuste.setDataInicio(ajusteDTO.getDataInicioCadastro());
		ajuste.setDataFim(ajusteDTO.getDataFimCadastro());
		ajuste.setFormaAjuste(ajusteDTO.getFormaAjuste());
		ajuste.setMotivo(ajusteDTO.getMotivoAjuste());
		ajuste.setUsuario(this.usuarioService.getUsuarioLogado());
		
		ajusteService.salvarAjuste(ajuste);
		
//		this.validarEntradaRegiao(nome);
		
		result.use(Results.json()).from(ajuste, "ajuste").recursive().serialize();
	}
	
	@Post
	@Path("/buscarCotaAjuste")
	public void buscarCotaAjuste (String sortorder, String sortname, int page, int rp){
		
		AjusteReparteDTO dto = new AjusteReparteDTO();
		dto.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		TableModel<CellModelKeyValue<AjusteReparteDTO>> tableModel = montarCotasEmAjuste(dto);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<AjusteReparteDTO>> montarCotasEmAjuste (AjusteReparteDTO dto) {
		
		List<AjusteReparteDTO> cotasEmAjuste = ajusteService.buscarCotasEmAjuste(dto);
		
		if (cotasEmAjuste == null || cotasEmAjuste.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<AjusteReparteDTO>> tableModel = new TableModel<CellModelKeyValue<AjusteReparteDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cotasEmAjuste));

		tableModel.setPage(dto.getPaginacao().getPaginaAtual());

		tableModel.setTotal(dto.getPaginacao().getQtdResultadosTotal());

		return tableModel;
	}
	
	
	
	private void validarEntradaRegiao(AjusteReparte ajuste) {
//		if (nomeRegiao == null || (nomeRegiao.isEmpty())) {
//			throw new ValidacaoException(TipoMensagem.WARNING,
//					"Nome da regiao � obrigat�rio.");
//		}

		
		
////		List<RegiaoDTO> listaRegiaoDTO = regiaoService.buscarRegiao();
//
//		for (RegiaoDTO regiaoDTO : listaRegiaoDTO) {
//			if (regiaoDTO.getNomeRegiao().equalsIgnoreCase(nomeRegiao)) {
//				throw new ValidacaoException(TipoMensagem.WARNING, "Regi�o j� cadastrada.");
//			}
//		}
	}
	
	/*
	 * Carrega o combo de status da cota. 
	 */
	private void carregarComboMotivoStatusCota() {
		
		List<ItemDTO<MotivoAlteracaoSituacao, String>> listaMotivosStatusCota =
			new ArrayList<ItemDTO<MotivoAlteracaoSituacao, String>>();
		
		for (MotivoAlteracaoSituacao motivoAlteracaoSituacao : MotivoAlteracaoSituacao.values()) {
			
			listaMotivosStatusCota.add(new ItemDTO<MotivoAlteracaoSituacao, String>(
					motivoAlteracaoSituacao, motivoAlteracaoSituacao.toString())
			);
		}
		result.include("listaMotivosStatusCota", listaMotivosStatusCota);
	}
}
