package br.com.abril.nds.controllers.cadastro;

/**
 * Enum com os modos de operação da tela de cadastro de cota
 * 
 * @author francisco.garcia
 * 
 */
public enum ModoTela {
     
    /**
     * Indica que a tela estará operando para
     * o cadastro / edição da cota 
     */
    CADASTRO_COTA, 
     
    /**
     * Indica que a tela está operando para 
     * consulta de histórico de titularidade da cota
     */
    HISTORICO_TITULARIDADE;
     
}