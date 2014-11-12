<style>
.linkDisabled {
	cursor: default;
	opacity: 0.4;
}

.opcoesEstudos {

padding: 15px;
display: none; position:absolute; border:1px solid #ccc; background:#FFF; width:220px; z-index:10;top: 35px;
}

.gridLinha {
  background:#CCFFFF; 
}




</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/matrizDistribuicao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/distribuicaoVendaMedia.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>

<script type="text/javascript">

var estudoParaDivisao=null;

function showTab(link, title) {
	$('#workspace').tabs('addTab', title, link);
}

var pathTela = "${pageContext.request.contextPath}";

var matrizDistribuicao = new MatrizDistribuicao(pathTela, "matrizDistribuicao", BaseController.workspace);

matrizDistribuicao.inicializar();

var distribuicaoVendaMedia = new DistribuicaoVendaMedia(pathTela, BaseController.workspace);

var lancamentosSelecionados = [];
</script>

<style>

.ui-datepicker-today a { display:block !important; }
.dialog-detalhe-produto { display:none; }
.dialog-confirm-balanceamento { display:none; }

.fieldFiltroMatriz{margin-top:27px; width:1100px!important; margin-right:0px!important;}
.fieldGridMatriz{width:1100px;}

</style>


<div id="telaAnalise" />
<div id="telasAuxiliaresContent"/>
<div id="matrizDistribuicaoContent">
	<form id="form_exclusao_estudo">
	<div id="popup_confirmar_exclusao_estudo" title="Excluir Estudo" style="display:none">
		<p>Confirma a exclus&atildeo do estudo?</p>
	</div>
	
	<div id="confirmar_variaveis" title="Variáveis" style="display:none" >
		<p>Deseja exibir as variáveis do estudo?</p>
	</div>
	
	<div id="confirmar_uma_edicao_base" title="Uma edi&ccedil&atildeo base" style="display:none" >
	</div>
	
	</form>
		
		   	<jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-novo" name="messageDialog"/>
			</jsp:include>
			
			
			<div class="areaBts">
				<div class="area">
				<div id=btnsMatrizDistribuicao style="display: none;">
							<span class="bt_novos">
			         	  		<a id="linkExcluir" idEdicao="true" href="javascript:;" onclick="matrizDistribuicao.popup_confirmar_exclusao_estudo();" rel="tipsy" title="Excluir Estudo"> 
									<img id="imgExcluir" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0"/>
 						        </a>
		         	  		</span>
		         	  		
		         	  		<div class="bt_novos" style="position:relative; z-index:1;" >
				        		<a href="javascript:;" onclick="matrizDistribuicao.mostrarOpcoes();" rel="tipsy" title="Opções">
				        			<img src="${pageContext.request.contextPath}/images/ico_boletos.gif" hspace="5" border="0"/><img src="${pageContext.request.contextPath}/images/p7PM_dark_south.gif" width="14" height="8" border="0" class="setaMuda" />
				        		</a>
				        		<div class="opcoesEstudos" >
						            <ul style="list-style-image: url(data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7);line-height:0px">
							               <li><a href="javascript:;" onmouseover="$(this).parent().mouseout(function() {});" onclick="matrizDistribuicao.distribuicaoVendaMedia();"><img src="${pageContext.request.contextPath}/images/ico_distribuicao_normal.gif" border="0"/>Distribuição Venda Média</a></li>
							               <li><a href="javascript:;" onmouseover="$(this).parent().mouseout(function() {});" onclick="matrizDistribuicao.gerarEstudoManual();"><img src="${pageContext.request.contextPath}/images/ico_estudo_manual.gif" border="0"/>Distribuição Manual</a></li>
							               <li><a href="javascript:;" onmouseover="$(this).parent().mouseout(function() {});" onclick="matrizDistribuicao.somarEstudos();"><img src="${pageContext.request.contextPath}/images/ico_soma_estudos.gif" border="0"/>Somar Estudos</a></li>
							               <li><a href="javascript:;" onmouseover="$(this).parent().mouseout(function() {});" onclick="matrizDistribuicao.dividirEstudo();"><img src="${pageContext.request.contextPath}/images/ico_dividir_estudos.gif" border="0"/>Dividir Estudo</a></li>
							               <li><a href="javascript:;" onmouseover="$(this).parent().mouseout(function() {});" onclick="matrizDistribuicao.estudoComplementarShow();" ><img src="${pageContext.request.contextPath}/images/ico_estudo_complementar.gif" border="0"/>Estudo Complementar</a></li>
							               <li><a href="javascript:;" onmouseover="$(this).parent().mouseout(function() {});" onclick="matrizDistribuicao.copiarProporcionalDeEstudo();"><img src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif" border="0"/>Cópia Proporcional de Estudo</a></li>
						           </ul>
  							 	</div>
       	  					</div>
						        	
		         	  		<span class="bt_novos" disabled>
			         	  			<!-- a href="javascript:;" onclick="matrizDistribuicao.popup_confirmar_finalizacao_matriz();"  title="Finalizar Matriz de Distribui&ccedil&atildeo"  -->
		         	  					<img id="imgFinalizar" src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />
			         	  			<!--/a -->
		         	  		</span>	
		         	  		
