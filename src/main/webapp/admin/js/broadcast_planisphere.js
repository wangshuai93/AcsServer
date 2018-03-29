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

//获取不同分辨率
var screenWidth = window.screen.width;
var screenHeight = window.screen.height;

var data;
var item_choice = $("#item_choice").val();

function getchoice(){
	item_choice = $("#item_choice").val();
	
	$("#text").remove();
	$("#svg_draw").remove();
	getBroadcastTaskResultList(device_id, successCallback);
	 
	if(data != undefined){
		draw();
		getTipsText();
	} 
}

var device_id = $("[name = 'm_device_id']").attr("value");
getBroadcastTaskResultList(device_id, successCallback);

function successCallback(dataList){
	if(null != dataList && dataList.length != 0){
		//二维数组需先建立一维数组，再建二维    arr[0]表示一维长度为1
		var arr = new Array();
		arr[0] = Array.apply(0, Array(dataList.length)).map(function(item, j) {
		    j++;
		    if(item_choice == "bitErrorRate"){
				return {x: new Date(dataList[j-1].m_reportTime), y: dataList[j-1].m_symbolRate}
			} else if(item_choice == "signalStrength"){
				return {x: new Date(dataList[j-1].m_reportTime), y: dataList[j-1].m_signalStrength}
			} else if(item_choice == "signalQaulity"){
				return {x: new Date(dataList[j-1].m_reportTime), y: dataList[j-1].m_signalQaulity}
			} else if(item_choice == "currentFrequency"){
				return {x: new Date(dataList[j-1].m_reportTime), y: dataList[j-1].m_currentFrequency}
			} else if(item_choice == "frequencyDrift"){
				return {x: new Date(dataList[j-1].m_reportTime), y: dataList[j-1].m_frequencyDrift}
			} else if(item_choice == "symbolRate"){
				return {x: new Date(dataList[j-1].m_reportTime), y: dataList[j-1].m_symbolRate}
			} /*else if(item_choice == "frequency_signalStrength"){
				return {x: dataList[j-1].m_currentFrequency, y: dataList[j-1].m_signalStrength}
			}*/
		});
		if(item_choice == "frequency_signalStrength"){
			//数组排序，若后一个横坐标小于前一个，图像会出现往回画的现象
			dataList.sort(function(x, y){
				return x.m_currentFrequency - y.m_currentFrequency;
			});
			arr[0] = Array.apply(0, Array(dataList.length)).map(function(item, j) {
			    j++;
			    return {x: dataList[j-1].m_currentFrequency, y: dataList[j-1].m_signalStrength}
			});
		}
		
		data = arr;
		$("#text").show();
		$("#svg_draw").show();
		$("#white_block").show();
		$("#item_choice_td").show();
		$("#choice_td").show();
		paint();
	} else{
		$("#text").hide();
		$("#svg_draw").hide();
		$("#white_block").hide();
		$("#item_choice_td").hide();
		$("#choice_td").hide();
	}
}

getTipsText();

function draw(){
	var html = "";
	var planisphere_html = 
		'<div id="text" style="text-align: center; background: #2D353C; margin-left: 1.5%; margin-top:0.1%; margin-right: 1.5%; height: 25px; padding-top: 1%;">' + 
		'</div>' + 
		'<div id="svg_draw" class="morris-inverse" style="background: #2D353C; margin-left: 1.5%; margin-right: 1.5%;">' + 
		'</div>'
	html += planisphere_html;
	$("#div_block").html(html);
}

function getTipsText(){
	if(item_choice == "bitErrorRate"){
		xTipText = "日期";
		yTipText = "误码率";
	} else if(item_choice == "signalStrength"){
		xTipText = "日期";
		yTipText = "信号强度";
	} else if(item_choice == "signalQaulity"){
		xTipText = "日期";
		yTipText = "信号质量";
	} else if(item_choice == "currentFrequency"){
		xTipText = "日期";
		yTipText = "当前频点";
	} else if(item_choice == "frequencyDrift"){
		xTipText = "日期";
		yTipText = "频点漂移";
	} else if(item_choice == "symbolRate"){
		xTipText = "日期";
		yTipText = "符号率";
	} else if(item_choice == "frequency_signalStrength"){
		xTipText = "频率";
		yTipText = "信号强度";
	}
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
	}   
}

