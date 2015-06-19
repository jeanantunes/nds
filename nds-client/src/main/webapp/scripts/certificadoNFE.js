var certificadoNFEController  = $.extend(true, {

	path : contextPath +"/nfe/certificado/",
	
	init : function() {
		
		this.initDatas();
		this.initFlexiGrids();
		
	},

	initDatas : function() {
		$("#certificado-data-inicio", certificadoNFEController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			defaultDate: new Date()
		});
		
		$("#certificado-data-fim", certificadoNFEController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			defaultDate: new Date()
		});
	},
	
	initFlexiGrids : function() {
		
		
	},
	
	upload : function() {
		
	},
	
	confirmar : function() {
		
	},
	
	limparTabela : function() {
		
	},

	exibirMensagemSucesso: function (result){
		
		var tipoMensagem = result.tipoMensagem;
		var listaMensagens = result.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		}
	},
	
}, BaseController);
//@ sourceURL=certificadoNFE.js