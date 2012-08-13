var usuariosPermissaoController = $.extend(true, {
		path : "",
		init : function(contextPath) {
			this.path = contextPath + "/administracao/gruposAcesso";
			this.initUsuariosGrid();
			this.mostrarUsuario();
		},
		limpar_selecoes : function() {
			$("#gruposUsuario option", this.workspace).remove();
			$("#gruposSelecionadosUsuario option", this.workspace).remove();
			$("#permissoesUsuario option", this.workspace).remove();
			$("#permissoesSelecionadasUsuario option", this.workspace).remove();
			$("#usuarioCep").mask("99999-999");
		},
		popup_alterar_senha : function(idUsuario) {
			
			this.clearForm($("#alterar_senha_form", this.workspace));
			
			$.getJSON(
					this.path + "/editarUsuario",
					"codigoUsuario=" + idUsuario, 
					function(result) {
						if (result) {
							usuariosPermissaoController.bindData(result, $("#alterar_senha_form", usuariosPermissaoController.workspace));
						}
					}
				);
			
			$( "#dialog-alterar-senha" , this.workspace).dialog({
				resizable: false,
				height:220,
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						var obj = $("#alterar_senha_form", this.workspace).serialize();

						$.postJSON(usuariosPermissaoController.path + '/alterarSenha', obj, function(data) {
							var tipoMensagem = data.tipoMensagem;
							var listaMensagens = data.listaMensagens;

							if(tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}
							
							console.log(tipoMensagem);

							$("#effect").show("highlight", {}, 1000, callback);
							$(".usuariosGrid", usuariosPermissaoController.workspace).flexReload();

						}, null, true);

						$( this ).dialog("close");
					},
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		popup_novo_usuario : function() {

			$("#trInsercaoSenhas").show();
			$("#trLembreteSenha").show();
			
			this.limpar_selecoes();
			
			this.clearForm($("#novo_usuario_form", this.workspace));
			
			$('#usuarioAtivaTrue').attr('checked', true).button("refresh");								
			
			$.getJSON(
					this.path + "/novoUsuario",
					null, 
					function(result) {
						if (result) {
							$(result.usuarioDTO.grupos).each(function() {
								$("#gruposUsuario", this.workspace).append($("<option/>", {value: $(this)[0].id,
																			 			   text: $(this)[0].nome 
																			 			  }));
							});
							$(result.usuarioDTO.permissoes).each(function() {
								$("#permissoesUsuario", this.workspace).append($("<option/>", {value: $(this)[0].toString(),
																			 			   text: $(this)[0].toString() 
																			 			  }));
							});
						}
					}
				);
			
			this.popup_usuario();
		},
		popup_editar_usuario : function(idGrupo) {
			
			$("#trInsercaoSenhas").hide();
			$("#trLembreteSenha").hide();
			
			this.limpar_selecoes();
			
			$.getJSON(
					this.path + "/editarUsuario",
					"codigoUsuario=" + idGrupo, 
					function(result) {
						if (result) {
							
							usuariosPermissaoController.bindData(result, $("#novo_usuario_form", usuariosPermissaoController.workspace));
							
							if (result.usuarioDTO.contaAtiva == "ativa") {
								$('#usuarioAtivaTrue').attr('checked', true).button("refresh");								
							} else {
								$('#usuarioAtivaFalse').attr('checked', true).button("refresh");
							}
							
							$(result.usuarioDTO.grupos).each(function() {
								$("#gruposUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].id,
																			 			   text: $(this)[0].nome 
																			 			  }));
							});

							$(result.usuarioDTO.gruposSelecionadosList).each(function() {
								$("#gruposSelecionadosUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].id,
																			 			   text: $(this)[0].nome 
																			 			  }));
							});

							$(result.usuarioDTO.permissoes).each(function() {
								$("#permissoesUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].toString(),
																			 			   text: $(this)[0].toString() 
																			 			  }));
							});

							$(result.usuarioDTO.permissoesSelecionadasList).each(function() {
								$("#permissoesSelecionadasUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].toString(),
																			 			   text: $(this)[0].toString() 
																			 			  }));
							});

						}
					}
				);
			
			this.popup_usuario();
		},
		popup_usuario : function() {
			$( "#dialog-novo-usuario" , this.workspace).dialog({
				resizable: false,
				height:620,
				width:750,
				modal: true,
				buttons: {
					"Confirmar": function() {
						var obj = $("#novo_usuario_form", this.workspace).serialize();
						
						var permissoes = "";
						$("#permissoesSelecionadasUsuario option", this.workspace).each(function() {
							if (permissoes!="") {
								permissoes += ",";
							}
							permissoes += $(this).val();
					    });

						var grupos = "";
						$("#gruposSelecionadosUsuario option", this.workspace).each(function() {
							if (grupos!="") {
								grupos += ",";
							}
							grupos += $(this).val();
					    });

						var ativa = "";
						if ($('#usuarioAtivaTrue:checked').attr("checked") == "checked") {
							ativa = "ativa";
						}
						
						obj += "&usuarioDTO.contaAtiva=" + ativa + "&usuarioDTO.permissoesSelecionadas=" + permissoes + "&usuarioDTO.gruposSelecionados=" + grupos;

						$.postJSON(usuariosPermissaoController.path + '/salvarUsuario', obj, function(data) {
							var tipoMensagem = data.tipoMensagem;
							var listaMensagens = data.listaMensagens;

							if(tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}
							
							console.log(tipoMensagem);

							$("#effect").show("highlight", {}, 1000, callback);
							$(".usuariosGrid", usuariosPermissaoController.workspace).flexReload();

						}, null, true);

						$( this ).dialog("close");
					},
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		popup_excluir_usuario : function(codigoUsuario) {
			$( "#dialog-excluir-usuario", this.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$.getJSON(
								usuariosPermissaoController.path + "/excluirUsuario",
								"codigoUsuario=" + codigoUsuario, 
								function(result) {

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;

									if(tipoMensagem && listaMensagens) {
										exibirMensagemDialog(tipoMensagem, listaMensagens, "dialog-excluir-usuario");
									}

									$("#effect").show("highlight", {}, 1000, callback);
									$(".usuariosGrid", usuariosPermissaoController.workspace).flexReload();

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
		adicionaGruposSelecionados : function() {
			$('#gruposUsuario', this.workspace).find(":selected").each(function() {
				$("#gruposSelecionadosUsuario", this.workspace).append($("<option/>", {value: $(this)[0].value,
		 			   text: $(this)[0].text
		 			  }));
			});
			$("#gruposUsuario option:selected", this.workspace).remove();
		},
		removeGruposSelecionados : function() {
			$('#gruposSelecionadosUsuario', this.workspace).find(":selected").each(function() {
				$("#gruposUsuario", this.workspace).append($("<option/>", {value: $(this)[0].value,
		 			   text: $(this)[0].text
		 			  }));
			});
			$("#gruposSelecionadosUsuario option:selected", this.workspace).remove();
		},
		adicionaPermissoesSelecionadas : function() {
			$('#permissoesUsuario', this.workspace).find(":selected").each(function() {
				$("#permissoesSelecionadasUsuario", this.workspace).append($("<option/>", {value: $(this)[0].value,
		 			   text: $(this)[0].text
		 			  }));
			});
			$("#permissoesUsuario option:selected", this.workspace).remove();
		},
		removePermissoesSelecionadas : function() {
			$('#permissoesSelecionadasUsuario', this.workspace).find(":selected").each(function() {
				$("#permissoesUsuario", this.workspace).append($("<option/>", {value: $(this)[0].value,
		 			   text: $(this)[0].text
		 			  }));
			});
			$("#permissoesSelecionadasUsuario option:selected", this.workspace).remove();
		},
		mostrarUsuario : function() {
			var serializedObj = $("#pesquisar_usuario_form", this.workspace).serialize();
			$(".usuariosGrid", this.workspace).flexOptions({
				"url" : this.path + '/pesquisarUsuarios?' + serializedObj,
				method: 'GET',
				newp:1
			});
			$('.usuariosGrid', this.workspace).flexReload();
		},
		initUsuariosGrid : function() {
			$(".usuariosGrid", this.workspace).flexigrid({
				preProcess: function(data) {
					if( typeof data.mensagens == "object") {
						exibirMensagemDialog(data.mensagens.tipoMensagem, data.mensagens.listaMensagens, "dialog-novo-usuario");
						$(".gridsGrupos", this.workspace).hide();
					} else {
						$.each(data.rows , function(index, value) {

							var linkEditarUsuario = '<a href="javascript:;" onclick="usuariosPermissaoController.popup_editar_usuario(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Editar Usuário" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
				  		    '</a>';

							var linkAlterarSenha = '<a href="javascript:;" onclick="usuariosPermissaoController.popup_alterar_senha(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Alterar Senha" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
				  		    '</a>';
							
							var linkExcluirUsuario = '<a href="javascript:;" onclick="usuariosPermissaoController.popup_excluir_usuario(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Excluir Usuário" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
				  		    '</a>';

							// lembreteSenha
							var lembreteSenha = "";
							if (value.cell.lembreteSenha != undefined) {
								lembreteSenha = value.cell.lembreteSenha;
							}
							value.cell[1] = lembreteSenha;

							// Nome
							var nome = "";
							if (value.cell.nome != undefined) {
								nome = value.cell.nome;
								if (value.cell.sobrenome != undefined) {
									nome += " " + value.cell.sobrenome;
								}
							}
							value.cell[2] = nome;

							// E-mail
							var email = "";
							if (value.cell.email != undefined) {
								email = value.cell.email;
							}
							value.cell[3] = email;
							
							// Telefone
							var telefone = "";
							if (value.cell.telefone != undefined) {
								if (value.cell.ddd != undefined) {
									telefone = "(" + value.cell.ddd + ") ";
								}
								telefone += value.cell.telefone;
							}
							value.cell[4] = telefone;

							// Ativa
							var ativa = "N";
							if (value.cell.ativa == "true") {
								ativa = "S";
							}
							value.cell[5] = ativa;
							
							// Ação
							value.cell[6] = linkEditarUsuario + linkAlterarSenha + linkExcluirUsuario;

						});
						$(".gridsUsuario", this.workspace).show();
					}
					return data;
				},
				dataType : 'json',
				colModel : [ {
					display : 'Username',
					name : 'login',
					width : 90,
					sortable : true,
					align : 'left'
				}, {
					display : 'Lembrete',
					name : 'lembreteSenha',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 140,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-mail',
					name : 'email',
					width : 140,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ativa',
					name : 'contaAtiva',
					width : 30,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
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
				height : 255
			});
		}
}, BaseController);
