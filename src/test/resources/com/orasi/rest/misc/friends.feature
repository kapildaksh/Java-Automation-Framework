Feature: Friends list
    As a Microblog user
    I want to add and remove friends
    In order to manage my online presence

    Background:
        Given Larry has Tom on his list of friends

    @friends
    Scenario: Add a Friend
        Given I am logged in as Tom
        When I send a request to Tom Adds Larry
        Then I expect a response matching addFriendTomLarryExample
        When I send a request to Check User Tom
        Then I expect a response matching verifyFriendTomLarryExample

    @friends
    Scenario: Delete a Friend
        Given I am logged in as Larry
        When I send a request to Larry Removes Tom
        Then I expect a response matching removeFriendLarryTomExample
        When I send a request to Check User Larry
        Then I expect a response matching verifyRemoveFriendLarryTomExample