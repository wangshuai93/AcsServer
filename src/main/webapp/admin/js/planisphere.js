var width;
var height;
var end_time;
var begin_time;
var x;
var y;
var svg;
var xAxis;
var yAxis;
var line;
var zoom;
var container;
var points;
var xTipText;
var yTipText;
var showTips;
//获取不同分辨率
var screenWidth = window.screen.width;
var screenHeight = window.screen.height;

var bt;
var et;

var data;
var wording1;
var wording2;
var select_checkbox = $("input[type='checkbox']").is(':checked');;
var tips; 

//var device_id = $("[name = 'm_device_id']").attr("value");
var m_serialNumber = $("[name = 'm_serialNumber']").attr("value");

//by hdt
var data_xy;
var data_line;
var countResultId;
var text_con="";
var text_first_con;

var cx;
var x0;
var data_tip;
var data_f;

var kind_data="";
var wording3;
var wording4;
var xUnit;
var yUnit;
var zUnit;
//点击星座图按钮
function viewplanisphere(m_countResultId,m_frequency){
	var taskId = $("#all_task").val();
	var countResultId = m_countResultId;
	m_frequency = m_frequency.replace(/\]/g,"");
	m_frequency = m_frequency.replace(/\[/g,"");
	text_con = "调制方式：QAM64     符号率：6900   频点：" + m_frequency;
	getTaskResult(countResultId, taskId, callback);
}

function viewplanispherelist(){
	$("#text_con").hide();
	$("#svg_draw_con").hide();
	$("#datatable_div").show();
	text_first_con.remove();
}

function viewfulldata(m_countResultId,m_kinddata){
	var taskId = $("#all_task").val();
	var countResultId = m_countResultId;
	text_con = "level" ;
	kind_data = m_kinddata;
	getTaskResult(countResultId, taskId, callback_full);
}

function viewfulldatalist(){
	$("#text_full").hide();
	$("#svg_draw_full").hide();
	$("#datatable_div").show();
//	text_first.remove();
	wording3 = "";
	wording4 = "";

}

function getCheckBox(){
	select_checkbox = $("input[type='checkbox']").is(':checked');
}

var item_choice = $("#item_choice").val();

//任务选择
var taskId = $("#all_task").val();
if(taskId != ""){
	getTaskResultList(m_serialNumber, taskId, successCallback);
}
if(taskId == 13 || taskId == 14){
	$("#item_list_figure").hide();
	$("#item_list_figure_text").hide();
	$("#item_list_figure").val("display_list");
}
else{
	$("#item_list_figure").show();
	$("#item_list_figure_text").show();
}

function getTaskItemChoice(){
	taskId = $("#all_task").val();
	if(taskId != "" && taskId != undefined){
		getTaskResultList(m_serialNumber, taskId, successCallback);
	}
}

//频谱图、混合图选择判断
var item_choice_pic = $("#item_choice_pic").val();
if(item_choice_pic == "frequency_signalStrength"){
	$("#item_choice_td").hide();
} else if(item_choice_pic == "combine"){
	$("#item_choice_td").show();
}

//列表图形呈现方式判断
var item_list_figure = $("#item_list_figure").val();
if(item_list_figure == "display_list"){
	$("#choice_div").hide();
	//画图形部分
	$("#text").hide();
	$("#svg_draw").hide();
	$("#div_block").hide();
	$("#white_block").hide();
	
	$("#datatable_div").show();
} else if(item_list_figure == "display_figure"){
	$("#choice_div").show();
	
	$("#text").show();
	$("#svg_draw").show();
	$("#div_block").show();
	$("#white_block").show();
	
	$("#datatable_div").hide();
}
function getListTaskChange(){

	var taskId = $("#all_task").val();
	if(taskId == 13 || taskId == 14){
		$("#item_list_figure").hide();
		$("#item_list_figure_text").hide();
		$("#item_list_figure").val("display_list");
	}
	else{
		$("#item_list_figure").show();
		$("#item_list_figure_text").show();
	}
}
function getListFigureChange(){
	item_list_figure = $("#item_list_figure").val();
	
	$("#text").remove();
	$("#svg_draw").remove();
	$("#white_block").show();
//	$("#line_discription").remove();
	
	if(item_list_figure == "display_list"){
		$("#choice_div").hide();
		//画图形部分
		$("#text").hide();
		$("#svg_draw").hide();
		$("#div_block").hide();
		$("#white_block").hide();
		
		$("#datatable_div").show();
	} else if(item_list_figure == "display_figure"){
		$("#choice_div").show();
		$("#text").show();
		$("#svg_draw").show();
		$("#div_block").show();
		$("#white_block").show();
		
		$("#datatable_div").hide();
	}
}

var flag = false;
function getPicChoice(){
	if(item_choice_pic == "frequency_signalStrength"){
		$("#item_choice_td").hide();
//		$("#text").hide();
	} else if(item_choice_pic == "combine"){
		$("#item_choice_td").show();
		/*if(data != undefined || data != null){
			$("#text").show();
		}else{
			$("#text").hide();
		}*/
	}
	
	if(data != undefined){
		$("#text").show();
		draw();
	}
	//刷新页面
	history.go(0);
}

function getchoice(){
	item_choice = $("#item_choice").val();
	
	if(data != undefined){
		$("#text").show();
		draw();
	}
	//刷新页面
	history.go(0);

}

function countxyf(string){
	var a = "][";
	var length1 = string.length;
	var length2 = string.replace(/\]\[/g,"").length;
	var countxy = 0;
	countxy = (length1-length2)/a.length+1;
	return countxy;
}

