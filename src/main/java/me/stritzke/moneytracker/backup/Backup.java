package me.stritzke.moneytracker.backup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Backup {
  private final int version = 1;
  Collection<ExpenseBackupDTO> expenses;
}
