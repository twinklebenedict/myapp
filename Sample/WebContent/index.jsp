<html>
<head>
    <title>Spring 3.0 MVC Series: Index - ViralPatel.net</title>
</head>
 <!-- Place this asynchronous JavaScript just before your </body> tag -->
    <script type="text/javascript">
      (function() {
       var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
       po.src = 'https://apis.google.com/js/client:plusone.js';
       var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
     })();
    </script>
    
<body>
<form action="login.htm"  method="post">
	Email <input type="text" name="email"/> <br>
	Password <input type="password" name="password"/> <br>
	<input type="submit" name="login"></input>
</form>
	
	<script Language="JavaScript">
        function signinCallback(authResult) {
  			if (authResult['status']['signed_in']) {
    		// Update the app to reflect a signed in user
    		// Hide the sign-in button now that the user is authorized, for example:
   		 document.getElementById('signinButton').setAttribute('style', 'display: none');
  		} else {
    		// Update the app to reflect a signed out user
    		// Possible error values:
    		//   "user_signed_out" - User is signed-out
   			 //   "access_denied" - User denied access to your app
   			 //   "immediate_failed" - Could not automatically log in the user
    		console.log('Sign-in state: ' + authResult['error']);
  		}
	}
    </script>
	
<span id="signinButton">
  <span
    class="g-signin"
    data-callback="signinCallback"
    data-clientid="303431103031-s113ha133heh0jqjh37kjh8hi98qj4ng.apps.googleusercontent.com"
    data-cookiepolicy="single_host_origin"
    data-requestvisibleactions="http://schemas.google.com/AddActivity"
    data-scope="https://www.googleapis.com/auth/plus.login">
  </span>
</span>


</body>
</html>