function callback_full(dataList){
	if(null != dataList && dataList.length != 0){
		var arr_LEV = new Array();
		var arr_SNR = new Array();
		var arr_BER = new Array();
		var arr_FRE = new Array();
		var line_xy = new Array();

		var LEV_values = "";
		var SNR_values = "";
		var BER_values = "";
		var FRE_values = "";
		
		for(var i = 0; i != dataList.length; i++){
			if(dataList[i].m_paramName == "Level"){
				LEV_values = dataList[i].m_paramValue;
				arr_LEV = LEV_values.split(",");
			}
			if(dataList[i].m_paramName == "SNR"){
				SNR_values = dataList[i].m_paramValue;
				arr_SNR = SNR_values.split(",");
			}
			if(dataList[i].m_paramName == "BER"){
				BER_values = dataList[i].m_paramValue;
				arr_BER = BER_values.split(",");
			}
			if(dataList[i].m_paramName == "CurrentFrequency"){
				FRE_values = dataList[i].m_paramValue;
				arr_FRE = FRE_values.split(",");
			}			
		}

		var arr = new Array();		
		if(kind_data == "LEVEL")
			arr[0] = Array.apply(0, Array(arr_LEV.length)).map(function(item, j){
				j++;
				return {x: arr_FRE[j-1], y: arr_LEV[j-1]}				
			});
		if(kind_data == "SNR")
			arr[0] = Array.apply(0, Array(arr_SNR.length)).map(function(item, j){
				j++;
				return {x: arr_FRE[j-1], y: arr_SNR[j-1]}				
			});	
		if(kind_data == "BER")
			arr[0] = Array.apply(0, Array(arr_BER.length)).map(function(item, j){
				j++;
				return {x: arr_FRE[j-1], y: arr_BER[j-1]}				
			});	
		
		data = arr;
		data_tip = arr[0];
		paint_full();

		
		$("#text_full").show();
		$("#svg_draw_full").show();
		$("#div_block_full").show();
		$("#white_block").show();
		
		$("#datatable_div").hide();
	}else{
		$("#text_full").hide();
		$("#svg_draw_full").hide();
		$("#white_block").hide();
		$("#item_choice_td").hide();
//		$("#line_discription").hide();
	}
}

function callback(dataList){
	if(null != dataList && dataList.length != 0){
		var arr_x = new Array();
		var arr_y = new Array();
		var arr_xy = new Array();
		var line_xy = new Array();

		var xys = "";
		
		for(var i = 0; i != dataList.length; i++){
			if(dataList[i].m_paramName == "ConstellationData"){
				xys = xys + dataList[i].m_paramValue;

			}
			if(dataList[i].m_paramName == "ConstellationData1"){
				xys = xys + dataList[i].m_paramValue;
			}
			if(dataList[i].m_paramName == "ConstellationData2"){
				xys = xys + dataList[i].m_paramValue;
			}
			if(dataList[i].m_paramName == "ConstellationData3"){
				xys = xys + dataList[i].m_paramValue;
			}
			if(dataList[i].m_paramName == "ConstellationData4"){
				xys = xys + dataList[i].m_paramValue;
			}
			if(dataList[i].m_paramName == "ConstellationData5"){
				xys = xys + dataList[i].m_paramValue;
			}
			if(dataList[i].m_paramName == "ConstellationData6"){
				xys = xys + dataList[i].m_paramValue;
			}
			if(dataList[i].m_paramName == "ConstellationData7"){
				xys = xys + dataList[i].m_paramValue;
			}
			
		}
		var count = countxyf(xys);
		count = count*2;
		
		xys = xys.replace(/\]\[/g," ");
		//xys = xys.replace("]["," ");		
		xys = xys.replace(/,/g," ");
		xys = xys.replace(/\[/g,"");
		xys = xys.replace(/\]/g,"");
		arr_xy = xys.split(" ");

		var	count_x = 0;
		var count_y = 0;
		for(var i=0; i<count; i++){
			if(i%2 == 0 || i == 0){
				arr_x[count_x] = arr_xy[i];
				count_x++;
			}	
			else{				
				arr_y[count_y] = arr_xy[i];
				count_y++;
			}	
		}
		
		var arr = new Array();
		arr = Array.apply(0, Array(count_x)).map(function(item, j){
			j++;
			return {x: arr_x[j-1], y: arr_y[j-1]}				
		});
		
		data_xy = arr;
		
		var arr = new Array();
		for(var i = 0;i<9;i++)
		{
			arr[i] = Array.apply(0, Array(2)).map(function(item, j){
				j++;
				return {x: (j-1)*512, y: 64*i}				
			});
		}
		
		for(var i = 0;i<9;i++)
		{
			arr[i+9] = Array.apply(0, Array(2)).map(function(item, j){
				j++;
				return {x: 64*i, y: (j-1)*512}				
			});
		}

		data_line = arr
		
		paint_xy();
			
		$("#text_con").show();
		$("#svg_draw_con").show();
		//$("#div_block_con").show();
		//$("#white_block").show();
		
		$("#datatable_div").hide();

	}else{
		$("#text_con").hide();
		$("#svg_draw_con").hide();
		$("#datatable_div").show();
	}
	
}

