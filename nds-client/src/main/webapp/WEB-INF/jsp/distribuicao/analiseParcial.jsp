<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/analiseParcial.js"></script>
<script language="javascript" type="text/javascript">
function popup_mudar_base() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-mudar-base" ).dialog({
			resizable: false,
			height:470,
			width:550,
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
function popup_cotas_estudo() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-cotas-estudos" ).dialog({
			resizable: false,
			height:530,
			width:550,
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

function popup_cotas_detalhes() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-cotas-detalhes" ).dialog({
			resizable: false,
			height:560,
			width:740,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			}
		});
	};
	
function popup_edicoes_produto() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-edicoes-produtos" ).dialog({
			resizable: false,
			height:420,
			width:550,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};

$(function() {   
    $('.legendas').tipsy({gravity: $.fn.tipsy.autoNS});
  });


function mostraDados(){
	$('.detalhesDados').show();
	}
function escondeDados(){
	$('.detalhesDados').hide();
	}
</script>
   


<style>
.gridScroll tr:hover{background:#FFC}
.analiseRel tbody{height:100px; overflow:auto;}
.analiseRel tr:hover{background:#FFC;}
.class_tpdv{width:55px;}
.class_novaCota{width:32px;}
.class_cota{width:40px;}
.class_nome{width:90px;}
.class_npdv{width:30px;}
.class_media{width:35px; color:#F00; font-weight:bold;}
.class_vlrs{width:35px;}
.class_vda{width:35px; color:#F00; font-weight:bold;}
.detalhesDados{position:absolute; display:none; background:#fff; border:1px solid #ccc; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); }
</style>

</head>

<body>
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
                <span class="ui-state-default ui-corner-all" style="float:right;">
                <a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a></span>
				<b>Base de Estudo < evento > com < status >.</b></p>
	</div>
    
    	<div class="detalhesDados">
    	  <table width="976" border="0" cellpadding="2" cellspacing="2" class="dadosTab">
    	    <tr>
    	      <td class="class_linha_1"><strong>Edição:</strong></td>
    	      <td class="class_linha_1">0209</td>
    	      <td class="class_linha_1">0208</td>
    	      <td class="class_linha_1">0207</td>
    	      <td class="class_linha_1">0206</td>
    	      <td class="class_linha_1">0205</td>
    	      <td class="class_linha_1">0204</td>
    	      <td class="class_linha_1">0203</td>
    	      <td class="class_linha_1">0202</td>
    	      <td class="class_linha_1">0202</td>
    	      <td><a href="javascript:;" onclick="escondeDados();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" width="15" height="15" border="0" /></a></td>
  	        </tr>
    	    <tr>
    	      <td width="165" class="class_linha_2"><strong>Data Lançamento:</strong></td>
    	      <td width="80" align="center" class="class_linha_2">09/08/2012</td>
    	      <td width="80" align="center" class="class_linha_2">12/07/2012</td>
    	      <td width="80" align="center" class="class_linha_2">18/06/2012</td>
    	      <td width="80" align="center" class="class_linha_2">14/05/2012</td>
    	      <td width="80" align="center" class="class_linha_2">13/04/2012</td>
    	      <td width="80" align="center" class="class_linha_2">12/03/2012</td>
    	      <td width="80" align="center" class="class_linha_2">07/02/2012</td>
    	      <td width="80" align="center" class="class_linha_2">04/01/2012</td>
    	      <td width="80" align="center" class="class_linha_2">04/01/2012</td>
    	      <td width="19" align="center">&nbsp;</td>
  	        </tr>
    	    <tr>
    	      <td class="class_linha_1"><strong>Reparte:</strong></td>
    	      <td align="right" class="class_linha_1">8.588</td>
    	      <td align="right" class="class_linha_1">8.590</td>
    	      <td align="right" class="class_linha_1">8.595</td>
    	      <td align="right" class="class_linha_1">8.590</td>
    	      <td align="right" class="class_linha_1">8.585</td>
    	      <td align="right" class="class_linha_1">7.797</td>
    	      <td align="right" class="class_linha_1">7.797</td>
    	      <td align="right" class="class_linha_1">7.237</td>
    	      <td align="right" class="class_linha_1">6.588</td>
    	      <td align="right">&nbsp;</td>
  	        </tr>
    	    <tr>
    	      <td class="class_linha_2"><strong>Venda:</strong></td>
    	      <td align="right" class="class_linha_2">0</td>
    	      <td align="right" class="class_linha_2">2.587</td>
    	      <td align="right" class="class_linha_2">2.713</td>
    	      <td align="right" class="class_linha_2">3.007</td>
    	      <td align="right" class="class_linha_2">2.691</td>
    	      <td align="right" class="class_linha_2">2.791</td>
    	      <td align="right" class="class_linha_2">2.409</td>
    	      <td align="right" class="class_linha_2">2.109</td>
    	      <td align="right" class="class_linha_2">1.109</td>
    	      <td align="right">&nbsp;</td>
  	        </tr>
  	    </table>
    	</div>
      <fieldset class="classFieldset">
   	    <legend> Pesquisar </legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="51">Código:</td>
              <td width="65">${estudoCota.estudo.produtoEdicao.produto.codigo}</td>
              <td width="54">Produto:</td>
              <td width="210">${estudoCota.estudo.produtoEdicao.produto.nomeComercial}</td>
              <td width="45">Edição:</td>
              <td width="140">${estudoCota.estudo.produtoEdicao.numeroEdicao}</td>
              <td width="45">Estudo:</td>
              <td width="141">${estudoCota.estudo.id}</td>
              <td width="83">Nro. da Parcial:</td>
              <td width="65"></td>
            </tr>
          </table>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="75">Classificação:</td>
            <td width="91">${estudoCota.estudo.produtoEdicao.produto.tipoClassificacaoProduto.descricao}</td>
            <td width="60">Segmento:</td>
            <td width="86">${estudoCota.estudo.produtoEdicao.produto.tipoSegmentoProduto.descricao}</td>
            <td width="78">Ordenar por:</td>
            <td width="143">
            	<select name="select5" id="filtroOrdenarPor" style="width:138px;" onchange="apresentarOpcoesOrdenarPor(this.value);">
	              <option selected="selected" value="selecione">Selecione...</option>
	              <option value="reparte">Reparte</option>
	              <option value="ranking">Ranking</option>
	              <option value="percentual_de_venda">% de Venda</option>
	              <option value="reducao_de_reparte">R de Reparte</option>
	            </select>
	        </td>
            <td width="55">Reparte:</td>
            <td width="49"><input type="text" name="textfield6" id="textfield6" style="width:40px;" /></td>
            <td width="72">Abrangência:</td>
            <td width="32">${estudoCota.estudo.produtoEdicao.produto.percentualAbrangencia}</td>
            <td width="84">Pacote Padrão:</td>
            <td width="64"><input type="text" name="textfield6" id="textfield7" style="width:30px;" /></td>
          </tr>
        </table>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="84">Componente:</td>
            <td width="188">
	            <select id="componentes" name="componentes" style="width:170px;" onchange="javascript:selecionarElementos(this.value,'elementos')">
	              <option value="null" selected="selected">Selecione...</option>
	              <option value="tipo_ponto_venda">Tipo de Ponto de Venda</option>
	              <option value="gerador_de_fluxo">Gerador de Fluxo</option>
	              <option value="bairro">Bairro</option>
	              <option value="regiao">Região</option>
	              <option value="cotas_a_vista">Cotas A Vista</option>
	              <option value="cotas_novas">Cotas Novas</option>
	              <option value="area_influencia">Área de Influência</option>
	              <option value="distrito">Distrito</option>
	            </select>
            </td>
            <td width="60">Elemento:</td>
            <td width="179">
	            <select id="elementos" name="elementos" style="width:170px;" onchange="javascript:filtrarOrdenarPor(${estudoCota.estudo.id})">
	              <option selected="selected">Selecione...</option>
	            </select>
            </td>
            <td width="271">
            	<span id="opcoesOrdenarPor" style="display:none;" class="label">
            		<span id="label_reparte" style="display:none;" class="label"> Reparte: </span> 
            		<span id="label_reducao_de_reparte" style="display:none;" class="label"> % Dê: </span>
            		<span id="label_ranking" style="display:none;" class="label"> Ranking: </span>
            		<span id="label_percentual_de_venda" style="display:none;" class="label"> % Venda: </span>
            		
            		<input id="ordenarPorDe" type="text"style="width:60px;" />
              		At� <input id="ordenarPorAte" type="text" style="width:60px;" />
              		Exs. <a href="javascript:filtrarOrdenarPor(${estudoCota.estudo.id});">
              				<img src="${pageContext.request.contextPath}/images/ico_check.gif" alt="Confirmar" border="0" />
						</a> 
              	</span>
			</td>
            <td width="35" align="center"><a href="javascript:;" onclick="mostraDados();"><img src="${pageContext.request.contextPath}/images/ico_boletos.gif" title="Exibir Detalhes" width="19" height="15" border="0" /></a></td>
            <td width="97">
            	<span class="bt_novos">
            		<a href="javascript:;" onmouseover="popup_detalhes();" onmouseout="popup_detalhes_close();">
            			<img src="${pageContext.request.contextPath}/images/ico_detalhes.png" alt="Ver Capa" hspace="5" border="0" />
            			Ver Capa
            		</a>
            	</span>
            </td>
          </tr>
        </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Base de Estudo / Análise</legend>
        <div class="grids" style="display:block;">
        
        
        
        <table width="950" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="505" align="right"><strong>Edições Base:</strong></td>
                <td width="67" align="center" class="header_table" id="edicao_base_1">1</td>
                <td width="67" align="center" class="header_table" id="edicao_base_2">2</td>
                <td width="67" align="center" class="header_table" id="edicao_base_3">3</td>
                <td width="67" align="center" class="header_table" id="edicao_base_4">4</td>
                <td width="67" align="center" class="header_table" id="edicao_base_5">5</td>
                <td width="67" align="center" class="header_table" id="edicao_base_6">6</td>
                <td width="13" align="center">&nbsp;</td>
            </tr>
          </table>
        <table class="baseEstudoGrid"></table>
        

        <table width="950" border="0" cellspacing="2" cellpadding="2">
          <tr class="class_linha_1">
            <td width="88">Qtde Cotas:</td>
            <td width="236" id="total_de_cotas">02*</td>
            <td width="46" align="right" id="total_juramento">1.230</td>
            <td width="56" align="right" id="total_media_venda">1.230</td>
            <td width="55" align="right" id="total_ultimo_reparte">1.230</td>
            <td width="31" align="right" id="total_reparte1">1.230</td>
            <td width="31" align="right" id="total_venda1"><span class="vermelho">600</span></td>
            <td width="31" align="right" id="total_reparte2">1.230</td>
            <td width="31" align="right" id="total_venda2"><span class="vermelho">600</span></td>
            <td width="31" align="right" id="total_reparte3">1.240</td>
            <td width="31" align="right" id="total_venda3"><span class="vermelho">620</span></td>
            <td width="31" align="right" id="total_reparte4">150</td>
            <td width="31" align="right" id="total_venda4"><span class="vermelho">148</span></td>
            <td width="31" align="right" id="total_reparte5">150</td>
            <td width="31" align="right" id="total_venda5"><span class="vermelho">148</span></td>
            <td width="31" align="right" id="total_reparte6">150</td>
            <td width="31" align="right" id="total_venda6"><span class="vermelho">148</span></td>
            <td width="15" align="right">&nbsp;</td>
          </tr>
          </table>
	<span class="bt_novos">123
		<a href="javascript:return false;" id="liberar">
			<img src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" alt="Liberar" hspace="5" border="0" />
			Liberar
		</a>
	</span>
    <span class="bt_novos">
    	<a href="javascript:history.back(-1);;">
    		<img src="${pageContext.request.contextPath}/images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />
    		Voltar
    	</a>
    </span>
    <span class="bt_novos">
    	<a href="javascript:;" onclick="popup_cotas_estudo();">
    		<img src="${pageContext.request.contextPath}/images/ico_jornaleiro.gif" alt="Cotas que não entraram no Estudo" hspace="5" border="0" />
    		Cotas que não entraram no Estudo
    	</a>
    </span>
    <span class="bt_novos" title="Imprimir">
    	<a href="exportar?fileType=PDF&id=${estudoCota.estudo.id}" >
    		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
    		Imprimir
    	</a>
    </span>
    <span class="bt_novos" title="Gerar Arquivo">
    	<a href="exportar?fileType=XLS&id=${estudoCota.estudo.id}" >
    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
    		Arquivo
    	</a>
    </span>
   </div>
      </fieldset>
       </div>
     </div>
     
     
 <script type="text/javascript">
 $(function(){
	analiseParcialController.init('${estudoCota.estudo.id}', '${faixaDe}', '${faixaAte}'); 
 });
 
 </script>
  </body>
</html>