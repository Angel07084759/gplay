<?php

require_once 'login.php';
$conn = new mysqli($db_hostname, $db_username, $db_password, $db_database);


$phone = $_POST["phone"];
$ftime =$_POST["ftime"];


$mysql_qry = "INSERT INTO raiteusers (phone, ftime, ltime) VALUES('$phone', '$ftime', '$ftime');";

mysqli_query($conn, $mysql_qry);

$result = mysqli_query($conn, "SELECT * FROM raiteusers WHERE phone LIKE '$phone';");

if (mysqli_num_rows($result) > 0 && !empty($_POST))
{
	while($row = $result->fetch_assoc()) 
	{
		echo $row["driver"]       .",";
		echo $row["phone"]        .",";
		echo $row["fname"]        .",";
		echo $row["lname"]        .",";
		echo $row["latitude"]     .",";
		echo $row["longitude"]    .",";
		echo $row["ftime"]        .",";	
		echo $row["ltime"];
	}
}

$result->close();
$conn->close();

?>
