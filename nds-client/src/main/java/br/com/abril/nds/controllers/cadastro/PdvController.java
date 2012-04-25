package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.PdvVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CaracteristicaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
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
@Path("/cadastro/pdv")
public class PdvController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private PdvService pdvService;

	@Path("/")
	public void index(){
	
	}
	
	/**
	 * Carrega dados básicos para abrir tela de PDV
	 */
	public void preCarregamento() {
				
		result.include("listaStatus",gerarItemStatus(StatusPDV.values()));
		result.include("listaTamanhoPDV",gerarTamanhosPDV(TamanhoPDV.values()));
		result.include("listaTipoEstabelecimento",gerarTiposEstabelecimento());
		result.include("listaTipoLicencaMunicipal",gerarTiposLicencaMunicipal());
				
	}
	
	private List<ItemDTO<Long, String>> gerarTiposEstabelecimento() {
		List<ItemDTO<Long, String>> itens = new ArrayList<ItemDTO<Long,String>>();
		
		//TODO obter lista do banco de dados
		List<TipoEstabelecimentoAssociacaoPDV> licencas = getFakeTiposEstabelecimento();
		
		for(TipoEstabelecimentoAssociacaoPDV licenca: licencas) {
			itens.add(new ItemDTO<Long, String>(licenca.getCodigo(), licenca.getDescricao()));
		}
		
		return itens;
	}

	private List<TipoEstabelecimentoAssociacaoPDV> getFakeTiposEstabelecimento() {
		
		List<TipoEstabelecimentoAssociacaoPDV> estabelecimentos = new ArrayList<TipoEstabelecimentoAssociacaoPDV>();
		 
		 estabelecimentos.add(new TipoEstabelecimentoAssociacaoPDV());
		 estabelecimentos.get(0).setCodigo(1L);
		 estabelecimentos.get(0).setDescricao("Licenca 1");
		 
		 estabelecimentos.add(new TipoEstabelecimentoAssociacaoPDV());
		 estabelecimentos.get(1).setCodigo(1L);
		 estabelecimentos.get(1).setDescricao("Licenca 2");
		 
		return estabelecimentos;
	}

	private List<ItemDTO<String, String>> gerarItemStatus(StatusPDV[] statusPDVs) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for(StatusPDV item: statusPDVs) {
			itens.add(new ItemDTO<String, String>(item.name(), item.getDescricao()));
		}
		
		return itens;
	}
	
	private List<ItemDTO<String, String>> gerarTamanhosPDV(TamanhoPDV[] tamanhoPDVs) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for(TamanhoPDV item: tamanhoPDVs) {
			itens.add(new ItemDTO<String, String>(item.name(), item.getDescricao()));
		}
		
		return itens;
	}
	
	/**
	 * Gera dados do combo de Dias de Funcionamento (Aba - Dados Básicos)
	 * @return
	 */
	private List<ItemDTO<String, String>> gerarTiposLicencaMunicipal() {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		//TODO obter lista do banco de dados
		List<LicencaMunicipal> licencas = getFakelicencas();
		
		for(LicencaMunicipal licenca: licencas) {
			itens.add(new ItemDTO<String, String>(licenca.getNumeroLicenca(), licenca.getNomeLicenca()));
		}
		
		return itens;
	}
	

	//TODO remover após implementação real
	private List<LicencaMunicipal> getFakelicencas() {
		
		 List<LicencaMunicipal> licencas = new ArrayList<LicencaMunicipal>();
		 
		 licencas.add(new LicencaMunicipal());
		 licencas.get(0).setNumeroLicenca("1");
		 licencas.get(0).setNomeLicenca("Licenca 1");
		 
		 licencas.add(new LicencaMunicipal());
		 licencas.get(1).setNumeroLicenca("2");
		 licencas.get(1).setNomeLicenca("Licenca 2");
		 
		return licencas;
	}


	@Post
	@Path("/consultar")
	public void consultarPDVs(Long idCota,String sortname, String sortorder){
		
		List<PdvVO>  listaPdvs = getMock();
		
		ordenarListaPdvs(sortname, sortorder, listaPdvs);
		
		TableModel<CellModelKeyValue<PdvVO>> tableModel = new TableModel<CellModelKeyValue<PdvVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPdvs));
		
		tableModel.setTotal(listaPdvs.size());
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private void ordenarListaPdvs(String sortname, String sortorder, List<PdvVO> listaPdvs){
		
		if (sortname != null && !listaPdvs.isEmpty()) {

			sortorder = sortorder == null ? "asc" : sortorder;

			Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);

			PaginacaoUtil.ordenarEmMemoria(listaPdvs, ordenacao, sortname);
		}
	}
	
	@Post
	@Path("/excluir")
	public void excluirPDV(Long idPdv, Long idCota){
		
		if(!pdvService.isExcluirPdv(idPdv)){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pdv não pode ser excluido!");
		}
		
		//TODO Chamar componente para exclusão
	
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "PDV excluido com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	public List<PdvVO> getMock(){
		
		List<PdvVO> listaPdvs = new ArrayList<PdvVO>();
		
		for(int x=0; x<6 ; x++){
			listaPdvs.add(new PdvVO(x,Long.valueOf(x),Long.valueOf(x),"Nome PDV" + x,"Tipo PDV","CONTATo",
									"3689-25444","Rua XPTO","Bairro","Cidade",true,"Status","10"));
		}
		
		return listaPdvs;
		
		
	}

		
	@Post
	@Path("/editar")
	public void editarPDV(Long idPdv, Long idCota){
		
		PdvDTO dto = new PdvDTO();
		dto.setContato("asasaa");
		dto.setDentroOutroEstabelecimento(Boolean.TRUE);
		
		CaracteristicaDTO caracteristicaDTO = new CaracteristicaDTO();
		caracteristicaDTO.setLuminoso(Boolean.TRUE);
		
		dto.setCaracteristicaDTO(caracteristicaDTO);
		
		result.use(Results.json()).from(dto).recursive().serialize();
	}
	
	@Post
	@Path("/salvar")
	public void salvarPDV(PdvDTO pdvDTO){		
		//TODO implementar a logica de validação e salvar os dados na sessão do usuario
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
		
}
