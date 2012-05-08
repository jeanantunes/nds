<head>
<title>NDS - Novo Distrib</title>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/pdv.js"></script>

<script language="javascript" type="text/javascript">
	
	function popup_cnpj() {
		
		$( "#dialog-cnpj" ).dialog({
			resizable: false,
			height:640,
			width:840,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}
	
	function limparFormsTabs() {
		
		ENDERECO_COTA.limparFormEndereco();
		COTA.limparCamposTelefone();

	}

	function editarCota() {
		
		var idCota = prompt("Digite o id da cota a ser editada.");
		
		if (idCota) {
			
			popup_cpf(idCota);
			
			$("#_idCotaRef").val(idCota);
		}
	}
	
    function novaCota() {
		    	
		var idCota = prompt("Digite o id da nova cota.");
		
		if (idCota) {
			
			PDV.idCota =  idCota;
			
			popup_cpf(idCota);
			
			$("#_idCotaRef").val(idCota);
		}
	}
	
	function popup_cpf(idCota) {

		if (idCota) {

			$.ajax({
				type: 'POST',
				url: '<c:url value="/cadastro/cota/editarCota" />',
				data: { "idCota": idCota }
			});
		
		} else {
			
			$.ajax({
				type: 'POST',
				url: '<c:url value="/cadastro/cota/novaCota" />'
			});
		}

		$( "#dialog-cpf" ).dialog({
			resizable: false,
			height:640,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {
					salvarCota();
					$( this ).dialog( "close" );
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$.postJSON(
						'<c:url value="/cadastro/cota/cancelar" />',
						null,
						function() {
							limparFormsTabs();
							$("#tabpf").tabs('select', 0);
							$( this ).dialog( "close" );
						},
						null, 
						true
					);
				}
			}
		});

		$(".ui-dialog-titlebar-close").click(function() {

			$("#tabpf").tabs('select', 0);
			limparFormsTabs();
		});
	}
	  
	function salvarCota(){
		
		$.ajax({
			type: 'POST',
			url: '<c:url value="/cadastro/cota/salvarCota" />',
			data: { "idCota" : $("#_idCotaRef").val() }
		});

		limparFormsTabs();
		
		$("#tabpf").tabs('select', 0);
		
		$( this ).dialog( "close" );
	}
	
	$(function() {
		
		$( "#tabpf" ).tabs();
		
		$( "#tabpj" ).tabs();
		
		$( "#tabpdv" ).tabs();
	});
	
	function carregarPDV(){
		
		var idCota = $("#_idCotaRef").val();
		
		PDV.idCota = idCota;
		PDV.pesquisarPdvs(idCota);
	}
	
</script>
<style>

	.diasFunc label, .finceiro label, .materialPromocional label{ vertical-align:super;}
	.complementar label, .distribuicao label{ vertical-align:super; margin-right:5px; margin-left:5px;}
	
	#dialog-pdv fieldset{width:777px!important; margin-bottom:10px;  margin-left: -11px;}
	
	#tabpj-5 fieldset, #tabpf-5 fieldset {width:755px!important;}
	
		
</style>

</head>

<body>

	<fieldset class="classFieldset">
	
		<legend>Cotas Cadastradas</legend>
	
		<div class="grids" style="display: none;">
			<table class="pessoasGrid"></table>
		</div>

		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="novaCota();">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo CPF
			</a>
		</span>
		
		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="editarCota();">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Editar CPF
			</a>
		</span>

	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

	<div id="dialog-cpf" title="Nova Cota">

		<jsp:include page="../messagesDialog.jsp" />

		<div id="tabpf">
			<ul>
			<li><a href="#tabpf-1">Dados Cadastrais</a></li>
			<li><a href="#tabpf-2" onclick="ENDERECO_COTA.popularGridEnderecos()">Endereços</a></li>			
			<li><a href="#tabpf-3" onclick="COTA.carregarTelefones()">Telefones</a></li>
			<li><a href="#tabpf-4" onclick="carregarPDV()">PDV</a></li>
			<li><a href="#tabpf-5" onclick="void(0);">Garantia</a></li>
			<li><a href="#tabpf-6" onclick="carregaFinanceiro();">Financeiro</a></li>
			<li><a href="#tabpf-7">Bancos</a></li>
			<li><a href="#tabpf-8">Distribuição</a></li>
			<li><a href="#tabpf-9">Fornecedor</a></li>
			</ul>
		
			<div id="tabpf-1"> </div>
			
			<div id="tabpf-2">
				<jsp:include page="../endereco/index.jsp">
					<jsp:param value="ENDERECO_COTA" name="telaEndereco"/>
				</jsp:include>
			</div>
			
			<div id="tabpf-3">
				<jsp:include page="../telefone/index.jsp">
					<jsp:param value="COTA" name="tela"/>
				</jsp:include>
			</div>

			<div id="tabpf-4">
				 <jsp:include page="../pdv/index.jsp"></jsp:include>
			</div> 
			<div id="tabpf-5"> 
				<jsp:include page="../cotaGarantia/index.jsp"></jsp:include>
			</div>

			<div id="tabpf-6"> 
			    <jsp:include page="../financeiro/index.jsp"></jsp:include> 
			</div>
			
			<div id="tabpf-7"> </div>
			
			<div id="tabpf-8"> </div>
			
			<div id="tabpf-9"> </div>
		
		</div>
		
	</div>
	<input type="hidden" id="_idCotaRef"/>
</body>
