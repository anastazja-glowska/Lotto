package com.lotto.domain.numberreceiver;

enum ValidationInfo {
  SIX_NUMBERS_ARE_NOT_PROVIDEN("6 GIVEN NUMBERS ARE REQUIRED"),
 NUMBERS_NOT_IN_RANGE("YOU SHOULD PROVIDE NUMBERS FROM 1 TO 99"),
 SUCCESS_MESSAGE("SUCCESS");

 final String message;

 ValidationInfo(String message) {
  this.message = message;
 }
}
