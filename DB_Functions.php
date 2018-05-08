<?php

 
class DB_Functions {
 
    private $conn;
 
    
    function __construct() {
        require_once 'DB_Connect.php';
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    
    function __destruct() {
         
    }
    
    public function data($sql){
        
        $result = $this->conn->query($sql);
        if ($result->num_rows > 0) {
          return $result;
        }
       
    }
   
    
    
    public function insertAlert($type,$discription,$date,$time,$resolved)
    {
        $result=$this->conn->query("INSERT INTO security (Type,Discription,Date,Time,Resolved) VALUES('$type','$discription','$date','$time','$resolved')");
        if ($result) {
        $response["success"] = 1;
        $response["message"] = $discription;
 
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
    }
    
    public function updateAlert($date,$resolved)
    {
       
        $sql="UPDATE security SET Resolved ='".$resolved."' WHERE Date ='".$date."'";
        $result=$this->conn->query($sql);
        return $result;
    }
    
}
?>
