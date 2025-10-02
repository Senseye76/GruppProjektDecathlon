Feature: HepAndDecCompetitorsInfo

  Scenario: Calculate decathlon score
    Given I have a discipline "100m" and a result 11.00
    When I calculate the score
    Then the score will be 861
