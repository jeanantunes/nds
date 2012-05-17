<head>
	
	<script>

		$(function() {
			
			inicializar();
		});
		
		function iniciarGrid() {
			$(".serviceGrid").flexigrid({
				dataType : 'json',
				colModel : [ {
					display : 'C�digo',
					name : 'codigo',
					width : 20,
					sortable : true,
					align : 'left'
				}, {
					display : 'Descri��o',
					name : 'descricao',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Taxa R$',
					name : 'taxa',
					width : 20,
					sortable : true,
					align : 'right'
				}, {
					display : 'Isen��o',
					name : 'isencao',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Base de C�lculo',
					name : 'baseCalculo',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : '% Calculo sobre Base',
					name : 'calculoSobreBase',
					width : 100,
					sortable : true,
					align : 'right'
				}, {
					display : 'A��o',
					name : 'cpfRemetente',
					width : 100,
					sortable : true,
					align : 'center'
				}],
				sortname : "descricao",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180,
				singleSelect : true
			});
		}
		
		function inicializar() {
			
			iniciarGrid();
		}
		
		function pesquisar() {
	
			var codigo = $("#codigo").val();
			var descricao = $("#descricao").val();
			var periodicidade = $("#periodicidade").val();
	
			$(".serviceGrid").flexOptions({
				url: "<c:url value='/servico/cadastroServico/pesquisarServicos' />",
				params: [{name:'codigo', value: codigo },
					     {name:'descricao', value: descricao },
					     {name:'periodicidade', value: periodicidade }],
				newp: 1,
			});
			
			$(".serviceGrid").flexReload();
		}
		
	</script>
	
	
	<style>
		label{ vertical-align:super;}
		#dialog-novo label{width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px;}
		#dialog-novo select, #dialog-novo input {
		    float: right!important;
		}
	</style>

</head>

<body>

	<div id="dialog-excluir" title="Excluir Servi�o de Entrega">
		<p>Confirma a exclus�o deste Servi�o de Entrega?</p>
	</div>

	<!--modal 
	<div id="dialog-novo" title="Incluir Novo Servi�o de Entrega">
    
    	<label>C�digo:<input name="codigoCadastro" type="text" style="width:250px;" /></label>
    	
    	<br clear="all" />
    
    	<label>Descri��o:<input name="descricaoCadastro" type="text" style="width:250px;" /></label>
    	
    	<br clear="all" />
    	
    	<label>Taxa Fixa R$:<input name="taxaFixaCadastro" type="text" style="width:250px;" /></label>
    
    	<br clear="all" />
    	
    	<label>
    		<span style="float:left!important;" >Possibilita Isen��o:</span>
    		<input name="isIsento" type="checkbox" value="false" style="float:left!important;" />
    	</label>
    	
    	<br clear="all" />
    	
    	<label>Periodicidade:
		    <select name="periodicidadeCadastro" style="width:257px;">
			    <option value="" selected="selected">Selecione...</option>
			    <option>Di�rio</option>
			    <option>Semanal</option>
			    <option>Mensal</option>
		    </select>
	    
    	</label>
    	
    	<br clear="all" />
	    
	    <label>Base de C�lculo:
		    <select name="seelct" style="width:257px;">
			    <option value="" selected="selected">Selecione...</option>
			    <option>Faturamento Bruto</option>
			    <option>Faturamento L�quido</option>
		    </select>
	    </label>
	    
	    <br clear="all" />

    
    	<label>
    		<span style="float:left!important;" >(%) para calculo sobre base:</span>
    		<input name="percentualCalculoBase" type="text" style="float:left!important; width:70px; margin-left:5px;" />
    	</label>
 
	    <br />
	    
	    <br />
	  	
	  	<br clear="all" />
	
		<span class="bt_add">
			<a href="javascript:;" onclick="popup();">Incluir Novo</a>
		</span> 
	</div>
	-->
	
	<!-- pesquisa -->	
    <fieldset class="classFieldset">
    	<legend> Pesquisar Servi�os de Entrega</legend>
   		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="46">C�digo:</td>
				<td width="118" ><input type="text" name="codigo" id="codigo" style="width:100px;"/></td>
				
				<td width="63">Descri��o:</td>
				<td width="250"><input type="text" name="descricao" id="descricao" style="width:222px;"/></td>
				
				<td width="82">Periodicidade:</td>
				<td width="251">
					<select name="periodicidade" id="periodicidade" style="width:120px;">
						<option value="" selected="selected">Selecione...</option>
						<option value="D" >Di�rio</option>
						<option value="S" >Semanal</option>
						<option value="M" >Mensal</option>
					</select>
				</td>
				<td width="104">
					<span class="bt_pesquisar" title="Pesquisar Servi�o">
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>

	<!-- GRID PAGINACAO -->
	<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="classFieldset">
	 		<legend>Servi�os de Entrega Cadastrados</legend>
			
			<div class="grids" style="display:none;">
				<table class="serviceGrid"></table>
			</div>
	
			<span class="bt_novos" title="Novo">
				<a href="javascript:;" onclick="popup();">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
					Novo
				</a>
			</span>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
	   
	</div>

</body>