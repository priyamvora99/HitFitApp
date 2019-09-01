<?php
include 'databaseconnect.php';
$email=$_POST["email"];
$medH=$_POST["medhistory"];
$sql = "Update useraccount SET medhistory = '".$medH."' where email = '".$email."'";
if(mysqli_query($conn, $sql)){
	echo "Updated successfully!";
}
?>