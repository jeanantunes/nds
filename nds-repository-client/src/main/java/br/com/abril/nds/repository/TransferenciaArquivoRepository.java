package br.com.abril.nds.repository;

import java.util.List;

import org.hibernate.SQLQuery;

import br.com.abril.nds.dto.DiretorioTransferenciaArquivoDTO;
import br.com.abril.nds.model.DiretorioTransferenciaArquivo;
import br.com.abril.nds.util.Util;

public interface TransferenciaArquivoRepository extends Repository<DiretorioTransferenciaArquivo, Long> {

	List<DiretorioTransferenciaArquivoDTO> carregarDiretorios(DiretorioTransferenciaArquivoDTO filtro);

	Boolean isDiretorioExistente(String pathDiretorio);

}
