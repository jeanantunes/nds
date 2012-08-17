var gruposPermissaoController = $.extend(true, {
		path : "",
		init : function(contextPath) {
			this.path = contextPath + "/administracao/gruposAcesso";
			$( "#tabs-grupos" , gruposPermissaoController.workspace).tabs();
			this.initGruposGrid();
			this.mostrarGrupo();
		},
		popup_novo_grupo : function() {
			$("#permissoesGrupo option", gruposPermissaoController.workspace).remove();
			$("#permissoesGrupoSelecionadas option", gruposPermissaoController.workspace).remove();

			$("#grupoPermissaonome", gruposPermissaoController.workspace).val("");
			$("#grupoPermissaoId", gruposPermissaoController.workspace).val("");

			$.getJSON(
					this.path + "/novoGrupoPermissao",
					null, 
					function(result) {
						if (result) {
							$(result.permissoes).each(function() {
								$("#permissoesGrupo", gruposPermissaoController.workspace).append($("<option/>", {value: $(this)[0].toString(),
																			 			   text: $(this)[0].toString() 
																			 			  }));
							});
						}
					}
				);
			
			this.popup_grupo();
		},
		popup_grupo : function() {
			$( "#dialog-novo-grupo", gruposPermissaoController.workspace).dialog({
				resizable: false,
				height:400,
				width:650,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						var obj = $("#novo_grupo_form", gruposPermissaoController.workspace).serialize();
						//var obj = $("#novo_grupo_form", gruposPermissaoController.workspace).serialize();
						
						var permissoes = "";
						$("#permissoesGrupoSelecionadas option", gruposPermissaoController.workspace).each(function() {
							if (permissoes!="") {
								permissoes += ",";
							}
							permissoes += $(this).val();
					    });

						obj += "&grupoPermissaoDTO.permissoesSelecionadas=" + permissoes;
						
						$.postJSON(gruposPermissaoController.path + '/salvarGrupoPermissao', obj, function(data) {
							var tipoMensagem = data.tipoMensagem;
							var listaMensagens = data.listaMensagens;

							if(tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}

							$("#effect").show("highlight", {}, 1000, callback);
							$(".gruposGrid", gruposPermissaoController.workspace).flexReload();

						}, null, true);

						$( this ).dialog("close");

					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-novo-grupo", gruposPermissaoController.workspace).parents("form")
			});
		},
		adicionaGruposSelecionados : function() {
			$('#permissoesGrupo', gruposPermissaoController.workspace).find(":selected").each(function() {
				$("#permissoesGrupoSelecionadas", gruposPermissaoController.workspace).append($("<option/>", {value: $(this).val(),
		 			   text: $(this).val() 
		 			  }));
			});
			$("#permissoesGrupo option:selected", gruposPermissaoController.workspace).remove();
		},
		removeGruposSelecionados : function() {
			$('#permissoesGrupoSelecionadas', gruposPermissaoController.workspace).find(":selected").each(function() {
				$("#permissoesGrupo", gruposPermissaoController.workspace).append($("<option/>", {value: $(this).val(),
		 			   text: $(this).val() 
		 			  }));
			});
			$("#permissoesGrupoSelecionadas option:selected", gruposPermissaoController.workspace).remove();
		},
		popup_excluir_grupo : function(codigoGrupo) {
			$( "#dialog-excluir_grupo", gruposPermissaoController.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$.getJSON(
								gruposPermissaoController.path + "/excluirGrupoPermissao",
								"codigoGrupo=" + codigoGrupo, 
								function(result) {

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;

									if(tipoMensagem && listaMensagens) {
										exibirMensagemDialog(tipoMensagem, listaMensagens, "dialog-excluir_grupo");
									}

									$("#effect").show("highlight", {}, 1000, callback);
									$(".gruposGrid", gruposPermissaoController.workspace).flexReload();

								}
							);

						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
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
				newp:1
			});
			$(".gruposGrid", gruposPermissaoController.workspace).flexReload();
		},
		popup_editar_grupo : function(idGrupo) {
			$("#permissoesGrupo option", gruposPermissaoController.workspace).remove();
			$("#permissoesGrupoSelecionadas option", gruposPermissaoController.workspace).remove();
			
			$.getJSON(
					this.path + "/editarGrupoPermissao",
					"codigoGrupo=" + idGrupo, 
					function(result) {
						if (result) {
							
							$("#grupoPermissaonome", gruposPermissaoController.workspace).val(result.nome);
							$("#grupoPermissaoId", gruposPermissaoController.workspace).val(result.id);
							
							$(result.permissoes).each(function() {
								$("#permissoesGrupo", gruposPermissaoController.workspace).append($("<option/>", {value: $(this)[0].toString(),
																			 			   text: $(this)[0].toString() 
																			 			  }));
							});

							$(result.permissoesSelecionadas).each(function() {
								$("#permissoesGrupoSelecionadas", gruposPermissaoController.workspace).append($("<option/>", {value: $(this)[0].toString(),
																			 			   text: $(this)[0].toString() 
																			 			  }));
							});

						}
					}
				);
			
			this.popup_grupo();
		},
		initGruposGrid : function() {
			$(".gruposGrid", gruposPermissaoController.workspace).flexigrid({
				preProcess: function(data) {
					if( typeof data.mensagens == "object") {
						exibirMensagemDialog(data.mensagens.tipoMensagem, data.mensagens.listaMensagens, "dialog-novo-grupo");
						$(".gridsGrupos", gruposPermissaoController.workspace).hide();
					} else {
						$.each(data.rows , function(index, value) {

							var linkEditarGrupo = '<a href="javascript:;" onclick="gruposPermissaoController.popup_editar_grupo(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Editar Grupo" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
				  		    '</a>';

							var linkExcluirGrupo = '<a href="javascript:;" onclick="gruposPermissaoController.popup_excluir_grupo(\'' + value.cell.id + '\');" style="cursor:pointer">' +
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
					width : 750,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 50,
					sortable : false,
					align : 'left'
				}],
				sortname : "nome",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 900,
				height : 255
			});
		}
}, BaseController);