//Tuner: 误码率BER, 信号强度Level, 当前频点Frequency, 符号率SymbolRate, 信噪比SNR
function successCallback(dataList){
	if(null != dataList && dataList.length != 0){
		var frequencyLength = 0;
		var symbolRateLength = 0; 
		var sNRLength = 0;
		var levelLength = 0;
		var bERLength = 0;
		
		var bERStartPosition = -2;
		var bEREndPosition = -2;
		var levelStartPosition = -2;
		var levelEndPosition = -2;
		var frequencyStartPosition = -2;
		var frequencyEndPosition = -2;
		var symbolRateStartPosition = -2;
		var symbolRateEndPosition = -2;
		var sNRStartPosition = -2;
		var sNREndPosition = -2;
		
		for(var i = 0; i != dataList.length; i++){
			if(dataList[i].m_paramName == "BER"){
				bEREndPosition = i;
				bERLength++;
			}
			if(dataList[i].m_paramName == "Level"){
				levelEndPosition = i;
				levelLength++;
			}
			if(dataList[i].m_paramName == "CurrentFrequency"){
				frequencyEndPosition = i;
				frequencyLength++;
			}
			if(dataList[i].m_paramName == "SymbolRate"){
				symbolRateEndPosition = i;
				symbolRateLength++;
			}
			if(dataList[i].m_paramName == "SNR"){
				sNREndPosition = i;
				sNRLength++;
			}
		}
		
		bERStartPosition = bEREndPosition - bERLength + 1;
		levelStartPosition = levelEndPosition - levelLength + 1;
		frequencyStartPosition = frequencyEndPosition - frequencyLength + 1;
		symbolRateStartPosition = symbolRateEndPosition - symbolRateLength + 1;
		sNRStartPosition = sNREndPosition - sNRLength + 1;
		
		console.info("bERStartPosition = " + bERStartPosition + "; levelStartPosition = " + levelStartPosition
				+ "; frequencyStartPosition = " + frequencyStartPosition + "; symbolRateStartPosition = " + 
				symbolRateStartPosition + "; sNRStartPosition = " + sNRStartPosition);
		
		var arr = new Array();
		if(item_choice_pic == "frequency_signalStrength"){
			//此处默认频率与信号强度的参数值个数相对应，长度应相等
			arr[0] = Array.apply(0, Array(frequencyLength)).map(function(item, j){
				j++;
				if(dataList[j-1+frequencyStartPosition].m_paramName == "Frequency"){
					return {x: dataList[j-1+frequencyStartPosition].m_paramValue, y: dataList[j-1+levelStartPosition].m_paramValue}
				}else{
					return {x: "", y: ""}
				}
			});
		} else if(item_choice_pic == "combine"){
			
			if(item_choice == "bitErrorRate"){
				arr[0] = Array.apply(0, Array(bERLength)).map(function(item, j){
					j++;
					if(dataList[j-1+bERStartPosition].m_paramName == "BER"){
						return {x: new Date(dataList[j-1+bERStartPosition].m_reportTime), y: dataList[j-1+bERStartPosition].m_paramValue}
					}else{
						return {x: "", y: ""}
					}
				});
			}
			
			if(item_choice == "signalStrength"){
				arr[0] = Array.apply(0, Array(levelLength)).map(function(item, j) {
					j++;
					if(dataList[j-1+levelStartPosition].m_paramName == "Level"){
						return {x: new Date(dataList[j-1+levelStartPosition].m_reportTime), y: dataList[j-1+levelStartPosition].m_paramValue}
					}else{
						return {x: "", y: ""}
					}
				});
			}
			
			if(item_choice == "currentFrequency"){
				arr[0] = Array.apply(0, Array(frequencyLength)).map(function(item, j) {
					j++;
					if(dataList[j-1+frequencyStartPosition].m_paramName == "Frequency"){
						return {x: new Date(dataList[j-1+frequencyStartPosition].m_reportTime), y: dataList[j-1+frequencyStartPosition].m_paramValue}
					}else{
						return {x: "", y: ""}
					}
				});
			}
			
			if(item_choice == "symbolRate"){
				arr[0] = Array.apply(0, Array(symbolRateLength)).map(function(item, j) {
					j++;
					if(dataList[j-1+symbolRateStartPosition].m_paramName == "SymbolRate"){
						return {x: new Date(dataList[j-1+symbolRateStartPosition].m_reportTime), y: dataList[j-1+symbolRateStartPosition].m_paramValue}
					}else{
						return {x: "", y: ""}
					}
				});
			}
			
			if(item_choice == "sinalToNoice"){
				arr[0] = Array.apply(0, Array(sNRLength)).map(function(item, j) {
					j++;
					if(dataList[j-1+sNRStartPosition].m_paramName == "SNR"){
						return {x: new Date(dataList[j-1+sNRStartPosition].m_reportTime), y: dataList[j-1+sNRStartPosition].m_paramValue}
					}else{
						return {x: "", y: ""}
					}
				});
			}
			
			data_f = Array.apply(0, Array(frequencyLength)).map(function(item, j) {
				j++;
				if(dataList[j-1+frequencyStartPosition].m_paramName == "CurrentFrequency"){
					return {x: new Date(dataList[j-1+frequencyStartPosition].m_reportTime), y: dataList[j-1+frequencyStartPosition].m_paramValue}
				}else{
					return {x: "", y: ""}
				}
			});
		}
		
		data = arr;
		data_tip = arr[0];

		paint();
//		if(item_choice_pic == "combine"){
//			$("#line_discription").show();
//		}
	} else{
		$("#text").hide();
		$("#svg_draw").hide();
		$("#white_block").hide();
		$("#item_choice_td").hide();
//		$("#line_discription").hide();
	}
}

function draw(){
	var html = "";
	var planisphere_html = 
		'<div id="text" style="text-align: center; background: #2D353C; margin-left: 1.5%; margin-top:0.1%; margin-right: 1.5%; height: 25px; padding-top: 1%; display: none;">' + 
		'</div>' + 
		'<div id="svg_draw" class="morris-inverse" style="background: #2D353C; margin-left: 1.5%; margin-right: 1.5%; display: none;">' + 
		'</div>' +
/*		
		'<div id="line_discription" style="background: #2D353C; margin-left: 1.5%; margin-right: 1.5%; height: 50px; padding-top: 1%; display: none;">' + 
			'<div style="width: 12%; display: inline-block; margin-left: 4%;">' + 
				'<div style="margin-left:5%; margin-top:2%; height: 2px; width: 100%; background: #00ACAC;"/>' +
				'<div style="margin-top: 3%; height: 20px; margin-left: 20%; color: #00ACAC;">日期/误码率</div>' +
			'</div>' +
			
			'<div style="width: 12%;display: inline-block; margin-left: 8%;">' +
				'<div style=" margin-top:2%; height: 2px; width: 100%; background: #FF0000;"/>' +
				'<div style="margin-top: 3%; height: 20px; margin-left: 13%; color: #FF0000;">日期/信号质量</div>' +
			'</div>' +
			
			'<div style="width: 12%;display: inline-block; margin-left: 8%;">' +
				'<div style="margin-top:2%; height: 2px; width: 100%; background: ##FF0000;"/>' +
				'<div style="margin-top: 3%; height: 20px; margin-left: 13%; color: ##FF0000;">日期/当前频点</div>' +
			'</div>' +
			
			'<div style="width: 12%;display: inline-block; margin-left: 8%;">' +
				'<div style="margin-top:2%; height: 2px; width: 100%; background: #FFA500;"/>' +
				'<div style="margin-top: 3%; height: 20px; margin-left: 13%; color: #FFA500;">日期/信噪比</div>' +
			'</div>' +
			
			'<div style="width: 12%;display: inline-block; margin-left: 8%;">' +
				'<div style="margin-top:2%; height: 2px; width: 100%; background: #FF5B57;"/>' +
				'<div style="margin-top: 3%; height: 20px; margin-left: 16%; color: #FF5B57;">日期/符号率</div>' +
			'</div>' +
*/			
		'</div>'
		
	html += planisphere_html;
	$("#div_block").html(html);
}

