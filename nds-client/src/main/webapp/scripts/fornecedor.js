var fornecedorController = $.extend(true,{
		init:function(){
			$('#fornecedorController-filtroConsultaFornecedorRazaoSocial,#fornecedorController-filtroConsultaFornecedorCnpj,#fornecedorController-filtroConsultaFornecedorNomeFantasia').bind('keypress', function(e) {
				if(e.keyCode == 13) {
					fornecedorController.pesquisarFornecedores();
				}
			});
			
			$('#fornecedorController-filtroConsultaFornecedorRazaoSocial').focus();
			$("#fornecedorController-fornecedoresGrid").flexigrid({
				dataType : 'json',
				colModel : [  {
					display : 'C&oacute;digo',
					name : 'codigoInterface',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Raz&atilde;o Social',
					name : 'razaoSocial',
					width : 160,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNPJ',
					name : 'cnpj',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Contato',
					name : 'responsavel',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-Mail',
					name : 'email',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : 'A&ccedil;&atilde;o',
					name : 'acao',
					width : 50,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				singleSelect: true,
				rp : 15,
				showTableToggleBtn : true,
				width : 950,
				height : 'auto'
			});
			
			$("#fornecedorController-validadeContrato").datepicker({
				showOn : "button",
				buttonImage: contextPath + "/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy'
			});
			
			$( "#fornecedorController-validadeContrato" ).mask("99/99/9999");
			
			$( "#fornecedorController-cnpj" ).mask("99.999.999/9999-99",{completed:function(){
				fornecedorController.obterPessoaJuridica();
			}});
					
			$( "#fornecedorController-validadeContrato" ).mask("99/99/9999");
			
			$( "#fornecedorController-tabFornecedores" ).tabs();
		
			jQuery(function($){
			   $.mask.definitions['#']='[\-\.0-9]';
			   $("#fornecedorController-inscricaoEstadual").mask("?##################",{placeholder:" "});
			});
			
			
			
		},

		novoFornecedor:	function (isEdicao) {
				
				if (!isEdicao) {
					
					fornecedorController.limparCamposModal();
					
					$.postJSON(
						 contextPath +"/cadastro/fornecedor/novoCadastro",
						null,
						function(result) {
							$("#fornecedorController-inicioAtividade").html(result.data);
							$("#fornecedorController-codigoInterface").val(result.nextCodigo);
							
							fornecedorController.showPopupFornecedor();
						},
						null,
						true
					);
				
				} else {
					
					fornecedorController.showPopupFornecedor();
				}

				$( "#fornecedorController-tabFornecedores" ).tabs( "select" , 0 );
			},

			showPopupFornecedor:	function () {

				$( "#fornecedorController-dialogNovoFornecedor" ).dialog({
					resizable: false,
					height:570,
					width:840,
					modal: true,
					buttons: {
						"Confirmar": function() {
							fornecedorController.cadastrarFornecedor();
						},
						"Cancelar": function() {
							$( this ).dialog( "close" );
						}
					}
				});
			},
			
			limparCamposModal:	function () {
				$("#fornecedorController-idFornecedor").val("");
				$("#fornecedorController-codigoInterface").val("");
				$("#fornecedorController-razaoSocial").val("");
				$("#fornecedorController-nomeFantasia").val("");
				$("#fornecedorController-cnpj").val("");
				$("#fornecedorController-inscricaoEstadual").val("");
				$("#fornecedorController-responsavel").val("");
				$("#fornecedorController-email").val("");
				$("#fornecedorController-tipoFornecedor").val("");
				$("#fornecedorController-validadeContrato").val("");
				$("#fornecedorController-emailNfe").val("");
				$("#fornecedorController-possuiContrato").uncheck();
				$( '#fornecedorController-validade' ).hide();
				FORNECEDOR.limparCamposTelefone();
				ENDERECO_FORNECEDOR.limparFormEndereco();
			},
			
			cadastrarFornecedor:function () {
				
				var formData = $("#fornecedorController-formNovoFornecedor").serializeArray();
				
				$.postJSON(
					 contextPath +"/cadastro/fornecedor/cadastrarFornecedor",
					formData,
					function(result) {
						
						$( "#fornecedorController-dialogNovoFornecedor" ).dialog( "close" );
						
						exibirMensagem(
							result.tipoMensagem, 
							result.listaMensagens
						);
						fornecedorController.pesquisarFornecedores();
					},
					function(result) {
						exibirMensagemDialog(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens, ""
						);
					},
					true
				);
			},
			
			pesquisarFornecedores:function () {
				
				var formData = $("#fornecedorController-formularioPesquisaFornecedores").serializeArray();

				$("#fornecedorController-fornecedoresGrid").flexOptions({
					url :  contextPath +"/cadastro/fornecedor/pesquisarFornecedores",
					preProcess : fornecedorController.processarResultadoFornecedores,
					params : formData
				});
				
				$("#fornecedorController-fornecedoresGrid").flexReload();

				$(".fornecedorController-grids").show();
			},

			processarResultadoFornecedores:function (data) {
				
				if (data.mensagens) {

					exibirMensagem(
						data.mensagens.tipoMensagem,
						data.mensagens.listaMensagens
					);

					$(".fornecedorController-grids").hide();

					return;
				}

				data = data.tableModel;

				var i;

				for (i = 0; i < data.rows.length; i++) {

					if (!data.rows[i].cell.codigoInterface) {
						
						data.rows[i].cell.codigoInterface = "";
					}
					
					if (!data.rows[i].cell.razaoSocial) {
						
						data.rows[i].cell.razaoSocial = "";
					}
								
					if (!data.rows[i].cell.cnpj) {
						
						data.rows[i].cell.cnpj = "";
					}else{
						data.rows[i].cell.cnpj = Cnpj(data.rows[i].cell.cnpj);
					}
					
					if (!data.rows[i].cell.responsavel) {
						
						data.rows[i].cell.responsavel = "";
					}
					
					if (!data.rows[i].cell.telefone) {
						
						data.rows[i].cell.telefone = "";
					}

					if (!data.rows[i].cell.email) {
						
						data.rows[i].cell.email = "";
					}
					
					if (!data.rows[i].cell.emailNfe) {
						
						data.rows[i].cell.emailNfe = "";
					}
					
					var isHabilitado = data.rows[i].cell.origem != 'INTERFACE';
					
					data.rows[i].cell.acao = fornecedorController.getActionFornecedor(data.rows[i].id, isHabilitado);
				}

				if (data.rows.length < 0) {

					$(".fornecedorController-grids").hide();

					return;
				} 

				$(".fornecedorController-grids").show();

				return data;
			},

			editarFornecedor:function (idFornecedor) {

				$.postJSON(
					 contextPath +"/cadastro/fornecedor/editarFornecedor",
					{'idFornecedor': idFornecedor},
					function(result) {
						
						$("#fornecedorController-inicioAtividade").html(result.inicioAtividade);
						$("#fornecedorController-idFornecedor").val(result.idFornecedor);
						$("#fornecedorController-codigoInterface").val(result.codigoInterface);
						$("#fornecedorController-razaoSocial").val(result.razaoSocial);
						$("#fornecedorController-nomeFantasia").val(result.nomeFantasia);
						$("#fornecedorController-cnpj").val(Cnpj(result.cnpj));
						$("#fornecedorController-inscricaoEstadual").val(result.inscricaoEstadual);
						$("#fornecedorController-responsavel").val(result.responsavel);
						$("#fornecedorController-email").val(result.email);
						$("#fornecedorController-emailNfe").val(result.emailNfe);
						$("#fornecedorController-tipoFornecedor").val(result.tipoFornecedor);
						
						if (result.possuiContrato) {

							$("#fornecedorController-possuiContrato").check();

							$("#fornecedorController-validadeContrato").val(result.validadeContrato);	
						
							$( '.fornecedorController-validade' ).show();
							
						} else {
							
							$("#fornecedorController-possuiContrato").uncheck();
							
							$("#fornecedorController-validadeContrato").val("");
							
							$( '.fornecedorController-validade' ).hide();
						}

						fornecedorController.novoFornecedor(true);
						
						ENDERECO_FORNECEDOR.popularGridEnderecos();
						
						FORNECEDOR.carregarTelefones();
					},
					function(result) {
						exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
						);
					}
				);
			},
			
			getActionFornecedor:function (idFornecedor, isHabilitado) {

				if (isHabilitado) {
				
					return '<a href="javascript:;" onclick="fornecedorController.editarFornecedor('
							+ idFornecedor
							+ ')" '
							+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar fornecedor">'
							+ '<img src="'+contextPath +'/images/ico_editar.gif" border="0px"/>'
							+ '</a>'
							+ '<a href="javascript:;" onclick="fornecedorController.confirmarExclusaoFornecedor('
							+ idFornecedor
							+ ')" '
							+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir fornecedor">'
							+ '<img src="'+contextPath + '/images/ico_excluir.gif" border="0px"/>'
							+ '</a>';
				
				} else {
					
					return '<img src="'+contextPath +'/images/ico_editar.gif" border="0px"'
							+ ' style="opacity:0.4;padding-right:10px"/>'
						 	+ '<img src="'+contextPath + '/images/ico_excluir.gif" border="0px"'
						 	+ ' style="opacity:0.4"/>';

				}
			},

			confirmarExclusaoFornecedor:function (idFornecedor) {
			
				$( "#fornecedorController-dialog-excluir" ).dialog({
					resizable: false,
					height:170,
					width:380,
					modal: true,
					buttons: {
						"Confirmar": function() {
							fornecedorController.excluirFornecedor(idFornecedor);
							$( this ).dialog( "close" );
						},
						"Cancelar": function() {
							$( this ).dialog( "close" );
						}
					}
				});
			},
			
			excluirFornecedor:function (idFornecedor) {
			
				$.postJSON(
					 contextPath +"/cadastro/fornecedor/excluirFornecedor",
					{'idFornecedor': idFornecedor},
					function(result) {
						exibirMensagem(
							result.tipoMensagem, 
							result.listaMensagens
						);
						
						fornecedorController.pesquisarFornecedores();
					},
					function(result) {
						exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
						);
					}
				);
			},
			
			obterPessoaJuridica:function () {				
				$.postJSON(
						 contextPath +"/cadastro/fornecedor/obterPessoaJuridica",
						{'cnpj': $("#fornecedorController-cnpj").val()},
						function(result) {
							
							if (result.razaoSocial) 
								$("#fornecedorController-razaoSocial").val(result.razaoSocial);
							if (result.nomeFantasia)
								$("#fornecedorController-nomeFantasia").val(result.nomeFantasia);
							if (result.inscricaoEstadual)
								$("#fornecedorController-inscricaoEstadual").val(result.inscricaoEstadual);
							if (result.email)
								$("#fornecedorController-email").val(result.email);
						},
						null,
						true
					);
			},
			

			mostraValidade:function () {
				
				if ($(".fornecedorController-validade").css("display") == "none") {

					$( '.fornecedorController-validade' ).fadeIn( "slow" );
				
				} else {

					$( '.fornecedorController-validade' ).val("");

					$( '.fornecedorController-validade' ).fadeOut( "slow" );
				}

			}		
}, BaseController);