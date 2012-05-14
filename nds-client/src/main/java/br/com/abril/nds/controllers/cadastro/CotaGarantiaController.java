package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/garantia")
public class CotaGarantiaController {

		
	@Autowired
	private CotaGarantiaService cotaGarantiaService;
	
	private Result result;

	public CotaGarantiaController(Result result) {
		super();
		this.result = result;
	}

	@Post
	@Path("/salvaNotaPromissoria.json")
	public void salvaNotaPromissoria(NotaPromissoria notaPromissoria,
			Long idCota) {

		cotaGarantiaService.salvaNotaPromissoria(notaPromissoria, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Nota Promissoria salva com Sucesso."), "result")
				.recursive().serialize();
	}

	@Post("/salvaChequeCaucao.json")
	public void salvaChequeCaucao(Cheque chequeCaucao, Long idCota) {

		validaChequeCaucao(chequeCaucao);

		cotaGarantiaService.salvaChequeCaucao(chequeCaucao, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Cheque Caução salvo com Sucesso."), "result")
				.recursive().serialize();
	}

	@Post("/salvaImovel.json")
	public void salvaImovel(List<Imovel> listaImoveis, Long idCota) {

		cotaGarantiaService.salvaImovel(listaImoveis, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Imóveis salvos com Sucesso."), "result").recursive()
				.serialize();
	}

	@Post("/getByCota.json")
	public void getByCota(Long idCota) {
		CotaGarantia cotaGarantia =	cotaGarantiaService.getByCota(idCota);
		
		if (cotaGarantia != null) {			
			result.use(Results.json()).from(cotaGarantia, "cotaGarantia").exclude("cota").recursive().serialize();		
		}else{			
			result.use(CustomJson.class).from("OK").serialize();		
		}	
	}

	@Get("/impriNotaPromissoria/{id}")
	public void impriNotaPromissoria(Long id) {
		// TODO: chamada do relatorio para nota.
		result.nothing();
	}

	@Get("/getTiposGarantia.json")
	public void getTiposGarantia() {
		List<TipoGarantia> cotaGarantias = cotaGarantiaService
				.obtemTiposGarantiasAceitas();
		result.use(Results.json()).withoutRoot().from(cotaGarantias)
				.recursive().serialize();
	}

	@Post("/buscaFiador.json")
	public void buscaFiador(String nome, int maxResults) {
		List<ItemDTO<Long, String>> listFiador = cotaGarantiaService
				.buscaFiador(nome, maxResults);
		result.use(Results.json()).from(listFiador, "items").recursive()
				.serialize();
	}


	@Post("/incluirImovel.json")
	public void incluirImovel(Imovel imovel) {
		validaImovel(imovel);
						
		result.use(Results.json()).from(imovel, "imovel").serialize();
	}

	/**
	 * @param cheque para ser validado
	 */
	private void validaChequeCaucao(Cheque cheque) {

		List<String> listaMensagens = new ArrayList<String>();

		if (StringUtil.isEmpty(cheque.getNumeroBanco())) {
			listaMensagens
					.add("O preenchimento do campo [Num. Banco] é obrigatório");
		}

		if (StringUtil.isEmpty(cheque.getNomeBanco())) {
			listaMensagens.add("O preenchimento do campo [Nome] é obrigatório");
		}

		if (cheque.getAgencia() == null
				|| StringUtil.isEmpty(cheque.getDvAgencia())) {
			listaMensagens
					.add("O preenchimento do campo [Agência] é obrigatório");
		}

		if (cheque.getConta() == null
				|| StringUtil.isEmpty(cheque.getDvConta())) {
			listaMensagens
					.add("O preenchimento do campo [Conta] é obrigatório");
		}

		if (StringUtil.isEmpty(cheque.getNumeroCheque())) {
			listaMensagens
					.add("O preenchimento do campo [Nº Cheque] é obrigatório");
		}

		if (cheque.getValor() == null) {
			listaMensagens
					.add("O preenchimento do campo [Valor R$] é obrigatório");
		}

		if (cheque.getEmissao() == null) {
			listaMensagens
					.add("O preenchimento do campo [Data do Cheque] é obrigatório");
		}

		if (cheque.getValidade() == null) {
			listaMensagens
					.add("O preenchimento do campo [Validade] é obrigatório");
		}

		if (StringUtil.isEmpty(cheque.getCorrentista())) {
			listaMensagens
					.add("O preenchimento do campo [Nome Correntista] é obrigatório");
		}

		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagens));
		}
	}

	/**
	 * @param imóvel para ser validado.
	 */
	private void validaImovel(Imovel imovel) {

		List<String> listaMensagens = new ArrayList<String>();

		if (StringUtil.isEmpty(imovel.getProprietario())) {
			listaMensagens
					.add("O preenchimento do campo [Proprietário] é obrigatório");
		}

		if (StringUtil.isEmpty(imovel.getEndereco())) {
			listaMensagens
					.add("O preenchimento do campo [Endereço] é obrigatório");
		}

		if (StringUtil.isEmpty(imovel.getNumeroRegistro())) {
			listaMensagens
					.add("O preenchimento do campo [Número Registro] é obrigatório");
		}

		if (imovel.getValor() == null) {
			listaMensagens
					.add("O preenchimento do campo [Valor R$] é obrigatório");
		}

		if (StringUtil.isEmpty(imovel.getObservacao())) {
			listaMensagens
					.add("O preenchimento do campo [Observação] é obrigatório");
		}

		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagens));
		}
	}
	
	@Post("/getFiador.json")
	public void getFiador(Long idFiador, String documento) {
		Fiador fiador = cotaGarantiaService.getFiador(idFiador, documento);
		if (fiador != null) {
			result.use(CustomJson.class).from(fiador).serialize();
		} else {
			result.use(CustomJson.class).from("NotFound").serialize();
		}
	}

	@Post("/salvaFiador.json")
	public void getFiador(Long idFiador, Long idCota) {
		cotaGarantiaService.salvaFiador(idFiador, idCota);
		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Fiador salvo com Sucesso."), "result").recursive()
				.serialize();
	}
}
