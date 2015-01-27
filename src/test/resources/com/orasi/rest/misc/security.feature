Feature: Security
    As a Microblog user
    I want to be able to post messages or change my own account
    Without other users causing issues

    Scenario: A Non-Logged In User Can Add User
        When I send a request to Create User Tom
        Then I expect a response with code 200 OK

    Scenario: A Non-Logged In User Can't Post
        When I send a request to Tom Posts Message
        Then I expect a response with code 401 Unauthorized

    Scenario: A Non-Logged In User Can't Add Friends
        Given Larry has Tom on his list of friends
        When I send a request to Tom Adds Larry
        Then I expect a response with code 401 Unauthorized

    Scenario: A Non-Logged In User Can't Remove Friends
        Given Larry has Tom on his list of friends
        When I send a request to Larry Removes Tom
        Then I expect a response with code 401 Unauthorized

    Scenario: A Wrong User Can't Post
        Given Larry's account has been created
        And Tom's account has been created
        And I am logged in as Larry
        When I send a request to Tom Posts Message
        Then I expect a response with code 403 Forbidden

    Scenario: A Wrong User Can't Add To Friend List
        Given Larry's account has been created
        And Tom's account has been created
        And I am logged in as Larry
        When I send a request to Tom Adds Larry
        Then I expect a response with code 403 Forbidden

    Scenario: A Wrong User Can't Delete From Friend List
        Given Larry has Tom on his list of friends
        And Omega's account has been created
        And I am logged in as Omega
        When I send a request to Larry Removes Tom
        Then I expect a response with code 403 Forbidden

    Scenario: A Wrong User Can't Delete A Given User
        Given Larry's account has been created
        And Tom's account has been created
        And I am logged in as Tom
        When I send a request to Larry Deletes Account
        Then I expect a response with code 403 Forbidden

    Scenario: A Wrong User Has Read Access To Users
        Given Larry's account has been created
        Given Tom's account has been created
        And I am logged in as Larry
        When I send a request to Check User Tom
        Then I expect a response with code 200 OK

    Scenario: A Wrong User Has Read Access To Posts
        Given Larry's account has been created
        Given Tom's account has been created
        And I am logged in as Larry
        When I send a request to Check Tom's Posts
        Then I expect a response with code 200 OK