function getWidthAndTicks(flag){
	if(screenWidth == 1920 && screenHeight == 1080){
		if(flag == "changeWidth"){
			width = 1550;
		} else if(flag == "changeTicks"){ 
			xAxis.ticks(20);
		}
	} else if(screenWidth == 1366 && screenHeight == 768){
		if(flag == "changeWidth"){
			width = 995;
		} else if(flag == "changeTicks"){
			xAxis.ticks(14);
		}
	} else if(screenWidth == 1280 && screenHeight == 1024){
		if(flag == "changeWidth"){
			width = 940;
		} else if(flag == "changeTicks"){
			xAxis.ticks(14);
		}
	} else if(screenWidth == 1280 && screenHeight == 720){
		if(flag == "changeWidth"){
			width = 920;
		} else if(flag == "changeTicks"){
			xAxis.ticks(14);
		}
	} else if (screenWidth == 1152 && screenHeight == 864){
		if(flag == "changeWidth"){
			width = 800;
		} else if(flag == "changeTicks"){
			xAxis.ticks(10);
		}
	} else if(screenWidth == 1024 && screenHeight == 768){
		if(flag == "changeWidth"){
			width = 660;
		} else if(flag == "changeTicks"){
			xAxis.ticks(10);
		}
	} else if(screenWidth == 800 && screenHeight == 600){
		if(flag == "changeWidth"){
			width = 460;
		} else if(flag == "changeTicks"){
			xAxis.ticks(10);
		}
	}  else{
		if(flag == "changeWidth"){
			width = 1550;
		} else if(flag == "changeTicks"){ 
			xAxis.ticks(20);
		}
	} 
	


}

function getWidthAndTicks_con(flag){
	if(screenWidth == 1920 && screenHeight == 1080){
		if(flag == "changeWidth"){
			width = 1550;
		} else if(flag == "changeTicks"){ 
			xAxis.ticks(20);
		}
	} else if(screenWidth == 1366 && screenHeight == 768){
		if(flag == "changeWidth"){
			width = 995;
		} else if(flag == "changeTicks"){
			xAxis.ticks(14);
		}
	} else if(screenWidth == 1280 && screenHeight == 1024){
		if(flag == "changeWidth"){
			width = 940;//940
		} else if(flag == "changeTicks"){
			xAxis.ticks(14);
		}
	} else if(screenWidth == 1280 && screenHeight == 720){
		if(flag == "changeWidth"){
			width = 920;
		} else if(flag == "changeTicks"){
			xAxis.ticks(14);
		}
	} else if (screenWidth == 1152 && screenHeight == 864){
		if(flag == "changeWidth"){
			width = 800;
		} else if(flag == "changeTicks"){
			xAxis.ticks(10);
		}
	} else if(screenWidth == 1024 && screenHeight == 768){
		if(flag == "changeWidth"){
			width = 660;
		} else if(flag == "changeTicks"){
			xAxis.ticks(10);
		}
	} else if(screenWidth == 800 && screenHeight == 600){
		if(flag == "changeWidth"){
			width = 460;
		} else if(flag == "changeTicks"){
			xAxis.ticks(10);
		}
	}  else{
		if(flag == "changeWidth"){
			width = 1550;
		} else if(flag == "changeTicks"){ 
			xAxis.ticks(20);
		}
	} 
}
function paint_xy(){

	  $("svg").remove();//清除之前画的
	  var point_colour = "orange";
	  var line_colour = "#00ACAC";

	  //var parseDate = d3.time.format('%Y-%m-%d %H:%M:%S').parse;
	  var margin = {top: 10, right: 20, bottom: 20, left: 35};
//	  	width = 920;
//	  	width = 1550;
//	  	height = 265 - margin.top - margin.bottom;
//	  	height = 500 - margin.top - margin.bottom;
	  	height = 700 - margin.top - margin.bottom;
	  	getWidthAndTicks_con("changeWidth");

//	  console.log("onload  begin_time = " + begin_time + "; end_time = " + end_time);

	  //横坐标为频率    暂时219~698
	  x = d3.scale.linear()
      .domain([0, 700])
      .range([0, width]);
	  
	  y = d3.scale.linear()//创建一个先行比例尺
	  .domain([0, 520])
	  .range([height, 0]);
	  
	  xAxis = d3.svg.axis()
	  .scale(x)
	  .tickSize([3])  //刻度线长度 默认为[10]  innerTickSize([3]) 内部刻度线长度
	  .orient("bottom"); //orient("bottom")坐标轴的位置


	  getWidthAndTicks_con("changeTicks");
	  
	  //ticks()除最大值和最小值之外最多还有多少刻度
	  yAxis = d3.svg.axis()
	      .scale(y)
	      .tickSize([3])
	      .orient("left")
	      .ticks(15);
	
	  //.on 添加监听事件
	  zoom = d3.behavior.zoom()
	      .x(x)
	      .y(y)
	      .scaleExtent([0.1, 10])
	      .on("zoom", zoomed);	
	
	  //调整画布大小
	  container = d3.select('body').select("#svg_draw_con")
	  	.append('svg')
	  	.call(zoom)
	  	.attr('width', width + margin.left + margin.right)
	  	.attr('height', height + margin.top + margin.bottom);//height
	
	  svg = container.append('g')
	  	.attr('class', 'content')
	  	.attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
	
	  //构建坐标轴
	  svg.append("g")
	      .attr("class", "x axis")
	      .attr("transform", "translate(0," + height + ")")//0,height
	      .call(xAxis)
	      .append('text')
//	      .text(xTipText)
	      .attr('transform', 'translate(' + (width - 25) + ', -5)');
	   
	  svg.append("g")
	      .attr("class", "y axis")
	      .call(yAxis)
	      .append('text')
//	      .text(yTipText)
	      .attr('transform', 'translate(5, 10)');

	  //平移超出部分隐藏
	  svg.append("clipPath")
	  	.attr("id", "clip")
	  	.append("rect")
	  	.attr("width", width)
	  	.attr("height", height);//height
	  
	  //画垂直水平线
	  //线条函数interpolate,可以为：liner、step-before、step-after、basis、basis-open、basis-closed、bundle、cardinal、cardinal-open、cardinal-closed、monotone
	  line = d3.svg.line()
	  .interpolate("cardinal")	
	  .x(function(d) { return x(d.x); })
	  .y(function(d) { return y(d.y); });		
		  	
	  //.attr("vector-effect", "non-scaling-stroke") 线条缩放时不改变粗细
	 svg.selectAll('.line')
	  	.data(data_line)
	  	.enter()
	  	.append("path")
	    .attr("class", "line")
	  	.attr("fill", "none")
	  	.attr("clip-path", "url(#clip)")
	  	.attr('stroke', function(d,i){ 			
	  		return line_colour;
	  	})
	    .attr("d", line);
	 
	 //显示标语
	 text_first_con =  d3.select('body').select("#text_con").append('text');
	 
	 /*
	  var tt = x.invert(width/2);
	  d3.select('body').select("#text_con")
	  .append('text')
	  .attr("id", "title")
	  .attr('class', 'title')
	  .style('color', '#00ACAC')
	  .text(text_con);
	 */
	 text_first_con.attr("id", "title")
	  .attr('class', 'title')
	  .style('color', '#00ACAC')
	  .text(text_con);
	 //画点
	  points = svg.selectAll('.dots')
	  	.data(data_xy)
	  	.enter()
	  	.append("g")
	    .attr("class", "dots")
	  	.attr("clip-path", "url(#clip)");	
	   
	  points.selectAll('.dot')
	  	.data(function(d, index){ 	
	  		  var a = [];
	  		  a.push({'index': index, 'point': d});		
	  		return a;})
	  	.enter()
	  	.append('circle')
	  	.attr('class','dot')
	  	.attr("r", 1.6)
	      .on('mouseover', function() {
	        d3.select(this).transition().duration(500).attr('r', 4);//6
	      })
	      .on('mouseout', function() {
	      	d3.select(this).transition().duration(500).attr('r', 1.6);//4
	      })
	  	.attr('fill', function(d,i){ 	//圆点颜色
	  		return point_colour;
	  	})	
	  	.attr("transform", function(d) { 
	  		return "translate(" + x(d.point.x) + "," + y(d.point.y) + ")"; }
	  	);
	 
	  tips = svg.append('g').attr('class', 'tips');
	  tips.append('rect')
	    .attr('class', 'tips-border')
	    .attr('width', 200)
	    .attr('height', 50)
	    .attr('rx', 10)
	    .attr('ry', 10);

	  container.on('mousemove', function() {
	      var m = d3.mouse(this);  //返回当前鼠标的坐标，是一个数组，分别是x和y坐标
	      var cx = m[0] - margin.left; 

//	    	  d3.select('.tips').style('display', 'block');
	      showWording(cx);

	    })
	    .on('mouseout', function() {
	      d3.select('.tips').style('display', 'none');
	    });	
	  	
	  function zoomed() {
	  	svg.select(".x.axis").call(xAxis);
	  	svg.select(".y.axis").call(yAxis);   
	  	svg.selectAll('path.line').attr('d', line);  
	  	
	  	points.selectAll('circle').attr("transform", function(d) { 
	  		return "translate(" + x(d.point.x) + "," + y(d.point.y) + ")"; }
	  	);
	  }
	
	  function showWording(cx) {
		  
  		 showTips = 0;
		 xTipText = "x";
		 yTipText = "y";

		  	
		  if(wording1 == undefined || wording1 == null){
			  wording1 = tips.append('text')
			  .attr('class', 'tips-text')
			  .style('font-size', 5)
			  .attr('x', 10)
			  .attr('y', 20)
			  .text('');
		  }
		  
		  if(wording2 == undefined || wording2 == null){
			  wording2 = tips.append('text')
			  .attr('class', 'tips-text')
			  .attr('x', 10)
			  .attr('y', 40)
			  .text('');
		  }
		  
	      var x0 = x.invert(cx);  //跟据传入的横坐标数值返回该横坐标的实际数据上的值，在本例中返回一个日期
	      var i = (d3.bisector(function(d) {  //d3.bisector 自定义一个二分函数 
	      	return d.x;
	      }).left)(data_xy[showTips], x0, 1);
	      
	     //下面的i是根据返回的日期反向得到data数组中的元素位置。判断鼠标在两个日期之间离哪个更近
	      var d0 = data_xy[showTips][i - 1];
	      var d1 = data_xy[showTips][i] || {};
	      var d;
	      if(d0 != undefined){
	    	  d3.select('.tips').style('display', 'block');
	    	  d = x0 - d0.x > d1.x - x0 ? d1 : d0;
	      }else{
	    	  d3.select('.tips').style('display', 'none');
	    	  return;
	      }
	        
	      wording1.text(formatWording(d));
	      wording2.text(yTipText + ':' + d.y);
	      
	      function formatWording(d) {

	    		  return xTipText + ':' + d.x;

	      }
	      
	      var x1 = x(d.x),
	        y1 = y(d.y);
	      // 处理超出边界的情况
	      var dx = x1 > width ? x1 - width + 200 : x1 + 200 > width ? 200 : 0;
	      var dy = y1 > height ? y1 - height + 50 : y1 + 50 > height ? 50 : 0;
	      x1 -= dx;
	      y1 -= dy;
	      d3.select('.tips')
	        .attr('transform', 'translate(' + x1 + ',' + y1 + ')');
	  }
}


