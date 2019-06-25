<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <%--<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>--%>
    <link type="text/css" rel='stylesheet' href="charisma/css/bootstrap-datetimepicker.min.css">
    <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
    <script src="js/bootstrap-datetimepicker.fr.js" type="text/javascript" charset="UTF-8"></script>
    <script type="text/javascript" src="charisma/bower_components/moment/min/moment.min.js"></script>
    <script type="text/javascript">
        $(function() {
            $(".form_datetime").datetimepicker({
                language:'zh-CN',
                format: "yyyy-mm-dd hh:ii",
                autoclose: true,
                todayBtn: true,
                pickerPosition:"bottom-left",
                minuteStep: 10
            });
        });
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
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2>
                                <i class="glyphicon glyphicon-globe"></i> 班组--编辑
                            </h2>
                        </div>
                        <div class="box-content">
                            <form action="updclass" method="post">
                                <input type="hidden" name="id" value="${classinfo.id}">
                                <table class="table table-striped table-bordered table-hover bootstrap-datatable datatable responsive dataTable">
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="idc">编组编号:</label>
                                            <input type="text" class="form-control" required style="width: 300px;" id="idc" name="customid" value="${classinfo.customid}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="username">班组描述:</label>
                                            <textarea type="text" class="form-control" rows="3" cols="35" id="username" name="classdesc" required>${classinfo.classdesc}</textarea>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="pwd">上岗时间:</label>
                                            <input type="text" class="form_datetime form-control" style="width: 300px;" id="pwd" name="workstarttime" required value="<sdf:formatDate value='${classinfo.workstarttime}' pattern='yyyy-MM-dd HH:mm:ss'></sdf:formatDate>">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="pwd2">下岗时间:</label>
                                            <input type="text" class="form_datetime form-control" style="width: 300px;" id="pwd2" name="workendtime" required value="<sdf:formatDate value='${classinfo.workendtime}' pattern='yyyy-MM-dd  HH:mm:ss'></sdf:formatDate>">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="pwd3">班组负责人:</label>
                                           <%-- <input type="text" class="form-control" style="width: 300px;" name="directorid" value="${classinfo.userinfo.realname}" id="pwd3">--%>
                                            <select class="form-control" name="directorid" id="pwd3">
                                                <c:forEach items="${userinfos}" var="user">
                                                    <option ${classinfo.userinfo.realname eq user.realname ? 'selected' : ''} value="${user.id}">${user.realname}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="form-inline">
                                            <label class="control-label" for="pwd4">所属厂区:</label>
                                            <%--<input type="text" class="form-control" style="width: 300px;" name="siteareaid" value="${classinfo.site.customid}" id="pwd4">--%>
                                            <select class="form-control" name="siteareaid" id="pwd4">
                                                <c:forEach items="${siteareainfos}" var="site">
                                                    <option ${classinfo.site.customid eq site.customid ? 'selected' : ''} value="${site.id}">${site.customid}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><input type="submit" class="btn btn-primary" value="修改">
                                            <input type="reset" class="btn btn-primary white" value="取消" onclick="javascript:history.go(-1);"></td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <hr>


</div>

</body>
</html>
