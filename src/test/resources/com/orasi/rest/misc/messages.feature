Feature: Messaging
    As a Microblog user
    I want to be able to post messages on my account
    In order that people know about me

    Background:
        Given Larry's account has been created
        And Tom's account has been created

    Scenario: Send a Message
        When I send a request to Tom Posts Message
        Then I expect a response matching createPostTomExample
        When I send a request to Check Tom's Posts
        Then I want a response like verifyPostTomExample ignoring /0/:
            |   created     |

    Scenario: Read a Single Post
        When I send a request to Lots of Hash Tags
        Then I expect a response matching createPostLarryExample
        When I send a request to Another Way to Read a Post
        Then I want a response like verifyPostLarrySingleExample ignoring /:
            |   created     |