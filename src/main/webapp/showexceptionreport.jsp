<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sdf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>智能巡检系统</title>
    <%@ include file="/WEB-INF/pages/common/header.jsp"%>
    <script type="text/javascript" src="js/formatdate.js"></script>
    <script type="text/javascript">
        $(function () {
            sitechange();
        })

        function sitechange(){
            var siteId= $("#siteid option:selected")[0].value;
            var areaId = '${areaid}';
            if(siteId == null || siteId == undefined || siteId == ''){
                $("#areainfo").append("<option  value='' >所有区域</option>");
            }else{
                $("#areainfo").empty();
                $.ajax({
                    url:"queryareabysiteid",
                    type:"post",
                    data:{
                        siteid:siteId,
                    },
                    dataType:"json",
                    success:function(data){
                        if(data!=null){
                            for(var i=0;i<data.length;i++){
                                if(data[i].id==areaId){
                                    $("#areainfo").append("<option  value='"+data[i].id+"' selected>"+data[i].customid+"</option>");
                                }else{
                                    $("#areainfo").append("<option  value='"+data[i].id+"' >"+data[i].customid+"</option>");
                                }
                            }
                        }
                    }
                });
            }
            areachange();
        }


        /*根据区域编号显示所有的设备*/
        function areachange() {
            $("#equipment").empty();
            var areaid = $("#areainfo option:selected")[0].value;
            if(areaid == null || areaid == undefined || areaid == ''){
                $("#equipment").append("<option  value='' >所有设备</option>");
            }
            var equipmentid = '${equipmentid}';
            $.ajax({
                url: "showequipment?areaid=" + areaid,
                type: "post",
                dataType: "json",
                success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            if (data[i].id == equipmentid) {
                                $("#equipment").append("<option  value='" + data[i].id + "' selected>" + data[i].name + "</option>");
                            } else {
                                $("#equipment").append("<option  value='" + data[i].id + "' >" + data[i].name + "</option>");
                            }
                        }
                }
            })
        }

        function closetaskreport(id) {
            window.location="/toclosetaskreport?id="+id;
        }

        function assignprincipal(id) {
            window.location="/toassignprincipal?id="+id;
        }
    </script>
