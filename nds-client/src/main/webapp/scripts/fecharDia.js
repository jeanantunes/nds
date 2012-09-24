var fecharDiaController =  $.extend(true, {
	
	init : function(){
		
	},
	
	popup : function() {	
		
		$( "#dialog-novo", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-novo", fecharDiaController.workspace).parents("form")
		});
	},
	
	popup_repartes : function() {		
		
		$( "#dialog-repartes", fecharDiaController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-repartes", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_recolhimento : function(){
		
		$( "#dialog-recolhimentos", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-recolhimentos", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_suplementar : function(){		
		
		$( "#dialog-suplementares", fecharDiaController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-suplementares", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_vendasTot : function() {		
		
		$( "#dialog-venda-total",  fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:900,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
			form: $("#dialog-venda-total", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	popup_cotasGrid : function(){
	
		$( "#dialog-cota-grid", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:390,
			width:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			},
			form: $("#dialog-cota-grid", fecharDiaController.workspace).parents("form")
		});
	},
	
	popup_boletos_baixados : function(){
	
		$( "#dialog-boletos-baixados", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:430,
			width:800,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
				
			},
			form: $("#dialog-boletos-baixados", fecharDiaController.workspace).parents("form")
		});	
		      
	},
	
	//callback function to bring a hidden box back
	callback : function(){
		setTimeout(function() {
			$( "#effect:visible").removeAttr( "style" ).fadeOut();

		}, 1000 );
	},
	
	mostrar : function(){
		$(".grids", fecharDiaController.workspace).show();
	},
	
	popup_processos : function() {
		fecharDiaController.iniciarValidacoes();
		$( "#dialog-processos", fecharDiaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					$(".grids").show();
					
				}
			},
			form: $("#dialog-processos", fecharDiaController.workspace).parents("form")
		});
		
		
		      
	},
	
	iniciarValidacoes : function(){
		$.postJSON(contextPath + "/administracao/fecharDia/inicializarValidacoes", null,
				function(result){
					fecharDiaController.validacaoBaixaBancaria(result);
				});
	},
	
	validacaoBaixaBancaria : function(result){
		var baixaBancaria = "<tr class='class_linha_1'><td>Baixa Bancária</td>";					
		var iconeBaixaBancaria = null;
		if(result.baixaBancaria){
			iconeBaixaBancaria = 'ico_bloquear.gif';
		}else{
			iconeBaixaBancaria = 'ico_check.gif';
		}
		var imagem = "<td align='center'><img src='"+ contextPath +"/images/"+iconeBaixaBancaria+"' alt='Processo Efetuado' width='16' height='16' /></td></tr>";
		$('#tabela-validacao').append(baixaBancaria + imagem);
	}
	
}, BaseController);