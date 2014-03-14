<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<title>Insert title here</title>
</head>
<body>
<p id="status">Show Logs:</p>

<script>
var btn=document.createElement("BUTTON");
var t=document.createTextNode("SEND MAIL");
btn.appendChild(t);
</script>

<p id="logs"></p>
<p id="sendmail"></p>

<script>
var myVar=setInterval(function(){showlogs()},5000);

function showlogs()
{
	
	var d=new Date();
	var t=d.toLocaleTimeString();
	document.getElementById("logs").innerHTML=t;
	
	$.ajax({
        type: 'GET',
        url: 'showlogs.htm',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        cache: false,
        success: function(data, status, xmlHttp) {
            try {
            	var logs = "";
            	
            	var arr = data.logs;
            	
            	for (var d = 0, len = arr.length; d < len; d += 1) {
            		logs += arr[d] + "<br/>";
                }
                
                /* $.each(data, function(index, element) {
                	logs += element + "<br/>";
                }); */
                
                document.getElementById("logs").innerHTML = logs;
                document.body.appendChild(btn);
                btn.onclick=function(){sendmail(data)};
                
            } catch (e) {
                //alert('json parse error');
            }
        },
        error: function (request, status, error) {
            //alert(request.responseText);
        }
    });
}

function sendmail(data){
	$.ajax({
        type: 'POST',
        url: 'sendmail.htm',
        data: JSON.stringify(data),
        dataType: 'text',
        contentType: "application/json; charset=utf-8",
        cache: false,
        success: function(data, status, xmlHttp) {
        	document.getElementById("sendmail").innerHTML = data;
        },
        error: function (request, status, error) {
        	$('body').html(request.responseText);
        	//document.write(request.responseText);
            //alert(request.responseText);
        }
	});
}
</script>
</body>
</html>