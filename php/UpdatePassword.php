<?php
include 'datababse.php';

			$otp=$_POST["otp"];
			$password=$_POST["password"];
			$updatedPassword=$_POST["updated_password"];
			$email = $_POST["email"];
			
				//Verify OTP
				
				$sql = "SELECT otp from register where email = '$email'";
				$res=$conn->query($sql);
				if($res->num_rows>0) {
						if($otp === $res['otp']) {
							
							//Update Pasdword
							$sql = "UPDATE table login SET Password = '$updatedPassword' where email = '$email'";
							$res=$conn->query($sql);
							
						}
				}		
				else
				{
					echo 'Error while inserting in table';
				}
			
			}
?>