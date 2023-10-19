




function sortBasedOnSumOfPrimeFactors(array){
    let returnArray = [];

    for(let i = 0; i < array.length; i++){
        returnArray.push([array[i], (((prime_factors(array[i])).reduce((sum, num) => sum + num, 0)))]);
        
        
    }  

  

    returnArray.sort((a, b) => {
        return a[1] - b[1];
      });

     
      

      for(let j = returnArray.length - 1; j > 0; j--){
         if(returnArray[j][1] === returnArray[j - 1][1] & (returnArray[j-1][0] < returnArray[j][0])){
           // let swappingVar = returnArray[j - 1][1];
           // returnArray[j - 1][1] = returnArray[j][1];
           // returnArray[j] = swappingVar;
            returnArray[j] = returnArray.splice([j-1], 1, returnArray[0][0])
            

 
         }
             
     }
    console.log(returnArray)
    
}




function prime_factors(num) {
    function is_prime(num) {
      for (let i = 2; i <= Math.sqrt(num); i++)
      {
        if (num % i === 0) return false;
      }
      return true;
    }
    const result = [];
    for (let i = 2; i <= num; i++)
    {
      while (is_prime(i) && num % i === 0) 
      {
        if (!result.includes(i)) result.push(i);
        num /= i;
      }
    }
    return result;
  }





  let start = Date.now();
(sortBasedOnSumOfPrimeFactors([120312, 1000, 4, 1024, 1000, 32, 123, 123012]));
  let timeTaken = Date.now() - start;
  console.log("Total time taken : " + timeTaken + " milliseconds");




