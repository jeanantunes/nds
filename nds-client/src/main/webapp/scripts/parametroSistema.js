
var parametroSistemaController = $.extend(true, {
	path : contextPath + '/administracao/parametrosSistema/',
	init : function() {
		
		$("#dtOperacaoCorrente").mask("99/99/9999");
		
		$("#frequenciaExpurgo").numeric();
	},
	
	salvar: function() {
		
		$("#nfeDpecHidden", this.workspace).val($('#nfeDpec', this.workspace).is(":checked"));
		
		var formData = $("#formParametroSistema", this.workspace).serializeArray();
		
		$.postJSON(this.path + "salvar", 
			formData,
			function(result) {
				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens, "");
				}
		},
		null,true);
	}
	
}, BaseController);
