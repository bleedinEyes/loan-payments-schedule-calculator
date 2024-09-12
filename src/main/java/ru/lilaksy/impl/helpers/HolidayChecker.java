package ru.lilaksy.impl.helpers;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HolidayChecker {
    private final static Map<Month, Set<Integer>> HOLIDAYS = new HashMap<>() {{
        put(Month.JANUARY, Set.of(1, 2, 3, 4, 5, 6, 7, 8));
        put(Month.FEBRUARY, Set.of(23));
        put(Month.MARCH, Set.of(8));
        put(Month.MAY, Set.of(1, 9));
        put(Month.JUNE, Set.of(12));
        put(Month.NOVEMBER, Set.of(4));

    }};
    public boolean isHoliday(LocalDate paymentDate) {
        return HOLIDAYS.getOrDefault(paymentDate.getMonth(), Collections.emptySet())
                .contains(paymentDate.getDayOfMonth());
    }
}
