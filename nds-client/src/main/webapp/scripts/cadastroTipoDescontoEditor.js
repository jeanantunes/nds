var descontoEditorController = $.extend(true,{
		
	popup_editor:function() {		
		 
		$("#selectFornecedorSelecionado_option_editor",this.workspace).clear();
		$("#selectFornecedor_option_editor",this.workspace).clear();
		
		$("#numEditor",this.workspace).val("");
		$("#descontoEditor",this.workspace).val("");
		$("#descontoEditor",this.workspace).justPercent("floatValue");
		$("#descricaoEditor",this.workspace).val("");
		
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
		
		var numEditor = $("#numEditor", this.workspace).val();
		var descontoEditor = $("#descontoEditor", this.workspace).justPercent("stringValue");
		
		var fornecedores = new Array();
		
		$("#selectFornecedorSelecionado_option_editor option",this.workspace).each(function (index) {
			 fornecedores.push($(this).val());
		});
		
		var param = {numeroEditor: numEditor, desconto: descontoEditor};		
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
	
	pesquisarEditorSuccessCallBack:function() {
		
	},

	carregarCotas: function(idComboCotas, numeroEditor) {
		
		$.postJSON(contextPath + "/cadastro/cota/obterCotas",
				[{name:"numeroEditor",value:numeroEditor}], 
				function(result) {
					
					if(result) {
						var comboClassificacao = montarComboBox(result, false);
						
						$(idComboFornecedores,this.workspace).html(comboClassificacao);
					}
				},function(result){
					
					$("#selectFornecedor_option_editor",this.workspace).clear();
					
				},true,"idModalDescontoEditor"
		);
	},
	
	pesquisarEditorErrorCallBack: function() {
		
		exibirMensagemDialog("WARNING", [' Editor não encontrado!'], "idModalDescontoEditor");
		
		$("#selectCotaSelecionado_option_editor", this.workspace).clear();
		$("#selectCota_option_editor", this.workspace).clear();
	},
	
	mostrarGridCota:function(){
		$('.especificaCota',this.workspace).show();
	},

	esconderGridCota:function(){
		
		$('.especificaCota',this.workspace).hide();
		
		descontoProdutoController.resetGridCota();
	},
	
	init: function() {
		
		$("select[name='selectCotaSelecionado_editor']",this.workspace).multiSelect("select[name='selectCota_editor']", {trigger: "#linkCotaVoltarTodos_editor"});
		
		$("select[name='selectCota_editor']",this.workspace).multiSelect("select[name='selectCotaSelecionado_editor']", {trigger: "#linkCotaEnviarTodos_editor"});
		
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
//@ sourceURL=cadastroTipoDescontoEditor.js