<?php
include 'databaseconnect.php';
$flag=1;
try{
if(!isset($_POST['otp'])){
	
$e=$_POST["email"];
	
	$sql="select * from login where email='".$e."'";
	$result = mysqli_query($conn, $sql);
	if(!mysqli_num_rows($result)>0){
		$sql="Select otp from signup where email='".$e."'";
		$result = mysqli_query($conn, $sql);
		if (!mysqli_num_rows($result) > 0) {
			$headers = 'From: HitFit' . "\r\n" .
    					'Reply-To: No-Reply' . "\r\n" ;
    		$headers .=  'MIME-Version: 1.0' . "\r\n"; 
			$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
			
			$code=mt_rand(10000,99999);
			$subject = 'OTP for Login to HitFit. ';
			$body    = 'Welcome to HitFit! <br><b> This is your OTP(One Time Password): '.$code."</b><br> Sign Up with HitFit by completing the last process by setting your Account password.<br>Stay Fit ,Stay Healthy !<br><br><img src='https://ci5.googleusercontent.com/proxy/g0FbVUYqqGbbs7WgE9gjRLNB4oKGYhyP5z9TEJmvAsgwH-kTyVLLTciXahd9uNZiuXNam3LzjGoAE9Ecfc57M1cgU8ADwVeTFCfE-D9QcoDNnZ6MQKiA3Y-Pf4dc1YYe_E2zV5nd=s0-d-e1-ft#https://drive.google.com/uc?id=1QGmccVsbO__0d1aX_xg76TVN3Ky_XhQo&export=download' height=100 width=100><br><br>Regards,<br>Team HitFit.<br>";
			if(!mail($e,$subject,$body,$headers))
			{    echo 'Message could not be sent.Try again!';
			} 
			else {
			    echo 'Mail has been sent';
			 	$sql="insert into signup(email,otp) values('".$e."',".$code.")";
			 	$result = mysqli_query($conn, $sql);
			}
		}
		else{
			echo 'Mail has been sent previously, please check your inbox !';
		}
	}
	else{
		echo 'You are a Registered Customer !';
	}
}
else{
	$otp=$_POST["otp"];
	$password=$_POST["pwd"];
	$sql1="Select * from signup where otp=".$otp;
	$result1 = mysqli_query($conn, $sql1);
	if(mysqli_num_rows($result1)>0){
		$row = mysqli_fetch_assoc($result1);
			
		$sql2="Insert into login values('".$row['email']."','".md5($password)."')";
		$result2=mysqli_query($conn,$sql2);
		$flag=1;
		$sql2="delete from signup where otp=".$otp;
		$result2=mysqli_query($conn,$sql2);
	}
	else{
		$flag=0;
	}
if($flag==1){
	echo"Registered Successfully";
}
else{
	echo"Invalid OTP";

}
}
}
catch(Exception $e){
	echo 'Message: ' .$e->getMessage();

}
?>