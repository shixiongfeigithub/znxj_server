<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <script type="text/javascript">
        function delappversion(id,name){
            var isDel=confirm('确定删除吗？');
            if (isDel!=true) {
                return false;
            }else {
                $.ajax({
                    url: "delappversion?id=" + id+"&name="+name,
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
        function showtask(){
            window.location="/showappversion?page="+$("#page").val();
        }
    </script>
</head>
<body>
<input type="hidden" id="page" value="${info.pageNum}">
<div class="ch-container">
    <div class="row">
        <%--<%@ include file="/WEB-INF/pages/common/menu.jsp"%>--%>
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <div style="float: left;">
                                <h2>
                                    <i class="glyphicon glyphicon-globe"></i> 系统设置
                                </h2>
                            </div>
                            <div style="float: right;">
                                <shiro:hasPermission name="add:apk">
                                    <a href="appversion/addappversion.jsp" id="button" class="btn btn-primary" style="margin-top: -6px;">添加Android APK</a>
                                </shiro:hasPermission>
                            </div>
                            <div class="clearfix"></div>
                        </div>

                        <ul id="myTab" class="nav nav-tabs">
                            <li><a href="showsystem?type=1">终端显示和报告缓存设置</a></li>
                            <li><a href="showsystem?type=2">一般性设置</a></li>
                            <%--<li><a href="showsystem?type=3">邮件服务器设置</a></li>--%>
                            <li><a href="showsystem?type=4">图片|视频|音频服务器设置</a></li>
                            <li><a href="showsystem?type=3">报告波动值设置</a></li>
                            <li>
                                <shiro:hasPermission name="item:data">
                                <a href="showdaterecord?page=1">可读数据管理</a>
                            </shiro:hasPermission></li>
                            <li class="active">
                                <shiro:hasPermission name="item:apk">
                                    <a href="#data">Android APK更新</a>
                                </shiro:hasPermission>
                            </li>
                            <li><shiro:hasPermission name="item:warntype"><a href="showwarntype?page=1">隐患类型和终止原因设置</a></shiro:hasPermission></li>
                            <li><a href="showReportSetting">显示单次报告设置</a></li>
                            <li><a href="showDoubleReportSetting">显示任务报告汇总设置</a></li>
                        </ul>
                        <div id="myTabContent" class="tab-content">
                            <div class="tab-pane fade in active" id="data">
                                <div class="box-content">

                                    <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                        <tr style="text-align: center">
                                            <th>操作</th>
                                            <th>版本名称</th>
                                            <th>版本号</th>
                                            <th>版本更新信息</th>
                                            <th>上传时间</th>
                                        </tr>
                                        <c:forEach items="${info.list}" var="appversion">
                                            <tr>
                                                <td>
                                                    <%--<shiro:hasPermission name="upd:knowtype">--%>
<%--
                                                        <a href="getknowledgetypebyid?typeid=${appversion.id}"> <i class="glyphicon glyphicon-edit red "></i></a>
--%>
                                                    <%--</shiro:hasPermission>--%>
                                                    <shiro:hasPermission name="del:apk">
                                                        <a href="javascript:void(0)" onclick="delappversion(${appversion.id},'${appversion.versionname}')"><i class="glyphicon glyphicon-trash"></i></a>
                                                    </shiro:hasPermission>
                                                </td>
                                                <td>${appversion.versionname}</td>
                                                <td>${appversion.versioncode}</td>
                                                <td>${appversion.versiondesc}</td>
                                                <td><sdf:formatDate value="${appversion.createtime}" pattern="yyyy-MM-dd HH:mm"></sdf:formatDate></td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                    <div style="height: 50px;width: 500px;text-align: center;margin-left: 300px;">
                                        <a href="showappversion?page=1">第一页</a>
                                        <c:if test="${info.pageNum>1}">
                                            <a href="showappversion?page=${info.pageNum-1}">上一页</a>
                                        </c:if>

                                        <c:if test="${info.pageNum<info.pages}">
                                            <a href="showappversion?page=${info.pageNum+1}">下一页</a>
                                        </c:if>

                                        <a href="showappversion?page=${info.pages}">最后一页</a>

                                        第${info.pageNum}页/共${info.pages}页
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
