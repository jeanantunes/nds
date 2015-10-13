package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.DiretorioTransferenciaArquivoDTO;
import br.com.abril.nds.model.DiretorioTransferenciaArquivo;

public interface TransferenciaArquivoRepository extends Repository<DiretorioTransferenciaArquivo, Long> {

	List<DiretorioTransferenciaArquivoDTO> carregarDiretorios(DiretorioTransferenciaArquivoDTO filtro);

}
