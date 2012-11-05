package br.com.abril.nds.controllers.cadastro;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.BancoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO.OrdenacaoColunaBancos;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
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
	private BancoService bancoService;
	
    private Result result;
    
    private HttpSession httpSession;
    
    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaBancos";
    
    private static final String APELIDO_ANTIGO_SESSION_ATTRIBUTE = "apelidoAntigo";
    
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
    @Rules(Permissao.ROLE_CADASTRO_BANCO)
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
     * @throws Mensagem de nenhum registro encontrado
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
		FiltroConsultaBancosDTO filtroAtual = new FiltroConsultaBancosDTO(nome != null ? nome.trim() : null,
														                  numero != null ? numero.trim() : null,
														                  cedente != null ? cedente.trim() : null,
														                  ativo);
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
										  (banco.getConta()!=null?banco.getConta().toString()+"-"+banco.getDvConta():""),
										  (banco.getCodigoCedente()!=null?banco.getCodigoCedente().toString():""),
										  (banco.getApelido()!=null?banco.getApelido().toString():""),
										  (banco.getCarteira()!=null?banco.getCarteira():""),
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
	
	
	
	/**
	 * Método responsável pela inclusão de novo Banco
	 * @param numero
	 * @param nome
	 * @param codigoCedente
	 * @param agencia
	 * @param conta
	 * @param digito
	 * @param apelido
	 * @param carteira
	 * @param juros
	 * @param ativo
	 * @param multa
	 * @param vrMulta
	 * @param instrucoes
	 */
	@Post
	@Path("/novoBanco")
	public void novoBanco(String numero,
						  String nome,
						  String codigoCedente,
						  String agencia,
						  String conta,
						  String digito,
						  String apelido,
						  Integer carteira,
						  BigDecimal juros,
						  boolean ativo,
						  BigDecimal multa,
						  BigDecimal vrMulta,
						  String instrucoes){
		
		if (bancoService.obterBancoPorApelido(apelido) != null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um banco com este apelido.");
		}
		
		validarCadastroBanco(
				true, 
				numero,
				nome,
				codigoCedente,
				agencia,
				conta,
				digito,
				apelido,
				carteira,
				juros,
				multa,
				vrMulta);
		
		long lAgencia = Long.parseLong(agencia);
		long lConta = Long.parseLong(conta);
		
	    Banco banco = new Banco();	
        banco.setNumeroBanco(numero);
        banco.setNome(nome);
        banco.setCodigoCedente(codigoCedente);
        banco.setAgencia(lAgencia);
        banco.setConta(lConta);
        banco.setDvConta(digito);
        banco.setApelido(apelido);
        banco.setCarteira(carteira);
        banco.setJuros(juros);
        banco.setAtivo(ativo);
        banco.setMulta(multa);
        banco.setVrMulta(vrMulta);
        banco.setInstrucoes(instrucoes);
	
		try {
	        this.bancoService.incluirBanco(banco);
		} catch(DataIntegrityViolationException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Já existe outro registro com este Número de Banco, Agência, Dígito da Agência, Conta e Dígito da Conta.");
		}
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Banco "+nome+" cadastrado com sucesso."),"result").recursive().serialize();
	}
	
	
	
	/**
	 * Método responsável por buscar os dados do banco para alteração.
	 * @param idBanco
	 * @throws Mensagem de banco não encontrado. 
	 */
	@Post
	@Path("/buscaBanco")
	public void buscaBanco(long idBanco){
		BancoVO bancoVO = this.bancoService.obterDadosBanco(idBanco);
		if (bancoVO==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Banco "+idBanco+" não encontrado.");
		}
		this.httpSession.setAttribute(APELIDO_ANTIGO_SESSION_ATTRIBUTE, bancoVO.getApelido());
		result.use(Results.json()).from(bancoVO,"result").recursive().serialize();
	}
	
	
	
	/**
	 * Método responsável pela alteração de um Banco
	 * 
	 * @param idBanco
	 * @param numero
	 * @param nome
	 * @param codigoCedente
	 * @param agencia
	 * @param conta
	 * @param digito
	 * @param apelido
	 * @param carteira
	 * @param juros
	 * @param ativo
	 * @param multa
	 * @param vrMulta
	 * @param instrucoes
	 * @throws Mensagem de pendencias financeiras do banco
	 */
	@Post
	@Path("/alteraBanco")
	public void alteraBanco(long idBanco,
						  	String numero,
						  	String nome,
						  	String codigoCedente,
						  	String agencia,
						  	String conta,
						  	String digito,
						  	String apelido,
						  	Integer carteira,
						  	BigDecimal juros,
						  	boolean ativo,
						  	BigDecimal multa,
						  	BigDecimal vrMulta,
						  	String instrucoes){
		
		if (!this.httpSession.getAttribute(APELIDO_ANTIGO_SESSION_ATTRIBUTE).equals(apelido) && bancoService.obterBancoPorApelido(apelido) != null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Já existe um banco com este apelido.");
		}
		
		validarCadastroBanco(
				false, 
				numero, 
				nome, 
				codigoCedente, 
				agencia, 
				conta, 
				digito, 
				apelido,
				carteira,
				juros, 
				multa, 
				vrMulta);
		
		if (ativo){
			if (this.bancoService.verificarPendencias(idBanco)){
				throw new ValidacaoException(TipoMensagem.WARNING, "O banco "+nome+" possui pendências e não pode ser desativado.");
			}
		}
        		
		long lAgencia = Long.parseLong(agencia);
		long lConta = Long.parseLong(conta);
		
		Banco banco = this.bancoService.obterBancoPorId(idBanco);
		banco.setNumeroBanco(numero);
		banco.setNome(nome);
		banco.setCodigoCedente(codigoCedente);
		banco.setAgencia(lAgencia);
		banco.setConta(lConta);
		banco.setDvConta(digito);
		banco.setApelido(apelido);
		banco.setCarteira(carteira);
		banco.setJuros(juros);
		banco.setAtivo(ativo);
		banco.setMulta(multa);
		banco.setVrMulta(vrMulta);
		banco.setInstrucoes(instrucoes);

		try {
			this.bancoService.alterarBanco(banco);
		} catch(DataIntegrityViolationException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Já existe outro registro com este Número de Banco, Agência, Dígito da Agência, Conta e Dígito da Conta.");
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Banco "+nome+" alterado com sucesso."),"result").recursive().serialize();
    }
	
	
	
	/**
	 * Método responsável por validar os dados de um novo banco ou de uma alteração de banco.
	 * 
	 * @param indNovoRegistro
	 * @param numero
	 * @param nome
	 * @param codigoCedente
	 * @param agencia
	 * @param conta
	 * @param digito
	 * @param apelido
	 * @param carteira
	 * @param juros
	 * @param multa
	 * @param vrMulta
	 */
	private void validarCadastroBanco(boolean indNovoRegistro,
								  	  String numero,
								  	  String nome,
								  	  String codigoCedente,
								  	  String agencia,
								  	  String conta,
								  	  String digito,
								  	  String apelido,
								  	  Integer carteira,
								  	  BigDecimal juros,
								  	  BigDecimal multa,
								  	  BigDecimal vrMulta){
		
		
		List<String> errorMsgs = new LinkedList<String>();
		
		/*if (indNovoRegistro){
			
			Banco banco = this.bancoService.obterbancoPorNumero(numero);
			
			if(banco!=null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Banco "+numero+" já cadastrado.");
			}

			banco = this.bancoService.obterbancoPorNome(nome);
			
			if(banco!=null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Banco "+nome+" já cadastrado.");
			}
			
		}*/
		
		if ((numero==null)||("".equals(numero))){
			errorMsgs.add("Preencha o número do banco.");
		}
		
		if ((nome==null)||("".equals(nome))){
			errorMsgs.add("Preencha o nome do banco.");
		}
		
		if ((codigoCedente==null)||("".equals(codigoCedente))){
			errorMsgs.add("Preencha o código do cedente.");
		}
		
		if ((agencia==null)||("".equals(agencia))){
			errorMsgs.add("Preencha o campo agência.");
		}
		
		if ((conta==null)||("".equals(conta))){
			errorMsgs.add("Preencha o campo conta.");
		}
		
		if ((digito==null)||("".equals(digito))){
			errorMsgs.add("Preencha o campo dígito da conta do banco.");
		}
		
		if ((apelido==null)||("".equals(apelido))){
			errorMsgs.add("Preencha o campo apelido.");
		}
		
		if(carteira == null) {
			errorMsgs.add("Valor inválido para o campo cateira.");
		}
		
		if(juros==null){
			errorMsgs.add("Especifique a taxa de juros.");
		}
		
        if((multa==null)&&(vrMulta==null)){
        	errorMsgs.add("Especifique a taxa ou o valor da multa.");
		}
		
        if(errorMsgs != null && !errorMsgs.isEmpty()) {
        	throw new ValidacaoException(TipoMensagem.WARNING, errorMsgs);
        }
        
	}
	
	
	
	/**
	 * Método responsável por desativar um banco.
	 * @param idBanco
	 * @throws Mansagens de validação segundo as regras de desativação de banco
	 */
	@Post
	@Path("/desativaBanco")
	public void desativaBanco(long idBanco){
		
		Banco banco = this.bancoService.obterBancoPorId(idBanco);
		String nomebanco = banco.getNome();
		
		if (!banco.isAtivo()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Banco "+nomebanco+" já desativado.");
		}
		
		if (this.bancoService.verificarPendencias(idBanco)){
			throw new ValidacaoException(TipoMensagem.WARNING, "O banco "+nomebanco+" possui pendências e não pode ser desativado.");
		}

		this.bancoService.dasativarBanco(idBanco);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Banco "+nomebanco+" desativado com sucesso."),"result").recursive().serialize();
    }
	
}
