package me.stritzke.moneytracker.expenses;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
class Expense {
  @Id
  @GeneratedValue
  private long id;
  private BigDecimal amount;
  private String comment;
  private int year;
  private int month;
}