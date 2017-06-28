Feature: Backgorund aspect tests

  Background:
  @background(WAIT)
    Given I run '[ "SHOULD_RUN_WAIT" = "SHOULD_RUN_WAIT" ]' locally
  @/background
  @background(WAIT_NO)
    Given I run '[ "SHOULD_RUN" = "FAIL_RUN" ]' locally
  @/background
    Given I run '[ "SHOULD_RUN" = "SHOULD_RUN" ]' locally


  Scenario: default scenario for background1
    Given I run '[ "SHOULD_RUN" = "SHOULD_RUN" ]' locally
