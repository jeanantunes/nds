/**
 * 
 */
package br.com.abril.nds.exception;

import java.util.List;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * @author Diego Fernandes
 *
 */
public class FormaCobrancaExcepion extends ValidacaoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9068457276563010922L;

	/**
	 * @param tipoMensagem
	 * @param mensagem
	 */
	public FormaCobrancaExcepion(TipoMensagem tipoMensagem, String mensagem) {
		super(tipoMensagem, mensagem);
	}

	/**
	 * @param tipoMensagem
	 * @param mensagem
	 * @param tratarValidacao
	 */
	public FormaCobrancaExcepion(TipoMensagem tipoMensagem, String mensagem,
			boolean tratarValidacao) {
		super(tipoMensagem, mensagem, tratarValidacao);
	}

	/**
	 * @param tipoMensagem
	 * @param listaMensagens
	 */
	public FormaCobrancaExcepion(TipoMensagem tipoMensagem,
			List<String> listaMensagens) {
		super(tipoMensagem, listaMensagens);
	}

	/**
	 * @param validacao
	 */
	public FormaCobrancaExcepion(ValidacaoVO validacao) {
		super(validacao);
	}

	/**
	 * @param url
	 * @param tipoMensagem
	 * @param mensagem
	 */
	public FormaCobrancaExcepion(String url, TipoMensagem tipoMensagem,
			String mensagem) {
		super(url, tipoMensagem, mensagem);
	}

	/**
	 * @param url
	 * @param validacao
	 */
	public FormaCobrancaExcepion(String url, ValidacaoVO validacao) {
		super(url, validacao);
	}

	/**
	 * 
	 */
	public FormaCobrancaExcepion() {
	}

}
