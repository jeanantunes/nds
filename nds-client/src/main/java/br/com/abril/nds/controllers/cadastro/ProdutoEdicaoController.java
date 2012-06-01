package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.metamodel.PluralAttribute.CollectionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.BaseComboVO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/edicao")
public class ProdutoEdicaoController {

	@Autowired
	private Result result;
	
	@Autowired
	private ProdutoEdicaoService peService;
	
	/** Traz a página inicial. */
	@Get
	@Path("/")
	public void index() { }
	
	@Post
	@Path("/pesquisarEdicoes.json")
	public void pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Date dataLancamento, String situacaoLancamento,
			String codigoDeBarras, boolean brinde,
            String sortorder, String sortname, int page, int rp) {

		/* TODO: Remover ao finalizar a implementação:
		
		// Validar:
		if ((codigoProduto == null || codigoProduto.trim().isEmpty()) 
				|| (nomeProduto == null || nomeProduto.trim().isEmpty())) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, preencha o campo 'Código' ou 'Produto'!");
		}
		*/
		
		// Popular o DTO:
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		dto.setCodigoProduto(codigoProduto);
		dto.setNomeProduto(nomeProduto);
		dto.setDataLancamento(dataLancamento);
		dto.setCodigoDeBarras(codigoDeBarras);
		dto.setPossuiBrinde(brinde);
		dto.setSituacaoLancamento(null);
		for (StatusLancamento status : StatusLancamento.values()) {
			if (status.getDescricao().equals(situacaoLancamento)) {
				dto.setSituacaoLancamento(status);
			}
		}
		
		// Pesquisar:
		Long qtd = peService.countPesquisarEdicoes(dto);
		List<ProdutoEdicaoDTO> lst = peService.pesquisarEdicoes(dto, sortorder, sortname, page, rp);
		
		this.result.use(FlexiGridJson.class).from(lst).total(qtd.intValue()).page(page).serialize();
	}
	
	@Post
	public void carregarDadosProdutoEdicao(String codigoProduto) {
		
		if (codigoProduto == null || codigoProduto.trim().isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Por favor, Por favor, escolha um produto para adicionar a Edição!");
		}
		
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		dto.setCodigoProduto(codigoProduto);
		
		// Lista - últimas edições:
		List<ProdutoEdicaoDTO> lstUltimasEdicoes = peService.pesquisarUltimasEdicoes(dto, 5);
		dto.setUltimasEdicoes(lstUltimasEdicoes);
		/*
		
		// Lista - Tipos de lançamento:
		List<TipoLancamento> lstTpLancamento = Arrays.asList(TipoLancamento.values());
		Collections.sort(lstTpLancamento, new Comparator<TipoLancamento>() {
			@Override
			public int compare(TipoLancamento o1, TipoLancamento o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		dto.setTiposLancamento(lstTpLancamento);
		
		
		
		// TODO: Lista - Categorias:
		
		// TODO: Lista - Tipos de Desconto
		
		// TODO: Lista - Regime de Recolhimento
		
		
		// TODO: As listas abaixos são da aba 'segmentação' (não precisa implementar agora). 
		// TODO: Lista - Classe Social
		// TODO: Lista - Sexo
		// TODO: Lista - Faixa-Etária
		// TODO: Lista - Tema Principal
		// TODO: Lista - Tema Secundário
		
		
		List<Object> lst = new ArrayList<Object>();
		lst.add(lstUltimasEdicoes);	// Index 0: Últimas 5 Edições;
		lst.add(lstTpLancamento);	// Index 1: Tipos de Lançamento;
		*/
		this.result.use(Results.json()).from(dto, "result").serialize();
//		this.result.use(FlexiGridJson.class).from(lstUltimasEdicoes).total(lstUltimasEdicoes.size()).page(1).serialize();
	}
	
	
//	private List<BaseComboVO> parseComboTipoLancamento() {
//		
//		List<BaseComboVO> lst = new ArrayList<BaseComboVO>();
//		for (TipoLancamento tl : TipoLancamento.values()) {
//			
//			lst.add(new BaseComboVO(tl.getDescricao(), tl.getDescricao()))
//		}
//	}
	
}
