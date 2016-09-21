var parametroSistemaController = $.extend(true, {
	path : contextPath + '/administracao/parametrosSistema/',
	init : function() {
		
		$("#dtOperacaoCorrente").mask("99/99/9999");
		
		$("#frequenciaExpurgo").numeric();
		
		$("#ftfCnpjEstabelecimentoEmissor", this.workspace).mask("99.999.999/9999-99");
	},
	
	salvar: function() {
		
		$("#autenticaEmailHidden", this.workspace).val($('#autenticaEmail', this.workspace).is(":checked"));
		
		var formData = $("#formParametroSistema", this.workspace).serializeArray();
		
		$.postJSON(this.path + "salvar", 
			formData,
			function(result) {
				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens, "");
				}
				
				$(".tipsy").hide();
				
				$('.ui-tabs-selected .ui-icon-close').click();
				
				$("#menu_principal ul li ul li a[href^='"+ parametroSistemaController.path.substring(0, parametroSistemaController.path.length - 1) +"']").click();
				
		},
		null,true);
		
		
		
		//setTimeout($("#menu_principal ul li ul li a[href^='"+ this.path.substring(0, this.path.length - 1) +"']").click(), 200);
				
	},
	
remover: function() {
		
		
		var formData = $("#formParametroSistema", this.workspace).serializeArray();
		
		$.postJSON(this.path + "removerTravas", 
			formData,
			function(result) {
				
				
				
				
		},
		null,true);
		
		
		
		//setTimeout($("#menu_principal ul li ul li a[href^='"+ this.path.substring(0, this.path.length - 1) +"']").click(), 200);
				
	},
	
testEmail: function() {
		
		
		var formData = $("#formParametroSistema", this.workspace).serializeArray();
		
		$.postJSON(this.path + "testEmail", 
			formData,
			function(result) {
			exibirMensagem("ERROR",["Email enviado com Sucesso"]);
		    
				
		},
		function(result) {
		
		  
		    exibirMensagem("ERROR",[result.mensagens.listaMensagens[0]]);
				
		},
		
		true);
		
		
		
		//setTimeout($("#menu_principal ul li ul li a[href^='"+ this.path.substring(0, this.path.length - 1) +"']").click(), 200);
				
	},
	
}, BaseController);