function paint(){
	  $("svg").remove();
	  var colors = [
	          	'#00ACAC',
	          	'green',
	          	'red',
	          	'#FF5B57',
	          	'orange'
	          ]
	  var point_colour = "orange";
	  var line_colour = "orange";

	  //var parseDate = d3.time.format('%Y-%m-%d %H:%M:%S').parse;
	  var margin = {top: 10, right: 20, bottom: 20, left: 35};
//	  	width = 920;
//	  	width = 1550;
//	  	height = 265 - margin.top - margin.bottom;
	  	height = 500 - margin.top - margin.bottom;
//		width = document.body.clientWidth-margin.left-margin.right;
	  	getWidthAndTicks("changeWidth");
  
	  	bt = $(document.getElementById('query_begin_time')).val();
	  	et = $(document.getElementById('query_end_time')).val();
	  	if(bt != "" && et != ""){
	  		begin_time = new Date(bt);
	  		end_time = new Date(et);
	  	} else{
	  		end_time = new Date();
	  		begin_time = new Date(end_time.getTime() - 24*60*60*1000); //前一天
	  	}
//	  console.log("onload  begin_time = " + begin_time + "; end_time = " + end_time);
	  if(item_choice_pic == "frequency_signalStrength"){
		  //横坐标为频率    暂时219~698
		  x = d3.scale.linear()
	      .domain([210, 700])
	      .range([0, width]);
		  
		  y = d3.scale.linear()
		  .domain([-110, -48])
		  .range([height, 0]);
		  
		  xAxis = d3.svg.axis()
		  .scale(x)
		  .tickSize([3])  //刻度线长度 默认为[10]  innerTickSize([3]) 内部刻度线长度
		  .orient("bottom"); //orient("bottom")坐标轴的位置
	  }else if(item_choice_pic == "combine"){
		  //横坐标为时间
		  x = d3.time.scale()
		  .domain([begin_time, end_time])
		  .range([0, width]);
		  
		  y = d3.scale.linear()
		  .domain([0, 30])
		  .range([height, 0]);
		  
		  xAxis = d3.svg.axis()
		  .scale(x)
		  .tickFormat(d3.time.format("%H:%M"))
		  .tickSize([3])  //刻度线长度 默认为[10]  innerTickSize([3]) 内部刻度线长度
		  .orient("bottom");
		  //.ticks(20);	 
	  }
	  if(kind_data != ""){
		  //横坐标为频率    暂时219~698
		  x = d3.scale.linear()
	      .domain([10000, 20000])
	      .range([0, width]);
		  
		  y = d3.scale.linear()
		  .domain([0, 100])
		  .range([height, 0]);
		  
		  xAxis = d3.svg.axis()
		  .scale(x)
		  .tickSize([3])  //刻度线长度 默认为[10]  innerTickSize([3]) 内部刻度线长度
		  .orient("bottom"); //orient("bottom")坐标轴的位置 		  
	  }  
	  
 
	  getWidthAndTicks("changeTicks");
	  
	  //ticks()除最大值和最小值之外最多还有多少刻度
	  yAxis = d3.svg.axis()
	      .scale(y)
	      .tickSize([3])
	      .orient("left")
	      .ticks(15);
	
	  //.on 添加监听事件
	  zoom = d3.behavior.zoom()
	      .x(x)
	      .y(y)
	      .scaleExtent([0.1, 10])
	      .on("zoom", zoomed);	
	
	  container = d3.select('body').select("#svg_draw")
	  	.append('svg')
	  	.call(zoom)
	  	.attr('width', width + margin.left + margin.right)
	  	.attr('height', height + margin.top + margin.bottom);
	
	  svg = container.append('g')
	  	.attr('class', 'content')
	  	.attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
	
	  //构建坐标轴
	  svg.append("g")
	      .attr("class", "x axis")
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis)
	      .append('text')
