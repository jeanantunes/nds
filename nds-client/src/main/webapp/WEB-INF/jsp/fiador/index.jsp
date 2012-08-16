<head>
	<script language="javascript" type="text/javascript">
		var fecharModalCadastroFiador = false;
	
		function popupCadastroFiadorCPF() {
			
			$("#tabSocio").hide();
			$('#tabs').tabs('select', 0);
			
			$("#cadastroCnpj").hide();
			$("#cadastroCpf").show();
			
			modalCadastroFiador("CPF");
		};
	
		function popupCadastroFiadorCNPJ() {
			
			$("#tabSocio").show();
			$('#tabs').tabs('select', 0);
			
			$("#cadastroCnpj").show();
			$("#cadastroCpf").hide();
			
			modalCadastroFiador("CNPJ");
		};
		
		function modalCadastroFiador(paramCpfCnpj){
			
			fecharModalCadastroFiador = false;
			
			limparCamposCadastroFiador();
			
			$("#dialog-fiador").dialog({
				resizable: false,
				height:610,
				width:840,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						if (paramCpfCnpj == "CPF") {
							cadastrarFiadorCpf(this);
						} else {
							cadastrarFiadorCnpj(this);
						}
					},
					"Cancelar": function() {
						
						$(this).dialog("close");
					}
				},
				beforeClose: function(event, ui) {
					
					if (!fecharModalCadastroFiador){
						cancelarCadastro();
						
						return fecharModalCadastroFiador;
					}
					
					return fecharModalCadastroFiador;
				}
			});
		
			$(".trSocioPrincipal").hide();
		}
		
		function cancelarCadastro(){
			$("#dialog-cancelar-cadastro-fiador").dialog({
				resizable: false,
				height:150,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON("<c:url value='/cadastro/fiador/cancelarCadastro'/>", null, 
							function(result){
								fecharModalCadastroFiador = true;
								$("#dialog-close").dialog("close");
								$("#dialog-fiador").dialog("close");
								$("#dialog-cancelar-cadastro-fiador").dialog("close");
								$('[name="cpfFiador"]').removeAttr("disabled");
								$('[name="cpfConjuge"]').removeAttr("disabled");
								$("#cnpjFiador").removeAttr("disabled");
								
								limparCamposCadastroFiador();
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
						fecharModalCadastroFiador = false;
					}
				}
			});
		}
	
		function popup_excluir() {
			$( "#dialog-excluir" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						cadastrarFiadorCnpj(this);
					},
					"Cancelar": function() {
						$(this).dialog( "close" );
					}
				}
			});
		};
		
		function exibirGridFiadoresCadastrados(){
			
			var data = "filtro.nome=" + $("#nomeFiadorPesquisa").val() + "&filtro.cpfCnpj=" + $("#cpfCnpjFiadorPesquisa").val();
			$.postJSON("<c:url value='/cadastro/fiador/pesquisarFiador'/>", data, 
				function(result){
					
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".pessoasGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$("#gridFiadoresCadastrados").show();
					} else {
						$("#gridFiadoresCadastrados").hide();
					}
				}
			);
		}
		
		$(function() {
			$("#tabs").tabs();
			
			$(".pessoasGrid").flexigrid({
				preProcess: processarResultadoConsultaFiadores,
				dataType : 'json',
				colModel : [  {
					display : 'Código',
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
					display : 'RG / Inscrição Estadual',
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
					display : 'Ação',
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
			
			$(".pessoasGrid").flexOptions({url: "<c:url value='/cadastro/fiador/pesquisarFiador'/>"});
		});
		
		function processarResultadoConsultaFiadores(data){
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

				data.rows[i].cell[lastIndex] = getActionsConsultaFiadores(data.rows[i].id);
			}

			$('.imoveisGrid').show();
			
			if (data.result){
				return data.result[1];
			}
			return data;
		}
		
		function getActionsConsultaFiadores(idFiador){
			return '<a href="javascript:;" onclick="editarFiador(' + idFiador + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar Fiador">' +
					'<img src="/nds-client/images/ico_editar.gif" border="0px"/>' +
					'</a>' +
					'<a href="javascript:;" onclick="excluirFiador(' + idFiador + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir Fiador">' +
					'<img src="/nds-client/images/ico_excluir.gif" border="0px"/>' +
					'</a>';
		}
		
		function editarFiador(idFiador){
			$.postJSON("<c:url value='/cadastro/fiador/editarFiador' />", "idFiador=" + idFiador, 
				function(result) {
					
					$(".inicioAtividadeNovo").hide();
					$(".inicioAtividadeEdicao").show();
					
					limparCamposCadastroFiador();
				
					if (result[0] == "CPF"){
						
						popupCadastroFiadorCPF();
						
						$("#nomeFiadorCpf").val(result[1]);
						$("#emailFiadorCpf").val(result[2]);
						$("#cpfFiador").val(result[3]);
						$("#rgFiador").val(result[4]);
						$("#dataNascimentoFiadorCpf").val(result[5]);
						$("#orgaoEmissorFiadorCpf").val(result[6]);
						$("#selectUfOrgaoEmiCpf").val(result[7]);
						$("#estadoCivilFiadorCpf").val(result[8]);
						$("#selectSexoFiador").val(result[9]);
						$("#nacionalidadeFiadorCpf").val(result[10]);
						$("#naturalFiadorCpf").val(result[11]);
						
						if ($("#estadoCivilFiadorCpf").val() == "CASADO"){
							
							opcaoCivilPf("CASADO");
							
							$("#nomeConjugeCpf").val(result[12]);
					        $("#emailConjugeCpf").val(result[13]);
					        $("#cpfConjuge").val(result[14]);
					        $("#rgConjuge").val(result[15]);
					        $("#dataNascimentoConjugeCpf").val(result[16]);
					        $("#orgaoEmissorConjugeCpf").val(result[17]);
					        $("#selectUfOrgaoEmiConjugeCpf").val(result[18]);
					        $("#selectSexoConjuge").val(result[19]);
					        $("#nacionalidadeConjugeCpf").val(result[20]);
					        $("#naturalConjugeCpf").val(result[21]);
					        
					        $(".inicioAtividadeEdicao").text(result[22]);
					        
						} else {
							$(".inicioAtividadeEdicao").text(result[12]);
						}
						
						$('[name="cpfFiador"]').attr("disabled", true);
						$('[name="cpfConjuge"]').attr("disabled", true);
					} else {
						
						popupCadastroFiadorCNPJ();
						
						$("#razaoSocialFiador").val(result[1]);
						$("#nomeFantasiaFiador").val(result[2]);
						$("#inscricaoEstadualFiador").val(result[3]);
						$("#cnpjFiador").val(result[4]);
						$("#emailFiadorCnpj").val(result[5]);
						$(".inicioAtividadeEdicao").text(result[6]);
						
						$("#cnpjFiador").attr("disabled", true);
					}
				}
			);
		}
		
		function excluirFiador(idFiador){
			$(".dialog-excluir-fiador").dialog({
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
									$(".pessoasGrid").flexAddData({
										page: result[1].page, total: result[1].total, rows: result[1].rows
									});
									
									$("#gridFiadoresCadastrados").show();
								} else {
									$("#gridFiadoresCadastrados").hide();
								}
							}
						);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$(".dialog-excluir-fiador").show();
		}
		
		function limparCamposCadastroFiador(){
			//dados cadastrais cpf
			limparDadosCadastraisCPF(0);
	        
	        //dados cadastrais cnpj
			limparDadosCadastraisCNPJ();
	        
			//dados cadastrais socios
			limparDadosCadastraisCPF(1);
		    
		    //endereços
			ENDERECO_FIADOR.limparFormEndereco();
		    
		    //telefones
		    FIADOR.limparCamposTelefone();
		    
		    //garantias
		    limparCamposGarantias();
		    
		    //cotas associadas
		    limparCamposCotasAssociadas();
		    
		    opcaoCivilPf("");
		}
	</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
		
	<style>
		.diasFunc label, .finceiro label{ vertical-align:super;}
	</style>
</head>

<body>
	
	<div id="dialog-excluir-fiador" class="dialog-excluir-fiador" title="Fiadores" style="display: none;">
		<p>Confirma esta Exclusão?</p>
	</div>
	
	<div id="dialog-cancelar-cadastro-fiador" title="Fiadores" style="display: none;">
		<p>Dados não salvos serão perdidos. Confirma o cancelamento?</p>
	</div>
	<div id="dialog-fiador" title="Novo Fiador" style="display: none;">
	
		<jsp:include page="../messagesDialog.jsp" />
	
		<div id="tabs">
			<ul>
				<li><a href="#tab-1">Dados Cadastrais</a></li>
				<li id="tabSocio"><a href="#tab-2" onclick="$('.trSocioPrincipal').show();carregarSocios();" >Sócios</a></li>
	            <li><a href="#tab-3" onclick="ENDERECO_FIADOR.popularGridEnderecos();">Endereços</a></li>
	            <li><a href="#tab-4" onclick="FIADOR.carregarTelefones();">Telefones</a></li>
	            <li><a href="#tab-5" onclick="carregarGarantias();">Garantia</a></li>
				<li><a href="#tab-6" onclick="carregarCotasAssociadas();">Cotas Associadas</a></li>
			</ul>
			
	        <div id="tab-1">
	        	<div id="cadastroCnpj" style="display: none;">
					<jsp:include page="dadosCadastraisCnpj.jsp"></jsp:include>
				</div>
				
				<div id="cadastroCpf" style="display: none;">
					<jsp:include page="dadosCadastraisCpf.jsp"></jsp:include>
				</div>
	        </div>
	        
	        <div id="tab-2">
				<jsp:include page="socios.jsp"></jsp:include>
			</div>
	        
			<div id="tab-3">
				<jsp:include page="../endereco/index.jsp">
					<jsp:param value="ENDERECO_FIADOR" name="telaEndereco"/>
				</jsp:include>
	    	</div>
	    	
	        <div id="tab-4">
	        	<jsp:include page="../telefone/index.jsp">
	        		<jsp:param value="FIADOR" name="tela"/>
	        	</jsp:include>
			</div>
			
			<div id="tab-5">
				<jsp:include page="garantias.jsp"></jsp:include>
	    	</div>
	    
			<div id="tab-6">
				<jsp:include page="cotasAssociadas.jsp"></jsp:include>
	    	</div>
		</div>
	</div>
	
	<fieldset class="classFieldset">
   		<legend> Pesquisar Fiador</legend>
        	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="41">Nome:</td>
              		<td colspan="3">
              			<input type="text" name="textfield2" id="nomeFiadorPesquisa" style="width:180px;" maxlength="255"/>
              		</td>
                	<td width="68">CPF/CNPJ:</td>
                	<td width="477">
                		<input type="text" name="textfield" id="cpfCnpjFiadorPesquisa" style="width:130px;" maxlength="255"/>
                	</td>
              		<td width="104">
              			<span class="bt_pesquisar"><a href="javascript:exibirGridFiadoresCadastrados();">Pesquisar</a></span>
              		</td>
            	</tr>
          	</table>
	</fieldset>
    
    <div class="linha_separa_fields">&nbsp;</div>
    
    <fieldset class="classFieldset">
		<legend>Fiadores Cadastrados</legend>
        	<div class="grids" style="display:none;" id="gridFiadoresCadastrados">
        		<table class="pessoasGrid"></table>
        	</div>

            <span class="bt_novos" title="Novo">
            	<a href="javascript:;" onclick='$(".inicioAtividadeNovo").show();$(".inicioAtividadeEdicao").hide();popupCadastroFiadorCPF();'><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CPF</a>
            </span>
        	
        	<span class="bt_novos" title="Novo">
        		<a href="javascript:;" onclick="popupCadastroFiadorCNPJ();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CNPJ</a>
        	</span>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
</body>