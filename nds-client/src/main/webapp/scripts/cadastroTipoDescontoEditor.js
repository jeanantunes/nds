var descontoEditorController = $.extend(true,{
		
	popup_editor:function() {		
		 
		$("#selectFornecedorSelecionado_option_editor",this.workspace).clear();
		$("#selectFornecedor_option_editor",this.workspace).clear();
		
		$("#numCotaEditor",this.workspace).val("");
		$("#descontoEditor",this.workspace).val("");
		$("#descontoEditor",this.workspace).justPercent("floatValue");
		$("#descricaoCotaEditor",this.workspace).val("");
		
		$( "#dialog-editor",this.workspace ).dialog({
			resizable: false,
			height:400,
			width:560,
			modal: true,
			buttons: [{
						id:"id_confirmar_editor", text:"Confirmar",
						click: function() {
							descontoEditorController.novoDescontoEditor();
						}
				},{
					id:"id_close_editor", text:"Cancelar",
					click: function() {
						$( this ).dialog( "close" );
					}
				}
			],
			form: $("#dialog-editor", this.workspace).parents("form")
		});		      
	},
	
	novoDescontoEditor: function() {
		
		var cotaEspecifica = $("#numCotaEditor", this.workspace).val();
		var descontoEditor = $("#descontoEditor", this.workspace).justPercent("stringValue");
		
		var fornecedores = new Array();
		
		$("#selectFornecedorSelecionado_option_editor option",this.workspace).each(function (index) {
			 fornecedores.push($(this).val());
		});
		
		var param = {numeroCota: cotaEspecifica, desconto: descontoEditor};		
		param = serializeArrayToPost('fornecedores', fornecedores, param);
		
		$.postJSON(contextPath +"/financeiro/tipoDescontoCota/novoDescontoEditor", param,				   
				function(result) {
			           
						 if (result.tipoMensagem && result.tipoMensagem != "SUCCESS" && result.listaMensagens) {			      
							   exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "idModalDescontoEditor");
					       }
						   else{
							   tipoDescontoController.fecharDialogs();
							   exibirMensagem(result.tipoMensagem, result.listaMensagens, "");
							   $(".tiposDescEditorGrid", this.workspace).flexReload();
						   }
	               },
				   null,
				   true,"idModalDescontoEditor");
		
		$(".tiposDescEditorGrid",this.workspace).flexReload();
	},
	
	pesquisarCotaSuccessCallBack:function(){
		
		var cotaEspecifica = $("#numCotaEditor",this.workspace).val();
		
		descontoEditorController.carregarFornecedoresCota("#selectFornecedor_option_editor", cotaEspecifica);
		
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
					
					$("#selectFornecedor_option_editor",this.workspace).clear();
					
				},true,"idModalDescontoEditor"
		);
	},
	
	pesquisarCotaErrorCallBack:function(){
		
		exibirMensagemDialog("WARNING", [' Cota não encontrada!'], "idModalDescontoEditor");
		
		$("#selectFornecedorSelecionado_option_editor",this.workspace).clear();
		$("#selectFornecedor_option_editor",this.workspace).clear();
	},
	
	init: function() {
		
		$("select[name='selectFornecedorSelecionado_editor']",this.workspace).multiSelect("select[name='selectFornecedor_editor']", {trigger: "#linkFornecedorVoltarTodos_editor"});
		
		$("select[name='selectFornecedor_editor']",this.workspace).multiSelect("select[name='selectFornecedorSelecionado_editor']", {trigger: "#linkFornecedorEnviarTodos_editor"});
		
		$("#descontoEditor", this.workspace).justPercent();
		
		$(".tiposDescEditorGrid", this.workspace).flexigrid({
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