package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO.OrdenacaoColunaBancos;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/banco")
public class BancoController {
	
	@Autowired
	private Validator validator;	
	
	@Autowired
	private BancoService bancoService;
	
    private Result result;
    
    private HttpSession httpSession;

    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaBoletos";
   

	@Post
	@Path("/consultaBancos")
	public void consultaBancos(String nome,
			                   String numero,
			                   String cedente,
			                   String status,
			                   String sortorder, 
							   String sortname, 
							   int page, 
							   int rp){
		
		//VALIDACOES
		validar(nome,
                numero,
                cedente,
                status);
		

		
		//CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaBancosDTO filtroAtual = new FiltroConsultaBancosDTO(nome,
														                  numero,
														                  cedente,
														                  status);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaBancos.values(), sortname));
	
		FiltroConsultaBancosDTO filtroSessao = (FiltroConsultaBancosDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		
		
		
		
		
		
		
		
		//BUSCA BOLETOS
		List<Banco> bancos = this.bancoService.obterBancos(filtroAtual);
		
		//CARREGA DIRETO DA ENTIDADE PARA A TABELA
		List<CellModel> listaModelo = new LinkedList<CellModel>();
		
		if (bancos.size()==0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 

		for (Banco banco : bancos){	
			listaModelo.add(new CellModel(1,
										  (banco.getId()!=null?banco.getId().toString():""),
										  (banco.getNumeroBanco()!=null?banco.getNumeroBanco():""),
										  (banco.getNome()!=null?banco.getNome():""),
										  (banco.getAgencia()!=null?banco.getAgencia().toString():""),
										  (banco.getConta()!=null?banco.getConta().toString():""),
										  (banco.getCodigoCedente()!=null?banco.getCodigoCedente().toString():""),
										  (banco.getMoeda()!=null?banco.getMoeda().name():""),
										  (banco.getCarteira()!=null?banco.getCarteira().getTipoRegistroCobranca().name():""),
										  (banco.isAtivo()?"Ativo":"Desativado"),
										  ""
                      					)
              );
		}	
		
		TableModel<CellModel> tm = new TableModel<CellModel>();

		//DEFINE TOTAL DE REGISTROS NO TABLEMODEL
		tm.setTotal( (int) this.bancoService.obterQuantidadeBancos(filtroAtual));
		
		//DEFINE CONTEUDO NO TABLEMODEL
		tm.setRows(listaModelo);
		
		//DEFINE PAGINA ATUAL NO TABLEMODEL
		tm.setPage(filtroAtual.getPaginacao().getPaginaAtual());
		
		
		//PREPARA RESULTADO PARA A VIEW (HASHMAP)
		Map<String, TableModel<CellModel>> resultado = new HashMap<String, TableModel<CellModel>>();
		resultado.put("TblModelBancos", tm);
		
		//RETORNA HASHMAP EM FORMATO JSON PARA A VIEW
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();

	}
	
	
	
	
	public void validar(String nome,
            			String numero,
            			String cedente,
            			String status){
		//VALIDACOES
		if (validator.hasErrors()) {
			List<String> mensagens = new ArrayList<String>();
			for (Message message : validator.getErrors()) {
				mensagens.add(message.getMessage());
			}
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
			throw new ValidacaoException(validacao);
		}
		/*
		if ((nome==null || !"".equals(nome))&&
		    (nome==null || !"".equals(numero))&&
		    (nome==null || !"".equals(cedente))&&
		    (nome==null || !"".equals(status))){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Digite um parâmetro de busca.");
		}
		*/
    }
	
	
	
	
}
