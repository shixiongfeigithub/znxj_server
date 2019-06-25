<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <style>
        .selected{background: #35A7E7;
            color: white;}
    </style>
    <script type="text/javascript">
        var count;
        $(function(){
            var x=document.getElementById('table').rows[0].cells;
            var len=$("#statuslen").val();
            for(var i=0;i<len;i++){
                if(i==0){
                    x[0].colSpan="7";
                }else
                    x[i].colSpan="2";
            }
            var y=document.getElementById('table').rows;
//            if(count )
//            var count=0;
//            var no=0;
//            for(var j=2;j<y.length;j++){
//                if(y[j].cells[0].innerHTML==y[j-1].cells[0].innerHTML){
//                    count++;
//                }else
//                    no=j;
//                y[no].cells[0].rowSpan=count;
//            }

        })
        function exportexcel(){
            $("#myModal").modal("show");
            $("#arealist").css("display","none");
            $("#dotime").css("display","block");
//            var taskname=$("#taskname").val();
//            var donetime=$("#donetime").val();
//            window.location="exportExcel?type="+type+"&taskid="+taskid+"&taskname="+taskname+"&donetime="+donetime;
        }
        function exportexce2(taskid,type) {
            $("#myModal").modal("hide");
            var taskname=$("#taskname").val();
//            var donetime=$("#donetime").val();
            var time1=$("#time1").val();
            var time2=$("#time2").val();
            window.location="exportExcel?type="+type+"&taskid="+taskid+"&taskname="+taskname+"&time1="+time1+"&time2="+time2;
        }
        function showimg(img,audio,video){
            var imgs=new Array();
            var audios=new Array();
            var videos=new Array();
            if(""!=img && img!="undefined"&&img!="null"){
                imgs=JSON.parse(img);
            }else{
                imgs="";
            }

            if(""!=audio && audio!="undefined"&&audio!="null"){
                audios=JSON.parse(audio);
            }else{
                audios="";
            }
            if(""!=video && video!="undefined" &&video!="null"){
                videos=JSON.parse(video);
            }else{
                videos="";
            }
            var errorul=$("#errorul");
            errorul.empty();
            if(imgs==""&&audios==""&&videos==""){
                alert("暂时没有其他异常信息");
            }else {
                for (var i = 0; i < imgs.length; i++) {
                    var j = parseInt(i) + 1;
                    errorul.append("<li class='list-group-item'  onclick=showimgs(this,'" + imgs[i] + "')>图片" + j + "</li>");
                }

                for (var m = 0; m < audios.length; m++) {
                    var n = parseInt(m) + 1;
                    errorul.append("<li class='list-group-item'  onclick=showaudio(this,'" + audios[m] + "')>音频" + n + "</li>");
                }

                for (var a = 0; a < videos.length; a++) {
                    var b = parseInt(a) + 1;
                    errorul.append("<li class='list-group-item'  onclick=showvideo(this,'" + videos[a] + "')>视频" + b + "</li>");
                }
                $("#myModal").modal("show");
                $("#arealist").css("display","block");
                $("#dotime").css("display","none");
            }
        }
        function showimgs(obj,imgs){
            $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $(obj).siblings('li').prop("selected", false);
            $(obj).addClass('selected');
            $(obj).prop("selected", true);

            $("#pic").empty();
            var pic = document.createElement("img");
            pic.src="report"+imgs;

            $(pic).css("width","auto");
            $(pic).css("height","auto");
            $(pic).css("max-width","100%");
            $(pic).css("max-height","100%");

            $("#pic")[0].appendChild(pic);
        }
        function showaudio(obj,audioval){
            $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $(obj).siblings('li').prop("selected", false);
            $(obj).addClass('selected');
            $(obj).prop("selected", true);
            $("#pic").empty();
            var au = document.createElement('audio');
            au.controls = true;
            au.preload="auto";
//            au.loop="loop";
            var source = document.createElement('source');
            au.appendChild(source);
            source.src="report"+audioval;
            source.type="audio/mpeg";
            $("#pic")[0].appendChild(au);
        }
        function showvideo(obj,videoval){
            $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $(obj).siblings('li').prop("selected", false);
            $(obj).addClass('selected');
            $(obj).prop("selected", true);
            $("#pic").empty();
            var video = document.createElement('video');
            video.src = "report"+videoval;
            video.controls = true;
            video.preload="auto";
//            video.loop="loop";
            video.width="400";
            video.height="210";

            $("#pic")[0].appendChild(video);
        }
    </script>
