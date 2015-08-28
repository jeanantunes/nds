package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.EstudoGeradoPreAnaliseDTO;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.EstudoGerado}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoGeradoRepository extends Repository<EstudoGerado, Long> {
	
	EstudoGeradoPreAnaliseDTO obterEstudoPreAnalise(Long id);
	
	void liberarEstudo(List<Long> listIdEstudos, boolean liberado);
	
	EstudoGerado obterEstudoECotasPorIdEstudo(Long idEstudo);
	
	ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long id, boolean isEdicoesBaseEspecificas);
	
	EstudoGerado obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO);
	
	List<EstudoGerado> obterEstudosPorIntervaloData(Date dataStart, Date dataEnd);

	void setIdLancamentoNoEstudo(Long idLancamento, Long idEstudo);

	Long countDeCotasEntreEstudos(Long estudoBase, Long estudoSomado);

    int obterCotasComRepartePorIdEstudo(Long estudoId);

	BigDecimal reparteFisicoOuPrevistoLancamento(Long idEstudo);
	
	EstudoGerado obterParaAtualizar(Long id);

	Long countEstudoGeradoParaLancamento(Long idLancamento, Date dtLancamento);

	List<EstudoGerado> obterPorLancamentoId(Long idLancamento);
	
   EstudoGerado obterEstudoSql(Long id);
	
}