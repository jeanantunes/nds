package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.util.Intervalo;

public interface GeracaoNotaEnvioService {

	public abstract List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox, Intervalo<Integer> intervalorCota, Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, String sortname, String sortorder, Integer resultsPage, Integer page,
			SituacaoCadastro situacaoCadastro, Long idRoteiro, Long idRota);

	public abstract NotaEnvio gerar(Long idCota, Long idRota, String chaveAcesso,
			Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao, Date dataEmissao, Intervalo<Date> periodo, List<Long> listaIdFornecedores);

}
