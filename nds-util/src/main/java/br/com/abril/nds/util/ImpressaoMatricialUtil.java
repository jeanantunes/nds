package br.com.abril.nds.util;


import java.io.IOException;

import javax.print.PrintException;
import javax.print.PrintService;

import org.apache.commons.lang.StringUtils;



public class ImpressaoMatricialUtil {
    
    
    private final StringBuilder saida;
    
    public ImpressaoMatricialUtil(final StringBuilder saida) {
        this.saida = saida;
    }
    
    
    public static void main(final String[] args) {
        final StringBuilder sb = new StringBuilder();
        new ImpressaoMatricialUtil(sb).adicionarCompleteTraco("12", "500,66");
        System.out.println(sb);
        System.out.println(sb.length());
    }
    
    public void adicionar(final String texto) {
        saida.append(texto);
    }
    
    public String preencher(final String texto, final String caracterPreenchimento, final int qtd){
        
        return StringUtils.rightPad(texto, qtd, caracterPreenchimento);
        
    }
    
    public void adicionarCompleteEspaco(final String textoColuna, final String valor) {
        adicionar(textoColuna, valor, ImpressaoConstantes.LARGURA_FOLHA_MATRICIAL, ImpressaoConstantes.ESPACO);
    }
    
    public void adicionarCompleteTraco(final String textoColuna, final String valor) {
        adicionar(textoColuna, valor, ImpressaoConstantes.LARGURA_FOLHA_MATRICIAL, ImpressaoConstantes.TRACO);
    }
    
    public void adicionar(final String textoColuna, final String valor, final int tamanhoColuna, final String caracterPreenchimento) {
        if(textoColuna != null){
            
            if((textoColuna.length() + valor.length()) > tamanhoColuna){//Maior que o tamanho total
                adicionar(textoColuna, tamanhoColuna - valor.length());//trunca conforme o tamanho
            }else{
                final String preencher = preencher(textoColuna, caracterPreenchimento, tamanhoColuna-(textoColuna.length()+valor.length()));
                adicionar(preencher);
            }
            adicionar(valor);
        }
    }
    
    public void adicionar(String texto, int tamanhoColuna) {
        if(texto != null){
            if(texto.length() > tamanhoColuna){
                texto = texto.substring(0, tamanhoColuna);
            }
            tamanhoColuna = tamanhoColuna - texto.length();
        }
        
        saida.append(texto).append(getEspaco(tamanhoColuna));
    }
    
    
    public void darEspaco(final int qtde) {
        saida.append(getEspaco(qtde));
    }
    
    public String getEspaco(final int qtde) {
        return StringUtils.rightPad("", qtde);
    }
    
