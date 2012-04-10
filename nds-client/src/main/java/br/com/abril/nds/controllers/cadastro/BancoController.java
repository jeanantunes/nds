package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.BancoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO.OrdenacaoColunaBancos;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de consulta, cadastro e alteração de Bancos. 
 * 
 * @author Discover Technology
 *
 */

@Resource
@Path("/banco")
public class BancoController {
	
	@Autowired
	private Validator validator;	
	
	@Autowired
	private BancoService bancoService;
	
    private Result result;
    
    private HttpSession httpSession;
    
    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaBancos";
    
    
    
    /**
	 * Construtor da classe
	 * @param result
	 * @param httpSession
	 * @param httpResponse
	 */
	public BancoController(Result result, HttpSession httpSession, HttpServletResponse httpResponse) {
		this.result = result;
		this.httpSession = httpSession;
	}
   
	
	
    /**
     * Método de chamada da página
     */
    @Get
    public void bancos(){ 

	}
    
    

    /**
     * Método de consulta de bancos
     * @param nome
     * @param numero
     * @param cedente
     * @param status
     * @param sortorder
     * @param sortname
     * @param page
     * @param rp
     */
	@Post
	@Path("/consultaBancos")
	public void consultaBancos(String nome,
			                   String numero,
			                   String cedente,
			                   boolean ativo,
			                   String sortorder, 
							   String sortname, 
							   int page, 
							   int rp){
		
		//CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaBancosDTO filtroAtual = new FiltroConsultaBancosDTO(nome,
														                  numero,
														                  cedente,
														                  /*ativo*/false);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaBancos.values(), sortname));
	
		FiltroConsultaBancosDTO filtroSessao = (FiltroConsultaBancosDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);

		//BUSCA BANCOS
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
										  (banco.isAtivo()==true?"Ativo":"Desativado"),
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
	
	
	
	/**
	 * Método responsável pela inclusão de novo Banco
	 * @param numero
	 * @param nome
	 * @param codigoCedente
	 * @param agencia
	 * @param conta
	 * @param digito
	 * @param moeda
	 * @param carteira
	 * @param juros
	 * @param ativo
	 * @param multa
	 * @param instrucoes
	 */
	@Post
	@Path("/novoBanco")
	public void novoBanco(String numero,
						  String nome,
						  String codigoCedente,
						  Long agencia,
						  Long conta,
						  String digito,
						  String moeda,
						  String carteira,
						  String juros,
						  boolean ativo,
						  String multa,
						  String instrucoes){

		validarCadastroBanco(0,
				             numero,
						     nome,
						     codigoCedente,
						     agencia,
						     conta,
						     digito,
						     moeda,
						     carteira,
						     juros,
						     ativo,
						  	 multa,
						  	 instrucoes);
		
	    Banco banco = new Banco();	
        banco.setNumeroBanco(numero);
        banco.setNome(nome);
        banco.setCodigoCedente(codigoCedente);
        banco.setAgencia(agencia);
        banco.setConta(conta);
        banco.setDvConta(digito);
        banco.setMoeda( Moeda.valueOf(moeda));
        //banco.setCarteira( carteira );
        //banco.setJuros(juros);
        banco.setAtivo(ativo);
        //banco.setMulta(multa);
        banco.setInstrucoes(instrucoes);
	
        this.bancoService.incluirBanco(banco);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Banco "+nome+" cadastrado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	
	
	/**
	 * Método responsável por buscar os dados do banco para alteração.
	 * @param idBanco
	 */
	@Post
	@Path("/buscaBanco")
	public void buscaBanco(long idBanco){
		BancoVO bancoVO = this.bancoService.obterDadosBanco(idBanco);
		if (bancoVO==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Banco "+idBanco+" não encontrado.");
		}
		result.use(Results.json()).from(bancoVO,"result").recursive().serialize();
	}
	
	
	
	/**
	 * Método responsável pela alteração de um Banco
	 * @param idBanco
	 * @param numero
	 * @param nome
	 * @param codigoCedente
	 * @param agencia
	 * @param conta
	 * @param digito
	 * @param moeda
	 * @param carteira
	 * @param juros
	 * @param ativo
	 * @param multa
	 * @param instrucoes
	 */
	@Post
	@Path("/alteraBanco")
	public void alteraBanco(long idBanco,
			                String numero,
						    String nome,
						    String codigoCedente,
						    Long agencia,
						    Long conta,
						    String digito,
						    String moeda,
						    String carteira,
						    String juros,
						    boolean ativo,
						    String multa,
						  	String instrucoes){

		validarCadastroBanco(idBanco,
				             numero,
						     nome,
						     codigoCedente,
						     agencia,
						     conta,
						     digito,
						     moeda,
						     carteira,
						     juros,
						     ativo,
						  	 multa,
						  	 instrucoes);
		
		Banco banco = this.bancoService.obterBancoPorId(idBanco);
		banco.setNumeroBanco(numero);
		banco.setNome(nome);
		banco.setCodigoCedente(codigoCedente);
		banco.setAgencia(agencia);
		banco.setConta(conta);
		banco.setDvConta(digito);
		banco.setMoeda( Moeda.valueOf(moeda));
		//banco.setCarteira( carteira );
		//banco.setJuros(juros);
		banco.setAtivo(ativo);
		//banco.setMulta(multa);
		banco.setInstrucoes(instrucoes);

		this.bancoService.alterarBanco(banco);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Banco "+nome+" alterado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
    }
	
	
	
	/**
	 * Método responsável por validar os dados de um novo banco ou de uma alteração de banco.
	 * @param numero
	 * @param nome
	 * @param codigoCedente
	 * @param agencia
	 * @param conta
	 * @param digito
	 * @param moeda
	 * @param carteira
	 * @param juros
	 * @param ativo
	 * @param multa
	 * @param instrucoes
	 * @throws Mensagens de validações de campos
	 */
	private void validarCadastroBanco(long idBanco,
			                          String numero,
									  String nome,
									  String codigoCedente,
									  Long agencia,
									  Long conta,
									  String digito,
									  String moeda,
									  String carteira,
									  String juros,
									  boolean ativo,
									  String multa,
									  String instrucoes){
		
		//VALIDACOES
		if (validator.hasErrors()) {
			List<String> mensagens = new ArrayList<String>();
			for (Message message : validator.getErrors()) {
				mensagens.add(message.getMessage());
			}
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
			throw new ValidacaoException(validacao);
		}
		
		if (idBanco==0){
			Banco banco = this.bancoService.obterbancoPorNumero(numero);
			if(banco!=null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Banco "+numero+" já cadastrado.");
			}
			banco = this.bancoService.obterbancoPorNome(nome);
			if(banco!=null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Banco "+nome+" já cadastrado.");
			}
		}
		
		if ((numero==null)||("".equals(numero))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o numero do banco.");
		}
		
		if ((nome==null)||("".equals(nome))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o nome do banco.");
		}
		
		if ((codigoCedente==null)||("".equals(codigoCedente))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o código do cedente.");
		}
		
		if ((agencia==null)||("".equals(agencia))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo agência.");
		}
		
		if ((conta==null)||("".equals(conta))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo conta.");
		}
		
		if ((digito==null)||("".equals(digito))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo dígito da conta do banco.");
		}
		
		if ((moeda==null)||("".equals(moeda))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo moeda.");
		}
		
		if ((carteira==null)||("".equals(carteira))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo carteira.");
		}
		
		if ((juros==null)||("".equals(juros))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo juros.");
		}
		
		if ("".equals(ativo)){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo status.");
		}
		
		if ((multa==null)||("".equals(multa))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo multa.");
		}
		if ((instrucoes==null)||("".equals(instrucoes))){
			throw new ValidacaoException(TipoMensagem.WARNING, "Digite as intruções.");
		}
		
	}
	
	/**
	 * Método responsável por desativar um banco.
	 * @param idBanco
	 */
	@Post
	@Path("/desativaBanco")
	public void desativaBanco(long idBanco){
		
		if (!this.bancoService.verificarPendencias(idBanco)){
		    this.bancoService.dasativarBanco(idBanco);
  		    result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Banco desativado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
		}
		else{
			throw new ValidacaoException(TipoMensagem.WARNING, "O banco possui pendências e não pode ser desativado.");
		}
    }
	
}
