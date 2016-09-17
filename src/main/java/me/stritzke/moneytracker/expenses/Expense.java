package me.stritzke.moneytracker.expenses;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class Expense extends ResourceSupport {
  @Id
  @GeneratedValue
  private long numericId;
  private BigDecimal amount;
  private String comment;
  private int year;
  private int month;
}