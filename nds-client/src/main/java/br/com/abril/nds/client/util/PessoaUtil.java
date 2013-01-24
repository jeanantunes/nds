package br.com.abril.nds.client.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

/**
 * Classe utilitária para complexidades envolvendo a entidade Pessoa.
 * 
 * @author Discover Technology
 * 
 */
public class PessoaUtil {

	public static final String SUFIXO_PESSOA_FISICA = " (PF)";

	public static final String SUFIXO_PESSOA_JURIDICA = " (PJ)";
	
	public static final int RG_QUANTIDADE_DIGITOS = 15;

	/**
	 * Obtém o nome de exibição de acordo com o tipo de pessoa.
	 * 
	 * Se for Física retorna o nome e se for jurídica retorna a Razão Social.
	 * 
	 * @param pessoa
	 *            - pessoa
	 * 
	 * @return Nome para exibição
	 */
	public static String obterNomeExibicaoPeloTipo(Pessoa pessoa) {
		return obterNomeExibicaoPeloTipo(pessoa, true);
	}
	
	/**
	 * Obtém o nome de exibição de acordo com o tipo de pessoa.
	 * 
	 * Se for Física retorna o apelido e se for jurídica retorna a nome fantasia.
	 * 
	 * @param pessoa
	 *            - pessoa
	 * 
	 * @return Nome para exibição
	 */
	public static String obterApelidoExibicaoPeloTipo(Pessoa pessoa) {
		return obterApelidoExibicaoPeloTipo(pessoa,true);
	}

	/**
	 * Obtém o nome de exibição de acordo com o tipo de pessoa.
	 * 
	 * Se for Física retorna o nome e se for jurídica retorna a Razão Social.
	 * 
	 * @param pessoa
	 *            - pessoa
	 * @param inclueSufixo - indica se deve ser incluido o sufixo ao nome <code>" (PF)"</code> para pessoa física ou <code>" (PJ)"</code> para jurídica
	 * 
	 * @return Nome para exibição
	 */
	public static String obterNomeExibicaoPeloTipo(Pessoa pessoa,
			boolean inclueSufixo) {
		String nomeExibicao = null;
		String sufixo = null;
	
		if (pessoa != null) {
	
			if (pessoa instanceof PessoaJuridica) {
	
				nomeExibicao = ((PessoaJuridica) pessoa).getRazaoSocial();
	
				sufixo =  SUFIXO_PESSOA_JURIDICA;
	
			} else if (pessoa instanceof PessoaFisica) {
	
				nomeExibicao = ((PessoaFisica) pessoa).getNome();
	
				sufixo = SUFIXO_PESSOA_FISICA;
			}
		}
		
		if(inclueSufixo && nomeExibicao != null){
			nomeExibicao += sufixo;
		}
		
		return nomeExibicao;
	}
	
	/**
	 * Obtém o apelido de exibição de acordo com o tipo de pessoa.
	 * 
	 * Se for Física retorna o apelido e se for jurídica retorna a nome fantasia.
	 * 
	 * @param pessoa
	 *            - pessoa
	 * @param inclueSufixo - indica se deve ser incluido o sufixo ao nome <code>" (PF)"</code> para pessoa física ou <code>" (PJ)"</code> para jurídica
	 * 
	 * @return Nome para exibição
	 */
	public static String obterApelidoExibicaoPeloTipo(Pessoa pessoa,boolean inclueSufixo) {
		
		String nomeExibicao = null;
		String sufixo = null;
	
		if (pessoa != null) {
	
			if (pessoa instanceof PessoaJuridica) {
	
				nomeExibicao = ((PessoaJuridica) pessoa).getNomeFantasia();
	
				sufixo =  SUFIXO_PESSOA_JURIDICA;
	
			} else if (pessoa instanceof PessoaFisica) {
	
				nomeExibicao = ((PessoaFisica) pessoa).getApelido();
	
				sufixo = SUFIXO_PESSOA_FISICA;
			}
		}
		
		if(inclueSufixo && nomeExibicao != null){
			nomeExibicao += sufixo;
		}
		
		return nomeExibicao;
	}


	/**
	 * Remove o sufixo do tipo de pessoa.
	 * 
	 * @param nomeExibicao
	 *            - nome de exibicao
	 * 
	 * @return Nome de exibição sem o sufixo de tipo de pessoa
	 */
	public static String removerSufixoDeTipo(String nomeExibicao) {

		if (nomeExibicao != null && !nomeExibicao.trim().isEmpty()) {

			if (nomeExibicao.contains(SUFIXO_PESSOA_FISICA)) {

				return nomeExibicao.replace(SUFIXO_PESSOA_FISICA, "");

			} else if (nomeExibicao.contains(SUFIXO_PESSOA_JURIDICA)) {

				return nomeExibicao.replace(SUFIXO_PESSOA_JURIDICA, "");
			}
		}

		return nomeExibicao;
	}
	
	/**
	 * Verifica se todos os numeros são iguais
	 * @param str
	 * @return boolean
	 */
	public static boolean numerosIguais(String str){
		
		for (int i=0; i<str.length()-1;i++){
	
			if ((str.charAt(i) != str.charAt(i+1)) ) return false;
		}
		
		return true;
	}
	
	/**
	 * Valida CPF
	 * @param cpf
	 */
	public static void validarCPF(String cpf){
		
		String c = Util.removerMascaraCpf(cpf);
		
		if (c.length() != 11 || numerosIguais(c)){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [Número CPF] está inválido!");
		}
		
		CPFValidator cpfValidator = new CPFValidator(true);
		
		try{
			
			cpfValidator.assertValid(cpf);
		}catch(InvalidStateException e){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [Número CPF] está inválido!");
		}
	}
	
	/**
	 * Valida CNPJ
	 * @param cnpj
	 */
    public static void validarCNPJ(String cnpj){
    	
    	String c = Util.removerMascaraCnpj(cnpj);
		
		if (c.length() != 14 || numerosIguais(c)){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [Número CNPJ] está inválido!");
		}
		
		CNPJValidator cnpjValidator = new CNPJValidator(true);
		
		try{
			
			cnpjValidator.assertValid(cnpj);
		}catch(InvalidStateException e){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [Número CNPJ] está inválido!");
		}
	}

}
