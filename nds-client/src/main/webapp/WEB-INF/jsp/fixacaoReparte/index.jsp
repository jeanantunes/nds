
<script type="text/javascript">
function porCota(){
	$('.porCota').show();
	$('.porExcessao').hide();
}
function porExcessao(){
	$('.porCota').hide();
	$('.porExcessao').show();
}
function filtroPorCota(){
	$('.filtroPorCota').show();
	$('.filtroPorProduto').hide();
	$('.porExcessao').hide();
}
function filtroPorProduto(){
	$('.filtroPorCota').hide();
	$('.filtroPorProduto').show();
	$('.porCota').hide();
}
</script>

<body>
<br clear="all"/>
    <br />
    
    
    <fieldset class="classFieldset">
   	    <legend> Pesquisar Fixação</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="22" align="right"><input type="radio" name="radio" id="radio" value="radio" onclick="filtroPorCota();" /></td>
            <td width="50">Cota</td>
            <td width="22"><input type="radio" name="radio" id="radio2" value="radio" onclick="filtroPorProduto()" /></td>
            <td width="49">Produto</td>
            <td width="781"><table width="760" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorProduto" style="display:none;">
          <tr>
            <td width="52">Código:</td>
            <td width="86"><input type="text" name="textfield4" id="textfield5" style="width:80px;"/></td>
            <td width="48">Produto:</td>
            <td width="206"><input type="text" name="textfield4" id="textfield6" style="width:200px;"/></td>
            <td width="75">Classificação:</td>
            <td width="167">
            	<select name="select" id="select" style="width:160px;">
            		<option selected="selected" value="-1"></option>
			            <c:forEach items="${listaTiposProduto}" var="tipoProduto">
							<option value="<c:out value="${tipoProduto.id}"/>"><c:out value="${tipoProduto.descricao}"/></option>
						</c:forEach>
          		</select>
            
            </td>
            <td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="porExcessao();">Pesquisar</a></span></td>
          </tr>
        </table>
        
        <table width="758" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorCota" style="display:none;">
            <tr>
           	  <td width="30">Cota:</td>
                <td width="91">
                <input type="text" name="textfield" id="textfield" style="width:80px;"/></td>
                <td width="37">Nome:</td>
                <td width="470"><input type="text" name="textfield2" id="textfield2" style="width:200px;"/></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="porCota();">Pesquisar</a></span></td>
            </tr>
          </table>
            </td>
          </tr>
        </table>
        
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
    
    
    
<script type="text/javascript">

</script>
 </body>     
      