    //negrito
    public void adicionarNegrito(final String texto) {
        
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.E + texto + ImpressaoConstantes.ESC + ImpressaoConstantes.F);
        
    }
    
    public void inic(){
        //reset default settings
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.AT);
    }
    
    //define tabela de caracteres
    
    public void setCharacterSet(final String charset) {
        //assign character table
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.PARENTHESIS_LEFT + ImpressaoConstantes.t + ImpressaoConstantes.ARGUMENT_3 + ImpressaoConstantes.ARGUMENT_0 + ImpressaoConstantes.ARGUMENT_1 + charset + ImpressaoConstantes.ARGUMENT_0);
        //adicionar("\u001B" + "(t" + (char)3 + (char)0 + (char)0 + (char)25 + (char)0);
        
        
        //select character table
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.t + ImpressaoConstantes.ARGUMENT_1);
        //adicionar("\u001B" + "t" + (char)0);
        //Character.toString( ESC);
        
    }
    
    public void quebrarLinha(final int qtde) {
        for (int x1 = 1; x1 <= qtde; x1++) {
            quebrarLinha();
        }
    }
    
    public void quebrarLinhaEscape(final int qtde) {
        for (int x1 = 1; x1 <= qtde; x1++) {
            quebrarLinhaEscape();
        }
    }
    
    //pula linha
    public void quebrarLinha() {
        
        adicionar(ImpressaoConstantes.CR + ImpressaoConstantes.LINE_FEED);//according to epson esc/p ref. manual always send carriage return before line feed
    }
    
    
    //pula linha
    public void quebrarLinhaEscape() {
        
        adicionar(ImpressaoConstantes.CARACTER_INDENT_LINEFEED_SCAPE);
    }
    
    
    /**
     * Ejeta folha
     */
    public void ejectFolha() {
        //post: ejects single sheet
        adicionar(ImpressaoConstantes.CR + ImpressaoConstantes.FF );
        
    }
    public void comecoPag(){
        adicionar(ImpressaoConstantes.CR);
    }
    
    
    /**
     * pre: centimeters >= 0p
     * post: advances horizontal print position approx. centimeters
     * @param centimeters
     */
    public void avancaHorizontal(final float cem) {
        //pre: centimeters >= 0
        //post: advances horizontal print position approx. centimeters
        final float inches = cem / ImpressaoConstantes.CM_PER_INCH;
        final int units_low = (int) (inches * 120) % 256;
        final int units_high = (int) (inches * 120) / 256;
        
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.BACKSLASH + (char) units_low + (char) units_high);
        
    }
    
    /**
     * pre: centimenters >= 0 (cm)
     * post: sets absolute horizontal print position to x centimeters from left margin
     * @param centimeters
     */
    public void avancaAbsHorizontal(final float cem) {
        
        final float inches = cem / ImpressaoConstantes.CM_PER_INCH;
        final int units_low = (int) (inches * 60) % 256;
        final int units_high = (int) (inches * 60) / 256;
        
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.$ + (char) units_low + (char) units_high);
        
    }
    
    public void avancaVertical(final float cem) {
        /*
        float inches = cem*256 / CM_PER_INCH;
        adicionar(ESC + J + (char) inches);
         * */
        //pre: centimeters >= 0 (cm)
        //post: advances vertical print position approx. y centimeters (not precise due to truncation)
        final float inches = cem / ImpressaoConstantes.CM_PER_INCH;
        int units = (int) (inches * 216);
        
        while (units > 0) {
            char n;
            if (units > ImpressaoConstantes.MAX_UNITS) {
                n = (char) ImpressaoConstantes.MAX_UNITS; //want to move more than range of parameter allows (0 - 255) so move max amount
            }
            else {
                n = (char) units; //want to move a distance which fits in range of parameter (0 - 255)
            }
            
            adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.J + n);
            
            units -= ImpressaoConstantes.MAX_UNITS;
        }
        
    }
    
    /**
     * //pre: tabs >= 0
     * //post: performs horizontal tabs tabs number of times
     * @param tabs
     */
    public void horizontalTab(final int tabs) {
        //pre: tabs >= 0
        //post: performs horizontal tabs tabs number of times
        for (int i = 0; i < tabs; i++) {
            adicionar(ImpressaoConstantes.TAB+"");
        }
    }
    
    /**
     * pre: columnsLeft > 0 && <= 255, columnsRight > 0 && <= 255
     * post: sets left margin to columnsLeft columns and right margin to columnsRight columns
     * @param columnsLeft
     * @param columnsRight
     */
    public void setMargem(final int colunaEsq, final int colunaDir) {
        //esquerda
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.l + (char) colunaEsq);
        
        //direita
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.Q + (char) colunaDir);
    }
    
    
    /**
     * This command sets the page length in 1-inch increments only.
     * Set the page length before paper is loaded or when the print position is at the top-ofform
     * position. Otherwise, the current print position becomes the top-of-form position.
     * Setting the page length cancels the top and bottom-margin settings.
     * @param tam
     */
    public void setTamPag(final float tam){
        final float inches = tam / ImpressaoConstantes.CM_PER_INCH;
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.C + ImpressaoConstantes.NULL+ (char) inches);
    }
    /**
     * ESC k 0 -Roman
     * ESC k 1 -Sans serif
     * @param fonte
     */
    public void setFonte(final int fonte){
        adicionar(ImpressaoConstantes.ESC + ImpressaoConstantes.k + (char) fonte);
    }
    
    public void setCondensado(){
        adicionar(ImpressaoConstantes.SI);
    }
    
    public void setRascunho() { //set draft quality printing
        adicionar(ImpressaoConstantes.ESC+ ImpressaoConstantes.x +(char) 48);
    }
    
    public void setLQ() { //set letter quality printing
        adicionar(ImpressaoConstantes.ESC+ ImpressaoConstantes.x +(char) 49);
    }
    public void set10CPI() { //10 characters per inch (condensed available)
        adicionar(ImpressaoConstantes.ESC+ ImpressaoConstantes.P);
    }
    
    public void imprimirImpressoraPadrao(String saida) throws PrintException, IOException {
        
        saida = replaceComandosImpressao(saida);
        
        new ImpressoraUtil().imprimir(saida.getBytes(), ImpressoraUtil.getImpressoraLocalConfiguradaPadrao());
    }
    
    
    public void imprimir() throws PrintException, IOException {
        
        if (saida == null) {
            return;
        }
        
        imprimirImpressoraPadrao(saida.toString());
    }
    
    public void imprimir(final PrintService impressora) throws PrintException, IOException {
        
        if (saida == null) {
            return;
        }
        
        new ImpressoraUtil().imprimir(replaceComandosImpressao(saida.toString()).getBytes(), impressora);
    }
    
    private String replaceComandosImpressao(String saida) {
        saida = saida.replaceAll(ImpressaoConstantes.CARACTER_INDENT_LINEFEED_SCAPE, ImpressaoConstantes.CR + ImpressaoConstantes.LINE_FEED);
        System.out.println("#########>>>>>>>>>>>>>>>");
        System.out.println(saida);
        System.out.println("<<<<<<<<<<<<<<<<###############");
        return saida;
    }
}
