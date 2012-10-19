var verificadorProgressoGravacaoDescontoGeral = null;

var descontoDistribuidorController = $.extend(true,{
		
		popup_geral:function () {
			
			$("#selectFornecedorSelecionado_option",this.workspace).clear();
			
			$("#descontoGeral",this.workspace).val("");
			
			tipoDescontoController.carregarFornecedores("#selectFornecedor_option");
			 
			$( "#dialog-geral",this.workspace ).dialog({
				resizable: false,
				height:345,
				width:550,
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

			var descontoGeral = $("#descontoGeral",this.workspace).justPercent("floatValue");;
			
			var fornecedores = new Array();
			
		    $("#selectFornecedorSelecionado_option option",this.workspace).each(function (index) {
		    	fornecedores.push($(this).val());
		    });
		    var param = {desconto:$("#descontoGeral",this.workspace).val()};
		    
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

		    verificadorProgressoGravacaoDescontoGeral = setInterval(function () {
				$.getJSON(contextPath +"/financeiro/tipoDescontoCota/verificaProgressoGravacaoDescontoGeral",
						   null,				   
						   function(result) {
						   		if (!result.ativo) {
						   			exibirMensagem(result.validacao.tipoMensagem, result.validacao.listaMensagens, "");
						   			clearInterval(verificadorProgressoGravacaoDescontoGeral);
						   		}
					   	   });
		    }, 20000);
			
		}, 
		
		init:function(){
			
			$("select[name='selectFornecedorSelecionado']",this.workspace).multiSelect("select[name='selectFornecedor']", {trigger: "#linkFornecedorVoltarTodos"});
			
			$("select[name='selectFornecedor']",this.workspace).multiSelect("select[name='selectFornecedorSelecionado']", {trigger: "#linkFornecedorEnviarTodos"});
			
			$("#descontoGeral",this.workspace).justPercent();
			
			$(".tiposDescGeralGrid",this.workspace).flexigrid({
				preProcess: tipoDescontoController.executarPreProcessamento,
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
					sortname : "sequencial",
					sortorder : "asc",
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