function paint(){
	  var colors = [
	          	'#00ACAC',
	          	'green',
	          	'red',
	          	'#FF5B57'
	          ]

	  //var parseDate = d3.time.format('%Y-%m-%d %H:%M:%S').parse;
	  var margin = {top: 10, right: 20, bottom: 20, left: 35};
//	  	width = 920;
//	  	width = 1550;
	  	height = 265 - margin.top - margin.bottom;
	  	getWidthAndTicks("changeWidth");
	  	
	  
	  	var bt = $(document.getElementById('query_begin_time')).val();
	  	var et = $(document.getElementById('query_end_time')).val();
	  	if(bt != "" && et != ""){
	  		begin_time = new Date(bt);
	  		end_time = new Date(et);
	  	} else{
	  		end_time = new Date();
	  		begin_time = new Date(end_time.getTime() - 24*60*60*1000); //前一天
	  	}
//	  console.log("onload  begin_time = " + begin_time + "; end_time = " + end_time);
	  if(item_choice == "frequency_signalStrength"){
		  //横坐标为频率    暂时219~698
		  x = d3.scale.linear()
	      .domain([210, 700])
	      .range([0, width]);
	  }else{
		  //横坐标为时间
		  x = d3.time.scale()
		  .domain([begin_time, end_time])
		  .range([0, width]);
	  }
	   
	  if(item_choice == "frequency_signalStrength"){
		  y = d3.scale.linear()
		  .domain([-110, -48])
		  .range([height, 0]);
	  }else{
		  y = d3.scale.linear()
		  .domain([0, 8])
		  .range([height, 0]);
	  }
	  	
	  //orient("bottom")坐标轴的位置
	  if(item_choice == "frequency_signalStrength"){
		  xAxis = d3.svg.axis()
		  .scale(x)
		  .tickSize([3])  //刻度线长度 默认为[10]  innerTickSize([3]) 内部刻度线长度
		  .orient("bottom");
	  }else{
		  xAxis = d3.svg.axis()
		  .scale(x)
		  .tickFormat(d3.time.format("%H:%M"))
		  .tickSize([3])  //刻度线长度 默认为[10]  innerTickSize([3]) 内部刻度线长度
		  .orient("bottom");
		  //.ticks(20);	 
	  }
	  getWidthAndTicks("changeTicks");
	  
	  //ticks()除最大值和最小值之外最多还有多少刻度
	  yAxis = d3.svg.axis()
	      .scale(y)
	      .tickSize([3])
	      .orient("left")
	      .ticks(10);
	
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
	      .attr("id", "x_axis")
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis)
	      .append('text')
	      .text(xTipText)
	      .attr('transform', 'translate(' + (width - 25) + ', -5)');
	   
	  svg.append("g")
	      .attr("class", "y axis")
	      .attr("id", "y_axis")
	      .call(yAxis)
	      .append('text')
	      .text(yTipText)
	      .attr('transform', 'translate(5, 10)');
	   
	  //平移超出部分隐藏
	  svg.append("clipPath")
	  	.attr("id", "clip")
	  	.append("rect")
	  	.attr("width", width)
	  	.attr("height", height);
	
	  if(item_choice == "frequency_signalStrength"){
		  //线条函数interpolate,可以为：liner、step-before、step-after、basis、basis-open、basis-closed、bundle、cardinal、cardinal-open、cardinal-closed、monotone
		  line = d3.svg.line()
		  .interpolate("cardinal")	
		  .x(function(d) { return x(d.x); })
		  .y(function(d) { return y(d.y); });		
		  
	  }else{
		  //线条函数
		  line = d3.svg.line()
		  .x(function(d) { return x(d.x); })
		  .y(function(d) { return y(d.y); });		
	  }
	  	
	
	  //.attr("vector-effect", "non-scaling-stroke") 线条缩放时不改变粗细
	  svg.selectAll('.line')
	  	.data(data)
	  	.enter()
	  	.append("path")
	    .attr("class", "line")
	  	.attr("clip-path", "url(#clip)")
	  	.attr('stroke', function(d,i){ 			
	  		return colors[i%colors.length];
	  	})
	    .attr("d", line);		
	
	  if(item_choice != "frequency_signalStrength"){
		  var tt = x.invert(width/2);
		  d3.select('body').select("#text")
		  .append('text')
		  .attr("id", "title")
		  .attr('class', 'title')
		  .style('color', '#00ACAC')
		  .text(d3.time.format('%Y-%m-%d')(tt));
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
	  	.attr('fill', function(d,i){ 	
	  		return colors[d.index%colors.length];
	  	})	
	  	.attr("transform", function(d) { 
	  		return "translate(" + x(d.point.x) + "," + y(d.point.y) + ")"; }
	  	);
	
	  container.on('mousemove', function() {
//	  	console.log("mousemove");
	      var m = d3.mouse(this);  //返回当前鼠标的坐标，是一个数组，分别是x和y坐标
	      var cx = m[0] - margin.left;  //container.on('mousemove'
	      //var cx = m[0];    //svg.on('mousemove'
	      if(item_choice != "frequency_signalStrength"){
	    	  title_time(cx);
	      }
	      showWording(cx);
	      d3.select('.tips').style('display', 'block');
	    })
	    .on('mouseout', function() {
	      d3.select('.tips').style('display', 'none');
	    });	
	  	
	  function zoomed() {
//	  	console.log("zoomed  begin_time = " + begin_time + "; end_time = " + end_time);
	  	svg.select(".x.axis").call(xAxis);
	  	svg.select(".y.axis").call(yAxis);   
	  	svg.selectAll('path.line').attr('d', line);  
	  	points.selectAll('circle').attr("transform", function(d) { 
	  		return "translate(" + x(d.point.x) + "," + y(d.point.y) + ")"; }
	  	);
	  }
	
	  var tips = svg.append('g').attr('class', 'tips');
	
	  tips.append('rect')
	    .attr('class', 'tips-border')
	    .attr('width', 200)
	    .attr('height', 50)
	    .attr('rx', 10)
	    .attr('ry', 10);
	
	  function showWording(cx) {
	      var x0 = x.invert(cx);  //跟据传入的横坐标数值返回该横坐标的实际数据上的值，在本例中返回一个日期
	      var i = (d3.bisector(function(d) {  //d3.bisector 自定义一个二分函数 
	      	return d.x;
	      }).left)(data[0], x0, 1);
	      
	     //下面的i是根据返回的日期反向得到data数组中的元素位置。判断鼠标在两个日期之间离哪个更近
	      var d0 = data[0][i - 1],
	        d1 = data[0][i] || {},
	        d = x0 - d0.x > d1.x - x0 ? d1 : d0;
	        
	      wording1.text(formatWording(d));
	      wording2.text(yTipText + ':' + d.y);
	
	      function formatWording(d) {
	    	  if(item_choice == "frequency_signalStrength"){
	    		  return xTipText + ':' + d.x;
	    	  }else{
	    		  return '日期：' + d3.time.format('%Y-%m-%d %H:%M:%S')(d.x);
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
	
	  var wording1 = tips.append('text')
	  	.attr('class', 'tips-text')
	  	.style('font-size', 5)
	  	.attr('x', 10)
	  	.attr('y', 20)
	  	.text('');
	
	  var wording2 = tips.append('text')
	  	.attr('class', 'tips-text')
	  	.attr('x', 10)
	  	.attr('y', 40)
	  	.text('');
	
}

function title_time(cx){
	var title_time = x.invert(cx);
	if(title_time != ""){
		title_time = d3.time.format('%Y-%m-%d')(title_time);
		$("#title").text(title_time);
	} 
}