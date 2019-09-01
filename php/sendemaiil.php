<?php
include 'databaseconnect.php';

$flag=0;
$otp=$password="";
try{

if(!isset($_POST['otp'])){

$e=$_POST["email"];

//$e="priyamvora99@gmail.com";

	
		$sql="Select otp from signup where email='".$e."'";
		$result = mysqli_query($conn, $sql);
		if (!mysqli_num_rows($result) > 0) {
			//$headers .= 'From: cc@gmail.com' . "\r\n";
			//ini_set("SMTP", "smtp.gmail.com");
			//ini_set("smtp_port", "465"); 
			//ini_set('sendmail_from', 'confectionconnection71011@gmail.com');
			$headers = 'From: HitFit' . "\r\n" .
    					'Reply-To: No-Reply' . "\r\n" ;
    		$headers .=  'MIME-Version: 1.0' . "\r\n"; 
			$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
			
			$code=mt_rand(10000,99999);
			$subject = 'OTP for password reset. ';
			$body    = 'Reset Your Password.<br><b> This is your OTP(One Time Password) to reset you password: '.$code."</b><br> <br>Stay Fit ,Stay Healthy !<br><br><img src='https://ci5.googleusercontent.com/proxy/g0FbVUYqqGbbs7WgE9gjRLNB4oKGYhyP5z9TEJmvAsgwH-kTyVLLTciXahd9uNZiuXNam3LzjGoAE9Ecfc57M1cgU8ADwVeTFCfE-D9QcoDNnZ6MQKiA3Y-Pf4dc1YYe_E2zV5nd=s0-d-e1-ft#https://drive.google.com/uc?id=1QGmccVsbO__0d1aX_xg76TVN3Ky_XhQo&export=download' height=100 width=100><br><br>Regards,<br>Team HitFit.<br>";
			
			if(mail($e,$subject,$body,$headers))
			{			
		    	echo 'Mail has been sent';
			    $sql="delete from login where email='".$e."'";
			    mysqli_query($conn, $sql);
			 	$conn->query($sql);
			 	$sql="insert into signup(email,otp) values('".$e."',".$code.")";
			 	$result = mysqli_query($conn, $sql);
			 }
			 else{
			 	error_get_last();
			 	echo"Technical Problem try after sometime !";
			 }
			
		}else{
			echo 'Mail has been sent Previously please check your inbox !';
		}
}
else{
	$otp=$_POST["otp"];
	$password=$_POST["pwd"];
	$sql1="Select * from signup where otp=".$otp;
	$result1 = mysqli_query($conn, $sql1);
	if(mysqli_num_rows($result1)>0){
		$row = mysqli_fetch_assoc($result1);
			
		$sql2="Insert into login values('".$row['email']."','".$password."')";
		$result2=mysqli_query($conn,$sql2);
		$flag=1;
		$sql2="delete from signup where otp=".$otp;
		$result2=mysqli_query($conn,$sql2);
	}
	else{
		$flag=0;
	}
if($flag==1){
	echo"Password Reset Successfully";
}
else{
	echo"Invalid OTP";

}
}
}
catch(Exception $e){
	echo 'Error:' .$mail->ErrorInfo;
}
?>