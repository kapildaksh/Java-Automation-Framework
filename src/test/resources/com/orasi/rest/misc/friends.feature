Feature: Friends list
    As a Microblog user
    I want to add and remove friends
    In order to manage my online presence

    Background:
        Given Larry's account has been created
		And Tom's account has been created
		And Larry has Tom on his list of friends

    Scenario: Add a Friend
        When I send a request to Tom Adds Larry
		Then I expect a response matching addFriendTomLarryExample
        When I send a request to Check User Tom
		Then I expect a response matching verifyFriendTomLarryExample

    Scenario: Delete a Friend
        When I send a request to Larry Removes Tom
		Then I expect a response matching removeFriendLarryTomExample
        When I send a request to Check User Larry
		Then I expect a response matching verifyRemoveFriendLarryTomExample