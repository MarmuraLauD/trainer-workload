Feature: Trainer Workload API Component Tests

  Scenario: Successfully update trainer workload
    Given a valid workload request for trainer "John.Doe" with action type "ADD"
    When a POST request is made to update workload at "/api/v1/workloads"
    Then the workload response status should be 200

  Scenario: Successfully get trainer workload summary
    Given a valid workload request for trainer "Jane.Doe" with action type "ADD"
    And the workload is updated via POST to "/api/v1/workloads"
    When a GET request is made to fetch workload at "/api/v1/workloads/Jane.Doe"
    Then the workload response status should be 200
    And the response should contain the correct workload summary for "Jane.Doe"

  Scenario: Fail to get workload for non-existent trainer
    When a GET request is made to fetch workload at "/api/v1/workloads/Ghost.Trainer"
    Then the workload response status should be 400
    And the response should contain a generic error message