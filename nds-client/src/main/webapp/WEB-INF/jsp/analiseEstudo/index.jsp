<!-- analiseEstudo -->
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/histogramaPosEstudo.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/matrizDistribuicao.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript" src="scripts/analiseEstudo.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaProduto = new PesquisaProduto();

$(function(){
	analiseEstudoController.init();
});
</script>

<div id="histogramaPosEstudoContent">

<div id="AnaliseEstudoMainContent">

<body>
<form name="form" id="form">
<div class="areaBts">
  <div class="area">
      <div class="" style='display:none;'></div>
  </div>
</div>
<br clear="all" />
  <br />

<div class="corpo">
    <div class="container">
      <fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
   	   
   	    <legend> Pesquisar </legend>
       
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="44">Estudo:</td>
              <td width="90"><input type="text" name="idEstudo" id="idEstudo"  style="width:80px;" /></td>
              <td width="46">C&oacute;digo:</td>
              <td width="71"><input type="text" name="codProduto" id="codProduto"  style="width:60px;" 
              				 onchange="pesquisaProduto.pesquisarPorCodigoProduto('#codProduto','#analise-estudo-produto', false, undefined, undefined);"/></td>
              
              <td style="width:55px">Produto:</td>
              <td width="138"><input type="text" name="analise-estudo-produto" id="analise-estudo-produto"  style="width:130px;"/></td>
            </tr>
            <tr>
              <td width="46">Edi&ccedil;&atilde;o:</td>
              <td width="70"><input type="text" name="edicaoProd" id="edicaoProd"  style="width:60px;" /></td>
              <td width="78">Classifica&ccedil;&atilde;o:</td>
              <td width="152">
	           
              <select name="comboClassificacao" id="comboClassificacao" style="width:140px;">
	              <option selected="selected">SELECIONE</option>
	              	<c:forEach items="${listaClassificacao}" var="classificacao">
						<option value="${classificacao.id}">${classificacao.descricao}</option>
					</c:forEach>
              </select>
	            
			  <td width="72" align="right">Data Lan&ccedil;amento:</td>
				<td width="146">
				<input type="text" name="dataLancamento" id="dataLancamento" style="width:70px;"/>
	          </td>
            
            </td>
              <td width="104">
              	<span class="bt_pesquisar">
              		<a href="javascript:;" onclick="analiseEstudoController.carregarEstudos();">
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
</div>