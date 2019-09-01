<?php
   include 'JSON.php';
   include 'databaseconnect.php';
 	$email=$_POST["email"];
   /* grab the posts from the db */
   $query = "Select email,name,phone,age,gender,height,weight,bmi,city FROM useraccount where email='".$email."'";
   echo "";
   encodequery($query);
?>