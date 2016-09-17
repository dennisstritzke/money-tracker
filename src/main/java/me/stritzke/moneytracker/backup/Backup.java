package me.stritzke.moneytracker.backup;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
class Backup {
  private final int version = 1;
  Collection<ExpenseBackupDTO> expenses;
}
