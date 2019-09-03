package net.flyingfishflash.ledger.utilities.exceptions.handlers;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.NextSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.PrevSiblingAccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(
    assignableTypes = {
      net.flyingfishflash.ledger.accounts.AccountController.class,
      net.flyingfishflash.ledger.accounts.AccountCategoryController.class,
      net.flyingfishflash.ledger.accounts.AccountTypeController.class
    })
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ConstraintViolationException.class)
  public void handleConstraintViolation(
      HttpServletResponse response, ConstraintViolationException e) throws IOException {
    logger.warn(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
    response.sendError(HttpStatus.BAD_REQUEST.value());
  }

  @ExceptionHandler(AccountNotFoundException.class)
  public void handleAccountNotFoundException(
      HttpServletResponse response, AccountNotFoundException e) throws IOException {
    logger.warn(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
    response.sendError(HttpStatus.NOT_FOUND.value());
  }

  @ExceptionHandler(AccountCreateException.class)
  public void handleAccountCreateException(HttpServletResponse response, AccountCreateException e)
      throws IOException {
    logger.error(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), e);
    response.sendError(HttpStatus.NOT_FOUND.value());
  }

  @ExceptionHandler(NextSiblingAccountNotFoundException.class)
  public void handleNextSiblingAccountNotFoundException(
      HttpServletResponse response, NextSiblingAccountNotFoundException e) throws IOException {
    logger.debug(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
    response.sendError(HttpStatus.NOT_FOUND.value());
  }

  @ExceptionHandler(PrevSiblingAccountNotFoundException.class)
  public void handlePrevSiblingAccountNotFoundException(
      HttpServletResponse response, PrevSiblingAccountNotFoundException e) throws IOException {
    logger.debug(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
    response.sendError(HttpStatus.NOT_FOUND.value());
  }
}
