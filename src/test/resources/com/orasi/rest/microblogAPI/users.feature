Feature: Account management
    As a new Microblog user
    I want to be able to register for an account
    In order to take advantage of a new web service

    Background:
        Given Larry's account has been created

    @users
    Scenario: Log In
        When I send a request to Login As Larry
        Then I expect a response matching LoginSuccess

    @users
    Scenario: Create an Account
        Given I am not logged in
        And Tom's account has not been created
        When I send a request to Create User Tom
        Then I expect a response matching createUserTomExample
        When I send a request to Login As Tom
        Then I expect a response matching LoginSuccess
        When I send a request to Check User Tom
        Then I expect a response matching verifyUserTomExample

    @users @conflict
    Scenario: Displacing Users Should Not Occur
        Given Tom's account has been created
        When I send a request to Create User Tom
        Then I expect a response with code 409 Conflict (Already Exists)

    @users
    Scenario: Delete an Account
        Given I am logged in as Larry
        When I send a request to Larry Deletes Account
        Then I expect a response with code 200 OK
        When I send a request to Check User Larry
        Then I expect a response matching verifyUserLarryDeletedExample

    @users
    Scenario Outline: Create a Named Account
        Given I am logged in as Larry
        And I define replacements:
                |   username    |   <username>    |
                |   nickname    |   <nickname>    |
                |   email       |   <email>       |
                |   password    |   <password>    |
        When I send a request to Create User Variable
        Then I expect a response matching createUserVariableExample
        And I send a request to Check User Variable
        And I replace the values in / with replacements
        And I ignore /password
        Then I expect a response matching verifyUserVariableExample
        Examples:
        These show username, nickname, and email of some new accounts to create
        with the template createUserVariableExample.
            |   username    |   nickname    |   email               |   password    |
            |   tomfields   |   TomRFields  |   tom@arven.info      |   xyzzy       |
            |   arven       |   arven       |   arven@arven.info    |   plugh       |
            |   brian       |   BrianJ      |   brianj@arven.info   |   reisub      |