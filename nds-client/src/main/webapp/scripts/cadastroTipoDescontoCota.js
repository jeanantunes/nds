var descontoCotaController = $.extend(true,{
		
	popup_especifico:function() {		
		 
		$("#selectFornecedorSelecionado_option_especifico",this.workspace).clear();
		$("#selectFornecedor_option_especifico",this.workspace).clear();
		
		$("#numCotaEspecifico",this.workspace).val("");
		$("#descontoEspecifico",this.workspace).val("");
		$("#descontoEspecifico",this.workspace).justPercent("floatValue");
		$("#descricaoCotaEspecifico",this.workspace).val("");
		
		$( "#dialog-especifico",this.workspace ).dialog({
			resizable: false,
			height:400,
			width:560,
			modal: true,
			buttons: [{
						id:"id_confirmar_especifico", text:"Confirmar",
						click: function() {
							descontoCotaController.novoDescontoEspecifico();
						}
				},{
					id:"id_close_especifico", text:"Cancelar",
					click: function() {
						$( this ).dialog( "close" );
					}
				}
			],
			form: $("#dialog-especifico", this.workspace).parents("form")
		});		      
	},
	
	novoDescontoEspecifico: function() {
		
		var cotaEspecifica = $("#numCotaEspecifico", this.workspace).val();
		var descontoEspecifico = $("#descontoEspecifico", this.workspace).justPercent("stringValue");
		
		var fornecedores = new Array();
		
		$("#selectFornecedorSelecionado_option_especifico option",this.workspace).each(function (index) {
			 fornecedores.push($(this).val());
		});
		
		var param = {numeroCota: cotaEspecifica, desconto: descontoEspecifico};		
		param = serializeArrayToPost('fornecedores', fornecedores, param);
		
		$.postJSON(contextPath +"/financeiro/tipoDescontoCota/novoDescontoEspecifico", param,				   
				function(result) {
			           
						 if (result.tipoMensagem && result.tipoMensagem != "SUCCESS" && result.listaMensagens) {			      
							   exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "idModalDescontoEspecifico");
					       }
						   else{
							   tipoDescontoController.fecharDialogs();
							   exibirMensagem(result.tipoMensagem, result.listaMensagens, "");
							   $(".tiposDescEspecificoGrid", this.workspace).flexReload();
						   }
	               },
				   null,
				   true,"idModalDescontoEspecifico");
		
		$(".tiposDescEspecificoGrid",this.workspace).flexReload();
	},
	
	pesquisarCotaSuccessCallBack:function(){
		
		var cotaEspecifica = $("#numCotaEspecifico",this.workspace).val();
		
		descontoCotaController.carregarFornecedoresCota("#selectFornecedor_option_especifico", cotaEspecifica);
		
	},

	carregarFornecedoresCota:function(idComboFornecedores,numeroCota){
		
		$.postJSON(contextPath + "/financeiro/tipoDescontoCota/obterFornecedoresCota",
				[{name:"numeroCota",value:numeroCota}], 
				function(result){
					
					if(result){
						var comboClassificacao =  montarComboBox(result, false);
						
						$(idComboFornecedores,this.workspace).html(comboClassificacao);
					}
				},function(result){
					
					$("#selectFornecedor_option_especifico",this.workspace).clear();
					
				},true,"idModalDescontoEspecifico"
		);
	},
	
	pesquisarCotaErrorCallBack:function(){
		
		exibirMensagemDialog("WARNING", [' Cota não encontrada!'], "idModalDescontoEspecifico");
		
		$("#selectFornecedorSelecionado_option_especifico",this.workspace).clear();
		$("#selectFornecedor_option_especifico",this.workspace).clear();
	},
	
	init: function() {
		
		$("select[name='selectFornecedorSelecionado_especifico']",this.workspace).multiSelect("select[name='selectFornecedor_especifico']", {trigger: "#linkFornecedorVoltarTodos_especifico"});
		
		$("select[name='selectFornecedor_especifico']",this.workspace).multiSelect("select[name='selectFornecedorSelecionado_especifico']", {trigger: "#linkFornecedorEnviarTodos_especifico"});
		
		$("#descontoEspecifico", this.workspace).justPercent();
		
		$(".tiposDescEspecificoGrid", this.workspace).flexigrid({
			preProcess: tipoDescontoController.executarPreProcessamento,
			onSuccess: function(){bloquearItensEdicao(tipoDescontoController.workspace);},
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Desconto %',
				name : 'desconto',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor(es)',
				name : 'fornecedor',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Alteração',
				name : 'dataAlteracao',
				width : 120,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'nomeUsuario',
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
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
		});		
	}
}, BaseController);
//@ sourceURL=tipoDescontoCota.js