//	      .text(xTipText)
	      .attr('transform', 'translate(' + (width - 25) + ', -5)');
	   
	  svg.append("g")
	      .attr("class", "y axis")
	      .call(yAxis)
	      .append('text')
//	      .text(yTipText)
	      .attr('transform', 'translate(5, 10)');
  
	  //平移超出部分隐藏
	  svg.append("clipPath")
	  	.attr("id", "clip")
	  	.append("rect")
	  	.attr("width", width)
	  	.attr("height", height);
	
	  if(item_choice_pic == "frequency_signalStrength"){
		  //线条函数interpolate,可以为：liner、step-before、step-after、basis、basis-open、basis-closed、bundle、cardinal、cardinal-open、cardinal-closed、monotone
		  line = d3.svg.line()
		  .interpolate("liner")	//cardinal
		  .x(function(d) { return x(d.x); })
		  .y(function(d) { return y(d.y); });		
		  
	  }else{
		  //线条函数
		  line = d3.svg.line()
		  .x(function(d) { return x(d.x); })
		  .y(function(d) { return y(d.y); })
	  }
	
	  //.attr("vector-effect", "non-scaling-stroke") 线条缩放时不改变粗细
	 svg.selectAll('.line')
	  	.data(data)
	  	.enter()
	  	.append("path")
	    .attr("class", "line")
	  	.attr("fill", "none")
	  	.attr("clip-path", "url(#clip)")
	  	.attr('stroke', function(d,i){ 			
	  		return line_colour;//colors[i%colors.length]
	  	})
	    .attr("d", line);

	  if(item_choice_pic == "combine"){
		  var tt = x.invert(width/2);
		  d3.select('body').select("#text")
		  .append('text')
		  .attr("id", "title")
		  .attr('class', 'title')
		  .style('color', '#00ACAC')
		  .text("日期"+d3.time.format('%Y-%m-%d')(tt)+" 调制方式:QAM64 符号率:6900(Ksps)");
	  }

	  points = svg.selectAll('.dots')
	  	.data(data)
	  	.enter()
	  	.append("g")
	    .attr("class", "dots")
	  	.attr("clip-path", "url(#clip)");	

	  
	  points.selectAll('.dot')
	  	.data(function(d, index){ 	
	  			var a = [];
	  			d.forEach(function(point,i){
	  				a.push({'index': index, 'point': point});
	  		});		
	  		return a;})
	  	.enter()
	  	.append('circle')
	  	.attr('class','dot')
	  	.attr("r", 4)
	      .on('mouseover', function() {
	        d3.select(this).transition().duration(500).attr('r', 6);	        		     		      
	      })
	      .on('mouseout', function() {
	      	d3.select(this).transition().duration(500).attr('r', 4);

	      })
	  	.attr('fill', function(d,i){ 	//圆点颜色
	  		return point_colour;//colors[d.index%colors.length]
	  	})	
	  	.attr("transform", function(d) { 
	  		return "translate(" + x(d.point.x) + "," + y(d.point.y) + ")"; }
	  	);
	 
	  tips = svg.append('g').attr('class', 'tips');
	  tips.append('rect')
	    .attr('class', 'tips-border')
	    .attr('width', 260)
	    .attr('height', 50)
	    .attr('rx', 10)
	    .attr('ry', 10);

	  container.on('mousemove', function() {
	      var m = d3.mouse(this);  //返回当前鼠标的坐标，是一个数组，分别是x和y坐标
	      cx = m[0] - margin.left; //var cx = m[0] - margin.left; 
	      if(item_choice_pic == "combine"){
	    	  title_time(cx);
	      }
	      	      
	      if(select_checkbox){
//	    	  d3.select('.tips').style('display', 'block');
	    	  showWording(cx);
	      }else if(!select_checkbox){
	    	  d3.select('.tips').style('display', 'none');
	      }
	    })
	    .on('mouseout', function() {
	      d3.select('.tips').style('display', 'none');
	    });	
	  	
	  function zoomed() {
	  	svg.select(".x.axis").call(xAxis);
	  	svg.select(".y.axis").call(yAxis);   
	  	svg.selectAll('path.line').attr('d', line);  
	  	
	  	points.selectAll('circle').attr("transform", function(d) { 
	  		return "translate(" + x(d.point.x) + "," + y(d.point.y) + ")"; }
	  	);
	  }
	
	  function showWording(cx) {
		  //var showTips;
		  if(item_choice_pic == "combine"){
		  		if(item_choice == "bitErrorRate"){
		  			showTips = 0;
		  			xTipText = "时间";
		  			yTipText = "误码率";
		  			yUnit="(Err)";
		  			zUnit="(10Khz)";
		  		} else if(item_choice == "signalStrength"){
		  			showTips = 1;
		  			xTipText = "时间";
		  			yTipText = "信号强度";
		  			yUnit="(dBuV)";
		  			zUnit="(10Khz)";
		  		} else if(item_choice == "currentFrequency"){
		  			showTips = 2;
		  			xTipText = "时间";
		  			yTipText = "当前频点";
		  		} else if(item_choice == "symbolRate"){
		  			showTips = 3;
		  			xTipText = "时间";
		  			yTipText = "符号率";
		  		} else if(item_choice == "sinalToNoice"){
		  			showTips = 4;
		  			xTipText = "时间";
		  			yTipText = "信噪比";
		  			yUnit="(dB)";
		  			zUnit="(10Khz)";
		  		} 
		  }else if(item_choice_pic == "frequency_signalStrength"){
		  		showTips = 0;
				xTipText = "频率";
				yTipText = "信号强度";
		  }
		  
		  if(wording1 == undefined || wording1 == null){
			  wording1 = tips.append('text')
			  .attr('class', 'tips-text')
			  .style('font-size', 5)
			  .attr('x', 10)
			  .attr('y', 20)
//			  .text('');
		  }
		  
		  if(wording2 == undefined || wording2 == null){
			  wording2 = tips.append('text')
			  .attr('class', 'tips-text')
			  .attr('x', 10)
			  .attr('y', 40)
//			  .text('');
		  }

		  data_tip.sort(function(a,b){
			 return a.x-b.x; 
		  });
		  
		  if(kind_data == ""){
			  data_f.sort(function(a,b){
				 return a.x-b.x; 
			  });
		  }
	      x0 = x.invert(cx);  //跟据传入的横坐标数值返回该横坐标的实际数据上的值，在本例中返回一个日期
	      var i = d3.bisector(function(d) {  //d3.bisector 自定义一个二分函数 
	      	return d.x;
	      }).left(data_tip, x0,1);//data[showTips]
	      
	
	     //下面的i是根据返回的日期反向得到data数组中的元素位置。判断鼠标在两个日期之间离哪个更近
	      var d0 = data_tip[i - 1];
	      var d1 = data_tip[i] || {};
	      var d;
	      var d_f;
	      if(d0 != undefined){
	    	  d3.select('.tips').style('display', 'block');
	    	  d = x0 - d0.x > d1.x - x0 ? d1 : d0;
	    	  
	    	  if(d==d1)
	    		  d_f = data_f[i];
	    	  else(d==0)
	    	      d_f = data_f[i-1];

	      }else{
	    	  d3.select('.tips').style('display', 'none');
	    	  return;
	      }
	        
	      wording1.text(formatWording(d));
	      
	      wording2.text(yTipText + '：' + d.y+ yUnit +' '+' 频点：'+d_f.y +zUnit);

	      
	      function formatWording(d) {
	    	  if(item_choice_pic == "frequency_signalStrength"){
	    		  return xTipText + ':' + d.x;
	    	  }else if(item_choice_pic == "combine"){
	    		  return '时间：' + d3.time.format('%Y-%m-%d %H:%M:%S')(d.x);
	    	  }
	      }
	      
	      var x1 = x(d.x),
	        y1 = y(d.y);
	      // 处理超出边界的情况
	      var dx = x1 > width ? x1 - width + 200 : x1 + 200 > width ? 200 : 0;
	      var dy = y1 > height ? y1 - height + 50 : y1 + 50 > height ? 50 : 0;
	      x1 -= dx;
	      y1 -= dy;

	      d3.select('.tips')
	        .attr('transform', 'translate(' + x1 + ',' + y1 + ')');
	  }
}


