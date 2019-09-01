<?php
include 'databaseconnect.php';
	
	$otp=$_POST["otp"];
	$password=$_POST["password"];
	//$otp="38069";
	//$password="pass";

	$sql="Select * from signup where otp=$otp";
	$result=mysqli_query($conn,$sql);
	$row = mysqli_fetch_assoc($result);
	if($row["otp"]==$otp){
		$sql="Update login set password='$password' where email='".$row["email"]."'";
		
		$result1=mysqli_query($conn,$sql);
		echo "Password reset successful!!";
		$sql2="delete from signup where otp=".$otp;
		$result2=mysqli_query($conn,$sql2);
	}else{
		echo "Enter correct OTP";
	}
?>