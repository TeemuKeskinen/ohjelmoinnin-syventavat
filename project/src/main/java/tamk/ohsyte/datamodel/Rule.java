package tamk.ohsyte.datamodel;

import java.time.MonthDay;

public interface Rule {
    MonthDay getMonthDay(int year);
}