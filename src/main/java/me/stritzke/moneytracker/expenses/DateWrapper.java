package me.stritzke.moneytracker.expenses;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@RequiredArgsConstructor
class DateWrapper {
  private final Integer year;
  private final Integer month;

  DateWrapper() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    this.year = calendar.get(Calendar.YEAR);
    this.month = calendar.get(Calendar.MONTH) + 1;
  }

  DateWrapper getPreviousMonth() {
    if (getMonth() == 1) {
      return new DateWrapper(getYear() - 1, 12);
    } else {
      return new DateWrapper(getYear(), getMonth() - 1);
    }
  }

  DateWrapper getNextMonth() {
    if (getMonth() == 12) {
      return new DateWrapper(getYear() + 1, 1);
    } else {
      return new DateWrapper(getYear(), getMonth() + 1);
    }
  }
}
