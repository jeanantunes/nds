<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/alteracaoCota.js"></script>

	<style>
		.diasFunc label, .finceiro label{ vertical-align:super;}
		#tabs-4 .especialidades fieldset{width:220px!important; margin-left: -16px; width: 258px !important;}
		#tabs-4 .bt_novos, #tabs-4 .bt_confirmar_novo{margin-left:-14px!important;}
		
		.associacao{width:818px!important; margin-left:-11px!important;}
		.semanal, .quinzenal, .mensal{display:none;}
		.linha_separa_fields{width:700px;}
	</style>	
</head>

<body>

	<fieldset class="classFieldset">
   		<legend> Pesquisar         </legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
        	<tr>
            	<td>Cota:</td>
              	<td colspan="3"><input type="text" name="textfield4" id="textfield4" style="width:80px;"/></td>
              	<td>Nome:</td>
              	<td><input type="text" name="textfield6" id="textfield6" style="width:153px;"/></td>
              	<td>Cidade:</td>
              	<td colspan="3">
              		<select name="select3" id="select" style="width:270px;">
              		</select>
              	</td>
              	<td>&nbsp;</td>
            </tr>
            <tr>
              	<td width="74">Fornecedor:</td>
              	<td colspan="3">
              		<select name="filtro.idFornecedor" style="width:100px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listFornecedores}" var="fornecedor">
							<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
						</c:forEach>
					</select>
              	</td>
                <td width="73">Bairro:</td>
              	<td width="167">
              		<select name="select5" id="select4" style="width:160px;">
              		</select>
              	</td>
                <td width="85">Municipio:</td>
                <td colspan="3">
                	<select name="select6" id="select5" style="width:270px;">
              		</select>
              	</td>
              	<td width="109">&nbsp;</td>
            </tr>
            <tr>
              	<td>Desconto:</td>
              	<td colspan="3">
              		<select name="select7" id="select6" style="width:100px;">
              		</select>
              	</td>
              	<td>Vencimento:</td>
              	<td>
              		<select name="select8" id="select7" style="width:160px;">
              		</select>
              	</td>
              	<td>Valor M&iacute;nimo:</td>
              	<td width="106">
              		<select name="select9" id="select8" style="width:80px;">
              		</select>
              	</td>
              	<td width="76">Tipo Entrega:</td>
              	<td width="102">
              		<select name="select10" id="select9" style="width:80px;">
              		</select>
              	</td>
              	<td><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
            </tr>
		</table>
	</fieldset>
    
    <div class="linha_separa_fields">&nbsp;</div>
    
    <fieldset class="classFieldset">
       	<legend>Resultado da Pesquisa</legend>
        <div class="grids" style="display:none;">
       		<table class="alteracaoGrid"></table>
            <table width="950" border="0" cellspacing="0" cellpadding="0">
  				<tr>
    				<td width="502"><span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="../images/ico_editar.gif" hspace="5" border="0"/>Alterar</a></span></td>
    				<td width="168"><strong>Total de Cotas Selecionadas:</strong></td>
    				<td width="141">4</td>
    				<td width="91">Selecionar Todos</td>
    				<td width="48"><input type="checkbox" name="checkbox3" id="checkbox3" /></td>
  				</tr>
			</table>
        </div>
    </fieldset>






	<script type="text/javascript">
		$(function(){
			alteracaoCotaController.init();
		});
	</script>

</body>