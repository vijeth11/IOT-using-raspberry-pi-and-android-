<?php
require_once 'security/DB_Functions.php';
$db = new DB_Functions();

if(isset($_GET["on"]))
  {
      $myfile = fopen("newfile.txt", "w") or die("Unable to open file!");
      fwrite($myfile, $_GET["on"]);
  }
 else if(isset($_GET["type"])&&isset($_GET["discription"])&&isset($_GET["date"])&&isset($_GET["time"])&& isset($_GET["resolved"]))
 {
     $db->insertAlert($_GET["type"],$_GET["discription"],$_GET["date"],$_GET["time"],$_GET["resolved"]);
 }
 else if(isset($_GET["date"])&& isset($_GET["resolved"]))
 {
     $result=$db->updateAlert($_GET["date"],$_GET["resolved"]);
     if($result)
     {
         $data->error="FALSE";
         $data->message="the problem has been solved";
     }
     else
     {
         $data->error="TRUE";
         $data->message="the problem still persist please hurry up";
     }
     echo json_encode($data);
 }
 else if(isset($_GET["device"]))
 {
     if($_GET["device"]=="android")
     {
     $result = $db->data("select * from security where Resolved='no'");
if($result->num_rows>0)
{
   while($row = $result->fetch_assoc())
    {
        $data["alerts"][]=$row;
    }
}
else
{
    $data->error="TRUE";
    $data->message="the data is not present";
}
     echo json_encode($data);
}
}
else
{
     $myfile = fopen("newfile.txt", "r") or die("Unable to open file!");
     $data["on"]=fread($myfile,filesize("newfile.txt"));
     echo json_encode($data);
 }
?>
