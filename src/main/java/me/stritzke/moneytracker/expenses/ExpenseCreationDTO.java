package me.stritzke.moneytracker.expenses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseCreationDTO {
  private BigDecimal amount;
  private String comment;
}
