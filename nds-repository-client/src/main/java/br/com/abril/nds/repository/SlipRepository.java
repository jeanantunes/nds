package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.movimentacao.Slip;


public interface SlipRepository extends Repository<Slip, Long>{
    
    Slip obterPorNumeroCotaData(Integer numeroCota, Date dataConferencia);

    List<DebitoCreditoCota> obterComposicaoSlip(Long idSlip, boolean composicao);

	Map<Long, Slip> obterSlipsPorCotasData(List<Integer> listaCotas, Date dataDe, Date dataAte);

	List<Long> obterIdsSlipsPorCotasDataOrdenados(List<Integer> listaCotas, Date dataDe, Date dataAte);
}
