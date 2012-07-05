/**
 * Gerencia Ação da telca Enter
 * 
 * Caso haja necessidade de alterar a ação padrão da tecla enter ou seu botão pesquisar não
 * possua o ID 'btnPesquisar', utilize o método 'definir()' passando o id do botão a ser clicado.
 * Ex: Tela de Confirmação de Expedição - 'confirmaçãoExpedicao/confirmaçãoExpedicao.jsp'
 */

var AcaoEnter = new AcaoEnter();

/**
 * Define "btnPesquisar" como id padrão do componente a ser executado na tecla Enter
 */
$(document).ready(function(){	
	AcaoEnter.definir('btnPesquisar');
});

function AcaoEnter() {
	
	var T = this;
	
	var idBtnPesquisar = null;
		
	/**
	 * Define novo id deBotão do componente a ser executado na tecla Enter
	 */
	this.definir = function(idBotaoPesquisar) {	   
	
		idBtnPesquisar = idBotaoPesquisar;
		
		$("input").bind("keydown", T.clickPesquisar);
		$("select").bind("keydown", T.clickPesquisar);		
	},
	
	this.clickPesquisar = function(event) {
		
		  var keycode = (event.keyCode ? event.keyCode : (event.which ? event.which : event.charCode));
	      if (keycode == 13) { 	    	  
	    	  document.getElementById(idBtnPesquisar).click();
	    	  return false;
	      } else  {
	    	  return true;
	      }
	};
}

 