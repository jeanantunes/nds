var descontoEditorController = $.extend(true,{
		
	pesquisaCota: null,
	
	popup_editor: function() {		
		 
		$("#selectFornecedorSelecionado_option_editor", this.workspace).clear();
		$("#selectFornecedor_option_editor", this.workspace).clear();
		
		$("#codigoEditor", this.workspace).val("");
		$("#descontoEditor", this.workspace).val("");
		$("#descontoEditor", this.workspace).justPercent("floatValue");
		$("#descricaoEditor", this.workspace).val("");
		
		$( "#dialog-editor", this.workspace).dialog({
			resizable: false,
			height:400,
			width:560,
			modal: true,
			buttons: [{
						id:"id_confirmar_editor", text:"Confirmar",
						click: function() {
							
							var mensagens = [];
							if($("#codigoEditor", this.workspace).val()  == '') {
								
								mensagens.push('Valor incorreto para o campo Editor!');
							}
							if($("#descontoEditor", this.workspace).val() == '' || parseInt($("#descontoEditor", this.workspace).val()) <= 0) {
								
								mensagens.push('Valor incorreto para o campo Desconto do Editor!');
							} 
							if($('input[type="radio"][name="radioCotasEditor"]:checked').val() == undefined) {
								
								mensagens.push('Valor incorreto para o campo Todas / Específica!');
							}
							
							if(mensagens.length > 0) {
								
								exibirMensagem("WARNING", mensagens, "");
								return false;
							}
							
							descontoEditorController.novoDescontoEditor();
							$( this ).dialog( "close" );
						}
				},{
					id:"id_close_editor", text:"Cancelar",
					click: function() {
						descontoEditorController.clearModalDescontoEditor();
						$( this ).dialog( "close" );
					}
				}
			],
			form: $("#dialog-editor", this.workspace).parents("form")
		});		      
	},
	
	clearModalDescontoEditor: function() {
		
		$.each($('.trCotasEditor input[id^="cotaEditorInput"]', this.workspace), function(k, v) {

			if(($(v).attr('id') != 'cotaEditorInput1')) {
				
				$('#trCotaEditor'+ $(v).attr('id').substring("cotaEditorInput".length)).remove();
			}
		});
		
		$("#cotaEditorInput1", this.workspace).val('');
		$("#nomeCotaEditorInput1", this.workspace).val('');
		
	},
	
	novoDescontoEditor: function() {
		
		var data = descontoEditorController.obterParametrosNovoDescontoEditor();

		$.postJSON(contextPath +"/financeiro/tipoDescontoCota/novoDescontoEditor",
				   data,
				   function(result) {

					   $( "#dialog-editor",this.workspace ).dialog( "close" );

					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
					   
					   $("#radioEditorCotasEspecificas", this.workspace).prop('checked', false);
					   $("#radioEditorTodasCotas", this.workspace).prop('checked', false);
					   
					   tipoDescontoController.pesquisarDescontoEditor();
	               },
				   null,
				   true);
		
		descontoEditorController.clearModalDescontoEditor();
		
	},
	
	obterParametrosNovoDescontoEditor: function() {
		
		var data = new Array();
		
		var codigoEditor = $("#codigoEditor", this.workspace).val();
		var descontoEditor = $("#descontoEditor", this.workspace).justPercent("stringValue");
		var hasCotaEspecifica = document.getElementById("radioEditorCotasEspecificas", this.workspace).checked;
		var isTodasCotas = document.getElementById("radioEditorTodasCotas", this.workspace).checked;
		
		data.push({name:'descontoDTO.codigoEditor' , value: codigoEditor});
		data.push({name:'descontoDTO.valorDesconto' , value: descontoEditor});
		data.push({name:'descontoDTO.hasCotaEspecifica' , value: hasCotaEspecifica});
		data.push({name:'descontoDTO.isTodasCotas' , value: isTodasCotas});
		
		$("input[id^=cotaEditorInput]", this.workspace).each(function(index, value) {
			if ($(this).val()) {
				data.push({name:'descontoDTO.cotas' , value: $(this).val()});
			}
		});
		
		return data;
	},
	
	pesquisarEditorSuccessCallBack:function() {
		
	},

	carregarCotas: function(idComboCotas, numeroEditor) {
		
		$.postJSON(contextPath + "/cadastro/cota/obterCotas",
				[{name:"numeroEditor",value:numeroEditor}], 
				function(result) {
					
					if(result) {
						var comboClassificacao = montarComboBox(result, false);
						
						$(idComboFornecedores, this.workspace).html(comboClassificacao);
					}
				},function(result){
					
					$("#selectFornecedor_option_editor", this.workspace).clear();
					
				},true,"idModalDescontoEditor"
		);
	},
	
	pesquisarEditorErrorCallBack: function() {
		
		exibirMensagemDialog("WARNING", [' Editor não encontrado!'], "idModalDescontoEditor");
		
		$("#selectCotaSelecionado_option_editor", this.workspace).clear();
		$("#selectCota_option_editor", this.workspace).clear();
	},
	
	mostrarGridCota:function(){
		$('.especificaCota', this.workspace).show();
	},

	esconderGridCota: function() {
		
		$('.especificaCota', this.workspace).hide();
		
		descontoEditorController.resetGridCota();
	},
	
	resetGridCota: function() {
		
		$.each($('.trCotasEditor input[id^="cotaEditorInput"]', this.workspace), function(k, v) {

			if(($(v).attr('id') != 'cotaEditorInput1')) {
				
				$('#trCotaEditor'+ $(v).attr('id').substring("cotaEditorInput".length)).remove();
			}
		});
		
		$("#cotaEditorInput1", this.workspace).val('');
		$("#nomeCotaEditorInput1", this.workspace).val('');
	},
	
	adicionarLinhaCota: function(linhaAtual) {
		
		var cotaRepetida = false;
		$.each($('.trCotasEditor input[id^="cotaEditorInput"]', this.workspace), function(k, v) {

			if(($(v).attr('id') != 'cotaEditorInput'+ linhaAtual) && ($(v).val() == $('#cotaEditorInput'+ linhaAtual).val())) {
				
				exibirMensagemDialog("WARNING", ['Já existe essa Cota na lista!'], "idModalDescontoEditor");
				cotaRepetida = true;
				return false;
			}
		});
		
		if(cotaRepetida) {
			
			$("#cotaEditorInput"+ linhaAtual, this.workspace).val('');
			$("#nomeCotaEditorInput"+ linhaAtual, this.workspace).val('');
			
			$("#cotaEditorInput"+ linhaAtual, this.workspace).focus();
			
			$("#cotaEditorInput"+ linhaAtual, this.workspace).numeric();
			
			return false;
		}
		
		if ($('#trCotaEditor' + (linhaAtual + 1),this.workspace).length == 0 && $('#cotaEditorInput' + (linhaAtual),this.workspace).val() != "") {
			
			var tr = $('<tr class="trCotasEditor" id="trCotaEditor'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual + 1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
					'<td><input type="text" name="cotaEditorInput" maxlength="255" id="cotaEditorInput'+ (linhaAtual + 1) +'" onblur="descontoEditorController.pesquisaCota.pesquisarPorNumeroCota(cotaEditorInput'+ (linhaAtual + 1) +', nomeCotaEditorInput'+ (linhaAtual + 1) +', true,function(){descontoEditorController.adicionarLinhaCota('+ (linhaAtual + 1) +')});" style="width:120px;" /></td>' +
					'<td>'+
						 '<input type="text" name="nomeCotaEditorInput" maxlength="255" id="nomeCotaEditorInput'+ (linhaAtual + 1) +'" style="width:245px;" '+
							 ' onkeyup="descontoEditorController.pesquisaCota.autoCompletarPorNome(nomeCotaEditorInput'+ (linhaAtual + 1) +');" ' +
							 ' onblur="descontoEditorController.pesquisaCota.pesquisarPorNomeCota(cotaEditorInput'+ (linhaAtual + 1) +', nomeCotaEditorInput'+ (linhaAtual + 1) +', true, function(){descontoEditorController.adicionarLinhaCota('+ (linhaAtual + 1) +');});" ' +
						 '/>'+
					'</td>' +
					'</tr>'
			);
			
			$("#gridCotasEditor",this.workspace).append(tr);
			
			$("#cotaEditorInput" + (linhaAtual + 1),this.workspace).focus();
			
			$("#cotaEditorInput"+ (linhaAtual + 1),this.workspace).numeric();
		}
	},
	
	init: function(pesquisaCota) {
		
		this.pesquisaCota = pesquisaCota;
		
		$("select[name='selectCotaSelecionado_editor']",this.workspace).multiSelect("select[name='selectCota_editor']", {trigger: "#linkCotaVoltarTodos_editor"});
		
		$("select[name='selectCota_editor']",this.workspace).multiSelect("select[name='selectCotaSelecionado_editor']", {trigger: "#linkCotaEnviarTodos_editor"});
		
		$("#descontoEditor", this.workspace).justPercent();
		
		$(".tiposDescEditorGrid", this.workspace).flexigrid({
			preProcess: tipoDescontoController.executarPreProcessamento,
			onSuccess: function(){bloquearItensEdicao(tipoDescontoController.workspace);},
			dataType : 'json',
			colModel : [ {
				display : 'Editor',
				name : 'codigoEditor',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeEditor',
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
				display : 'Cota(s)',
				name : 'qtdCotas',
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
			sortname : "nomeEditor",
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