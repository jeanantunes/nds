package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.EstudoGerado;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.EstudoGerado}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoGeradoRepository extends Repository<EstudoGerado, Long> {
	
	void liberarEstudo(List<Long> listIdEstudos, boolean liberado);
	
	public EstudoGerado obterEstudoECotasPorIdEstudo(Long idEstudo);
	
	ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long id);
	
	public EstudoGerado obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO);
	
	public List<EstudoGerado> obterEstudosPorIntervaloData(Date dataStart, Date dataEnd);

	public void setIdLancamentoNoEstudo(Long idLancamento, Long idEstudo);

	public Long countDeCotasEntreEstudos(Long estudoBase, Long estudoSomado);

    int obterCotasComRepartePorIdEstudo(Long estudoId);

	Long obterUltimoAutoIncrement();
	
	BigDecimal reparteEstudoOriundoDoLancamento(Long idEstudo);	
}