</head>
<body>
<%--<%@ include file="/WEB-INF/pages/common/navigation.jsp"%>--%>
<!-- topbar ends -->
<div class="ch-container">
    <div class="row">
        <%--<%@ include file="/WEB-INF/pages/common/menu.jsp"%>--%>
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div>
                        <ul id="myTab" class="nav nav-tabs">
                            <li>
                                <a href="showtaskplan?page=1&type=${type}">任务列表</a>
                            </li>
                            <li class="active"><a href="#report" data-toggle="tab" style="background: #35A7E7;" onclick="refer()">任务报告</a></li>
                            <c:if test="${type==2}">
                                <li><a href="showQuickReport1?page=1&type=${type}&qtype=1">立即上报</a></li>
                            </c:if>
                        </ul>
                    </div>

                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="report">
                            <div class="box-inner">
                                <div class="box-header well" data-original-title="">
                                    <h2>
                                        <i class="glyphicon glyphicon-globe"></i> 日常巡检任务报告--汇总
                                    </h2>
                                </div>
                                <div class="box-content">
                                    <form action="reportcount2" method="post">
                                        <input type="hidden" id="taskid" name="taskid" value="${taskid}">
                                        <input type="hidden" value="${type}" id="type" name="type">
                                        <input type="hidden" value="${taskname}" id="taskname" name="taskname">
                                        <span style="font-size: 15px;margin-top: 20px;margin-bottom: 20px">报告完成时间：</span>
                                        <input type="text" name="donetime" id="donetime" readonly onClick="WdatePicker()" value="${donetime}" style="margin-top: 20px;margin-bottom: 20px">
                                        <input type="submit" value="搜索" class="btn btn-primary">
                                        <input type="button" class="btn btn-primary" value="导出Excel" style="margin-left: 30px;" onclick="exportexcel()">
                                        <input type="button" class="btn btn-primary" value="返回" style="margin-left: 30px;" onclick="javascript:history.go(-1);">
                                    </form>
                                    <%--<span style="font-size:15px;margin-top: 20px;margin-bottom: 20px">操作状态:</span>--%>
                                    <%--<select id = "reportstate" name="reportstate" onchange="datechange(this)" style="margin-top: 20px;margin-bottom: 20px">--%>
                                        <%--<option id="option1" ${param.reportstate eq ''?'selected':''} value="">所有</option>--%>
                                        <%--<option id="option2" ${param.reportstate eq '0'?'selected':''} value="0">正常</option>--%>
                                        <%--<option id="option3" ${param.reportstate eq '1'?'selected':''} value="1">异常</option>--%>
                                    <%--</select>--%>
                                    <table id="table"  style="height: 700px;overflow: auto" class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable" id="table1">
                                        <c:forEach items="${reportinfos}" var="reports">
                                            <tr>
                                                <c:forEach items="${reports}" var="item">
                                                    <td style="text-align: center !important;vertical-align: middle">${item}</td>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                    <c:forEach items="${taskcode}" var="taskcode" varStatus="status">
                                        <input type="hidden" id="code${status.index}" value="${taskcode}">
                                    </c:forEach>
                                    <input type="hidden" id="statuslen" value="${fn:length(taskcode)}">
                                    <input type="button" class="btn btn-primary" value="返回" onclick="javascript:history.go(-1);">

                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<div aria-hidden="false" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade in" style="z-index: 0">
    <div class="modal-dialog" style="z-index: 9999">
        <div class="modal-content" style="overflow: auto;">
            <div id="arealist">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>显示异常</h3>
                </div>
                <div class="modal-body">
                    <div id="showerror"style="height: 30%;">
                        <div style="width: 150px;float: left;height: 300px;overflow:auto;">
                            <ul class="list-group" id="errorul">

                            </ul>
                        </div>
                        <div id="pic" style="border: 1px solid #F3F3F3; width: 400px;height: 100%;float: left;">

                        </div>
                        <div class="clearfix" style="display: none;"></div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
            <div id="dotime">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>选择时间</h3>
                </div>
                <div class="modal-body">
                    开始时间：<input type="text" id="time1" name="time1" onClick="WdatePicker()" >
                    结束时间:<input type="text" id="time2" name="time2" onClick="WdatePicker()">
                </div>
                <div class="modal-footer">
                    <input type="button" id="btn2" value="添加" class="btn btn-primary" onclick="exportexce2(${taskid},${type})">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
