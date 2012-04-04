<head>
	<script language="javascript" type="text/javascript">
		function popup_cpf() {
			$("#tabSocio").hide();
			$('#tabs').tabs('select', 0);
			
			$("#cadastroCnpj").hide();
			$("#cadastroCpf").show();
			
			modalCadastroFiador();
		};
	
		function popup_cnpj() {
			$("#tabSocio").show();
			$('#tabs').tabs('select', 0);
			
			$("#cadastroCnpj").show();
			$("#cadastroCpf").hide();
			
			modalCadastroFiador();
		};
		
		function modalCadastroFiador(){
			$("#dialog-fiador").dialog({
				resizable: false,
				height:610,
				width:840,
				modal: true,
				buttons: {
					"Confirmar": function() {
						cadastrarFiadorCpf(this);
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$(".trSocioPrincipal").hide();
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
						$( this ).dialog( "close" );
					}
				}
			});
		};
		
		function exibirGridFiadoresCadastrados(){
			$("#gridFiadoresCadastrados").show();
		}
		
		$(function() {
			$("#tabs").tabs();
			
			$(".pessoasGrid").flexigrid({
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
					name : 'cpf',
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
					sortable : true,
					align : 'center'
				}],
				sortname : "nome",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
			});
		});
	</script>
	
	<style>
		.diasFunc label, .finceiro label{ vertical-align:super;}
	</style>
</head>

<body>
	<div id="dialog-fiador" title="Novo Fiador" style="display: none;">
	
		<div id="tabs">
			<ul>
				<li><a href="#tab-1">Dados Cadastrais</a></li>
				<li id="tabSocio"><a href="#tab-2" onclick="$('.trSocioPrincipal').show();" >Sócios</a></li>
	            <li><a href="#tab-3">Endereços</a></li>
	            <li><a href="#tab-4" onclick="carregarTelefones();">Telefones</a></li>
	            <li><a href="#tab-5">Garantia</a></li>
				<li><a href="#tab-6">Cotas Associadas</a></li>
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
				<jsp:include page="../endereco/index.jsp"></jsp:include>
	    	</div>
	    	
	        <div id="tab-4">
	        	<jsp:include page="../telefone/index.jsp"></jsp:include>
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
              			<input type="text" name="textfield2" id="textfield2" style="width:180px;"/>
              		</td>
                	<td width="68">CPF/CNPJ:</td>
                	<td width="477"><input type="text" name="textfield" id="textfield" style="width:130px;"/></td>
              		<td width="104">
              			<span class="bt_pesquisar"><a href="javascript:;" onclick="exibirGridFiadoresCadastrados();">Pesquisar</a></span>
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
            	<a href="javascript:;" onclick="popup_cpf();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CPF</a>
            </span>
        	
        	<span class="bt_novos" title="Novo">
        		<a href="javascript:;" onclick="popup_cnpj();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CNPJ</a>
        	</span>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
</body>