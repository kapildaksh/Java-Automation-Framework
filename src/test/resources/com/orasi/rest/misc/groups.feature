Feature: Groups
    As a Microblog user
    I want to be able to create and join groups
    So that I can keep up with topics I am interested in

    Scenario: Creating A Group
        Given Larry's account has been created
        And I am logged in as Larry
        When I send a request to Create Group Alpha
        Then I expect a response with code 200 OK
        When I send a request to Group Alpha Roster
        Then I expect a response matching Initial Roster