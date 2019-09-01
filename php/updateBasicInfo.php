<?php
include 'databaseconnect.php';
$email=$_POST["email"];
$name=$_POST["name"];
$age=$_POST["age"];
$phone=$_POST["phone"];
$height=$_POST["height"];
$weight=$_POST["weight"];
$bmi=$_POST["bmi"];
$city=$_POST["city"];
$sql = "Update useraccount SET name = '".$name."',phone=".$phone.",age=".$age.",height=".$height.",weight=".$weight.",bmi=".$bmi.",city='".$city."' where email = '".$email."'";
if(mysqli_query($conn,$sql)){
	echo "Updated successfully";
}else{
	echo mysqli_error($conn);
}

?>
