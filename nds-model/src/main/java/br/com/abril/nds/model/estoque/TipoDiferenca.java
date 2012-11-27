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
	
	FALTA_DE(GrupoMovimentoEstoque.FALTA_DE, "Falta de"),
	FALTA_EM(GrupoMovimentoEstoque.FALTA_EM, "Falta em"),
	SOBRA_DE(GrupoMovimentoEstoque.SOBRA_DE, "Sobra de"),
	SOBRA_EM(GrupoMovimentoEstoque.SOBRA_EM, "Sobra em");
	
	private GrupoMovimentoEstoque tipoMovimentoEstoque;
	
	private String descricao;
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_DE = EnumSet.of(FALTA_DE, SOBRA_DE);

	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_EM = EnumSet.of(FALTA_EM, SOBRA_EM);
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_FALTA = EnumSet.of(FALTA_DE, FALTA_EM);
	
	private static final Set<TipoDiferenca> TIPOS_DIFERENCA_SOBRA = EnumSet.of(SOBRA_DE, SOBRA_EM);
	
	private TipoDiferenca(GrupoMovimentoEstoque tipoMovimentoEstoque, String descricao) {
		
		this.tipoMovimentoEstoque = tipoMovimentoEstoque;
		this.descricao = descricao;
	}
	
	public GrupoMovimentoEstoque getTipoMovimentoEstoque() {
		
		return tipoMovimentoEstoque;
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
	
	/**
     * Verifica se a diferença é do tipo SOBRA
     * 
     * @return true se o tipo de diferença é do tipo SOBRA, false caso contrário
     */
	public boolean isSobra() {
	    return TIPOS_DIFERENCA_SOBRA.contains(this);
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