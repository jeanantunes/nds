package br.com.abril.nds.exception;

import br.com.abril.nds.vo.ValidacaoVO;

public class GerarCobrancaValidacaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5039379246787865347L;
	
	private ValidacaoVO validacaoVO;
	
	public GerarCobrancaValidacaoException(ValidacaoVO validacaoVO){
		
		this.validacaoVO = validacaoVO;
	}

	public ValidacaoVO getValidacaoVO() {
		return validacaoVO;
	}

	public void setValidacaoVO(ValidacaoVO validacaoVO) {
		this.validacaoVO = validacaoVO;
	}
}