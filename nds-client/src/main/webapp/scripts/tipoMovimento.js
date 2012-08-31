function TipoMovimento(pathTela,obj, workspace) {
	
	var _workspace = workspace;
	
	var T = this;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
	
	this.init = function() {

		$(".movimentosGrid", _workspace).flexigrid($.extend({},{
			colModel : [ {
					display : 'Código',
					name : 'codigo',
					width : 95,
					sortable : true,
					align : 'left'
				}, {
					display : 'Descrição',
					name : 'descricao',
					width : 300,
					sortable : true,
					align : 'left'
				}, {
					display : 'Grupo de Operação',
					name : 'grupoOperacao',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Operação',
					name : 'operacao',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Aprovação',
					name : 'aprovacao',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Incide na Dívida',
					name : 'incideDivida',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto'
		})); 	
		
		$(".grids", _workspace).show();	

		$("#codigo").numeric();

	},
	
	this.popup = function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo", _workspace ).dialog({
			resizable: false,
			height:280,
			width:460,
			modal: true,
			buttons: {
				"Confirmar": function() {
					TM.salvarTipoMovimento();					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-novo", this.workspace).parents("form")
		});
	},
	
	this.popup_alterar = function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo", _workspace ).dialog({
			resizable: false,
			height:280,
			width:460,
			modal: true,
			buttons: {
				"Confirmar": function() {
					TM.alterarTipoMovimento();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-novo", this.workspace).parents("form")
		});	
		      
	},
	
	this.popup_excluir = function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir", _workspace ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					TM.excluirTipoMovimento();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-excluir", this.workspace).parents("form")
		});
	},

	this.cliquePesquisar = function() {
		
		var data = [];
		
		data.push({name:'filtro.codigo',		value: T.get("codigo")});
		data.push({name:'filtro.descricao',		value: T.get("descricao")});
		
		$(".movimentosGrid", _workspace).flexOptions({			
			url : pathTela + "/tipoMovimento/pesquisarTipoMovimento",
			dataType : 'json',
			preProcess: T.processaRetornoPesquisa,
			params:data
		});
		
		$(".movimentosGrid", _workspace).flexReload();
	},
	
	this.processaRetornoPesquisa = function(result) {
		
		T.tiposMovimento = [];
		
		$.each(result.rows, function(index,row){T.processarLinha(index,row.cell);} );
		
		return result;
	},
	
	this.processarLinha = function(index,cell) {
		
		T.tiposMovimento.push(cell);
		
		var onClickAlteracao = cell.permiteAlteracao == false ? '' : (obj +'.carregarAlteracao(\''+ index + '\')');
		var onClickExclusao =  cell.permiteAlteracao == false ? '' : (obj +'.carregarExclusao(\''+ index + '\')');
		var styleOpacity = cell.permiteAlteracao == false 	  ? ('style="opacity: 0.5"') : '';
		
		cell.acao = 			
			'<a href="javascript:;" onclick="'+ onClickAlteracao +'" style="margin-right:10px;">' +
			'<img src="'+pathTela+'/images/ico_editar.gif" border="0" hspace="5" ' + styleOpacity + ' />' +
			'</a>'+
			'<a href="javascript:;" onclick="' + onClickExclusao + '">' +
			'<img src="'+pathTela+'/images/ico_excluir.gif" hspace="5" border="0" ' + styleOpacity + ' />' +
			'</a>';
	},
	
	this.carregarAlteracao = function(index) {
		
		T.tipoMovimento = T.tiposMovimento[index];
		
		T.preencherModal(T.tipoMovimento.codigo, 
						 T.tipoMovimento.descricao, 
						 T.tipoMovimento.grupoOperacaoValue, 
						 T.tipoMovimento.operacaoValue, 
						 T.tipoMovimento.aprovacaoValue, 
						 T.tipoMovimento.incideDividaValue);
		
		popup_alterar();
		
	},
		
	this.carregarNovo = function() {
		
		T.preencherModal(null,'','FINANCEIRO','CREDITO','SIM','SIM');
		
		popup();
	},
	
	this.excluirTipoMovimento = function() {
		
		var data = [];		
		data.push({name:'codigo',		value: T.tipoMovimento.codigo});

		$.postJSON(contextPath + "/tipoMovimento/excluirTipoMovimento",
				data,
				function(result){										
					$(".movimentosGrid", _workspace).flexReload();
					exibirMensagem('SUCCESS', ['Tipo de Movimento excluido com sucesso.']);										
				});		
		
		$( "#dialog-excluir", _workspace ).dialog( "close" );
	},
	
	this.salvarTipoMovimento = function() {
		
		var data = [];		
		data.push({name:'tipoMovimentoDTO.codigo',		value: T.get('codigoModal')});
		data.push({name:'tipoMovimentoDTO.descricao',		value: T.get('descricaoModal')});
		data.push({name:'tipoMovimentoDTO.grupoOperacaoValue',		value: T.get('grupoOperacaoModal')});
		data.push({name:'tipoMovimentoDTO.operacaoValue',		value: T.get('operacaoModal')});
		data.push({name:'tipoMovimentoDTO.aprovacaoValue',		value: T.get('aprovacaoModal')});
		data.push({name:'tipoMovimentoDTO.incideDividaValue',		value: T.get('incideDividaModal')});
		
		$.postJSON(contextPath + "/tipoMovimento/salvarTipoMovimento",
				data,
				function(result){
					$( "#dialog-novo", _workspace ).dialog( "close" );					
					$(".movimentosGrid", _workspace).flexReload();
					exibirMensagem('SUCCESS', ['Tipo de Movimento salvo com sucesso.']);										
				},	
				null, 
				true,
				"dialog-novo");		
		
	},
	
	this.alterarTipoMovimento = function() {
		
		var data = [];		
		data.push({name:'tipoMovimentoDTO.codigo',		value: T.get('codigoModal')});
		data.push({name:'tipoMovimentoDTO.descricao',		value: T.get('descricaoModal')});
		data.push({name:'tipoMovimentoDTO.grupoOperacaoValue',		value: T.get('grupoOperacaoModal')});
		data.push({name:'tipoMovimentoDTO.operacaoValue',		value: T.get('operacaoModal')});
		data.push({name:'tipoMovimentoDTO.aprovacaoValue',		value: T.get('aprovacaoModal')});
		data.push({name:'tipoMovimentoDTO.incideDividaValue',		value: T.get('incideDividaModal')});
		
		$.postJSON(contextPath + "/tipoMovimento/alterarTipoMovimento",
				data,
				function(result){		
					$( "#dialog-novo", _workspace ).dialog( "close" );
					$(".movimentosGrid", _workspace).flexReload();
					exibirMensagem('SUCCESS', ['Tipo de Movimento alterado com sucesso.']);										
				},
				null, 
				true,
				"dialog-novo");		
	},
	
	this.preencherModal = function(codigo, descricao, grupoOperacao, operacao, aprovacao, incideDivida) {
		
		if(codigo!=null) {
			$('#grupoOperacaoModal', _workspace).disable();
		} else {
			$('#grupoOperacaoModal', _workspace).enable();
		}
		
		T.atualizarCombosPorGrupoOperacao(grupoOperacao);
		
		T.set('codigoModal', codigo);
		T.set('descricaoModal', descricao);
		T.set('grupoOperacaoModal', grupoOperacao);
		T.set('operacaoModal', operacao);
		T.set('aprovacaoModal', aprovacao);
		T.set('incideDividaModal', incideDivida == ('NAO_SE_APLICA') ? 'SIM' : incideDivida);
		
	},
	
	this.atualizarCombosPorGrupoOperacao = function(grupoOperacao) {
		
		var opcoes = [];
				
		if(grupoOperacao == 'FINANCEIRO') {
			opcoes.push({value:{$:'Crédito'},key:{$:'CREDITO'}});
			opcoes.push({value:{$:'Débito'},key:{$:'DEBITO'}});
			
			T.set('incideDividaModal','SIM');
			$('#incideDividaModal', _workspace).disable();
			
		} else {
			opcoes.push({value:{$:'Entrada'},key:{$:'ENTRADA'}});
			opcoes.push({value:{$:'Saída'},key:{$:'SAIDA'}});
			
			$('#incideDividaModal', _workspace).enable();
		}
		
		var combo =  montarComboBox(opcoes, false);
		$("#operacaoModal", _workspace).html(combo);
	},
		
	this.carregarExclusao = function(index) {
		T.tipoMovimento = T.tiposMovimento[index];
		T.popup_excluir();
	},
	
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	this.set = function(campo,value) {
				
		var elemento = $("#" + campo, _workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	this.get = function(campo) {
		
		var elemento = $("#" + campo, _workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	};
	
}
