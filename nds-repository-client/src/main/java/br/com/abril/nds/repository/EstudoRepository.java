package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.Estudo;

/**
 * Interface que define as regras de acesso a dados referentes a entidade {@link br.com.abril.nds.model.planejamento.Estudo}
 * 
 * @author Discover Technology
 * 
 */
public interface EstudoRepository extends Repository<Estudo, Long> {

    Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao);

    void liberarEstudo(List<Long> listIdEstudos, boolean liberado);

    public Estudo obterEstudoECotasPorIdEstudo(Long idEstudo);

    public List<Estudo> obterEstudosPorIntervaloData(Date dataStart, Date dataEnd);

    ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long id);

    public Estudo obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO);

    public Long obterMaxId();
}
