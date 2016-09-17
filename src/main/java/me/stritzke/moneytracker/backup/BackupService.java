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
}