function paint_full(){
	  $("svg").remove();
	  var colors = [
	          	'#00ACAC',
	          	'green',
	          	'red',
	          	'#FF5B57',
	          	'orange'
	          ]
	  var point_colour = "orange";
	  var line_colour = "orange";

	  //var parseDate = d3.time.format('%Y-%m-%d %H:%M:%S').parse;
	  var margin = {top: 10, right: 20, bottom: 20, left: 35};
//	  	width = 920;
//	  	width = 1550;
//	  	height = 265 - margin.top - margin.bottom;
	  	height = 500 - margin.top - margin.bottom;
//		width = document.body.clientWidth-margin.left-margin.right;
	  	getWidthAndTicks("changeWidth");

	  	bt = $(document.getElementById('query_begin_time')).val();
	  	et = $(document.getElementById('query_end_time')).val();
	  	if(bt != "" && et != ""){
	  		begin_time = new Date(bt);
	  		end_time = new Date(et);
	  	} else{
	  		end_time = new Date();
	  		begin_time = new Date(end_time.getTime() - 24*60*60*1000); //前一天
	  	}
//	  console.log("onload  begin_time = " + begin_time + "; end_time = " + end_time);
	  if(kind_data != ""){
		  //横坐标为频率    暂时219~698
		  x = d3.scale.linear()
	      .domain([10000, 20000])
	      .range([0, width]);
		  
		  y = d3.scale.linear()
		  .domain([0, 100])
		  .range([height, 0]);
		  
		  xAxis = d3.svg.axis()
		  .scale(x)
		  .tickSize([3])  //刻度线长度 默认为[10]  innerTickSize([3]) 内部刻度线长度
		  .orient("bottom"); //orient("bottom")坐标轴的位置 		  
	  }  
	  

	  getWidthAndTicks("changeTicks");
	  
	  //ticks()除最大值和最小值之外最多还有多少刻度
	  yAxis = d3.svg.axis()
	      .scale(y)
	      .tickSize([3])
	      .orient("left")
	      .ticks(15);
	
	  //.on 添加监听事件
	  zoom = d3.behavior.zoom()
	      .x(x)
	      .y(y)
	      .scaleExtent([0.1, 10])
	      .on("zoom", zoomed);	
	
	  container = d3.select('body').select("#svg_draw_full")
	  	.append('svg')
	  	.call(zoom)
	  	.attr('width', width + margin.left + margin.right)
	  	.attr('height', height + margin.top + margin.bottom);
	
	  svg = container.append('g')
	  	.attr('class', 'content')
	  	.attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
	
	  //构建坐标轴
	  svg.append("g")
	      .attr("class", "x axis")
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis)
	      .append('text')
//	      .text(xTipText)
	      .attr('transform', 'translate(' + (width - 25) + ', -5)');
	   
	  svg.append("g")
	      .attr("class", "y axis")
	      .call(yAxis)
	      .append('text')
