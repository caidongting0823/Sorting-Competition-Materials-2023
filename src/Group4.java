import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// To run on a single core, compile and then run as:
// taskset -c 0 java GroupN
// To avoid file reading/writing connections to the server, run in /tmp 
// of your lab machine.

public class Group4 {

    public static void main(String[] args) throws InterruptedException {
        String fileName = args[0];
        String outFileName = args[1];
        String[] originalData = readData(fileName);
        String[] toSort;

        // JVM warmup
        toSort = originalData.clone();
        sort(toSort);

        // Pause for 10ms
        Thread.sleep(10);

        // Timing the sorting process
        toSort = originalData.clone();
        long startTime = System.currentTimeMillis();
        String[] sorted = sort(toSort);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        writeOutResult(sorted, outFileName);  // write out the results
    }


	public static String[] readData(String file) {
		List<String> dataList = new ArrayList<>();
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			String line;
			while ((line = input.readLine()) != null) {
				dataList.add(line.trim());
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(99);
		}
		// Convert the list to an array and return
		return dataList.toArray(new String[0]);
	}

    public static void writeOutResult(String[] sorted, String file) {
        try {
            PrintWriter out = new PrintWriter(file);
            // Calculate the maximum number of digits
            int maxDigits = Arrays.stream(sorted)
                                  .mapToInt(String::length)
                                  .max()
                                  .orElse(0);
            for (int i = 0; i < sorted.length; i++) {
                out.println(String.format("%0" + maxDigits + "d", Integer.parseInt(sorted[i])));
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(98);
        }
    }    


    // Your sorting algorithm goes here.
    // You may call other methods and use other classes.
    // You may ALSO add methods to the Data Class (or any other class) BUT...
    // DO NOT REMOVE OR CHANGE THIS METHOD'S SIGNATURE OR ITS TYPE

    public static String[] sort(String[] data) {
        int maxNumber = Arrays.stream(data).mapToInt(Integer::parseInt).max().orElse(0);
        List<Integer> primes = precomputePrimes(maxNumber);
    
        int[] numbers = Arrays.stream(data).mapToInt(Integer::parseInt).toArray();
        Pair[] pairs = new Pair[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            pairs[i] = new Pair(numbers[i], getSumPrimeFactors(numbers[i], primes));
        }
    
        // Updated sorting criteria: Sort in descending order if prime sums are equal
        Arrays.sort(pairs, (a, b) -> {
            if (a.sum == b.sum) {
                return b.number - a.number;
            }
            return a.sum - b.sum;
        });
    
        String[] sorted = new String[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            sorted[i] = String.valueOf(pairs[i].number);
        }
        return sorted;
    }    


    public static List<Integer> precomputePrimes(int limit) {
        boolean[] isPrime = new boolean[limit + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;
    
        for (int i = 2; i <= Math.sqrt(limit); i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    isPrime[j] = false;
                }
            }
        }
    
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }
        return primes;
    }
    

    public static List<Integer> sieveOfEratosthenes(int max) {
        boolean[] isPrime = new boolean[max + 1];
        for (int i = 2; i <= max; i++) {
            isPrime[i] = true;
        }

        int num = 2;
        while (true) {
            for (int i = 2; num * i <= max; i++) {
                isPrime[num * i] = false;
            }

            boolean nextPrimeFound = false;
            for (int i = num + 1; i <= max; i++) {
                if (isPrime[i]) {
                    num = i;
                    nextPrimeFound = true;
                    break;
                }
            }

            if (!nextPrimeFound) {
                break;
            }
        }

        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= max; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        return primes;
    }


    public static int getSumPrimeFactors(int number, List<Integer> primes) {
        int sum = 0;
        for (int prime : primes) {
            if (prime > Math.sqrt(number)) {
                break;
            }
            if (number % prime == 0) {
                sum += prime;  // Adding the prime factor to the sum
                while (number % prime == 0) {
                    number /= prime;  // Reducing the number to its next prime factor
                }
            }
        }
        if (number > 1) {  // If the remaining number itself is prime
            sum += number;
        }
        return sum;
    }    
    

    static class Pair {
        int number;
        int sum;

        Pair(int number, int sum) {
            this.number = number;
            this.sum = sum;
        }
    }
}
