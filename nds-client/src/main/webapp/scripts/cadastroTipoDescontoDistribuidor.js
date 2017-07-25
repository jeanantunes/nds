var verificadorProgressoGravacaoDescontoGeral = null;

var descontoDistribuidorController = $.extend(true,{
		
		popup_geral:function () {
			
			$("#selectFornecedorSelecionado_option",this.workspace).clear();
			
			$("#descontoGeral",this.workspace).val("");
			
			tipoDescontoController.carregarFornecedores("#selectFornecedor_option");
			 
			$( "#dialog-geral",this.workspace ).dialog({
				resizable: false,
				height:450,
				width:700,
				modal: true,
				buttons: [{
							id:"id_confirmar_geral",text:"Confirmar",
							click: function() {								
								descontoDistribuidorController.novoDescontoGeral();
							}
						},{
							id:"id_close_geral",text:"Cancelar",
							click: function() {
								$( this ).dialog( "close" );
							}
						}
					],
				form: $("#dialog-geral", this.workspace).parents("form")
			});
		},

		novoDescontoGeral:function () {

			var fornecedores = new Array();
			
		    $("#selectFornecedorSelecionado_option option",this.workspace).each(function (index) {
		    	fornecedores.push($(this).val());
		    });
		    var param = {desconto:$("#descontoGeral",this.workspace).justPercent("stringValue")};
		    
		    param = serializeArrayToPost('fornecedores', fornecedores, param);

			$.postJSON(contextPath +"/financeiro/tipoDescontoCota/novoDescontoGeral",param,				   
				   function(result) {
			        
					   if (result.tipoMensagem && result.tipoMensagem !="SUCCESS" && result.listaMensagens) {			      
						   exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "");
				       }
					   else{
						   exibirMensagem(result.tipoMensagem, result.listaMensagens, "");
						   tipoDescontoController.fecharDialogs();
						   tipoDescontoController.pesquisar();
						   $(".tiposDescGeralGrid",this.workspace).flexReload();
					   }
		           },
				   null,
				   true,"idModalDescontoGeral");	

		}, 
		
		changeComboFornecedor : function(){
			var valueCombo = $("#selectFornecedor_option", tipoDescontoController.workspace).val();
			
			$.postJSON(contextPath +"/financeiro/tipoDescontoCota/obterDescontoProdutoPorFornecedor", {idFornecedor:valueCombo},				   
					   function(result) {
							var result2 = result;
			           },
					   function(result){
			        	   
			           });
		},
		
		init:function(){
			
			$("select[name='selectFornecedorSelecionado']",this.workspace).multiSelect("select[name='selectFornecedor']", {trigger: "#linkFornecedorVoltarTodos"});
			
			$("select[name='selectFornecedor']",this.workspace).multiSelect("select[name='selectFornecedorSelecionado']", {trigger: "#linkFornecedorEnviarTodos"});
			
			$("#descontoGeral",this.workspace).justPercent();
			
			$("#selectFornecedor_option", this.workspace).change(function(){
				descontoDistribuidorController.changeComboFornecedor();
			});
			
			$(".tiposDescGeralGrid",this.workspace).flexigrid({
				preProcess: tipoDescontoController.executarPreProcessamento,
				onSuccess: function(){bloquearItensEdicao(tipoDescontoController.workspace);},
					dataType : 'json',
					colModel : [ {
						display : '',
						name : 'sequencial',
						width : 60,
						sortable : true,
						align : 'center'
					},{
						display : 'Desconto %',
						name : 'desconto',
						width : 150,
						sortable : true,
						align : 'center'
					}, {
						display : 'Fornecedores',
						name : 'fornecedor',
						width : 320,
						sortable : true,
						align : 'left'
					}, {
						display : 'Data Alteração',
						name : 'dataAlteracao',
						width : 150,
						sortable : true,
						align : 'center'
					}, {
						display : 'Usuário',
						name : 'usuario',
						width : 150,
						sortable : true,
						align : 'left'
					}, {
						display : 'Ação',
						name : 'acao',
						width : 35,
						sortable : false,
						align : 'center'
					}],
					sortname : "dataAlteracao",
					sortorder : "desc",
					usepager : true,
					useRp : true,
					rp : 15,
					showTableToggleBtn : true,
					width : 960,
					height : 255
				});
				
		}
	}, BaseController);
//@ sourceURL=cadastroTipoDescontoDistribuidor.js	