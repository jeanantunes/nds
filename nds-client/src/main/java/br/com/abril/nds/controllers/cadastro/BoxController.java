package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/box")
public class BoxController {

	@Autowired
	private BoxService boxService;

	private Result result;

	public BoxController(Result result) {
		super();
		this.result = result;
	}

	@Path("/")
	public void index() {
	}

	@Path("/busca.json")
	@Post
	public void busca(String codigoBox, TipoBox tipoBox, boolean postoAvancado,
			String sortname, String sortorder, int rp, int page) {
		List<Box> boxs = boxService.busca(codigoBox, tipoBox, postoAvancado,
				sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
		Long quantidade = boxService.quantidade(codigoBox, tipoBox,
				postoAvancado);
		result.use(FlexiGridJson.class).from(boxs).total(quantidade.intValue()).page(page).serialize();

	}
	@Post
	@Path("/buscaPorId.json")
	public void buscaPorId(long id) {
		Box box = boxService.buscarPorId(id);
		result.use(Results.json()).from(box).serialize();
	}

	@Post
	@Path("/salvar.json")
	public void salvar(Box box) {
		valida(box);
		try {
			boxService.merge(box);
		} catch (UniqueConstraintViolationException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
		}
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Box salvo com Sucesso."),"result").recursive().serialize();
	}

	private void valida(Box box) {
		List<String> listaMensagens = new ArrayList<String>();

		if (StringUtil.isEmpty(box.getCodigo())) {
			listaMensagens.add("O preenchimento do campo [Código] é obrigatório.");
		}
		if (StringUtil.isEmpty(box.getNome())) {
			listaMensagens.add("O preenchimento do campo [Nome] é obrigatório.");
		}
		if (box.getTipoBox() == null) {
			listaMensagens.add("O preenchimento do campo [Tipo Box] é obrigatório.");
		}else if(box.isPostoAvancado() && TipoBox.LANCAMENTO != box.getTipoBox()){
			listaMensagens.add("Apenas o Tipo Box lançamento pode ser posto avançado.");
		}
		
		if (!listaMensagens.isEmpty()) {			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
		}
	}
	
	@Post
	@Path("/remove.json")
	public void remove(long id){
		try {
			boxService.remover(id);
		} catch (RelationshipRestrictionException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
		}
		result.use(Results.nothing());
	}
	
	@Post
	@Path("/detalhe.json")
	public void detalhe(long id){		
		List<CotaRotaRoteiroDTO> rotaRoteiroDTOs = boxService.obtemCotaRotaRoteiro(id);		
		result.use(FlexiGridJson.class).from(rotaRoteiroDTOs).total(rotaRoteiroDTOs.size()).page(1).serialize();
	}
}
