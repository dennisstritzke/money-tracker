package me.stritzke.moneytracker.expenses;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateWrapperTest {
  @Test
  public void getPreviousMonth() {
    DateWrapper current = new DateWrapper(2016, 6);

    DateWrapper calculated = current.getPreviousMonth();

    assertThat(calculated.getYear(), is(2016));
    assertThat(calculated.getMonth(), is(5));
  }

  @Test
  public void getPreviousMonth_currentIsFirstOfYear() {
    DateWrapper current = new DateWrapper(2016, 1);

    DateWrapper calculated = current.getPreviousMonth();

    assertThat(calculated.getYear(), is(2015));
    assertThat(calculated.getMonth(), is(12));
  }

  @Test
  public void getNextMonth() {
    DateWrapper current = new DateWrapper(2016, 6);

    DateWrapper calculated = current.getNextMonth();

    assertThat(calculated.getYear(), is(2016));
    assertThat(calculated.getMonth(), is(7));
  }

  @Test
  public void getNextMonth_currentIsLastOfYear() {
    DateWrapper current = new DateWrapper(2016, 12);

    DateWrapper calculated = current.getNextMonth();

    assertThat(calculated.getYear(), is(2017));
    assertThat(calculated.getMonth(), is(1));
  }
}
