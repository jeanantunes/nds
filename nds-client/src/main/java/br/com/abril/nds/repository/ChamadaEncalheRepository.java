package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;

public interface ChamadaEncalheRepository extends Repository<ChamadaEncalhe, Long> {
	
	ChamadaEncalhe obterPorNumeroEdicaoEDataRecolhimento(ProdutoEdicao produtoEdicao,
														 Date dataRecolhimento,
														 TipoChamadaEncalhe tipoChamadaEncalhe);
	
	List<ChamadaEncalhe> obterPorPeriodoTipoChamadaEncalhe(Date dataInicialRecolhimento,
													   	   Date dataFinalRecolhimento,
													   	   TipoChamadaEncalhe tipoChamadaEncalhe);
}
