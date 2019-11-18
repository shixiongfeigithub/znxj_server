<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

        #testalert {
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            width: 200px;
        }
    </style>
    <script type="text/javascript">
        function querybyquickid(id) {
            $.ajax({
                url: "querybyquickid?id=" + id,
                type: "post",
                datatype: "json",
                success: function (data) {
                    var imgs;
                    var audios;
                    var videos;
                    if ("" != data.img && data.img != "undefined") {
                        imgs = JSON.parse(data.img);
                    } else {
                        imgs = "";
                    }

                    if ("" != data.audio && data.audio != "undefined") {
                        audios = JSON.parse(data.audio);
                    } else {
                        audios = "";
                    }
                    if ("" != data.video && data.video != "undefined") {
                        videos = JSON.parse(data.video);
                    } else {
                        videos = "";
                    }
                    if (imgs == "" && audios == "" && videos == "") {
                        alert("暂时没有其他异常信息");
                    } else {
                        showquickdetail(imgs, data.content, audios, videos);
                    }
                }
            })
        }
        function showquickdetail(imgs, val3, audios, videos) {
            // val2图片  val3文字  val4音频
            var errorul = $("#errorul");
            errorul.empty();
            errorul.append("<li class='list-group-item'  onclick=showtext(this,'" + val3 + "')>描述</li>");
            if (val3 != null && "" != val3) {
                showtext(this, val3)
            }
            if (imgs != null && "" != imgs) {
                for (var i = 0; i < imgs.length; i++) {
                    var j = parseInt(i) + 1;
                    errorul.append("<li class='list-group-item'  onclick=showimg(this,'" + imgs[i] + "')>图片" + j + "</li>");
                }
                if (val3 == null && val3 == undefined) {
                    showimg(this, imgs[0]);
                }
            }
            if (audios != null && "" != audios) {
                for (var m = 0; m < audios.length; m++) {
                    var n = parseInt(m) + 1;
                    errorul.append("<li class='list-group-item'  onclick=showaudio(this,'" + audios[m] + "')>音频" + n + "</li>");
                }
                if (imgs == null && imgs == undefined) {
                    showaudio(this, audios[0]);
                }
            }
            if (videos != null && "" != videos) {
                for (var a = 0; a < videos.length; a++) {
                    var b = parseInt(a) + 1;
                    errorul.append("<li class='list-group-item'  onclick=showvideo(this,'" + videos[a] + "')>视频" + b + "</li>");
                }
                if (imgs == null && imgs == undefined && audios == null && audios == undefined) {
                    showvideo(this, videos[0]);
                }
            }
            $("#myModal").modal("show");
        }
        /*显示图片*/
        function showimg(obj, imgs) {
            $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $(obj).siblings('li').prop("selected", false);
            $(obj).addClass('selected');
            $(obj).prop("selected", true);

            $("#pic").empty();
            var pic = document.createElement("img");
            pic.src = "report" + imgs;

            $(pic).css("width", "auto");
            $(pic).css("height", "auto");
            $(pic).css("max-width", "100%");
            $(pic).css("max-height", "100%");

            $("#pic")[0].appendChild(pic);
        }
        /*显示文字*/
        function showtext(obj, val) {
            $(obj).siblings('li').removeClass('selected');  // 删除其他兄弟元素的样式
            $(obj).siblings('li').prop("selected", false);
            $(obj).addClass('selected');
            $(obj).prop("selected", true);
            $("#pic").empty();
            $("#pic")[0].innerHTML = val;
        }
        /*显示音频*/
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
//            video.loop="loop";
            video.width = "400";
            video.height = "210";

            $("#pic")[0].appendChild(video);
        }
        function delbyquickid(id) {
            var isDel = confirm('确定删除吗？');
            if (isDel != true) {
                return false;
            } else {
                $.ajax({
                    url: "delbyquickid?id=" + id,
                    type: "post",
                    datatype: "json",
                    success: function (data) {
                        if (data > 0) {
                            alert("删除成功");
                            showtask();
                        } else {
                            alert("删除失败");
                            return false;
                        }
                    }
                });
            }
        }
        function showtask() {
            window.location = "showdanger?page=" + $("#page").val();
        }

        function closetaskreport(id) {
            window.location="/toclosetaskreport?id="+id;
        }

        function assignprincipal(id) {
            window.location="/toassignprincipal?id="+id;
        }
        function handlerdetail(id){
            window.location="/handlerexceptiondetail?id="+id;
        }

        $(function(){
            var errorlen=$("#errorlen").val();
            console.log(errorlen);
            for(var i=0; i<parseInt(errorlen); i++){
                var reporttype=JSON.parse($("#report"+i).val());
                var type=$("#type"+i);
                var level=$("#level"+i);
                var content=$("#content"+i);
                for(var j=0;j<reporttype.length;j++){
                    if (reporttype[0] == null) {
                        reporttype[0] = "";
                    }
                    if (reporttype[1] == null) {
                        reporttype[1] = "";
                    }
                    if (reporttype[2] == null) {
                        reporttype[2] = "";
                    }
                    type.text(reporttype[0]);
                    level.text(reporttype[1]);
                    content.text(reporttype[2]);
                }
            }
        })
        function refer(){
            location.reload();
        }
    </script>
    <script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<input type="hidden" id="page" value="${pageBean.currentPage}">
