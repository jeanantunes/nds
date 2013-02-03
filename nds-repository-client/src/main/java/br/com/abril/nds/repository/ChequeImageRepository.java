package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ChequeImage;

/**
 * 
 * @author Diego Fernandes
 *
 */
public interface ChequeImageRepository extends Repository<ChequeImage, Long> {
	/**
	 * Recupera a imagem do cheque
	 * @param idCheque
	 * @return
	 */
	public abstract ChequeImage get(long idCheque);
	/**
	 * Recupera a imagem do cheque
	 * @param idCheque
	 * @return bytes da imagem
	 */
	public abstract byte[] getImageCheque(long idCheque);

}
