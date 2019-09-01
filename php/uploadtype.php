<?php
	include "databaseconnect.php";
	$veg=$_POST["veg"];
	$nonveg=$_POST["nonveg"];
	$vegan=$_POST["vegan"];
	$alcohol=$_POST["alcohol"];
	$howoftenalcohol=$_POST["howoften"];
	$smoke=$_POST["smoking"];
	$howoftensmoke=$_POST["howoftensmoking"];

	$email=$_POST["email"];
	$count=mysqli_query($conn,"SELECT * from type where email='".$email."'");
	if($count->num_rows>0){
		$count=mysqli_query($conn,"DELETE from type where email='".$email."'");
	}
	$sql="Insert into type (veg,nonveg,vegan,alcoholic,howoften,smoke,howsmoke,email) value('".$veg."','".$nonveg."','".$vegan."','".$alcohol."','".$howoftenalcohol."','".$smoke."','".$howoftensmoke."','".$email."' )";
	
	$result=mysqli_query($conn,$sql);
	if($result){
		echo "Inserted";
	}else
		echo "Failed";
?>
