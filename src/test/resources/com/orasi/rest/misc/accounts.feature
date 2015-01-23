Feature: Account management
    As a new Microblog user
    I want to be able to register for an account
    In order to take advantage of a new web service

    Background:
        Given Larry's account has been created

    Scenario: Create an Account
        Given I am not logged in
		And Tom's account has not been created
        When I send a request to Create User Tom
		Then I expect a response matching createUserTomExample
        When I send a request to Check User Tom
		Then I expect a response matching verifyUserTomExample

    Scenario: Delete an Account
        Given I am logged in as Larry
        When I send a request to Larry Deletes Account
		Then I expect the response to succeed
        When I send a request to Check User Larry
		Then I expect a response matching verifyUserLarryDeletedExample

    Scenario: Create a Named Account
        Given I am not logged in
		And TomFields's account has not been created
		And I set the environment to:
			|   username    |   TomFields       |
			|   nickname    |   Tommy           |
			|   email       |   tom@arven.info  |
        When I send a request to Create User Variable
		Then I expect a response matching createUserVariableExample
        When I send a request to Check User Variable
		Then I want a response like verifyUserVariableExample 
		But with /username replaced by TomFields
		And with /nickname replaced by Tommy
		And with /email replaced by tom@arven.info
		And I expect it to match the new values