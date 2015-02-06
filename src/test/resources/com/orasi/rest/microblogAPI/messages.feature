Feature: Messaging
    As a Microblog user
    I want to be able to post messages on my account
    In order that people know about me

    Background:
        Given Larry's account has been created
        And Tom's account has been created

    @messages
    Scenario: Send a Message
        Given I am logged in as Tom
        When I send a request to Tom Posts Message
        Then I expect a response matching createPostTomExample
        When I send a request to Check Tom's Posts
        And I ignore /0/created
        Then I expect a response matching verifyPostTomExample

    @messages
    Scenario: Send A Message Directly
        Given I am logged in as Tom
        When I send the POST request to http://localhost:4567/users/tom/posts:
            """
            { "text": "This is just a quick test." }
            """
        Then I expect a response with code 200 OK
        When I send a request to Check Tom's Posts
        And I ignore /0/created
        Then I expect a response matching:
            """
            [{ "id": 0, "text": "This is just a quick test." }]
            """

    @messages
    Scenario: Send and Read a Single Message
        Given I am logged in as Larry
        When I send a request to Lots of Hash Tags
        Then I expect a response matching createPostLarryExample
        When I send a request to Another Way to Read a Post
        And I ignore /created
        Then I expect a response matching verifyPostLarrySingleExample