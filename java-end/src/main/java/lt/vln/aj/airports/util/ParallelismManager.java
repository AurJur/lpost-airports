package lt.vln.aj.airports.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class ParallelismManager {

    @Value("${java.util.concurrent.ForkJoinPool.common.parallelism}")
    private int parallelism;

    private final Boolean isParallel;

    private ParallelismManager() {
        this.isParallel = this.parallelism > 0;
    }

    public synchronized <T> Stream<T> getStream(List<T> list) {
        return isParallel ? list.parallelStream() : list.stream();
    }

    public synchronized <T> Stream<T> getStream(Set<T> set) {
        return isParallel ? set.parallelStream() : set.stream();
    }

    public synchronized <T> List<List<T>> splitList(List<T> list) {
        List<T> internalCopy = List.copyOf(list);
        if (isParallel) {
            List<List<T>> result = new ArrayList<>();
            int originalListSize = internalCopy.size();
            int normalSize = originalListSize / parallelism;
            int leftOver = originalListSize % parallelism;
            int lastIndex = 0;
            for (int firstIndex = 0; firstIndex < originalListSize; firstIndex = lastIndex) {
                lastIndex = firstIndex + normalSize;
                if (leftOver > 0) {
                    lastIndex = lastIndex + 1;
                    leftOver = leftOver - 1;
                }
                result.add(internalCopy.subList(firstIndex, lastIndex));
            }
            return result;
        } else {
            return List.of(internalCopy);
        }
    }

}
