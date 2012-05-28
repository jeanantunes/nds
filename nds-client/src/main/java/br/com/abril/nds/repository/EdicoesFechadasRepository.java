package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.estoque.MovimentoEstoque;


/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * @author InfoA2
 */
public interface EdicoesFechadasRepository extends Repository<MovimentoEstoque, Long> {

	/**
	 * Retorna a lista de edições fechadas em um período de datas
	 * @param dataDe
	 * @param dateAte
	 * @return
	 */
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dateAte);

	/**
	 * Retorna a lista de edições fechadas filtrada pelo código do fornecedor em um período de datas
	 * @param dataDe
	 * @param dateAte
	 * @param codigoFornecedor
	 * @return
	 */
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dateAte, String codigoFornecedor);

}