<div class="ch-container">
    <div class="row">
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div id="myTabContent" class="tab-content">
                        <div class="box-inner">
                            <div class="box-header well" data-original-title="">
                                <h2>
                                    <i class="glyphicon glyphicon-globe"></i> 隐患 - 列表
                                </h2>
                            </div>
                            <div class="box-content">
                                <div class="form-inline" style="margin-bottom: 20px;">
                                    <form action="showdanger?page=1" method="post">
                                        <label class="control-label" for="siteid">厂区：</label>
                                        <select id="siteid" class="form-control" name="siteid">
                                            <c:if test="${siteid==null}">
                                                <option value="">所有厂区</option>
                                            </c:if>
                                            <c:forEach items="${sites}" var="site">
                                                <option  value="${site.id}" ${siteid eq site.id ?'selected':''}>${site.customid}</option>
                                            </c:forEach>
                                        </select>
                                        <label class="control-label" for="dangerstate">处理状态：</label>
                                        <select class="form-control" id="dangerstate" name="dangerstate" style="width: 80px;">
                                            <option ${dangerstate eq '' ? 'selected' : ''} value="">所有</option>
                                            <option ${dangerstate eq '1' ? 'selected' : ''} value="1">已关闭</option>
                                            <option ${dangerstate eq '2' ? 'selected' : ''} value="2">处理中</option>
                                        </select>
                                        <label class="control-label" for="operatorname">负责人：</label>
                                        <input type="text" style="width: 120px;" id="operatorname" name="operatorname" value="${operatorname}">
                                        <label class="control-label" for="reportcode">报告编号：</label>
                                        <input type="text" class="form-control" style="width: 150px;" id="reportcode"
                                               name="reportcode" value="${reportcode}">
                                        <br>
                                        <label class="control-label" for="time">时间：</label>
                                        <input type="text" class="form-control" style="width: 300px;"
                                               onClick="WdatePicker()" id="time" name="uploadtime"
                                               value="${uploadtime}">
                                        <input type="submit" class="btn btn-primary" value="搜索">
                                    </form>
                                </div>
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <th>厂区</th>
                                        <th>任务报告编号</th>
                                        <th>上传人</th>
                                        <th>班组</th>
                                        <th>报告类型</th>
                                        <th>报告等级</th>
                                        <th>文字异常</th>
                                        <th>问题描述</th>
                                        <th>上传时间</th>
                                        <th>操作</th>
                                    </tr>
                                    <c:forEach items="${info.list}" var="report">
                                        <tr>
                                            <td>${report.site.customid}</td>
                                            <td>${report.reportcode}</td>
                                            <td>${report.u.realname}</td>
                                            <td>${report.group.customid}</td>
                                            <td id="type${status.index}"></td>
                                            <td id="level${status.index}"></td>
                                            <td id="content${status.index}"></td>
                                            <td>${report.content}</td>
                                            <td><sdf:formatDate value="${report.uploadtime}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"></sdf:formatDate></td>
                                            <td>
                                                <a class="btn btn-primary" href="javascript:void(0)"
                                                   onclick="querybyquickid(${report.id})"><i
                                                        class="glyphicon glyphicon-zoom-in icon-white"></i>详情</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <input type="hidden" id="errorlen" value="${pageBean.list.size()}">
                                </table>
                                <div style="height: 50px;width: 500px;text-align: center;margin-left: 300px;">
                                    <a href="showdanger?page=1&siteid=${siteid}&dangerstate=${dangerstate}&operatorname=${operatorname}&&reportcode=${reportcode}&time=${updatetime}">第一页</a>
                                    <c:if test="${pageBean.currentPage>1}">
                                        <a href="showdanger?page=${pageBean.currentPage-1}&siteid=${siteid}&dangerstate=${dangerstate}&operatorname=${operatorname}&&reportcode=${reportcode}&time=${updatetime}">上一页</a>
                                    </c:if>

                                    <c:if test="${pageBean.currentPage<pageBean.totalPage}">
                                        <a href="showdanger?page=${pageBean.currentPage+1}&siteid=${siteid}&dangerstate=${dangerstate}&operatorname=${operatorname}&&reportcode=${reportcode}&time=${updatetime}">下一页</a>
                                    </c:if>

                                    <a href="showdanger?page=${pageBean.totalPage}&siteid=${siteid}&dangerstate=${dangerstate}&operatorname=${operatorname}&&reportcode=${reportcode}&time=${updatetime}">最后一页</a>

                                    第${pageBean.currentPage}页/共${pageBean.totalPage}页

                                </div>
                            </div>
                        </div>

                    </div>

                </div>

            </div>
            <hr>
        </div>
    </div>
</div>
<div aria-hidden="false" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade in"
     style="z-index: 0">
    <div class="modal-dialog" style="z-index: 9999;width: 60%;">
        <div class="modal-content" style="overflow: auto;">
            <div id="arealist">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>即拍即传</h3>
                </div>
                <div class="modal-body">
                    <div id="showerror" style="height: 60%;">
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
