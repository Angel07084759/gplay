<?php

require_once 'login.php';
$conn = new mysqli($db_hostname, $db_username, $db_password, $db_database);


$phone = $_POST["phone"];
$ftime =$_POST["ftime"];


$result = mysqli_query($conn, "SELECT * FROM raiteusers WHERE phone LIKE '$phone';");

//mysqli_query($conn, "INSERT INTO raiteusers (times) VALUES('$ftime');");

//$row = mysqli_fetch_row($result);
if (mysqli_num_rows($result) > 0 && !empty($_POST))//isset($_POST["post"]))
{
        while($row = $result->fetch_assoc()) 
        {
           echo $row["verified"].",".$row["driver"].",".$row["phone"].",".$row["fname"].",".$row["lname"].",".$row["ftime"];
        }
}
else
{
	echo "FAILED";
}
	
$result->close();
$conn->close();
	

?>
