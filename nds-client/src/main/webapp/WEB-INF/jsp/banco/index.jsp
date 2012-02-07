<body>
<div id="dialog-excluir" title="Excluir Banco">
	<p>Confirma a exclusão deste Banco?</p>
</div>

<div id="dialog-novo" title="Incluir Novo Banco">
     
	<label><strong>Dados de Bancos</strong></label>
    
    <table width="626" border="0" cellpadding="2" cellspacing="1">
            <tr>
              <td width="111">Número Banco:</td>
              <td width="216"><input type="text" name="textfield31" id="textfield32" style="width:150px;"/></td>
                <td width="73">Nome:</td>
              <td width="205"><input type="text" name="textfield29" id="textfield30" style="width:177px;"/></td>
            </tr>
            <tr>
              <td>Código Cedente:</td>
              <td><input type="text" name="textfield30" id="textfield31" style="width:150px;"/></td>
              <td>Agência:</td>
              <td><input type="text" name="textfield32" id="textfield33" style="width:177px;"/></td>
            </tr>
            <tr>
              <td>Conta / Digito:</td>
              <td><input type="text" name="textfield28" id="textfield28" style="width:97px;"/>
-
  <input type="text" name="textfield28" id="textfield29" style="width:37px;"/></td>
              <td>Moeda:</td>
              <td><input type="text" name="textfield15" id="textfield15" style="width:80px;"/></td>
            </tr>
            <tr>
              <td>Carteira:</td>
              <td><input type="text" name="textfield33" id="textfield34" style="width:150px;"/></td>
              <td>Juros %:</td>
              <td><input type="text" name="textfield16" id="textfield16" style="width:80px;"/></td>
            </tr>
            <tr>
              <td>Status:</td>
              <td><input name="statusBco" type="checkbox" value="" checked="checked" id="statusBco" /><label for="statusBco">Ativo</label></td>
              <td>Multa %:</td>
              <td><input type="text" name="textfield17" id="textfield17" style="width:80px;"/></td>
            </tr>
            <tr>
              <td>Instruções:</td>
              <td colspan="3"><textarea name="textfield18" id="textfield18" style="width:477px;"></textarea></td>
            </tr>
          </table>
    </div>




<script>
	$(".bancosGrid").flexigrid({
			url : '../xml/bancos-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'banco',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Agência / Dígito',
				name : 'agencia',
				width : 90,
				sortable : true,
				align : 'left',
			}, {
				display : 'Conta-Corrente / Dígito',
				name : 'contaCorrente',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cedente',
				name : 'cedente',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Moeda',
				name : 'moeda',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Carteira',
				name : 'carteira',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "Nome",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
</script>
</body>