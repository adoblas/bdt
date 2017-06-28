Feature: Backgorund aspect tests 3

  Background:
  @background(default)
    Given I run '[ "SHOULD_RUN" = "SHOULD_RUN" ]' locally
  @background(WAIT_NO)
    Given I run '[ "SHOULD_RUN" = "FAIL_RUN" ]' locally
  @background(default)
    Given I run '[ "SHOULD_RUN" = "SHOULD_RUN" ]' locally


  Scenario: default scenario for background3
    Given I run '[ "SHOULD_RUN" = "SHOULD_RUN" ]' locally
