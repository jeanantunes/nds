<head>
	<script language="javascript" type="text/javascript">
		function popup_cpf() {
			$("#tabSocio").hide();
			$('#tabs').tabs('select', 0);
			$("#dialog-fiador").dialog({
				resizable: false,
				height:610,
				width:840,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						$("#effect").show("highlight", {}, 1000, callback);
						$(".grids").show();
						
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
		};
	
		function popup_cnpj() {
			$("#tabSocio").show();
			$('#tabs').tabs('select', 0);
			$("#dialog-fiador").dialog({
				resizable: false,
				height:610,
				width:840,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
						$("#effect").show("highlight", {}, 1000, callback);
						$(".grids").show();
						
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
		};
	
		function popup_excluir() {
			$( "#dialog-excluir" ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
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
			
			$(".imoveisGrid").flexigrid({
				dataType : 'json',
				colModel : [ {
					display : 'Descrição',
					name : 'descricao',
					width : 510,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor R$',
					name : 'valor',
					width : 130,
					sortable : true,
					align : 'right'
				},  {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				width : 770,
				height : 150
			});


			$(".cotasCadastradasGrid").flexigrid({
				dataType : 'json',
				colModel : [  {
					display : 'Cota',
					name : 'cota',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Nome',
					name : 'nome',
					width : 620,
					sortable : true,
					align : 'left'
				}, {
					display : '',
					name : 'sel',
					width : 30,
					sortable : true,
					align : 'center'
				}],
				width : 770,
				height : 150
			});
				
			$(".sociosGrid").flexigrid({
				dataType : 'json',
				colModel : [  {
					display : 'Nome',
					name : 'nome',
					width : 580,
					sortable : true,
					align : 'left'
				}, {
					display : 'Principal',
					name : 'principal',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : true,
					align : 'center'
				}],
				width : 770,
				height : 160
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
				<li id="tabSocio"><a href="#tab-2">Sócios</a></li>
	            <li><a href="#tab-3">Endereços</a></li>
	            <li><a href="#tab-4" onclick="carregarTelefones();">Telefones</a></li>
	            <li><a href="#tab-5">Garantia</a></li>
				<li><a href="#tab-6">Cotas Associadas</a></li>
			</ul>
			
	        <div id="tab-1">
				<table width="754" cellpadding="2" cellspacing="2" style="text-align:left;">
					<tr>
						<td nowrap="nowrap"><strong>Início de Atividade:</strong></td>
						<td>13/04/1998</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td width="120">Razão Social:</td>
						<td width="240"><input type="text" style="width:230px " /></td>
						<td width="136">Nome Fantasia:</td>
						<td width="230"><input type="text" style="width:230px " /></td>
	    			</tr>
	    			<tr>
						<td>Inscrição Estadual:</td>
						<td><input type="text" style="width:230px " /></td>
						<td>CNPJ:</td>
						<td><input type="text" style="width:230px " /></td>
	    			</tr>
	    			<tr>
						<td>E-mail:</td>
						<td><input type="text" style="width:230px" /></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
	     		</table>
	      		<br />
	        </div>
	        
	        <div id="tab-2">
				<table width="765" cellpadding="2" cellspacing="2" style="text-align:left;">
					<tr>
						<td width="108">Nome:</td>
						<td width="274"><input type="text" style="width:230px " /></td>
						<td width="118">E-mail:</td>
						<td colspan="3"><input type="text" style="width:230px" /></td>
					</tr>
	          		<tr>
						<td>CPF:</td>
						<td><input type="text" style="width:150px" /></td>
						<td>R. G.:</td>
						<td colspan="3"><input type="text" style="width:150px" /></td>
	          		</tr>
	          		<tr>
						<td>Data Nascimento:</td>
						<td><input type="text" style="width:150px" /></td>
						<td>Orgão Emissor:</td>
						<td width="63"><input type="text" style="width:50px" /></td>
						<td width="26">UF:</td>
						<td width="136">
							<select name="select6" id="select6" style="width:50px">
	              				<option selected="selected"> </option>
								<option>RJ</option>
								<option>SP</option>
	            			</select>
	            		</td>
	          		</tr>
					<tr>
						<td>Estado Civil:</td>
						<td>
							<select name="select4" style="width:155px;" onchange="opcaoCivil(this.value);">
								<option selected="selected">Selecione...</option>
								<option value=" ">Solteiro</option>
								<option value="1">Casado</option>
								<option value=" ">Divorciado</option>
								<option value=" ">Víuvo</option>
				            </select>
				        </td>
				        <td>Sexo:</td>
	            		<td colspan="3">
	            			<select name="select3" id="select3" style="width:150px">
								<option selected="selected">Selecione... </option>
								<option>Masculino</option>
								<option>Feminino</option>
							</select>
						</td>
	          		</tr>
	          		<tr>
						<td>Nacionalidade:</td>
						<td><input type="text" style="width:150px" /></td>
						<td>Natural:</td>
						<td colspan="3"><input type="text" style="width:150px" /></td>
	          		</tr>
	          		<tr>
			            <td>Principal:</td>
			            <td><input type="checkbox" name="checkbox" id="checkbox" /></td>
			            <td>&nbsp;</td>
			            <td colspan="3">&nbsp;</td>
	          		</tr>
				</table>
				
				<div id="divOpcao10" style="display:none; margin-left:5px; margin-top:5px;">
					<strong>Dados do Conjuge</strong>
					<table width="760" cellpadding="2" cellspacing="2" style="text-align:left;">
	          			<tr>
				            <td width="108">Nome:</td>
				            <td width="274"><input type="text" style="width:230px " /></td>
				            <td width="118">E-mail:</td>
				            <td colspan="3"><input type="text" style="width:230px" /></td>
	          			</tr>
	          			<tr>
				            <td>CPF:</td>
				            <td><input type="text" style="width:150px" /></td>
				            <td>R. G.:</td>
				            <td colspan="3"><input type="text" style="width:175px" /></td>
	          			</tr>
	          			<tr>
				            <td>Data Nascimento:</td>
				            <td><input type="text" style="width:150px" /></td>
				            <td>Orgão Emissor:</td>
				            <td width="63"><input type="text" style="width:50px" /></td>
				            <td width="26">UF:</td>
				            <td width="136">
				            	<select name="select6" id="select6" style="width:50px">
		              				<option selected="selected"> </option>
									<option>RJ</option>
									<option>SP</option>
		            			</select>
		            		</td>
	          			</tr>
		          		<tr>
		            		<td>Sexo:</td>
		            		<td>
		            			<select name="select2" id="select2" style="width:150px">
									<option selected="selected">Selecione... </option>
									<option>Masculino</option>
									<option>Feminino</option>
								</select></td>
		            		<td>Nacionalidade:</td>
		            		<td colspan="3">
		            			<input type="text" style="width:150px" />
		            		</td>
		          		</tr>
		          		<tr>
		            		<td>Natural:</td>
		            		<td>
		            			<input type="text" style="width:175px" />
		            		</td>
		            		<td>&nbsp;</td>
		            		<td colspan="3">&nbsp;</td>
		          		</tr>
					</table>
				</div>
	      		<br />
				<span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span>
	 			<br />
				<br />
	      		<br clear="all" />
				<strong>Sócios Cadastrados</strong>
				<br />
				<table class="sociosGrid"></table>
			</div>
	        
			<div id="tab-3">
				<jsp:include page="../endereco/index.jsp"></jsp:include>
	    	</div>
	    	
	        <div id="tab-4">
	        	<jsp:include page="../telefone/index.jsp"></jsp:include>
			</div>
			
			<div id="tab-5">
				<table width="750" cellpadding="2" cellspacing="2" style="text-align:left; display:s;" class="fiadorPF">
	        		<tr>
						<td>Valor R$:</td>
						<td><input type="text" style="width:100px" /></td>
	          		</tr>
	        		<tr>
						<td>Descrição:</td>
						<td>
							<textarea name="textarea2" rows="4" style="width:600px">
							</textarea>
						</td>
	        		</tr>
	        		<tr>
						<td>&nbsp;</td>
	          			<td>
	          				<span class="bt_add"><a href="javascript:;">Incluir Novo</a></span>
	          			</td>
	        		</tr>
	        	</table>
	      		<br />
	        	<label><strong>Garantias Cadastradas</strong></label><br />
	        	<table class="imoveisGrid"></table>
	    	</div>
	    
			<div id="tab-6">
				<table width="280" cellpadding="2" cellspacing="2" style="text-align:left;">
					<tr>
						<td width="46">Cota:</td>
						<td width="218"><input type="text" style="width:80px; float:left; margin-right:5px;"/><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
					</tr>
	        		<tr>
						<td>Nome:</td>
						<td><input type="text" style="width:200px" /></td>
	          		</tr>
	          		<tr>
						<td>&nbsp;</td>
						<td><span class="bt_add"><a href="javascript:;">Incluir Novo</a></span></td>
	          		</tr>
	        	</table>
	      		<br />
	        	<label><strong>Cotas Cadastradas</strong></label>
	        	<br />
	        	<table class="cotasCadastradasGrid"></table>
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