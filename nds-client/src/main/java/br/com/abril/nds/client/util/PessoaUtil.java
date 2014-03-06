package br.com.abril.nds.client.util;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

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
	 * Se for Física retorna o nome e se for jurídica retorna a Razão Social.
	 * @param pessoa - pessoa
	 * @return Nome para exibição
	 */
	public static String obterNomeExibicaoPeloTipo(final Pessoa pessoa) {
		return obterNomeExibicaoPeloTipo(pessoa, true);
	}
	
	/**
	 * Obtém o nome de exibição de acordo com o tipo de pessoa.
	 * Se for Física retorna o apelido e se for jurídica retorna a nome fantasia.
	 * @param pessoa - pessoa
	 * @return Nome para exibição
	 */
	public static String obterApelidoExibicaoPeloTipo(Pessoa pessoa) {
		return obterApelidoExibicaoPeloTipo(pessoa,true);
	}

	/**
	 * Obtém o nome de exibição de acordo com o tipo de pessoa.
	 * Se for Física retorna o nome e se for jurídica retorna a Razão Social.
	 * @param pessoa - pessoa
	 * @param inclueSufixo- indica se deve ser incluido o sufixo ao nome <code>" (PF)"</code> para pessoa física ou <code>" (PJ)"</code> para jurídica
	 * @return Nome para exibição
	 */
	public static String obterNomeExibicaoPeloTipo(final Pessoa pessoa, final boolean inclueSufixo) {
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
	public static String obterApelidoExibicaoPeloTipo(final Pessoa pessoa, final boolean inclueSufixo) {
		
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
	public static String removerSufixoDeTipo(final String nomeExibicao) {

		if (nomeExibicao != null && !nomeExibicao.trim().isEmpty()) {

			if (nomeExibicao.contains(SUFIXO_PESSOA_FISICA)) {

				return nomeExibicao.replace(SUFIXO_PESSOA_FISICA, "");

			} else if (nomeExibicao.contains(SUFIXO_PESSOA_JURIDICA)) {

				return nomeExibicao.replace(SUFIXO_PESSOA_JURIDICA, "");
			}
		}

		return nomeExibicao;
	}

}
