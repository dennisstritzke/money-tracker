package me.stritzke.moneytracker.expenses;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
class ExpenseCreationDTO {
  private BigDecimal amount;
  private String comment;

  void setAmount(BigDecimal amount) {
    this.amount = amount.setScale(2, RoundingMode.CEILING);
  }
}
