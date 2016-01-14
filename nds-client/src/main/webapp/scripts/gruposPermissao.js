var gruposPermissaoController = $.extend(true, {
		path : "",
		init : function(contextPath) {
			this.path = contextPath + "/administracao/gruposAcesso";
						
			$("#tabs-acesso" , gruposPermissaoController.workspace).tabs({height:400});
			
			
			this.initGruposGrid();
			this.mostrarGrupo();
			$(".permissaoGrid", gruposPermissaoController.workspace).flexOptions({
				url : contextPath + "/administracao/gruposAcesso/obterPermissoes",
			}).flexReload();
			
		},
		popup_novo_grupo : function() {
			
			$('input[tipo="permissao"]').attr("checked", false);
			$('#permissaoGridConteudo').appendTo($('#localPermissaoGridGrupo'));

			$("#grupoPermissaonome", gruposPermissaoController.workspace).val("");
			$("#grupoPermissaoId", gruposPermissaoController.workspace).val("");
			

			
			this.popup_grupo();
		},
		popup_grupo : function() {
			$( "#dialog-novo-grupo", gruposPermissaoController.workspace).dialog({
				resizable: false,
				height:470,
				width:680,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						var self = this;
						
						var params = [];
						
						params.push({'name' : 'grupoPermissaoDTO.id', 'value' : $("#grupoPermissaoId", gruposPermissaoController.workspace).val()});
						
						params.push({'name' : 'grupoPermissaoDTO.nome', 'value' : $('#grupoPermissaonome', gruposPermissaoController.workspace).val()});
						
						var checkSelecionados = $('.permissao:checked');
							
						if( checkSelecionados.length <= 0 ) {
							
							$( "#ok-falta-de-permissoes", gruposPermissaoController.workspace ).dialog({
								resizable: false,
								height:170,
								width:380,
								modal: true,
								buttons: {
									"Ok": function() {
										$( this ).dialog( "close" );
									}
								}
							});
						} else {
							$.each(checkSelecionados, function(index, elemento) {
								params.push({'name':'grupoPermissaoDTO.permissoes['+index+']', value : elemento.getAttribute('role')});
							});
																			
							$.postJSON(gruposPermissaoController.path + '/salvarGrupoPermissao', params, function(data) {
															
								$( self ).dialog("close");
								$(".gruposGrid", gruposPermissaoController.workspace).flexReload();

							}, null, true);
						}
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-novo-grupo", gruposPermissaoController.workspace).parents("form")
			});
		},
	
		popup_excluir_grupo : function(codigoGrupo) {
			$( "#dialog-excluir_grupo", gruposPermissaoController.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						var self = this;
						$.getJSON(
								gruposPermissaoController.path + "/excluirGrupoPermissao",
								{codigoGrupo:codigoGrupo}, 
								function(result) {

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;

									if(tipoMensagem && listaMensagens) {
										exibirMensagemDialog(tipoMensagem, listaMensagens, "dialog-excluir_grupo");
									}

									$( self ).dialog( "close" );
									$("#effect").show("highlight", {}, 1000, callback);
									$(".gruposGrid", gruposPermissaoController.workspace).flexReload();

								}
							);

						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-excluir_grupo", gruposPermissaoController.workspace).parents("form")
			});
		},
		mostrarGrupo : function() {
			var serializedObj = $("#pesquisar_grupos_form", gruposPermissaoController.workspace).serialize();
			$(".gruposGrid", gruposPermissaoController.workspace).flexOptions({
				"url" : gruposPermissaoController.path + '/pesquisarGrupos?' + serializedObj,
				method: 'GET',
				newp:1,
				onSuccess: function() {bloquearItensEdicao(gruposPermissaoController.workspace.workspace);}
			});
			$(".gruposGrid", gruposPermissaoController.workspace).flexReload();
		},
		popup_editar_grupo : function(idGrupo) {
			
			$('input[tipo="permissao"]').attr("checked", false);
			$('#permissaoGridConteudo').appendTo($('#localPermissaoGridGrupo'));

			$(".permissaoGrid", gruposPermissaoController.workspace).flexOptions({
				url : contextPath + "/administracao/gruposAcesso/obterPermissoes",
				onSuccess: function() {
					$.getJSON(
						contextPath + "/administracao/gruposAcesso/editarGrupoPermissao",
						{codigoGrupo:idGrupo}, 
						function(result) {
							if (result) {
								
								$("#grupoPermissaonome", gruposPermissaoController.workspace).val(result.nome);
								$("#grupoPermissaoId", gruposPermissaoController.workspace).val(result.id);
								
								$.each(result.permissoes, function(index, role) {
									$('input[tipo="permissao"][role="'+role+'"][isPai="false"]').attr("checked", true);
								});							
								
								gruposPermissaoController.popup_grupo();
							}
						}
					);
				}
			}).flexReload();

		},
		initGruposGrid : function() {
			$(".gruposGrid", gruposPermissaoController.workspace).flexigrid({
				preProcess: function(data) {
					if( typeof data.mensagens == "object") {
						exibirMensagemDialog(data.mensagens.tipoMensagem, data.mensagens.listaMensagens, "dialog-novo-grupo");
						$(".gridsGrupos", gruposPermissaoController.workspace).hide();
					} else {
						$.each(data.rows , function(index, value) {

							var linkEditarGrupo = '<a isEdicao="true" href="javascript:;" onclick="gruposPermissaoController.popup_editar_grupo(\'' + value.cell.id + '\');" style="cursor:pointer; margin-right:10px;">' +
				     	  	'<img title="Editar Grupo" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
				  		    '</a>';

							var linkExcluirGrupo = '<a isEdicao="true" href="javascript:;" onclick="gruposPermissaoController.popup_excluir_grupo(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Excluir Grupo" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
				  		    '</a>';
							
							value.cell[1] = linkEditarGrupo + linkExcluirGrupo;

						});
						$(".gridsGrupos", gruposPermissaoController.workspace).show();
					}
					return data;
				},
				dataType : 'json',
				colModel : [ {
					display : 'Nome',
					name : 'nome',
					width : 823,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 50,
					sortable : false,
					align : 'center'
				}],
				sortname : "nome",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 900,
				height : 'auto'
			});
			
			$(".permissaoGrid", gruposPermissaoController.workspace).flexigrid({
				preProcess : gruposPermissaoController.preProcessPermissao,
				dataType : 'json',
				colModel : [ {
					display : 'Regra',
					name : 'descricao',
					width : 443,
					sortable : false,
					align : 'left'
				}, {
					display : 'Visualização',
					name : 'visualizacao',
					width : 70,
					sortable : false,
					align : 'center'
				}, {
					display : 'Alteração',
					name : 'alteracao',
					width : 70,
					sortable : false,
					align : 'center'
				}],
				width : 640,
				height : 260
			});
		},
		
		preProcessPermissao : function(data) {

			$.each(data.rows, function(indice, linha) { 
				
				if(linha.cell.pai)
					linha.cell.descricao = '<div style="margin-left:20px;padding: 0px">' + linha.cell.descricao + '</div>'; 
				else	
					linha.cell.descricao = '<div style="font-weight: bolder;padding: 0px">' + linha.cell.descricao + '</div>'; 
				
				if (linha.cell.observacao){
					
					linha.cell.descricao = '<span><a title="' + linha.cell.observacao + '"/>'+
						linha.cell.descricao
						+'</span>';
				}
				
				var alteracao = linha.cell.alteracao;
				
				var visualizacao = linha.cell.visualizacao;
								
				if(visualizacao)
					linha.cell.visualizacao = gruposPermissaoController.getInput(false, visualizacao, linha.cell.pai, alteracao, linha.cell.habilitado);
				else
					linha.cell.visualizacao = '';
				
				if(alteracao)
					linha.cell.alteracao = gruposPermissaoController.getInput(true, visualizacao, linha.cell.pai, alteracao, linha.cell.habilitado);				
				else
					linha.cell.alteracao = ''; 
			});
			
			return data;
		},
		
		getInput : function(isAlteracao, permissao, pai, alteracao, habilitado) {
						 
			var isPai = !pai ? true : false;
			
			var onchange = '';
			var disabled = '';
			
			if (isPai)
				onchange = 'gruposPermissaoController.paiSelecionado(this)';
			else if(isAlteracao)
				onchange = 'gruposPermissaoController.alteracaoSelecionada(this)';
			else 
				onchange = 'gruposPermissaoController.visualizacaoSelecionada(this)';
			
			if(!pai)
				pai='';
			
			if(!permissao)
				permissao='';
			
			if(!alteracao)
				alteracao='';
			
			if (!habilitado)
				disabled = 'disabled="disabled"';
			
			var role = isAlteracao ? alteracao : permissao;
						
			return '<input class="permissao" tipo="permissao" type="checkbox" onchange="'+onchange+'" role="'+role+'" permissao="'+permissao
						+'"  alteracao="'+alteracao+'" pai="'+pai+'" isAlteracao="'+isAlteracao+'" isPai="'+isPai+ '"' +  disabled + '/>';
		},
		
		paiSelecionado : function(elemento) {
			
			var rolePai = elemento.getAttribute('permissao');
			
			var isAlteracao = (elemento.getAttribute('isAlteracao') === 'true');
			
			var filhos = $('input[pai="'+rolePai+'"][isAlteracao="'+isAlteracao+'"]' );
			
			filhos.attr('checked',elemento.checked);
		
			if(isAlteracao===true && elemento.checked===true) {
				var filhosVisualizacao = $('input[pai="'+rolePai+'"][isAlteracao="false"]' );
				filhosVisualizacao.attr('checked',true);
				
				var paiVisualizacao = $('input[permissao="'+rolePai+'"][isAlteracao="false"]' );
				paiVisualizacao.attr('checked',true);
			}
			
			if(isAlteracao===false && elemento.checked===false) {
				
				var filhosAlteracao = $('input[pai="'+rolePai+'"][isAlteracao="true"]' );
				filhosAlteracao.attr('checked',false);
				
				var paiAlteracao = $('input[permissao="'+rolePai+'"][isAlteracao="true"]' );
				paiAlteracao.attr('checked',false);
			}
				
		},
				
		alteracaoSelecionada : function(elemento) {
			var rolePai = elemento.getAttribute('pai');
			
			var elementoPai = $('input[permissao="'+rolePai+'"][isAlteracao="true"]').get(0);
			
			elementoPai.checked=false;
			
			if(elemento.checked===true) {
				
				var visualizacao = elemento.getAttribute('permissao');
				
				var elementoVisualizacao = $('input[permissao="'+visualizacao+'"][isAlteracao="false"]').get(0);
				
				elementoVisualizacao.checked=true;
			}
		},
		
		visualizacaoSelecionada : function(elemento) {
						
			var rolePai = elemento.getAttribute('pai');
						
			$('input[permissao="'+rolePai+'"]').attr('checked', false);
			
			if(elemento.checked===false) {			
				
				var visualizacao = elemento.getAttribute('permissao');
				
				var elementoAlteracao = $('input[permissao="'+visualizacao+'"][isAlteracao="true"]').get(0);
				
				elementoAlteracao.checked=false;
			}
		}
		
}, BaseController);

//@ sourceURL=gruposPermissao.js