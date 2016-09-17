package me.stritzke.moneytracker.backup;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The application already contains expenses. Override by adding ?force=true")
class DataExistingWarning extends RuntimeException {
}
