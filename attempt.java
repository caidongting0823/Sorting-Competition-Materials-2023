import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        sortBasedOnSumOfPrimeFactors(new int[]{120312, 1000, 4, 1024, 1000, 32, 123, 123012});
    }

    public static void sortBasedOnSumOfPrimeFactors(int[] array) {
        List<int[]> returnArray = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            returnArray.add(new int[]{array[i], sumOfPrimeFactors(array[i])});
        }
        returnArray.sort((a, b) -> a[1] - b[1]);
        for (int j = returnArray.size() - 1; j > 0; j--) {
            if (returnArray.get(j)[1] == returnArray.get(j - 1)[1] && returnArray.get(j - 1)[0] < returnArray.get(j)[0]) {
                int[] swappingVar = returnArray.get(j - 1);
                returnArray.set(j - 1, returnArray.get(j));
                returnArray.set(j, swappingVar);
            }
        }
        System.out.println(returnArray);
    }

    public static List<Integer> primeFactors(int num) {
        List<Integer> result = new ArrayList<>();
        for (int i = 2; i <= num; i++) {
            while (isPrime(i) && num % i == 0) {
                if (!result.contains(i)) {
                    result.add(i);
                }
                num /= i;
            }
        }
        return result;
    }

    public static boolean isPrime(int num) {
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int sumOfPrimeFactors(int num) {
        List<Integer> primeFactors = primeFactors(num);
        int sum = 0;
        for (int factor : primeFactors) {
            sum += factor;
        }
        return sum;
    }
}