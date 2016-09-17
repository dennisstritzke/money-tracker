package me.stritzke.moneytracker.backup;

import lombok.RequiredArgsConstructor;
import me.stritzke.moneytracker.expenses.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BackupService {
  private final ExpenseService expenseService;

  Backup create() {
    Collection<ExpenseBackupDTO> expenseBackupDTOs = expenseService.findAll().stream()
            .map(ExpenseBackupDTO::new)
            .collect(Collectors.toList());
    return new Backup(expenseBackupDTOs);
  }

  void restore(Backup backup, boolean ignoreDataExistence) throws DataExistingWarning {
    if (applicationContainsData() && !ignoreDataExistence) {
      throw new DataExistingWarning();
    }

    backup.getExpenses().forEach(expense -> {
      expenseService.save(expense.getAmount(), expense.getComment(), expense.getYear(), expense.getMonth());
    });

  }

  private boolean applicationContainsData() {
    return expenseService.findAll().size() > 0;
  }
}
