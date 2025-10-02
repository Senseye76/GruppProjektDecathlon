Feature: HepAndDecCompetitorsInfo

  Scenario Outline: Calculate decathlon score
    Given I have a discipline <discipline> and a result <result>
    When I calculate the score
    Then the score will be <calculated>

    Examples:
      | discipline    | result | calculated |
      | 100m          | 11     | 861        |
      | 110m Hurdles  | 25     | 63         |
      | 400m          | 75     | 52         |
      | 1500m         | 5      | 3372       |
      | Discus Throw  | 60     | 1081       |
      | High Jump     | 82     | 13         |
      | Javelin Throw | 99     | 1339       |
      | Long Jump     | 777    | 1002       |
      | Pole Vault    | 992    | 2689       |
      | Shot Put      | 23     | 1288       |




