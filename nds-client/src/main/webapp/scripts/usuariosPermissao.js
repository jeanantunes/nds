var usuariosPermissaoController = $.extend(true, {
		path : "",
		init : function(contextPath) {
			this.path = contextPath + "/administracao/gruposAcesso";
			this.initUsuariosGrid();
			this.mostrarUsuario();
		},
		limpar_selecoes : function() {
			$("#gruposUsuario option", usuariosPermissaoController.workspace).remove();
			$("#gruposSelecionadosUsuario option", usuariosPermissaoController.workspace).remove();
			$("#permissoesUsuario option", usuariosPermissaoController.workspace).remove();
			$("#permissoesSelecionadasUsuario option", usuariosPermissaoController.workspace).remove();
			$("#usuarioCep").mask("99999-999");
		},
		popup_alterar_senha : function(idUsuario) {
			
			this.clearForm($("#alterar_senha_form", usuariosPermissaoController.workspace));
			
			$.getJSON(
					this.path + "/editarUsuario",
					{codigoUsuario:idUsuario}, 
					function(result) {
						if (result) {
							usuariosPermissaoController.bindData(result, $("#alterar_senha_form", usuariosPermissaoController.workspace));
						}
					}
				);
			
			$( "#dialog-alterar-senha" , usuariosPermissaoController.workspace).dialog({
				resizable: false,
				height:220,
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						var obj = $("#alterar_senha_form", usuariosPermissaoController.workspace).serialize();

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
				},
				form: $("#dialog-alterar-senha", usuariosPermissaoController.workspace).parents("form")
			});
		},
		popup_novo_usuario : function() {

			$("#trInsercaoSenhas", usuariosPermissaoController.workspace).show();
			$("#trLembreteSenha", usuariosPermissaoController.workspace).show();
			
			this.limpar_selecoes();
			
			this.clearForm($("#novo_usuario_form", usuariosPermissaoController.workspace));
			
			$('#usuarioAtivaTrue').attr('checked', true).button("refresh");								
			
			$('input[tipo="permissao"]').attr("checked", false);
			$('#permissaoGridConteudo').appendTo($('#localPermissaoGridUsuario'));
			
			$.getJSON(
					this.path + "/novoUsuario",
					null, 
					function(result) {
						if (result) {
							$(result.usuarioDTO.grupos).each(function() {
								$("#gruposUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].id,
																			 			   text: $(this)[0].nome 
																			 			  }));
							});
						}
					}
				);
			
			this.popup_usuario();
			
			//Correcao da Kaina... Permissoes nao aparecem no novo
			$(".permissaoGrid", gruposPermissaoController.workspace).flexOptions({
				url : contextPath + "/administracao/gruposAcesso/obterPermissoes",
			}).flexReload();
			// Fim
		},
		popup_editar_usuario : function(idGrupo) {
			
			$("#trInsercaoSenhas", usuariosPermissaoController.workspace).hide();
			$("#trLembreteSenha", usuariosPermissaoController.workspace).hide();
			
			$('input[tipo="permissao"]').attr("checked", false);
			$('#permissaoGridConteudo').appendTo($('#localPermissaoGridUsuario'));
			
			this.limpar_selecoes();
			
			$.getJSON(
					this.path + "/editarUsuario",
					{codigoUsuario:idGrupo}, 
					function(result) {
						if (result) {
							
							usuariosPermissaoController.bindData(result, $("#novo_usuario_form", usuariosPermissaoController.workspace));
							
							$("#supervisor", usuariosPermissaoController.workspace).attr("checked", 
									result.usuarioDTO.supervisor);
							
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

							$.each(result.usuarioDTO.permissoes, function(index, role) {
								$('input[tipo="permissao"][role="'+role+'"][isPai="false"]').attr("checked", true);
							});	
							
						}
					}
				);
			
			this.popup_usuario();
			
			//Correcao da Kaina... Permissoes nao aparecem no novo
			$(".permissaoGrid", gruposPermissaoController.workspace).flexOptions({
				url : contextPath + "/administracao/gruposAcesso/obterPermissoes",
				onSuccess: function() {
					$.getJSON(
						contextPath + "/administracao/gruposAcesso/editarGrupoPermissao",
						{codigoGrupo:1}, 
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
			//Fim
		},
		popup_usuario : function() {
			$( "#dialog-novo-usuario" , usuariosPermissaoController.workspace).dialog({
				resizable: false,
				height:620,
				width:780,
				modal: true,
				buttons: {
					"Confirmar": function() {
						var self = this;
						
						$("#supervisor", usuariosPermissaoController.workspace).val(
								$("#supervisor:checked", usuariosPermissaoController.workspace).length > 0);
						
						var obj = $("#novo_usuario_form", usuariosPermissaoController.workspace).serializeObject();
						
						var permissoes = new Array();
						
						var checkSelecionados = $('.permissao:checked');
						
						$.each(checkSelecionados, function(index, elemento) {
							permissoes.push(elemento.getAttribute('role'));
						});
						//$("#permissoesSelecionadasUsuario option", usuariosPermissaoController.workspace).each(function() {							
						//	permissoes.push($(this).val());
					    //});
						
						obj = serializeArrayToPost('usuarioDTO.permissoes', permissoes, obj);
						
						var grupos = new Array();
						$("#gruposSelecionadosUsuario option", usuariosPermissaoController.workspace).each(function() {
							
							grupos.push($(this).val());
					    });
						
						obj = serializeArrayToPost('usuarioDTO.idsGrupos', grupos, obj);
						
						obj['usuarioDTO.contaAtiva'] = ($('#usuarioAtivaTrue:checked').attr("checked") == "checked");
						

						$.postJSON(usuariosPermissaoController.path + '/salvarUsuario', obj, function(data) {
							var tipoMensagem = data.tipoMensagem;
							var listaMensagens = data.listaMensagens;

							if(tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}

							$( self ).dialog("close");
							$(".usuariosGrid", usuariosPermissaoController.workspace).flexReload();

						}, null, true);

						
					},
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-novo-usuario", usuariosPermissaoController.workspace).parents("form")
			});
		},
		popup_excluir_usuario : function(codigoUsuario) {
			$( "#dialog-excluir-usuario", usuariosPermissaoController.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$.getJSON(
								usuariosPermissaoController.path + "/excluirUsuario",
								{codigoUsuario:codigoUsuario}, 
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
				},
				form: $("#dialog-excluir-usuario", usuariosPermissaoController.workspace).parents("form")
			});
		},
		adicionaGruposSelecionados : function() {
			$('#gruposUsuario', usuariosPermissaoController.workspace).find(":selected").each(function() {
				$("#gruposSelecionadosUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].value,
		 			   text: $(this)[0].text
		 			  }));
			});
			$("#gruposUsuario option:selected", usuariosPermissaoController.workspace).remove();
		},
		removeGruposSelecionados : function() {
			$('#gruposSelecionadosUsuario', usuariosPermissaoController.workspace).find(":selected").each(function() {
				$("#gruposUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].value,
		 			   text: $(this)[0].text
		 			  }));
			});
			$("#gruposSelecionadosUsuario option:selected", usuariosPermissaoController.workspace).remove();
		},
		adicionaPermissoesSelecionadas : function() {
			$('#permissoesUsuario', usuariosPermissaoController.workspace).find(":selected").each(function() {
				$("#permissoesSelecionadasUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].value,
		 			   text: $(this)[0].text
		 			  }));
			});
			$("#permissoesUsuario option:selected", usuariosPermissaoController.workspace).remove();
		},
		removePermissoesSelecionadas : function() {
			$('#permissoesSelecionadasUsuario', usuariosPermissaoController.workspace).find(":selected").each(function() {
				$("#permissoesUsuario", usuariosPermissaoController.workspace).append($("<option/>", {value: $(this)[0].value,
		 			   text: $(this)[0].text
		 			  }));
			});
			$("#permissoesSelecionadasUsuario option:selected", usuariosPermissaoController.workspace).remove();
		},
		mostrarUsuario : function() {
			var serializedObj = $("#pesquisar_usuario_form", usuariosPermissaoController.workspace).serialize();
			$(".usuariosGrid", usuariosPermissaoController.workspace).flexOptions({
				"url" : this.path + '/pesquisarUsuarios?' + serializedObj,
				method: 'GET',
				newp:1,
				onSuccess: function() {bloquearItensEdicao(usuariosPermissaoController.workspace.workspace);}
			});
			$('.usuariosGrid', usuariosPermissaoController.workspace).flexReload();
		},
		initUsuariosGrid : function() {
			$(".usuariosGrid", usuariosPermissaoController.workspace).flexigrid({
				preProcess: function(data) {
					if( typeof data.mensagens == "object") {
						exibirMensagemDialog(data.mensagens.tipoMensagem, data.mensagens.listaMensagens, "dialog-novo-usuario");
						$(".gridsGrupos", usuariosPermissaoController.workspace).hide();
					} else {
						$.each(data.rows , function(index, value) {

							var linkEditarUsuario = '<a isEdicao="true" href="javascript:;" onclick="usuariosPermissaoController.popup_editar_usuario(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Editar Usuário" src="' + contextPath + '/images/ico_editar.gif" border="0px" />' +
				  		    '</a>';

							var linkAlterarSenha = '<a isEdicao="true" href="javascript:;" onclick="usuariosPermissaoController.popup_alterar_senha(\'' + value.cell.id + '\');" style="cursor:pointer; margin-left:10px; margin-right:10px;">' +
				     	  	'<img title="Alterar Senha" src="' + contextPath + '/images/ico_bloqueado.gif"  border="0px" />' +
				  		    '</a>';
							
							var linkExcluirUsuario = '<a isEdicao="true" href="javascript:;" onclick="usuariosPermissaoController.popup_excluir_usuario(\'' + value.cell.id + '\');" style="cursor:pointer">' +
				     	  	'<img title="Excluir Usuário" src="' + contextPath + '/images/ico_excluir.gif" border="0px" />' +
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
							if (value.cell.contaAtiva == true) {
								ativa = "S";
							}
							value.cell[5] = ativa;
							
							// Ação
							value.cell[6] = linkEditarUsuario + linkAlterarSenha + linkExcluirUsuario;

						});
						$(".gridsUsuario", usuariosPermissaoController.workspace).show();
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
					width : 145,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-mail',
					name : 'email',
					width : 168,
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
					width : 70,
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
				height : 240
			});
		}
}, BaseController);

//@ sourceURL=usuariosPermissao.js