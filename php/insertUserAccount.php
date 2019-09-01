<?php
	include 'databaseconnect.php';

	$email=$_POST["email"];
	$name=$_POST["name"];
	$phone=$_POST["phone"];
	$age=$_POST["age"];
	$gender=$_POST["gender"];
	$height=$_POST["height"];
	$weight=$_POST["weight"];
	$bmi=$_POST["bmi"];
	$city=$_POST["city"];
	$daily_activity=$_POST["daily_activity"];
	$medhist="";
	$count=mysqli_query($conn,"SELECT * from useraccount where email='".$email."'");

	if($count->num_rows>0){
		$count=mysqli_query($conn,"DELETE from useraccount where email='".$email."'");
	}
	$sql="Insert into useraccount values('$email','$name',$phone,$age,'$gender',$height,$weight,$bmi,'$city','$medhist','$daily_activity')";
	if(mysqli_query($conn, $sql)){
		echo "Now start your journey";
	}else{
		echo mysql_error($conn);
	}
?>
