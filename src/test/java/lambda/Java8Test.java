package lambda;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.Test;

public class Java8Test {

    @Test
    public void test01() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, null);
        map.computeIfAbsent(4, k -> String.valueOf(k));
        map.putIfAbsent(5, String.valueOf(5));
        map.forEach((k, v) -> System.out.println(k + " = " + v));
        
        System.err.println(Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6)).flatMap(t -> t.parallelStream()).noneMatch(e -> e < -1));
        
        
        
    }

}
