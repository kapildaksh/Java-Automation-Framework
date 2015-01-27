Feature: Messaging
    As a Microblog user
    I want to be able to post messages on my account
    In order that people know about me

    Background:
        Given Larry's account has been created
        And Tom's account has been created

    Scenario: Send a Message
        Given I am logged in as Tom
        When I send a request to Tom Posts Message
        Then I expect a response matching createPostTomExample
        When I send a request to Check Tom's Posts
        And I ignore /0/created
        Then I expect a response matching verifyPostTomExample

    Scenario: Send and Read a Single Message
        Given I am logged in as Larry
        When I send a request to Lots of Hash Tags
        Then I expect a response matching createPostLarryExample
        When I send a request to Another Way to Read a Post
        And I ignore /created
        Then I expect a response matching verifyPostLarrySingleExample