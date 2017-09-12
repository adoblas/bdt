[![Coverage Status](https://coveralls.io/repos/github/Stratio/bdt/badge.svg?branch=master)](https://coveralls.io/github/Stratio/bdt?branch=master)

Stratio bdt
===========

Stratio Acceptance Test library

 * Cucumber for test definition 
 * TestNG for execution
 * AsyncHTTP Client
 * Selenium 
 * JSONPath
 * AssertJ

Testing runtime to rule over Stratio's acceptance tests


[Javadoc always live](http://stratiodocs.s3.amazonaws.com/bdt/0.4.0-SNAPSHOT/index.html)


# README

## ACCEPTANCE TESTS

Stratio BDT Hands-on-labs project.

Get hands-on with Stratio BDT library. Practice with the latest step definition and implement your own scenarios.

C'mon!! Improve your QA skills for automated testing.


## EXECUTION

These tests are suposed to be executed as part of the continuous integration flow as follows:

` mvn verify [-D\<ENV_VAR>=\<VALUE>] [-Dit.test=\<TEST_TO_EXECUTE>|-Dgroups=\<GROUP_TO_EXECUTE>] `

Examples:

_**single class execution**_

` mvn verify -DSECS=5 -Dit.test=com.stratio.qa.hol.testsAT.ITsample `

_**group execution**_

` mvn verify -DSECS=5 -Dgroups=hol `

## ASPECTS

As part of BDT implementation, there are a couple of AspectJ aspects which may be useful for your scenarios:

- **RunOnTagAspect**:

` @runOnEnv(METRIC_HOST) `

` @skipOnEnv(METRIC_HOST) `

An AspectJ aspect could handle cucumber tags so that the annotated scenario will be overseen if no environment variable METRIC_HOST exists and has value (so that no traces of it execution show up)

_Example @runOnEnv:_

` 
    @runOnEnv(SECS)
	Scenario: Dummy scenario
      And I wait '${SECS}' seconds
` 

This scenario will ONLY be executed if environment vairable SECS is defined.


_Example @skipOnEnv:_

` 
    @skipOnEnv(SECS_2)
	Scenario: Dummy scenario
      And I wait '${SECS}' seconds
` 

This scenario will be omitted if environment vairable SECS_2 is defined.

- **IgnoreTagAspect**

An AspectJ aspect that allows the skipping of an scenario or a feature. To do so a tag must be used before the scenario or the feature. Additionally an ignored reason can be set.

` @ignore @manual ` 

` @ignore @unimplemented ` 

` @ignore @toocomplex ` 

` @ignore @tillfixed(DCS-XXX) ` 

This last ignored reason is associated to a ticket in Jira. After executing the test class the ticket link is shown as execution result.

- **IncludeTagAspect**

An AspectJ aspect that includes an scenario before the taged one. It manages parameters as well.

` @include(feature:<feature>,scenario:<scenario>)` 

` @include(feature:<feature>,scenario:<scenario>,params:<params>)` 


_Examples:_

` 
    @include(feature:sample.feature,scenario:Not so dummy scenario)
    Scenario: Dummy scenario
          And I wait '${SECS}' seconds
` 

` 
    @include(feature:sample.feature,scenario:Not so dummy scenario,params:param1=1)
    Scenario: Dummy scenario
          And I wait '${SECS}' seconds
` 
