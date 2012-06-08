package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;

public interface RoteirizacaoRepository extends Repository<Roteirizacao, Long> {
	
	Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota);
	
	/**
	 * Retorna uma lista de Roteirizacao.
	 * @param sortname - nome do campo para ordenação
	 * @param ordenacao - tipo da ordenção 
	 * @return List<Rota>
	 */
    List<Roteirizacao> buscarRoterizacaoPorRota(Long rotaId);
    
    Integer buscarMaiorOrdem(Long rotaId);
    
    List<PDV> buscarRoteirizacaoNumeroCota(Integer numeroCota, Long rotaId, Roteiro roteiro  );
    
    List<PDV> buscarRoteirizacaoPorEndereco (String CEP, String uf, String municipio, String bairro, Long rotaId , Roteiro roteiro );
    	
	List<String> buscarUF();
	
	List<LogLocalidade> buscarMunicipioPorUf(String uf);
	
	List<LogBairro> buscarBairroPorMunicipio(Long municipio, String uf);
}

