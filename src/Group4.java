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
            for (int i = 0; i < sorted.length; i++) {
                out.println(sorted[i]);
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

    public static String[] sort(String[] toSort) {
        List<Integer> primes = sieveOfEratosthenes(1000000); // Assume a large enough limit for now
        Pair[] pairs = new Pair[toSort.length];
        for (int i = 0; i < toSort.length; i++) {
            int num = Integer.parseInt(toSort[i]);
            int sum = getSumPrimeFactors(num, primes);
            pairs[i] = new Pair(num, sum);
        }

        Arrays.sort(pairs, (p1, p2) -> {
            if (p1.sum != p2.sum) {
                return p1.sum - p2.sum;
            }
            return p2.number - p1.number; 
        });

        for (int i = 0; i < pairs.length; i++) {
            toSort[i] = String.format("%04d", pairs[i].number);
        }

        return toSort;
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


    public static int getSumPrimeFactors(int n, List<Integer> primes) {
        int sum = 0;
        for (int i = 0; i < primes.size() && primes.get(i) <= n; i++) {
            if (n % primes.get(i) == 0) {
                sum += primes.get(i);
                while (n % primes.get(i) == 0) {
                    n /= primes.get(i);
                }
            }
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
