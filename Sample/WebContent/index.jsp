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
<p>A script on this page starts this clock:</p>

<form action="login.htm"  method="post">
<p id="demo"></p>
</form>

<script>
var myVar=setInterval(function(){myTimer()},2000);

function myTimer()
{
	
	var d=new Date();
	var t=d.toLocaleTimeString();
	document.getElementById("demo").innerHTML=t;
	
	$.ajax({
        type: 'GET',
        url: 'showlogs.htm',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        cache: false,
        success: function(data, status, xmlHttp) {
            try {
            	//alert(data);
            	//var obj = JSON.stringify(data);
            	var logs = "";
                //alert(obj);
                
                $.each(data, function(index, element) {
                	logs += element + "<br/>";
                    /* $('demo').append($('<div>', {
                        text: element
                    })); */
                });
                document.getElementById("demo").innerHTML = logs;
            } catch (e) {
                alert('json parse error');
            }
        },
        error: function (request, status, error) {
            alert(request.responseText);
        }
    });
	

}
</script>
</body>
</html>