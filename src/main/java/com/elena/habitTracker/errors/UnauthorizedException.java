package com.elena.habitTracker.errors;

import com.elena.habitTracker.util.ApplicationConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = ApplicationConstants.UNAUTHORIZED_ERROR_MESSAGE)
public class UnauthorizedException extends RuntimeException {
}
