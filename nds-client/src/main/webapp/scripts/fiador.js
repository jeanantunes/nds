var fiadorController = {
		fecharModalCadastroFiador: false,
		addConjuge : false,
		
		popupCadastroFiadorCPF:function () {
			
			$("#fiadorController-fiadorController-tabSocio").hide();
			$('#fiadorController-fiadorController-tabs').tabs('select', 0);
			
			$("#fiadorController-cadastroCnpj").hide();
			$("#fiadorController-cadastroCpf").show();
			
			this.modalCadastroFiador("CPF");
		},
	
		popupCadastroFiadorCNPJ:function () {
			
			$("#fiadorController-tabSocio").show();
			$('#fiadorController-tabs').tabs('select', 0);
			
			$("#fiadorController-cadastroCnpj").show();
			$("#fiadorController-cadastroCpf").hide();
			
			this.modalCadastroFiador("CNPJ");
		},
		
		modalCadastroFiador: function (paramCpfCnpj){
			var _this = this;
			this.fecharModalCadastroFiador = false;
			
			this.limparCamposCadastroFiador();
			
			$("#fiadorController-dialog-fiador").dialog({
				resizable: false,
				height:610,
				width:840,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						if (paramCpfCnpj == "CPF") {
							_this.cadastrarFiadorCpf(this);
						} else {
							_this.cadastrarFiadorCnpj(this);
						}
					},
					"Cancelar": function() {
						
						$(this).dialog("close");
					}
				},
				beforeClose: function(event, ui) {
					
					if (!_this.fecharModalCadastroFiador){
						_this.cancelarCadastro();
					}
					
					return _this.fecharModalCadastroFiador;
				}
			});
		
			$(".fiadorController-trSocioPrincipal").hide();
		},
		
		cancelarCadastro:function (){
			var _this =  this;
			$("#fiadorController-dialog-cancelar-cadastro-fiador").dialog({
				resizable: false,
				height:150,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON("<c:url value='/cadastro/fiador/cancelarCadastro'/>", null, 
							function(result){
							_this.fecharModalCadastroFiador = true;
								$("#fiadorController-dialog-close").dialog("close");
								$("#fiadorController-dialog-fiador").dialog("close");
								$("#fiadorController-dialog-cancelar-cadastro-fiador").dialog("close");
								$('[name="cpfFiador"]').removeAttr("disabled");
								$('[name="cpfConjuge"]').removeAttr("disabled");
								$("#fiadorController-cnpjFiador").removeAttr("disabled");
								
								_this.limparCamposCadastroFiador();
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
						_this.fecharModalCadastroFiador = false;
					}
				}
			});
		},
	
		popup_excluir:function () {
			var _this = this;
			$( "#fiadorController-dialog-excluir" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						_this.cadastrarFiadorCnpj(this);
					},
					"Cancelar": function() {
						$(this).dialog( "close" );
					}
				}
			});
		},
		
		exibirGridFiadoresCadastrados:function (){
			var _this = this;
			var data = "filtro.nome=" + $("#fiadorController-nomeFiadorPesquisa").val() + "&filtro.cpfCnpj=" + $("#fiadorController-cpfCnpjFiadorPesquisa").val();
			$.postJSON("<c:url value='/cadastro/fiador/pesquisarFiador'/>", data, 
				function(result){
					
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".fiadorController-pessoasGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados").show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados").hide();
					}
				}
			);
		},
		
		init : function() {
			var _this  = this;
			$("#fiadorController-tabs").tabs();
			
			$(".fiadorController-pessoasGrid").flexigrid({
				preProcess: function(){_this.processarResultadoConsultaFiadores();},
				dataType : 'json',
				colModel : [  {
					display : 'CÃ³digo',
					name : 'codigo',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Nome',
					name : 'nome',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'CPF / CNPJ',
					name : 'cpfCnpj',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'RG / InscriÃ§Ã£o Estadual',
					name : 'rg',
					width : 110,
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
					display : 'AÃ§Ã£o',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				sortorder : "desc",
				disableSelect: true,
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
			});
			
			$(".fiadorController-pessoasGrid").flexOptions({url: "<c:url value='/cadastro/fiador/pesquisarFiador'/>"});
			
			
			$("#fiadorController-cnpjFiador").mask("99.999.999/9999-99",{completed:function(){_this.buscarPessoaCNPJ(this.val()); }});
			jQuery(function($){
				   $.mask.definitions['#']='[\-\.0-9]';
				   $("#inscricaoEstadualFiador").mask("?##################",{placeholder:" "});
			});
			
			$('[name="cpfFiador"]').mask("999.999.999-99");
			$('[name="cpfConjuge"]').mask("999.999.999-99");
			$('[name="dataNascimentoFiadorCpf"]').mask("99/99/9999");
			$('[name="dataNascimentoConjugeCpf"]').mask("99/99/9999");
			$('[name="selectUfOrgaoEmiCpf"]').mask("aa");
			$('[name="selectUfOrgaoEmiConjugeCpf"]').mask("aa");
		},
		
		processarResultadoConsultaFiadores:function (data){
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				
				return;
			}
			
			if (data.result){
				data.rows = data.result[1].rows;
			}
			
			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length;

				data.rows[i].cell[lastIndex] = this.getActionsConsultaFiadores(data.rows[i].id);
			}

			$('.imoveisGrid').show();
			
			if (data.result){
				return data.result[1];
			}
			return data;
		},
		
		getActionsConsultaFiadores: function (idFiador){
			return '<a href="javascript:;" onclick="editarFiador(' + idFiador + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar Fiador">' +
					'<img src="/nds-client/images/ico_editar.gif" border="0px"/>' +
					'</a>' +
					'<a href="javascript:;" onclick="excluirFiador(' + idFiador + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir Fiador">' +
					'<img src="/nds-client/images/ico_excluir.gif" border="0px"/>' +
					'</a>';
		},
		
		editarFiador:function (idFiador){
			var _this = this;
			$.postJSON("<c:url value='/cadastro/fiador/editarFiador' />", "idFiador=" + idFiador, 
				function(result) {
					
					$(".fiadorController-inicioAtividadeNovo").hide();
					$(".fiadorController-inicioAtividadeEdicao").show();
					
					_this.limparCamposCadastroFiador();
				
					if (result[0] == "CPF"){
						
						_this.popupCadastroFiadorCPF();
						
						$("#fiadorController-nomeFiadorCpf").val(result[1]);
						$("#fiadorController-emailFiadorCpf").val(result[2]);
						$("#fiadorController-cpfFiador").val(result[3]);
						$("#fiadorController-rgFiador").val(result[4]);
						$("#fiadorController-dataNascimentoFiadorCpf").val(result[5]);
						$("#fiadorController-orgaoEmissorFiadorCpf").val(result[6]);
						$("#fiadorController-selectUfOrgaoEmiCpf").val(result[7]);
						$("#fiadorController-estadoCivilFiadorCpf").val(result[8]);
						$("#fiadorController-selectSexoFiador").val(result[9]);
						$("#fiadorController-nacionalidadeFiadorCpf").val(result[10]);
						$("#fiadorController-naturalFiadorCpf").val(result[11]);
						
						if ($("#fiadorController-estadoCivilFiadorCpf").val() == "CASADO"){
							
							_this.opcaoCivilPf("CASADO");
							
							$("#fiadorController-nomeConjugeCpf").val(result[12]);
					        $("#fiadorController-emailConjugeCpf").val(result[13]);
					        $("#fiadorController-cpfConjuge").val(result[14]);
					        $("#fiadorController-rgConjuge").val(result[15]);
					        $("#fiadorController-dataNascimentoConjugeCpf").val(result[16]);
					        $("#fiadorController-orgaoEmissorConjugeCpf").val(result[17]);
					        $("#fiadorController-selectUfOrgaoEmiConjugeCpf").val(result[18]);
					        $("#fiadorController-selectSexoConjuge").val(result[19]);
					        $("#fiadorController-nacionalidadeConjugeCpf").val(result[20]);
					        $("#fiadorController-naturalConjugeCpf").val(result[21]);
					        
					        $(".fiadorController-inicioAtividadeEdicao").text(result[22]);
					        
						} else {
							$(".fiadorController-inicioAtividadeEdicao").text(result[12]);
						}
						
						$('[name="cpfFiador"]').attr("disabled", true);
						$('[name="cpfConjuge"]').attr("disabled", true);
					} else {
						
						_this.popupCadastroFiadorCNPJ();
						
						$("#fiadorController-razaoSocialFiador").val(result[1]);
						$("#fiadorController-nomeFantasiaFiador").val(result[2]);
						$("#fiadorController-inscricaoEstadualFiador").val(result[3]);
						$("#fiadorController-cnpjFiador").val(result[4]);
						$("#fiadorController-emailFiadorCnpj").val(result[5]);
						$(".fiadorController-inicioAtividadeEdicao").text(result[6]);
						
						$("#fiadorController-cnpjFiador").attr("disabled", true);
					}
				}
			);
		},
		
		excluirFiador:function (idFiador){
			$(".fiadorController-dialog-excluir-fiador").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						
						$.postJSON("<c:url value='/cadastro/fiador/excluirFiador' />", "idFiador=" + idFiador, 
							function(result) {
								
								if (result[0].tipoMensagem){
									exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
								}
								
								if (result[1] != ""){
									$(".fiadorController-pessoasGrid").flexAddData({
										page: result[1].page, total: result[1].total, rows: result[1].rows
									});
									
									$("#fiadorController-gridFiadoresCadastrados").show();
								} else {
									$("#fiadorController-gridFiadoresCadastrados").hide();
								}
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$(".fiadorController-dialog-excluir-fiador").show();
		},
		
		
		limparCamposCadastroFiador:function (){
			//dados cadastrais cpf
			this.limparDadosCadastraisCPF(0);
	        
	        //dados cadastrais cnpj
			this.limparDadosCadastraisCNPJ();
	        
			//dados cadastrais socios
			this.limparDadosCadastraisCPF(1);
		    
		    //endereÃ§os
			ENDERECO_FIADOR.limparFormEndereco();
		    
		    //telefones
		    FIADOR.limparCamposTelefone();
		    
		    //garantias
		    this.limparCamposGarantias();
		    
		    //cotas associadas
		    this.limparCamposCotasAssociadas();
		    
		    this.opcaoCivilPf("");
		},
		cadastrarFiadorCnpj :function (janela){
			var _this = this;
			this.fecharModalCadastroFiador = true;
			
			var data = "fiador.razaoSocial=" + $("#fiadorController-razaoSocialFiador").val() + "&" +
			           "fiador.nomeFantasia=" + $("#fiadorController-nomeFantasiaFiador").val() + "&" +
			           "fiador.inscricaoEstadual=" + $("#fiadorController-inscricaoEstadualFiador").val() + "&" +
			           "fiador.cnpj=" + $("#fiadorController-cnpjFiador").val() + "&" +
			           "fiador.email=" + $("#fiadorController-emailFiadorCnpj").val();
			
			$.postJSON("<c:url value='/cadastro/fiador/cadastrarFiadorCnpj' />", data, 
				function(result){
			
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".fiadorController-pessoasGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados").show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados").hide();
					}
					
					if (result[0].tipoMensagem == "SUCCESS"){
						$(janela).dialog("close");
						$("#fiadorController-cnpjFiador").removeAttr("disabled");
					}
				},
				function(){
					_this.fecharModalCadastroFiador = false;
				},
				true
			);
		},
		
		limparDadosCadastraisCNPJ:function (){
			$("#fiadorController-razaoSocialFiador").val("");
			$("#fiadorController-nomeFantasiaFiador").val("");
			$("#fiadorController-inscricaoEstadualFiador").val("");
			$("#fiadorController-cnpjFiador").val("");
			$("#fiadorController-emailFiadorCnpj").val("");
		},
		
		buscarPessoaCNPJ:function (cnpj){
			
			if (cnpj != "__.___.___/____-__" && cnpj != ""){
				
				$.postJSON("<c:url value='/cadastro/fiador/buscarPessoaCNPJ' />", "cnpj=" + cnpj, 
					function(result) {
						
						if (result[0] != undefined){
							$("#fiadorController-razaoSocialFiador").val(result[0]);
							$("#fiadorController-nomeFantasiaFiador").val(result[1]);
							$("#fiadorController-inscricaoEstadualFiador").val(result[2]);
							$("#fiadorController-cnpjFiador").val(result[3]);
							$("#fiadorController-emailFiadorCnpj").val(result[4]);
						}
					},
					null,
					true
				);
			}
		},
		
		opcaoCivilPf:function (valor){
			if (valor == "CASADO"){
				$(".fiadorController-divConjuge").show();
				this.addConjuge = true;
			} else {
				$(".fiadorController-divConjuge").hide();
				this.addConjuge = false;
			}
		},
		
		cadastrarFiadorCpf:function (janela){
			var _this = this;
			this.fecharModalCadastroFiador = true;
			
			var data = "pessoa.nome=" + $("#fiadorController-nomeFiadorCpf").val() + "&" +
			           "pessoa.email=" + $("#fiadorController-emailFiadorCpf").val() + "&" +
			           "pessoa.cpf=" + $("#fiadorController-cpfFiador").val() + "&" +
			           "pessoa.rg=" + $("#fiadorController-rgFiador").val() + "&" +
			           "pessoa.dataNascimento=" + $("#fiadorController-dataNascimentoFiadorCpf").val() + "&" +
			           "pessoa.orgaoEmissor=" + $("#fiadorController-orgaoEmissorFiadorCpf").val() + "&" +
			           "pessoa.ufOrgaoEmissor=" + $("#fiadorController-selectUfOrgaoEmiCpf").val() + "&" +
			           "pessoa.estadoCivil=" + $("#fiadorController-estadoCivilFiadorCpf").val() + "&" +
			           "pessoa.sexo=" + $("#fiadorController-selectSexoFiador").val() + "&" +
			           "pessoa.nacionalidade=" + $("#fiadorController-nacionalidadeFiadorCpf").val() + "&" +
			           "pessoa.natural=" + $("#fiadorController-naturalFiadorCpf").val();
			
			if (this.addConjuge){ 
			           data = data + "&pessoa.conjuge.nome=" + $("#fiadorController-nomeConjugeCpf").val() + "&" +
			           "pessoa.conjuge.email=" + $("#fiadorController-emailConjugeCpf").val() + "&" +
			           "pessoa.conjuge.cpf=" + $("#fiadorController-cpfConjuge").val() + "&" +
			           "pessoa.conjuge.rg=" + $("#fiadorController-rgConjuge").val() + "&" +
			           "pessoa.conjuge.dataNascimento=" + $("#fiadorController-dataNascimentoConjugeCpf").val() + "&" +
			           "pessoa.conjuge.orgaoEmissor=" + $("#fiadorController-orgaoEmissorConjugeCpf").val() + "&" +
			           "pessoa.conjuge.ufOrgaoEmissor=" + $("#fiadorController-selectUfOrgaoEmiConjugeCpf").val() + "&" +
			           "pessoa.conjuge.sexo=" + $("#fiadorController-selectSexoConjuge").val() + "&" +
			           "pessoa.conjuge.nacionalidade=" + $("#fiadorController-nacionalidadeConjugeCpf").val() + "&" +
			           "pessoa.conjuge.natural=" + $("#fiadorController-naturalConjugeCpf").val();
			}
			
			$.postJSON("<c:url value='/cadastro/fiador/cadastrarFiadorCpf'/>", data, 
				function(result){
						
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".fiadorController-pessoasGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#fiadorController-gridFiadoresCadastrados").show();
					} else {
						$("#fiadorController-gridFiadoresCadastrados").hide();
					}
					
					if (result[0].tipoMensagem == "SUCCESS"){
						$(janela).dialog("close");
						$("#fiadorController-cpfFiador").removeAttr("disabled");
						$("#fiadorController-cpfConjuge").removeAttr("disabled");
					}
				},
				function (){
					_this.fecharModalCadastroFiador = false;
				},
				true
			);
		},
		
		limparDadosCadastraisCPF:function (indiceAba){
			
			$('[name="nomeFiadorCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="emailFiadorCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="cpfFiador"]:eq('+ indiceAba +')').val("");
	        $('[name="rgFiador"]:eq('+ indiceAba +')').val("");
	        $('[name="dataNascimentoFiadorCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="orgaoEmissorFiadorCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="selectUfOrgaoEmiCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="estadoCivilFiadorCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="selectSexoFiador"]:eq('+ indiceAba +')').val("");
	        $('[name="nacionalidadeFiadorCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="naturalFiadorCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="nomeConjugeCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="emailConjugeCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="cpfConjuge"]:eq('+ indiceAba +')').val("");
	        $('[name="rgConjuge"]:eq('+ indiceAba +')').val("");
	        $('[name="dataNascimentoConjugeCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="orgaoEmissorConjugeCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="selectUfOrgaoEmiConjugeCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="selectSexoConjuge"]:eq('+ indiceAba +')').val("");
	        $('[name="nacionalidadeConjugeCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="naturalConjugeCpf"]:eq('+ indiceAba +')').val("");
	        $('[name="checkboxSocioPrincipal"]:eq('+ indiceAba +')').uncheck();
	        enableFields(indiceAba);
		},
		
		enableFields:function (indiceAba) {
			$('[name="nomeFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="emailFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="cpfFiador"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="rgFiador"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="dataNascimentoFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="orgaoEmissorFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="selectUfOrgaoEmiCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="estadoCivilFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="selectSexoFiador"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="nacionalidadeFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="naturalFiadorCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="nomeConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="emailConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="cpfConjuge"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="rgConjuge"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="dataNascimentoConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="orgaoEmissorConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="selectUfOrgaoEmiConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="selectSexoConjuge"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="nacionalidadeConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
	        $('[name="naturalConjugeCpf"]:eq('+ indiceAba +')').attr("disabled", false);
		},
		buscarPessoaCPF:function (cpf, fiador){
			var _this = this;
			var refAba = $("#tab-1").css("display") == "block" ? 0 : 1;
			
			if (cpf != "___.___.___-__" && cpf != ""){
				
				var data = "cpf=" + cpf + "&isFiador=" + fiador + "&cpfConjuge=" + $('[name="cpfFiador"]:eq(' + refAba + ')').val() +
					"&socio=" + (refAba == 0 ? "false" : "true");
				
				$.postJSON("<c:url value='/cadastro/fiador/buscarPessoaCPF' />", data, 
					function(result) {
					
						if (result[0] != undefined){
							
							if (fiador){
								$('[name="nomeFiadorCpf"]:eq(' + refAba + ')').val(result[0]);
								$('[name="emailFiadorCpf"]:eq(' + refAba + ')').val(result[1]);
								$('[name="cpfFiador"]:eq(' + refAba + ')').val(result[2]);
								$('[name="rgFiador"]:eq(' + refAba + ')').val(result[3]);
								$('[name="dataNascimentoFiadorCpf"]:eq(' + refAba + ')').val(result[4]);
								$('[name="orgaoEmissorFiadorCpf"]:eq(' + refAba + ')').val(result[5]);
								$('[name="selectUfOrgaoEmiCpf"]:eq(' + refAba + ')').val(result[6]);
								$('[name="estadoCivilFiadorCpf"]:eq(' + refAba + ')').val(result[7]);
								$('[name="selectSexoFiador"]:eq(' + refAba + ')').val(result[8]);
								$('[name="nacionalidadeFiadorCpf"]:eq(' + refAba + ')').val(result[9]);
								$('[name="naturalFiadorCpf"]:eq(' + refAba + ')').val(result[10]);
								
								if (result[7] == "CASADO"){
									
									_this.opcaoCivilPf(result[7]);
									
									$('[name="nomeConjugeCpf"]:eq(' + refAba + ')').val(result[11]);
									$('[name="emailConjugeCpf"]:eq(' + refAba + ')').val(result[12]);
									$('[name="cpfConjuge"]:eq(' + refAba + ')').val(result[13]);
									$('[name="rgConjuge"]:eq(' + refAba + ')').val(result[14]);
									$('[name="dataNascimentoConjugeCpf"]:eq(' + refAba + ')').val(result[15]);
									$('[name="orgaoEmissorConjugeCpf"]:eq(' + refAba + ')').val(result[16]);
									$('[name="selectUfOrgaoEmiConjugeCpf"]:eq(' + refAba + ')').val(result[17]);
									$('[name="selectSexoConjuge"]:eq(' + refAba + ')').val(result[18]);
									$('[name="nacionalidadeConjugeCpf"]:eq(' + refAba + ')').val(result[19]);
									$('[name="naturalConjugeCpf"]:eq(' + refAba + ')').val(result[20]);
								}
							} else {
								
								$('[name="nomeConjugeCpf"]:eq(' + refAba + ')').val(result[0]);
								$('[name="emailConjugeCpf"]:eq(' + refAba + ')').val(result[1]);
								$('[name="cpfConjuge"]:eq(' + refAba + ')').val(result[2]);
								$('[name="rgConjuge"]:eq(' + refAba + ')').val(result[3]);
								$('[name="dataNascimentoConjugeCpf"]:eq(' + refAba + ')').val(result[4]);
								$('[name="orgaoEmissorConjugeCpf"]:eq(' + refAba + ')').val(result[5]);
								$('[name="selectUfOrgaoEmiConjugeCpf"]:eq(' + refAba + ')').val(result[6]);
								$('[name="selectSexoConjuge"]:eq(' + refAba + ')').val(result[8]);
								$('[name="nacionalidadeConjugeCpf"]:eq(' + refAba + ')').val(result[9]);
								$('[name="naturalConjugeCpf"]:eq(' + refAba + ')').val(result[10]);
							}
						}
					},
					null,
					true
				);
			}
		}
		
};