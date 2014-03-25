package br.com.abril.nds.model.estoque;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoDiferenca {
	
	FALTA_DE(GrupoMovimentoEstoque.FALTA_DE, "Falta de", GrupoMovimentoEstoque.FALTA_DE_COTA),
	FALTA_EM(GrupoMovimentoEstoque.FALTA_EM, "Falta em", GrupoMovimentoEstoque.FALTA_EM_COTA),
	SOBRA_DE(GrupoMovimentoEstoque.SOBRA_DE, "Sobra de", GrupoMovimentoEstoque.SOBRA_DE_COTA),
	SOBRA_EM(GrupoMovimentoEstoque.SOBRA_EM, "Sobra em", GrupoMovimentoEstoque.SOBRA_EM_COTA),
	SOBRA_ENVIO_PARA_COTA(GrupoMovimentoEstoque.SOBRA_ENVIO_PARA_COTA, "Sobra envio para Cota"),
	FALTA_PARA_COTA(GrupoMovimentoEstoque.FALTA_PARA_COTA, "Falta para Cota"),
	AJUSTE_REPARTE_FALTA_COTA(GrupoMovimentoEstoque.AJUSTE_REPARTE_FALTA_COTA, "Ajuste de falta para Cota"),
	SOBRA_DE_DIRECIONADA_COTA(GrupoMovimentoEstoque.SOBRA_DE_DIRECIONADA_PARA_COTA, "Sobra DE direcionada para Cota", GrupoMovimentoEstoque.SOBRA_DE_COTA),
	SOBRA_EM_DIRECIONADA_COTA(GrupoMovimentoEstoque.SOBRA_EM_DIRECIONADA_PARA_COTA, "Sobra EM direcionada para Cota", GrupoMovimentoEstoque.SOBRA_EM_COTA),
	PERDA_EM(GrupoMovimentoEstoque.PERDA_EM, "Perda em"),
	PERDA_DE(GrupoMovimentoEstoque.PERDA_DE, "Perda de"),
	GANHO_EM(GrupoMovimentoEstoque.GANHO_EM, "Ganho em"),
	GANHO_DE(GrupoMovimentoEstoque.GANHO_DE, "Ganho de"),
	ALTERACAO_REPARTE_PARA_LANCAMENTO(GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_LANCAMENTO, "Alteração de Reparte", GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA),
	ALTERACAO_REPARTE_PARA_RECOLHIMENTO(GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_RECOLHIMENTO, "Alteração de Reparte", GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA),
	ALTERACAO_REPARTE_PARA_SUPLEMENTAR(GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR, "Alteração de Reparte", GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA),
	ALTERACAO_REPARTE_PARA_PRODUTOS_DANIFICADOS(GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA_PARA_PRODUTOS_DANIFICADOS, "Alteração de Reparte", GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA);
	
	private GrupoMovimentoEstoque grupoMovimentoEstoque;
	
	private String descricao;
	
	private GrupoMovimentoEstoque grupoMovimentoEstoqueCota;
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_DE = EnumSet.of(FALTA_DE, SOBRA_DE);

	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_EM = EnumSet.of(FALTA_EM, SOBRA_EM);
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_FALTA = EnumSet.of(FALTA_DE, FALTA_EM);
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_SOBRA = EnumSet.of(SOBRA_DE, SOBRA_EM, SOBRA_DE_DIRECIONADA_COTA, SOBRA_EM_DIRECIONADA_COTA);
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_PERDA = EnumSet.of(PERDA_DE, PERDA_EM);
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_GANHO = EnumSet.of(GANHO_DE, GANHO_EM);
	
	private static final Set<TipoDiferenca> AJUSTES_DISTRIBUIDOR = EnumSet.of(SOBRA_ENVIO_PARA_COTA, FALTA_PARA_COTA, AJUSTE_REPARTE_FALTA_COTA);
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_ALTERACAO_REPARTE = 
		EnumSet.of(ALTERACAO_REPARTE_PARA_LANCAMENTO, ALTERACAO_REPARTE_PARA_PRODUTOS_DANIFICADOS, 
			ALTERACAO_REPARTE_PARA_RECOLHIMENTO, ALTERACAO_REPARTE_PARA_SUPLEMENTAR);
	
	private TipoDiferenca(GrupoMovimentoEstoque grupoMovimentoEstoqueCota, 
						  String descricao) {
		
		this.grupoMovimentoEstoque = grupoMovimentoEstoqueCota;
		this.descricao = descricao;
	}
	
	private TipoDiferenca(GrupoMovimentoEstoque tipoMovimentoEstoque, 
						  String descricao,
						  GrupoMovimentoEstoque grupoMovimentoEstoqueCota) {
		
		this.grupoMovimentoEstoque = tipoMovimentoEstoque;
		this.descricao = descricao;
		this.grupoMovimentoEstoqueCota = grupoMovimentoEstoqueCota;
	}
	
	public GrupoMovimentoEstoque getTipoMovimentoEstoque() {
		
		return grupoMovimentoEstoque;
	}
	
	public GrupoMovimentoEstoque getGrupoMovimentoEstoqueCota() {
		
		return grupoMovimentoEstoqueCota;
	}

	public String getDescricao() {
		return descricao;
	}
	
    /**
     * Verifica se a diferença é do tipo DE
     * 
     * @return true se o tipo da diferença é do tipo DE, false caso contrário
     */
	public boolean isDiferencaDe() {
	    return TIPOS_DIFERENCA_DE.contains(this);
	}
	
	/**
	 * Verifica se a diferença é do tipo FALTA
	 * 
	 * @return true se o tipo de diferença é do tipo FALTA, false caso contrário
	 */
	public boolean isFalta() {
	    return TIPOS_DIFERENCA_FALTA.contains(this);
	}
	
	public boolean isAjusteReparteFaltaCota() {
	    return AJUSTE_REPARTE_FALTA_COTA.equals(this);
	}
	
	/**
     * Verifica se a diferença é do tipo SOBRA
     * 
     * @return true se o tipo de diferença é do tipo SOBRA, false caso contrário
     */
	public boolean isSobra() {
	    return TIPOS_DIFERENCA_SOBRA.contains(this);
	}
	
	public boolean isPerda() {
		return TIPOS_DIFERENCA_PERDA.contains(this);  
	}
	
	public boolean isGanho() {
		return TIPOS_DIFERENCA_GANHO.contains(this);  
	}
	
	public boolean isAlteracaoReparte() {
		return TIPOS_DIFERENCA_ALTERACAO_REPARTE.contains(this);
	}
	
	public boolean isAjusteDistribuidor() {
		return AJUSTES_DISTRIBUIDOR.contains(this);
	}
	
	/**
	 * Separa os tipos de diferença DE
	 * 
	 * @return subgrupo de tipos de diferença DE
	 */
	public static Collection<TipoDiferenca> getTiposDiferencaDe() {
	    return Collections.unmodifiableSet(TIPOS_DIFERENCA_DE);
	}
	
	/**
	 * Separa os tipos de diferença EM
	 * 
	 * @return subgrupo de tipos de diferença EM
	 */
	public static Collection<TipoDiferenca> getTiposDiferencaEm() {
        return Collections.unmodifiableSet(TIPOS_DIFERENCA_EM);
    }
	
	@Override
	public String toString() {
	
		return this.getDescricao();
	}
	
}