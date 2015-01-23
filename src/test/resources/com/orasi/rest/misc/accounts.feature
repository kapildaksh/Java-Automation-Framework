Feature: Accounts

    Scenario: Create an Account
        Given I am not logged in
        And Tom's account has not been created
        When I send a request to Create User Tom
        Then I expect a response matching createUserTomExample
        When I send a request to Check User Tom
        Then I expect a response matching verifyUserTomExample

    Scenario: Create another Account
        Given I am not logged in
        And Larry's account has not been created
        When I send a request to Create User Larry
        Then I expect a response matching createUserLarryExample
        When I send a request to Check User Larry
        Then I expect a response matching verifyUserLarryExample

    Scenario: Delete an Account
        Given I am logged in as Larry
        And Larry's account has been created
        When I send a request to Larry Deletes Account
        Then I expect the response to succeed
        When I send a request to Check User Larry
        Then I expect a response matching verifyUserLarryDeletedExample