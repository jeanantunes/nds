<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript" src="scripts/analiseEstudo.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaProduto = new PesquisaProduto();

$(function(){
	analiseEstudoController.init();
});
</script>

</head>

<body>
<form name="form" id="form">

<div class="corpo">
    <div class="container">
      <fieldset class="classFieldset">
   	   
   	    <legend> Pesquisar </legend>
       
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="44">Estudo:</td>
              <td width="90"><input type="text" name="textfield5" id="textfield5"  style="width:80px;" /></td>
              <td width="46">Código:</td>
              <td width="71"><input type="text" name="textfield4" id="textfield4"  style="width:60px;" /></td>
              
              <td width="55">Produto:</td>
              <td width="138"><input type="text" name="produto" id="produto"  style="width:130px;" 
              					onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#produto');"/></td>
              
              <td width="46">Edição:</td>
              <td width="70"><input type="text" name="textfield7" id="textfield7"  style="width:60px;" /></td>
              <td width="78">Classificação:</td>
              
              <td width="152"><select name="comboClassificacao" id="comboClassificacao" style="width:140px;">
              <option selected="selected">Selecione...</option>
              	<c:forEach items="${listaClassificacao}" var="classificacao">
					<option value="${classificacao.id}">${classificacao.descricao}</option>
				</c:forEach>
            
            </select>
            
            </td>
              <td width="104">
              	<span class="bt_pesquisar">
              		<a href="javascript:;" onclick="mostrar();">
              			Pesquisar
              		</a>
              	</span>
              </td>
            </tr>
          </table>
      </fieldset>
   
      <div class="linha_separa_fields">&nbsp;</div>
    
      <div class="grids" style="display:none;">
      <fieldset class="classFieldset">
       	  <legend>Estudos Cadastrados</legend>
        
          <table class="estudosGrid"></table>

      </fieldset>
      </div>
      
      </div>
     </div>
 </form>
</body>