</head>
<body>
<input type="hidden" id="page" value="${pageBean.currentPage}">
<div class="ch-container">
    <div class="row">
        <div id="content" class="col-lg-12 col-sm-12">
            <div class="row">
                <div class="box col-md-12">
                    <input type="hidden" value="${roleid}" id="roleid">
                    <div>
                        <ul id="myTab" class="nav nav-tabs">
                            <li>
                                <a href="showexceptionreport?page=1">异常任务列表</a>
                            </li>
                        </ul>
                    </div>

                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="report">
                            <div class="box-inner">
                                <div class="box-content">
                                    <div class="form-inline" style="margin-bottom: 20px;">
                                        <form action="showexceptionreport?page=1" method="post">
                                            <label class="control-label" for="siteid">厂区：</label>
                                            <select id="siteid" class="form-control" name="siteid" onchange="sitechange()">
                                                <c:if test="${siteid==null}">
                                                    <option value="">所有厂区</option>
                                                </c:if>
                                                <c:forEach items="${sites}" var="site">
                                                    <option  value="${site.id}" ${siteid eq site.id ?'selected':''}>${site.customid}</option>
                                                </c:forEach>
                                            </select>

                                            <label class="control-label" for="areainfo">区域：</label>
                                            <select class="form-control" id="areainfo" name="areaid" onchange="areachange()">
                                                <option value="" selected>所有区域</option>
                                            </select>

                                            <label class="control-label" for="equipment">设备：</label>
                                            <select class="form-control" id="equipment" name="equipmentid" >
                                                <option value="" selected>所有设备</option>
                                            </select>

                                            <label class="control-label" for="exceptionstate">处理状态：</label>
                                            <select class="form-control" id="exceptionstate" name="exceptionstate">
                                                <option ${exceptionstate eq '' ? 'selected' : ''} value="">所有</option>
                                                <option ${exceptionstate eq '1' ? 'selected' : ''} value="1">已关闭</option>
                                                <option ${exceptionstate eq '2' ? 'selected' : ''} value="2">处理中</option>
                                            </select>
                                            <label class="control-label" for="operatorname">负责人：</label>
                                            <input type="text" style="width: 100px;" id="operatorname" name="operatorname" value="${operatorname}">
                                            <br>
                                            <label class="control-label" style="margin-top: 10px">执行时间：</label>
                                            <input type="text" name="time1" onClick="WdatePicker()" readonly value="${param.time1}"style="margin-top: 10px" id="time1">--<input type="text" name="time2" onClick="WdatePicker()" readonly value="${param.time2}"style="margin-top: 10px" id="time2">
                                            <input type="submit" class="btn btn-primary" value="搜索" style="margin-left: 30px;margin-top: 10px">
                                            <script language="JavaScript">
                                                Date.prototype.format = function (format) {
                                                    var args = {
                                                        "M+": this.getMonth() + 1,
                                                        "d+": this.getDate(),
                                                        "h+": this.getHours(),
                                                        "m+": this.getMinutes(),
                                                        "s+": this.getSeconds(),
                                                        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter

                                                        "S": this.getMilliseconds()
                                                    };
                                                    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
                                                    for (var i in args) {
                                                        var n = args[i];
                                                        if (new RegExp("(" + i + ")").test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
                                                    }
                                                    return format;
                                                };
                                                $(function () {
                                                    if($("#time1").val() == ""){
                                                        $("#time1").val(new Date().format("yyyy-MM-dd"));
                                                    }
                                                    if($("#time2").val() == ""){
                                                        $("#time2").val(new Date().format("yyyy-MM-dd"));
                                                    }
                                                });
                                            </script>

                                        </form>
                                    </div>

                                    <table id="table" class="table table-striped table-bordered table-hover ">
                                        <tr>
                                            <th>操作</th>
                                            <th>厂区</th>
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
                                            <%--<c:if test="${recordname==1}">
                                                <th class="fontcenter">数据名称</th>
                                            </c:if>
                                            <c:if test="${unitname==1}">
                                                <th class="unitname">单位</th>
                                            </c:if>--%>
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
                                            </c:if>--%>
                                            <c:if test="${checkvalue==1}">
                                                <th class="fontcenter">复核值</th>
                                            </c:if>
                                            <th>异常记录链接</th>
                                        </tr>
                                        <c:forEach items="${pageBean.list}" var="item" varStatus="status">
                                        <input type="hidden" id="img${status.index}" value='${item.img}'>
                                        <input type="hidden" id="audio${status.index}" value='${item.audio}'>
                                        <input type="hidden" id="video${status.index}" value='${item.video}'>
                                        <tr>
                                            <td>
                                                <shiro:hasPermission name="item:closereport">
                                                    <a href="javascript:;" onclick="closetaskreport('${item.id==null?'':item.id}')">
                                                        <i class="glyphicon glyphicon-lock"></i></a>&nbsp;
                                                </shiro:hasPermission>
                                                <shiro:hasPermission name="item:assignprincipal">
                                                    <a href="javascript:;" onclick="assignprincipal('${item.id==null?'':item.id}')">
                                                        <i class="glyphicon glyphicon-share"></i></a>
                                                </shiro:hasPermission>
                                            </td>
                                            <td>${item.sitename}</td>
                                            <c:if test="${areaname==1}">
                                                <td class="fontcenter">
                                                    <span>${item.areaname}</span>
                                                </td>
                                            </c:if>
                                            <c:if test="${equipname==1}">
                                                <td class="fontcenter">
                                                    <span>${item.equipname}</span>
                                                </td>
                                            </c:if>
                                            <c:if test="${checkname==1}">
                                                <td class="fontcenter">
                                                        ${item.checkname}
                                                </td>
                                            </c:if>

                                            <c:if test="${checktype==1}">
                                                <td class="fontcenter">${item.checktype}</td>
                                            </c:if>
                                            <c:if test="${operationtime==1}">
                                                <td class="fontcenter">${item.operationtime}</td>
                                            </c:if>
                                            <c:if test="${normalmin==1}">
                                                <td class="fontcenter">${item.normalmin}</td>
                                            </c:if>
                                            <c:if test="${normalmax==1}">
                                                <td class="fontcenter">${item.normalmax}</td>
                                            </c:if>
                                            <c:if test="${lowerwarning==1}">
                                                <td class="fontcenter">${item.lowerwarning}</td>
                                            </c:if>
                                            <c:if test="${upperwarning==1}">
                                                <td class="fontcenter">${item.upperwarning}</td>
                                            </c:if>
                                            <c:if test="${numvalue==1}">
                                                <td class="fontcenter"id="numvalue${status.index}">
                                                    <c:if test="${item.checktype == '枚举项'}">
                                                        <c:if test="${item.enumitem == ''}">-</c:if>
                                                        <c:if test="${item.enumitem != ''}">${reports.enumitem}</c:if>
                                                    </c:if>
                                                    <c:if test="${item.checktype == '记录项'}">
                                                        <c:if test="${item.numvalue == ''}">-</c:if>
                                                        <c:if test="${item.numvalue != ''}">${item.numvalue}</c:if>
                                                    </c:if>
                                                    <c:if test="${item.checktype == '状态项'}">
                                                        <c:if test="${item.areaskipdesc != null or item.equipmentskipdesc != null}">
                                                            -
                                                        </c:if>

                                                        <c:if test="${item.areaskipdesc == null and item.equipmentskipdesc == null}">
                                                            <c:if test="${item.reportstate == ''}">-</c:if>
                                                            <c:if test="${item.reportstate != ''}">
                                                                <c:if test="${item.reportstate == 1}">
                                                                    异常
                                                                </c:if>
                                                                <c:if test="${item.reportstate == 0}">
                                                                    正常
                                                                </c:if>
                                                            </c:if>
                                                        </c:if>

                                                    </c:if>

                                                </td>
                                            </c:if>
                                            <c:if test="${errcontent==1}">
                                                <td class="fontcenter" id="error${status.index}">${item.errcontent}</td>
                                            </c:if>
                                                <%--<c:if test="${recordname==1}">
                                                    <td class="fontcenter">${item.recordname}</td>
                                                </c:if>
                                                <c:if test="${unitname==1}">
                                                    <td class="fontcenter">${item.unitname}</td>
                                                </c:if>--%>
                                                <%--<c:if test="${firstval==1}">
                                                    <td class="fontcenter" id="first${status.index}">
                                                        <c:if test="${taskreport==null}">-</c:if>
                                                    </td>
                                                </c:if>--%>
                                                <c:if test="${checkvalue==1}">
                                                    <td class="fontcenter">
                                                        <c:if test="${item.checktype=='记录项'}">
                                                            ${item.checkvalue}
                                                        </c:if>
                                                        <c:if test="${item.checktype=='状态项'}">-</c:if>
                                                    </td>
                                                </c:if>
                                            <td>
                                                <c:if test="${item.img != 'null' or item.audio != 'null' or item.video != 'null'}">
                                                    <a href='http://${ip}/toException?img=${item.img}&audio=${item.audio}&video=${item.video}'  target="_Blank">查看异常详情</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                        </c:forEach>
                                    </table>
                                    <div style="height: 50px;width: 500px;text-align: center;margin-left: 300px;">
                                        <a href="showexceptionreport?page=1&siteid=${siteid}&areaid=${areaid}&equipmentid=${equipmentid}&operationstate=${operationstate}&operatorname=${operatorname}&time1=${time1}&time2=${time2}">第一页</a>
                                        <c:if test="${pageBean.currentPage>1}">
                                            <a href="showexceptionreport?page=${pageBean.currentPage-1}&siteid=${siteid}&areaid=${areaid}&equipmentid=${equipmentid}&operationstate=${operationstate}&operatorname=${operatorname}&time1=${time1}&time2=${time2}">上一页</a>
                                        </c:if>

                                        <c:if test="${pageBean.currentPage<pageBean.totalPage}">
                                            <a href="showexceptionreport?page=${pageBean.currentPage+1}&siteid=${siteid}&areaid=${areaid}&equipmentid=${equipmentid}&operationstate=${operationstate}&operatorname=${operatorname}&time1=${time1}&time2=${time2}">下一页</a>
                                        </c:if>

                                        <a href="showexceptionreport?page=${pageBean.totalPage}&siteid=${siteid}&areaid=${areaid}&equipmentid=${equipmentid}&operationstate=${operationstate}&operatorname=${operatorname}&time1=${time1}&time2=${time2}">最后一页</a>

                                        第${pageBean.currentPage}页/共${pageBean.totalPage}页
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
<div aria-hidden="false" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade in" style="z-index: 0">
    <div class="modal-dialog" style="z-index: 9999">
        <div class="modal-content" style="overflow: auto;">
            <div id="arealist">
                <div class="modal-header">
                    <button data-dismiss="modal" class="close" type="button">×</button>
                    <h3>显示异常</h3>
                </div>
                <div class="modal-body">
                    <div id="showstop"style="height: 30%;">

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
