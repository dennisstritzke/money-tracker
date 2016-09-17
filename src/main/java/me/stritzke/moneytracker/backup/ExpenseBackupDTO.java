package me.stritzke.moneytracker.backup;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.stritzke.moneytracker.expenses.Expense;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
class ExpenseBackupDTO {
  ExpenseBackupDTO(Expense expense) {
    this.amount = expense.getAmount();
    this.comment = expense.getComment();
    this.year = expense.getYear();
    this.month = expense.getMonth();
  }

  private BigDecimal amount;
  private String comment;
  private int year;
  private int month;
}
