package br.com.abril.nds.util;


public interface ImpressaoConstantes {

	//Constantes ref a impressao na matricial
		int MAX_UNITS = 127; //for vertical positioning range is between 0 - 255 (0 <= n <= 255) according to epson ref. but 255 gives weird errors at 1.5f, 127 as max (0 - 128) seems to be working
	    float CM_PER_INCH = 2.54f;

	    /* decimal ascii values for epson ESC/P commands */
	    
	    String ESC = "\u001B"; //escape
	    String AT =(char) 64+""; //@
	    String LINE_FEED = (char) 10+""; //line feed/new line
	    String PARENTHESIS_LEFT = (char)40+"";
	    String BACKSLASH = (char)92+"";
	    String CR = (char)13+""; //carriage return
	    String TAB = (char)9+""; //horizontal tab
	    String FF = (char)12+""; //form feed
	    String SI = (char)15+""; //condense
	    String g = (char)103+""; //15cpi pitch
	    String p = (char)112+""; //used for choosing proportional mode or fixed-pitch
	    String t = (char)116+""; //used for Stringacter set assignment/selection
	    String k = (char)107+""; //used for font
	    String l = (char)108+""; //used for setting left margin
	    String x = (char)120+""; //used for setting draft or letter quality (LQ) printing
	    String C = (char)67+""; //used for page lenght
	    String E = (char)69+""; //bold font on
	    String F = (char)70+""; //bold font off
	    String J = (char)74+""; //used for advancing paper vertically
	    String P = (char)80+""; //10cpi pitch
	    String Q = (char)81+""; //used for setting right margin
	    String NULL = (char)0 +""; //used for setting right margin

	    String $ = (char)36+""; //used for absolute horizontal positioning
	    String ARGUMENT_0 = (char)0+"";
	    String ARGUMENT_1 = (char)1+"";
	    String ARGUMENT_2 = (char)2+"";
	    String ARGUMENT_3 = (char)3+"";
	    String ARGUMENT_4 = (char)4+"";
	    String ARGUMENT_5 = (char)5+"";
	    String ARGUMENT_6 = (char)6+"";
	    String ARGUMENT_7 = (char)7+"";
	    String ARGUMENT_25 = (char)25+"";

	    /* Stringacter sets */
	    String USA = ARGUMENT_1;
	    String BRAZIL = ARGUMENT_25;
	    
	    String CARACTER_INDENT_LINEFEED_SCAPE = "###";
	    int LARGURA_FOLHA_MATRICIAL = 40;
	    String ESPACO = " ";
	    String TRACO = "-";
	    
	    String NOME_PADRAO_IMPRESSORA_NAO_MATRICIAL = "impressora_dgb_default_nao_matricial";
	    String NOME_PADRAO_IMPRESSORA_MATRICIAL = "impressora_dgb_default_matricial";
}
