package org.example.utils;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class Utils {
    Long max = Long.MAX_VALUE;

    public <T> Long getUniqueId(HashMap<Long, T> list, Long hash) {
        long finalHash;

        if (hash == 0) {
            Random random = new Random();
            int randomNumber = random.nextInt(11);
            int moreRandomNumber = random.nextInt(11);
            finalHash = (long) Math.pow(randomNumber, moreRandomNumber);
        } else {
            finalHash = hash;
        }
        List<Long> isRepeat = list.keySet().stream().filter(i -> Objects.equals(i, finalHash)).toList();

        if (isRepeat.isEmpty()) {
            return finalHash;
        } else {

            if (Objects.equals(finalHash, max)) {
                return getUniqueId(list, finalHash / list.size());
            } else {
                return getUniqueId(list, finalHash + 1);
            }
        }
    }

    public <T, U> List<U> getListDTO(List<T> list, Function<T, U> mapper) {
        List<U> dtos = new ArrayList<>();
        for (T item : list) {
            dtos.add(mapper.apply(item));
        }
        return dtos;
    }
}
