Feature: Kafka steps test

  Scenario: Connect to kafka
    Given I connect to kafka at '${ZOOKEEPER_HOSTS}' using path 'brokers/topics'

  Scenario: Send message to kafka topic
    Given I send a message 'hello' to the kafka topic named 'testqa'
    Then The kafka topic 'testqa' has a message containing 'hello'

  Scenario: Increase partitions in kafka topic
    Given I increase '1' partitions in a Kafka topic named 'testqa'

  Scenario: A kafka topic does not exist
    When I delete a Kafka topic named 'testqa'
    Then A kafka topic named 'testqa' does not exist
