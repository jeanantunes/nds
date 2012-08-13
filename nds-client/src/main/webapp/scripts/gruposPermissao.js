var gruposPermissaoController = $.extend(true, {
		path : "",
		init : function(contextPath) {
			this.path = contextPath + "/administracao/gruposAcesso";
			$( "#tabs-grupos" , this.workspace).tabs();
			this.initGruposGrid();
			this.mostrarGrupo();
		},
		popup_novo_grupo : function() {
			$("#permissoesGrupo option", this.workspace).remove();
			$("#permissoesGrupoSelecionadas option", this.workspace).remove();

			$("#grupoPermissaonome", this.workspace).val("");
			$("#grupoPermissaoId", this.workspace).val("");

			$.getJSON(
					this.path + "/novoGrupoPermissao",
					null, 
					function(result) {
						if (result) {
							$(result.permissoes).each(function() {
								$("#permissoesGrupo", this.workspace).append($("<option/>", {value: $(this)[0].toString(),
																			 			   text: $(this)[0].toString() 
																			 			  }));
							});
						}
					}
				);
			
			this.popup_grupo();
		},
		popup_grupo : function() {
			$( "#dialog-novo-grupo", this.workspace).dialog({
				resizable: false,
				height:400,
				width:650,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						var obj = $("#novo_grupo_form", this.workspace).serialize();
						//var obj = $("#novo_grupo_form", this.workspace).serialize();
						
						var permissoes = "";
						$("#permissoesGrupoSelecionadas option", this.workspace).each(function() {
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
				}
			});
		},
		adicionaGruposSelecionados : function() {
			$('#permissoesGrupo', this.workspace).find(":selected").each(function() {
				$("#permissoesGrupoSelecionadas", this.workspace).append($("<option/>", {value: $(this).val(),
		 			   text: $(this).val() 
		 			  }));
			});
			$("#permissoesGrupo option:selected", this.workspace).remove();
		},
		removeGruposSelecionados : function() {
			$('#permissoesGrupoSelecionadas', this.workspace).find(":selected").each(function() {
				$("#permissoesGrupo", this.workspace).append($("<option/>", {value: $(this).val(),
		 			   text: $(this).val() 
		 			  }));
			});
			$("#permissoesGrupoSelecionadas option:selected", this.workspace).remove();
		},
		popup_excluir_grupo : function(codigoGrupo) {
			$( "#dialog-excluir_grupo", this.workspace ).dialog({
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
										exibirMensagem(tipoMensagem, listaMensagens);
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
				}
			});
		},
		mostrarGrupo : function() {
			var serializedObj = $("#pesquisar_grupos_form", this.workspace).serialize();
			$(".gruposGrid", this.workspace).flexOptions({
				"url" : gruposPermissaoController.path + '/pesquisarGrupos?' + serializedObj,
				method: 'GET',
				newp:1
			});
			$(".gruposGrid", this.workspace).flexReload();
		},
		popup_editar_grupo : function(idGrupo) {
			$("#permissoesGrupo option", this.workspace).remove();
			$("#permissoesGrupoSelecionadas option", this.workspace).remove();
			
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
			$(".gruposGrid", this.workspace).flexigrid({
				preProcess: function(data) {
					if( typeof data.mensagens == "object") {
						exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
						$(".gridsGrupos", this.workspace).hide();
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
						$(".gridsGrupos", this.workspace).show();
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