//	      .text(yTipText)
	      .attr('transform', 'translate(5, 10)');

	  //平移超出部分隐藏
	  svg.append("clipPath")
	  	.attr("id", "clip")
	  	.append("rect")
	  	.attr("width", width)
	  	.attr("height", height);

		  //线条函数interpolate,可以为：liner、step-before、step-after、basis、basis-open、basis-closed、bundle、cardinal、cardinal-open、cardinal-closed、monotone
	  line = d3.svg.line()
		  .interpolate("liner")	//cardinal
		  .x(function(d) { return x(d.x); })
		  .y(function(d) { return y(d.y); });		

	
	  //.attr("vector-effect", "non-scaling-stroke") 线条缩放时不改变粗细
	 svg.selectAll('.line')
	  	.data(data)
	  	.enter()
	  	.append("path")
	    .attr("class", "line")
	  	.attr("fill", "none")
	  	.attr("clip-path", "url(#clip)")
	  	.attr('stroke', function(d,i){ 			
	  		return line_colour;//colors[i%colors.length]
	  	})
	    .attr("d", line);


	  points = svg.selectAll('.dots')
	  	.data(data)
	  	.enter()
	  	.append("g")
	    .attr("class", "dots")
	  	.attr("clip-path", "url(#clip)");	

	  
	  points.selectAll('.dot')
	  	.data(function(d, index){ 	
	  			var a = [];
	  			d.forEach(function(point,i){
	  				a.push({'index': index, 'point': point});
	  		});		
	  		return a;})
	  	.enter()
	  	.append('circle')
	  	.attr('class','dot')
	  	.attr("r", 4)
	      .on('mouseover', function() {
	        d3.select(this).transition().duration(500).attr('r', 6);	        		     		      
	      })
	      .on('mouseout', function() {
	      	d3.select(this).transition().duration(500).attr('r', 4);

	      })
	  	.attr('fill', function(d,i){ 	//圆点颜色
	  		return point_colour;//colors[d.index%colors.length]
	  	})	
	  	.attr("transform", function(d) { 
	  		return "translate(" + x(d.point.x) + "," + y(d.point.y) + ")"; }
	  	);
	 
	  tips = svg.append('g').attr('class', 'tips');
	  tips.append('rect')
	    .attr('class', 'tips-border')
	    .attr('width', 200)
	    .attr('height', 50)
	    .attr('rx', 10)
	    .attr('ry', 10);

	  container.on('mousemove', function() {
	      var m = d3.mouse(this);  //返回当前鼠标的坐标，是一个数组，分别是x和y坐标
	      cx = m[0] - margin.left; //var cx = m[0] - margin.left; 
	      	      
	      if(kind_data != ""){
	    	  showWording(cx);
	      }
	    })
	    .on('mouseout', function() {
	      d3.select('.tips').style('display', 'none');
	    });	
	  	
	  function zoomed() {
	  	svg.select(".x.axis").call(xAxis);
	  	svg.select(".y.axis").call(yAxis);   
	  	svg.selectAll('path.line').attr('d', line);  
	  	
	  	points.selectAll('circle').attr("transform", function(d) { 
	  		return "translate(" + x(d.point.x) + "," + y(d.point.y) + ")"; }
	  	);
	  }
	
	  function showWording(cx) {
		  //var showTips;
		  if(kind_data != ""){
			  	if(kind_data == "LEVEL"){
		  			xTipText = "频点";
		  			yTipText = "信号强度";
		  			xUnit="(10Khz)";
		  			yUnit="(dBuV)";
		  		} else if(kind_data == "SNR"){
		  			xTipText = "频点";
		  			yTipText = "信噪比";	
		  			xUnit="(10Khz)";
		  			yUnit="(dB)";
		  		} else if(kind_data == "BER"){
		  			xTipText = "频点";
		  			yTipText = "误码率";	
		  			xUnit="(10Khz)";
		  			yUnit="(Err)";
		  		}
		  }
		  	
		  if(wording3 == undefined || wording3 == null || wording3 == ""){
			  wording3 = tips.append('text')
			  .attr('class', 'tips-text')
			  .style('font-size', 5)
			  .attr('x', 10)
			  .attr('y', 20)
//			  .text('');
		  }
		  
		  if(wording4 == undefined || wording4 == null || wording4 == ""){
			  wording4 = tips.append('text')
			  .attr('class', 'tips-text')
			  .attr('x', 10)
			  .attr('y', 40)
//			  .text('');
		  }

		  data_tip.sort(function(a,b){
			 return a.x-b.x; 
		  });
		  
		  if(kind_data == ""){
			  data_f.sort(function(a,b){
				 return a.x-b.x; 
			  });
		  }
	      x0 = x.invert(cx);  //跟据传入的横坐标数值返回该横坐标的实际数据上的值，在本例中返回一个日期
	      var i = d3.bisector(function(d) {  //d3.bisector 自定义一个二分函数 
	      	return d.x;
	      }).left(data_tip, x0,1);//data[showTips]
	      
	
	     //下面的i是根据返回的日期反向得到data数组中的元素位置。判断鼠标在两个日期之间离哪个更近
	      var d0 = data_tip[i - 1];
	      var d1 = data_tip[i] || {};
	      var d;
	      var d_f;
	      if(d0 != undefined){
	    	  d3.select('.tips').style('display', 'block');
	    	  d = x0 - d0.x > d1.x - x0 ? d1 : d0;
	    	  
			  if(kind_data == ""){
		    	  if(d==d1)
		    		  d_f = data_f[i];
		    	  else(d==0)
		    	      d_f = data_f[i-1];
			  }
	      }else{
	    	  d3.select('.tips').style('display', 'none');
	    	  return;
	      }
	        
	      wording3.text(formatWording(d));
	      

	      wording4.text(yTipText + '：' + d.y + yUnit);
	      
	      function formatWording(d) {
	    		  return xTipText + ':' + d.x + xUnit;
	      }
	      
	      var x1 = x(d.x),
	        y1 = y(d.y);
	      // 处理超出边界的情况
	      var dx = x1 > width ? x1 - width + 200 : x1 + 200 > width ? 200 : 0;
	      var dy = y1 > height ? y1 - height + 50 : y1 + 50 > height ? 50 : 0;
	      x1 -= dx;
	      y1 -= dy;

	      d3.select('.tips')
	        .attr('transform', 'translate(' + x1 + ',' + y1 + ')');
	  }
}
//end paint();

function title_time(cx){
	var title_time = x.invert(cx);
	if(title_time != ""){
		title_time = d3.time.format('%Y-%m-%d')(title_time);
		$("#title").text("日期:"+title_time+" 调制方式:QAM64 符号率:6900(Ksps)");
	} 
}

function queryByStartEndTime(){
	 bt = $(document.getElementById('query_begin_time')).val();
  	 et = $(document.getElementById('query_end_time')).val();
  	 history.go(0);
}