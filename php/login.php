<?php
 include 'databaseconnect.php';
 $email=$_POST["email"];
 $password=$_POST["password"];
 $sql="Select * from login where email='".$email."'";
 
 $result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
    // output data of each row
    $row = mysqli_fetch_assoc($result);
    	if(md5($password)==$row["password"]){
            $sql="select * from useraccount where email='".$email."'";
            $result2=mysqli_query($conn,$sql);
            if(mysqli_num_rows($result2) > 0){
    	   	   echo "WelcomeBack: ".$row["email"];
            }
            else{
                echo "Welcome: ".$row["email"];
            }
    	}else{
    		echo "Incorrect Password";
    	}
    }
   else{
   		echo "Incorrect Email Id Or User not registered, Please Register";
   }
?>