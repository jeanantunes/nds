package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaPromissoriaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
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
			Long idCota) throws Exception {

		validaNotaPromissoria(notaPromissoria);
		cotaGarantiaService.salvaNotaPromissoria(notaPromissoria, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Nota Promissoria salva com Sucesso."), "result")
				.recursive().serialize();
	}

	@Post("/salvaChequeCaucao.json")
	public void salvaChequeCaucao(Cheque chequeCaucao, Long idCota) throws Exception  {

		validaChequeCaucao(chequeCaucao);

		cotaGarantiaService.salvaChequeCaucao(chequeCaucao, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Cheque Caução salvo com Sucesso."), "result")
				.recursive().serialize();
	}

	@Post("/salvaImovel.json")
	public void salvaImovel(List<Imovel> listaImoveis, Long idCota) throws Exception  {

		cotaGarantiaService.salvaImovel(listaImoveis, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Imóveis salvos com Sucesso."), "result").recursive()
				.serialize();
	}

	@Post("/salvaCaucaoLiquida.json")
	public void salvaCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota) throws Exception {
		
		for(CaucaoLiquida caucaoLiquida: listaCaucaoLiquida){			
			caucaoLiquida.setAtualizacao(Calendar.getInstance());
			validaCaucaoLiquida(caucaoLiquida);
		}
		
		cotaGarantiaService.salvarCaucaoLiquida(listaCaucaoLiquida, idCota);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,
				"Caução Líquida salva com Sucesso."), "result").recursive()
		.serialize();
	}
	
	@Post("/getByCota.json")
	public void getByCota(Long idCota) {
		CotaGarantiaDTO cotaGarantia =	cotaGarantiaService.getByCota(idCota);
		
		if (cotaGarantia != null && cotaGarantia.getCotaGarantia() != null) {			
			result.use(CustomJson.class).from(cotaGarantia).serialize();		
		}else{			
			result.use(CustomJson.class).from("OK").serialize();		
		}	
	}
	
	@Post("/getTipoGarantiaCadastrada.json")
	public void getTipoGarantiaCadastrada(Long idCota){
		
	}

	@Get("/impriNotaPromissoria/{id}")
	public void impriNotaPromissoria(Long id) {
		NotaPromissoriaDTO nota = cotaGarantiaService.getDadosImpressaoNotaPromissoria(id);
		
		result.include("nota",nota);
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
	 * @param caucaoLiquida para ser validado
	 */
	private void validaCaucaoLiquida(CaucaoLiquida caucaoLiquida) {
				
		if (caucaoLiquida.getValor() == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Parametros inválidos"));
		}
		
		if (caucaoLiquida.getAtualizacao() == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Parametros inválidos"));
		}
		
		if (caucaoLiquida.getIndiceReajuste() == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Parametros inválidos"));
		}	
	}
	/**
	 * @param notaPromissoria para ser validado
	 */
	private void validaNotaPromissoria(NotaPromissoria notaPromissoria) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (notaPromissoria.getValor() == null || notaPromissoria.getValor() <= 0) {
			listaMensagens.add("O preenchimento do campo [Valor R$] é obrigatório");
		}
		
		if (StringUtil.isEmpty(notaPromissoria.getValorExtenso())) {
			listaMensagens
					.add("O preenchimento do campo [Valor Extenso] é obrigatório");
		}
		
		if (notaPromissoria.getVencimento() == null) {
			listaMensagens
					.add("O preenchimento do campo [Vencimento] é obrigatório");
		}else if(notaPromissoria.getVencimento().compareTo(Calendar.getInstance()) <= 0  ) {
			listaMensagens
			.add("O campo [Vencimento] deve ser uma data no futuro.");
		}
		
		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagens));
		}
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
	public void getFiador(Long idFiador, Long idCota)throws Exception  {
		cotaGarantiaService.salvaFiador(idFiador, idCota);
		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Fiador salvo com Sucesso."), "result").recursive()
				.serialize();
	}

	
	public Download getImageCheque(long idCheque) {		
		
		byte[] buff = cotaGarantiaService.getImageCheque(idCheque);
		
		if (buff == null) {
			buff = new byte[0];
		}
		
		return new ByteArrayDownload(buff, "image/jpeg", "cheque.jpg");
	}
	
	public void uploadCheque(Long idCheque, UploadedFile image) throws Exception{		
		
		if(idCheque != null){
			byte[] bytes = IOUtils.toByteArray(image.getFile());
			cotaGarantiaService.salvaChequeImage(idCheque, bytes);
			
			result.use(PlainJSONSerialization.class)
			.from(new ValidacaoVO(TipoMensagem.SUCCESS,
					"Upload feito com Sucesso."), "result").recursive()
			.serialize();
		}else{
			result.use(PlainJSONSerialization.class)
			.from(new ValidacaoVO(TipoMensagem.ERROR,
					"Cheque deve estar salvo."), "result").recursive()
			.serialize();
		}
		
	}
	
	
}