<!-- 		         	  		<span class="bt_novos"> -->
<!-- 						         	<a href="javascript:;" onclick="matrizDistribuicao.popup_confirmar_reabertura_matriz();"> -->
<%-- 								        <img id="imgReabrirMat" src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" hspace="5" border="0"> --%>
<!-- 								         	 Reabrir Itens -->
<!-- 						        		</img> -->
<!-- 						        	</a> -->
<!-- 		         	  		</span>	 -->
		         	  		
		         	  		<span class="bt_novos">
			         	  			<a id="linkReabrir" href="javascript:;" onclick="matrizDistribuicao.popup_confirmar_reabertura_estudo();" rel="tipsy" title="Reabrir Estudo">
							        		<img id="imgReabrirEst" src="${pageContext.request.contextPath}/images/ico_add_novo.gif" hspace="5" border="0" />
							        </a>
		         	  		</span>
		         	  		
		         	  		<span class="bt_novos">
			         	  			<a href="javascript:;" onclick="matrizDistribuicao.analise()" rel="tipsy" title="Analisar Estudo">
		         	  					<img id="imgAnalise" src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif" hspace="5" border="0"/>
			         	  			</a>
		         	  		</span>
		         	  		
		         	  		<span class="bt_novos">
								<a href="javascript:;" onclick="matrizDistribuicao.popup_confirmar_duplicarLinha();" rel="tipsy" title="Duplicar Linha">
									<img src="${pageContext.request.contextPath}/images/ico_negociar.png" hspace="5" border="0" /></a>
							</span>
							
							<span class="bt_novos" id="spanGerarEstudoAutomatico">
								<a id="linkGerarEstudoAutomatico" href="javascript:void(0)" onclick="" rel="tipsy" title="Gera&ccedil;&atilde;o Autom&aacute;tica">
									<img src="${pageContext.request.contextPath}/images/ico_geracao_automatica.gif" hspace="5" border="0" />
								</a>
							</span>
		         	  		
		         	  		<span class="bt_arq">
								<a href="${pageContext.request.contextPath}/matrizDistribuicao/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
							    	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						   	 	</a>
						   	</span>
							<span class="bt_arq">
								<a href="${pageContext.request.contextPath}/matrizDistribuicao/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
							    	<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0"/>
						    	</a>
							</span>
								
						</div>
				
				</div>
			</div>
			
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="linha_separa_fields">&nbsp;</div>
		     
			    <div id="telaPesquisaMatriz"> 
			      <fieldset class="fieldFiltroMatriz fieldFiltroItensNaoBloqueados">
			   	    <legend>Pesquisar Matriz de Distribui&ccedil&atildeo
		        </legend>
		   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		   	      <tr>
		   	        <td width="68">Fornecedor:&nbsp;</td>
		   	        <td width="228">
		            <a href="javascript:void(0)" id="selFornecedor" onclick="return false;">Clique e Selecione o Fornecedor</a>
		              <div class="menu_fornecedor" style="display:none;">
		                	<span class="bt_sellAll">

							<input type="checkbox" id="selTodos1" checked="checked" name="selTodos1" onclick="checkAll(this, 'checkgroup_menu');" style="float:left;"/>

							<label for="selTodos1">Selecionar Todos</label></span>
		                    <br clear="all" />
		                    <c:forEach items="${fornecedores}" var="fornecedor">
		                      <input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}"  name="checkgroup_menu" onclick="verifyCheck($('#selTodos1'));" type="checkbox" checked="checked"/>
		                      <label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</label>
		                      <br clear="all" />
		                   </c:forEach> 
		              </div>
		            
		            </td>
			   	        <td width="120">Data de Lançamento:</td>
		   	        <td width="109"><input class="campoDePesquisa" type="text" name="datepickerDe" id="matrizDistribuicao_datepickerDe" style="width:80px;" value="${data}" /></td>
			   	        <td width="30" align="center">&nbsp;</td>
			   	        <td ><span class="bt_novos" title="Pesquisar">   
						<!-- Pesquisar -->
							<input type='image' id="linkPesquisar" onclick="matrizDistribuicao.pesquisar();" src="${pageContext.request.contextPath}/images/ico_pesquisar.png"/>
					</td>
		          </tr>
		        </table>
		      </fieldset>
		      <div class="linha_separa_fields">&nbsp;</div>
			      <fieldset class="fieldGridMatriz">
			       	  <legend>Matriz de Distribui&ccedil&atildeo</legend>
			       
			        	<div id="gridMatrizDistribuicao" class="gridMatrizDistribuicao" style="display:none;">
	
		        
		       	   <table id="lancamentoMatrizDistribuicaoGrid" class="lancamentoMatrizDistribuicaoGrid"></table>
		         	  		
		         	  		
		         	  		
		         	  		<span class="bt_arq">
			         	  			<input type="checkbox" id="selTodos" name="Todos" onclick="matrizDistribuicao.checkUncheckLancamentos()">
		         	  				Selecionar Todos
		         	  			
		         	  		</span>
				 </br>	
					
				<div>
					<table width="650" border="0" align="left">
						
						<tr>
							<td colspan="4">
								<b>Total de Publica&ccedil&otildees: Publica&ccedil&otildees Liberadas</b> &nbsp;<span id="totalLiberado"></span>, &nbsp;<b>Publica&ccedil&otildees sem Estudo:&nbsp;</b> <span id="totalGerado"></span>
							</td>
						</tr>
					</table>
				</div>	
			      	
			      	</div>
			      	
			      	<div id="matrizFinalizada" class="matrizFinalizada" style="display: none;">
			       	   	<span align="center"><h3>MATRIZ FINALIZADA</h3></span>
						<br clear="all" />
						<span class="bt_novos">
				         	<a href="javascript:;" onclick="matrizDistribuicao.popup_confirmar_reabertura_matriz();">
						        <img id="imgReabrirMat" src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" hspace="5" border="0">
						         	 Reabrir Matriz
						        </img>
				         	</a>
				     	</span>		    
			    	</div> 
			      
			      </fieldset>
			
			<form id="form-confirm-finalizacao">
			<div id="dialog-confirm-finalizacao" title="Finalizar Matriz" style="display:none;">
			    
			    <jsp:include page="../messagesDialog.jsp">
					<jsp:param value="dialog-confirmar" name="messageDialog"/>
				</jsp:include>
				
			    <fieldset style="width:250px!important;">
			    	<legend>Confirmar Finaliza&ccedil&atildeo de Matriz</legend>
					
						Confirma finaliza&ccedil&atildeo da Matriz de Distribui&ccedil&atildeo?
	
			    </fieldset>
			</div>
			</form>
			
			<form id="form-confirm-reabrir-matriz">
			<div id="dialog-confirm-reabrir-matriz" title="Reabrir Matriz" style="display:none;">
			    
			    <jsp:include page="../messagesDialog.jsp">
					<jsp:param value="dialog-confirmar" name="messageDialog"/>
				</jsp:include>
				
			    <fieldset style="width:250px!important;">
			    	<legend>Confirmar Reabertura de Matriz</legend>
					
						Confirma reabertura da Matriz de Distribui&ccedil&atildeo?
	
		      </fieldset>
			</div>
			</form>

		<form id="form-confirm-exclusao">
		<div id="dialog-confirm-exclusao" title="Excluir Estudo" style="display:none;">
		    
		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-confirmar" name="messageDialog"/>
			</jsp:include>
			
		    <fieldset style="width:250px!important;">
			    	<legend>Exclus&atildeo de Estudo</legend>
				
						Excluir estudo selecionado?

		    </fieldset>
		</div>
		</form>
		
		<form id="form-confirm-reabert">
		<div id="dialog-confirm-reabert" title="Reabrir Estudo(s)" style="display:none;">
		    
		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-confirmar" name="messageDialog"/>
			</jsp:include>
			
		    <fieldset style="width:250px!important;">
		    	<legend>Confirmar Reabertura de Estudo(s)</legend>
				
					Confirmar a reabertura do(s) estudo(s) selecionado(s)?

		    </fieldset>
		</div>
		</form>
		
			<form id="form-confirm-duplicar">
			<div id="dialog-confirm-duplicar" title="Duplicar Linha" style="display:none;">
			    
			    <jsp:include page="../messagesDialog.jsp">
					<jsp:param value="dialog-confirmar" name="messageDialog"/>
				</jsp:include>
				
			    <fieldset style="width:250px!important;">
			    	<legend>Duplicação Registro</legend>
					
						Confirmar a duplica&ccedil&atildeo do registro selecionado?
	
			    </fieldset>
			</div>
			</form> 	
				
		</div>
		
		<div style="display:none" class="campoPesquisaEstudo"></div>
			<form id="form-copiar-estudo">
			<div id="dialog-copiar-estudo" title="Copia Proporcional de Estudo" style="display:none;">
			    
			    <jsp:include page="copiarEstudo.jsp" />
			</div>
		</form>

		<div id="dialog-somar-estudo" title="Somar Estudos" style="display:none;">
		    <jsp:include page="somarEstudo.jsp" />
		</div>
</div>
