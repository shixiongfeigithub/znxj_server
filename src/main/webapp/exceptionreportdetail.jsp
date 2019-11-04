<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp" %>
    <style>
        .selected {
            background: #35A7E7;
            color: white;
        }

        .fontcenter {
            text-align: center !important;
            vertical-align: middle;
        }
    </style>
    <%--<script src="/js/jquery-1.11.0.min.js"></script>--%>
    <script type="text/javascript">
        var count;
        //浮点数值
        function CheckInputIntFloat(oInput) {
            if('' != oInput.value.replace(/-?\d{1,}\.{0,1}\d{0,}/,'')) {
                oInput.value = oInput.value.match(/-?\d{1,}\.{0,1}\d{0,}/) == null ? '' :oInput.value.match(/-?\d{1,}\.{0,1}\d{0,}/);
            }
        }
        $(function () {
            var x = document.getElementById('table').rows[0].cells;
            var len = $("#statuslen").val();
            for (var i = 0; i < len; i++) {
                var oldErrorContent = $("#error"+i).text();
                var imgContent = new Array();
                imgContent=$("#img"+i).val();
                var audioContent =  new Array();
                audioContent=$("#audio"+i).val();
                var videoContent =  new Array();
                videoContent=$("#video"+i).val();
                var newErrorContent = "";
                if(oldErrorContent!="-"){
                    $("#error"+i).empty();
                    if(oldErrorContent.indexOf("/") > 0 ){
                        var ss = oldErrorContent.substring(0,oldErrorContent.indexOf("/"));
                        var ff = oldErrorContent.substring(oldErrorContent.indexOf("/")+1,oldErrorContent.length);
                        $("#error"+i).append("<span style='color: red;'>"+ss+"</span>&nbsp;&nbsp;&nbsp;&nbsp;<span style='color: blue;'>"+ff+"</span>");
                    }else {
                        $("#error"+i).append("<a href='javascript:void(0);' onclick=showimg('"+imgContent+"','"+audioContent+"','"+videoContent+"')>"+oldErrorContent+"</a>");
                    }

                }
            }
            var y = document.getElementById('table').rows;
            var reportid = $("#reportid").val();
            $.ajax({
                url: "getReportContentByReportId",
                type: "post",
                data: {
                    taskreportid: reportid,
                },
                dataType: "json",
                success: function (data) {
                    if (data != null) {
                        for (var i = 0; i < data.length; i++) {
                            var presentNumValue = data[i].numvalue;
                            var nowNumValue = $("#numvalue" + i).text();
                            if(data[i].checkvalue==null||data[i].checkvalue==''){
                                $("#first" + i).text("-");
                            }else
                                $("#first" + i).text(data[i].checkvalue);
//                            }
                        }
                        return true;
                    } else {
                        alert("出错了");
                        return false;
                    }
                }
            });
        })

        function showimg(img,audio,video){
            var imgs=new Array();
            var audios=new Array();
            var videos=new Array();
            if(""!=img && img!="undefined"&&img!="null"&&img!="[]"){
                imgs=JSON.parse(img);
            }else{
                imgs="";
            }

            if(""!=audio && audio!="undefined"&&audio!="null" && audio!="[]"){
                audios=JSON.parse(audio);
            }else{
                audios="";
            }
            if(""!=video && video!="undefined" &&video!="null" && video!="[]"){
                videos=JSON.parse(video);
            }else{
                videos="";
            }
            var errorul=$("#errorul");
            errorul.empty();
            if(imgs==""&&audios==""&&videos=="" ){
                alert("暂时没有其他异常信息");
                return;
            }
            if(imgs!=null && ""!=imgs) {
                for (var i = 0; i < imgs.length; i++) {
                    var j = parseInt(i) + 1;
                    errorul.append("<li id ='imgLiLJJ"+i+"' class='list-group-item'  onclick=showimg(this,'" + imgs[i] + "')>图片" + j + "</li>");
                }
                var firstImgLi = document.getElementById("imgLiLJJ0");
                showimg(firstImgLi, imgs[0]);
            }
            if(audios!=null && ""!=audios){
                for (var m = 0; m < audios.length; m++) {
                    var n = parseInt(m) + 1;
                    errorul.append("<li id ='audioLiLJJ"+m+"' class='list-group-item'  onclick=showaudio(this,'" + audios[m] + "')>音频" + n + "</li>");
                }
                if(img ==null && img == undefined) {
                    var firstAudioLi = document.getElementById("audioLiLJJ0");
                    showaudio(firstAudioLi, audios[0]);
                }
            }
            if(videos!=null && ""!=videos){
                for (var a = 0; a < videos.length; a++) {
                    var b = parseInt(a) + 1;
                    errorul.append("<li id ='videoLiLJJ"+a+"' class='list-group-item'  onclick=showvideo(this,'" + videos[a] + "')>视频" + b + "</li>");
                }
                if(img ==null && img == undefined && audio ==null && audio == undefined) {
                    var firstVideoLi = document.getElementById("videoLiLJJ0");
                    showvideo(firstVideoLi, videos[0]);
                }
            }
            $("#myModal3").modal("show");

        }
        function showimg(obj,imgs){
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
        function showaudio(obj, audioval) {
            $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $(obj).siblings('li').prop("selected", false);
            $(obj).addClass('selected');
            $(obj).prop("selected", true);

            $("#pic").empty();
            var au = document.createElement('audio');
            au.controls = true;
            au.preload = "auto";
//            au.loop="loop";
            var source = document.createElement('source');
            au.appendChild(source);
            source.src = "report" + audioval;
            source.type = "audio/mpeg";
            $("#pic")[0].appendChild(au);
        }
        function showvideo(obj, videoval) {
            $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $(obj).siblings('li').prop("selected", false);
            $(obj).addClass('selected');
            $(obj).prop("selected", true);
            $("#pic").empty();
            var video = document.createElement('video');
            video.src = "report" + videoval;
            video.controls = true;
            video.preload = "auto";
            $(video).css("width","100%");
            $(video).css("height","100%");

            $("#pic")[0].appendChild(video);
        }

    </script>
</head>
<body>

<input type="hidden" value="${taskreport.id}" id="reportid">
<input type="hidden" value="${taskreportid}" id="oldreportid">
<input type="hidden" value="${type}" id="oldtype">
<input type="hidden" value="${FLUCTUATE}" name="FLUCTUATE" id="maxval">
<input type="hidden" value="${page}" name="page" id="page">
<input type="hidden" value="${taskcode}"  id="taskcode">
<div class="ch-container">
    <div class="row">
        <%--<%@ include file="/WEB-INF/pages/common/menu.jsp"%>--%>
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div>
                        <ul id="myTab" class="nav nav-tabs">
                            <li>
                                <a href="showexceptionreport?page=1">异常任务列表</a>
                            </li>
                        </ul>
                    </div>

                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="report">
                            <div class="box-inner" style="height: 700px !important;overflow: scroll">
                                <div class="box-header well" data-original-title="">
                                    <div style="float: left;">
                                        <h2>
                                            <i class="glyphicon glyphicon-globe"></i>
                                            ${fn:substring(taskcode,0,taskcode.indexOf('-'))}(${taskcode})
                                        </h2>
                                    </div>
                                    <div style="float: right;">
                                            <a href="javascript:void(0)" class="btn btn-primary" style="margin-top: -6px;"
                                               onclick="javascript:history.go(-1);">返回</a>
                                    </div>
                                    <div class="clearfix"></div>
                                </div>
                                <div class="box-content">
                                    <table id="table" class="table table-striped table-bordered table-hover ">
                                        <tr>
                                            <c:if test="${areaname==1}">
                                                <th class="fontcenter">区域</th>
                                            </c:if>
                                            <c:if test="${equipname==1}">
                                                <th class="fontcenter">设备</th>
                                            </c:if>
                                            <c:if test="${checkname==1}">
                                                <th class="fontcenter">巡检项</th>
                                            </c:if>
                                            <c:if test="${checktype==1}">
                                                <th class="fontcenter">巡检项类型</th>
                                            </c:if>
                                            <%--<c:if test="${unitname==1}">--%>
                                                <th class="fontcenter">单位</th>
                                            <%--</c:if>--%>
                                            <c:if test="${operationtime==1}">
                                                <th class="fontcenter">执行时间</th>
                                            </c:if>
                                            <c:if test="${normalmin==1}">
                                                <th class="fontcenter">低值</th>
                                            </c:if>
                                            <c:if test="${normalmax==1}">
                                                <th class="fontcenter">高值</th>
                                            </c:if>
                                            <c:if test="${lowerwarning==1}">
                                                <th class="fontcenter">告警下限</th>
                                            </c:if>
                                            <c:if test="${upperwarning==1}">
                                                <th class="fontcenter">告警上限</th>
                                            </c:if>
                                            <c:if test="${numvalue==1}">
                                                <th class="fontcenter">报告值</th>
                                            </c:if>
                                            <c:if test="${errcontent==1}">
                                                <th class="fontcenter">异常描述</th>
                                            </c:if>
                                            <c:if test="${recordname==1}">
                                                <th class="fontcenter">数据名称</th>
                                            </c:if>
                                            <c:if test="${unitname==1}">
                                                <th class="unitname">单位</th>
                                            </c:if>
                                            <%--<c:if test="${firstval==1}">
                                                <c:if test="${taskreport==null}">
                                                    <th class="fontcenter">前次复核值</th>
                                                </c:if>
                                                <c:if test="${taskreport.examstate==0}">
                                                    <th class="fontcenter">前次复核值</th>
                                                </c:if>
                                                <c:if test="${taskreport.examstate==1}">
                                                    <th class="fontcenter">前次复核值(人工)</th>
                                                </c:if>
                                                <c:if test="${taskreport.examstate==2}">
                                                    <th class="fontcenter">前次复核值(自动)</th>
                                                </c:if>
                                                &lt;%&ndash;<th class="fontcenter">前次复核值</th>&ndash;%&gt;
                                            </c:if>
                                            <c:if test="${checkvalue==1}">
                                                <th class="fontcenter">复核值</th>
                                            </c:if>--%>
                                        </tr>
                                        <c:forEach items="${reportinfos}" var="reports" varStatus="status">
                                            <input type="hidden" id="img${status.index}" value='${reports.img}'>
                                            <input type="hidden" id="audio${status.index}" value='${reports.audio}'>
                                            <input type="hidden" id="video${status.index}" value='${reports.video}'>
                                            <tr>
                                                <td style="display: none">${reports.id}</td>
                                                <c:if test="${areaname==1}">
                                                    <td class="fontcenter">
                                                        <c:if test="${reports.areaskip==1}">
                                                            <span style="color: red">${reports.areaname}(${reports.areaskipdesc})</span>
                                                        </c:if>
                                                        <c:if test="${reports.areaskip==0}">
                                                            <span>${reports.areaname}</span>
                                                        </c:if>
                                                    </td>
                                                </c:if>
                                                <c:if test="${equipname==1}">
                                                    <td class="fontcenter">
                                                        <c:if test="${reports.equipmentskip==1}">
                                                            <span style="color: red">${reports.equipname}(${reports.equipmentskipdesc})</span>
                                                        </c:if>
                                                        <c:if test="${reports.equipmentskip==0}">
                                                            <span>${reports.equipname}</span>
                                                        </c:if>
                                                    </td>
                                                </c:if>
                                                <c:if test="${checkname==1}">
                                                    <td class="fontcenter">
                                                        ${reports.checkname}
                                                    </td>
                                                </c:if>

                                                <c:if test="${checktype==1}">
                                                    <td class="fontcenter">${reports.checktype}</td>
                                                </c:if>
                                                <%--<c:if test="${unitname == 1}">--%>
                                                    <td class="fontcenter">${reports.unitname}</td>
                                                <%--</c:if>--%>
                                                <c:if test="${operationtime==1}">
                                                    <td class="fontcenter">${reports.operationtime}</td>
                                                </c:if>
                                                <c:if test="${normalmin==1}">
                                                    <td class="fontcenter">${reports.normalmin}</td>
                                                </c:if>
                                                <c:if test="${normalmax==1}">
                                                    <td class="fontcenter">${reports.normalmax}</td>
                                                </c:if>
                                                <c:if test="${lowerwarning==1}">
                                                    <td class="fontcenter">${reports.lowerwarning}</td>
                                                </c:if>
                                                <c:if test="${upperwarning==1}">
                                                    <td class="fontcenter">${reports.upperwarning}</td>
                                                </c:if>
                                                <c:if test="${numvalue==1}">
                                                    <td class="fontcenter"id="numvalue${status.index}">
                                                        <c:if test="${reports.checktype == '枚举项'}">
                                                            <c:if test="${reports.enumitem == ''}">-</c:if>
                                                            <c:if test="${reports.enumitem != ''}">${reports.enumitem}</c:if>
                                                        </c:if>
                                                        <c:if test="${reports.checktype == '记录项'}">
                                                            <c:if test="${reports.numvalue == ''}">-</c:if>
                                                            <c:if test="${reports.numvalue != ''}">${reports.numvalue}</c:if>
                                                        </c:if>
                                                        <c:if test="${reports.checktype == '状态项'}">
                                                            <c:if test="${reports.areaskipdesc != null or reports.equipmentskipdesc != null}">
                                                                -
                                                                <%--<c:if test="${reports.reportstate == ''}">-</c:if>--%>
                                                            </c:if>

                                                            <c:if test="${reports.areaskipdesc == null and reports.equipmentskipdesc == null}">
                                                                <c:if test="${reports.reportstate == ''}">-</c:if>
                                                                <c:if test="${reports.reportstate != ''}">
                                                                    <c:if test="${reports.reportstate == 1}">
                                                                        异常
                                                                    </c:if>
                                                                    <c:if test="${reports.reportstate == 0}">
                                                                        正常
                                                                    </c:if>
                                                                </c:if>
                                                            </c:if>

                                                        </c:if>

                                                    </td>
                                                </c:if>
                                                <c:if test="${errcontent==1}">
                                                    <td class="fontcenter" id="error${status.index}">${reports.errcontent}</td>
                                                        <%--<a href="javascript:void(0);" onclick="showimg('${reports.imgs}','${reports.audio}','${reports.video}')">${reports.errcontent}--%>
                                                        <%--</a>--%>

                                                </c:if>
                                                <c:if test="${recordname==1}">
                                                    <td class="fontcenter">${reports.recordname}</td>
                                                </c:if>
                                                <c:if test="${unitname==1}">
                                                    <td class="fontcenter">${reports.unitname}</td>
                                                </c:if>
                                                <%--<c:if test="${firstval==1}">
                                                    <td class="fontcenter" id="first${status.index}">
                                                        <c:if test="${taskreport==null}">-</c:if>
                                                    </td>
                                                </c:if>
                                                <c:if test="${checkvalue==1}">
                                                    <td class="fontcenter">
                                                        <c:if test="${reports.checktype=='记录项'}">
                                                            <c:if test="${taskreportinfo.examstate==0}">
                                                                <input type="text" onblur="javascript:CheckInputIntFloat(this);" style="width: 50px"
                                                                       value="${reports.numvalue}">
                                                            </c:if>
                                                            <c:if test="${taskreportinfo.examstate==1}">
                                                                <c:if test="${reports.checkvalue!=reports.numvalue}">
                                                                    <span style="color: white"><input type="number" style="width: 50px;background:red;color: white;text-align: center;border:0;" value="${reports.checkvalue}" readonly></span>
                                                                </c:if>
                                                                <c:if test="${reports.checkvalue==reports.numvalue}">
                                                                    <input type="number" style="width: 50px;text-align: center;background:#f9f9f9;border:0;"
                                                                           value="${reports.checkvalue}" readonly>
                                                                </c:if>
                                                            </c:if>
                                                            <c:if test="${taskreportinfo.examstate==2}">
                                                                <input type="number" style="width: 50px;text-align: center;background:#f9f9f9;border:0;"
                                                                           value="${reports.checkvalue}" readonly>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${reports.checktype=='状态项'}">-</c:if>
                                                    </td>
                                                </c:if>--%>
                                            </tr>
                                        </c:forEach>
                                        <input type="hidden" id="statuslen" value="${fn:length(reportinfos)}">
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<div aria-hidden="false" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal3" class="modal fade in" style="z-index: 0">
    <div class="modal-dialog" style="z-index: 9999;width: 60%;">
        <div class="modal-content" style="overflow: auto;">
            <div id="arealist">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>显示异常</h3>
                </div>
                <div class="modal-body">
                    <div id="showerror"style="height: 60%;">
                        <div style="width: 150px;float: left;height: 100%;;overflow:auto;">
                            <ul class="list-group" id="errorul">

                            </ul>
                        </div>
                        <div id="pic" style="border: 1px solid #F3F3F3; width: 600px;height: 100%;float: left;">

                        </div>
                        <div class="clearfix" style="display: none;"></div>
                    </div>
                </div>
                <div class="modal-footer">
                    <a data-dismiss="modal" class="btn btn-default" href="#">取消</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
